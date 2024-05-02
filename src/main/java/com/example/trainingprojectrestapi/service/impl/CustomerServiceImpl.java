package com.example.trainingprojectrestapi.service.impl;

import com.example.trainingprojectrestapi.dto.request.CustomerRequestDto;
import com.example.trainingprojectrestapi.dto.response.CustomerResponseDto;
import com.example.trainingprojectrestapi.entity.Customer;
import com.example.trainingprojectrestapi.exception.DataProcessingException;
import com.example.trainingprojectrestapi.repository.CustomerRepository;
import com.example.trainingprojectrestapi.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the CustomerService interface for managing customers.
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ObjectMapper mapper;

    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customer -> mapper.convertValue(customer, CustomerResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponseDto addCustomer(CustomerRequestDto dto) {
        try {
            Customer customerToSave = mapper.convertValue(dto, Customer.class);

            return mapper.convertValue(customerRepository.save(customerToSave),
                    CustomerResponseDto.class);
        } catch (RuntimeException ex) {
            throw new DataProcessingException("Failed to save customer. "
                    + ex.getCause().getCause().getMessage());
        }
    }

    @Override
    public CustomerResponseDto updateCustomer(Long customerId, CustomerRequestDto dto) {
        Customer customerFromDb = getCustomerIfExists(customerId);
        customerFromDb.setEmail(dto.getEmail());
        customerFromDb.setFirstName(dto.getFirstName());
        customerFromDb.setLastName(dto.getLastName());
        customerFromDb.setPhoneNumber(dto.getPhoneNumber());

        try {
            return mapper.convertValue(customerRepository.save(customerFromDb),
                    CustomerResponseDto.class);
        } catch (RuntimeException ex) {
            throw new DataProcessingException("Failed to update customer. "
                    + ex.getCause().getCause().getMessage());
        }
    }

    @Override
    public void deleteCustomer(Long customerId) {
        Customer customerFromDb = getCustomerIfExists(customerId);
        customerRepository.delete(customerFromDb);
    }

    @Override
    public Customer getCustomerIfExists(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(
                () -> new DataProcessingException("Couldn't find customer by id: " + customerId)
        );
    }
}
