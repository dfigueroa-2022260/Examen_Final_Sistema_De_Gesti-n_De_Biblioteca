package com.biblioteca.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Libro {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String isbn;
    private String titulo;
    private String autor;
    private String categoria;
    private int stockTotal;
    private int stockDisponible;
    @OneToMany(mappedBy = "libro")
    @Builder.Default
    private List<Prestamo> prestamos = new ArrayList<>();
}
