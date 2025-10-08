package py.edu.facitec.filtro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputTransferenciaDetalle {
    private Long transferenciaId;
    private Long productoId;
    private Integer cantidad;
    private Double precioUnitario;
}
