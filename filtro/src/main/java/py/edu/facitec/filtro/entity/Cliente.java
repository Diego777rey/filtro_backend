package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Entity
@Table(name = "clientes")
@NoArgsConstructor
@AllArgsConstructor//esto se utliza para que no tengamos que construir los constructores ni los getters y setters
@Builder
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoCliente;
    private Date fechaRegistro;

   /* @OneToOne
    @JoinColumn(name = "persona_id")*/
    @ManyToOne
    private Persona persona;
}
