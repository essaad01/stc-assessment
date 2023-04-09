package com.stc.assessment.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.stc.assessment.exception.ResourceNotFoundException;
import com.stc.assessment.model.Folder;
import com.stc.assessment.model.Item;

@Service
public class FileService {
	
	@Autowired
	private ValidationService validationService;

	public String uploadFile(Item item, MultipartFile file) throws IOException {
		String path;
		if(validationService.isSpace(item.getId()))
			path = item.getName();
		else if(validationService.isFolder(item.getId())) {
			Folder folder = validationService.getFolder(item.getId());
			path = folder.getSpace().getName() + "\\" + folder.getName();
		}
		else path = "uploads";
		String fileName = file.getOriginalFilename();
        byte[] fileBytes = file.getBytes();
        Path filePath = Paths.get(path);
        try {
            Files.createDirectories(filePath);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Could not create the directory where the uploaded files will be stored.");
        }
        Path targetLocation = filePath.resolve(fileName);
        Files.write(targetLocation, fileBytes);
        
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/download/")
                .path(path.replace("\\", "/") + "/")
                .path(fileName)
                .toUriString();
        return fileDownloadUri;
	}
	
	public Resource loadFileAsResource(String fileName, String path) {
        try {
            Path filePath = Paths.get(path).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("File " + fileName + " not found");
            }
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("File " + fileName + " not found");
        }
    }

}
