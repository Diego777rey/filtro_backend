package py.edu.facitec.filtro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import py.edu.facitec.filtro.entity.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    Page<Venta> findByCodigoVentaContainingIgnoreCase(String codigoVenta, Pageable pageable);
}
