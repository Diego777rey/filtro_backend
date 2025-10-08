package py.edu.facitec.filtro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputTransferencia {
    private Long id;
    private Long sucursalOrigenId;
    private Long sucursalDestinoId;
    private String estado;
    private String observacion;
    private String fecha;
}
