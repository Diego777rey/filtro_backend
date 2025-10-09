package py.edu.facitec.filtro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import py.edu.facitec.filtro.entity.Sucursal;

import java.util.List;
import java.util.Optional;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    Optional<Sucursal> findByNombre(String nombre);
    Page<Sucursal> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    @Query("SELECT c FROM Sucursal c " +
            "WHERE (:nombre IS NULL OR c.nombre LIKE %:nombre%) ")//aca realizamos una consulta y filtramos por nombre
    List<Sucursal> findByFiltros(String nombre);
}
