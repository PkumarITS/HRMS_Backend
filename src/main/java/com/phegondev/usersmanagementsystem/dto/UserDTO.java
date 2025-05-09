package com.phegondev.usersmanagementsystem.dto;



public class UserDTO {
	  
	private Integer userId;
    private String email;    
    private String name;
    private String city;
    private String role;
    private String empId;
    
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}


	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	
	
    public UserDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserDTO(Integer userId, String email, String name, String city, String role, String empId) {
        this.userId = userId;
		this.email = email;
        this.name = name;
        this.city = city;
        this.role = role;
        this.empId = empId;
    }
	
	@Override
	public String toString() {
		return "UserDTO [email=" + email + ", name=" + name + ", city=" + city + ", role=" + role + ", empId=" + empId
				+ "]";
	}
    
    
    
    

}
