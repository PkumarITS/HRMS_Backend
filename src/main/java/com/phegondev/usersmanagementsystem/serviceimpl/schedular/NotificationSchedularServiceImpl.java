package com.phegondev.usersmanagementsystem.serviceimpl.schedular;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.phegondev.usersmanagementsystem.entity.OurUsers;
import com.phegondev.usersmanagementsystem.entity.Timesheet;
import com.phegondev.usersmanagementsystem.entity.notificationconfig.ApprovalReminder;
import com.phegondev.usersmanagementsystem.entity.notificationconfig.EmployeeReminder;
import com.phegondev.usersmanagementsystem.entity.notificationconfig.EscalationReminder;
import com.phegondev.usersmanagementsystem.entity.notificationconfig.HrReminder;
import com.phegondev.usersmanagementsystem.entity.notificationconfig.SupervisorReminder;
import com.phegondev.usersmanagementsystem.enumuration.ReminderLevel;
import com.phegondev.usersmanagementsystem.repository.TimesheetRepo;
import com.phegondev.usersmanagementsystem.repository.UsersRepo;
import com.phegondev.usersmanagementsystem.repository.notificationconfig.ApprovalReminderRepo;
import com.phegondev.usersmanagementsystem.repository.notificationconfig.EmployeeReminderRepo;
import com.phegondev.usersmanagementsystem.repository.notificationconfig.EscalationReminderRepo;
import com.phegondev.usersmanagementsystem.repository.notificationconfig.HrReminderRepo;
import com.phegondev.usersmanagementsystem.repository.notificationconfig.SupervisorReminderRepo;
import com.phegondev.usersmanagementsystem.service.MailService;
import com.phegondev.usersmanagementsystem.service.schedular.NotificationSchedularService;

@Service
public class NotificationSchedularServiceImpl implements NotificationSchedularService{
	 
	    private final TimesheetRepo timesheetRepo;
	    private final UsersRepo usersRepo;
	    private final MailService mailService;

	    private final ApprovalReminderRepo approvalReminderRepo;
	    private final EmployeeReminderRepo employeeReminderRepo;
	    private final EscalationReminderRepo escalationReminderRepo;
	    private final HrReminderRepo hrReminderRepo;
	    private final SupervisorReminderRepo supervisorReminderRepo;

	   
	    public NotificationSchedularServiceImpl(TimesheetRepo timesheetRepo,
	                                            UsersRepo usersRepo,
	                                            MailService mailService,
	                                            ApprovalReminderRepo approvalReminderRepo,
	                                            EmployeeReminderRepo employeeReminderRepo,
	                                            EscalationReminderRepo escalationReminderRepo,
	                                            HrReminderRepo hrReminderRepo,
	                                            SupervisorReminderRepo supervisorReminderRepo) {
	        this.timesheetRepo = timesheetRepo;
	        this.usersRepo = usersRepo;
	        this.mailService = mailService;
	        this.approvalReminderRepo = approvalReminderRepo;
	        this.employeeReminderRepo = employeeReminderRepo;
	        this.escalationReminderRepo = escalationReminderRepo;
	        this.hrReminderRepo = hrReminderRepo;
	        this.supervisorReminderRepo = supervisorReminderRepo;
	    }
	

	@Override
	public void runEmployeeReminder(EmployeeReminder reminder) {


	      System.out.println("=== Reminder Scheduler Triggered ===");
	      
	      LocalDate today = LocalDate.now();
	      LocalDate currentWeekStart = today.with(DayOfWeek.MONDAY);
	      System.out.println("Today's date: " + today);
	      System.out.println("Current week start (Monday): " + currentWeekStart);

	      List<Timesheet> draftTimesheets = timesheetRepo.findByStatusAndWeekStart("DRAFT", currentWeekStart);
	      System.out.println("Total draft timesheets found: " + draftTimesheets.size());

	      String subject = getReminderSubject(reminder.getLevel());

	      for (Timesheet ts : draftTimesheets) {
	          System.out.println("Processing Timesheet ID: " + ts.getTimesheetId());

	          try {
	              OurUsers employee = usersRepo.findByEmpId(ts.getEmpId());
	              System.out.println("Employee found: " + employee.getName() + " (Email: " + employee.getEmail() + ")");

	              String toEmail = employee.getEmail();
	              String emailBody =
	                  "Dear " + employee.getName() + ",\n\n" +
	                  "This is a gentle reminder that your timesheet (ID: " + ts.getTimesheetId() + ") submission is still pending.\n\n" +
	                  "Kindly ensure it is completed at the earliest to avoid any delays in processing.\n\n" +
	                  "Regards,\n" +
	                  "Timesheet Management Team";

	              System.out.println("Sending email to: " + toEmail);
	              mailService.sendEmail(toEmail, subject, emailBody);
	              System.out.println("✅ Email sent successfully to: " + toEmail);

	          } catch (Exception e) {
	              System.err.println("❌ Failed to send email for Timesheet ID: " + ts.getTimesheetId());
	              e.printStackTrace();
	          }
	      }

	      System.out.println("=== Reminder Scheduler Completed ===");
	  
		
	}

