package py.edu.facitec.filtro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import py.edu.facitec.filtro.entity.Producto;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto,Long> {

    // Cargar tanto categoría como proveedor para evitar N+1
    @EntityGraph(attributePaths = {"categoria", "proveedor"})
    Page<Producto> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"categoria", "proveedor"})
    Page<Producto> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    @EntityGraph(attributePaths = {"categoria", "proveedor"})
    Optional<Producto> findById(Long id);
}
