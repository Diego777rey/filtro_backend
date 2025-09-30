package py.edu.facitec.filtro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.edu.facitec.filtro.enums.TipoVenta;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputVenta {
    private Long clienteId;
    private Long vendedorId;
    private Long cajeroId;
    private Long cajaId;
    private TipoVenta tipoVenta;
    private List<InputVentaDetalle> detalles;
}
