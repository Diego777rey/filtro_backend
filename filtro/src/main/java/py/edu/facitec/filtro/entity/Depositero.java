package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Entity
@Table(name = "depositero")
@NoArgsConstructor
@AllArgsConstructor//esto se utliza para que no tengamos que construir los constructores ni los getters y setters
@Builder
public class Depositero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoDepositero;
    private Date fechaIngreso;
    private String turno;
    private String almacenAsignado;
    private String supervisor;
    private String horario;
    private String estado;

    @OneToOne
    @JoinColumn(name = "persona_id")
    private Persona persona;
}
