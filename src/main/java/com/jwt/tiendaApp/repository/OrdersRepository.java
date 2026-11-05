package com.jwt.tiendaApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.tiendaApp.entity.Orders;

public interface  OrdersRepository extends  JpaRepository<Orders, Long>{
    
}
