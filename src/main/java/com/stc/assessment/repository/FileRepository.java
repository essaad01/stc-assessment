package com.stc.assessment.repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stc.assessment.model.File;

public interface FileRepository extends JpaRepository<File, UUID> {
	
	@Query(value = "SELECT f.id as \"fileId\", i.name, i.type, i.permission_group_id as \"permissionGroupId\" "
			+ "FROM item i "
			+ "INNER JOIN file f "
			+ "ON i.id = f.id "
			+ "WHERE i.id = :fileId" ,nativeQuery = true)
	public Map<String, Object> getFileMetadata(UUID fileId);

	public Optional<File> findFirstByName(String fileName);
}
