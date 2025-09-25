package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "facturas_detalle")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FacturaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;
    private String descripcion;
    private BigDecimal precioUnitario;
    private BigDecimal ivaPorcentaje; // 10, 5, 0
    private BigDecimal ivaMonto;
    private BigDecimal subtotal;

    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
}