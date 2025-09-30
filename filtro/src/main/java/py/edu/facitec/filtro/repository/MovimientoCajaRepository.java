/*package py.edu.facitec.filtro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import py.edu.facitec.filtro.entity.MovimientoCaja;

import java.util.Optional;

public interface MovimientoCajaRepository extends JpaRepository<MovimientoCaja,Long> {

    // Cargar tanto categoría como proveedor para evitar N+1
    @EntityGraph(attributePaths = {"caja", "venta"})
    Page<MovimientoCaja> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"caja", "venta"})
    Page<MovimientoCaja> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    @EntityGraph(attributePaths = {"caja", "venta"})
    Optional<MovimientoCaja> findById(Long id);
}*/