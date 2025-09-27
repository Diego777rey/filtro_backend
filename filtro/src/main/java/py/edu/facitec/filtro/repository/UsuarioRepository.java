package py.edu.facitec.filtro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import py.edu.facitec.filtro.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByPersonaNombre(String nombre);

    Optional<Usuario> findByPersonaEmail(String email);

    Optional<Usuario> findByPersonaId(Long personaId);  // <-- nuevo método

    @Query("SELECT u FROM Usuario u WHERE u.persona.nombre LIKE %:nombre%")
    Page<Usuario> findByPersonaNombreContainingIgnoreCase(@Param("nombre") String nombre, Pageable pageable);
}
