package com.stc.assessment.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stc.assessment.utils.PermissionLevel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "user_email")
    private String userEmail;
    @Column(name = "permission_level")
    @Enumerated(EnumType.STRING)
    private PermissionLevel permissionLevel;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private PermissionGroup group;
    
	public Permission() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Permission(String userEmail, PermissionLevel permissionLevel) {
		super();
		this.userEmail = userEmail;
		this.permissionLevel = permissionLevel;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public PermissionLevel getPermissionLevel() {
		return permissionLevel;
	}

	public void setPermissionLevel(PermissionLevel permissionLevel) {
		this.permissionLevel = permissionLevel;
	}

	public PermissionGroup getGroup() {
		return group;
	}

	public void setGroup(PermissionGroup group) {
		this.group = group;
	}
	
}
