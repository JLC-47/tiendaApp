package com.jwt.tiendaApp.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.tiendaApp.dto.HttpGlobalResponseDTO;
import com.jwt.tiendaApp.dto.LoginResponseDTO;
import com.jwt.tiendaApp.dto.RegisterRequestDTO;
import com.jwt.tiendaApp.dto.UserDTO;
import com.jwt.tiendaApp.entity.Roles;
import com.jwt.tiendaApp.entity.Users;
import com.jwt.tiendaApp.enums.RoleEnum;
import com.jwt.tiendaApp.repository.RolesRepository;
import com.jwt.tiendaApp.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class AuthService {
    
    private  final UsersRepository usersRepository;


    private final RolesRepository rolesRepository;


    private final PasswordEncoder passwordEncoder;

    private  final JwtService jwtService;


    public HttpGlobalResponseDTO<UserDTO> register(RegisterRequestDTO request){
        HttpGlobalResponseDTO<UserDTO> response = new  HttpGlobalResponseDTO<>();
        UserDTO userDTO = new UserDTO();

        Optional<Users> usernameFound = usersRepository.findByUsername(request.getUsername());
        Optional<Users> emailFound = usersRepository.findByEmail(request.getEmail());
        


        if (usernameFound.isPresent()) {
            response.setMesssage("El nombre de ususario ya está en uso.");
            return  response;
        }

        if (emailFound.isPresent()) {
            response.setMesssage("El correo de ususario ya está en uso.");
            return response;
        }


        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setActive(request.getActive());
        user.setId(RoleEnum.CLIENTE.getId());


        usersRepository.save(user);
    
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setActive(user.getActive());
        userDTO.setRol(RoleEnum.CLIENTE);

        response.setMesssage("Usuario registrado correctamente");
        response.setData(userDTO);
        return  response;

    }


    public HttpGlobalResponseDTO<LoginResponseDTO> login(LoginResponseDTO request){
        HttpGlobalResponseDTO<LoginResponseDTO> response = new  HttpGlobalResponseDTO<>();
        Optional<Users> email = usersRepository.findByEmail(request.getUser());

    }

    
}
