package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import py.edu.facitec.filtro.enums.TipoFactura;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "facturas")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroFactura;
    private String timbrado;
    private String establecimiento;
    private String puntoExpedicion;
    private Date fechaEmision;
    private Date fechaVencimiento;
    private BigDecimal subTotal;
    private BigDecimal iva10;
    private BigDecimal iva5;
    private BigDecimal total;
    private String estado; // PENDIENTE, PAGADA, ANULADA, CONTABILIZADA
    private TipoFactura tipoFactura; // CONTADO, CREDITO
    private String condicionVenta; // EFECTIVO, TARJETA, TRANSFERENCIA

    // Relación con Venta (Una factura puede venir de una venta)
    @OneToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    // Relación directa con Cliente (puede facturar sin venta)
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}