package py.edu.facitec.filtro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import py.edu.facitec.filtro.entity.Cajero;
import py.edu.facitec.filtro.enums.TipoPersona;

import java.util.Optional;

public interface CajeroRepository extends JpaRepository<Cajero,Long> {

    @Query("""
        SELECT DISTINCT v
        FROM Cajero v
        JOIN v.persona.roles r
        WHERE r.tipoPersona = :rol
    """)
    Page<Cajero> findByPersonaRol(@Param("rol") TipoPersona rol, Pageable pageable);

    @Query("""
        SELECT DISTINCT v
        FROM Cajero v
        JOIN v.persona.roles r
        WHERE r.tipoPersona = :rol
          AND LOWER(v.persona.nombre) LIKE LOWER(CONCAT('%', :search, '%'))
    """)
    Page<Cajero> findByPersonaRolAndNombreContaining(@Param("rol") TipoPersona rol,
                                                     @Param("search") String search,
                                                     Pageable pageable);

    @EntityGraph(attributePaths = {"caja"})
    Page<Cajero> findAll(Pageable pageable);

    // CORRECCIÓN: buscar por nombre dentro de persona
    @EntityGraph(attributePaths = {"caja"})
    @Query("""
        SELECT c FROM Cajero c
        WHERE LOWER(c.persona.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
    """)
    Page<Cajero> findByNombreContainingIgnoreCase(@Param("nombre") String nombre, Pageable pageable);

    @EntityGraph(attributePaths = {"caja"})
    Optional<Cajero> findById(Long id);
}
