package com.phegondev.usersmanagementsystem.dto.useraccess;

public class UserActionDTO {
	
	 private Long categoryId;
	 private Long roleId;
	 private Long actionId;
	 
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getActionId() {
		return actionId;
	}
	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}
	
	public UserActionDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UserActionDTO(Long categoryId, Long roleId, Long actionId) {
		super();
		this.categoryId = categoryId;
		this.roleId = roleId;
		this.actionId = actionId;
	}
	
	@Override
	public String toString() {
		return "UserActionDTO [categoryId=" + categoryId + ", roleId=" + roleId + ", actionId=" + actionId + "]";
	}
	 
	 
	 

}
