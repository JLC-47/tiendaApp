package com.jwt.tiendaApp.dtos.OrderDTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data 
public class OrderRequestDTO {
    private Long clientId;
    private Long productId;
    private Integer quantity;
    private Double total;
    private String status;
    private LocalDateTime createdAt;
}
