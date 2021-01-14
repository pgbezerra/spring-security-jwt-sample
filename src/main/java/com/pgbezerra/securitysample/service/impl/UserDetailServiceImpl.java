package com.pgbezerra.securitysample.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pgbezerra.securitysample.model.entity.User;
import com.pgbezerra.securitysample.repository.UserRepository;
import com.pgbezerra.securitysample.service.UserService;

@Service
public class UserDetailServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		if(Objects.nonNull(user))
			return user;
		throw new UsernameNotFoundException(String.format("User %s not found", email));
	}

}
