package com.jwt.tiendaApp.dtos.authDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data 
public class RegisterRequestDTO {
    
    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private  String password;

    private  Boolean active;

    
}
