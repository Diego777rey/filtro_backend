package py.edu.facitec.filtro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.edu.facitec.filtro.enums.EstadoVenta;
import py.edu.facitec.filtro.enums.TipoVenta;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputVenta {
    private String codigoVenta;
    private Date fechaVenta;
    private BigDecimal total;
    private TipoVenta tipoVenta;
    private EstadoVenta estadoVenta; // MOSTRADOR, DELIVERY, WEB
    private Long clienteId;
    private Long vendedorId;
    private Long cajeroId;
    private Long facturaId;
}
