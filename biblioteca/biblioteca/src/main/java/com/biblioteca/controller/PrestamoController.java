package com.biblioteca.controller;

import com.biblioteca.dto.Dto.*;
import com.biblioteca.service.PrestamoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/prestamos")
@RequiredArgsConstructor
public class PrestamoController {
    private final PrestamoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrestamoResponse prestar(@Valid @RequestBody PrestamoRequest r) { return service.prestar(r); }

    @PatchMapping("/{id}/devolucion")
    public PrestamoResponse devolver(@PathVariable Long id) { return service.devolver(id); }

    @GetMapping("/mis-prestamos")
    public List<PrestamoResponse> misPrestamos(Authentication auth) { return service.misPrestamos(auth.getName()); }

    @GetMapping("/atrasados")
    public List<PrestamoResponse> atrasados() { return service.atrasados(); }
}
