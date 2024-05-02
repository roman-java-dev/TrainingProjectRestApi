package com.example.trainingprojectrestapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Customer customer;
    @Column(name = "order_date")
    private LocalDate orderDate;
    @Column(name = "status_payment")
    private boolean statusPayment;
    private String description;
    @Column(name = "total_price")
    private BigDecimal totalPrice;
}
