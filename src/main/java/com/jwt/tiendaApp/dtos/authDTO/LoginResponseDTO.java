package com.jwt.tiendaApp.dtos.authDTO;


import com.jwt.tiendaApp.dtos.UserDTO.UserResponseDTO;

import lombok.Data;

@Data 
public class LoginResponseDTO {
    
        private  String jwt;

        private UserResponseDTO user;

}
