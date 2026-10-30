package com.jwt.tiendaApp.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
public enum OrderStatusEnum {
    PENDIENTE(1L),
    ENVIADO(2L),
    ENTREGADO(3L),
    CANCELADO(4L);

    private final Long id;

    public Long getId(){
        return id;
    }


    public static OrderStatusEnum fromId(Long id){
        if (id == null) {
            return null;
        }
        for (OrderStatusEnum role : OrderStatusEnum.values()) {
            if (role.getId().equals(id)) {
                return  role;
            }
        }
        throw new  IllegalArgumentException("Id de la orden no valida: " + id);
    } 

}


