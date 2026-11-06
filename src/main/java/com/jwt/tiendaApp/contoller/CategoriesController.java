package com.jwt.tiendaApp.contoller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.tiendaApp.dtos.HttpGlobalResponseDTO;
import com.jwt.tiendaApp.dtos.categoriesDTO.CategoryRequestDTO;
import com.jwt.tiendaApp.dtos.categoriesDTO.CategoryResponseDTO;
import com.jwt.tiendaApp.enums.RoleEnum;
import com.jwt.tiendaApp.security.RequiresRole;
import com.jwt.tiendaApp.service.CategoriesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/categories")
@RequiredArgsConstructor 
public class CategoriesController {
    
    private final CategoriesService categoriesService;

    @GetMapping
    @RequiresRole({RoleEnum.ADMIN, RoleEnum.CLIENTE, RoleEnum.VENDEDOR})
    public ResponseEntity<HttpGlobalResponseDTO<List<CategoryResponseDTO>>> getAllCategories() {
        return ResponseEntity.ok(categoriesService.getAllCategories());
    }

    @PostMapping
    @RequiresRole(RoleEnum.ADMIN)
    public ResponseEntity<HttpGlobalResponseDTO<CategoryResponseDTO>> createCategory(
            @Valid @RequestBody CategoryRequestDTO body) {
        return ResponseEntity.status(201).body(categoriesService.createCategory(body));
    }

    @DeleteMapping("/{id}")
    @RequiresRole(RoleEnum.ADMIN)
    public ResponseEntity<HttpGlobalResponseDTO<CategoryResponseDTO>> deleteCategory(
            @PathVariable Long id) {
        return ResponseEntity.ok(categoriesService.deleteCategory(id));
    }

}
