package com.biblioteca.service;

import com.biblioteca.dto.Dto.*;
import com.biblioteca.exception.BusinessRuleException;
import com.biblioteca.model.Enums.*;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.UsuarioRepository;
import com.biblioteca.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwt;

    public UsuarioResponse register(RegisterRequest r) {
        if (repo.existsByEmail(r.email())) throw new BusinessRuleException("El email ya está registrado");
        Usuario u = repo.save(Usuario.builder().nombre(r.nombre()).email(r.email())
                .password(encoder.encode(r.password())).estado(EstadoUsuario.ACTIVO).rol(Rol.LECTOR).build());
        return new UsuarioResponse(u.getId(), u.getNombre(), u.getEmail(), u.getRol(), u.getEstado());
    }


    public AuthResponse login(LoginRequest r) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(r.email(), r.password()));
        Usuario u = repo.findByEmail(r.email()).orElseThrow();
        return new AuthResponse(jwt.generate(u.getEmail(), u.getRol().name()), u.getEmail(), u.getRol());
    }
}
