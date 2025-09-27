package py.edu.facitec.filtro.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import py.edu.facitec.filtro.entity.Caja;

import java.util.List;
import java.util.Optional;

public interface CajaRepository extends JpaRepository<Caja,Long> {
    Optional<Caja> findByDescripcion(String descripcion);
    Page<Caja> findByDescripcionContainingIgnoreCase(String descripcion, Pageable pageable);
    @Query("SELECT c FROM Caja c " +
            "WHERE (:descripcion IS NULL OR c.descripcion LIKE %:descripcion%) ")//aca realizamos una consulta y filtramos por nombre
    List<Caja> findByFiltros(String descripcion);
}
