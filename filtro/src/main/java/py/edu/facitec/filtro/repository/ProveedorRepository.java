package py.edu.facitec.filtro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import py.edu.facitec.filtro.entity.Proveedor;
import py.edu.facitec.filtro.enums.TipoPersona;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    // Traer todos los vendedores que tengan rol VENDEDOR (incluye personas con rol CLIENTE también)
    @Query("""
        SELECT DISTINCT v
        FROM Proveedor v
        JOIN v.persona.roles r
        WHERE r.tipoPersona = :rol
    """)
    Page<Proveedor> findByPersonaRol(@Param("rol") TipoPersona rol, Pageable pageable);

    // Traer todos los vendedores por rol y filtrando por nombre
    @Query("""
        SELECT DISTINCT v
        FROM Proveedor v
        JOIN v.persona.roles r
        WHERE r.tipoPersona = :rol
          AND LOWER(v.persona.nombre) LIKE LOWER(CONCAT('%', :search, '%'))
    """)
    Page<Proveedor> findByPersonaRolAndNombreContaining(@Param("rol") TipoPersona rol,
                                                       @Param("search") String search,
                                                       Pageable pageable);
}
