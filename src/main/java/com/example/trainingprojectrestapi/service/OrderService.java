package com.example.trainingprojectrestapi.service;

import com.example.trainingprojectrestapi.dto.request.OrderRequestDto;
import com.example.trainingprojectrestapi.dto.response.OrderResponseDto;
import com.example.trainingprojectrestapi.model.ImportedResult;
import com.example.trainingprojectrestapi.model.PaginatedOrderResponse;
import com.example.trainingprojectrestapi.dto.response.RetrieveOrderResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * This service interface defines methods for managing orders.
 */
public interface OrderService {

    /**
     * Adds a new order.
     *
     * @param dto The {@link OrderRequestDto} containing information about the new order.
     * @return A {@link OrderResponseDto} representing the newly added order.
     */
    OrderResponseDto addOrder(OrderRequestDto dto);

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId The ID of the order to retrieve.
     * @return A {@link RetrieveOrderResponseDto} representing the retrieved order.
     */
    RetrieveOrderResponseDto getOrder(Long orderId);

    /**
     * Updates an existing order.
     *
     * @param orderId The ID of the order to update.
     * @param dto     The {@link OrderRequestDto} containing updated information.
     * @return A {@link OrderResponseDto} representing the updated order.
     */
    OrderResponseDto updateOrder(Long orderId, OrderRequestDto dto);

    /**
     * Deletes an order.
     *
     * @param orderId The ID of the order to delete.
     */
    void deleteOrder(Long orderId);

    /**
     * Retrieves orders based on specified criteria, paginated.
     *
     * @param criteria A {@link Map} containing the search criteria.
     * @param size     The number of orders per page.
     * @param page     The page number.
     * @return A {@link PaginatedOrderResponse} containing the paginated list of orders.
     */
    PaginatedOrderResponse getOrdersByCriteria(Map<String, Object> criteria, short size, short page);

    /**
     * Retrieves orders based on specified criteria.
     *
     * @param criteria A {@link Map} containing the search criteria.
     * @return A list of {@link OrderResponseDto} representing the matching orders.
     */
    List<OrderResponseDto> getOrdersByCriteria(Map<String, Object> criteria);

    /**
     * Processes orders from a file.
     *
     * @param file The {@link MultipartFile} containing order data.
     * @return An {@link ImportedResult} representing the result of the import process.
     */
    ImportedResult processOrders(MultipartFile file);
}
