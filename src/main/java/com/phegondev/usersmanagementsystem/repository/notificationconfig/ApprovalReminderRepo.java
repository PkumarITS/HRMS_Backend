package com.phegondev.usersmanagementsystem.repository.notificationconfig;

import org.springframework.data.jpa.repository.JpaRepository;
import com.phegondev.usersmanagementsystem.entity.notificationconfig.ApprovalReminder;

public interface ApprovalReminderRepo extends JpaRepository<ApprovalReminder, Long> {
	
}
