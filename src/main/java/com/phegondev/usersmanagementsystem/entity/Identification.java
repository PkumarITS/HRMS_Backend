package com.phegondev.usersmanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
//@Data
public class Identification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Immigration status is required")
    private String immigrationStatus;

    @Pattern(regexp = "^$|^\\d{10}$", message = "Personal Tax ID must be 10 digits if provided")
    private String personalTaxId;

    private String socialInsurance;

    @NotBlank(message = "ID Proof is required")
    private String idProof;

    private String documentName;

    @NotBlank(message = "Document number is required")
    private String documentNumber;

    @OneToOne(mappedBy = "identification", cascade = CascadeType.ALL)
    @JsonBackReference
    private Employee employee;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImmigrationStatus() {
		return immigrationStatus;
	}

	public void setImmigrationStatus(String immigrationStatus) {
		this.immigrationStatus = immigrationStatus;
	}

	public String getPersonalTaxId() {
		return personalTaxId;
	}

	public void setPersonalTaxId(String personalTaxId) {
		this.personalTaxId = personalTaxId;
	}

	public String getSocialInsurance() {
		return socialInsurance;
	}

	public void setSocialInsurance(String socialInsurance) {
		this.socialInsurance = socialInsurance;
	}

	public String getIdProof() {
		return idProof;
	}

	public void setIdProof(String idProof) {
		this.idProof = idProof;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
    
    
}