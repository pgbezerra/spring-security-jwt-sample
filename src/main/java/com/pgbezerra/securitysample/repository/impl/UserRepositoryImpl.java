package com.pgbezerra.securitysample.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.pgbezerra.securitysample.model.entity.User;
import com.pgbezerra.securitysample.model.enums.Role;
import com.pgbezerra.securitysample.repository.UserRepository;

@Repository
public class UserRepositoryImpl implements UserRepository {
	
	private static final List<User> users = new ArrayList<>();
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	public UserRepositoryImpl(BCryptPasswordEncoder pe) {
		User u1 = new User();
		u1.setId(1L);
		u1.setEmail("bob@gmail.com");
		u1.setPassword(pe.encode("bob123"));
		u1.getRoles().add(Role.ROLE_USER);
		
		User u2 = new User();
		u2.setId(2L);
		u2.setEmail("paulo@gmail.com");
		u2.setPassword(pe.encode("paulo123"));
		u2.getRoles().add(Role.ROLE_ADMIN);
		
		User u3 = new User();
		u3.setId(3L);
		u3.setEmail("camila@gmail.com");
		u3.setPassword(pe.encode("camila123"));
		u3.getRoles().addAll(Arrays.asList(Role.ROLE_USER, Role.ROLE_ADMIN));
		
		users.addAll(Arrays.asList(u1, u2, u3));
	}
	

	@Override
	public User insert(User user) {
		user.setPassword(pe.encode(user.getPassword()));
		user.setId(users.get(users.size()-1).getId() + 1L);
		users.add(user);
		return user;
	}

	@Override
	public List<User> findaAll() {
		return users;
	}

	@Override
	public User findById(Long id) {
		return users.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
	}

	@Override
	public boolean update(User user) {
		User oldUser = findById(user.getId());
		updateData(oldUser, user);
		return true;
	}

	private void updateData(User oldUser, User user) {
		oldUser.setEmail(user.getEmail());
		oldUser.setPassword(pe.encode(user.getPassword()));
	}

	@Override
	public boolean deleteById(Long id) {
		User user = findById(id);
		users.remove(user);
		return true;
	}

	@Override
	public User findByEmail(String email) {
		return users.stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
	}

}
