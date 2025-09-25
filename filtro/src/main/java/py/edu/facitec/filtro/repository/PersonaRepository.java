package py.edu.facitec.filtro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import py.edu.facitec.filtro.entity.Persona;

import java.util.List;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    // Buscar personas por nombre (opcional)
    List<Persona> findByNombreContainingIgnoreCase(String nombre);
}
