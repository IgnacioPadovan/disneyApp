package alkemy.challenge.servicios;

import alkemy.challenge.entidades.Pelicula;
import alkemy.challenge.entidades.Personaje;
import alkemy.challenge.repositorios.PeliculaRepository;
import alkemy.challenge.repositorios.PersonajeRepository;
import java.io.IOException;
import java.util.Date;
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
    private PersonajeRepository personajeRepository;

    @Transactional
    public void agregar(MultipartFile archivo, List<Personaje> personajes, String titulo, Date fechaCreacion, String calificacion) throws Error, IOException {

        validar(archivo, personajes, titulo, calificacion, fechaCreacion);
        List<Personaje> respuestas = validarPersonajes(personajes);

        Pelicula pelicula = new Pelicula();
        pelicula.setImagen(archivo.getBytes());
        pelicula.setFechaDeCreacion(fechaCreacion);
        pelicula.setCalificacion(calificacion);
        pelicula.setPersonajes(personajes);
        pelicula.setTitulo(titulo);

        peliculaRepository.save(pelicula);

    }

    @Transactional
    public void modificar(MultipartFile archivo, List<Personaje> personajes, String titulo, Date fechaCreacion, String calificacion, String id) throws Error, IOException {

        Optional<Pelicula> respuesta = peliculaRepository.findById(id);
        if (respuesta.isPresent()) {
            validar(archivo, personajes, titulo, calificacion, fechaCreacion);
            List<Personaje> respuestas = validarPersonajes(personajes);

            Pelicula pelicula = respuesta.get();
            pelicula.setImagen(archivo.getBytes());
            pelicula.setFechaDeCreacion(fechaCreacion);
            pelicula.setCalificacion(calificacion);
            pelicula.setPersonajes(personajes);
            pelicula.setTitulo(titulo);

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

    private void validar(MultipartFile archivo, List<Personaje> personajes, String titulo, String calificacion, Date fechaCreacion) {

        if (titulo == null || titulo.isEmpty()) {
            throw new Error("La película debe tener un nombre");
        }
        if (calificacion == null || calificacion.isEmpty()) {
            throw new Error("La película debe tener una calificación");
        }
        if (fechaCreacion == null) {
            throw new Error("La película debe tener una fecha de creación");
        }
        if (personajes == null || personajes.isEmpty()) {
            throw new Error("La película debe vincularse con personajes");
        }
        if (archivo == null || archivo.isEmpty()) {
            throw new Error("Se debe adjuntar una imagen a la película");
        }

    }

    private List<Personaje> validarPersonajes(List<Personaje> personajes) {

        List<Personaje> respuestas = null;

        for (Personaje personaje : personajes) {
            Optional<Personaje> respuesta = personajeRepository.findById(personaje.getId());
            if (respuesta.isPresent()) {
                respuestas.add(personaje);
            } else {
                throw new Error("No se encontró el personaje solicitado.");
            }
        }

        return respuestas;

    }

//    public List<Personaje> filtrarPorPelicula(String idPelicula) {
//
//        List<String> respuesta = peliculaRepository.personajesPorPelicula(idPelicula);
//        List<Personaje> personajes = null;
//
//        for (String id : respuesta) {
//            Optional<Personaje> op = personajeRepository.findById(id);
//            if (op.isPresent()) {
//                personajes.add(op.get());
//                return personajes;
//            } else {
//                throw new Error("No se encontraron personajes para esa pelicula.");
//            }
//        }
//        return null;
//    }

    public List<Pelicula> listarPeliculas() {

        List<Pelicula> respuesta = peliculaRepository.findAll();

        if (respuesta != null) {
            return respuesta;
        } else {
            throw new Error("No se encontraron peliculas para presentar");
        }

    }

}
