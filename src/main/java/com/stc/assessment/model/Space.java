package com.stc.assessment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity(name = "space")
@PrimaryKeyJoinColumn(name = "id")
public class Space extends Item {
}