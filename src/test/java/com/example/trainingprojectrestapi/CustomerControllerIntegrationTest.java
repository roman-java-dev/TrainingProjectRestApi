package com.example.trainingprojectrestapi;

import com.example.trainingprojectrestapi.dto.request.CustomerRequestDto;
import com.example.trainingprojectrestapi.entity.Customer;
import com.example.trainingprojectrestapi.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class CustomerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomerRepository customerRepository;
    private Customer customer;
    private CustomerRequestDto customerRequestDto;

    @BeforeEach
    void setUp() {
        customer = customerRepository.save(Customer.builder()
                .firstName("Alice")
                .lastName("Smith")
                .phoneNumber("380777777777")
                .email("test_alice@test.test")
                .build());

        customerRequestDto = CustomerRequestDto.builder()
                .firstName("Bob")
                .lastName("Test")
                .phoneNumber("380333333333")
                .email("test_bob@test.test")
                .build();
    }

    @Test
    @DisplayName("givenGetAllCustomers_whenValidData_thenGetAllCustomersFromDb")
    public void testGetAllCustomers_ok() throws Exception {
        int size = customerRepository.findAll().size();

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(size)));
    }

    @Test
    @DisplayName("givenAddCustomer_whenValidInput_thenSuccess")
    public void testAddCustomer_ok() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("Bob")))
                .andExpect(jsonPath("$.lastName", is("Test")))
                .andExpect(jsonPath("$.phoneNumber", is("380333333333")))
                .andExpect(jsonPath("$.email", is("test_bob@test.test")));
    }

    @Test
    @DisplayName("givenAddCustomer_whenInvalidCustomerData_thenGetException")
    public void testAddCustomer_invalidCustomerData_notOk() throws Exception {
        CustomerRequestDto invalidCustomerDto = CustomerRequestDto.builder()
                .firstName("")
                .lastName("Test")
                .phoneNumber("bad_phone")
                .email("wrong_email")
                .build();
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCustomerDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("givenAddCustomer_whenInputCustomerExistsInDb_thenGetException")
    public void testAddCustomer_duplicatedCustomer_notOk() throws Exception {
        customerRepository.save(Customer.builder()
                .firstName("Bob")
                .lastName("Test")
                .phoneNumber("380333333333")
                .email("test_bob@test.test")
                .build());

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("givenUpdateCustomer_whenValidInput_thenSuccess")
    public void testUpdateCustomer_ok() throws Exception {
        mockMvc.perform(put("/api/customers/{id}", customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("Bob")))
                .andExpect(jsonPath("$.lastName", is("Test")))
                .andExpect(jsonPath("$.phoneNumber", is("380333333333")))
                .andExpect(jsonPath("$.email", is("test_bob@test.test")));
    }

    @Test
    @DisplayName("givenUpdateCustomer_whenCustomerNotExistsInDb_thenGetException")
    public void testUpdateCustomer_customerNotExistsInDb_notOk() throws Exception {
        Long nonExistentId = generateNonExistentId();

        mockMvc.perform(put("/api/customers/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("givenDeleteCustomer_whenValidInput_thenSuccess")
    public void testDeleteCustomer_ok() throws Exception {
        mockMvc.perform(delete("/api/customers/{id}", customer.getId()))
                .andExpect(status().isOk());

        assertFalse(customerRepository.existsById(customer.getId()));
    }

    private Long generateNonExistentId() {
        Random random = new Random();
        long id;
        do {
            id = random.nextLong(1000, Integer.MAX_VALUE);
        } while (customerRepository.existsById(id));
        return id;
    }
}
