package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.edu.facitec.filtro.enums.EstadoVenta;
import py.edu.facitec.filtro.enums.TipoVenta;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "ventas")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoVenta;
    private Date fechaVenta;
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private TipoVenta tipoVenta;

    @Enumerated(EnumType.STRING)
    private EstadoVenta estadoVenta; // PENDIENTE, COBRADA

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Vendedor vendedor;

    @ManyToOne
    @JoinColumn(name = "cajero_id")
    private Cajero cajero; // Cajero que cobró la venta

    @ManyToOne
    @JoinColumn(name = "caja_id")
    private Caja caja; // Caja donde se registró el cobro
    @OneToOne(mappedBy = "venta")
    private Factura factura;
}
