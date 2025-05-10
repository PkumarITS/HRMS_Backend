package com.phegondev.usersmanagementsystem.repository.notificationconfig;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phegondev.usersmanagementsystem.entity.notificationconfig.HrReminder;

public interface HrReminderRepo extends JpaRepository<HrReminder, Long> {

}
