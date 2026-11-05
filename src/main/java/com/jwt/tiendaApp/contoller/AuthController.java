package com.jwt.tiendaApp.contoller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.tiendaApp.dtos.HttpGlobalResponseDTO;
import com.jwt.tiendaApp.dtos.UserDTO.UserDTO;
import com.jwt.tiendaApp.dtos.authDTO.LoginRequestDTO;
import com.jwt.tiendaApp.dtos.authDTO.LoginResponseDTO;
import com.jwt.tiendaApp.dtos.authDTO.RegisterRequestDTO;
import com.jwt.tiendaApp.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("/auth")
@RequiredArgsConstructor 
public class AuthController {
    
    private  final AuthService authService;

    @PostMapping("register")
    public HttpGlobalResponseDTO<UserDTO> register(@RequestBody RegisterRequestDTO request) {
        HttpGlobalResponseDTO<UserDTO> response = authService.register(request);

        return response;
    }


    @PostMapping("login")
    public HttpGlobalResponseDTO<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        HttpGlobalResponseDTO<LoginResponseDTO> response = authService.login(request);

        return response;
    }


    @PostMapping("refreshtoken")
    public HttpGlobalResponseDTO<LoginResponseDTO> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        HttpGlobalResponseDTO<LoginResponseDTO> response = new  HttpGlobalResponseDTO<>();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setMessage("Token is invalid");        
        }

        String token = authHeader.replace("Bearer ", "");

        response = authService.resfreshToken(token);


        return response;
    }


    
    

}
