package alkemy.challenge.controladores;

import alkemy.challenge.entidades.Genero;
import alkemy.challenge.entidades.Pelicula;
import alkemy.challenge.enumeraciones.TituloGenero;
import alkemy.challenge.servicios.GeneroService;
import alkemy.challenge.servicios.PeliculaService;
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
@RequestMapping("/genero")
public class GeneroController {

    @Autowired
    private GeneroService generoService;
    @Autowired
    private PeliculaService peliculaService;

    @GetMapping("/")
    public String generos(ModelMap modelo) {
        try {
            List<Genero> generos = generoService.listarGeneros();
            modelo.put("generos", generos);
        } catch (Error e) {
            modelo.put("error", e.getMessage());
        }
        return "generos";

    }

    @GetMapping("/imagen/{id}")
    public ResponseEntity<byte[]> imagenGenero(@PathVariable String id) {
        Genero genero;
        genero = generoService.buscarPorId(id);
        if (genero.getImagen() == null) {
            throw new Error("El genero no tiene una foto asignada");
        }
        byte[] imagen = genero.getImagen();

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
                Genero genero = new Genero();
                modelo.put("genero", genero);

            } else {
                Genero genero = generoService.buscarPorId(id);
                modelo.put("genero", genero);

            }
            modelo.put("titulosGeneros", TituloGenero.values());
            return "registroGenero";
        } catch (Error e) {
            modelo.put("error", e.getMessage());
            return "inicio";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/carga-edicion")
    public String carga(ModelMap modelo, HttpSession session, @RequestParam MultipartFile archivo,
            @RequestParam(required = false) String id, @RequestParam TituloGenero nombre,
            @RequestParam(required = false) List<String> idPeliculas) throws Error, IOException {
        try {
            if (id == null || id.isEmpty()) {
                generoService.agregar(archivo, idPeliculas, nombre);
            } else {
                generoService.modificar(archivo, idPeliculas, nombre, id);
            }
            return "redirect:/inicio";

        } catch (Error e) {
            List<Pelicula> peliculas = peliculaService.listarPeliculas();
            modelo.put("peliculas", peliculas);
            Genero genero = new Genero();
            genero.setId(id);
            genero.setImagen(archivo.getBytes());
            genero.setNombre(nombre);

            modelo.put("titulosGeneros", TituloGenero.values());
            modelo.put("error", e.getMessage());
            modelo.put("genero", genero);

            return "registroPersonaje.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/eliminar")
    public String eliminar(ModelMap modelo, @RequestParam String id) {

        try {
            generoService.eliminar(id);
        } catch (Error e) {
            modelo.put("error", e.getMessage());
        }
        List<Genero> generos = generoService.listarGeneros();
        modelo.put("generos", generos);
        return "generos";

    }

    @GetMapping("/detalle")
    public String detalle(ModelMap modelo, String id) {
        try {
            Genero genero = generoService.buscarPorId(id);

            modelo.put("genero", genero);
            return "detalle";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "redirect:/genero/";
        }

    }
}
