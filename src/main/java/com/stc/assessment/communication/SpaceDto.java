package com.stc.assessment.communication;

import java.util.UUID;

public class SpaceDto {
	
	private UUID id;
	private String name;
	private UUID groupId;
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public UUID getGroupId() {
		return groupId;
	}
	public void setGroupId(UUID groupId) {
		this.groupId = groupId;
	}

}
