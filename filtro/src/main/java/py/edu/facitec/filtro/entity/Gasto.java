package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "gastos")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal monto;          // Cuánto se gastó
    private String concepto;           // Tipo de gasto
    private Date fecha;                // Fecha del gasto
    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;        // Sucursal donde se realizó el gasto

    @ManyToOne
    @JoinColumn(name = "cajero_id")
    private Cajero responsable;       // Cajero que realizó el gasto

    @ManyToOne
    @JoinColumn(name = "caja_id")
    private Caja caja;                // Caja de donde salió el dinero
}
