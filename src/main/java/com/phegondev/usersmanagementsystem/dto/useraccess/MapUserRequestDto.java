package com.phegondev.usersmanagementsystem.dto.useraccess;

import java.util.List;

public class MapUserRequestDto {
	
	 private Long userId;
	 private List<String> categoryRoleActionIds;
	 
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public List<String> getCategoryRoleActionIds() {
		return categoryRoleActionIds;
	}
	public void setCategoryRoleActionIds(List<String> categoryRoleActionIds) {
		this.categoryRoleActionIds = categoryRoleActionIds;
	}
	
	@Override
	public String toString() {
		return "MapUserRequestDto [userId=" + userId + ", categoryRoleActionIds=" + categoryRoleActionIds + "]";
	}
	 
	 

}
