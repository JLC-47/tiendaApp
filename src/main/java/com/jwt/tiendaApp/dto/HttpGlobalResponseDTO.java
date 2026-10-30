package com.jwt.tiendaApp.dto;

import lombok.Data;

@Data 
public class HttpGlobalResponseDTO<T> {
    
    private  String messsage;

    private  T data;

}
