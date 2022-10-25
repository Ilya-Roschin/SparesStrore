package com.example.sparesstrore.repository;

import com.example.sparesstrore.entity.Category;
import com.example.sparesstrore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
