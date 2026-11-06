package com.jwt.tiendaApp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.tiendaApp.dtos.HttpGlobalResponseDTO;
import com.jwt.tiendaApp.dtos.UserDTO.UserRequestActiveDTO;
import com.jwt.tiendaApp.dtos.UserDTO.UserRequestDTO;
import com.jwt.tiendaApp.dtos.UserDTO.UserRequestRolIdDTO;
import com.jwt.tiendaApp.dtos.UserDTO.UserResponseDTO;
import com.jwt.tiendaApp.entity.Roles;
import com.jwt.tiendaApp.entity.Users;
import com.jwt.tiendaApp.enums.RoleEnum;
import com.jwt.tiendaApp.repository.RolesRepository;
import com.jwt.tiendaApp.repository.UsersRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * método para consultar el perfil a traves del id
     * 
     * @param id
     * @return
     */

    public HttpGlobalResponseDTO<UserResponseDTO> getMe(HttpServletRequest request) {
        HttpGlobalResponseDTO<UserResponseDTO> response = new HttpGlobalResponseDTO<>();

        Long id = (Long) request.getAttribute("userId"); // del token

        Optional<Users> userFound = usersRepository.findById(id);

        if (userFound.isEmpty()) {
            response.setMessage("¡Vos quién sos!, andate de acá (404)");
            response.setData(null);
            response.setStatus(false);
            return response;
        }

        Users user = userFound.get();
        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setActive(user.getActive());
        userDTO.setRolId(RoleEnum.fromId(user.getRolId()));

        response.setMessage("¡Mi papacho!, vea aqui estan sus datos😎: ");
        response.setData(userDTO);
        response.setStatus(true);
        return response;
    }

    /**
     * metodo para obtener todos los usuarios (role = ADMIN)
     * 
     * @return
     */

    public HttpGlobalResponseDTO<List<UserResponseDTO>> getUsers() {
        HttpGlobalResponseDTO<List<UserResponseDTO>> response = new HttpGlobalResponseDTO<>();
        List<Users> userList = usersRepository.findAll();

        List<UserResponseDTO> userDTOList = new ArrayList<>();

        for (Users users : userList) {
            UserResponseDTO user = new UserResponseDTO();
            user.setId(users.getId());
            user.setUsername(users.getUsername());
            user.setEmail(users.getEmail());
            user.setActive(users.getActive());
            user.setRolId(RoleEnum.fromId(users.getRolId()));

            userDTOList.add(user);

        }

        response.setMessage("¡Mi locooo!, ya encontramos los usuarios mira: ");
        response.setData(userDTOList);
        response.setStatus(true);
        return response;

    }

    /**
     * Crea un usuario con cualquier rol (Rol: ADMIN -> para crear VENDEDORES u
     * otros ADMIN)
     * 
     * @param username
     * @param email
     * @param password
     * @param rolId
     * @return
     */

    public HttpGlobalResponseDTO<UserResponseDTO> createUserByAdmin(UserRequestDTO request) {
        HttpGlobalResponseDTO<UserResponseDTO> response = new HttpGlobalResponseDTO<>();
        Optional<Users> usernameFound = usersRepository.findByUsername(request.getUsername());
        Optional<Users> emailFound = usersRepository.findByEmail(request.getEmail());

        if (usernameFound.isPresent()) {
            response.setMessage("El nombre de ususario ya está en uso.");
            response.setStatus(false);
            return response;
        }

        if (emailFound.isPresent()) {
            response.setMessage("El correo de ususario ya está en uso.");
            response.setStatus(false);
            return response;
        }

        Optional<Roles> roleOpt = rolesRepository.findById(request.getRolId());
        if (roleOpt.isEmpty()) {
            response.setMessage("Rol no encontrado (404)");
            response.setData(null);
            response.setStatus(false);
            return response;
        }

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        user.setRolId(request.getRolId());

        Users saved = usersRepository.save(user);

        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setId(saved.getId());
        userDTO.setUsername(saved.getUsername());
        userDTO.setEmail(saved.getEmail());
        userDTO.setActive(saved.getActive());
        userDTO.setRolId(RoleEnum.fromId(saved.getRolId()));

        response.setMessage("¡Usuario creado con éxito por el Admin! 🔥");
        response.setData(userDTO);
        response.setStatus(true);
        return response;
    }

    public HttpGlobalResponseDTO<UserResponseDTO> updateRole(Long id, UserRequestRolIdDTO request) {
        HttpGlobalResponseDTO<UserResponseDTO> response = new HttpGlobalResponseDTO<>();
        Optional<Users> userFound = usersRepository.findById(id);

        if (userFound.isEmpty()) {
            response.setMessage("Usuario no encontrado (404)");
            response.setData(null);
            response.setStatus(false);
            return response;
        }

        Optional<Roles> roleOpt = rolesRepository.findById(request.getRolId());
        if (roleOpt.isEmpty()) {
            response.setMessage("Rol no encontrado (404)");
            response.setData(null);
            response.setStatus(false);
            return response;
        }

        Users user = userFound.get();
        user.setRolId(request.getRolId());
        Users updated = usersRepository.save(user);

        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setId(updated.getId());
        userDTO.setUsername(updated.getUsername());
        userDTO.setEmail(updated.getEmail());
        userDTO.setActive(updated.getActive());
        userDTO.setRolId(RoleEnum.fromId(updated.getRolId()));

        response.setMessage("¡Rol actualizado correctamente!");
        response.setData(userDTO);
        response.setStatus(true);
        return response;
    }

    public HttpGlobalResponseDTO<UserResponseDTO> updateStatus(Long id, UserRequestActiveDTO request) {
        HttpGlobalResponseDTO<UserResponseDTO> response = new HttpGlobalResponseDTO<>();
        Optional<Users> userFound = usersRepository.findById(id);

        if (userFound.isEmpty()) {
            response.setMessage("Usuario no encontrado (404)");
            response.setData(null);
            response.setStatus(false);
            return response;
        }

        Users user = userFound.get();
        user.setActive(request.getActive());
        Users updated = usersRepository.save(user);

        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setId(updated.getId());
        userDTO.setUsername(updated.getUsername());
        userDTO.setEmail(updated.getEmail());
        userDTO.setActive(updated.getActive());
        userDTO.setRolId(RoleEnum.fromId(updated.getRolId()));

        response.setMessage("¡Estado del usuario actualizado con éxito!");
        response.setData(userDTO);
        response.setStatus(true);
        return response;
    }

}
