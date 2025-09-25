package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import py.edu.facitec.filtro.enums.ProductoEstado;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "productos")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoProducto;
    @Column(nullable = false)
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductoEstado productoEstado; // ACTIVO, INACTIVO

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
}