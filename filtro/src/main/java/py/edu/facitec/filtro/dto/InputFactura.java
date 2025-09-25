package py.edu.facitec.filtro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.edu.facitec.filtro.enums.TipoFactura;

import java.math.BigDecimal;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputFactura {
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
    private Long ventaId;
    private Long clienteId;
}
