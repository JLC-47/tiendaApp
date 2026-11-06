package com.jwt.tiendaApp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jwt.tiendaApp.dtos.HttpGlobalResponseDTO;
import com.jwt.tiendaApp.dtos.productDTO.ProductRequestDTO;
import com.jwt.tiendaApp.dtos.productDTO.ProductResponseDTO;
import com.jwt.tiendaApp.entity.Categories;
import com.jwt.tiendaApp.entity.Products;

import com.jwt.tiendaApp.repository.CategoriesRepository;
import com.jwt.tiendaApp.repository.ProductsRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;
    private final CategoriesRepository categoriesRepository;

    public HttpGlobalResponseDTO<List<ProductResponseDTO>> getProducts(Long categoryId) {
        HttpGlobalResponseDTO<List<ProductResponseDTO>> response = new HttpGlobalResponseDTO<>();
        List<ProductResponseDTO> result = new ArrayList<>();

        if (categoryId != null) {
            List<Products> filtered = productsRepository.findByCategoryId(categoryId);

            for (Products p : filtered) {
                ProductResponseDTO dto = new ProductResponseDTO();
                dto.setId(p.getId());
                dto.setName(p.getName());
                dto.setDescription(p.getDescription());
                dto.setPrice(p.getPrice());
                dto.setStock(p.getStock());
                dto.setCategoryId(p.getCategoryId());
                dto.setSellerId(p.getSellerId());
                result.add(dto);
            }
            response.setMessage("Catálogo filtrado por categoría 🛒");

        } else {
            List<Products> allProducts = productsRepository.findAll();

            for (Products p : allProducts) {
                ProductResponseDTO dto = new ProductResponseDTO();
                dto.setId(p.getId());
                dto.setName(p.getName());
                dto.setDescription(p.getDescription());
                dto.setPrice(p.getPrice());
                dto.setStock(p.getStock());
                dto.setCategoryId(p.getCategoryId());
                dto.setSellerId(p.getSellerId());
                result.add(dto);
            }
            response.setMessage("¡Catálogo de productos! 🛒");
        }

        response.setData(result);
        response.setStatus(true);
        return response;
    }

    public HttpGlobalResponseDTO<ProductResponseDTO> getProductId(Long id) {
        HttpGlobalResponseDTO<ProductResponseDTO> response = new HttpGlobalResponseDTO<>();
        Optional<Products> productFound = productsRepository.findById(id);

        if (productFound.isEmpty()) {
            response.setMessage("¡Pedido no encontrado mi llave! (404)");
            response.setData(null);
            return response;
        }

        Products p = productFound.get();
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setStock(p.getStock());
        dto.setCategoryId(p.getCategoryId());
        dto.setSellerId(p.getSellerId());

        response.setMessage("¡Aquí tienes el detalle del producto!");
        response.setData(dto);
        response.setStatus(true);
        return response;

    }

    public HttpGlobalResponseDTO<List<ProductResponseDTO>> getMyProducts(HttpServletRequest request) {
        HttpGlobalResponseDTO<List<ProductResponseDTO>> response = new HttpGlobalResponseDTO<>();

        Long sellerId = (Long) request.getAttribute("userId");

        List<Products> allProducts = productsRepository.findBySellerId(sellerId);
        List<ProductResponseDTO> sellerProducts = new ArrayList<>();

        for (Products p : allProducts) {
            ProductResponseDTO dto = new ProductResponseDTO();
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setDescription(p.getDescription());
            dto.setPrice(p.getPrice());
            dto.setStock(p.getStock());
            dto.setCategoryId(p.getCategoryId());
            dto.setSellerId(p.getSellerId());
            sellerProducts.add(dto);
        }

        response.setMessage("Tus productos publicados mi pana:");
        response.setData(sellerProducts);
        response.setStatus(true);
        return response;
    }

    public HttpGlobalResponseDTO<ProductResponseDTO> createProduct(ProductRequestDTO body, HttpServletRequest request) {
        HttpGlobalResponseDTO<ProductResponseDTO> response = new HttpGlobalResponseDTO<>();

        Long sellerId = (Long) request.getAttribute("userId");

        Optional<Categories> category = categoriesRepository.findById(body.getCategoryId());

        if (category.isEmpty()) {
            response.setMessage("Categoría no encontrada");
            response.setData(null);
            return response;
        }

        Products product = new Products();
        product.setName(body.getName());
        product.setDescription(body.getDescription());
        product.setPrice(body.getPrice());
        product.setStock(body.getStock());
        product.setCategoryId(body.getCategoryId());
        product.setSellerId(sellerId);

        Products saved = productsRepository.save(product);

        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(saved.getId());
        dto.setName(saved.getName());
        dto.setDescription(saved.getDescription());
        dto.setPrice(saved.getPrice());
        dto.setStock(saved.getStock());
        dto.setCategoryId(saved.getCategoryId());
        dto.setSellerId(saved.getSellerId());

        response.setMessage("¡Producto creado con toda! 🔥");
        response.setData(dto);
        response.setStatus(true);
        return response;
    }

    public HttpGlobalResponseDTO<ProductResponseDTO> updateProduct(Long id, ProductRequestDTO body,
            HttpServletRequest request) {
        HttpGlobalResponseDTO<ProductResponseDTO> response = new HttpGlobalResponseDTO<>();

        Long currentUserId = (Long) request.getAttribute("userId");
        Long currentUserRolId = (Long) request.getAttribute("rolId");

        Optional<Products> productFound = productsRepository.findById(id);

        if (productFound.isEmpty()) {
            response.setMessage("Producto no encontrado (404)");
            response.setData(null);
            return response;
        }

        Products product = productFound.get();

        if (currentUserRolId == 3 && !product.getSellerId().equals(currentUserId)) {
            response.setMessage("¡Alto ahí!, ese producto no es tuyo (403)");
            response.setData(null);
        response.setStatus(true);
            return response;
        }

        product.setName(body.getName());
        product.setDescription(body.getDescription());
        product.setPrice(body.getPrice());
        product.setStock(body.getStock());

        Products updated = productsRepository.save(product);

        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(updated.getId());
        dto.setName(updated.getName());
        dto.setDescription(updated.getDescription());
        dto.setPrice(updated.getPrice());
        dto.setStock(updated.getStock());
        dto.setCategoryId(updated.getCategoryId());
        dto.setSellerId(updated.getSellerId());

        response.setMessage("¡Producto actualizado con éxito!");
        response.setData(dto);
        response.setStatus(true);
        return response;
    }

    public HttpGlobalResponseDTO<ProductResponseDTO> deleteProduct(Long id, HttpServletRequest request) {
        HttpGlobalResponseDTO<ProductResponseDTO> response = new HttpGlobalResponseDTO<>();

        Long currentUserId = (Long) request.getAttribute("userId");
        Long currentUserRolId = (Long) request.getAttribute("rolId");

        Optional<Products> productFound = productsRepository.findById(id);

        if (productFound.isEmpty()) {
            response.setMessage("Producto no encontrado (404)");
            response.setData(null);
            return response;
        }

        Products product = productFound.get();

        if (currentUserRolId == 3 && !product.getSellerId().equals(currentUserId)) {
            response.setMessage("¡No señor!, no puedes borrar un producto ajeno (403)");
            response.setData(null);
            return response;
        }

        productsRepository.delete(product);

        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setCategoryId(product.getCategoryId());
        dto.setSellerId(product.getSellerId());

        response.setMessage("¡Producto eliminado correctamente!");
        response.setData(dto);
        response.setStatus(true);
        return response;
    }

}
