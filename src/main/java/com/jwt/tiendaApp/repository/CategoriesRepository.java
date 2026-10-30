package com.jwt.tiendaApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.tiendaApp.entity.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    
}