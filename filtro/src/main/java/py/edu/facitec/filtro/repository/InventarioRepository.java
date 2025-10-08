package py.edu.facitec.filtro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import py.edu.facitec.filtro.entity.Inventario;

import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    @EntityGraph(attributePaths = {"producto", "depositero","ventaDetalle"})
    Page<Inventario> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"producto", "depositero","ventaDetalle"})
    Page<Inventario> findByMotivoContainingIgnoreCase(String motivo, Pageable pageable);

    @EntityGraph(attributePaths = {"producto", "depositero","ventaDetalle"})
    Optional<Inventario> findById(Long id);
}
