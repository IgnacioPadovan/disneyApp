package alkemy.challenge.servicios;

import alkemy.challenge.entidades.Pelicula;
import alkemy.challenge.entidades.Personaje;
import alkemy.challenge.repositorios.PeliculaRepository;
import alkemy.challenge.repositorios.PersonajeRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PersonajeService {

    @Autowired
    private PeliculaRepository peliculaRepository;
    @Autowired
    private PersonajeRepository personajeRepository;

    @Transactional
    public void agregar(MultipartFile archivo, List<String> idPeliculas, String nombre, String edad, String peso, String historia) throws Error, IOException {

        validar(archivo, nombre, edad, peso, historia);

        Personaje personaje = new Personaje();
        personaje.setImagen(archivo.getBytes());
        personaje.setEdad(edad);
        personaje.setHistoria(historia);
        personaje.setNombre(nombre);
        personaje.setPeso(peso);

        if (idPeliculas != null) {
            List<Pelicula> peliculas = new ArrayList<>();
            for (String idPelicula : idPeliculas) {
                Pelicula pelicula = peliculaRepository.getById(idPelicula);
                peliculas.add(pelicula);
            }

            personaje.setPeliculas(peliculas);
        }

        personajeRepository.save(personaje);

    }

    @Transactional
    public void modificar(MultipartFile archivo, List<String> idPeliculas, String nombre, String edad, String peso, String historia, String id) throws Error, IOException {

        Optional<Personaje> respuesta = personajeRepository.findById(id);
        if (respuesta.isPresent()) {

            validar(archivo, nombre, edad, peso, historia);

            Personaje personaje = new Personaje();
            personaje.setImagen(archivo.getBytes());
            personaje.setEdad(edad);
            personaje.setHistoria(historia);
            personaje.setNombre(nombre);
            personaje.setPeso(peso);

            if (idPeliculas != null) {
                List<Pelicula> peliculas = new ArrayList<>();
                for (String idPelicula : idPeliculas) {
                    Pelicula pelicula = peliculaRepository.getById(idPelicula);
                    peliculas.add(pelicula);
                }

                personaje.setPeliculas(peliculas);
            }

            personajeRepository.save(personaje);

        } else {
            throw new Error("No se encontró el personaje solicitado");
        }
    }

    @Transactional
    public void eliminar(String id) throws Error {

        Optional<Personaje> respuesta = personajeRepository.findById(id);
        if (respuesta.isPresent()) {
            Personaje personaje = respuesta.get();
            personajeRepository.delete(personaje);
        } else {
            throw new Error("No se encontró el personaje solicitado");
        }

    }

    private void validar(MultipartFile archivo, String nombre, String edad, String peso, String historia) {

        if (nombre == null || nombre.isEmpty()) {
            throw new Error("El personaje debe tener un nombre");
        }
        if (edad == null || edad.isEmpty()) {
            throw new Error("El personaje debe tener una edad");
        }
        if (peso == null || peso.isEmpty()) {
            throw new Error("El personaje debe tener un peso");
        }
        if (historia == null || historia.isEmpty()) {
            throw new Error("El personaje debe tener una historia");
        }
        if (archivo == null || archivo.isEmpty()) {
            throw new Error("Se debe adjuntar una imagen al personaje");
        }

    }

    private List<Pelicula> validarPeliculas(List<Pelicula> peliculas) {

        List<Pelicula> respuestas = null;

        for (Pelicula pelicula : peliculas) {
            Optional<Pelicula> respuesta = peliculaRepository.findById(pelicula.getId());
            if (respuesta.isPresent()) {
                respuestas.add(pelicula);
            } else {
                throw new Error("No se encontró la pelicula solicitada");
            }
        }

        return respuestas;

    }

    public List<Personaje> listarPersonajes() {

        return personajeRepository.findAll();

    }

    public Personaje buscarPorId(String id) {

        Optional<Personaje> respuesta = personajeRepository.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new Error("No se encontró el personaje solicitado.");
        }

    }

    public List<Personaje> buscarPorNombre(String nombre) {

        List<Personaje> respuesta = personajeRepository.buscarPorNombre(nombre);

        if (respuesta != null) {
            return respuesta;
        } else {
            throw new Error("No se encontró personaje con ese nombre.");
        }

    }

    public List<Personaje> filtrarPorEdad(String edad) {

        List<Personaje> respuesta = personajeRepository.filtrarPorEdad(edad);

        if (respuesta != null) {
            return respuesta;
        } else {
            throw new Error("No se encontró personaje con esa edad.");
        }

    }

    public List<Personaje> filtrarPorPeso(String peso) {

        List<Personaje> respuesta = personajeRepository.filtrarPorPeso(peso);

        if (respuesta != null) {
            return respuesta;
        } else {
            throw new Error("No se encontró personaje con ese peso.");
        }

    }

}
