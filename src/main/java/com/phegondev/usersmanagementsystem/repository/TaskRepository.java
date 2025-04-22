package com.phegondev.usersmanagementsystem.repository;

import com.phegondev.usersmanagementsystem.dto.TaskNameIdDTO;
import com.phegondev.usersmanagementsystem.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByStatus(String status);
    
    @Query("SELECT t FROM Task t JOIN t.project p JOIN p.assignments a WHERE a.empId = :empId")
    List<Task> findTasksByAssignedEmpId(@Param("empId") String empId);
    
    @Query("SELECT new com.phegondev.usersmanagementsystem.dto.TaskNameIdDTO(t.id, t.title) " +
           "FROM Task t WHERE t.project.id = :projectId")
     List<TaskNameIdDTO> findTaskIdAndTitleByProjectId(@Param("projectId") Long projectId);

}