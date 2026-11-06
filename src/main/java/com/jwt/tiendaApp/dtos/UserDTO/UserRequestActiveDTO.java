package com.jwt.tiendaApp.dtos.UserDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class UserRequestActiveDTO {
    
    @NotNull(message = "El estado es obligatorio")
    private  Boolean active;

}
