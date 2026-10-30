package com.jwt.tiendaApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.tiendaApp.entity.Roles;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    
}
