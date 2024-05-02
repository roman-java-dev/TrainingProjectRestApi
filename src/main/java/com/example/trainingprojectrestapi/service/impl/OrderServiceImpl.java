package com.example.trainingprojectrestapi.service.impl;

import com.example.trainingprojectrestapi.dto.request.OrderRequestDto;
import com.example.trainingprojectrestapi.dto.response.OrderResponseDto;
import com.example.trainingprojectrestapi.dto.response.RetrieveOrderResponseDto;
import com.example.trainingprojectrestapi.entity.Customer;
import com.example.trainingprojectrestapi.entity.Order;
import com.example.trainingprojectrestapi.exception.DataProcessingException;
import com.example.trainingprojectrestapi.exception.FileOperationException;
import com.example.trainingprojectrestapi.mapper.OrderMapper;
import com.example.trainingprojectrestapi.model.ImportedResult;
import com.example.trainingprojectrestapi.model.InvalidInputData;
import com.example.trainingprojectrestapi.model.PaginatedOrderResponse;
import com.example.trainingprojectrestapi.repository.CustomerRepository;
import com.example.trainingprojectrestapi.repository.OrderRepository;
import com.example.trainingprojectrestapi.service.CustomerService;
import com.example.trainingprojectrestapi.service.OrderService;
import com.example.trainingprojectrestapi.util.FileOperationUtil;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.trainingprojectrestapi.util.ConstantsUtil.*;

