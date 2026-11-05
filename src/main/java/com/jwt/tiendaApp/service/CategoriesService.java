package com.jwt.tiendaApp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jwt.tiendaApp.dtos.HttpGlobalResponseDTO;
import com.jwt.tiendaApp.dtos.categoriesDTO.CategoryRequestDTO;
import com.jwt.tiendaApp.dtos.categoriesDTO.CategoryResponseDTO;
import com.jwt.tiendaApp.entity.Categories;
import com.jwt.tiendaApp.entity.Products;
import com.jwt.tiendaApp.repository.CategoriesRepository;
import com.jwt.tiendaApp.repository.ProductsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;
    private final ProductsRepository productsRepository;

    public HttpGlobalResponseDTO<List<CategoryResponseDTO>> getAllCategories() {
        HttpGlobalResponseDTO<List<CategoryResponseDTO>> response = new HttpGlobalResponseDTO<>();
        List<CategoryResponseDTO> categoryDTOList = new ArrayList<>();
        List<Categories> categories = categoriesRepository.findAll();

        for (Categories c : categories) {
            CategoryResponseDTO dto = new CategoryResponseDTO();
            dto.setId(c.getId());
            dto.setName(c.getName());

            categoryDTOList.add(dto);
        }

        response.setMessage("¡Aquí están todas las categorías mi hermano! 📂");
        response.setData(categoryDTOList);
        return response;

    }

    public HttpGlobalResponseDTO<CategoryResponseDTO> createCategory(CategoryRequestDTO category){
        HttpGlobalResponseDTO<CategoryResponseDTO> response = new  HttpGlobalResponseDTO<>();

        Optional<Categories> categoryFound = categoriesRepository.findByName(category.getName());

        if (categoryFound.isPresent()) {
            response.setMessage("¡Ey mano!, esa categoría ya existe (409)");
            response.setData(null);
            return response;
        }

        
        Categories categories = new Categories();
        categories.setName(category.getName());
        categoriesRepository.save(categories);

        CategoryResponseDTO dto = new CategoryResponseDTO();
        
        response.setMessage("¡Categoría creada con éxito mi papacho!");
        response.setData(dto);
        return response;


    }

    public HttpGlobalResponseDTO<CategoryResponseDTO> deleteCategory(Long id) {
        HttpGlobalResponseDTO<CategoryResponseDTO> response = new HttpGlobalResponseDTO<>();
        Optional<Categories> categoryFound = categoriesRepository.findById(id);

        if (categoryFound.isEmpty()) {
            response.setMessage("Categoría no encontrada (404)");
            response.setData(null);
            return response;
        }

        Categories category = categoryFound.get();

        List<Products> allProducts = productsRepository.findAll();
        for (Products p : allProducts) {
            if (p.getCategoryId() != null && p.getCategoryId().equals(id)) {
                response.setMessage("¡Pilas!, no puedes borrar esta categoría porque tiene productos asociados (409)");
                response.setData(null);
                return response;
            }
        }

        categoriesRepository.delete(category);

        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());

        response.setMessage("¡Categoría borrada con éxito!");
        response.setData(dto);
        return response;

    }

}
