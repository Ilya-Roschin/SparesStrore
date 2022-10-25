package com.example.sparesstrore.repository;

import com.example.sparesstrore.entity.Purchase;
import com.example.sparesstrore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Purchase, Long> {
}
