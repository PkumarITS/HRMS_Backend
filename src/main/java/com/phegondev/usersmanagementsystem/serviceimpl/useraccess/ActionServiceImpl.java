package com.phegondev.usersmanagementsystem.serviceimpl.useraccess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phegondev.usersmanagementsystem.dto.useraccess.ActionDTO;
import com.phegondev.usersmanagementsystem.entity.OurUsers;
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
    public void createActionIfNotExist() {

        System.out.println("Checking and creating actions...");

        List<Action> predefinedActions = List.of(
                new Action("Add Employee", "ADD_EMPLOYEE", "Add a new employee"),
                new Action("update employee", "UPDATE_EMPLOYEE", "updating employee"),
                new Action("delete employee", "DELETE_EMPLOYEE", "deleting employee"),
                new Action("fetch employee", "FETCH_EMPLOYEE", "fetching all employee data"),
                new Action("create projects", "CREATE_PROJECTS", "creating a projects"),
                new Action("View Timesheet", "VIEW_TIMESHEET", "hr executive have an access to view timesheet"),
                new Action("Create Leave management", "CREATE_LEAVE_MANAGEMENT", "leave management creation"),
                new Action("VIEW LIST ROLES", "VIEW_LIST_ROLES", "fyhj"),
                new Action("Create Actions", "CREATE_ACTIONS", "actions creation"),
                new Action("View List Actions", "VIEW_LIST_ACTIONS", "view list of actions"),
                new Action("View User Management", "VIEW_USER_MANAGEMENT", "view users and user mapping to particular role"),
                new Action("View Admin Dashboard", "VIEW_ADMIN_DASHBOARD", "view dashboard"),
                new Action("Manage Employee", "MANAGE_EMPLOYEE", "Employee management"),
                new Action("Manage Project", "MANAGE_PROJECT", "project management (View project dashboard)"),
                new Action("Manage Task", "MANAGE_TASK", "Admin has an access to manage all the task"),
                new Action("Manage Timesheet", "MANAGE_TIMESHEET", "Timesheet management"),
                new Action("Manage Leave", "MANAGE_LEAVE", "Leave management"),
                new Action("Manage Leave Types", "MANAGE_LEAVE_TYPES", "Admin has an access to manage Types of leave"),
                new Action("Manage Leave Balance", "MANAGE_LEAVE_BALANCE", "manage leave balance"),
                new Action("Manage Holiday", "MANAGE_HOLIDAY", "holiday management"),
                new Action("Manage Attendance", "MANAGE_ATTENDANCE", "Attendance management"),
                new Action("View Profile", "VIEW_PROFILE", "profile view"),
                new Action("Create Role", "CREATE_ROLE", "role creation"),
                new Action("View User Dashboard", "VIEW_USER_DASHBOARD", "user dashboard"),
                new Action("View Project", "VIEW_PROJECT", "user can see their project"),
                new Action("View Task", "VIEW_TASK", "user can see their assign task"),
                new Action("View Attendance", "VIEW_ATTENDANCE", "user can see attendance"),
                new Action("View Leave", "VIEW_LEAVE", "user can see leave management"),
                new Action("View Leave balance", "VIEW_LEAVE_BALANCE", "user can check their leave balance"),
                new Action("View Holiday", "VIEW_HOLIDAY", "user can see their holiday")
            );
        
        for (Action action : predefinedActions) {
            if (!actionRepository.existsByAlias(action.getAlias())){
                actionRepository.save(action);
                System.out.println("Created action: " + action.getActionName());
            }
        }
    }

    @Override
    public void mapDefaultActionsToRoles() {
        System.out.println("Mapping default actions to roles...");

        Role adminRole = roleRepository.findByRoleName("ADMIN");
        Role hrRole = roleRepository.findByRoleName("HR");
        Role userRole = roleRepository.findByRoleName("USER");
        Role supervisorRole = roleRepository.findByRoleName("SUPERVISOR");

        List<Action> allActions = actionRepository.findAll();

        // Admin gets all actions
        adminRole.setActionList(allActions);
        System.out.println("Mapped all actions to ADMIN.");

        // HR specific actions
        List<String> hrAliases = List.of("MANAGE_EMPLOYEE", "MANAGE_LEAVE", "MANAGE_LEAVE_TYPES", "MANAGE_LEAVE_BALANCE", "MANAGE_HOLIDAY", "MANAGE_ATTENDANCE", "VIEW_PROFILE");
        List<Action> hrActions = allActions.stream().filter(a -> hrAliases.contains(a.getAlias())).collect(Collectors.toList());
        hrRole.setActionList(hrActions);
        System.out.println("Mapped actions to HR.");

        // Supervisor specific actions
        List<String> supervisorAliases = List.of("MANAGE_PROJECT", "MANAGE_TASK", "MANAGE_TIMESHEET", "MANAGE_LEAVE", "MANAGE_LEAVE_TYPES", "MANAGE_LEAVE_BALANCE", "MANAGE_ATTENDANCE", "VIEW_PROFILE", "VIEW_HOLIDAY");
        List<Action> supervisorActions = allActions.stream().filter(a -> supervisorAliases.contains(a.getAlias())).collect(Collectors.toList());
        supervisorRole.setActionList(supervisorActions);
        System.out.println("Mapped actions to SUPERVISOR.");

        // User specific actions
        List<String> userAliases = List.of("VIEW_TIMESHEET", "VIEW_PROFILE", "VIEW_USER_DASHBOARD", "VIEW_PROJECT", "VIEW_TASK", "VIEW_ATTENDANCE", "VIEW_LEAVE", "VIEW_LEAVE_BALANCE", "VIEW_HOLIDAY");
        List<Action> userActions = allActions.stream().filter(a -> userAliases.contains(a.getAlias())).collect(Collectors.toList());
        userRole.setActionList(userActions);
        System.out.println("Mapped actions to USER.");

        roleRepository.save(adminRole);
        roleRepository.save(hrRole);
        roleRepository.save(supervisorRole);
        roleRepository.save(userRole);

        System.out.println("All role-action mappings saved.");
    }
	
	public void mapAllActionsToAdminUser(OurUsers adminUser) {
		
		 System.out.println("Mapping all actions to admin user...");

	    // 2. Get ADMIN role
	    Role adminRole = roleRepository.findByRoleName("ADMIN");
	
	    // 3. Fetch all actions
	    List<Action> allActions = actionRepository.findAll();

	    // 4. Create mappings
	    List<UserActionMapping> mappings = new ArrayList<>();
	    for (Action action : allActions) {
	    	
	            UserActionMapping mapping = new UserActionMapping();
	            mapping.setUser(adminUser);
	            mapping.setRole(adminRole);
	            mapping.setAction(action);
	            mappings.add(mapping);
	        
	    }

	    // 5. Save all mappings
	    userActionMappingRepository.saveAll(mappings);
	    System.out.println("Mapped " + mappings.size() + " actions to admin user successfully.");
	}

	
	
}

