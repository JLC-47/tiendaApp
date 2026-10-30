package com.jwt.tiendaApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.tiendaApp.entity.Products;

public interface ProductsRepository extends JpaRepository<Products, Long> {

    
}