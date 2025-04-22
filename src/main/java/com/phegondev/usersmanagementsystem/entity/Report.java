package com.phegondev.usersmanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
//@Data
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Manager is required")
    private String manager;

    private String indirectManager;
    private String firstLevelApprover;
    private String secondLevelApprover;
    private String thirdLevelApprover;

    @Column(length = 2000)
    private String note;

    @OneToOne(mappedBy = "report", cascade = CascadeType.ALL)
    @JsonBackReference
    private Employee employee;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getIndirectManager() {
		return indirectManager;
	}

	public void setIndirectManager(String indirectManager) {
		this.indirectManager = indirectManager;
	}

	public String getFirstLevelApprover() {
		return firstLevelApprover;
	}

	public void setFirstLevelApprover(String firstLevelApprover) {
		this.firstLevelApprover = firstLevelApprover;
	}

	public String getSecondLevelApprover() {
		return secondLevelApprover;
	}

	public void setSecondLevelApprover(String secondLevelApprover) {
		this.secondLevelApprover = secondLevelApprover;
	}

	public String getThirdLevelApprover() {
		return thirdLevelApprover;
	}

	public void setThirdLevelApprover(String thirdLevelApprover) {
		this.thirdLevelApprover = thirdLevelApprover;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
    
    
}