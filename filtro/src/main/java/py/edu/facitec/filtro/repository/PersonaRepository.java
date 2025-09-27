package py.edu.facitec.filtro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.edu.facitec.filtro.entity.Persona;
import py.edu.facitec.filtro.enums.TipoPersona;

import java.util.List;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    // ------------------ BÚSQUEDAS SIMPLES ------------------

    // Buscar personas por nombre
    List<Persona> findByNombreContainingIgnoreCase(String nombre);

    // ------------------ BÚSQUEDAS POR ROL ------------------

    // Traer todas las personas que tengan un rol específico
    @Query("""
        SELECT DISTINCT p
        FROM Persona p
        JOIN p.roles r
        WHERE r.tipoPersona = :rol
    """)
    Page<Persona> findByRol(@Param("rol") TipoPersona rol, Pageable pageable);

    // Buscar personas por rol y nombre
    @Query("""
        SELECT DISTINCT p
        FROM Persona p
        JOIN p.roles r
        WHERE r.tipoPersona = :rol
        AND LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
    """)
    Page<Persona> findByRolAndNombreContaining(@Param("rol") TipoPersona rol,
                                               @Param("nombre") String nombre,
                                               Pageable pageable);

    // ------------------ BÚSQUEDAS POR VARIOS ROLES ------------------

    // Buscar personas que tengan cualquiera de los roles especificados
    @Query("""
        SELECT DISTINCT p
        FROM Persona p
        JOIN p.roles r
        WHERE r.tipoPersona IN :roles
    """)
    Page<Persona> findByRolesIn(@Param("roles") List<TipoPersona> roles, Pageable pageable);

    // Buscar personas que tengan cualquiera de los roles especificados y que contengan el nombre
    @Query("""
        SELECT DISTINCT p
        FROM Persona p
        JOIN p.roles r
        WHERE r.tipoPersona IN :roles
        AND LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
    """)
    Page<Persona> findByRolesInAndNombreContaining(@Param("roles") List<TipoPersona> roles,
                                                   @Param("nombre") String nombre,
                                                   Pageable pageable);

    // ------------------ BÚSQUEDA GLOBAL PARA PAGINACIÓN ------------------

    // Buscar personas por nombre, apellido o documento (para findAllPaginated)
    Page<Persona> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrDocumentoContainingIgnoreCase(
            String nombre,
            String apellido,
            String documento,
            Pageable pageable
    );
}
