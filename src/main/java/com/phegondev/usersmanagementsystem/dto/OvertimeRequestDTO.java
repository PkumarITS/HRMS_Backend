package com.phegondev.usersmanagementsystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OvertimeRequestDTO {
    private Long id;
    private String employeeId;
    private String employeeName;
    private String category;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String project;
    private String notes;
    private String status;
    private LocalDateTime createdAt;
    private double durationHours;

    // You can add a method to convert from entity to DTO
    public static OvertimeRequestDTO fromEntity(com.phegondev.usersmanagementsystem.entity.OvertimeRequest entity) {
        OvertimeRequestDTO dto = new OvertimeRequestDTO();
        dto.setId(entity.getId());
        dto.setEmployeeId(entity.getEmployeeId());
        dto.setEmployeeName(entity.getEmployeeName());
        dto.setCategory(entity.getCategory());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setProject(entity.getProject());
        dto.setNotes(entity.getNotes());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setDurationHours(entity.getDurationHours());
        return dto;
    }
}