package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Entity
@Table(name = "cajeros")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cajero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoCajero;
    private String turno; // MAÑANA, TARDE, NOCHE
    private Date fechaIngreso;
    private String estado; // ACTIVO, INACTIVO, VACACIONES

    // Relación con Caja (Muchos Cajeros pueden usar una Caja)
    @ManyToOne
    @JoinColumn(name = "caja_id")
    private Caja caja;

    // Relación con Persona (datos personales)
    @OneToOne
    @JoinColumn(name = "persona_id")
    private Persona persona;
}