package com.biblioteca.controller;

import com.biblioteca.dto.Dto.*;
import com.biblioteca.service.LibroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/libros")
@RequiredArgsConstructor
public class LibroController {
    private final LibroService service;

    @GetMapping
    public Page<LibroResponse> listar(@RequestParam(required = false) String titulo,
                                      @RequestParam(required = false) String categoria, Pageable pageable) {
        return service.listar(titulo, categoria, pageable);
    }

    @GetMapping("/{id}")
    public LibroResponse obtener(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LibroResponse crear(@Valid @RequestBody LibroRequest r) { return service.crear(r); }

    @PutMapping("/{id}")
    public LibroResponse actualizar(@PathVariable Long id, @Valid @RequestBody LibroRequest r) {
        return service.actualizar(id, r);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
