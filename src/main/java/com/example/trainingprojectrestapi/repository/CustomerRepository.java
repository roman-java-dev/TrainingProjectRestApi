package com.example.trainingprojectrestapi.repository;

import com.example.trainingprojectrestapi.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository interface for managing {@link Customer} entities.
 * Provides methods for basic CRUD operations.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
