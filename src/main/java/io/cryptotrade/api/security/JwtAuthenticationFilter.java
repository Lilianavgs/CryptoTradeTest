package io.cryptotrade.api.security;

import io.cryptotrade.api.service.SessionService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final SessionService sessionService;  // Inyecta tu servicio de sesiones

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);

        try {
            if (!jwtUtils.validateToken(token)) {
                // Token inválido: extraer sessionId y desactivar sesión
                String sessionIdStr = jwtUtils.extractSessionIdFromToken(token);
                if (sessionIdStr != null) {
                    sessionService.deactivateSessionById(sessionIdStr);
                }
                // Limpiar contexto para evitar autenticación
                SecurityContextHolder.clearContext();
                // Opcional: puedes enviar respuesta 401 aquí o dejar que continúe y sea manejado por EntryPoint
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{error: Token inválido o expirado.}");
                response.getWriter().flush();
                return; // Detener el filtro aquí
            }

            Integer userId = jwtUtils.getUserIdFromToken(token);
            var userDetails = userDetailsService.loadUserById(userId);
            var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            // En caso de otro error, limpiar contexto y continuar
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Error en autenticación.\"}");
            response.getWriter().flush();
            return;
        }

        filterChain.doFilter(request, response);
    }
}
