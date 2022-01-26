
package alkemy.challenge.repositorios;

import alkemy.challenge.entidades.Pelicula;
import alkemy.challenge.entidades.Personaje;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, String>{
    
    @Query("SELECT p FROM Pelicula p WHERE p.titulo LIKE :nombre")
    public List<Pelicula> buscarPorNombre(@Param("nombre") String nombre);
    
    @Query("SELECT p.personajes FROM Pelicula p WHERE p.id = :idMovie")
    public List<Personaje> filtrarPorPelicula(@Param("idMovie") String idMovie);

    @Query("SELECT p FROM Pelicula p ORDER BY p.titulo ASC")
    public List<Pelicula> ordenAscendente();

    @Query("SELECT p FROM Pelicula p ORDER BY p.titulo DESC")
    public List<Pelicula> ordenDescendente();
    
}
