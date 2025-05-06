package com.phegondev.usersmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.phegondev.usersmanagementsystem.entity.OurUsers;
import lombok.Data;

import java.util.List;
import java.util.Map;

//@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String city;
    private String role;
    private String email;
    private String empId;
    private String password;
    private OurUsers ourUsers;
    private List<OurUsers> ourUsersList;
    private Map<String, Object> employeeData;
    private List<String> actions;
    private List<UserDTO> userDTOList;
    
    

    
    
	public List<UserDTO> getUserDTOList() {
		return userDTOList;
	}
	public void setUserDTOList(List<UserDTO> userDTOList) {
		this.userDTOList = userDTOList;
	}
	public List<String> getActions() {
		return actions;
	}
	public void setActions(List<String> actions) {
		this.actions = actions;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public OurUsers getOurUsers() {
		return ourUsers;
	}
	public void setOurUsers(OurUsers ourUsers) {
		this.ourUsers = ourUsers;
	}
	public List<OurUsers> getOurUsersList() {
		return ourUsersList;
	}
	public void setOurUsersList(List<OurUsers> ourUsersList) {
		this.ourUsersList = ourUsersList;
	}
	public Map<String, Object> getEmployeeData() {
		return employeeData;
	}
	public void setEmployeeData(Map<String, Object> employeeData) {
		this.employeeData = employeeData;
	}
	@Override
	public String toString() {
		return "ReqRes [statusCode=" + statusCode + ", error=" + error + ", message=" + message + ", token=" + token
				+ ", refreshToken=" + refreshToken + ", expirationTime=" + expirationTime + ", name=" + name + ", city="
				+ city + ", role=" + role + ", email=" + email + ", empId=" + empId + ", password=" + password
				+ ", actions=" + actions + "]";
	}
    
    
}