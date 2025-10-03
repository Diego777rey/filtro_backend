package py.edu.facitec.filtro.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.edu.facitec.filtro.entity.RegistrarHorario;
import py.edu.facitec.filtro.entity.Persona;
import py.edu.facitec.filtro.enums.Horarios;
import py.edu.facitec.filtro.enums.Turno;
import py.edu.facitec.filtro.repository.RegistrarHorarioRepository;
import py.edu.facitec.filtro.repository.PersonaRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RegistrarHorarioService {

    private final RegistrarHorarioRepository registrarHorarioRepository;
    private final PersonaRepository personaRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public RegistrarHorarioService(RegistrarHorarioRepository registrarHorarioRepository,
                                   PersonaRepository personaRepository) {
        this.registrarHorarioRepository = registrarHorarioRepository;
        this.personaRepository = personaRepository;
    }

    // Crear registro
    public RegistrarHorario createRegistrarHorario(LocalDateTime fechaHora, Turno turno, Horarios horario, Long personaId) {
        Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada"));

        RegistrarHorario registrarHorario = RegistrarHorario.builder()
                .fechaHora(fechaHora)
                .turno(turno)
                .horarios(horario)
                .persona(persona)
                .build();

        System.out.println("Creando registro a las: " + fechaHora.format(formatter)); // evita error CharSequence

        return registrarHorarioRepository.save(registrarHorario);
    }

    // Actualizar registro
    public RegistrarHorario updateRegistrarHorario(Long id, LocalDateTime fechaHora, Turno turno, Horarios horario, Long personaId) {
        RegistrarHorario registro = registrarHorarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro no encontrado"));

        Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada"));

        registro.setFechaHora(fechaHora);
        registro.setTurno(turno);
        registro.setHorarios(horario);
        registro.setPersona(persona);

        System.out.println("Actualizando registro a las: " + fechaHora.format(formatter)); // evita error CharSequence

        return registrarHorarioRepository.save(registro);
    }

    // Eliminar registro
    public RegistrarHorario deleteRegistrarHorario(Long id) {
        RegistrarHorario registro = registrarHorarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro no encontrado"));

        registrarHorarioRepository.delete(registro);
        return registro;
    }

    // Buscar por ID
    public Optional<RegistrarHorario> findRegistrarHorarioById(Long id) {
        return registrarHorarioRepository.findById(id);
    }

    // Listar todos
    public List<RegistrarHorario> findAllRegistrarHorarios() {
        return registrarHorarioRepository.findAll();
    }

    // Listar por Persona
    public List<RegistrarHorario> findByPersonaId(Long personaId) {
        return registrarHorarioRepository.findByPersonaId(personaId);
    }
}
