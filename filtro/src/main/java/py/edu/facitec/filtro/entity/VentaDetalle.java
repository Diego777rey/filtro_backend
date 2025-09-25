package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "ventas_detalle")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class VentaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;
    private BigDecimal subtotal;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
}