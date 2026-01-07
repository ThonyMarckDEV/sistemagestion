package com.example.sistemagestion.config;

import com.example.sistemagestion.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Obtenemos el header "Authorization"
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Si no hay header o no empieza por "Bearer ", pasamos (no hacemos nada)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraemos el token (quitamos "Bearer " que son 7 caracteres)
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        // 4. Si hay email y el usuario no está autenticado todavía en el contexto
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Buscamos al usuario en la BD
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 5. Validamos el token
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Creamos la sesión de seguridad para esta petición
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // FINALMENTE: Actualizamos el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Pasamos al siguiente filtro
        filterChain.doFilter(request, response);
    }
}