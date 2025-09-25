package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.edu.facitec.filtro.enums.TipoMovimiento;

import java.util.Date;

@Data
@Entity
@Table(name = "inventario")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date fechaMovimiento;
    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipoMovimiento; // ENTRADA, SALIDA, AJUSTE
    private Integer cantidad;
    private Integer stockAnterior;
    private Integer stockNuevo;
    private String motivo; // COMPRA, VENTA, DEVOLUCION, AJUSTE

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "depositero_id")
    private Depositero depositero;

    // Relación opcional con venta si el movimiento es por venta
    @ManyToOne
    @JoinColumn(name = "venta_detalle_id")
    private VentaDetalle ventaDetalle;
}