	@Override
	public void runApprovalReminder(ApprovalReminder reminder) {
	    try {
	        LocalDate today = LocalDate.now();
	        LocalDate currentWeekStart = today.with(DayOfWeek.MONDAY);

	        // Fetch timesheets with status SUBMITTED (pending approval)
	        List<Timesheet> submittedTimesheets = timesheetRepo.findByStatusAndWeekStart("SUBMITTED", currentWeekStart);
	        System.out.println("Total submitted timesheets awaiting approval: " + submittedTimesheets.size());

	        if (submittedTimesheets.isEmpty()) {
	            System.out.println("No timesheets pending approval. No reminder emails sent.");
	            return;
	        }

	        String subject = getReminderSubjectForApproval(reminder.getLevel());

	        StringBuilder body = new StringBuilder();
	        body.append("Dear Approver,\n\n");
	        body.append("The following timesheets are pending your approval for the week starting ")
	            .append(currentWeekStart).append(":\n\n");

	        boolean hasPendingApprovals = false;

	        for (Timesheet ts : submittedTimesheets) {
	            try {
	                OurUsers employee = usersRepo.findByEmpId(ts.getEmpId());
	                if (employee == null) {
	                    System.err.println("No employee found with empId: " + ts.getEmpId());
	                    continue;
	                }

	                body.append(" - ").append(employee.getName())
	                    .append(" (Timesheet ID: ").append(ts.getTimesheetId()).append(")\n");
	                hasPendingApprovals = true;

	            } catch (Exception e) {
	                System.err.println("Error processing timesheet ID " + ts.getTimesheetId() + ": " + e.getMessage());
	                e.printStackTrace();
	            }
	        }

	        if (hasPendingApprovals) {
	            body.append("\nKindly review and approve them at your earliest convenience.\n\n")
	                .append("Regards,\nTimesheet Management System");

	            for (String recipient : reminder.getRecipients()) {
	                mailService.sendEmail(recipient, subject, body.toString());
	            }

	            // Update lastExecutedAt
	            reminder.setLastExecutedAt(LocalDateTime.now());
	            approvalReminderRepo.save(reminder);

	            System.out.println("Approval reminder emails sent successfully.");
	        }

	    } catch (Exception e) {
	        System.err.println("Exception during approval reminder execution: " + e.getMessage());
	        e.printStackTrace();
	    }
	}


	@Override
	public void runSupervisorReminder(SupervisorReminder reminder) {
	    LocalDate today = LocalDate.now();

	    try {
	        LocalDate currentWeekStart = today.with(DayOfWeek.MONDAY);

	        List<Timesheet> draftTimesheets = timesheetRepo.findByStatusAndWeekStart("DRAFT", currentWeekStart);
	        System.out.println("Total draft timesheets found: " + draftTimesheets.size());

	        if (draftTimesheets.isEmpty()) {
	            System.out.println("No unsubmitted timesheets found. No reminder emails sent.");
	            return;
	        }

	        String subject = getReminderSubject(reminder.getLevel());

	        StringBuilder body = new StringBuilder();
	        body.append("Dear Supervisor Team,\n\n");
	        body.append("The following employees have not submitted their timesheets for the week starting ")
	            .append(currentWeekStart).append(":\n\n");

	        boolean hasUnsubmitted = false;

	        for (Timesheet ts : draftTimesheets) {
	            try {
	                OurUsers employee = usersRepo.findByEmpId(ts.getEmpId());
	                if (employee == null) {
	                    System.err.println("No employee found with empId: " + ts.getEmpId());
	                    continue;
	                }

	                body.append(" - ").append(employee.getName())
	                    .append(" (Timesheet ID: ").append(ts.getTimesheetId()).append(")\n");
	                hasUnsubmitted = true;

	            } catch (Exception e) {
	                System.err.println("Error processing timesheet ID " + ts.getTimesheetId() + ": " + e.getMessage());
	                e.printStackTrace();
	            }
	        }

	        if (hasUnsubmitted) {
	            body.append("\nKindly follow up with them to ensure timely submission.\n\n")
	                .append("Regards,\nTimesheet Management System");

	            for (String recipient : reminder.getRecipients()) {
	                mailService.sendEmail(recipient, subject, body.toString());
	            }

	            // Update lastExecutedAt
	            reminder.setLastExecutedAt(LocalDateTime.now());
	            supervisorReminderRepo.save(reminder);

	            System.out.println("Reminder email sent to supervisor recipients.");
	        }

	    } catch (Exception e) {
	        System.err.println("Exception during Supervisor reminder execution: " + e.getMessage());
	        e.printStackTrace();
	    }
	}


