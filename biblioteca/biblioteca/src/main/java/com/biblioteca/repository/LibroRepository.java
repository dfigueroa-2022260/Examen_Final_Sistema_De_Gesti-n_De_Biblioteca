package com.biblioteca.repository;

import com.biblioteca.model.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    @Query("""
           select l from Libro l
           where lower(l.titulo) like lower(concat('%', :titulo, '%'))
             and lower(coalesce(l.categoria, '')) like lower(concat('%', :categoria, '%'))
           """)
    Page<Libro> buscar(@Param("titulo") String titulo, @Param("categoria") String categoria, Pageable pageable);
}
