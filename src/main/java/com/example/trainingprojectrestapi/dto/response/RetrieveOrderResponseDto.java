package com.example.trainingprojectrestapi.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class RetrieveOrderResponseDto {
    private Long id;
    private CustomerResponseDto customerResponseDto;
    private boolean statusPayment;
    private LocalDate orderDate;
    private String description;
    private BigDecimal totalPrice;
}
