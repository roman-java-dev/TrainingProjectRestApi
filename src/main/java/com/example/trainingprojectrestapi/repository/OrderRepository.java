package com.example.trainingprojectrestapi.repository;

import com.example.trainingprojectrestapi.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository interface for managing {@link Order} entities.
 * Provides methods for basic CRUD operations and executing specifications.
 */
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
}
