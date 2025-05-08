package com.phegondev.usersmanagementsystem.serviceimpl.useraccess;



import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phegondev.usersmanagementsystem.dto.useraccess.MapUserRequestDto;
import com.phegondev.usersmanagementsystem.dto.useraccess.UserActionDTO;
import com.phegondev.usersmanagementsystem.entity.Contact;
import com.phegondev.usersmanagementsystem.entity.Employee;
import com.phegondev.usersmanagementsystem.entity.Identification;
import com.phegondev.usersmanagementsystem.entity.OurUsers;
import com.phegondev.usersmanagementsystem.entity.Report;
import com.phegondev.usersmanagementsystem.entity.Work;
import com.phegondev.usersmanagementsystem.entity.personal;
import com.phegondev.usersmanagementsystem.entity.useraccess.Action;
import com.phegondev.usersmanagementsystem.entity.useraccess.Role;
import com.phegondev.usersmanagementsystem.entity.useraccess.UserActionMapping;
import com.phegondev.usersmanagementsystem.exceptions.useraccess.UserNotFoundException;
import com.phegondev.usersmanagementsystem.repository.EmployeeRepository;
import com.phegondev.usersmanagementsystem.repository.UsersRepo;
import com.phegondev.usersmanagementsystem.repository.useraccess.ActionRepo;
import com.phegondev.usersmanagementsystem.repository.useraccess.RoleRepo;
import com.phegondev.usersmanagementsystem.repository.useraccess.UserActionMappingRepository;
import com.phegondev.usersmanagementsystem.service.useraccess.UserService;



@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
	
	private final UsersRepo usersRepo;
	private final ActionRepo actionRepo;
	private final RoleRepo roleRepo;
	private final UserActionMappingRepository userActionMappingRepository;
	private final EmployeeRepository employeeRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UsersRepo usersRepo,
	                       ActionRepo actionRepo,
	                       RoleRepo roleRepo,
	                       UserActionMappingRepository userActionMappingRepository,
	                       EmployeeRepository employeeRepository,
	                       PasswordEncoder passwordEncoder) {
	    this.usersRepo = usersRepo;
	    this.actionRepo = actionRepo;
	    this.roleRepo = roleRepo;
	    this.userActionMappingRepository = userActionMappingRepository;
	    this.employeeRepository = employeeRepository;
	    this.passwordEncoder = passwordEncoder;
	}


	@Override
	public List<UserActionDTO> getUserActions(Integer id) {
		OurUsers user = usersRepo.findById(id)
		            .orElseThrow(() -> new UserNotFoundException("Employee not found with id: " + id));
		
		 List<UserActionMapping> userActionMappingList = user.getActionList();
		 
		 return userActionMappingList.stream()
		            .map(mapping -> new UserActionDTO(		     
		                    mapping.getRole().getRoleId(),
		                    mapping.getAction().getActionId()
		            ))
		            .collect(Collectors.toList());
	}

	@Override
	public boolean mapActionsToUser(MapUserRequestDto request) {

		OurUsers user = usersRepo.findById(request.getUserId())
		            .orElseThrow(() -> new UserNotFoundException("User not found with userId: " + request.getUserId()));

		    userActionMappingRepository.deleteByUser(user);

		    List<UserActionMapping> actions = request.getRoleActionIds().stream().map(id -> {
		        String[] tokens = id.split("_");
		       
		        Role role = roleRepo.findById(Long.parseLong(tokens[0]))
		                .orElseThrow(() -> new UserNotFoundException("Role not found with id: " + tokens[0]));
		        Action action = actionRepo.findById(Long.parseLong(tokens[1]))
		                .orElseThrow(() -> new UserNotFoundException("Action not found with id: " + tokens[1]));

		        UserActionMapping mapping = new UserActionMapping();		       
		        mapping.setRole(role);
		        mapping.setAction(action);
		        mapping.setUser(user);
		        return mapping;
		    }).collect(Collectors.toList());

		    userActionMappingRepository.saveAll(actions);
		    user.setActionList(actions);

		    return true;
	}

	@Override
	public void setupAdminUser() {


	    // Personal details
        personal personalInfo = new personal();
        personalInfo.setEmpId("EMP000");
        personalInfo.setFirstName("Admin");
        personalInfo.setLastName("User");
        personalInfo.setDateOfBirth(new Date(0, 0, 0));
        personalInfo.setGender("Male");
        personalInfo.setMaritalStatus("Single");
        personalInfo.setNationality("DemoLand");

        // Identification
        Identification idInfo = new Identification();
        idInfo.setImmigrationStatus("Citizen");
        idInfo.setIdProof("Passport");
        idInfo.setDocumentNumber("ABC12345");

        // Work
        Work workInfo = new Work();
        workInfo.setEmploymentStatus("Permanent");
        workInfo.setDepartment("IT");
        workInfo.setJobTitle("System Administrator");
        workInfo.setDoj(new Date(0, 0, 0));
        workInfo.setTimeZone("UTC");

        // Contact
        Contact contact = new Contact();
        contact.setResidentialAddress("123 Admin St.");
        contact.setCity("AdminCity");
        contact.setState("AdminState");
        contact.setCountry("AdminCountry");
        contact.setPostalCode("123456");
        contact.setWorkEmail("admin@company.com");
        contact.setMobileNumber("1234567890");
        contact.setPrimaryEmergencyContactName("John Doe");
        contact.setPrimaryEmergencyContactNumber("0987654321");
        contact.setRelationshipToPrimaryEmergencyContact("Brother");

        // Report
        Report report = new Report();
        report.setManager("CTO");

        // Employee
        Employee adminEmployee = new Employee();
        adminEmployee.setPersonal(personalInfo);
        adminEmployee.setIdentification(idInfo);
        adminEmployee.setWork(workInfo);
        adminEmployee.setContact(contact);
        adminEmployee.setReport(report);

        employeeRepository.save(adminEmployee);
        
        OurUsers adminUser = new OurUsers();
        adminUser.setName("admin");
        adminUser.setPassword(passwordEncoder.encode("admin123")); // default password
        adminUser.setRole("admin");
        adminUser.setEmail("superadmin@infinevocloud.com");
        adminUser.setCity("Dummy");
  
        adminUser.setEmpId("EMP000");
        adminUser.setEmployee(adminEmployee);

        usersRepo.save(adminUser);
        
        
		
	}

}
