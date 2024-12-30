package com.alura.literalura.model;

import jakarta.persistence.*;


import java.util.List;
@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nombre;
    private Integer fechaNacimiento;
    private Integer fechaFallecimiento;
    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Libro> libros;


    public Autor(){}

    public Autor(DatosAutor datosAutor){
        this.nombre = datosAutor.nombre();
        this.fechaNacimiento = datosAutor.fechaNacimiento();
        this.fechaFallecimiento = datosAutor.fechaFallecimiento();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Integer fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getFechaFallecimiento() {
        return fechaFallecimiento;
    }

    public void setFechaFallecimiento(Integer fechaFallecimiento) {
        this.fechaFallecimiento = fechaFallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public void addLibro(List<Libro> libros) {
       libros.forEach(libro -> libro.setAutores(List.of(this)));
       this.libros = libros;
    }

    public void mostrar(){
        System.out.println( "----- AUTOR -----\n"  +
                                    "Autor: " + nombre + "\n" +
                                    "Libros: " + libros.stream().map(Libro::getTitulo).toList() + "\n" +
                                    "Fecha de nacimiento: " + fechaNacimiento + "\n" +
                                    "Fecha de fallecimiento: " + fechaFallecimiento +
                                    "\n---------------\n");
    }
}
