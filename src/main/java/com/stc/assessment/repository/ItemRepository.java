package com.stc.assessment.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stc.assessment.model.Item;

public interface ItemRepository extends JpaRepository<Item, UUID> {
}
