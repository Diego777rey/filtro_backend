package py.edu.facitec.filtro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputDepositero {
    private String codigoDepositero;
    private String fechaIngreso;
    private String turno;
    private String supervisor;
    private String horario;
    private String estado;
    private Long sucursalId;
    private Long personaId;
}
