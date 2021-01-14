package com.pgbezerra.securitysample.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.pgbezerra.securitysample.model.entity.User;

public interface UserService extends UserDetailsService {
	
	default User authenticated() {
		try {
			return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch(Exception e) {
			return null;
		}
	}

}
