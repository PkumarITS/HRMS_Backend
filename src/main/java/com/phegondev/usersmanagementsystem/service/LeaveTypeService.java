//package com.phegondev.usersmanagementsystem.service;
//
//import com.phegondev.usersmanagementsystem.entity.LeaveType;
//import com.phegondev.usersmanagementsystem.repository.LeaveTypeRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//public class LeaveTypeService {
//
//    private final LeaveTypeRepository repository;
//
//    @Autowired
//    public LeaveTypeService(LeaveTypeRepository repository) {
//        this.repository = repository;
//    }
//
//    public List<LeaveType> getAllLeaveTypes() {
//        return repository.findAllByOrderByNameAsc();
//    }
//
//    public List<LeaveType> getAllActiveLeaveTypes() {
//        return repository.findAllByIsActive(true);
//    }
//
//    public LeaveType getLeaveTypeById(Long id) {
//        return repository.findById(id).orElse(null);
//    }
//
//    public LeaveType createLeaveType(LeaveType leaveType) {
//        if (repository.existsByName(leaveType.getName())) {
//            throw new IllegalArgumentException("Leave type with this name already exists");
//        }
//        leaveType.setCreatedAt(LocalDateTime.now());
//        return repository.save(leaveType);
//    }
//
//    public LeaveType updateLeaveType(Long id, LeaveType leaveTypeDetails) {
//        LeaveType existingLeaveType = repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("LeaveType not found with id: " + id));
//
//        if (!existingLeaveType.getName().equals(leaveTypeDetails.getName()) && 
//            repository.existsByName(leaveTypeDetails.getName())) {
//            throw new IllegalArgumentException("Leave type with this name already exists");
//        }
//
//        existingLeaveType.setName(leaveTypeDetails.getName());
//        existingLeaveType.setDescription(leaveTypeDetails.getDescription());
//        existingLeaveType.setDefaultDays(leaveTypeDetails.getDefaultDays());
//        existingLeaveType.setActive(leaveTypeDetails.isActive());
//        existingLeaveType.setUpdatedAt(LocalDateTime.now());
//
//        return repository.save(existingLeaveType);
//    }
//
//    public void deleteLeaveType(Long id) {
//        repository.deleteById(id);
//    }
//
//    public LeaveType toggleLeaveTypeStatus(Long id) {
//        LeaveType leaveType = repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("LeaveType not found with id: " + id));
//        leaveType.setActive(!leaveType.isActive());
//        leaveType.setUpdatedAt(LocalDateTime.now());
//        return repository.save(leaveType);
//    }
//
//    public boolean existsByName(String name) {
//        return repository.existsByName(name);
//    }
//}





package com.phegondev.usersmanagementsystem.service;

import com.phegondev.usersmanagementsystem.entity.LeaveType;
import com.phegondev.usersmanagementsystem.repository.LeaveTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveTypeService {

    private final LeaveTypeRepository repository;

    @Autowired
    public LeaveTypeService(LeaveTypeRepository repository) {
        this.repository = repository;
    }

    // Changed to return by creation date (newest first)
    public List<LeaveType> getAllLeaveTypes() {
        return repository.findAllByOrderByCreatedAtAsc();

    }

    // Active leave types also sorted by creation date (newest first)
    public List<LeaveType> getAllActiveLeaveTypes() {
        List<LeaveType> activeTypes = repository.findAllByIsActive(true);
        // Sort by createdAt descending (newest first)
        activeTypes.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        return activeTypes;
    }

    public LeaveType getLeaveTypeById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public LeaveType createLeaveType(LeaveType leaveType) {
        if (repository.existsByName(leaveType.getName())) {
            throw new IllegalArgumentException("Leave type with this name already exists");
        }
        leaveType.setCreatedAt(LocalDateTime.now());
        leaveType.setUpdatedAt(LocalDateTime.now());
        return repository.save(leaveType);
    }

    public LeaveType updateLeaveType(Long id, LeaveType leaveTypeDetails) {
        LeaveType existingLeaveType = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("LeaveType not found with id: " + id));

        if (!existingLeaveType.getName().equals(leaveTypeDetails.getName()) && 
            repository.existsByName(leaveTypeDetails.getName())) {
            throw new IllegalArgumentException("Leave type with this name already exists");
        }

        existingLeaveType.setName(leaveTypeDetails.getName());
        existingLeaveType.setDescription(leaveTypeDetails.getDescription());
        existingLeaveType.setDefaultDays(leaveTypeDetails.getDefaultDays());
        existingLeaveType.setActive(leaveTypeDetails.isActive());
        existingLeaveType.setUpdatedAt(LocalDateTime.now());

        return repository.save(existingLeaveType);
    }

    public void deleteLeaveType(Long id) {
        repository.deleteById(id);
    }

    public LeaveType toggleLeaveTypeStatus(Long id) {
        LeaveType leaveType = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("LeaveType not found with id: " + id));
        leaveType.setActive(!leaveType.isActive());
        leaveType.setUpdatedAt(LocalDateTime.now());
        return repository.save(leaveType);
    }

    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }
}