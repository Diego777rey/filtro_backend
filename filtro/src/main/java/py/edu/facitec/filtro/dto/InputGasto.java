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
public class InputGasto {

    private BigDecimal monto;
    private String concepto;
    private Date fecha;
    private String detalle;
    private Long sucursalId;
    private Long cajeroId;
    private Long cajaId;
}
