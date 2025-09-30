package py.edu.facitec.filtro.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import py.edu.facitec.filtro.entity.Caja;
import py.edu.facitec.filtro.entity.Venta;

import java.math.BigDecimal;
import java.util.Date;

public class InputMovimientoCaja {
    private BigDecimal monto;
    private String tipo; // INGRESO, EGRESO
    private Date fecha;
    private String descripcion;
    private Long cajaId;
    private Long ventaId;
}
