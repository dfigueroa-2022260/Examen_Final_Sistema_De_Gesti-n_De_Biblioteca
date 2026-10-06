package com.biblioteca.dto;

import com.biblioteca.model.Enums.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class Dto {
    public record RegisterRequest(@NotBlank String nombre, @Email @NotBlank String email,
                                  @NotBlank @Size(min = 6) String password) {}
    public record LoginRequest(@NotBlank String email, @NotBlank String password) {}
    public record AuthResponse(String token, String email, Rol rol) {}
    public record UsuarioResponse(Long id, String nombre, String email, Rol rol, EstadoUsuario estado) {}

    public record LibroRequest(@NotBlank String isbn, @NotBlank String titulo, @NotBlank String autor,
                               String categoria, @Min(0) int stockTotal, @Min(0) int stockDisponible) {}
    public record LibroResponse(Long id, String isbn, String titulo, String autor, String categoria,
                                int stockTotal, int stockDisponible) {}

    public record PrestamoRequest(@NotNull Long usuarioId, @NotNull Long libroId) {}
    public record PrestamoResponse(Long id, Long usuarioId, String usuarioEmail, Long libroId, String libroTitulo,
                                   LocalDate fechaPrestamo, LocalDate fechaDevolucionEsperada,
                                   LocalDate fechaDevolucionReal, EstadoPrestamo estado) {}

    public record ErrorResponse(int status, String error, String message, String timestamp) {}
}
