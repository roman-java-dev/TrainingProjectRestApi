package com.example.trainingprojectrestapi;

import com.example.trainingprojectrestapi.dto.request.OrderRequestDto;
import com.example.trainingprojectrestapi.entity.Customer;
import com.example.trainingprojectrestapi.entity.Order;
import com.example.trainingprojectrestapi.repository.CustomerRepository;
import com.example.trainingprojectrestapi.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;
import java.util.Random;

import static com.example.trainingprojectrestapi.util.ConstantsUtil.REPORT_FILE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    private OrderRequestDto orderRequestDto;
    private Order order;

    @BeforeEach
    void setUp() {
        Customer customer = customerRepository.save(Customer.builder()
                .firstName("Alice")
                .lastName("Smith")
                .phoneNumber("380777777777")
                .email("test_alice@test.test")
                .build());

        orderRequestDto = OrderRequestDto.builder()
                .orderDate(LocalDate.of(2024,2,20))
                .statusPayment(false)
                .customerId(customer.getId())
                .description("Laptop, Backpack, Coat, Towels")
                .totalPrice(BigDecimal.valueOf(200.50))
                .build();

        order = orderRepository.save(Order.builder()
                .orderDate(LocalDate.of(2024,1,10))
                .customer(customer)
                .statusPayment(true)
                .description("Gloves, Lamp, Soap, T-shirt")
                .totalPrice(BigDecimal.valueOf(123.50))
                .build());
    }

    @Test
    @DisplayName("givenAddOrder_whenValidInput_thenSuccess")
    public void testAddOrder_ok() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderDate", is("2024-02-20")))
                .andExpect(jsonPath("$.customerId", is(orderRequestDto.getCustomerId().intValue())))
                .andExpect(jsonPath("$.description", is("Laptop, Backpack, Coat, Towels")))
                .andExpect(jsonPath("$.totalPrice", is(200.50)));
    }

    @Test
    @DisplayName("givenDeleteOrder_whenValidInput_thenSuccess")
    public void testDeleteOrder_ok() throws Exception {
        mockMvc.perform(delete("/api/orders/{id}", order.getId()))
                .andExpect(status().isOk());

        assertFalse(orderRepository.existsById(order.getId()));
    }

    @Test
    @DisplayName("givenGetOrder_whenValidInput_thenSuccess")
    public void testGetOrder_ok() throws Exception {
        mockMvc.perform(get("/api/orders/{id}", order.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusPayment", is(true)))
                .andExpect(jsonPath("$.orderDate", is("2024-01-10")))
                .andExpect(jsonPath("$.description", is("Gloves, Lamp, Soap, T-shirt")))
                .andExpect(jsonPath("$.totalPrice", is(123.50)))
                .andExpect(jsonPath("$.customerResponseDto.firstName", is("Alice")))
                .andExpect(jsonPath("$.customerResponseDto.lastName", is("Smith")))
                .andExpect(jsonPath("$.customerResponseDto.phoneNumber", is("380777777777")))
                .andExpect(jsonPath("$.customerResponseDto.email", is("test_alice@test.test")));
    }

    @Test
    @DisplayName("givenUpdateOrder_whenValidInput_thenSuccess")
    public void testUpdateOrder_ok() throws Exception {
        mockMvc.perform(put("/api/orders/{id}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderDate", is("2024-02-20")))
                .andExpect(jsonPath("$.customerId", is(orderRequestDto.getCustomerId().intValue())))
                .andExpect(jsonPath("$.description", is("Laptop, Backpack, Coat, Towels")))
                .andExpect(jsonPath("$.totalPrice", is(200.50)));
    }

    @Test
    @DisplayName("givenFindByCriteriaOrder_whenValidInput_thenGetFilteringResult")
    public void testFindByCriteriaOrder_ok() throws Exception {
        Map<String, Object> criteriaMap = Map.of("customerId", order.getCustomer().getId());

        mockMvc.perform(post("/api/orders/_list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criteriaMap)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems", is(1)))
                .andExpect(jsonPath("$.page", is(1)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.pageSize", is(10)))
                .andExpect(jsonPath("$.items[0].customerId", is(order.getCustomer().getId().intValue())))
                .andExpect(jsonPath("$.items[0].orderDate", is("2024-01-10")))
                .andExpect(jsonPath("$.items[0].description", is("Gloves, Lamp, Soap, T-shirt")))
                .andExpect(jsonPath("$.items[0].totalPrice", is(123.50)));
    }

    @Test
    @DisplayName("givenUploadOrders_whenCorrectFile_thenGetResultsWithStatistics")
    public void testUploadOrders_ok() throws Exception {
        ClassPathResource resource = new ClassPathResource("test_orders.json");
        byte[] content = StreamUtils.copyToByteArray(resource.getInputStream());
        MockMultipartFile file = new MockMultipartFile(
                "file", "test_orders.json", "application/json", content);

        mockMvc.perform(multipart("/api/orders/upload")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successfulImports", is(18)))
                .andExpect(jsonPath("$.failedImports", is(2)))
                .andExpect(jsonPath("$.invalidInputData[0].errorMessage", is("Order date must be in the past or present")))
                .andExpect(jsonPath("$.invalidInputData[0].incorrectField", is("orderDate")))
                .andExpect(jsonPath("$.invalidInputData[0].fieldValue", is("2028-04-09")))
                .andExpect(jsonPath("$.invalidInputData[1].errorMessage", is("Total price must be greater than 0.0")))
                .andExpect(jsonPath("$.invalidInputData[1].incorrectField", is("totalPrice")))
                .andExpect(jsonPath("$.invalidInputData[1].fieldValue", is(-135.50)));
    }

    @Test
    @DisplayName("givenGetReportFile_whenCorrectCriteria_thenGetFileReportWIthResults")
    public void testGetReportFile_ok() throws Exception {
        Map<String, Object> criteriaMap = Map.of("customerId", order.getCustomer().getId());

        MockHttpServletResponse response = mockMvc.perform(post("/api/orders/_report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criteriaMap)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(header().string("Content-Disposition", "attachment; filename=" + REPORT_FILE_NAME))
                .andReturn().getResponse();

        String csvContent = response.getContentAsString(StandardCharsets.UTF_8);
        String expectedCsvContent = "\"Customer ID\",\"Order Date\",\"Description\",\"Total Price\"\n" +
                "\"" + order.getCustomer().getId().intValue() + "\",\"2024-01-10\",\"Gloves, Lamp, Soap, T-shirt\",\"123.5\"\n";

        assertThat(csvContent).isEqualTo(expectedCsvContent);
    }

    @Test
    @DisplayName("givenDeleteOrder_whenNotExistsInDb_thenGetException")
    public void testDeleteOrder_orderNotExistsInDb_notOk() throws Exception {
        Long nonExistentId = generateNonExistentId();

        mockMvc.perform(delete("/api/orders/{id}", nonExistentId))
                .andExpect(status().isBadRequest());

        assertFalse(orderRepository.existsById(nonExistentId));
    }

    @Test
    @DisplayName("givenUploadOrders_whenEmptyFile_thenGetException")
    public void testUploadOrders_emptyFile_notOk() throws Exception {
        ClassPathResource resource = new ClassPathResource("empty_file.json");
        byte[] content = StreamUtils.copyToByteArray(resource.getInputStream());
        MockMultipartFile file = new MockMultipartFile(
                "file", "empty_file.json", "application/json", content);

        mockMvc.perform(multipart("/api/orders/upload")
                        .file(file))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("givenUploadOrders_whenFileNotInJsonFormat_thenGetException")
    public void testUploadOrders_fileNotInJsonFormat_notOk() throws Exception {
        ClassPathResource resource = new ClassPathResource("file.wrong");
        byte[] content = StreamUtils.copyToByteArray(resource.getInputStream());
        MockMultipartFile file = new MockMultipartFile(
                "file", "file.wrong", "application/json", content);

        mockMvc.perform(multipart("/api/orders/upload")
                        .file(file))
                .andExpect(status().isBadRequest());
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
