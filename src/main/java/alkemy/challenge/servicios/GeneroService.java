package alkemy.challenge.servicios;

import alkemy.challenge.entidades.Genero;
import alkemy.challenge.entidades.Pelicula;
import alkemy.challenge.enumeraciones.TituloGenero;
import alkemy.challenge.repositorios.GeneroRepository;
import alkemy.challenge.repositorios.PeliculaRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GeneroService {

    @Autowired
    private GeneroRepository generoRepository;
    @Autowired
    private PeliculaRepository peliculaRepository;

    @Transactional
    public void agregar(MultipartFile archivo, List<Pelicula> peliculas, TituloGenero nombre) throws Error, IOException {

        validar(archivo, peliculas, nombre);
        List<Pelicula> respuestas = validarPeliculas(peliculas);

        Genero genero = new Genero();
        genero.setImagen(archivo.getBytes());
        genero.setNombre(nombre);
        genero.setPeliculas(peliculas);

        generoRepository.save(genero);

    }

    @Transactional
    public void modificar(MultipartFile archivo, List<Pelicula> peliculas, TituloGenero nombre, String id) throws Error, IOException {

        Optional<Genero> respuesta = generoRepository.findById(id);
        if (respuesta.isPresent()) {
            validar(archivo, peliculas, nombre);
            List<Pelicula> respuestas = validarPeliculas(peliculas);

            Genero genero = respuesta.get();
            genero.setImagen(archivo.getBytes());
            genero.setNombre(nombre);
            genero.setPeliculas(peliculas);

            generoRepository.save(genero);

        } else {
            throw new Error("No se encontró el genero solicitado");
        }
    }

    @Transactional
    public void eliminar(String id) throws Error {

        Optional<Genero> respuesta = generoRepository.findById(id);
        if (respuesta.isPresent()) {
            Genero genero = respuesta.get();
            generoRepository.delete(genero);
        } else {
            throw new Error("No se encontró el genero solicitado");
        }

    }

    private void validar(MultipartFile archivo, List<Pelicula> peliculas, TituloGenero nombre) {

        if (nombre == null) {
            throw new Error("El genero debe tener un nombre");
        }
        if (peliculas == null || peliculas.isEmpty()) {
            throw new Error("El genero debe vincularse con peliculas");
        }
        if (archivo == null || archivo.isEmpty()) {
            throw new Error("Se debe adjuntar una imagen al genero");
        }

    }

    private List<Pelicula> validarPeliculas(List<Pelicula> peliculas) {

        List<Pelicula> respuestas = null;

        for (Pelicula pelicula : peliculas) {
            Optional<Pelicula> respuesta = peliculaRepository.findById(pelicula.getId());
            if (respuesta.isPresent()) {
                respuestas.add(pelicula);
            } else {
                throw new Error("No se encontró la pelicula solicitada.");
            }
        }

        return respuestas;

    }

}
