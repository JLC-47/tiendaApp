package com.jwt.tiendaApp.dtos.UserDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class UserRequestRolIdDTO {
    
    @NotNull(message = "El rol es obligatorio")
    private  Long rolId;
    
}
