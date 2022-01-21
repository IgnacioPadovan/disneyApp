
package alkemy.challenge.controladores;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
    
    @GetMapping("/")
    public String index() {
        return "index.html";
    }
    
    @GetMapping("/inicio")
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    public String inicio() {
        return "inicio.html";
    }
    
    
}
