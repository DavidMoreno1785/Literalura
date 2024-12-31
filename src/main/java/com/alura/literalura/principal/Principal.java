package com.alura.literalura.principal;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.IAutorRepository;
import com.alura.literalura.repository.ILibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private final Scanner teclado = new Scanner(System.in);
    private final ConsumoAPI consumirAPI = new ConsumoAPI();
    private final ILibroRepository libroRepository;
    private final IAutorRepository autorRepository;

    public Principal(ILibroRepository libroRepository, IAutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void menu(){
        boolean mostrarMenu = true;
        do {
            System.out.println("""
                               Elija la opción a través de su número:
                               1- Buscar libro por titulo.
                               2- Listar libros registrados.
                               3- Listar autores registrados.
                               4- Listas autores vivos en un determinado año.
                               5- Obtener libros por idioma.
                               0- Salir.
                               """);

            int opcion = teclado.nextInt();
            teclado.nextLine();


            switch (opcion){
                case 1 -> obtenerLibroPorTitulo();
                case 2 -> obtenerLibrosRegistrados();
                case 3 -> obtenerAutoresRegistrados();
                case 4 -> obtenerAutoresVivosEnUnDeterminadoAnio();
                case 5 -> obtenerLibrosPorIdioma();
                case 0 -> mostrarMenu = false;
            }

        }while (mostrarMenu);
    }


    private void obtenerLibroPorTitulo(){
        ResultadoBusqueda resultadoBusqueda = obtenerResultados();

        if(resultadoBusqueda.resultado().isEmpty()){
            System.out.println("El libro buscado, no se encontro.");
        }else{
            DatosLibro datosLibro = resultadoBusqueda.resultado().getFirst();
            List<DatosAutor> datosAutores = datosLibro.autors();

            if(libroRepository
                    .findLibroByTituloContainsIgnoreCase(datosLibro.titulo())
                    .isPresent()){
                System.out.println("El libro ya se encuentra registrado.");
            }else{
                Libro libro = new Libro(datosLibro);

                List<Autor> listaAutores = new ArrayList<>();
                datosAutores.forEach(autorObtenido -> {

                    Optional<Autor> autorEncontrado =
                            autorRepository.findAutorByNombreContainsIgnoreCase(autorObtenido.nombre());

                    if(autorEncontrado.isEmpty()){
                        Autor nuevoAutor = new Autor(autorObtenido);
                        listaAutores.add(nuevoAutor);
                        autorRepository.save(nuevoAutor);
                    }else {
                        listaAutores.add(autorEncontrado.get());
                    }
                });

                listaAutores.forEach(autor1 -> autor1.addLibro(List.of(libro)));
                libro.addAutor(listaAutores);

                libroRepository.save(libro);
                System.out.println("Libro registrado correctamente.");
            }
        }

    }

    private ResultadoBusqueda obtenerResultados(){
        System.out.println("Ingrese el titulo del libro: ");
        String tituloLibro = teclado.nextLine();

        String resultados = consumirAPI.obtenerDatos(URL_BASE + tituloLibro.replaceAll(" ", "+"));

        return Util.convertirDatos(resultados, ResultadoBusqueda.class);

    }

    private void obtenerLibrosRegistrados(){
        List<Libro> librosRegistrados =  libroRepository.findAll();
        librosRegistrados.forEach(Libro::mostrar);
    }
    private void obtenerAutoresRegistrados(){
        List<Autor> autoresRegistrados = autorRepository.findAll();
        autoresRegistrados.forEach(Autor::mostrar);
    }

    private  void obtenerAutoresVivosEnUnDeterminadoAnio(){
        System.out.println("Ingrese el año a buscar: ");
        int anio = teclado.nextInt();
        teclado.nextLine();
        Optional<List<Autor>> autoresVivos =
                autorRepository.buscarAutoresVivosPorAnio(anio);

        autoresVivos.ifPresent(autors -> autors.forEach(Autor::mostrar));
    }

    private void obtenerLibrosPorIdioma(){
        System.out.println("""
        Ingrese el idioma a buscar:
        - es: Español
        - en: Inglés
        - fr: Francés
        - pt: Portugués
        """);

        String idioma = teclado.nextLine().toLowerCase();

        Optional<List<Libro>> librosEncontrados = libroRepository.findByIdiomaEquals(idioma);

        if (librosEncontrados.isPresent()){
            if (librosEncontrados.get().isEmpty()){
                System.out.println("No se encontraron libros en ese idioma.");
            }else{
                librosEncontrados.get().forEach(Libro::mostrar);
            }
        }

    }
}
