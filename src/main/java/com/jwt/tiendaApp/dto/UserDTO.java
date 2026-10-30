package com.jwt.tiendaApp.dto;

import com.jwt.tiendaApp.enums.RoleEnum;

import lombok.Data;

@Data 
public class UserDTO {
  
   private Long id;
   
   private  String username;

   private String email;

   private Boolean active;

   private  RoleEnum rol;

}
