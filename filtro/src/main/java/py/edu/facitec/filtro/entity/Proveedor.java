package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@Table(name = "proveedor")
@NoArgsConstructor
@AllArgsConstructor//esto se utliza para que no tengamos que construir los constructores ni los getters y setters
@Builder
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruc;
    private String rubro;
    private String contacto;

    @OneToOne
    @JoinColumn(name = "persona_id")
    private Persona persona;
}
