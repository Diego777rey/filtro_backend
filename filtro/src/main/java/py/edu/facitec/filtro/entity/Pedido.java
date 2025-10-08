package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "pedidos")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoDetalle> detalles = new ArrayList<>();

    private Boolean finalizado = false;
}
