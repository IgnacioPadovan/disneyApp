package alkemy.challenge.entidades;

import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDeCreacion;

    private String calificacion;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] imagen;

    @ManyToMany
    private List<Personaje> personajes;

}
