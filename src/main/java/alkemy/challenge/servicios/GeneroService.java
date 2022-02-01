package alkemy.challenge.servicios;

import alkemy.challenge.entidades.Genero;
import alkemy.challenge.entidades.Pelicula;
import alkemy.challenge.enumeraciones.TituloGenero;
import alkemy.challenge.repositorios.GeneroRepository;
import alkemy.challenge.repositorios.PeliculaRepository;
import java.io.IOException;
import java.util.ArrayList;
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
    public void agregar(MultipartFile archivo, List<String> idPeliculas, TituloGenero nombre) throws Error, IOException {
        try {
            validar(archivo, nombre);

            Genero genero = new Genero();
            genero.setImagen(archivo.getBytes());
            genero.setNombre(nombre);

            List<Pelicula> peliculas = new ArrayList<>();

            if (idPeliculas != null) {

                for (String idPelicula : idPeliculas) {
                    Pelicula pelicula = peliculaRepository.getById(idPelicula);
                    peliculas.add(pelicula);
                }

            }

            genero.setPeliculas(peliculas);

            generoRepository.save(genero);
        } catch (Error e) {
            throw new Error("No se pudo cargar el genero ingresado");
        }

    }

    @Transactional
    public void modificar(MultipartFile archivo, List<String> idPeliculas, TituloGenero nombre, String id) throws Error, IOException {

        Optional<Genero> respuesta = generoRepository.findById(id);
        if (respuesta.isPresent()) {
            validar(archivo, nombre);

            Genero genero = respuesta.get();
            genero.setImagen(archivo.getBytes());
            genero.setNombre(nombre);

            List<Pelicula> peliculas = new ArrayList<>();

            if (idPeliculas != null) {

                for (String idPelicula : idPeliculas) {
                    Pelicula pelicula = peliculaRepository.getById(idPelicula);
                    peliculas.add(pelicula);
                }

            }

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

    private void validar(MultipartFile archivo, TituloGenero nombre) {

        if (nombre == null) {
            throw new Error("El genero debe tener un nombre");
        }
        if (archivo == null || archivo.isEmpty()) {
            throw new Error("Se debe adjuntar una imagen al genero");
        }

    }

    public List<Genero> listarGeneros() {

        return generoRepository.findAll();

    }

    public Genero buscarPorId(String id) {

        Optional<Genero> respuesta = generoRepository.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new Error("No se encontró el genero solicitado.");
        }

    }

}
