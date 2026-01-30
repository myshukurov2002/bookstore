package com.bookstore.auth.api;

import com.bookstore.auth.app.AuthService;
import com.bookstore.security.JwtUtil;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}
    public record TokenResponse(String token) {}

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        if (!authService.authenticate(request.username(), request.password())) {
            return ResponseEntity.status(401).build();
        }
        var permissions = authService.permissionsFor(request.username());
        String token = jwtUtil.generateToken(request.username(), permissions);
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
