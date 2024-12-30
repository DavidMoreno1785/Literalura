package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IAutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findAutorByNombreContainsIgnoreCase(String nombreAutor);

}
