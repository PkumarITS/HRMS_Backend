package com.phegondev.usersmanagementsystem.serviceimpl.useraccess;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phegondev.usersmanagementsystem.dto.useraccess.ActionDTO;
import com.phegondev.usersmanagementsystem.entity.useraccess.Action;
import com.phegondev.usersmanagementsystem.entity.useraccess.Role;
import com.phegondev.usersmanagementsystem.entity.useraccess.UserActionMapping;
import com.phegondev.usersmanagementsystem.exceptions.useraccess.ActionAlreadyExistsException;
import com.phegondev.usersmanagementsystem.exceptions.useraccess.ActionDeletionException;
import com.phegondev.usersmanagementsystem.exceptions.useraccess.ActionNotFoundException;
import com.phegondev.usersmanagementsystem.repository.useraccess.ActionRepo;
import com.phegondev.usersmanagementsystem.repository.useraccess.RoleRepo;
import com.phegondev.usersmanagementsystem.repository.useraccess.UserActionMappingRepository;
import com.phegondev.usersmanagementsystem.service.useraccess.ActionService;



@Service
@Transactional(rollbackFor = Exception.class)
public class ActionServiceImpl implements ActionService {

    private final ActionRepo actionRepository;
    private final RoleRepo roleRepository;
    private final UserActionMappingRepository userActionMappingRepository;

    public ActionServiceImpl(ActionRepo actionRepository, RoleRepo roleRepository, UserActionMappingRepository userActionMappingRepository) {
        this.actionRepository = actionRepository;
        this.roleRepository = roleRepository;
        this.userActionMappingRepository = userActionMappingRepository;
    }

    public void createAction(ActionDTO actionDTO) {
        if (actionRepository.existsByActionName(actionDTO.getActionName())) {
            throw new ActionAlreadyExistsException("Action with name " + actionDTO.getActionName() + " already exists!");
        }

        String originalName = actionDTO.getActionName();
        String alias = originalName.trim().toUpperCase().replace(" ", "_");

        System.out.println("Original Name: " + originalName);
        System.out.println("Generated Alias: " + alias);

        Action action = new Action();
        action.setActionName(originalName);
        action.setAlias(alias);
        action.setDescription(actionDTO.getDescription());
        actionRepository.save(action);
    }


    @Override
    public List<ActionDTO> getAllActions() {
        List<Action> actions = actionRepository.findAll();
        return actions.stream()
                .map(action -> new ActionDTO(
                        action.getActionId(),
                        action.getActionName(),
                        action.getAlias(),
                        action.getDescription()))
                .collect(Collectors.toList());
    }

    @Override
    public ActionDTO getActionById(Long actionId) {
        Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new ActionNotFoundException("Action not found with actionId: " + actionId));
        return new ActionDTO(
                action.getActionId(),
                action.getActionName(),
                action.getAlias(),
                action.getDescription());
    }

    @Override
    public ActionDTO updateAction(Long actionId, ActionDTO actionDTO) {
        Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new ActionNotFoundException("Action not found with actionId: " + actionId));

        action.setActionName(actionDTO.getActionName());
        action.setDescription(actionDTO.getDescription());
        Action updatedAction = actionRepository.save(action);

        return new ActionDTO(
                updatedAction.getActionId(),
                updatedAction.getActionName(),
                updatedAction.getAlias(),
                updatedAction.getDescription());
    }

    @Override
    public void deleteActionById(Long id) {
        System.out.println("Fetching Action with ID: " + id);
        Action action = actionRepository.findById(id).orElseThrow(() -> 
            new ActionNotFoundException("Action not found with ID: " + id));

        System.out.println("Checking associations for Action ID: " + id);

   
        List<Role> associatedRoles = roleRepository.findRolesByActionId(id);
        List<String> roleNames = associatedRoles.stream()
                                                 .map(Role::getRoleName)
                                                 .toList();
        if (!roleNames.isEmpty()) {
            System.out.println("Associated Roles for Action ID " + id + ": " + roleNames);
        } else {
            System.out.println("No associated roles for Action ID: " + id);
        }

     
        List<UserActionMapping> associatedUsers = userActionMappingRepository.findByActionId(id);
        List<String> usernames = associatedUsers.stream()
                                                 .map(user -> user.getUser().getName())
                                                 .toList();
        if (!usernames.isEmpty()) {
            System.out.println("Associated Users for Action ID " + id + ": " + usernames);
        } else {
            System.out.println("No associated users for Action ID: " + id);
        }

     
        if (!roleNames.isEmpty() || !usernames.isEmpty()) {
            String message = "Please remove mapping before delete Action as it is associated with ";
            if (!roleNames.isEmpty() && usernames.isEmpty()) {
                message += "roles.";
            } else if (roleNames.isEmpty() && !usernames.isEmpty()) {
                message += "users.";
            } else {
                message += "roles and users.";
            }
            System.out.println("Deletion blocked for Action ID " + id + ". " + message);
            throw new ActionDeletionException(message, roleNames, usernames);
        }

    
        System.out.println("Deleting Action with ID: " + id);
        actionRepository.delete(action);
        System.out.println("Action deleted successfully for ID: " + id);
    }

	@Override
	public Action createActionIfNotExist(String name) {
		
		if(actionRepository.existsByActionName(name)) {
			return actionRepository.findByActionName(name);
		}
		  Action newAction = new Action();
          newAction.setActionName(name);
          newAction.setAlias("ADMIN");
          newAction.setDescription("Action for admin tasks");
          actionRepository.save(newAction);
          System.out.println("Default action created.");
          return newAction;	
	}
	
	
}

