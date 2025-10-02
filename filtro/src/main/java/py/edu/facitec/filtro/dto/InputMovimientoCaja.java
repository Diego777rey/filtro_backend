package py.edu.facitec.filtro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputMovimientoCaja {
    private BigDecimal monto;
    private String tipo; // INGRESO, EGRESO
    private String fecha;
    private String descripcion;
    private Long cajaId;
    private Long ventaId;
}
