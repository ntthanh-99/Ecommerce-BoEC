package com.tienthanh.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienthanh.domain.security.Role;

public interface RoleRepository extends CrudRepository<Role, Integer>{
	Role findByName(String name);
}
