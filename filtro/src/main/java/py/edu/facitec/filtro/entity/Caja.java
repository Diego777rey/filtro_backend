package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.edu.facitec.filtro.enums.EstadoCaja;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "cajas")
//@Table(name = "empleado", schema = "funcionario") esta anotacion se usa para mandar a un esquema especifico
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Caja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoCaja;
    private String descripcion;
    private String ubicacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCaja estadoCaja; // ACTIVA, CERRADA, MANTENIMIENTO

    private Double saldoInicial;
    private BigDecimal saldoActual;

    // Movimientos de caja (ingresos y egresos)
    @OneToMany(mappedBy = "caja", cascade = CascadeType.ALL)
    private List<MovimientoCaja> movimientos;
}
