package com.phegondev.usersmanagementsystem.controller;

import com.phegondev.usersmanagementsystem.dto.ProjectDTO;
import com.phegondev.usersmanagementsystem.entity.OurUsers;
import com.phegondev.usersmanagementsystem.entity.Project;
import com.phegondev.usersmanagementsystem.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/admin/projects")
    public ResponseEntity<List<ProjectDTO>> getAllProjects(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String managerId) { // Add managerId parameter
    
        List<Project> projects;
    
        if (managerId != null && !managerId.isEmpty()) {
            projects = projectService.getProjectsByManagerId(managerId);
        } else if (status != null && !status.isEmpty()) {
            projects = projectService.getProjectsByStatus(status);
        } else if (search != null && !search.isEmpty()) {
            projects = projectService.searchProjectsByName(search);
        } else {
            projects = projectService.getAllProjects();
        }
    
        // Convert to ProjectDTO with embedded AssignmentDTOs
        List<ProjectDTO> projectDTOs = projects.stream().map(project -> {
            ProjectDTO dto = new ProjectDTO();
            dto.setId(project.getId());
            dto.setName(project.getName());
            dto.setCategory(project.getCategory());
            dto.setStartDate(project.getStartDate());
            dto.setEndDate(project.getEndDate());
            dto.setNotification(project.getNotification());
            dto.setPriority(project.getPriority());
            dto.setBudget(project.getBudget());
            dto.setDescription(project.getDescription());
            dto.setStatus(project.getStatus());
            dto.setProgress(project.getProgress());
            dto.setManagerId(project.getManagerId());
            dto.setManagerName(project.getManagerName());
    
            // Convert Assignments
            List<com.phegondev.usersmanagementsystem.dto.AssignmentDTO> assignmentDTOs =
                    project.getAssignments().stream().map(assignment -> {
                        com.phegondev.usersmanagementsystem.dto.AssignmentDTO adto =
                                new com.phegondev.usersmanagementsystem.dto.AssignmentDTO();
                        adto.setId(assignment.getId());
                        adto.setEmpId(assignment.getEmpId());
                        adto.setEmpName(assignment.getEmpName());
                        adto.setProjectId(project.getId());
                        return adto;
                    }).toList();
    
            dto.setAssignments(assignmentDTOs);
            return dto;
        }).toList();
    
        return ResponseEntity.ok(projectDTOs);
    }
    

    @GetMapping("/admin/projects/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @PostMapping("/admin/projects/add")
    public ResponseEntity<Project> createProject(@RequestBody ProjectDTO projectDTO) {
        Project project = projectService.createProject(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    @PutMapping("/admin/projects/{id}")
    public ResponseEntity<Project> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectDTO projectDTO) {
        Project project = projectService.updateProject(id, projectDTO);
        return ResponseEntity.ok(project);
    }

    @PatchMapping("/admin/projects/{id}/status")
    public ResponseEntity<Project> updateProjectStatus(
            @PathVariable Long id,
            @RequestBody String status) {
        Project project = projectService.updateProjectStatus(id, status);
        return ResponseEntity.ok(project);
    }

    @PatchMapping("/admin/projects/{id}/progress")
    public ResponseEntity<Project> updateProjectProgress(
            @PathVariable Long id,
            @RequestBody Integer progress) {
        Project project = projectService.updateProjectProgress(id, progress);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/admin/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/projects/count")
    public ResponseEntity<Long> countProjects() {
        Long count = projectService.countProjects();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/admin/projects/status-count")
    public ResponseEntity<Map<String, Long>> countProjectsByStatus() {
        Map<String, Long> statusCounts = projectService.countProjectsByStatus();
        return ResponseEntity.ok(statusCounts);
    }
    
    @GetMapping("/user/projects/by-emp")
    public ResponseEntity<List<ProjectDTO>> getProjectsByEmpId() {
    	   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();      
          
           OurUsers user = (OurUsers) authentication.getPrincipal();
           String empId = user.getEmpId();
           System.out.println(" user emp id: " + user.getEmpId());
        List<ProjectDTO> projects = projectService.getProjectsByEmpId(empId);
        return ResponseEntity.ok(projects);
    }

}