package com.phegondev.usersmanagementsystem.controller.useraccess;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phegondev.usersmanagementsystem.dto.useraccess.MapUserRequestDto;
import com.phegondev.usersmanagementsystem.dto.useraccess.UserActionDTO;
import com.phegondev.usersmanagementsystem.service.UsersManagementService;


@RestController
@RequestMapping("/users")
public class UserController {
	
    @Autowired
    private UsersManagementService usersManagementService;
	
    @PostMapping("/mapActionsToUser")
    public ResponseEntity<?> mapActionsToUser(@RequestBody MapUserRequestDto request) {
    	usersManagementService.mapActionsToUser(request);	        
       return ResponseEntity.ok("Mapping updated successfully");
      
    }
    
    @GetMapping("{id}/actions")
    public ResponseEntity<List<UserActionDTO>> getUserActions(@PathVariable Long id ) {	               
       return ResponseEntity.ok(usersManagementService.getUserActions(id));
      
    }

    

}
