package com.jwt.tiendaApp.contoller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.tiendaApp.dtos.HttpGlobalResponseDTO;
import com.jwt.tiendaApp.dtos.productDTO.ProductRequestDTO;
import com.jwt.tiendaApp.dtos.productDTO.ProductResponseDTO;
import com.jwt.tiendaApp.enums.RoleEnum;
import com.jwt.tiendaApp.security.RequiresRole;
import com.jwt.tiendaApp.service.ProductsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/products")
@RequiredArgsConstructor 
public class ProductsController {

    private final ProductsService productsService;

    @GetMapping
    public ResponseEntity<HttpGlobalResponseDTO<List<ProductResponseDTO>>> getProducts(
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(productsService.getProducts(categoryId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpGlobalResponseDTO<ProductResponseDTO>> getProductById(
            @PathVariable Long id) {
        return ResponseEntity.ok(productsService.getProductId(id));
    }

     @GetMapping("/mine")
    @RequiresRole(RoleEnum.VENDEDOR)
    public ResponseEntity<HttpGlobalResponseDTO<List<ProductResponseDTO>>> getMyProducts(
            HttpServletRequest request) {
        return ResponseEntity.ok(productsService.getMyProducts(request));
    }

    @PostMapping
    @RequiresRole(RoleEnum.VENDEDOR)
    public ResponseEntity<HttpGlobalResponseDTO<ProductResponseDTO>> createProduct(
            @Valid @RequestBody ProductRequestDTO body, HttpServletRequest request) {
        return ResponseEntity.status(201).body(productsService.createProduct(body, request));
    }

    @PutMapping("/{id}")
    @RequiresRole({RoleEnum.VENDEDOR, RoleEnum.ADMIN})
    public ResponseEntity<HttpGlobalResponseDTO<ProductResponseDTO>> updateProduct(
            @PathVariable Long id, @Valid @RequestBody ProductRequestDTO body, HttpServletRequest request) {
        return ResponseEntity.ok(productsService.updateProduct(id, body, request));
    }

    @DeleteMapping("/{id}")
    @RequiresRole({RoleEnum.VENDEDOR, RoleEnum.ADMIN})
    public ResponseEntity<HttpGlobalResponseDTO<ProductResponseDTO>> deleteProduct(
            @PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(productsService.deleteProduct(id, request));
    }
    
}
