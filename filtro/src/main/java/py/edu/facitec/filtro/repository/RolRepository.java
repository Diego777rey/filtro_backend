package py.edu.facitec.filtro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import py.edu.facitec.filtro.entity.Rol;
import py.edu.facitec.filtro.enums.TipoPersona;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    // Con JpaRepository ya tienes findAllById, findById, save, delete, etc.
    Optional<Rol> findByTipoPersona(TipoPersona tipoPersona);
}
