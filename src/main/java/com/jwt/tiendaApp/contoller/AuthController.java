package com.jwt.tiendaApp.contoller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.tiendaApp.dtos.HttpGlobalResponseDTO;
import com.jwt.tiendaApp.dtos.UserDTO.UserResponseDTO;
import com.jwt.tiendaApp.dtos.authDTO.LoginRequestDTO;
import com.jwt.tiendaApp.dtos.authDTO.LoginResponseDTO;
import com.jwt.tiendaApp.dtos.authDTO.RegisterRequestDTO;
import com.jwt.tiendaApp.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("register")
    public ResponseEntity<HttpGlobalResponseDTO<UserResponseDTO>> register(
            @Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.status(201).body(authService.register(request));
    }

    @PostMapping("login")
    public ResponseEntity<HttpGlobalResponseDTO<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("refreshtoken")
    public ResponseEntity<HttpGlobalResponseDTO<LoginResponseDTO>> refreshToken(
            HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            HttpGlobalResponseDTO<LoginResponseDTO> response = new HttpGlobalResponseDTO<>();
            response.setMessage("Token inválido o ausente (401)");
            return ResponseEntity.status(401).body(response);
        }

        String token = authHeader.replace("Bearer ", "");

        try {
            return ResponseEntity.ok(authService.refreshToken(token));
        } catch (Exception e) {
            HttpGlobalResponseDTO<LoginResponseDTO> response = new HttpGlobalResponseDTO<>();
            response.setMessage("Token inválido o expirado (401)");
            return ResponseEntity.status(401).body(response);
        }
    }

}
