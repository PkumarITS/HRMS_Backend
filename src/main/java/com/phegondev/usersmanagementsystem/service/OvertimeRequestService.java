package com.phegondev.usersmanagementsystem.service;

import com.phegondev.usersmanagementsystem.entity.OvertimeRequest;
import com.phegondev.usersmanagementsystem.repository.OvertimeRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OvertimeRequestService {

    @Autowired
    private OvertimeRequestRepository repository;

    private String formatStatus(String status) {
        if (status == null || status.isEmpty()) return status;
        return status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();
    }

    public List<OvertimeRequest> getAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public OvertimeRequest save(OvertimeRequest request) {
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new IllegalArgumentException("End time cannot be before start time.");
        }
        request.setStatus(formatStatus(request.getStatus()));
        return repository.save(request);
    }

    public Optional<OvertimeRequest> updateStatus(Long id, String status) {
        return repository.findById(id).map(req -> {
            req.setStatus(formatStatus(status));
            return repository.save(req);
        });
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<OvertimeRequest> getByEmployeeId(String employeeId) {
        return repository.findByEmployeeIdOrderByCreatedAtDesc(employeeId);
    }

    public List<OvertimeRequest> getByStatus(String status) {
        return repository.findByStatusOrderByCreatedAtDesc(status);
    }

    public Optional<OvertimeRequest> findById(Long id) {
        return repository.findById(id);
    }

    public OvertimeRequest updateOvertimeRequest(Long id, OvertimeRequest requestDetails) {
        return repository.findById(id).map(request -> {
            if (requestDetails.getCategory() != null) {
                request.setCategory(requestDetails.getCategory());
            }
            if (requestDetails.getStartTime() != null) {
                request.setStartTime(requestDetails.getStartTime());
            }
            if (requestDetails.getEndTime() != null) {
                request.setEndTime(requestDetails.getEndTime());
            }
            if (requestDetails.getProject() != null) {
                request.setProject(requestDetails.getProject());
            }
            if (requestDetails.getNotes() != null) {
                request.setNotes(requestDetails.getNotes());
            }
            if (requestDetails.getStatus() != null) {
                request.setStatus(formatStatus(requestDetails.getStatus()));
            }
            return repository.save(request);
        }).orElseThrow(() -> new RuntimeException("OvertimeRequest not found with id: " + id));
    }
    
    public Optional<OvertimeRequest> getById(Long id) {
        return repository.findById(id);
    }
}