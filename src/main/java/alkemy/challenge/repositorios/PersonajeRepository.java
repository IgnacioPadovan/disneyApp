package alkemy.challenge.repositorios;

import alkemy.challenge.entidades.Personaje;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonajeRepository extends JpaRepository<Personaje, String> {

    @Query("SELECT c FROM Personaje c WHERE c.nombre = :nombre")
    public List<Personaje> buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT c FROM Personaje c WHERE c.edad = :edad")
    public List<Personaje> filtrarPorEdad(@Param("edad") String edad);

    @Query("SELECT c FROM Personaje c WHERE c.peso = :peso")
    public List<Personaje> filtrarPorPeso(@Param("peso") String peso);

}
