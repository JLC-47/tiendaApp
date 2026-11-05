package com.jwt.tiendaApp.dtos.authDTO;

import lombok.Data;

@Data 
public class LoginRequestDTO {
    
    private  String user;

    private  String password;

}
