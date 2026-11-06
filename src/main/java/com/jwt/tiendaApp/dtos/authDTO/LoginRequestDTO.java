package com.jwt.tiendaApp.dtos.authDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data 
public class LoginRequestDTO {

    @NotBlank(message = "El usuario es obligatorio")
    private  String user;

    @NotBlank(message = "La contraseña es obligatoria")
    private  String password;

}
