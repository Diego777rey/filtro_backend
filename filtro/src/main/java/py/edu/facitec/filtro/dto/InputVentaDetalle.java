package py.edu.facitec.filtro.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputVentaDetalle {
    private Long id;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;
    private BigDecimal subtotal;
    private Long ventaId;
    private Long productoId;
}
