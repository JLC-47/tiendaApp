package com.jwt.tiendaApp.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.jwt.tiendaApp.dto.HttpGlobalResponseDTO;
import com.jwt.tiendaApp.dto.UserDTO;
import com.jwt.tiendaApp.entity.Users;
import com.jwt.tiendaApp.enums.RoleEnum;
import com.jwt.tiendaApp.repository.UsersRepository;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class UsersService {

    private final UsersRepository usersRepository;

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
            response.setMessage("¡Vo quien so!, andate de aca");
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




    
}
