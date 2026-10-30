package com.jwt.tiendaApp.dto;

import lombok.Data;

@Data 
public class LoginRequestDTO {
    
    private  String user;

    private  String password;

}
