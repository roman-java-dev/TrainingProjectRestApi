package com.example.trainingprojectrestapi.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    @NotNull(message = "{validation.customer.id.notNull}")
    @Positive(message = "{validation.customer.id.positive}")
    private Long customerId;

    @NotNull(message = "{validation.order.date.notNull}")
    @PastOrPresent(message = "{validation.order.date.pastOrPresent}")
    private LocalDate orderDate;

    @NotNull(message = "{validation.payment.status.notNull}")
    private Boolean statusPayment;

    @NotBlank(message = "{validation.description.notBlank}")
    @Size(max = 255, message = "{validation.description.size}")
    private String description;

    @NotNull(message = "{validation.total.price.notNull}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{validation.total.price.decimalMin}")
    private BigDecimal totalPrice;
}
