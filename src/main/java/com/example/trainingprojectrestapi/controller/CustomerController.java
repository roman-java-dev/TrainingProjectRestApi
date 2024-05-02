package com.example.trainingprojectrestapi.controller;

import com.example.trainingprojectrestapi.dto.request.CustomerRequestDto;
import com.example.trainingprojectrestapi.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling customer-related operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getAllCustomers());
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid CustomerRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.addCustomer(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid CustomerRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.updateCustomer(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
