package com.empresa.inventario.config;

import com.empresa.inventario.interceptor.AuthInterceptor;
import com.empresa.inventario.interceptor.RolInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //Autenticación general
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/css/**", "/js/**");

        //ADMIN
        registry.addInterceptor(new RolInterceptor("ADMIN"))
                .addPathPatterns(
                        "/inventario/**",
                        "/movimientos/**"
                );

        //ALMACENISTA
        registry.addInterceptor(new RolInterceptor("ALMACENISTA"))
                .addPathPatterns(
                        "/salida/**"
                );
    }
}
