package com.phegondev.usersmanagementsystem.serviceimpl.useraccess;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phegondev.usersmanagementsystem.dto.useraccess.MapUserRequestDto;
import com.phegondev.usersmanagementsystem.dto.useraccess.UserActionDTO;
import com.phegondev.usersmanagementsystem.entity.OurUsers;
import com.phegondev.usersmanagementsystem.entity.useraccess.Action;
import com.phegondev.usersmanagementsystem.entity.useraccess.Role;
import com.phegondev.usersmanagementsystem.entity.useraccess.UserActionMapping;
import com.phegondev.usersmanagementsystem.exceptions.useraccess.UserNotFoundException;
import com.phegondev.usersmanagementsystem.repository.UsersRepo;
import com.phegondev.usersmanagementsystem.repository.useraccess.ActionRepo;
import com.phegondev.usersmanagementsystem.repository.useraccess.RoleRepo;
import com.phegondev.usersmanagementsystem.repository.useraccess.UserActionMappingRepository;
import com.phegondev.usersmanagementsystem.service.useraccess.UserService;



@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
	
	   private final UsersRepo usersRepo;
	   private final ActionRepo actionRepo;
	   private final RoleRepo roleRepo;
	   private final UserActionMappingRepository userActionMappingRepository;

	    public UserServiceImpl(UsersRepo usersRepo,
	                           ActionRepo actionRepo,
	                           RoleRepo roleRepo,
	                           UserActionMappingRepository userActionMappingRepository) {
	        this.usersRepo = usersRepo;
	        this.actionRepo = actionRepo;
	        this.roleRepo = roleRepo;
	        this.userActionMappingRepository = userActionMappingRepository;
	    }

	@Override
	public List<UserActionDTO> getUserActions(Integer id) {
		OurUsers user = usersRepo.findById(id)
		            .orElseThrow(() -> new UserNotFoundException("Employee not found with id: " + id));
		
		 List<UserActionMapping> userActionMappingList = user.getActionList();
		 
		 return userActionMappingList.stream()
		            .map(mapping -> new UserActionDTO(		     
		                    mapping.getRole().getRoleId(),
		                    mapping.getAction().getActionId()
		            ))
		            .collect(Collectors.toList());
	}

	@Override
	public boolean mapActionsToUser(MapUserRequestDto request) {

		OurUsers user = usersRepo.findById(request.getUserId())
		            .orElseThrow(() -> new UserNotFoundException("User not found with userId: " + request.getUserId()));

		    userActionMappingRepository.deleteByUser(user);

		    List<UserActionMapping> actions = request.getRoleActionIds().stream().map(id -> {
		        String[] tokens = id.split("_");
		       
		        Role role = roleRepo.findById(Long.parseLong(tokens[0]))
		                .orElseThrow(() -> new UserNotFoundException("Role not found with id: " + tokens[0]));
		        Action action = actionRepo.findById(Long.parseLong(tokens[1]))
		                .orElseThrow(() -> new UserNotFoundException("Action not found with id: " + tokens[1]));

		        UserActionMapping mapping = new UserActionMapping();		       
		        mapping.setRole(role);
		        mapping.setAction(action);
		        mapping.setUser(user);
		        return mapping;
		    }).collect(Collectors.toList());

		    userActionMappingRepository.saveAll(actions);
		    user.setActionList(actions);

		    return true;
	}

}
