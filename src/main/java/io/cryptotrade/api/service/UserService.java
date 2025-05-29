package io.cryptotrade.api.service;


import io.cryptotrade.api.model.User;
import io.cryptotrade.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger userLogger =
            LoggerFactory.getLogger("io.cryptotrade.api.auth");


    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registerUser(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            userLogger.error("Intento de registro con email existente: {}", email);
            return false;
        }
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setPasswordExpiresAt(Instant.now().plusSeconds(90 * 24 * 3600));
        userRepository.save(user);
        userLogger.info("Usuario registrado con email: {}", email);
        return true;
    }

    public Optional<User> authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPasswordHash())) {
            userLogger.info("Usuario autenticado: {}", email);
            return userOpt;
        }
        userLogger.error("Fallo de autenticación para email: {}", email);
        return Optional.empty();
    }
}