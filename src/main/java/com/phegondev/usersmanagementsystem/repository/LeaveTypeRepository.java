//package com.phegondev.usersmanagementsystem.repository;
//
//import com.phegondev.usersmanagementsystem.entity.LeaveType;
//import org.springframework.data.jpa.repository.JpaRepository;
//import java.util.List;
//import java.util.Optional;
//
//public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {
//    Optional<LeaveType> findByName(String name);
//    List<LeaveType> findAllByIsActive(boolean isActive);
//    List<LeaveType> findAllByOrderByNameAsc();
//    boolean existsByName(String name);
//    boolean existsByNameAndIdNot(String name, Long id);
//}



package com.phegondev.usersmanagementsystem.repository;

import com.phegondev.usersmanagementsystem.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {
    Optional<LeaveType> findByName(String name);
    List<LeaveType> findAllByIsActive(boolean isActive);
    
    // Changed from findAllByOrderByNameAsc() to these two methods
    List<LeaveType> findAllByOrderByCreatedAtDesc();  // Newest first
    List<LeaveType> findAllByOrderByNameAsc();        // Keep this for when alphabetical order is needed
    List<LeaveType> findAllByOrderByCreatedAtAsc();

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}