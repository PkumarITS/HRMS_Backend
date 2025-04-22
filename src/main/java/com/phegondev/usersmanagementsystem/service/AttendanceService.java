package com.phegondev.usersmanagementsystem.service;

import com.phegondev.usersmanagementsystem.entity.Attendance;
import com.phegondev.usersmanagementsystem.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository repository;

    public Attendance clockIn(String employeeId, String employeeName) {

        // 🔍 Debug Log
        System.out.println("📥 Creating attendance record for: " + employeeId + ", Name: " + employeeName);

        Attendance attendance = new Attendance();
        attendance.setEmployeeId(employeeId);
        attendance.setEmployeeName(employeeName);
        attendance.setInTime(LocalDateTime.now());
        attendance.setStatus("Present");

        // 🔍 Debug Log
        System.out.println("🕓 In-Time set to: " + attendance.getInTime());

        Attendance saved = repository.save(attendance);

        // 🔍 Debug Log
        System.out.println("✅ Attendance saved with ID: " + saved.getId());

        return saved;
    }


    public Optional<Attendance> clockOut(Long attendanceId) {
        return repository.findById(attendanceId).map(attendance -> {
            attendance.setOutTime(LocalDateTime.now());
            long durationHours = Duration.between(attendance.getInTime(), attendance.getOutTime()).toHours();
            
            if (durationHours < 4) attendance.setStatus("Absent");
            else if (durationHours < 6) attendance.setStatus("Half-day");
            else attendance.setStatus("Present");
            
            return repository.save(attendance);
        });
    }

    public List<Attendance> getAll() {
        return repository.findAllByOrderByInTimeDesc();
    }

    public List<Attendance> getByEmployeeId(String employeeId) {
        return repository.findByEmployeeIdOrderByInTimeDesc(employeeId);
    }

    public List<Attendance> getTodayAttendance() {
        return repository.findByDate(LocalDate.now());
    }

    public List<Attendance> getOpenAttendance(String employeeId) {
        return repository.findOpenAttendanceByEmployeeId(employeeId);
    }

    public List<Attendance> getByDateRange(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        return repository.findByInTimeBetweenOrderByInTimeDesc(start, end);
    }

    public List<Attendance> getByEmployeeAndDateRange(String employeeId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        return repository.findByEmployeeIdAndInTimeBetweenOrderByInTimeDesc(employeeId, start, end);
    }
}