package com.biblioteca.service;

import com.biblioteca.dto.Dto.*;
import com.biblioteca.exception.BusinessRuleException;
import com.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.model.*;
import com.biblioteca.model.Enums.*;
import com.biblioteca.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrestamoService {
    private static final List<EstadoPrestamo> ABIERTOS = List.of(EstadoPrestamo.ACTIVO, EstadoPrestamo.ATRASADO);
    private static final int MAX_ACTIVOS = 3;
    private static final int DIAS_PLAZO = 14;

    private final PrestamoRepository prestamos;
    private final UsuarioRepository usuarios;
    private final LibroRepository libros;

    // noRollbackFor: la sanción debe guardarse aunque se lance la excepción
    @Transactional(noRollbackFor = BusinessRuleException.class)
    public PrestamoResponse prestar(PrestamoRequest r) {
        Usuario u = usuarios.findById(r.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + r.usuarioId()));
        Libro l = libros.findById(r.libroId())
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado: " + r.libroId()));

        // Sanción automática si tiene préstamos vencidos
        var vencidos = prestamos.findByUsuarioIdAndEstadoInAndFechaDevolucionEsperadaBefore(
                u.getId(), ABIERTOS, LocalDate.now());
        if (!vencidos.isEmpty()) {
            vencidos.forEach(p -> p.setEstado(EstadoPrestamo.ATRASADO));
            u.setEstado(EstadoUsuario.SANCIONADO);
            throw new BusinessRuleException("Usuario sancionado por préstamos sin devolver a tiempo");
        }
        if (u.getEstado() == EstadoUsuario.SANCIONADO)
            throw new BusinessRuleException("Usuario sancionado");
        if (prestamos.countByUsuarioIdAndEstadoIn(u.getId(), ABIERTOS) >= MAX_ACTIVOS)
            throw new BusinessRuleException("El usuario ya tiene " + MAX_ACTIVOS + " préstamos activos");
        if (l.getStockDisponible() <= 0)
            throw new BusinessRuleException("No hay ejemplares disponibles de este libro");

        l.setStockDisponible(l.getStockDisponible() - 1);
        LocalDate hoy = LocalDate.now();
        Prestamo p = prestamos.save(Prestamo.builder().usuario(u).libro(l).fechaPrestamo(hoy)
                .fechaDevolucionEsperada(hoy.plusDays(DIAS_PLAZO)).estado(EstadoPrestamo.ACTIVO).build());
        return toDto(p);
    }

    @Transactional
    public PrestamoResponse devolver(Long id) {
        Prestamo p = prestamos.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado: " + id));
        if (p.getEstado() == EstadoPrestamo.DEVUELTO)
            throw new BusinessRuleException("El préstamo ya fue devuelto");
        p.setEstado(EstadoPrestamo.DEVUELTO);
        p.setFechaDevolucionReal(LocalDate.now());
        Libro l = p.getLibro();
        l.setStockDisponible(l.getStockDisponible() + 1);

        // Si ya no le quedan préstamos vencidos, se levanta la sanción
        Usuario u = p.getUsuario();
        boolean quedanVencidos = !prestamos.findByUsuarioIdAndEstadoInAndFechaDevolucionEsperadaBefore(
                u.getId(), ABIERTOS, LocalDate.now()).isEmpty();
        if (!quedanVencidos) u.setEstado(EstadoUsuario.ACTIVO);
        return toDto(p);
    }

    @Transactional(readOnly = true)
    public List<PrestamoResponse> misPrestamos(String email) {
        return prestamos.findByUsuarioEmailOrderByFechaPrestamoDesc(email).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<PrestamoResponse> atrasados() {
        return prestamos.findByEstadoInAndFechaDevolucionEsperadaBefore(ABIERTOS, LocalDate.now())
                .stream().map(this::toDto).toList();
    }

    private PrestamoResponse toDto(Prestamo p) {
        return new PrestamoResponse(p.getId(), p.getUsuario().getId(), p.getUsuario().getEmail(),
                p.getLibro().getId(), p.getLibro().getTitulo(), p.getFechaPrestamo(),
                p.getFechaDevolucionEsperada(), p.getFechaDevolucionReal(), p.getEstado());
    }
}
