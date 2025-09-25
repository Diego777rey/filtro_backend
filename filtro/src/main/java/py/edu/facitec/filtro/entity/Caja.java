package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import py.edu.facitec.filtro.enums.EstadoCaja;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "cajas")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Caja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoCaja;
    private String descripcion;
    private String ubicacion;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCaja estadoCaja; // ACTIVA, INACTIVA, MANTENIMIENTO
    private Double saldoInicial;
    private BigDecimal saldoActual;// "PRINCIPAL", "SECUNDARIA"
}