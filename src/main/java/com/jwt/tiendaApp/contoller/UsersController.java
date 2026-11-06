package com.jwt.tiendaApp.contoller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.tiendaApp.dtos.HttpGlobalResponseDTO;
import com.jwt.tiendaApp.dtos.UserDTO.UserRequestActiveDTO;
import com.jwt.tiendaApp.dtos.UserDTO.UserRequestDTO;
import com.jwt.tiendaApp.dtos.UserDTO.UserRequestRolIdDTO;
import com.jwt.tiendaApp.dtos.UserDTO.UserResponseDTO;
import com.jwt.tiendaApp.enums.RoleEnum;
import com.jwt.tiendaApp.security.RequiresRole;
import com.jwt.tiendaApp.service.UsersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/users")
@RequiredArgsConstructor 
public class UsersController {

    private  final UsersService usersService;

    @GetMapping("/me") 
    @RequiresRole({RoleEnum.ADMIN, RoleEnum.CLIENTE, RoleEnum.VENDEDOR})
    public ResponseEntity<HttpGlobalResponseDTO<UserResponseDTO>> getMe(
            HttpServletRequest request) {
        return ResponseEntity.ok(usersService.getMe(request));
    }

    @GetMapping
    @RequiresRole(RoleEnum.ADMIN)
    public ResponseEntity<HttpGlobalResponseDTO<List<UserResponseDTO>>> getUsers() {
        return ResponseEntity.ok(usersService.getUsers());
    }

    @PostMapping
    @RequiresRole(RoleEnum.ADMIN)
    public ResponseEntity<HttpGlobalResponseDTO<UserResponseDTO>> createUser(
            @Valid @RequestBody UserRequestDTO body) {
        return ResponseEntity.status(201).body(usersService.createUserByAdmin(body));
    }

     @PatchMapping("/{id}/role")
    @RequiresRole(RoleEnum.ADMIN)
    public ResponseEntity<HttpGlobalResponseDTO<UserResponseDTO>> updateRole(
            @PathVariable Long id, @Valid @RequestBody UserRequestRolIdDTO body) {
        return ResponseEntity.ok(usersService.updateRole(id, body));
    }

    @PatchMapping("/{id}/status")
    @RequiresRole(RoleEnum.ADMIN)
    public ResponseEntity<HttpGlobalResponseDTO<UserResponseDTO>> updateStatus(
            @PathVariable Long id, @Valid @RequestBody UserRequestActiveDTO body) {
        return ResponseEntity.ok(usersService.updateStatus(id, body));
    }
    
}
