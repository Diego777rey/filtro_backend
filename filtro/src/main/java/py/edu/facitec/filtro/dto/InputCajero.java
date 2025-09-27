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
public class InputCajero {
    private String codigoCajero;
    private String turno; // MAÑANA, TARDE, NOCHE
    private String fechaIngreso;
    private String estado; // ACTIVO, INACTIVO, VACACIONES
    private Long cajaId;
    private Long personaId;
}
