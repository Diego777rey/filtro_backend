package py.edu.facitec.filtro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import py.edu.facitec.filtro.entity.Caja;
import py.edu.facitec.filtro.entity.Venta;
import py.edu.facitec.filtro.enums.EstadoVenta;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByCajaAndEstadoVenta(Caja caja, EstadoVenta estadoVenta);
    Page<Venta> findByCodigoVentaContainingIgnoreCase(String codigoVenta, Pageable pageable);
}
