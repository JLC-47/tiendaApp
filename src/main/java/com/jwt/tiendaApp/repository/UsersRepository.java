package com.jwt.tiendaApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.tiendaApp.entity.Users;

import java.util.Optional;


public interface UsersRepository extends JpaRepository<Users, Long> {

        Optional<Users> findByUsername(String username);

        Optional<Users> findByEmail(String email);

}