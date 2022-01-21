package alkemy.challenge.entidades;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data 
@AllArgsConstructor 
@NoArgsConstructor
public class Personaje {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String nombre;
    private String edad;
    private String peso;
    private String historia;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] imagen;

    @ManyToMany @Column(nullable = true)
    private List<Pelicula> peliculas;

}