/**
 * Implementation of the OrderService interface for managing orders.
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final Validator validator;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    @Override
    public OrderResponseDto addOrder(OrderRequestDto dto) {
        Customer customerFromDB = customerService.getCustomerIfExists(dto.getCustomerId());

        Order savedOrder = orderRepository.save(Order.builder()
                .customer(customerFromDB)
                .orderDate(dto.getOrderDate())
                .statusPayment(dto.getStatusPayment())
                .description(dto.getDescription())
                .totalPrice(dto.getTotalPrice())
                .build());

        return OrderMapper.INSTANCE.mapOrderToDto(savedOrder);
    }

    @Override
    public RetrieveOrderResponseDto getOrder(Long orderId) {
        Order order = getOrderIfExists(orderId);
        return OrderMapper.INSTANCE.mapOrderToRetrieveDto(order);
    }

    @Override
    public OrderResponseDto updateOrder(Long orderId, OrderRequestDto dto) {
        Customer customerFromDB = customerService.getCustomerIfExists(dto.getCustomerId());

        Order orderFromDB = getOrderIfExists(orderId);

        orderFromDB.setCustomer(customerFromDB);
        orderFromDB.setOrderDate(dto.getOrderDate());
        orderFromDB.setStatusPayment(dto.getStatusPayment());
        orderFromDB.setDescription(dto.getDescription());
        orderFromDB.setTotalPrice(dto.getTotalPrice());
        return OrderMapper.INSTANCE.mapOrderToDto(orderRepository.save(orderFromDB));
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order orderFromDB = getOrderIfExists(orderId);
        orderRepository.delete(orderFromDB);
    }

    @Override
    public PaginatedOrderResponse getOrdersByCriteria(Map<String, Object> criteria, short size, short page) {
        Specification<Order> specification = buildSpecification(criteria);
        Page<Order> orders = orderRepository.findAll(specification, PageRequest.of(page - 1, size));

        List<OrderResponseDto> ordersDto = orders.getContent().stream()
                .map(OrderMapper.INSTANCE::mapOrderToDto)
                .toList();

        return PaginatedOrderResponse.builder()
                .totalItems(orders.getTotalElements())
                .page(page)
                .totalPages(orders.getTotalPages())
                .pageSize(size)
                .items(ordersDto)
                .build();
    }

    @Override
    public List<OrderResponseDto> getOrdersByCriteria(Map<String, Object> criteria) {
        Specification<Order> specification = buildSpecification(criteria);

        return orderRepository.findAll(specification).stream()
                .map(OrderMapper.INSTANCE::mapOrderToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ImportedResult processOrders(MultipartFile file) {
        List<InvalidInputData> invalidInputData = new ArrayList<>();

        List<OrderRequestDto> ordersFromFile = getOrderRequestDtoList(file);

        List<OrderResponseDto> savedOrders = ordersFromFile.stream()
                .filter(order -> isOrderValid(order, invalidInputData))
                .map(this::addOrder)
                .toList();

        return ImportedResult.builder()
                .failedImports(ordersFromFile.size() - savedOrders.size())
                .successfulImports(savedOrders.size())
                .invalidInputData(invalidInputData)
                .build();
    }

    /**
     * Checks if the given order is valid and populates the list of invalid input data.
     *
     * @param order             The {@link OrderRequestDto} to validate.
     * @param invalidInputData The list to populate with invalid input data.
     * @return {@code true} if the order is valid, {@code false} otherwise.
     */
    private boolean isOrderValid(OrderRequestDto order, List<InvalidInputData> invalidInputData) {
        Errors errors = new BeanPropertyBindingResult(order, OrderRequestDto.class.getSimpleName());
        validator.validate(order, errors);
        if (errors.hasErrors()) {
            errors.getFieldErrors().forEach(error -> invalidInputData.add(buildInputData(error)));
            return false;
        }
        if (!customerRepository.existsById(order.getCustomerId())) {
            invalidInputData.add(InvalidInputData.builder()
                    .errorMessage(ERROR_MESSAGE)
                    .incorrectField(CUSTOMER_ID_KEY)
                    .fieldValue(order.getCustomerId())
                    .build());
            return false;
        }
        return true;
    }

    /**
     * Builds an {@link InvalidInputData} object from the given {@link FieldError}.
     *
     * @param error The {@link FieldError} to build the invalid input data from.
     * @return An {@link InvalidInputData} object representing the error.
     */
    private InvalidInputData buildInputData(FieldError error) {
        return InvalidInputData.builder()
                .errorMessage(error.getDefaultMessage())
                .incorrectField(error.getField())
                .fieldValue(error.getRejectedValue()).build();
    }

    /**
     * Retrieves a list of {@link OrderRequestDto} objects from the provided multipart file.
     *
     * @param file The {@link MultipartFile} containing the JSON data.
     * @return A list of {@link OrderRequestDto} objects parsed from the JSON file.
     * @throws FileOperationException If the file is not in JSON format or if it is empty.
     */
    private List<OrderRequestDto> getOrderRequestDtoList(MultipartFile file) {
        if (!FileOperationUtil.isJsonFile(file)) {
            throw new FileOperationException("Received file " + file.getOriginalFilename()
            + "is not in JSON format");
        }
        List<OrderRequestDto> jsonOrderList = FileOperationUtil.parseJsonFileData(file);

        if (jsonOrderList.isEmpty()) {
            throw new FileOperationException("Received empty json file with no data");
        }

        return jsonOrderList;
    }

    /**
     * Builds a {@link Specification} based on the provided criteria.
     *
     * @param criteria The criteria map containing search parameters.
     * @return A {@link Specification} representing the search criteria.
     */
    private Specification<Order> buildSpecification(Map<String, Object> criteria) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            for (Map.Entry<String, Object> entry : criteria.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value != null) {
                    if (CUSTOMER_ID_KEY.equals(key)) {
                        Join<Order, Customer> customerJoin = root.join(CUSTOMER_JOIN_PROPERTY);
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.equal(customerJoin.get(CUSTOMER_ID_PROPERTY), value));
                    } else if (ORDER_DATE_KEY.equals(key)) {
                        LocalDate dateValue = LocalDate.parse((String) value);
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(key), dateValue));
                    } else if (DESCRIPTION_KEY.equals(key)) {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.like(root.get(key), "%" + value + "%"));
                    } else if (TOTAL_PRICE_KEY.equals(key)) {
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(key), value));
                    }
                }
            }
            return predicate;
        };
    }

    /**
     * Retrieves an {@link Order} by its ID if it exists in the database.
     *
     * @param orderId The ID of the order to retrieve.
     * @return The {@link Order} object corresponding to the provided ID.
     * @throws DataProcessingException If no order with the provided ID exists in the database.
     */
    private Order getOrderIfExists(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new DataProcessingException("Couldn't find order by id: " + orderId)
        );
    }
}
