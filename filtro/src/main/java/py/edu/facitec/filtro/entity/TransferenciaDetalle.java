package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "transferencia_detalles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferenciaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "transferencia_id")
    private Transferencia transferencia;
    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id")
    private Producto producto;
    private Integer cantidad;
    private Double precioUnitario;

}