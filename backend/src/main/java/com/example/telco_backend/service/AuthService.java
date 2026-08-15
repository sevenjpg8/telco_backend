package com.example.telco_backend.service;

import com.example.telco_backend.config.JwtService;
import com.example.telco_backend.dto.LoginRequest;
import com.example.telco_backend.dto.LoginResponse;
import com.example.telco_backend.entity.Usuario;
import com.example.telco_backend.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario y/o contraseña incorrectos"));

        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            throw new RuntimeException( "Usuario y/o contraseña incorrectos");
        } 

        String token = jwtService.generateToken(usuario.getUsername(), usuario.getRol().name());

        return new LoginResponse(
                token,
                usuario.getRol().name()
        );
    }
}
