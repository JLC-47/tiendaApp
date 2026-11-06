package com.jwt.tiendaApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.tiendaApp.entity.Products;

public interface ProductsRepository extends JpaRepository<Products, Long> {
    List<Products> findBySellerId(Long sellerId);
    List<Products> findByCategoryId(Long categoryId);
    
}