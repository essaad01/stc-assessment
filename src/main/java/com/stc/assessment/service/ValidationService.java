package com.stc.assessment.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stc.assessment.exception.ResourceNotFoundException;
import com.stc.assessment.model.File;
import com.stc.assessment.model.Folder;
import com.stc.assessment.model.Item;
import com.stc.assessment.model.Permission;
import com.stc.assessment.model.PermissionGroup;
import com.stc.assessment.model.Space;
import com.stc.assessment.repository.FileRepository;
import com.stc.assessment.repository.FolderRepository;
import com.stc.assessment.repository.ItemRepository;
import com.stc.assessment.repository.PermissionGroupRepository;
import com.stc.assessment.repository.PermissionRepository;
import com.stc.assessment.repository.SpaceRepository;
import com.stc.assessment.utils.PermissionLevel;

@Service
public class ValidationService {
	
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private PermissionGroupRepository permissionGroupRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	@Autowired
	private SpaceRepository spaceRepository;
	@Autowired
	private FolderRepository folderRepository;
	@Autowired
	private FileRepository fileRepository;
	
	public PermissionGroup validateGroupId(UUID groupId) {
		if(groupId == null) throw new ResourceNotFoundException("Group Id must not be null");
		return permissionGroupRepository.findById(groupId)
		        .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
	}
	
	public Space validateSpaceId(UUID spaceId) {
		if(spaceId == null) throw new ResourceNotFoundException("Space Id must not be null");
		return spaceRepository.findById(spaceId)
		        .orElseThrow(() -> new ResourceNotFoundException("Space not found"));
	}
	
	public Item validateItemId(UUID itemId) {
		if(itemId == null) throw new ResourceNotFoundException("Item Id must not be null");
		return itemRepository.findById(itemId).orElseThrow(()-> new ResourceNotFoundException("Item not found"));
	}
	
	public boolean hasPermission(UUID permissionGroupId, String userEmail, PermissionLevel permissionLevel) {
		List<Permission> userPermissions = permissionRepository.findByUserEmail(userEmail);
		return userPermissions.stream()
				.anyMatch(permission-> 
				permission.getPermissionLevel().equals(permissionLevel)
				&& permission.getGroup().getId() == permissionGroupId);
	}
	
	public boolean isFolder(UUID id) {
		return folderRepository.existsById(id);
	}
	
	public boolean isSpace(UUID id) {
		return spaceRepository.existsById(id);
	}
	
	public Folder getFolder(UUID id) {
		return folderRepository.findById(id)
		        .orElseThrow(() -> new ResourceNotFoundException("Folder not found"));
	}

	public File getFile(String fileName) {
		return fileRepository.findFirstByName(fileName).orElseThrow(()-> new ResourceNotFoundException("File not found"));
	}

}
