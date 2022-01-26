package alkemy.challenge.servicios;

import alkemy.challenge.entidades.Genero;
import alkemy.challenge.entidades.Pelicula;
import alkemy.challenge.entidades.Personaje;
import alkemy.challenge.repositorios.PeliculaRepository;
import alkemy.challenge.repositorios.PersonajeRepository;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PeliculaService {

    @Autowired
    private PeliculaRepository peliculaRepository;
    @Autowired
    private PersonajeService personajeService;
    @Autowired
    private GeneroService generoService;

    @Transactional
    public void agregar(MultipartFile archivo, List<String> idPersonajes, String titulo, LocalDate fechaCreacion, String calificacion) throws Error, IOException {
        try {

            validar(archivo, titulo, calificacion, fechaCreacion);

            Pelicula pelicula = new Pelicula();
            pelicula.setImagen(archivo.getBytes());
            pelicula.setFechaDeCreacion(fechaCreacion);
            pelicula.setCalificacion(calificacion);
            pelicula.setTitulo(titulo);

            List<Personaje> personajes = new ArrayList<>();

            if (idPersonajes != null) {

                for (String idPersonaje : idPersonajes) {
                    Personaje personaje = personajeService.buscarPorId(idPersonaje);
                    personajes.add(personaje);
                }

            }

            pelicula.setPersonajes(personajes);

            peliculaRepository.save(pelicula);

        } catch (Error e) {
            throw new Error("No se pudo cargar la película a la base de datos");
        }

    }

    @Transactional
    public void modificar(MultipartFile archivo, List<String> idPersonajes, String titulo, LocalDate fechaCreacion, String calificacion, String id) throws Error, IOException {

        Optional<Pelicula> respuesta = peliculaRepository.findById(id);
        if (respuesta.isPresent()) {
            validar(archivo, titulo, calificacion, fechaCreacion);

            Pelicula pelicula = respuesta.get();
            pelicula.setImagen(archivo.getBytes());
            pelicula.setFechaDeCreacion(fechaCreacion);
            pelicula.setCalificacion(calificacion);
            pelicula.setTitulo(titulo);

            List<Personaje> personajes = new ArrayList<>();

            if (idPersonajes != null) {

                for (String idPersonaje : idPersonajes) {
                    Personaje personaje = personajeService.buscarPorId(idPersonaje);
                    personajes.add(personaje);
                }

            }

            pelicula.setPersonajes(personajes);

            peliculaRepository.save(pelicula);

        } else {
            throw new Error("No se encontró la película solicitada");
        }
    }

    @Transactional
    public void eliminar(String id) throws Error {

        Optional<Pelicula> respuesta = peliculaRepository.findById(id);
        if (respuesta.isPresent()) {
            Pelicula pelicula = respuesta.get();
            peliculaRepository.delete(pelicula);
        } else {
            throw new Error("No se encontró la película solicitada");
        }

    }

    private void validar(MultipartFile archivo, String titulo, String calificacion, LocalDate fechaCreacion) {

        if (titulo == null || titulo.isEmpty()) {
            throw new Error("La película debe tener un nombre");
        }
        if (calificacion == null || calificacion.isEmpty()) {
            throw new Error("La película debe tener una calificación");
        }
        if (fechaCreacion == null) {
            throw new Error("La película debe tener una fecha de creación");
        }
        if (archivo == null || archivo.isEmpty()) {
            throw new Error("Se debe adjuntar una imagen a la película");
        }

    }

    @Transactional(readOnly = true)
    public List<Pelicula> listarPeliculas() {

        List<Pelicula> respuesta = peliculaRepository.findAll();

        if (respuesta != null) {
            return respuesta;
        } else {
            throw new Error("No se encontraron peliculas para presentar");
        }

    }

    @Transactional(readOnly = true)
    public Pelicula buscarPorId(String id) {

        Optional<Pelicula> respuesta = peliculaRepository.findById(id);

        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new Error("No se encontro una pelicula con esas caracteristicas");
        }

    }

    @Transactional(readOnly = true)
    public List<Pelicula> buscarPorNombre(String nombre) {

        List<Pelicula> respuesta = peliculaRepository.buscarPorNombre(nombre);

        if (respuesta != null) {
            return respuesta;
        } else {
            throw new Error("No se encontró pelicula con ese nombre.");
        }

    }

    @Transactional(readOnly = true)
    public List<Pelicula> filtrarPorGenero(String idGenero) {

        Genero genero = generoService.buscarPorId(idGenero);

        return genero.getPeliculas();

    }

    public List<Pelicula> ordenar(String order) {

        if (order.equalsIgnoreCase("ASC")) {
            return peliculaRepository.ordenAscendente();
        } else if (order.equalsIgnoreCase("DESC")) {
            return peliculaRepository.ordenDescendente();
        } else {
            throw new Error("No se pueden ordenar las peliculas");
        }

    }

}
