package com.phegondev.usersmanagementsystem.controller;
import com.phegondev.usersmanagementsystem.entity.Holiday;
import com.phegondev.usersmanagementsystem.service.HolidayService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// @RequestMapping("/api/holidays")
@CrossOrigin(origins = "http://localhost:5173")
public class HolidayController {
    
    @Autowired
    private HolidayService holidayService;
    
    @GetMapping("/adminuser/holidays")
    public List<Holiday> getAllHolidays() {
        return holidayService.getAllHolidays();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Holiday> getHolidayById(@PathVariable Long id) {
        Holiday holiday = holidayService.getHolidayById(id);
        if (holiday != null) {
            return ResponseEntity.ok(holiday);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/admin/holidays/add")
    public Holiday createHoliday(@RequestBody Holiday holiday) {
        return holidayService.createHoliday(holiday);
    }
    
    @PutMapping("/admin/holidays/{id}")
    public ResponseEntity<Holiday> updateHoliday(@PathVariable Long id, @RequestBody Holiday holidayDetails) {
        Holiday updatedHoliday = holidayService.updateHoliday(id, holidayDetails);
        if (updatedHoliday != null) {
            return ResponseEntity.ok(updatedHoliday);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/admin/holidays/{id}")
    public ResponseEntity<Void> deleteHoliday(@PathVariable Long id) {
        holidayService.deleteHoliday(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/adminuser/holidays/search")
    public List<Holiday> searchHolidays(@RequestParam String query) {
        return holidayService.searchHolidays(query);
    }
    
    @GetMapping("/user/holidays/upcoming")
    public List<Holiday> getUpcomingHolidays() {
        return holidayService.getUpcomingHolidays();
    }
}