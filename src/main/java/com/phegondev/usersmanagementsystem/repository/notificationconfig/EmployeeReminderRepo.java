package com.phegondev.usersmanagementsystem.repository.notificationconfig;

import org.springframework.data.jpa.repository.JpaRepository;
import com.phegondev.usersmanagementsystem.entity.notificationconfig.EmployeeReminder;

public interface EmployeeReminderRepo extends JpaRepository<EmployeeReminder, Long> {
}
