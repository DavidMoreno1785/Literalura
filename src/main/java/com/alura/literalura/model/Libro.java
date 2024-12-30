package com.alura.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String idioma;
    private Long cantidadDescargas;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Autor> autores;

    public Libro(){}

    public Libro(DatosLibro datosLibro){
        this.titulo = datosLibro.titulo();
        this.idioma = datosLibro.idiomas().getFirst();
        this.cantidadDescargas = datosLibro.cantidadDescargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Long getCantidadDescargas() {
        return cantidadDescargas;
    }

    public void setCantidadDescargas(Long cantidadDescargas) {
        this.cantidadDescargas = cantidadDescargas;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public void addAutor(List<Autor> autores) {
       autores.forEach(autor -> autor.setLibros(List.of(this)));
       this.autores = autores;
    }

    public void mostrar(){
        System.out.println("----- LIBRO -----\n"  +
                                   "Titulo: " + titulo + "\n" +
                                   "Autor: " + autores.stream().map(autor -> autor.getNombre().replaceAll(",", "")).toList()+ "\n" +
                                   "Idioma: " + idioma +  "\n" +
                                   "Numero de descargas: " + cantidadDescargas +
                                   "\n---------------\n");
    }
}

