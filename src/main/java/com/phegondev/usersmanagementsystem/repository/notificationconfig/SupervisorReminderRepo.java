package com.phegondev.usersmanagementsystem.repository.notificationconfig;

import org.springframework.data.jpa.repository.JpaRepository;
import com.phegondev.usersmanagementsystem.entity.notificationconfig.SupervisorReminder;

public interface SupervisorReminderRepo extends JpaRepository<SupervisorReminder, Long> {
}

