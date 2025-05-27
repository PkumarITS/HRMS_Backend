package com.phegondev.usersmanagementsystem.controller;

import com.phegondev.usersmanagementsystem.entity.Employee;
import com.phegondev.usersmanagementsystem.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@Valid @RequestBody Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("errors", result.getFieldErrors().stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.toList()));
            return ResponseEntity.badRequest().body(response);
        }

        try {
            Employee savedEmployee = employeeService.saveEmployee(employee);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", savedEmployee);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Failed to add employee: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchEmployees(@RequestParam String query) {
        try {
            List<Map<String, String>> employees = employeeService.searchEmployees(query);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", employees));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Failed to search employees: " + e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllEmployees() {
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", employees);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return errorResponse("Failed to fetch employees: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(id);
            if (employee.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("data", employee.get());
                return ResponseEntity.ok(response);
            } else {
                return errorResponse("Employee not found with id: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return errorResponse("Failed to fetch employee: " + e.getMessage());
        }
    }

    @GetMapping("/ids")
    public ResponseEntity<?> getAllEmployeeIds() {
        try {
            List<String> employeeIds = employeeService.getAllEmployeeIds();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", employeeIds);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return errorResponse("Failed to fetch employee IDs: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employeeDetails,
            BindingResult result) {
        if (result.hasErrors()) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("errors", result.getFieldErrors().stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.toList()));
            return ResponseEntity.badRequest().body(response);
        }

        try {
            Optional<Employee> existingEmployeeOpt = employeeService.getEmployeeById(id);
            if (existingEmployeeOpt.isPresent()) {
                Employee existingEmployee = existingEmployeeOpt.get();

                // Update nested entities
                if (employeeDetails.getPersonal() != null) {
                    if (existingEmployee.getPersonal() == null) {
                        existingEmployee.setPersonal(employeeDetails.getPersonal());
                    } else {
                        existingEmployee.getPersonal()
                                .setEmploymentStatus(employeeDetails.getPersonal().getEmploymentStatus());
                        existingEmployee.getPersonal().setEmpId(employeeDetails.getPersonal().getEmpId());
                        existingEmployee.getPersonal().setFirstName(employeeDetails.getPersonal().getFirstName());
                        existingEmployee.getPersonal().setMiddleName(employeeDetails.getPersonal().getMiddleName());
                        existingEmployee.getPersonal().setLastName(employeeDetails.getPersonal().getLastName());
                        existingEmployee.getPersonal().setDateOfBirth(employeeDetails.getPersonal().getDateOfBirth());
                        existingEmployee.getPersonal().setGender(employeeDetails.getPersonal().getGender());
                        existingEmployee.getPersonal()
                                .setMaritalStatus(employeeDetails.getPersonal().getMaritalStatus());
                        existingEmployee.getPersonal().setNationality(employeeDetails.getPersonal().getNationality());
                        existingEmployee.getPersonal().setEthnicity(employeeDetails.getPersonal().getEthnicity());
                    }
                    existingEmployee.getPersonal().setEmployee(existingEmployee);
                }

                if (employeeDetails.getIdentification() != null) {
                    if (existingEmployee.getIdentification() == null) {
                        existingEmployee.setIdentification(employeeDetails.getIdentification());
                    } else {
                        existingEmployee.getIdentification()
                                .setImmigrationStatus(employeeDetails.getIdentification().getImmigrationStatus());
                        existingEmployee.getIdentification()
                                .setAadharCardNumber(employeeDetails.getIdentification().getAadharCardNumber());
                        existingEmployee.getIdentification()
                                .setPanCardNumber(employeeDetails.getIdentification().getPanCardNumber());
                        existingEmployee.getIdentification()
                                .setAddressProof(employeeDetails.getIdentification().getAddressProof());
                        existingEmployee.getIdentification()
                                .setAddressDocumentName(employeeDetails.getIdentification().getAddressDocumentName());
                        existingEmployee.getIdentification()
                                .setAddressDocumentNumber(
                                        employeeDetails.getIdentification().getAddressDocumentNumber());
                        existingEmployee.getIdentification()
                                .setPersonalTaxId(employeeDetails.getIdentification().getPersonalTaxId());
                        existingEmployee.getIdentification()
                                .setSocialInsurance(employeeDetails.getIdentification().getSocialInsurance());
                        existingEmployee.getIdentification()
                                .setIdProof(employeeDetails.getIdentification().getIdProof());
                        existingEmployee.getIdentification()
                                .setDocumentName(employeeDetails.getIdentification().getDocumentName());
                        existingEmployee.getIdentification()
                                .setDocumentNumber(employeeDetails.getIdentification().getDocumentNumber());
                    }
                    existingEmployee.getIdentification().setEmployee(existingEmployee);
                }

                if (employeeDetails.getWork() != null) {
                    if (existingEmployee.getWork() == null) {
                        existingEmployee.setWork(employeeDetails.getWork());
                    } else {
                        existingEmployee.getWork().setDepartment(employeeDetails.getWork().getDepartment());
                        existingEmployee.getWork().setJobTitle(employeeDetails.getWork().getJobTitle());
                        existingEmployee.getWork().setPayGrade(employeeDetails.getWork().getPayGrade());
                        existingEmployee.getWork().setDoj(employeeDetails.getWork().getDoj());
                        existingEmployee.getWork().setTerminationDate(employeeDetails.getWork().getTerminationDate());
                        existingEmployee.getWork().setWorkstationId(employeeDetails.getWork().getWorkstationId());
                        existingEmployee.getWork().setTimeZone(employeeDetails.getWork().getTimeZone());
                        existingEmployee.getWork().setShiftStartTime(employeeDetails.getWork().getShiftStartTime());
                        existingEmployee.getWork().setShiftEndTime(employeeDetails.getWork().getShiftEndTime());
                    }
                    existingEmployee.getWork().setEmployee(existingEmployee);
                }

                if (employeeDetails.getContact() != null) {
                    if (existingEmployee.getContact() == null) {
                        existingEmployee.setContact(employeeDetails.getContact());
                    } else {
                        existingEmployee.getContact()
                                .setResidentialAddress(employeeDetails.getContact().getResidentialAddress());
                        existingEmployee.getContact()
                                .setPermanentAddress(employeeDetails.getContact().getPermanentAddress());
                        existingEmployee.getContact().setCity(employeeDetails.getContact().getCity());
                        existingEmployee.getContact().setState(employeeDetails.getContact().getState());
                        existingEmployee.getContact().setCountry(employeeDetails.getContact().getCountry());
                        existingEmployee.getContact().setPostalCode(employeeDetails.getContact().getPostalCode());
                        existingEmployee.getContact().setWorkEmail(employeeDetails.getContact().getWorkEmail());
                        existingEmployee.getContact().setPersonalEmail(employeeDetails.getContact().getPersonalEmail());
                        existingEmployee.getContact().setMobileNumber(employeeDetails.getContact().getMobileNumber());
                        existingEmployee.getContact().setPrimaryEmergencyContactName(
                                employeeDetails.getContact().getPrimaryEmergencyContactName());
                        existingEmployee.getContact().setPrimaryEmergencyContactNumber(
                                employeeDetails.getContact().getPrimaryEmergencyContactNumber());
                        existingEmployee.getContact().setRelationshipToPrimaryEmergencyContact(
                                employeeDetails.getContact().getRelationshipToPrimaryEmergencyContact());
                        existingEmployee.getContact().setSecondaryEmergencyContactName(
                                employeeDetails.getContact().getSecondaryEmergencyContactName());
                        existingEmployee.getContact().setSecondaryEmergencyContactNumber(
                                employeeDetails.getContact().getSecondaryEmergencyContactNumber());
                        existingEmployee.getContact().setRelationshipToSecondaryEmergencyContact(
                                employeeDetails.getContact().getRelationshipToSecondaryEmergencyContact());
                        existingEmployee.getContact()
                                .setFamilyDoctorName(employeeDetails.getContact().getFamilyDoctorName());
                        existingEmployee.getContact().setFamilyDoctorContactNumber(
                                employeeDetails.getContact().getFamilyDoctorContactNumber());
                    }
                    existingEmployee.getContact().setEmployee(existingEmployee);
                }

                if (employeeDetails.getReport() != null) {
                    if (existingEmployee.getReport() == null) {
                        existingEmployee.setReport(employeeDetails.getReport());
                    } else {
                        existingEmployee.getReport().setManager(employeeDetails.getReport().getManager());
                        existingEmployee.getReport()
                                .setIndirectManager(employeeDetails.getReport().getIndirectManager());
                        existingEmployee.getReport()
                                .setFirstLevelApprover(employeeDetails.getReport().getFirstLevelApprover());
                        existingEmployee.getReport()
                                .setSecondLevelApprover(employeeDetails.getReport().getSecondLevelApprover());
                        existingEmployee.getReport()
                                .setThirdLevelApprover(employeeDetails.getReport().getThirdLevelApprover());
                        existingEmployee.getReport().setNote(employeeDetails.getReport().getNote());
                    }
                    existingEmployee.getReport().setEmployee(existingEmployee);
                }

                Employee updatedEmployee = employeeService.saveEmployee(existingEmployee);
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("data", updatedEmployee);
                return ResponseEntity.ok(response);
            } else {
                return errorResponse("Employee not found with id: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return errorResponse("Failed to update employee: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(id);
            if (employee.isPresent()) {
                employeeService.deleteEmployee(id);
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Employee deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                return errorResponse("Employee not found with id: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return errorResponse("Failed to delete employee: " + e.getMessage());
        }
    }

    private ResponseEntity<?> errorResponse(String message) {
        return errorResponse(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<?> errorResponse(String message, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", message);
        return ResponseEntity.status(status).body(errorResponse);
    }

    @GetMapping("/by-emp-id/{empId}")
    public ResponseEntity<?> getEmployeeByEmpId(@PathVariable String empId) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeByEmpId(empId);
            if (employee.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("data", employee.get());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("status", "error", "message", "Employee not found with empId: " + empId));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Failed to fetch employee: " + e.getMessage()));
        }
    }

    @GetMapping("/next-emp-id")
    public ResponseEntity<?> getNextEmpId(@RequestParam String employmentStatus) {
        try {
            String nextEmpId = employeeService.generateNextEmpId(employmentStatus);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", nextEmpId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Failed to generate employee ID: " + e.getMessage()));
        }
    }

}