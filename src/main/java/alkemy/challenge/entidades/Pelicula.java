package alkemy.challenge.entidades;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pelicula {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String titulo;

    @Basic
    private LocalDate fechaDeCreacion;

    private String calificacion;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] imagen;

    @ManyToMany
    private List<Personaje> personajes;

}
