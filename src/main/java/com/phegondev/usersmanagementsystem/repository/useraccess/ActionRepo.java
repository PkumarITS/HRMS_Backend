package com.phegondev.usersmanagementsystem.repository.useraccess;

import org.springframework.data.jpa.repository.JpaRepository;
import com.phegondev.usersmanagementsystem.entity.useraccess.Action;

public interface ActionRepo extends JpaRepository<Action, Long> {
	
	 boolean existsByActionName(String actionName);
	 Action findByActionName(String actionName);

}
