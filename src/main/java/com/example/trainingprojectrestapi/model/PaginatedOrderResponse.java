package com.example.trainingprojectrestapi.model;

import com.example.trainingprojectrestapi.dto.response.OrderResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * A data class representing a paginated response for orders.
 */
@Builder
@Getter
public class PaginatedOrderResponse {
    private long totalItems;
    private int page;
    private int totalPages;
    private short pageSize;
    private List<OrderResponseDto> items;
}
