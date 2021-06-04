package com.tienthanh.service;

import com.tienthanh.domain.security.Role;

public interface RoleService {
	Role findByName(String name);
}
