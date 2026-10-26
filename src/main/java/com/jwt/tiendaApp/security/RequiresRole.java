package com.jwt.tiendaApp.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jwt.tiendaApp.enums.RoleEnum;

@Target(ElementType.METHOD)


@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {

  RoleEnum[] value();
} 
