package com.jwt.tiendaApp.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.tiendaApp.dtos.HttpGlobalResponseDTO;
import com.jwt.tiendaApp.dtos.UserDTO.UserDTO;
import com.jwt.tiendaApp.entity.Roles;
import com.jwt.tiendaApp.entity.Users;
import com.jwt.tiendaApp.enums.RoleEnum;
import com.jwt.tiendaApp.repository.RolesRepository;
import com.jwt.tiendaApp.repository.UsersRepository;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class UsersService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * método para consultar el perfil atravez del id
     * @param id
     * @return
     */

    public  HttpGlobalResponseDTO<UserDTO> getMe(Long id) {
        HttpGlobalResponseDTO<UserDTO> response = new HttpGlobalResponseDTO<>();
        UserDTO userDTO = new  UserDTO();

        Optional<Users> userFound = usersRepository.findById(id);

        if (userFound.isEmpty()) {
            response.setMessage("¡Vos quién sos!, andate de acá (404)");
            response.setData(null);
            return  response;
        }

        Users user = userFound.get(); 

        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setActive(user.getActive());
        userDTO.setRolId(RoleEnum.fromId(user.getRolId()));

        response.setMessage("¡Mi papacho!, vea aqui estan sus datos😎: ");
        response.setData(userDTO);
        return response;

    }


    /**
     * metodo para obtener todos los usuarios (role = ADMIN)
     * @return
     */

    public HttpGlobalResponseDTO<List<UserDTO>> getUsers() {
        HttpGlobalResponseDTO<List<UserDTO>> response = new  HttpGlobalResponseDTO<>();
        List<Users> userList = usersRepository.findAll();

        List<UserDTO> userDTOList = new ArrayList<>();
        
        
        for (Users users : userList) {
            UserDTO user = new  UserDTO();
            user.setId(users.getId());
            user.setUsername(users.getUsername());
            user.setEmail(users.getEmail());
            user.setActive(users.getActive());
            user.setRolId(RoleEnum.fromId(users.getRolId()));

            userDTOList.add(user);
            
        }

        response.setMessage("¡Mi locooo!, ya encontramos los usuarios mira: ");
        response.setData(userDTOList);
        return response;

    }


    /**
     * Crea un usuario con cualquier rol (Rol: ADMIN -> para crear VENDEDORES u otros ADMIN)
     * @param username
     * @param email
     * @param password
     * @param rolId
     * @return
     */

    public HttpGlobalResponseDTO<UserDTO> createUserByAdmin(String username, String email, String password, Long rolId) {
        HttpGlobalResponseDTO<UserDTO> response = new HttpGlobalResponseDTO<>();

        
        List<Users> allUsers = usersRepository.findAll();
        for (Users u : allUsers) {
            if (u.getUsername().equalsIgnoreCase(username) || u.getEmail().equalsIgnoreCase(email)) {
                response.setMessage("¡Pilas!, el username o el email ya están registrados (409)");
                response.setData(null);
                return response;
            }
        }

        Optional<Roles> roleOpt = rolesRepository.findById(rolId);
        if (roleOpt.isEmpty()) {
            response.setMessage("Rol no encontrado (404)");
            response.setData(null);
            return response;
        }

        Users user = new Users();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setActive(true);
        user.setRolId(rolId);

        Users saved = usersRepository.save(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(saved.getId());
        userDTO.setUsername(saved.getUsername());
        userDTO.setEmail(saved.getEmail());
        userDTO.setActive(saved.getActive());
        userDTO.setRolId(RoleEnum.fromId(saved.getRolId()));

        response.setMessage("¡Usuario creado con éxito por el Admin! 🔥");
        response.setData(userDTO);
        return response;
    }


    public HttpGlobalResponseDTO<UserDTO> updateRole(Long id, Long rolId) {
        HttpGlobalResponseDTO<UserDTO> response = new HttpGlobalResponseDTO<>();
        Optional<Users> userFound = usersRepository.findById(id);

        if (userFound.isEmpty()) {
            response.setMessage("Usuario no encontrado (404)");
            response.setData(null);
            return response;
        }

        Optional<Roles> roleOpt = rolesRepository.findById(rolId);
        if (roleOpt.isEmpty()) {
            response.setMessage("Rol no encontrado (404)");
            response.setData(null);
            return response;
        }

        Users user = userFound.get();
        user.setRolId(rolId);
        Users updated = usersRepository.save(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(updated.getId());
        userDTO.setUsername(updated.getUsername());
        userDTO.setEmail(updated.getEmail());
        userDTO.setActive(updated.getActive());
        userDTO.setRolId(RoleEnum.fromId(updated.getRolId()));

        response.setMessage("¡Rol actualizado correctamente!");
        response.setData(userDTO);
        return response;
    }

    public HttpGlobalResponseDTO<UserDTO> updateStatus(Long id, boolean active) {
        HttpGlobalResponseDTO<UserDTO> response = new HttpGlobalResponseDTO<>();
        Optional<Users> userFound = usersRepository.findById(id);

        if (userFound.isEmpty()) {
            response.setMessage("Usuario no encontrado (404)");
            response.setData(null);
            return response;
        }

        Users user = userFound.get();
        user.setActive(active);
        Users updated = usersRepository.save(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(updated.getId());
        userDTO.setUsername(updated.getUsername());
        userDTO.setEmail(updated.getEmail());
        userDTO.setActive(updated.getActive());
        userDTO.setRolId(RoleEnum.fromId(updated.getRolId()));

        response.setMessage("¡Estado del usuario actualizado con éxito!");
        response.setData(userDTO);
        return response;
    }




    
}
