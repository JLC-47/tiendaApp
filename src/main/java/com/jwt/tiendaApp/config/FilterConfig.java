package com.jwt.tiendaApp.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jwt.tiendaApp.filter.JwtValidationFilter;

@Configuration 
public class FilterConfig {
    
    @Bean  
    FilterRegistrationBean<JwtValidationFilter> jwtFilter(JwtValidationFilter jwtValidationFilter){
        FilterRegistrationBean<JwtValidationFilter> registrationBean = new FilterRegistrationBean<>();
        
        registrationBean.setFilter(jwtValidationFilter);

        registrationBean.setOrder(1);

        return  registrationBean;

    }
}
