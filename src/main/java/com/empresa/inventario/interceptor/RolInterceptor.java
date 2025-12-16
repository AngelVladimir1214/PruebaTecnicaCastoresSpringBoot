package com.empresa.inventario.interceptor;

import com.empresa.inventario.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class RolInterceptor implements HandlerInterceptor {

    private final String rolRequerido;

    public RolInterceptor(String rolRequerido) {
        this.rolRequerido = rolRequerido;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (!rolRequerido.equalsIgnoreCase(usuario.getRol().getNombre())) {
            response.sendRedirect("/acceso-denegado");
            return false;
        }

        return true;
    }
}
