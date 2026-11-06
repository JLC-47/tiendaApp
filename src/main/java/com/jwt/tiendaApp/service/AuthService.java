package com.jwt.tiendaApp.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.tiendaApp.dtos.HttpGlobalResponseDTO;
import com.jwt.tiendaApp.dtos.UserDTO.UserResponseDTO;
import com.jwt.tiendaApp.dtos.authDTO.LoginRequestDTO;
import com.jwt.tiendaApp.dtos.authDTO.LoginResponseDTO;
import com.jwt.tiendaApp.dtos.authDTO.RegisterRequestDTO;
import com.jwt.tiendaApp.entity.Users;
import com.jwt.tiendaApp.enums.RoleEnum;
import com.jwt.tiendaApp.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public HttpGlobalResponseDTO<UserResponseDTO> register(RegisterRequestDTO request) {
        HttpGlobalResponseDTO<UserResponseDTO> response = new HttpGlobalResponseDTO<>();
        UserResponseDTO userDTO = new UserResponseDTO();

        Optional<Users> usernameFound = usersRepository.findByUsername(request.getUsername());
        Optional<Users> emailFound = usersRepository.findByEmail(request.getEmail());

        if (usernameFound.isPresent()) {
            response.setMessage("El nombre de ususario ya está en uso.");
            return response;
        }

        if (emailFound.isPresent()) {
            response.setMessage("El correo de ususario ya está en uso.");
            return response;
        }

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        user.setRolId(RoleEnum.CLIENTE.getId());

        usersRepository.save(user);

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setActive(user.getActive());
        userDTO.setRolId(RoleEnum.CLIENTE);

        response.setMessage("Usuario registrado correctamente");
        response.setData(userDTO);
        return response;

    }

    public HttpGlobalResponseDTO<LoginResponseDTO> login(LoginRequestDTO request) {
        HttpGlobalResponseDTO<LoginResponseDTO> response = new HttpGlobalResponseDTO<>();
        Optional<Users> email = usersRepository.findByEmail(request.getUser());
        Optional<Users> username = usersRepository.findByUsername(request.getUser());

        if (username.isEmpty() && email.isEmpty()) {
            response.setMessage("Usuario y/o contraseña incorrectos");
            return response;
        }

        Users userFound;

        
        if (email.isPresent()) {
            userFound = email.get();
        } else {
            userFound = username.get();
        }

        if (!userFound.getActive()) {
            response.setMessage("Usuario inactivo, contacta al administrador");
            return response;
        }
        
        if (!passwordEncoder.matches(request.getPassword(), userFound.getPassword())) {
            response.setMessage("Usuario y/o contraseña incorrectos");
            return response;
        }

        UserResponseDTO user = new UserResponseDTO();
        user.setUsername(userFound.getUsername());
        user.setEmail(userFound.getEmail());
        user.setActive(userFound.getActive());
        user.setRolId(RoleEnum.fromId(userFound.getRolId()));

        String jwt = jwtService.generateToken(userFound.getId(), userFound.getUsername(), userFound.getRolId());
        LoginResponseDTO token = new LoginResponseDTO();
        token.setJwt(jwt);
        token.setUser(user);
        ;

        response.setMessage("Iniciio de sesión exitoso");
        response.setData(token);

        return response;

    }

    public HttpGlobalResponseDTO<LoginResponseDTO> refreshToken(String token) throws Exception {
        HttpGlobalResponseDTO<LoginResponseDTO> response = new HttpGlobalResponseDTO<>();
        LoginResponseDTO loginResponse = new LoginResponseDTO();
        UserResponseDTO user = new UserResponseDTO();
        String jwt = jwtService.refreshToken(token);
        Long userId = jwtService.extractUserId(token);

        Optional<Users> userFound = usersRepository.findById(userId);

        if (userFound.isEmpty()) {
            response.setMessage("Usuario no encotrado");
            return response;
        }

        Users users = userFound.get();

        user.setUsername(users.getUsername());
        user.setEmail(users.getEmail());
        user.setRolId(RoleEnum.fromId(users.getRolId()));
        user.setActive(users.getActive());

        loginResponse.setJwt(jwt);
        loginResponse.setUser(user);

        response.setData(loginResponse);
        response.setMessage(
                "¡Oe, Sociooo! 🔄 Su token estaba más vencido que una empanada de hace tres días, pero fresco, mi sooo, ya le dimos su refrescadita. ¡Quedó más activo que chisme en peluquería! 😂");
        return response;

    }

}
