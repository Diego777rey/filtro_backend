package py.edu.facitec.filtro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import py.edu.facitec.filtro.entity.Vendedor;
import py.edu.facitec.filtro.enums.TipoPersona;


public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    @Query("SELECT DISTINCT v FROM Vendedor v JOIN v.persona.roles r WHERE r.tipoPersona = :rol")
    Page<Vendedor> findByPersonaRol(@Param("rol") TipoPersona rol, Pageable pageable);

    @Query("SELECT DISTINCT v FROM Vendedor v JOIN v.persona.roles r " +
            "WHERE r.tipoPersona = :rol AND LOWER(v.persona.nombre) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Vendedor> findByPersonaRolAndNombreContaining(@Param("rol") TipoPersona rol,
                                                       @Param("search") String search,
                                                       Pageable pageable);
}
