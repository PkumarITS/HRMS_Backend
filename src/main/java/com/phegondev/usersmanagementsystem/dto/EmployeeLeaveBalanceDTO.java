package com.phegondev.usersmanagementsystem.dto;

import java.time.LocalDate;

public class EmployeeLeaveBalanceDTO {
    private Long id;
    private String name;
    private String description;
    private Integer defaultDays;
    private Boolean carryForward;  // Changed to boolean
    private Float  remainingDays;
    private LocalDate endDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getDefaultDays() {
		return defaultDays;
	}
	public void setDefaultDays(Integer defaultDays) {
		this.defaultDays = defaultDays;
	}
	public Boolean getCarryForward() {
		return carryForward;
	}
	public void setCarryForward(Boolean carryForward) {
		this.carryForward = carryForward;
	}
	public Float getRemainingDays() {
		return remainingDays;
	}
	public void setRemainingDays(Float remainingDays) {
		this.remainingDays = remainingDays;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	@Override
	public String toString() {
		return "EmployeeLeaveBalanceDTO [id=" + id + ", name=" + name + ", description=" + description
				+ ", defaultDays=" + defaultDays + ", carryForward=" + carryForward + ", remainingDays=" + remainingDays
				+ ", endDate=" + endDate + "]";
	}
    
    

}