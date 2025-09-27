package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "proveedor")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos del proveedor (puede ser empresa o persona)
    private String ruc;               // RUC o cédula
    private String razonSocial;       // Nombre de empresa o nombre completo
    private String rubro;             // Actividad principal
    private String telefono;
    private String email;

    // Contacto principal (opcional)
    @ManyToOne
    @JoinColumn(name = "contacto_id")
    private Persona persona;          // Persona de contacto dentro de la empresa

    // Observaciones o notas adicionales
    private String observaciones;
}
