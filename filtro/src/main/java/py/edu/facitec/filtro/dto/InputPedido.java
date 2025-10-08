package py.edu.facitec.filtro.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.edu.facitec.filtro.entity.PedidoDetalle;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputPedido {
    private String fecha;
    //aca siempre ponemos solo el id para evitar complicaciones innecesarias
    private Long proveedorId;
    private List<PedidoDetalle> detalles;

    private Boolean finalizado = false;
}
