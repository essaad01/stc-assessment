package com.stc.assessment.controller;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stc.assessment.communication.FileDto;
import com.stc.assessment.communication.FolderDto;
import com.stc.assessment.communication.SpaceDto;
import com.stc.assessment.exception.ResourceNotFoundException;
import com.stc.assessment.model.File;
import com.stc.assessment.model.PermissionGroup;
import com.stc.assessment.service.ItemService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class ItemController {
	
	@Autowired
    private ItemService itemService;
	
	@PostMapping("/permissionGroup")
	public ResponseEntity<PermissionGroup> createPermissionGroup() {
		PermissionGroup permissionGroup = itemService.createPermissionGroup();
		return ResponseEntity.status(HttpStatus.CREATED).body(permissionGroup);
	}
	@PostMapping("/spaces")
	public ResponseEntity<?> createSpace(@RequestBody SpaceDto space) {
		try {
			SpaceDto createdSpace = itemService.saveSpace(space);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdSpace);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
	}
	
	@PostMapping("/spaces/{spaceId}/folders")
	public ResponseEntity<?> createFolder(@PathVariable UUID spaceId, @RequestBody FolderDto folder,
			HttpServletRequest request) throws AccessDeniedException {
		if(request.getHeader("userEmail") == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("userEmail must not be null");
		String userEmail = request.getHeader("userEmail").toString();
		try {
			FolderDto createdFolder = itemService.createFolder(spaceId, userEmail, folder);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdFolder);
		} catch (ResourceNotFoundException | AccessDeniedException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PostMapping("/files")
	public ResponseEntity<?> createFile(FileDto fileDto, HttpServletRequest request) throws IOException, AccessDeniedException {
		if(request.getHeader("userEmail") == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("userEmail must not be null");
		String userEmail = request.getHeader("userEmail").toString();
		try {
		    File createdFileUrl = itemService.createFile(userEmail, fileDto);
		    return ResponseEntity.status(HttpStatus.OK).body(createdFileUrl);
		} catch (ResourceNotFoundException | AccessDeniedException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping("/files/{fileId}")
    public ResponseEntity<?> getFilesForUser(@PathVariable UUID fileId, HttpServletRequest request) {
		if(request.getHeader("userEmail") == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("userEmail must not be null");
		String userEmail = request.getHeader("userEmail").toString();
		try {
	        Map<String, Object> file = itemService.getFileMetadata(fileId, userEmail);
	        return new ResponseEntity<>(file, HttpStatus.OK);
		} catch (ResourceNotFoundException | AccessDeniedException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
    }
	
	@GetMapping("/download/{space}/{fileName}")
	public ResponseEntity<?> downloadImages(@PathVariable String space, @PathVariable String fileName, HttpServletRequest request) {
		if(request.getHeader("userEmail") == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("userEmail must not be null");
		String userEmail = request.getHeader("userEmail").toString();
		try {
			Resource resource = itemService.downloadFile(fileName, space, userEmail);
			return ResponseEntity.ok()
		    		  .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
		              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
		              .body(resource);
		} catch (ResourceNotFoundException | AccessDeniedException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
  }
	
	@GetMapping("/download/{space}/{folder}/{fileName}")
	public ResponseEntity<?> downloadImages(@PathVariable String space, @PathVariable String folder, @PathVariable String fileName, HttpServletRequest request) {
		if(request.getHeader("userEmail") == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("userEmail must not be null");
		String userEmail = request.getHeader("userEmail").toString();
		try {
			String path = space + "/" + folder;
			Resource resource = itemService.downloadFile(fileName, path, userEmail);
			return ResponseEntity.ok()
		    		  .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
		              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
		              .body(resource);
		} catch (ResourceNotFoundException | AccessDeniedException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
  }
	
}
