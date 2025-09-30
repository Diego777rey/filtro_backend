package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "movimientos_caja")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoCaja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal monto;
    private String tipo; // INGRESO, EGRESO
    private Date fecha;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "caja_id")
    private Caja caja;

    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = true)
    private Venta venta; // opcional: relacionado con la venta
}
