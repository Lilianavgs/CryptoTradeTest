package io.cryptotrade.api.controller;

import io.cryptotrade.api.model.Session;
import io.cryptotrade.api.security.JwtUtils;
import io.cryptotrade.api.service.SessionService;
import io.cryptotrade.api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final SessionService sessionService;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest request) {
        boolean created = userService.registerUser(request.getEmail(), request.getPassword());
        if (!created) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }
        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request, HttpServletRequest httpRequest) {
        return userService.authenticate(request.getEmail(), request.getPassword())
                .map(user -> {

                    // Verificar si ya existe una sesión activa para el usuario
                    List<Session> sesionesActivas = sessionService.getActiveSessionsByUser(user.getId());
                    if (!sesionesActivas.isEmpty()) {
                        return ResponseEntity
                                .status(HttpStatus.CONFLICT) // 409 Conflict
                                .body("El usuario ya tiene una sesión activa.");
                    }

                    String token = jwtUtils.generateToken(user.getId());

                    // Obtener IP del cliente
                    String ipAddress = httpRequest.getRemoteAddr();

                    // Crear registro de sesión
                    sessionService.createSession(user.getId(), ipAddress);

                    return ResponseEntity.ok(new AuthResponse(token));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());//401 UNAUTHORIZED
    }

    @Data
    private static class AuthRequest {
        private String email;
        private String password;
    }

    @Data
    private static class AuthResponse {
        private final String token;
    }
}
