package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "transferencias")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con sucursal origen
    @ManyToOne(optional = false)
    @JoinColumn(name = "sucursal_origen_id")
    private Sucursal sucursalOrigen;

    // Relación con sucursal destino
    @ManyToOne(optional = false)
    @JoinColumn(name = "sucursal_destino_id")
    private Sucursal sucursalDestino;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, length = 20)
    private String estado;
    // Ej: "PENDIENTE", "ENVIADA", "RECIBIDA", "ANULADA"
    @Column(length = 500)
    private String observacion;
}