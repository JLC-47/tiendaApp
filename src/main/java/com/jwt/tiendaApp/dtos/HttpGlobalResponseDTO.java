package com.jwt.tiendaApp.dtos;

import lombok.Data;

@Data 
public class HttpGlobalResponseDTO<T> {
    
    private  String message;

    private  T data;

}
