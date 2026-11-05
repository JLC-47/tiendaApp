package com.jwt.tiendaApp.dtos.UserDTO;

import lombok.Data;

@Data
public class UserRequestDTO {

    private String username;

    private String email;

    private String password;

    private Boolean active;

    private Long rolId;

}
