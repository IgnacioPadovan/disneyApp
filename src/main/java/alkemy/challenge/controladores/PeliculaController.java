package alkemy.challenge.controladores;

import alkemy.challenge.entidades.Genero;
import alkemy.challenge.entidades.Pelicula;
import alkemy.challenge.entidades.Personaje;
import alkemy.challenge.servicios.GeneroService;
import alkemy.challenge.servicios.PeliculaService;
import alkemy.challenge.servicios.PersonajeService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/pelicula")
public class PeliculaController {

    @Autowired
    private PersonajeService personajeService;
    @Autowired
    private PeliculaService peliculaService;
    @Autowired
    private GeneroService generoService;

    @GetMapping("/movies")
    public String personajes(ModelMap modelo, @RequestParam(required = false) String idGenero,
            @RequestParam(required = false) String nombre, @RequestParam(required = false) String order) {
        try {
            
            List<Pelicula> peliculas = new ArrayList<>();

            if (nombre != null && !nombre.isEmpty()) {
                peliculas = peliculaService.buscarPorNombre(nombre);
            } else if (idGenero != null && !idGenero.isEmpty()) {
                peliculas = peliculaService.filtrarPorGenero(idGenero);
            } else if (order != null && !order.isEmpty()) {
                peliculas = peliculaService.ordenar(order);
            } else {
                peliculas = peliculaService.listarPeliculas();
            }

            List<Genero> generos = generoService.listarGeneros();
            modelo.put("generos", generos);
            modelo.put("peliculas", peliculas);
        } catch (Error e) {
            modelo.put("error", e.getMessage());
        }
        return "peliculas";

    }

    @GetMapping("/imagen/{id}")
    public ResponseEntity<byte[]> imagenPelicula(@PathVariable String id) {
        Pelicula pelicula;
        pelicula = peliculaService.buscarPorId(id);
        if (pelicula.getImagen() == null) {
            throw new Error("La pelicula no tiene una imagen asignada");
        }
        byte[] imagen = pelicula.getImagen();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/carga-edicion")
    public String carga(ModelMap modelo, @RequestParam(required = false) String id) {
        try {
            List<Personaje> personajes = personajeService.listarPersonajes();
            modelo.put("personajes", personajes);

            List<Genero> generos = generoService.listarGeneros();
            modelo.put("generos", generos);

            if (id == null) {
                Pelicula pelicula = new Pelicula();
                modelo.put("pelicula", pelicula);
                return "registroPelicula";

            } else {
                Pelicula pelicula = peliculaService.buscarPorId(id);
                modelo.put("pelicula", pelicula);
                return "registroPelicula";

            }
        } catch (Error e) {
            modelo.put("error", e.getMessage());
            return "inicio";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/carga-edicion")
    public String carga(ModelMap modelo, HttpSession session, @RequestParam MultipartFile archivo,
            @RequestParam(required = false) String id, @RequestParam String titulo, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDeCreacion, @RequestParam String calificacion,
            @RequestParam(required = false) List<String> idPersonajes) throws Error, IOException {
        try {
            if (id == null || id.isEmpty()) {
                peliculaService.agregar(archivo, idPersonajes, titulo, fechaDeCreacion, calificacion);
            } else {
                peliculaService.modificar(archivo, idPersonajes, titulo, fechaDeCreacion, calificacion, id);
            }
            return "redirect:/inicio";

        } catch (Error e) {
            List<Personaje> personajes = personajeService.listarPersonajes();
            modelo.put("personajes", personajes);
            Pelicula pelicula = new Pelicula();

            pelicula.setCalificacion(calificacion);
            pelicula.setFechaDeCreacion(fechaDeCreacion);
            pelicula.setId(id);
            pelicula.setImagen(archivo.getBytes());
            pelicula.setPersonajes(personajes);
            pelicula.setTitulo(titulo);

            modelo.put("error", e.getMessage());
            modelo.put("pelicula", pelicula);

            return "registroPelicula";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/eliminar")
    public String eliminar(ModelMap modelo, @RequestParam String id) {

        try {
            peliculaService.eliminar(id);
        } catch (Error e) {
            modelo.put("error", e.getMessage());
        }
        List<Pelicula> peliculas = peliculaService.listarPeliculas();
        modelo.put("peliculas", peliculas);
        return "peliculas";

    }

    @GetMapping("/detalle")
    public String detalle(ModelMap modelo, String id) {
        try {
            Pelicula pelicula = peliculaService.buscarPorId(id);

            modelo.put("pelicula", pelicula);
            return "detalle";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "peliculas";
        }

    
    }

}
