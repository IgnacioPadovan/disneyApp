package alkemy.challenge.servicios;

import alkemy.challenge.entidades.Usuario;
import alkemy.challenge.repositorios.UsuarioRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public void registrar(String nombre, String apellido, String mail, String clave, String clave2) throws Error {

        validar(nombre, apellido, mail, clave, clave2);

        Usuario usuario = new Usuario();

        usuario.setAlta(new Date());
        usuario.setApellido(apellido);
        usuario.setBaja(null);
        String encriptada = new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(encriptada);
        usuario.setMail(mail);
        usuario.setNombre(nombre);

        usuarioRepository.save(usuario);

    }

    @Transactional
    public void modificar(String nombre, String apellido, String mail, String clave, String clave2, String id) throws Error {

        validar(nombre, apellido, mail, clave, clave2);

        Optional<Usuario> respuesta = usuarioRepository.findById(id);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            usuario.setApellido(apellido);
            usuario.setNombre(nombre);
            usuario.setMail(mail);
            String encriptada = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encriptada);

            usuarioRepository.save(usuario);

        } else {
            throw new Error("No se encontró el usuario solicitado.");
        }
    }

    @Transactional
    public void deshabilitar(String id) throws Error {

        Optional<Usuario> respuesta = usuarioRepository.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setBaja(new Date());
            usuarioRepository.save(usuario);
        } else {
            throw new Error("No se encontró el usuario solicitado.");
        }

    }

    private void validar(String nombre, String apellido, String mail, String clave, String clave2) throws Error {

        if (nombre == null || nombre.isEmpty()) {
            throw new Error("El nombre del usuario no puede ser nulo");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new Error("El apellido del usuario no puede ser nulo");
        }
        if (mail == null || mail.isEmpty()) {
            throw new Error("El mail del usuario no puede ser nulo");
        }
        if (clave == null || clave.isEmpty() || clave.length() <= 6) {
            throw new Error("La clave del usuario no puede ser nula o  menor a seis digitos");
        }
        if (!clave.equals(clave2)) {
            throw new Error("Las claves deben ser iguales");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.buscarPorMail(mail);

        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
            permisos.add(p1);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            User user = new User(usuario.getMail(), usuario.getClave(), permisos);
            return user;
        } else {
            return null;
        }
    }

}
