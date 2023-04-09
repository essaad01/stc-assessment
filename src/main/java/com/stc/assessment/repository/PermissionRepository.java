package com.stc.assessment.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stc.assessment.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {

	List<Permission> findByUserEmail(String userEmail);
}
