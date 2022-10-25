package com.example.sparesstrore.repository;

import com.example.sparesstrore.entity.Car;
import com.example.sparesstrore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
