package com.example.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.auth.entity.Permission;
import com.example.auth.entity.Role;

public interface PermissionRepository extends JpaRepository<Permission, Long>{

	Optional<Permission> findByName(String name);

	
	

}
