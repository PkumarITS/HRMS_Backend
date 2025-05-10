package com.phegondev.usersmanagementsystem.dto.notificationconfig;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.phegondev.usersmanagementsystem.enumuration.ReminderLevel;

public class HrReminderDto {

    private Long id;
    private boolean enabled;
    private DayOfWeek day;  // Changed to DayOfWeek
    private LocalTime time;  // Changed to LocalTime
    private ReminderLevel level;
    private List<String> recipients;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public ReminderLevel getLevel() {
        return level;
    }

    public void setLevel(ReminderLevel level) {
        this.level = level;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Constructors

    public HrReminderDto() {
        super();
    }

    public HrReminderDto(Long id, boolean enabled, DayOfWeek day, LocalTime time, ReminderLevel level,
                         List<String> recipients, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.enabled = enabled;
        this.day = day;
        this.time = time;
        this.level = level;
        this.recipients = recipients;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "HrReminderDto [id=" + id + ", enabled=" + enabled + ", day=" + day + ", time=" + time +
                ", level=" + level + ", recipients=" + recipients + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
}
