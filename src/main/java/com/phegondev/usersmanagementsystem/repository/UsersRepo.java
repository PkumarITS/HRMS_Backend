package com.phegondev.usersmanagementsystem.repository;

import com.phegondev.usersmanagementsystem.entity.OurUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepo extends JpaRepository<OurUsers, Integer> {

    Optional<OurUsers> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByEmpId(String empId);
    OurUsers findByEmpId(String empId);
    OurUsers findByRole(String role);
    List<OurUsers> findAllByRole(String role);
}