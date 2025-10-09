package py.edu.facitec.filtro.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.edu.facitec.filtro.enums.EstadoInventario;
import py.edu.facitec.filtro.enums.TipoMovimiento;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputInventario {
    private String fechaMovimiento;
    private TipoMovimiento tipoMovimiento; // ENTRADA, SALIDA, AJUSTE
    private Integer cantidad;
    private Integer stockAnterior;
    private Integer stockNuevo;
    private String motivo;
    private EstadoInventario estadoInventario;// COMPRA, VENTA, DEVOLUCION, AJUSTE
    private Long productoId;
    private Long depositeroId;
    private Long ventaDetalleId;
}
