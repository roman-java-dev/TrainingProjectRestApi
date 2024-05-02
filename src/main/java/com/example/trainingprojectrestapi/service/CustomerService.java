package com.example.trainingprojectrestapi.service;

import com.example.trainingprojectrestapi.dto.request.CustomerRequestDto;
import com.example.trainingprojectrestapi.dto.response.CustomerResponseDto;
import com.example.trainingprojectrestapi.entity.Customer;

import java.util.List;

/**
 * This service interface defines methods for managing customers.
 */
public interface CustomerService {

    /**
     * Retrieves a list of all customers.
     *
     * @return A list of {@link CustomerResponseDto} representing all customers.
     */
    List<CustomerResponseDto> getAllCustomers();

    /**
     * Adds a new customer.
     *
     * @param dto The {@link CustomerRequestDto} containing information about the new customer.
     * @return A {@link CustomerResponseDto} representing the newly added customer.
     */
    CustomerResponseDto addCustomer(CustomerRequestDto dto);

    /**
     * Updates an existing customer.
     *
     * @param customerId The ID of the customer to update.
     * @param dto         The {@link CustomerRequestDto} containing updated information.
     * @return A {@link CustomerResponseDto} representing the updated customer.
     */
    CustomerResponseDto updateCustomer(Long customerId, CustomerRequestDto dto);

    /**
     * Deletes a customer.
     *
     * @param customerId The ID of the customer to delete.
     */
    void deleteCustomer(Long customerId);

    /**
     * Retrieves a customer if it exists.
     *
     * @param customerId The ID of the customer to retrieve.
     * @return The {@link Customer} object if it exists.
     */
    Customer getCustomerIfExists(Long customerId);
}
