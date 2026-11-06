package com.jwt.tiendaApp.contoller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.tiendaApp.dtos.HttpGlobalResponseDTO;
import com.jwt.tiendaApp.dtos.OrderDTO.OrderRequestDTO;
import com.jwt.tiendaApp.dtos.OrderDTO.OrderResponseDTO;
import com.jwt.tiendaApp.dtos.OrderDTO.OrderStatusRequestDTO;
import com.jwt.tiendaApp.enums.RoleEnum;
import com.jwt.tiendaApp.security.RequiresRole;
import com.jwt.tiendaApp.service.OrdersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/orders")
@RequiredArgsConstructor 
public class OrdersController {
    
    private final OrdersService ordersService;

    @PostMapping
    @RequiresRole(RoleEnum.CLIENTE)
    public ResponseEntity<HttpGlobalResponseDTO<OrderResponseDTO>> createOrder(
            @Valid @RequestBody OrderRequestDTO body, HttpServletRequest request) {
        return ResponseEntity.status(201).body(ordersService.createOrder(body, request));
    }

    @GetMapping("/mine")
    @RequiresRole(RoleEnum.CLIENTE)
    public ResponseEntity<HttpGlobalResponseDTO<List<OrderResponseDTO>>> getMyOrders(
            HttpServletRequest request) {
        return ResponseEntity.ok(ordersService.getMyOrders(request));
    }

    @GetMapping("/sales")
    @RequiresRole(RoleEnum.VENDEDOR)
    public ResponseEntity<HttpGlobalResponseDTO<List<OrderResponseDTO>>> getMySales(
            HttpServletRequest request) {
        return ResponseEntity.ok(ordersService.getMySales(request));
    }

    @GetMapping
    @RequiresRole(RoleEnum.ADMIN)
    public ResponseEntity<HttpGlobalResponseDTO<List<OrderResponseDTO>>> getAllOrders() {
        return ResponseEntity.ok(ordersService.getAllOrders());
    }

    @PatchMapping("/{id}/status")
    @RequiresRole({RoleEnum.VENDEDOR, RoleEnum.ADMIN})
    public ResponseEntity<HttpGlobalResponseDTO<OrderResponseDTO>> updateStatus(
            @PathVariable Long id, @RequestBody OrderStatusRequestDTO status, HttpServletRequest request) {
        return ResponseEntity.ok(ordersService.updateStatus(id, status, request));
    }

    @PatchMapping("/{id}/cancel")
    @RequiresRole(RoleEnum.CLIENTE)
    public ResponseEntity<HttpGlobalResponseDTO<OrderResponseDTO>> cancelOrder(
            @PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(ordersService.cancelOrder(id, request));
    }

}
