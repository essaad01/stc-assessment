package com.stc.assessment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity(name = "folder")
@PrimaryKeyJoinColumn(name = "id")
public class Folder extends Item {
    @ManyToOne
    private Space space;
    
	public Space getSpace() {
		return space;
	}
	public void setSpace(Space space) {
		this.space = space;
	}
    
}
