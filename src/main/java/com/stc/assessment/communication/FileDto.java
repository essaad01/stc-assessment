package com.stc.assessment.communication;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class FileDto {

	private UUID itemId;
	private MultipartFile file;
	private UUID groupId;

	public UUID getItemId() {
		return itemId;
	}

	public void setItemId(UUID itemId) {
		this.itemId = itemId;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public UUID getGroupId() {
		return groupId;
	}

	public void setGroupId(UUID groupId) {
		this.groupId = groupId;
	}
	
}
