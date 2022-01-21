package alkemy.challenge.controladores;

import alkemy.challenge.entidades.Pelicula;
import alkemy.challenge.entidades.Personaje;
import alkemy.challenge.servicios.PeliculaService;
import alkemy.challenge.servicios.PersonajeService;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/personaje")
public class PersonajeController {

    @Autowired
    private PersonajeService personajeService;
    @Autowired
    private PeliculaService peliculaService;

    @GetMapping("/characters")
    public String personajes(ModelMap modelo) {
        try {
            List<Personaje> personajes = personajeService.listarPersonajes();
            modelo.put("personajes", personajes);
        } catch (Error e) {
            modelo.put("error", e.getMessage());
        }
        return "personajes.html";

    }

    @GetMapping("/imagen/{id}")
    public ResponseEntity<byte[]> imagenPersonaje(@PathVariable String id) {
        Personaje personaje;
        personaje = personajeService.buscarPorId(id);
        if (personaje.getImagen() == null) {
            throw new Error("El personaje no tiene una foto asignada");
        }
        byte[] imagen = personaje.getImagen();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/carga-edicion")
    public String carga(ModelMap modelo, @RequestParam(required = false) String id) {
        try {
            List<Pelicula> peliculas = peliculaService.listarPeliculas();
            modelo.put("peliculas", peliculas);
            if (id == null) {
                Personaje personaje = new Personaje();
                modelo.put("personaje", personaje);
                return "registroPersonaje.html";

            } else {
                Personaje personaje = personajeService.buscarPorId(id);
                modelo.put("personaje", personaje);
                return "registroPersonaje.html";

            }
        } catch (Error e) {
            modelo.put("error", e.getMessage());
            return "inicio.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/carga-edicion")
    public String carga(ModelMap modelo, HttpSession session, @RequestParam MultipartFile archivo,
            @RequestParam(required = false) String id, @RequestParam String nombre, @RequestParam String edad, @RequestParam String peso,
            @RequestParam String historia, @RequestParam(required = false) List<String> idPeliculas) throws Error, IOException {
        try {
            if (id == null || id.isEmpty()) {
                personajeService.agregar(archivo, idPeliculas, nombre, edad, peso, historia);
            } else {
                personajeService.modificar(archivo, idPeliculas, nombre, edad, peso, historia, id);
            }
            return "redirect:/inicio";

        } catch (Error e) {
            List<Pelicula> peliculas = peliculaService.listarPeliculas();
            modelo.put("peliculas", peliculas);
            Personaje personaje = new Personaje();
            personaje.setId(id);
            personaje.setImagen(archivo.getBytes());
            personaje.setEdad(edad);
            personaje.setHistoria(historia);
            personaje.setNombre(nombre);
            personaje.setPeliculas(peliculas);
            personaje.setPeso(peso);

            modelo.put("error", e.getMessage());
            modelo.put("personaje", personaje);

            return "registroPersonaje.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/eliminar")
    public String eliminar(ModelMap modelo, HttpSession session, @RequestParam String id) {

        try {
            personajeService.eliminar(id);
        } catch (Error e) {
            modelo.put("error", e.getMessage());
        }
        return "personajes.html";

    }

    @GetMapping("/detalle")
    public String detalle(ModelMap modelo, String id) {

        Personaje personaje = personajeService.buscarPorId(id);

        modelo.put("personaje", personaje);

        return "detalle.html";

    }

    @GetMapping("/characters/{nombre}")
    public String buscarPorNombre(ModelMap modelo, String nombre) {

        try {
            List<Personaje> personajes = personajeService.buscarPorNombre(nombre);
            modelo.put("personajes", personajes);
        } catch (Error e) {
            List<Personaje> personajes = personajeService.listarPersonajes();
            modelo.put("personajes", personajes);
            modelo.put("error", e.getMessage());
        }

        return "personajes.html";
    }

    @GetMapping("/characters/{edad}")
    public String filtrarPorEdad(ModelMap modelo, String edad) {

        try {
            List<Personaje> personajes = personajeService.filtrarPorEdad(edad);
            modelo.put("personajes", personajes);
        } catch (Error e) {
            List<Personaje> personajes = personajeService.listarPersonajes();
            modelo.put("personajes", personajes);
            modelo.put("error", e.getMessage());
        }

        return "personajes.html";
    }

    @GetMapping("/characters/{peso}")
    public String filtrarPorPeso(ModelMap modelo, String peso) {

        try {
            List<Personaje> personajes = personajeService.filtrarPorPeso(peso);
            modelo.put("personajes", personajes);
        } catch (Error e) {
            List<Personaje> personajes = personajeService.listarPersonajes();
            modelo.put("personajes", personajes);
            modelo.put("error", e.getMessage());
        }

        return "personajes.html";

    }
//    
//    @GetMapping("/characters/{idPelicula}")
//    public String filtrarPorPelicula(ModelMap modelo, String idPelicula){
//        
//         try {
//            List<Personaje> personajes = peliculaService.filtrarPorPelicula(idPelicula);
//            modelo.put("personajes", personajes);
//        } catch (Error e) {
//            List<Personaje> personajes = personajeService.listarPersonajes();
//            modelo.put("personajes", personajes);
//            modelo.put("error", e.getMessage());
//        }
//
//        return "personajes.html";
//        
//    }
}
