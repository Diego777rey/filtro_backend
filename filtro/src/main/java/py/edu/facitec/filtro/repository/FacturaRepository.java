package py.edu.facitec.filtro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import py.edu.facitec.filtro.entity.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
}
