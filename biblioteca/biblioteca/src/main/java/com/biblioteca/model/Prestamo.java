package com.biblioteca.model;

import com.biblioteca.model.Enums.EstadoPrestamo;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Prestamo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "libro_id")
    private Libro libro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionEsperada;
    private LocalDate fechaDevolucionReal;
    @Enumerated(EnumType.STRING)
    private EstadoPrestamo estado;
}
