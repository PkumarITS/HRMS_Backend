package com.phegondev.usersmanagementsystem.service;

import com.phegondev.usersmanagementsystem.dto.ProjectDTO;
import com.phegondev.usersmanagementsystem.entity.Assignment;
import com.phegondev.usersmanagementsystem.entity.Project;
import com.phegondev.usersmanagementsystem.repository.AssignmentRepository;
import com.phegondev.usersmanagementsystem.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final AssignmentRepository assignmentRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, AssignmentRepository assignmentRepository ) {
        this.projectRepository = projectRepository;
        this.assignmentRepository = assignmentRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
    }

    public List<Project> getProjectsByStatus(String status) {
        return projectRepository.findByStatus(status);
    }

    public List<Project> searchProjectsByName(String name) {
        return projectRepository.findByNameContainingIgnoreCase(name);
    }

    public Project createProject(ProjectDTO projectDTO) {
        Project project = new Project();
        mapProjectDTOToEntity(projectDTO, project);
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, ProjectDTO projectDTO) {
        Project project = getProjectById(id);
        mapProjectDTOToEntity(projectDTO, project);
        return projectRepository.save(project);
    }

    public Project updateProjectStatus(Long id, String status) {
        Project project = getProjectById(id);
        project.setStatus(status);
        return projectRepository.save(project);
    }

    public Project updateProjectProgress(Long id, Integer progress) {
        Project project = getProjectById(id);
        project.setProgress(progress);
        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public Long countProjects() {
        return projectRepository.count();
    }

    public Map<String, Long> countProjectsByStatus() {
        return projectRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Project::getStatus,
                        Collectors.counting()
                ));
    }

    private void mapProjectDTOToEntity(ProjectDTO dto, Project entity) {
        entity.setName(dto.getName());
        entity.setCategory(dto.getCategory());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setNotification(dto.getNotification());
        entity.setPriority(dto.getPriority());
        entity.setBudget(dto.getBudget());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        entity.setProgress(dto.getProgress());
    }
    
    public List<ProjectDTO> getProjectsByEmpId(String empId) {
        System.out.println("🔍 Fetching projects for Employee ID: " + empId);

        List<Assignment> assignments = assignmentRepository.findByEmpId(empId);
        System.out.println("📄 Total assignments found: " + assignments.size());

        Set<Project> uniqueProjects = assignments.stream()
                                                 .map(Assignment::getProject)
                                                 .collect(Collectors.toSet());

        System.out.println("📦 Unique projects extracted: " + uniqueProjects.size());

        List<ProjectDTO> projectDTOList = new ArrayList<>();

        for (Project project : uniqueProjects) {
            System.out.println("➡️ Mapping project: " + project.getId() + " - " + project.getName());

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

            // 🔁 Fetch employee names
            List<String> employeeNames = project.getAssignments()
                                                .stream()
                                                .map(Assignment::getEmpName)
                                                .collect(Collectors.toList());

            System.out.println("👥 Team Members for project ID " + project.getId() + ": " + employeeNames);

            dto.setAssignedEmployeeNames(employeeNames);

            projectDTOList.add(dto);
            System.out.println("✅ ProjectDTO added for project: " + dto.getName());
        }

        System.out.println("✅ Total ProjectDTOs created: " + projectDTOList.size());

        return projectDTOList;
    }



}