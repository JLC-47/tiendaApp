package com.jwt.tiendaApp.dtos.productDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data 
public class ProductRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String description;

    @Positive(message = "El precio debe ser mayor a 0")
    private Double price;

    @Positive(message = "El stock debe ser mayor a 0")
    private Integer stock;

    private Long categoryId;

    private Long sellerId;
}
