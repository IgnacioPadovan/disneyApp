package alkemy.challenge.controladores;

import alkemy.challenge.servicios.UsuarioService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class Authentication {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String registro() {
        return "registro.html";

    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model, @RequestParam(required = false) String logaut) {
        if (error != null) {
            model.put("error", "Nombre de usuario o clave incorrectos");
        }
        if (logaut != null) {
            model.put("logaut", "Ha salido correctamente de la plataforma");
        }
        return "login.html";

    }
    @GetMapping("/form")
    public String formulario() {
        
        return "registro.html";

    }

    @PostMapping("/register")
    public String registrar(ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String email, @RequestParam String clave1, @RequestParam String clave2) {

        try {
            usuarioService.registrar(nombre, apellido, email, clave1, clave2);
        } catch (Error e) {
            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("email", email);
            modelo.put("clave1", clave1);
            modelo.put("clave2", clave2);
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
            return "registro.html";
        }

        modelo.put("titulo", "Bienvenidos a la App de Disney");
        modelo.put("descripcion", "Tu usuario fue registrado con exito");
        return "exito.html";
    }
}
