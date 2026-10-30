package com.jwt.tiendaApp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity 
@Data 
@Table(name = "orders")
public class Orders {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(name = "cliente_id")
    private  Long cliente_id;

    @Column(name = "product_id")
    private  Long product_id;

    @Column(name = "quantity")
    private  Long quantity;

    @Column(name = "total")
    private  Double total;

    @Column(name = "status")
    private  String status;
    
    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "rol_id")
    private  Long rol_id;

}
