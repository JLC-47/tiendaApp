package com.jwt.tiendaApp.dto;

import lombok.Data;

@Data 
public class RegisterRequestDTO {
    
    private String username;

    private String email;

    private  String password;

    private  Boolean active;

    
}
