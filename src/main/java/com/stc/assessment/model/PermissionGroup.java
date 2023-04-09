package com.stc.assessment.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity(name = "permission_group")
public class PermissionGroup {
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "group_name")
    private String groupName;
    @JsonIgnore
    @OneToMany(mappedBy = "permissionGroup")
    private List<Item> items;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="group_id")
    private List<Permission> permissions = new ArrayList<>();
    
    public PermissionGroup() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PermissionGroup(String name) {
		this.groupName = name;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
	
}
