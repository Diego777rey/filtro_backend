package py.edu.facitec.filtro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.edu.facitec.filtro.entity.Cliente;
import py.edu.facitec.filtro.entity.Depositero;
import py.edu.facitec.filtro.enums.TipoPersona;

@Repository
public interface DepositeroRepository extends JpaRepository<Depositero, Long> {
    @Query("""
        SELECT c
        FROM Cliente c
        JOIN c.persona p
        JOIN p.roles r
        WHERE r.tipoPersona = :rol
    """)
    Page<Cliente> findByPersonaRol(@Param("rol") TipoPersona rol, Pageable pageable);
    @Query("""
        SELECT c
        FROM Depositero c
        JOIN c.persona p
        JOIN p.roles r
        WHERE r.tipoPersona = :rol
        AND LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
    """)
    Page<Depositero> findByPersonaRolAndNombreContaining(@Param("rol") TipoPersona rol,
                                                      @Param("nombre") String nombre,
                                                      Pageable pageable);
    @Query("""
        SELECT DISTINCT c
        FROM Depositero c
        JOIN c.persona p
        JOIN p.roles r
        WHERE r.tipoPersona IN :roles
    """)
    Page<Depositero> findByPersonaRolesIn(@Param("roles") java.util.List<TipoPersona> roles,
                                          Pageable pageable);

    @Query("""
        SELECT DISTINCT c
        FROM Depositero c
        JOIN c.persona p
        JOIN p.roles r
        WHERE r.tipoPersona IN :roles
        AND LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
    """)
    Page<Depositero> findByPersonaRolesInAndNombreContaining(@Param("roles") java.util.List<TipoPersona> roles,
                                                          @Param("nombre") String nombre,
                                                          Pageable pageable);
}
