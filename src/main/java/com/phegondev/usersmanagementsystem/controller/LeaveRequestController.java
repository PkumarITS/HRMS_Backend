
package com.phegondev.usersmanagementsystem.controller;

import com.phegondev.usersmanagementsystem.entity.LeaveRequest;
import com.phegondev.usersmanagementsystem.entity.LeaveType;
import com.phegondev.usersmanagementsystem.entity.OurUsers;
import com.phegondev.usersmanagementsystem.service.LeaveBalanceService;
import com.phegondev.usersmanagementsystem.service.LeaveRequestService;
import com.phegondev.usersmanagementsystem.service.LeaveTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
// @PreAuthorize("hasAnyAuthority('admin', 'user')")
// @RequestMapping("/adminuser/leaves")
@CrossOrigin(origins = "http://localhost:5173")
public class LeaveRequestController {

	private final LeaveBalanceService leaveBalanceService;
	 private final LeaveTypeService leaveTypeService;

	@Autowired
	private LeaveRequestService service;

	LeaveRequestController(LeaveBalanceService leaveBalanceService, LeaveTypeService leaveTypeService) {
		this.leaveBalanceService = leaveBalanceService;
		this.leaveTypeService = leaveTypeService;
	}

	// Get all leave requests
	@GetMapping("/admin/leaves/all")
	public List<LeaveRequest> getAllLeaveRequests(
			@RequestParam(required = false) String employeeName,
			@RequestParam(required = false) String leaveType) {

		if (employeeName != null && leaveType != null) {
			return service.getByEmployeeNameAndLeaveType(employeeName, leaveType);
		} else if (employeeName != null) {
			return service.getByEmployeeName(employeeName);
		} else if (leaveType != null) {
			return service.getByLeaveType(leaveType);
		} else {
			return service.getAll();
		}
	}

	// Create a new leave request
	@PostMapping("/user/leaves/add")
	public ResponseEntity<LeaveRequest> createLeaveRequest(@RequestBody LeaveRequest request) {
		// Backend validation to reject past dates
		if (request.getStartDate() != null && request.getStartDate().isBefore(java.time.LocalDate.now())) {
			return ResponseEntity.badRequest().body(null); // Reject if start date is in the past
		}
		return ResponseEntity.ok(service.save(request));
	}

	// Update the status of a leave request by ID
	// @PutMapping("/{id}/status")
	// public ResponseEntity<LeaveRequest> updateStatus(
	// @PathVariable Long id,
	// @RequestParam String status) {
	// return service.updateStatus(id, status)
	// .map(ResponseEntity::ok)
	// .orElse(ResponseEntity.notFound().build());
	// }

	@PutMapping("/admin/leaves/{id}/status")
	public ResponseEntity<LeaveRequest> updateStatus(
			@PathVariable Long id,
			@RequestParam String status) {
		Optional<LeaveRequest> updatedRequest = service.updateStatus(id, status);

		if (updatedRequest.isPresent() && "Approved".equalsIgnoreCase(status)) {
			leaveBalanceService.updateLeaveBalanceOnApproval(updatedRequest.get());
		}

		return updatedRequest
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Delete a leave request by ID
	// @DeleteMapping("/{id}")
	// public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
	// service.deleteById(id);
	// return ResponseEntity.noContent().build();
	// }

	// Get by employee ID
	@GetMapping("/user/leaves")
	public ResponseEntity<List<LeaveRequest>> getLeaveRequestsByEmployeeId() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		OurUsers user = (OurUsers) authentication.getPrincipal();
		String empId = user.getEmpId();
		System.out.println("🔐 Authenticated user empId: " + empId);
		List<LeaveRequest> requests = service.getByEmployeeId(empId);
		return requests.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(requests);
	}
	
	   @GetMapping("/leave-types")
	    public ResponseEntity<List<LeaveType>> getAllLeaveTypes() {
	        return ResponseEntity.ok(leaveTypeService.getAllLeaveTypes());
	    }


	// Add these methods to LeaveRequestController.java

	@GetMapping("/admin/leaves/{id}")
	public ResponseEntity<LeaveRequest> getLeaveRequestById(@PathVariable Long id) {
		Optional<LeaveRequest> leaveRequest = service.findById(id);
		return leaveRequest.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/admin/leaves/{id}")
	public ResponseEntity<LeaveRequest> updateLeaveRequest(
			@PathVariable Long id,
			@RequestBody LeaveRequest leaveRequest) {
		try {
			LeaveRequest updatedRequest = service.updateLeaveRequest(id, leaveRequest);
			return ResponseEntity.ok(updatedRequest);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
