package com.jwt.tiendaApp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jwt.tiendaApp.dtos.HttpGlobalResponseDTO;
import com.jwt.tiendaApp.dtos.productDTO.ProductDTO;
import com.jwt.tiendaApp.entity.Categories;
import com.jwt.tiendaApp.entity.Products;
import com.jwt.tiendaApp.entity.Users;
import com.jwt.tiendaApp.repository.CategoriesRepository;
import com.jwt.tiendaApp.repository.ProductsRepository;
import com.jwt.tiendaApp.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;
    private final CategoriesRepository categoriesRepository;
    private final UsersRepository usersRepository;

    public HttpGlobalResponseDTO<List<ProductDTO>> getProducts(Long categoryId) {
        HttpGlobalResponseDTO<List<ProductDTO>> response = new HttpGlobalResponseDTO<>();
        List<Products> allProducts = productsRepository.findAll();
        List<ProductDTO> filteredList = new ArrayList<>();

        if (categoryId != null) {
            for (Products p : allProducts) {
                if (p.getCategoryId() != null && p.getCategoryId().equals(categoryId)) {
                    ProductDTO dto = new ProductDTO();
                    dto.setId(p.getId());
                    dto.setName(p.getName());
                    dto.setDescription(p.getDescription());
                    dto.setPrice(p.getPrice());
                    dto.setStock(p.getStock());
                    dto.setCategoryId(p.getCategoryId());
                    dto.setSellerId(p.getSellerId());

                    filteredList.add(dto);
                }
            }
            response.setMessage("Catálogo filtrado por categoría 🛒");

        } else {
            response.setMessage("¡Mirá el catálogo de productos disponible! 🛒");

        }
        response.setData(filteredList);
        return response;

    }

    public HttpGlobalResponseDTO<ProductDTO> getProductId(Long id) {
        HttpGlobalResponseDTO<ProductDTO> response = new HttpGlobalResponseDTO<>();
        Optional<Products> productFound = productsRepository.findById(id);

        if (productFound.isEmpty()) {
            response.setMessage("Producto no encontrado (404)");
            response.setData(null);
            return response;
        }

        Products p = productFound.get();
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setStock(p.getStock());
        dto.setCategoryId(p.getCategoryId());
        dto.setSellerId(p.getSellerId());

        response.setMessage("¡Aquí tienes el detalle del producto!");
        response.setData(dto);
        return response;

    }

    public HttpGlobalResponseDTO<List<ProductDTO>> getProductsBySeller(Long sellerId) {
        HttpGlobalResponseDTO<List<ProductDTO>> response = new HttpGlobalResponseDTO<>();
        List<Products> allProducts = productsRepository.findAll();
        List<ProductDTO> sellerProducts = new ArrayList<>();

        for (Products p : allProducts) {
            if (p.getSellerId() != null && p.getSellerId().equals(sellerId)) {
                ProductDTO dto = new ProductDTO();
                dto.setId(p.getId());
                dto.setName(p.getName());
                dto.setDescription(p.getDescription());
                dto.setPrice(p.getPrice());
                dto.setStock(p.getStock());
                dto.setCategoryId(p.getCategoryId());
                dto.setSellerId(p.getSellerId());

                sellerProducts.add(dto);
            }
        }
        response.setMessage("Tus productos publicados mi pana:");
        response.setData(sellerProducts);
        return response;

    }

    public HttpGlobalResponseDTO<ProductDTO> createProduct(Products product, Long categoryId, Long sellerId) {
        HttpGlobalResponseDTO<ProductDTO> response = new HttpGlobalResponseDTO<>();

        Optional<Categories> category = categoriesRepository.findById(categoryId);
        Optional<Users> seller = usersRepository.findById(sellerId);

        if (category.isEmpty() || seller.isEmpty()) {
            response.setMessage("Categoría o vendedor no encontrados");
            response.setData(null);
            return response;
        }

        product.setCategoryId(categoryId);
        product.setSellerId(sellerId);

        Products saved = productsRepository.save(product);

        ProductDTO dto = new ProductDTO();
        dto.setId(saved.getId());
        dto.setName(saved.getName());
        dto.setDescription(saved.getDescription());
        dto.setPrice(saved.getPrice());
        dto.setStock(saved.getStock());
        dto.setCategoryId(saved.getCategoryId());
        dto.setSellerId(saved.getSellerId());

        response.setMessage("¡Producto creado con toda! 🔥");
        response.setData(dto);
        return response;

    }

    public HttpGlobalResponseDTO<ProductDTO> updateProduct(Long id, Products newData, Long currentUserId,
            Long currentUserRolId) {
        HttpGlobalResponseDTO<ProductDTO> response = new HttpGlobalResponseDTO<>();
        Optional<Products> productFound = productsRepository.findById(id);

        if (productFound.isEmpty()) {
            response.setMessage("Producto no encontrado (404)");
            response.setData(null);
            return response;
        }

        Products product = productFound.get();

        if (currentUserRolId == 3 && !product.getSellerId().equals(currentUserId)) {
            response.setMessage("¡Alto ahí!, ese producto no es tuyo, acceso denegado (403)");
            response.setData(null);
            return response;
        }

        product.setName(newData.getName());
        product.setDescription(newData.getDescription());
        product.setPrice(newData.getPrice());
        product.setStock(newData.getStock());

        Products updated = productsRepository.save(product);

        ProductDTO dto = new ProductDTO();
        dto.setId(updated.getId());
        dto.setName(updated.getName());
        dto.setDescription(updated.getDescription());
        dto.setPrice(updated.getPrice());
        dto.setStock(updated.getStock());
        dto.setCategoryId(updated.getCategoryId());
        dto.setSellerId(updated.getSellerId());

        response.setMessage("¡Producto actualizado con éxito!");
        response.setData(dto);
        return response;

    }

    public  HttpGlobalResponseDTO<ProductDTO> deleteProduct(Long id, Long currentUserId, Long currentUserRolId) {
        HttpGlobalResponseDTO<ProductDTO> response = new HttpGlobalResponseDTO<>();
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

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setCategoryId(product.getCategoryId());
        dto.setSellerId(product.getSellerId());

        response.setMessage("¡Producto eliminado correctamente!");
        response.setData(dto);
        return response;

    }

}
