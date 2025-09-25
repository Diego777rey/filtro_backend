package py.edu.facitec.filtro.dto;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class InputCliente {
    private String codigoCliente;
    private String fechaRegistro;
    private Long personaId;
}
