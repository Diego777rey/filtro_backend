package py.edu.facitec.filtro.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import py.edu.facitec.filtro.enums.EstadoCaja;

import java.math.BigDecimal;

public class InputCaja {
    private String codigoCaja;
    private String descripcion;
    private String ubicacion;
    private EstadoCaja estadoCaja; // ACTIVA, INACTIVA, MANTENIMIENTO
    private Double saldoInicial;
    private BigDecimal saldoActual;
}
