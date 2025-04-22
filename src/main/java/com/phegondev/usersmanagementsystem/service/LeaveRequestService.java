//package com.phegondev.usersmanagementsystem.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.phegondev.usersmanagementsystem.entity.LeaveRequest;
//import com.phegondev.usersmanagementsystem.repository.LeaveRequestRepository;
//
//import java.util.List;
//
//@Service
//public class LeaveRequestService {
//
//    @Autowired
//    private LeaveRequestRepository leaveRequestRepository;
//
//    public List<LeaveRequest> getAllLeaveRequests() {
//        return leaveRequestRepository.findAll();
//    }
//
//    public LeaveRequest getLeaveRequestById(Long id) {
//        return leaveRequestRepository.findById(id).orElse(null);
//    }
//
//    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
//        return leaveRequestRepository.save(leaveRequest);
//    }
//
//    public LeaveRequest updateLeaveRequest(Long id, LeaveRequest leaveRequest) {
//        LeaveRequest existingLeave = leaveRequestRepository.findById(id).orElse(null);
//        if (existingLeave != null) {
//            existingLeave.setEmpId(leaveRequest.getEmpId());
//            existingLeave.setName(leaveRequest.getName());
//            existingLeave.setLeaveType(leaveRequest.getLeaveType());
//            existingLeave.setFromDate(leaveRequest.getFromDate());
//            existingLeave.setToDate(leaveRequest.getToDate());
//            existingLeave.setReason(leaveRequest.getReason());
//            return leaveRequestRepository.save(existingLeave);
//        }
//        return null;
//    }
//
//    public void deleteLeaveRequest(Long id) {
//        leaveRequestRepository.deleteById(id);
//    }
//}

package com.phegondev.usersmanagementsystem.service;

import com.phegondev.usersmanagementsystem.entity.LeaveRequest;
import com.phegondev.usersmanagementsystem.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository repository;

    // Convert status to Title Case (e.g., "PENDING" → "Pending")
    private String formatStatus(String status) {
        if (status == null || status.isEmpty()) return status;
        return status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();
    }

    // Fetch all leave requests in descending order of createdAt
    public List<LeaveRequest> getAll() {
        List<LeaveRequest> leaveRequests = repository.findAllByOrderByCreatedAtDesc();
        leaveRequests.forEach(req -> req.setStatus(formatStatus(req.getStatus())));
        return leaveRequests;
    }

    // Save a new or updated leave request
    public LeaveRequest save(LeaveRequest request) {
        if (request.getToDate().isBefore(request.getFromDate())
                || request.getToDate().isEqual(request.getFromDate())) {
            throw new IllegalArgumentException("To date must be after From date.");
        }

        request.setStatus(formatStatus(request.getStatus()));
        return repository.save(request);
    }

    // Update only the status of a leave request
    public Optional<LeaveRequest> updateStatus(Long id, String status) {
        return repository.findById(id).map(req -> {
            req.setStatus(formatStatus(status));
            return repository.save(req);
        });
    }

    // Delete a leave request by ID
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    // Get leave requests by employee name
    public List<LeaveRequest> getByEmployeeName(String employeeName) {
        return repository.findByEmployeeNameIgnoreCaseOrderByCreatedAtDesc(employeeName);
    }

    // Get leave requests by leave type
    public List<LeaveRequest> getByLeaveType(String leaveType) {
        return repository.findByLeaveTypeIgnoreCaseOrderByCreatedAtDesc(leaveType);
    }

    // Get leave requests by employee name and leave type
    public List<LeaveRequest> getByEmployeeNameAndLeaveType(String employeeName, String leaveType) {
        return repository.findByEmployeeNameIgnoreCaseAndLeaveTypeIgnoreCaseOrderByCreatedAtDesc(employeeName, leaveType);
    }
    
    public List<LeaveRequest> getByEmployeeId(String employeeId) {
        return repository.findByEmployeeId(employeeId);
    }
    
    // Add these methods to LeaveRequestService.java

public Optional<LeaveRequest> findById(Long id) {
    return repository.findById(id);
}

public LeaveRequest updateLeaveRequest(Long id, LeaveRequest leaveRequestDetails) {
    return repository.findById(id).map(leaveRequest -> {
        if (leaveRequestDetails.getEmployeeName() != null) {
            leaveRequest.setEmployeeName(leaveRequestDetails.getEmployeeName());
        }
        if (leaveRequestDetails.getLeaveType() != null) {
            leaveRequest.setLeaveType(leaveRequestDetails.getLeaveType());
        }
        if (leaveRequestDetails.getFromDate() != null) {
            leaveRequest.setFromDate(leaveRequestDetails.getFromDate());
        }
        if (leaveRequestDetails.getToDate() != null) {
            leaveRequest.setToDate(leaveRequestDetails.getToDate());
        }
        if (leaveRequestDetails.getReason() != null) {
            leaveRequest.setReason(leaveRequestDetails.getReason());
        }
        if (leaveRequestDetails.getStatus() != null) {
            leaveRequest.setStatus(formatStatus(leaveRequestDetails.getStatus()));
        }
        return repository.save(leaveRequest);
    }).orElseThrow(() -> new RuntimeException("LeaveRequest not found with id: " + id));
}
    
    
    
    
    
    

    
}