package com.phegondev.usersmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "ourusers")
//@Data
public class OurUsers implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(unique = true)
    private String email;
    
    private String name;
    private String password;
    private String city;
    private String role;

    @Column(name = "emp_id", unique = true)
    private String empId;
    
    @Column(name = "blacklisted_token")
    private String blacklistedToken;

    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id") 
    // private personal personal;
    private Employee employee;

    public String getEmployeeId() {
        // String e_id=this.personal.getEmpId();
        // Long p_id=this.personal.getId();
        if (this.employee != null && this.employee.getPersonal() != null) {
            return this.employee.getPersonal().getEmpId();
        }
        return this.empId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getBlacklistedToken() {
		return blacklistedToken;
	}

	public void setBlacklistedToken(String blacklistedToken) {
		this.blacklistedToken = blacklistedToken;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public String toString() {
		return "OurUsers [id=" + id + ", email=" + email + ", name=" + name + ", password=" + password + ", city="
				+ city + ", role=" + role + ", empId=" + empId + ", blacklistedToken=" + blacklistedToken + "]";
	}
	
	
	
	
    
    
}