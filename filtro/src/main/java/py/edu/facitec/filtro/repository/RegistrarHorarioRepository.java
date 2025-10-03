package py.edu.facitec.filtro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import py.edu.facitec.filtro.entity.RegistrarHorario;

import java.util.List;

public interface RegistrarHorarioRepository extends JpaRepository<RegistrarHorario, Long> {
    List<RegistrarHorario> findByPersonaId(Long personaId);
}