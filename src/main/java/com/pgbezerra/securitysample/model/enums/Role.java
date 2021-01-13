package com.pgbezerra.securitysample.model.enums;

public enum Role {

	USER("ROLE_USER"),
	ADMIN("ROLE_ADMIN");
	
	private String roleName;
	
	private Role(String roleName) {
		this.roleName = roleName;
	}
	
	public String getRoleName() {
		return this.roleName;
	}
}
