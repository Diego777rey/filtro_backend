package py.edu.facitec.filtro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.edu.facitec.filtro.enums.ProductoEstado;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputProducto {
    private String codigoProducto;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private ProductoEstado productoEstado; // ACTIVO, INACTIVO
    private Long categoriaId;
    private Long proveedorId;
}
