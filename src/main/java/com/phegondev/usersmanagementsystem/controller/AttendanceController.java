package com.phegondev.usersmanagementsystem.controller;

import com.phegondev.usersmanagementsystem.entity.Attendance;
import com.phegondev.usersmanagementsystem.entity.OurUsers;
import com.phegondev.usersmanagementsystem.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
public class AttendanceController {

    @Autowired
    private AttendanceService service;

    @PostMapping("/attendance/clock-in")
    public ResponseEntity<Attendance> clockIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OurUsers user = (OurUsers) auth.getPrincipal();

        System.out.println("🟢 Clock-In Request by user: " + user.getEmpId() + " - " + user.getName());

        return ResponseEntity.ok(service.clockIn(user.getEmpId(), user.getName()));
    }


    @PutMapping("/attendance/clock-out")
    public ResponseEntity<Attendance> clockOut() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OurUsers user = (OurUsers) auth.getPrincipal();
        List<Attendance> open = service.getOpenAttendance(user.getEmpId());
        if (open.isEmpty()) return ResponseEntity.badRequest().build();
        return service.clockOut(open.get(0).getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/attendance/all")
    public ResponseEntity<List<Attendance>> getAll(
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        if (employeeId != null && date != null) {
            return ResponseEntity.ok(service.getByEmployeeAndDateRange(employeeId, date, date));
        } else if (employeeId != null) {
            return ResponseEntity.ok(service.getByEmployeeId(employeeId));
        } else if (date != null) {
            return ResponseEntity.ok(service.getByDateRange(date, date));
        }
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/attendance/my-attendance")
    public ResponseEntity<List<Attendance>> getMyAttendance() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OurUsers user = (OurUsers) auth.getPrincipal();
        return ResponseEntity.ok(service.getByEmployeeId(user.getEmpId()));
    }

    @GetMapping("/attendance/date-range")
    public ResponseEntity<List<Attendance>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(service.getByDateRange(startDate, endDate));
    }

    @GetMapping("/attendance/today")
    public ResponseEntity<List<Attendance>> getTodayAttendance() {
        return ResponseEntity.ok(service.getTodayAttendance());
    }
}