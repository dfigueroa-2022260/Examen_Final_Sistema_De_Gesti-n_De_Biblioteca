package com.biblioteca.repository;

import com.biblioteca.model.Enums.EstadoPrestamo;
import com.biblioteca.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    long countByUsuarioIdAndEstadoIn(Long usuarioId, List<EstadoPrestamo> estados);
    List<Prestamo> findByUsuarioIdAndEstadoInAndFechaDevolucionEsperadaBefore(
            Long usuarioId, List<EstadoPrestamo> estados, LocalDate fecha);
    List<Prestamo> findByUsuarioEmailOrderByFechaPrestamoDesc(String email);
    List<Prestamo> findByEstadoInAndFechaDevolucionEsperadaBefore(List<EstadoPrestamo> estados, LocalDate fecha);
}
