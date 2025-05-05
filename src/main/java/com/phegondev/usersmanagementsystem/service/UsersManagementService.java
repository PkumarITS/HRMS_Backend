package com.phegondev.usersmanagementsystem.service;

import com.phegondev.usersmanagementsystem.dto.ReqRes;
import com.phegondev.usersmanagementsystem.entity.Employee;
import com.phegondev.usersmanagementsystem.entity.OurUsers;
import com.phegondev.usersmanagementsystem.repository.UsersRepo;
import com.phegondev.usersmanagementsystem.repository.useraccess.UserActionMappingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsersManagementService {
	
	    private final UsersRepo usersRepo;
	    private final JWTUtils jwtUtils;
	    private final AuthenticationManager authenticationManager;
	    private final PasswordEncoder passwordEncoder;
	    private final EmployeeService employeeService;
	    private final UserActionMappingRepository userActionMappingRepo;

	    @Autowired
	    public UsersManagementService(
	            UsersRepo usersRepo,
	            JWTUtils jwtUtils,
	            AuthenticationManager authenticationManager,
	            PasswordEncoder passwordEncoder,
	            EmployeeService employeeService,
	            UserActionMappingRepository userActionMappingRepo) {
	        this.usersRepo = usersRepo;
	        this.jwtUtils = jwtUtils;
	        this.authenticationManager = authenticationManager;
	        this.passwordEncoder = passwordEncoder;
	        this.employeeService = employeeService;
	        this.userActionMappingRepo = userActionMappingRepo;
	    }
    
    

    // User Registration
    public ReqRes register(ReqRes registrationRequest) {
        System.out.println("Starting registration for email: " + registrationRequest.getEmail() + 
                           ", empId: " + registrationRequest.getEmpId());

        ReqRes resp = new ReqRes();
        try {
            if (usersRepo.existsByEmail(registrationRequest.getEmail())) {
                System.out.println("Email already exists: " + registrationRequest.getEmail());
                resp.setStatusCode(409);
                resp.setMessage("User with this email already exists.");
                return resp;
            }

            if (usersRepo.existsByEmpId(registrationRequest.getEmpId())) {
                System.out.println("Employee ID already registered: " + registrationRequest.getEmpId());
                resp.setStatusCode(409);
                resp.setMessage("Employee ID already registered.");
                return resp;
            }

            if (!employeeService.existsByEmpId(registrationRequest.getEmpId())) {
                System.out.println("No employee found with empId: " + registrationRequest.getEmpId());
                resp.setStatusCode(404);
                resp.setMessage("No employee found with this ID.");
                return resp;
            }

            // Create user
            OurUsers ourUser = new OurUsers();
            ourUser.setEmail(registrationRequest.getEmail());
            ourUser.setCity(registrationRequest.getCity());
            ourUser.setRole(registrationRequest.getRole());
            ourUser.setName(registrationRequest.getName());
            ourUser.setEmpId(registrationRequest.getEmpId());
            ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

            System.out.println("Saving new user: " + ourUser);
            OurUsers ourUsersResult = usersRepo.save(ourUser);
            System.out.println("User saved with ID: " + ourUsersResult.getId());

            if (ourUsersResult.getId() > 0) {
                System.out.println("Attempting to link employee to user...");

                Optional<Employee> employeeOpt = employeeService.getEmployeeByEmpId(registrationRequest.getEmpId());
                if (employeeOpt.isPresent()) {
                    Employee employee = employeeOpt.get();
                    System.out.println("Employee fetched: " + employee.getId());

                 
                    employee.setOurUser(ourUsersResult);
                    ourUsersResult.setEmployee(employee);

                    System.out.println("Saving updated employee with linked user...");
                    employeeService.saveEmployee(employee); 

                    System.out.println("Saving user again to set employee_id...");
                    usersRepo.save(ourUsersResult); 
                    System.out.println("User updated with employee_id: " + employee.getId());
                } else {
                    System.out.println("Unexpected: Employee not found even after existence check.");
                }
            }

            resp.setStatusCode(200);
            resp.setMessage("User registered successfully.");
            resp.setOurUsers(ourUsersResult);
        } catch (Exception e) {
            System.out.println("Exception occurred during registration: " + e.getMessage());
            e.printStackTrace();
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }

        return resp;
    }

    public ReqRes getCompleteProfile(String email) {
        System.out.println("Entered getCompleteProfile with email: " + email);

        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> optionalUser = usersRepo.findByEmail(email);
            if (optionalUser.isEmpty()) {
                System.out.println("No user found with email: " + email);
                throw new RuntimeException("User not found");
            }

            OurUsers user = optionalUser.get();
            System.out.println("User found: " + user);

            if (user.getEmployeeId() == null) {
                System.out.println("User has no linked employee. EmployeeId is null.");
                reqRes.setStatusCode(400);
                reqRes.setMessage("No employee linked to this user");
                return reqRes;
            }

            Employee employee = user.getEmployee();
            System.out.println("Employee fetched: " + employee);

            String userEmpId = user.getEmpId();
            String employeeEmpId = employee != null ? employee.getEmpId() : null;

            System.out.println("User EmpId: " + userEmpId);
            System.out.println("Employee EmpId: " + employeeEmpId);

            if (userEmpId == null || employeeEmpId == null || !userEmpId.equals(employeeEmpId)) {
                System.out.println("Employee ID mismatch.");
                reqRes.setStatusCode(400);
                reqRes.setMessage("Employee ID mismatch. User: " + userEmpId + 
                                ", Employee: " + employeeEmpId);
                return reqRes;
            }

            Map<String, Object> employeeData = new HashMap<>();
            employeeData.put("personal", employee.getPersonal());
            employeeData.put("identification", employee.getIdentification());
            employeeData.put("contact", employee.getContact());
            employeeData.put("work", employee.getWork());
            employeeData.put("report", employee.getReport());

            System.out.println("Employee personal: " + employee.getPersonal());
            System.out.println("Employee identification: " + employee.getIdentification());
            System.out.println("Employee contact: " + employee.getContact());
            System.out.println("Employee work: " + employee.getWork());
            System.out.println("Employee report: " + employee.getReport());

            reqRes.setStatusCode(200);
            reqRes.setMessage("Complete profile fetched successfully");
            reqRes.setOurUsers(user);
            reqRes.setEmployeeData(employeeData);

            System.out.println("Profile fetch successful. Returning response.");
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }

        return reqRes;
    }

    // Login implementation
    public ReqRes login(ReqRes loginRequest) {
        ReqRes response = new ReqRes();

        try {
            System.out.println("Authenticating user with email: " + loginRequest.getEmail());

            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            System.out.println("Authentication successful");

            // Set authenticated user into the security context
            SecurityContextHolder.getContext().setAuthentication(auth);

            var user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            System.out.println("User found: " + user.getUsername() + " (Employee ID : " + user.getEmpId() + ")");

            List<String> actionList = userActionMappingRepo.findActionNamesByUserId(user.getId());
            System.out.println("Actions fetched for user: " + actionList);
            
            var jwt = jwtUtils.generateToken(user, actionList);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            System.out.println("JWT generated: " + jwt);
            System.out.println("Refresh token generated: " + refreshToken);


            response.setActions(actionList);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully logged in");

        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }

        return response;
    }


    // Generate refresh token
    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes response = new ReqRes();

        try {
            String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            OurUsers users = usersRepo.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
            	
                List<String> actionList = userActionMappingRepo.findActionNamesByUserId(users.getId());
                System.out.println("Actions fetched for user: " + actionList);
                var jwt = jwtUtils.generateToken(users, actionList);
                
          

                response.setActions(actionList);

                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hrs");
                response.setMessage("Successfully Refreshed the token");
            }
            response.setStatusCode(200);
            return response;

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    // Get All Users
    public ReqRes getAllUsers() {
        ReqRes reqRes = new ReqRes();

        try {
            List<OurUsers> result = usersRepo.findAll();

            if (!result.isEmpty()) {
                reqRes.setOurUsersList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No Users Found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred : " + e.getMessage());
            return reqRes;
        }
    }

    // Get user by ID
    public ReqRes getUserByID(Integer id) {
        ReqRes reqRes = new ReqRes();

        try {
            OurUsers userById = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
            reqRes.setOurUsers(userById);
            reqRes.setStatusCode(200);
            reqRes.setMessage("User with id '" + id + "' found successfully");

        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage(e.getMessage());
        }
        return reqRes;

    }

    // Delete User
    public ReqRes deleteUser(Integer userId) {
        ReqRes reqRes = new ReqRes();

        try {
            Optional<OurUsers> userOptional = usersRepo.findById(userId);

            if (userOptional.isPresent()) {
                usersRepo.deleteById(userId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }

        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred while deleting user : " + e.getMessage());
        }

        return reqRes;
    }

    // update User
    public ReqRes updateUser(Integer userId, OurUsers updatedUser) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                OurUsers existingUser = userOptional.get();
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setName(updatedUser.getName());
                existingUser.setCity(updatedUser.getCity());
                existingUser.setRole(updatedUser.getRole());

                // Check if password is present in the request
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    // Encode the password and update it
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                OurUsers savedUser = usersRepo.save(existingUser);
                reqRes.setOurUsers(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User updated successfully");
            }

        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred while updating the user : " + e.getMessage());
        }
        return reqRes;
    }

    // Get my info
    public ReqRes getMyInfo(String email) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setOurUsers(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User is not found");
            }

        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting your info : " + e.getMessage());
        }
        return reqRes;
    }

}