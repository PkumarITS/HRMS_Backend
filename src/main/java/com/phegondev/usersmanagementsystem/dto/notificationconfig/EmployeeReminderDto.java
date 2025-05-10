package com.phegondev.usersmanagementsystem.dto.notificationconfig;

import java.time.LocalDateTime;

import com.phegondev.usersmanagementsystem.enumuration.ReminderLevel;

public class EmployeeReminderDto {

    private Long id;
    private boolean enabled;
    private String day;
    private String time;
    private ReminderLevel level;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ReminderLevel getLevel() {
        return level;
    }

    public void setLevel(ReminderLevel level) {
        this.level = level;
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
    
    
    

	public EmployeeReminderDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public EmployeeReminderDto(Long id, boolean enabled, String day, String time, ReminderLevel level,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.enabled = enabled;
		this.day = day;
		this.time = time;
		this.level = level;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "EmployeeReminderDto [id=" + id + ", enabled=" + enabled + ", day=" + day + ", time=" + time + ", level="
				+ level + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
    
    
    
}