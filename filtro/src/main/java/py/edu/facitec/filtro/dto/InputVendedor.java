package py.edu.facitec.filtro.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InputVendedor {
    private String codigoVendedor;
    private Double comision;
    private String sucursal;
    private Long personaId;
}
