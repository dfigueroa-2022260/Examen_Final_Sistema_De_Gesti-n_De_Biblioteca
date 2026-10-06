package com.biblioteca.service;

import com.biblioteca.dto.Dto.*;
import com.biblioteca.exception.BusinessRuleException;
import com.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.model.Libro;
import com.biblioteca.repository.LibroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LibroService {
    private final LibroRepository repo;

    public Page<LibroResponse> listar(String titulo, String categoria, Pageable p) {
        return repo.buscar(titulo == null ? "" : titulo, categoria == null ? "" : categoria, p).map(this::toDto);
    }

    public LibroResponse obtener(Long id) { return toDto(buscar(id)); }

    @Transactional
    public LibroResponse crear(LibroRequest r) {
        validarStock(r);
        Libro l = new Libro();
        aplicar(l, r);
        return toDto(repo.save(l));
    }

    @Transactional
    public LibroResponse actualizar(Long id, LibroRequest r) {
        validarStock(r);
        Libro l = buscar(id);
        aplicar(l, r);
        return toDto(repo.save(l));
    }

    @Transactional
    public void eliminar(Long id) { repo.delete(buscar(id)); }

    private Libro buscar(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado: " + id));
    }

    private void validarStock(LibroRequest r) {
        if (r.stockDisponible() > r.stockTotal())
            throw new BusinessRuleException("El stock disponible no puede superar el stock total");
    }

    private void aplicar(Libro l, LibroRequest r) {
        l.setIsbn(r.isbn()); l.setTitulo(r.titulo()); l.setAutor(r.autor()); l.setCategoria(r.categoria());
        l.setStockTotal(r.stockTotal()); l.setStockDisponible(r.stockDisponible());
    }

    private LibroResponse toDto(Libro l) {
        return new LibroResponse(l.getId(), l.getIsbn(), l.getTitulo(), l.getAutor(), l.getCategoria(),
                l.getStockTotal(), l.getStockDisponible());
    }
}
