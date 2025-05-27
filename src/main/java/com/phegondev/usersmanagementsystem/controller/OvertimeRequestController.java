package com.phegondev.usersmanagementsystem.controller;

import com.phegondev.usersmanagementsystem.entity.OvertimeRequest;
import com.phegondev.usersmanagementsystem.entity.OurUsers;
import com.phegondev.usersmanagementsystem.service.OvertimeRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OvertimeRequestController {

    @Autowired
    private OvertimeRequestService service;

    @PostMapping("/overtime/add")
    public ResponseEntity<OvertimeRequest> createOvertimeRequest(@RequestBody OvertimeRequest request) {
        // Validate time
        if (request.getEndTime().isBefore(request.getStartTime())) {
            return ResponseEntity.badRequest().build();
        }
        
        // Set employee info from authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OurUsers user = (OurUsers) authentication.getPrincipal();
        request.setEmployeeId(user.getEmpId());
        request.setEmployeeName(user.getName());
        
        return ResponseEntity.ok(service.save(request));
    }

    @GetMapping("/overtime")
    public ResponseEntity<List<OvertimeRequest>> getOvertimeRequestsByEmployeeId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OurUsers user = (OurUsers) authentication.getPrincipal();
        List<OvertimeRequest> requests = service.getByEmployeeId(user.getEmpId());
        return requests.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(requests);
    }

    @GetMapping("/overtime/all")
    public List<OvertimeRequest> getAllOvertimeRequests(
            @RequestParam(required = false) String status) {
        if (status != null) {
            return service.getByStatus(status);
        }
        return service.getAll();
    }

    @PutMapping("/overtime/{id}/status")
    public ResponseEntity<OvertimeRequest> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return service.updateStatus(id, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/overtime/{id}")
    public ResponseEntity<OvertimeRequest> updateOvertimeRequest(
            @PathVariable Long id,
            @RequestBody OvertimeRequest requestDetails) {
        try {
            OvertimeRequest updatedRequest = service.updateOvertimeRequest(id, requestDetails);
            return ResponseEntity.ok(updatedRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/overtime/{id}")
    public ResponseEntity<Void> deleteOvertimeRequest(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/overtime/{id}")
    public ResponseEntity<OvertimeRequest> getOvertimeRequestById(
            @PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}