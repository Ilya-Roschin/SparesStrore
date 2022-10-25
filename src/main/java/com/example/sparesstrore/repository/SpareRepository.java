package com.example.sparesstrore.repository;

import com.example.sparesstrore.entity.Spare;
import com.example.sparesstrore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpareRepository extends JpaRepository<Spare, Long> {
}
