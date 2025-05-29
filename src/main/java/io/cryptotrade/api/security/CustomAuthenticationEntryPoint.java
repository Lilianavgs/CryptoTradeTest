package io.cryptotrade.api.security;

import io.cryptotrade.api.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final SessionService sessionService;

    public CustomAuthenticationEntryPoint(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // Aquí puedes obtener el token o sessionId del request para actualizar la sesión
        String token = extractTokenFromRequest(request);
        if (token != null) {
            String sessionId = extractSessionIdFromToken(token);
            if (sessionId != null) {
                sessionService.deactivateSessionById(sessionId);
            }
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private String extractSessionIdFromToken(String token) {
        // Usa tu JwtUtils para extraer sessionId del token
        // Por ejemplo:
        // return jwtUtils.extractSessionId(token).orElse(null);
        return null; // Implementa según tu JwtUtils
    }
}
