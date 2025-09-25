package py.edu.facitec.filtro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import py.edu.facitec.filtro.entity.Cliente;
import py.edu.facitec.filtro.enums.TipoPersona;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Query("SELECT DISTINCT c FROM Cliente c JOIN c.persona.roles r WHERE r.tipoPersona = :rol")
    Page<Cliente> findByPersonaRol(@Param("rol") TipoPersona rol, Pageable pageable);

    // Lo mismo, pero filtrando por nombre
    @Query("SELECT DISTINCT c FROM Cliente c JOIN c.persona.roles r " +
            "WHERE r.tipoPersona = :rol AND LOWER(c.persona.nombre) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Cliente> findByPersonaRolAndNombreContaining(@Param("rol") TipoPersona rol,
                                                      @Param("search") String search,
                                                      Pageable pageable);

}
