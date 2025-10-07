/*package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "transferencia_detalles")
public class TransferenciaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "transferencia_id")
    private Transferencia transferencia;

    @Column(nullable = false)
    private String producto;

    private Integer cantidad;

    private Double precioUnitario;
}
*/