package com.example.sparesstrore.repository;

import com.example.sparesstrore.entity.Item;
import com.example.sparesstrore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
