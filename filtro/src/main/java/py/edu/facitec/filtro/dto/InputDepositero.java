package py.edu.facitec.filtro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputDepositero {
    private String codigoDepositero;
    private Date fechaIngreso;
    private String turno;
    private String almacenAsignado;
    private String supervisor;
    private String horario;
    private String estado;
    private Long personaId;
}
