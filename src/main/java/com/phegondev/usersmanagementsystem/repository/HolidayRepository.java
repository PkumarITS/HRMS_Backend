package com.phegondev.usersmanagementsystem.repository;
import com.phegondev.usersmanagementsystem.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    List<Holiday> findByNameContainingIgnoreCase(String name);
    List<Holiday> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Holiday> findByDate(LocalDate date);
}
