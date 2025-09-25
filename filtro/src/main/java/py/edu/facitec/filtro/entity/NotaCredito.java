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
@Table(name = "notas_credito")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaCredito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroNotaCredito;
    private Date fechaEmision;
    private String motivo; // DEVOLUCION, ERROR_FACTURA, DESCUENTO
    private BigDecimal monto;
    private String estado;

    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;
}