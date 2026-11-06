package com.jwt.tiendaApp.exeption;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jwt.tiendaApp.dtos.HttpGlobalResponseDTO;

@RestControllerAdvice
public class GlobalExeptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpGlobalResponseDTO<Object>> handleException(Exception e) {
        HttpGlobalResponseDTO<Object> response = new HttpGlobalResponseDTO<>();
        response.setMessage("Error interno del servidor: " + e.getMessage());
        response.setData(null);
        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpGlobalResponseDTO<Object>> handleValidationException(MethodArgumentNotValidException e) {
        HttpGlobalResponseDTO<Object> response = new HttpGlobalResponseDTO<>();

        List<String> errors = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        response.setMessage("Errores de validación");
        response.setData(errors);
        return ResponseEntity.status(400).body(response);
    }

}
