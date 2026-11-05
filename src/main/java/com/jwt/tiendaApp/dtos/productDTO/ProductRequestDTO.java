package com.jwt.tiendaApp.dtos.productDTO;

import lombok.Data;

@Data 
public class ProductRequestDTO {
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private Long categoryId;
    private Long sellerId;
}
