
package alkemy.challenge.repositorios;

import alkemy.challenge.entidades.Pelicula;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, String>{
    
//    @Query("SELECT c.personaje.id FROM Pelicula c WHERE c.id = :idPelicula")
//    public List<String> personajesPorPelicula(@Param("idPelicula") String idPelicula);
    
  //      @Query("SELECT c FROM Usuario c WHERE c.mail = :mail")
    //public Usuario buscarPorMail(@Param("mail") String mail);
    
}
