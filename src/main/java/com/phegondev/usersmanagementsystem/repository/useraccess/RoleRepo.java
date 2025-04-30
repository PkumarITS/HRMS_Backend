package com.phegondev.usersmanagementsystem.repository.useraccess;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.phegondev.usersmanagementsystem.entity.useraccess.Role;


public interface RoleRepo extends JpaRepository<Role, Long>{
	
	 boolean existsByRoleName(String roleName);
	 
	 @Query(value = "SELECT r.* FROM role r JOIN role_action_mapping ram ON r.role_id = ram.role_id WHERE ram.action_id = :actionId", nativeQuery = true)
	 List<Role> findRolesByActionId(@Param("actionId") Long actionId);
	 
	    @Query(value = "SELECT action_name FROM action a JOIN role_action_mapping ram ON a.action_id = ram.action_id WHERE ram.role_id = :roleId", nativeQuery = true)
	    List<String> findActioNamesByRoleId(@Param("roleId") Long roleId);
	    
	    @Query(value = "SELECT c.category_name FROM category c " +
	               "JOIN category_role_mapping crm ON c.category_id = crm.category_id " +
	               "WHERE crm.role_id = :roleId", nativeQuery = true)
	List<String> findCategoryNamesByRoleId(@Param("roleId") Long roleId);
	    
	  Role findByRoleName(String name);




}