	@Override	
	public void runHrReminder(HrReminder reminder) {
	
	    LocalDate today = LocalDate.now();	 

	    try {
	        LocalDate currentWeekStart = today.with(DayOfWeek.MONDAY);

	        List<Timesheet> draftTimesheets = timesheetRepo.findByStatusAndWeekStart("DRAFT", currentWeekStart);
	        System.out.println("Total draft timesheets found: " + draftTimesheets.size());

	        if (draftTimesheets.isEmpty()) {
	            System.out.println("No unsubmitted timesheets found. No reminder emails sent.");
	            return;
	        }

	        String subject = getReminderSubject(reminder.getLevel());

	        StringBuilder body = new StringBuilder();
	        body.append("Dear HR Team,\n\n");
	        body.append("The following employees have not submitted their timesheets for the week starting ")
	            .append(currentWeekStart).append(":\n\n");

	        boolean hasUnsubmitted = false;

	        for (Timesheet ts : draftTimesheets) {
	            try {
	                OurUsers employee = usersRepo.findByEmpId(ts.getEmpId());
	                if (employee == null) {
	                    System.err.println("No employee found with empId: " + ts.getEmpId());
	                    continue;
	                }

	                body.append(" - ").append(employee.getName())
	                    .append(" (Timesheet ID: ").append(ts.getTimesheetId()).append(")\n");
	                hasUnsubmitted = true;

	            } catch (Exception e) {
	                System.err.println("Error processing timesheet ID " + ts.getTimesheetId() + ": " + e.getMessage());
	                e.printStackTrace();
	            }
	        }

	        if (hasUnsubmitted) {
	            body.append("\nKindly follow up with them to ensure timely submission.\n\n")
	                .append("Regards,\nTimesheet Management System");

	            for (String recipient : reminder.getRecipients()) {
	                mailService.sendEmail(recipient, subject, body.toString());
	            }

	            // Update lastExecutedAt
	            reminder.setLastExecutedAt(LocalDateTime.now());
	            hrReminderRepo.save(reminder);

	            System.out.println("Reminder email sent to HR recipients.");
	        }

	    } catch (Exception e) {
	        System.err.println("Exception during HR reminder execution: " + e.getMessage());
	        e.printStackTrace();
	    }
	}


	@Override	
	public void runEscalationReminder(EscalationReminder reminder) {

	    LocalDate today = LocalDate.now();

	    try {
	        LocalDate currentWeekStart = today.with(DayOfWeek.MONDAY);
	        List<Timesheet> draftTimesheets = timesheetRepo.findByStatusAndWeekStart("DRAFT", currentWeekStart);
	        System.out.println("Total draft timesheets found: " + draftTimesheets.size());

	        if (draftTimesheets.isEmpty()) {
	            System.out.println("No unsubmitted timesheets found. No escalation emails sent.");
	            return;
	        }

	        String subject = "Escalation - Timesheet Still Not Submitted";

	        StringBuilder body = new StringBuilder();
	        body.append("Dear Escalation Team,\n\n");
	        body.append("The following employees have *still* not submitted their timesheets for the week starting ")
	            .append(currentWeekStart).append(":\n\n");

	        boolean hasUnsubmitted = false;

	        for (Timesheet ts : draftTimesheets) {
	            try {
	                OurUsers employee = usersRepo.findByEmpId(ts.getEmpId());
	                if (employee == null) {
	                    System.err.println("No employee found with empId: " + ts.getEmpId());
	                    continue;
	                }

	                body.append(" - ").append(employee.getName())
	                    .append(" (Timesheet ID: ").append(ts.getTimesheetId()).append(")\n");
	                hasUnsubmitted = true;

	            } catch (Exception e) {
	                System.err.println("Error processing timesheet ID " + ts.getTimesheetId() + ": " + e.getMessage());
	                e.printStackTrace();
	            }
	        }

	        if (hasUnsubmitted) {
	            body.append("\nImmediate action may be required.\n\n")
	                .append("Regards,\nTimesheet Management System");

	            for (String recipient : reminder.getRecipients()) {
	                mailService.sendEmail(recipient, subject, body.toString());
	            }

	            reminder.setLastExecutedAt(LocalDateTime.now());
	            escalationReminderRepo.save(reminder);

	            System.out.println("Escalation reminder email sent to recipients.");
	        }

	    } catch (Exception e) {
	        System.err.println("Exception during escalation reminder execution: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	
	private String getReminderSubject(ReminderLevel level) {
	    switch (level) {
	        case LEVEL_1:
	            return "1st Reminder - Timesheet Submission Pending";
	        case LEVEL_2:
	            return "2nd Reminder - Timesheet Submission Still Pending";
	        default:
	            return "Reminder - Timesheet Submission Pending";
	    }
	}
	
	private String getReminderSubjectForApproval(ReminderLevel level) {
		
	    switch (level) {
        case LEVEL_1:
            return "1st Reminder - Timesheet Approval Pending";
        case LEVEL_2:
            return "2nd Reminder - Timesheet Approval Still Pending";
        case LEVEL_3:
            return "3rd Reminder - Timesheet Approval Still Pending";
        default:
            return "Reminder - Timesheet Approval Pending";
    
	    }
	}



}
