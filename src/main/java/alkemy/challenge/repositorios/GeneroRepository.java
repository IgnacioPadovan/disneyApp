
package alkemy.challenge.repositorios;

import alkemy.challenge.entidades.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, String>{
    
  //      @Query("SELECT c FROM Usuario c WHERE c.mail = :mail")
    //public Usuario buscarPorMail(@Param("mail") String mail);
    
}
