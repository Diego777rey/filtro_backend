package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Entity
@Table(name = "sucursales")
@NoArgsConstructor
@AllArgsConstructor//esto se utliza para que no tengamos que construir los constructores ni los getters y setters
@Builder
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 200)
    private String direccion;

    @Column(length = 50)
    private String telefono;

    // Relación opcional bidireccional con Transferencias como origen
    @OneToMany(mappedBy = "sucursalOrigen")
    private List<Transferencia> transferenciasOrigen;

    // Relación opcional bidireccional con Transferencias como destino
    @OneToMany(mappedBy = "sucursalDestino")
    private List<Transferencia> transferenciasDestino;
}
