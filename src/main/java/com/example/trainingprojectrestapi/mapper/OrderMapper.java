package com.example.trainingprojectrestapi.mapper;

import com.example.trainingprojectrestapi.dto.response.OrderResponseDto;
import com.example.trainingprojectrestapi.dto.response.RetrieveOrderResponseDto;
import com.example.trainingprojectrestapi.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for mapping {@link Order} entities to DTOs and vice versa.
 */
@Mapper
public interface OrderMapper {
    /**
     * Singleton instance of the OrderMapper.
     */
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    /**
     * Maps an {@link Order} entity to a {@link RetrieveOrderResponseDto}.
     *
     * @param order The {@link Order} entity to map.
     * @return A {@link RetrieveOrderResponseDto} representing the mapped order.
     */
    @Mapping(target = "customerResponseDto", source = "customer")
    RetrieveOrderResponseDto mapOrderToRetrieveDto(Order order);

    /**
     * Maps an {@link Order} entity to an {@link OrderResponseDto}.
     *
     * @param order The {@link Order} entity to map.
     * @return An {@link OrderResponseDto} representing the mapped order.
     */
    @Mapping(target = "customerId", source = "order.customer.id")
    OrderResponseDto mapOrderToDto(Order order);
}
