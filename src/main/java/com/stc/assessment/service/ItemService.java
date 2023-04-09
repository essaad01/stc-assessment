package com.stc.assessment.service;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.stc.assessment.communication.FileDto;
import com.stc.assessment.communication.FolderDto;
import com.stc.assessment.communication.SpaceDto;
import com.stc.assessment.exception.ResourceNotFoundException;
import com.stc.assessment.model.File;
import com.stc.assessment.model.Folder;
import com.stc.assessment.model.Item;
import com.stc.assessment.model.Permission;
import com.stc.assessment.model.PermissionGroup;
import com.stc.assessment.model.Space;
import com.stc.assessment.repository.FileRepository;
import com.stc.assessment.repository.FolderRepository;
import com.stc.assessment.repository.PermissionGroupRepository;
import com.stc.assessment.repository.PermissionRepository;
import com.stc.assessment.repository.SpaceRepository;
import com.stc.assessment.utils.ItemType;
import com.stc.assessment.utils.PermissionLevel;

@Service
public class ItemService {
	
	@Autowired
	private FileService fileService;
	@Autowired
	private ValidationService validationService;
	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private PermissionGroupRepository permissionGroupRepository;
	@Autowired
	private SpaceRepository spaceRepository;
	@Autowired
	private FolderRepository folderRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	
	public PermissionGroup createPermissionGroup() {
		PermissionGroup adminGroup = new PermissionGroup("admin");
		List<Permission> permissions = new ArrayList<>();
		permissions.add(new Permission("viewUser@stc.com", PermissionLevel.VIEW));
		permissions.add(new Permission("editUser@stc.com", PermissionLevel.EDIT));
	    permissionRepository.saveAll(permissions);
	    adminGroup.setPermissions(permissions);
	    permissionGroupRepository.save(adminGroup);
		return adminGroup;
	}
	
	public SpaceDto saveSpace(SpaceDto spaceDto) {
		PermissionGroup adminGroup = validationService.validateGroupId(spaceDto.getGroupId());
		Space space = new Space();
		space.setName(spaceDto.getName());
		space.setType(ItemType.SPACE);
		space.setPermissionGroup(adminGroup);
	    spaceRepository.save(space);
	    spaceDto.setId(space.getId());
	    spaceDto.setGroupId(space.getPermissionGroup().getId());
		return spaceDto;
	}

	

	public FolderDto createFolder(UUID spaceId, String userEmail, FolderDto folderDto) throws AccessDeniedException {
		Space space = validationService.validateSpaceId(spaceId);
		PermissionGroup permissionGroup = validationService.validateGroupId(space.getPermissionGroup().getId());
		if(!validationService.hasPermission(permissionGroup.getId(), userEmail, PermissionLevel.EDIT))
			throw new AccessDeniedException("User does not have permission");
		Folder folder= new Folder();
		folder.setSpace(space);
		folder.setName(folderDto.getName());
		folder.setPermissionGroup(space.getPermissionGroup());
		folder.setType(ItemType.FOLDER);
		folderRepository.save(folder);
		folderDto.setId(folder.getId());
		return folderDto;
	}

	public File createFile(String userEmail, FileDto fileDto) throws IOException, AccessDeniedException {
		PermissionGroup permissionGroup = validationService.validateGroupId(fileDto.getGroupId());
		Item item = validationService.validateItemId(fileDto.getItemId());
		if(!validationService.hasPermission(item.getPermissionGroup().getId(), userEmail, PermissionLevel.EDIT))
			throw new AccessDeniedException("User does not have permission");
		if(fileDto.getFile() == null)
			throw new ResourceNotFoundException("File not found");
		String url = fileService.uploadFile(item, fileDto.getFile());
		File file = new File();
		file.setItem(item);
		file.setName(fileDto.getFile().getOriginalFilename());
		file.setPermissionGroup(permissionGroup);
		file.setType(ItemType.FILE);
		file.setBinary(url);
		fileRepository.save(file);
		return file;
	}

	public Map<String, Object> getFileMetadata(UUID fileId, String userEmail) throws AccessDeniedException {
		Map<String, Object> metadata = fileRepository.getFileMetadata(fileId);
		if(metadata.isEmpty())
			throw new ResourceNotFoundException("File not found");
		UUID groupId = null;
		if(metadata.get("permissionGroupId") instanceof UUID)
			 groupId = ((UUID) metadata.get("permissionGroupId"));
		PermissionGroup permissionGroup = validationService.validateGroupId(groupId);
		if(!validationService.hasPermission(permissionGroup.getId(), userEmail, PermissionLevel.VIEW))
			throw new AccessDeniedException("User does not have permission");
		return metadata;
	}

	public Resource downloadFile(String fileName, String path, String userEmail) throws AccessDeniedException {
		File file = validationService.getFile(fileName);
		PermissionGroup permissionGroup = validationService.validateGroupId(file.getPermissionGroup().getId());
		if(!validationService.hasPermission(permissionGroup.getId(), userEmail, PermissionLevel.VIEW))
			throw new AccessDeniedException("User does not have permission");
		return fileService.loadFileAsResource(fileName, path);

	}

	

}
