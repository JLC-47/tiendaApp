package com.jwt.tiendaApp.dtos.OrderDTO;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data 
public class OrderRequestDTO {

    private Long clientId;

    private Long productId;

    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer quantity;

    private Double total;

    private String status;

    private LocalDateTime createdAt;
}
