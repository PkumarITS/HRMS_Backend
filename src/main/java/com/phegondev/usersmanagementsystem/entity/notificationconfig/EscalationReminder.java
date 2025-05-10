package com.phegondev.usersmanagementsystem.entity.notificationconfig;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.phegondev.usersmanagementsystem.enumuration.ReminderLevel;

import jakarta.persistence.*;

@Entity
@Table(name = "escalation_reminders")
public class EscalationReminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean enabled;
    private String day;
    private String time;

    @ElementCollection
    @CollectionTable(name = "escalation_reminder_recipients", joinColumns = @JoinColumn(name = "reminder_id"))
    @Column(name = "email")
    private List<String> recipients = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
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
    
    

    public EscalationReminder() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    

	public EscalationReminder(Long id, boolean enabled, String day, String time, List<String> recipients,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.enabled = enabled;
		this.day = day;
		this.time = time;
		this.recipients = recipients;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	@Override
    public String toString() {
        return "EscalationReminder [id=" + id + ", enabled=" + enabled + ", day=" + day + ", time=" + time +
                ",  recipients=" + recipients +
                ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
}

