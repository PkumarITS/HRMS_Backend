package com.phegondev.usersmanagementsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.phegondev.usersmanagementsystem.dto.ReqRes;
import com.phegondev.usersmanagementsystem.entity.OurUsers;
import com.phegondev.usersmanagementsystem.service.UsersManagementService;
import com.phegondev.usersmanagementsystem.repository.UsersRepo;

@RestController
public class UserManagementController {

    @Autowired
    private UsersManagementService usersManagementService;

    @Autowired
    private UsersRepo usersRepo;

    // Register
    // Register
    @PostMapping("/register")
    public ResponseEntity<ReqRes> register(@RequestBody ReqRes reg) {
        System.out.println("Received registration request: " + reg);
        ReqRes response = usersManagementService.register(reg);
        System.out.println("Registration response: " + response);
        return ResponseEntity.ok(response);
    }

    // Login
    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes req) {
        return ResponseEntity.ok(usersManagementService.login(req));
    }

    // Refresh Token
    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes req) {
        return ResponseEntity.ok(usersManagementService.refreshToken(req));
    }

    // Get all users - permit only to admin
    @GetMapping("/get-all-users")
    public ResponseEntity<ReqRes> getAllUsers() {
        return ResponseEntity.ok(usersManagementService.getAllUsers());
    }

    // Get User by ID
    @GetMapping("/get-user/{userId}")
    public ResponseEntity<ReqRes> getUserById(@PathVariable Integer userId) {
        return ResponseEntity.ok(usersManagementService.getUserByID(userId));
    }

    // Update user
    @PutMapping("/update/{userId}")
    public ResponseEntity<ReqRes> updateUser(@PathVariable Integer userId, @RequestBody OurUsers reqRes) {
        return ResponseEntity.ok(usersManagementService.updateUser(userId, reqRes));
    }

    // Get the profile
    @GetMapping("/get-profile")
    public ResponseEntity<ReqRes> getMyProfile() {

        // Get the Authentication object for the current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // getName() -> principle name(Username) for the current authenticated user
        String email = authentication.getName();
        ReqRes response = usersManagementService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Delete User
    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<ReqRes> deleteUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(usersManagementService.deleteUser(userId));
    }

    @GetMapping("/get-complete-profile")
    public ResponseEntity<ReqRes> getCompleteProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        System.out.println("Authenticated user email: " + email);

        OurUsers user = (OurUsers) authentication.getPrincipal();
        System.out.println("Authenticated user : " + user);

        ReqRes response = usersManagementService.getCompleteProfile(email);
        System.out.println("Final response: " + response);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/managers")
    public ResponseEntity<List<OurUsers>> getAllManagers() {
        List<OurUsers> managers = usersRepo.findAllByRole("manager");
        return ResponseEntity.ok(managers);
    }
}