package py.edu.facitec.filtro.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import py.edu.facitec.filtro.dto.InputRegistrarHorario;
import py.edu.facitec.filtro.entity.RegistrarHorario;
import py.edu.facitec.filtro.enums.Horarios;
import py.edu.facitec.filtro.enums.Turno;
import py.edu.facitec.filtro.service.RegistrarHorarioService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class RegistrarHorarioController {

    private final RegistrarHorarioService registrarHorarioService;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public RegistrarHorarioController(RegistrarHorarioService registrarHorarioService) {
        this.registrarHorarioService = registrarHorarioService;
    }

    // -------------------- QUERIES --------------------

    @QueryMapping
    public RegistrarHorario findRegistrarHorarioById(@Argument Long registrarHorarioId) {
        return registrarHorarioService.findRegistrarHorarioById(registrarHorarioId)
                .orElseThrow(() -> new IllegalArgumentException("Registro no encontrado"));
    }

    @QueryMapping
    public List<RegistrarHorario> findAllRegistrarHorarios() {
        return registrarHorarioService.findAllRegistrarHorarios();
    }

    @QueryMapping
    public List<RegistrarHorario> findRegistrarHorariosPorVendedor(@Argument Long vendedorId) {
        return registrarHorarioService.findByPersonaId(vendedorId);
    }

    // -------------------- MUTATIONS --------------------

    @MutationMapping
    public RegistrarHorario createRegistrarHorario(@Argument("inputRegistrarHorario") InputRegistrarHorario input) {
        LocalDateTime fechaHora = LocalDateTime.parse(input.getFechaHora(), FORMATTER);
        Horarios horario = input.getHorarios(); // ✅ ya es enum
        Turno turno = input.getTurno();
        return registrarHorarioService.createRegistrarHorario(fechaHora, turno , horario, input.getPersonaId());
    }

    @MutationMapping
    public RegistrarHorario updateRegistrarHorario(@Argument Long id,
                                                   @Argument("inputRegistrarHorario") InputRegistrarHorario input) {
        LocalDateTime fechaHora = LocalDateTime.parse(input.getFechaHora(), FORMATTER);
        Horarios horario = input.getHorarios();
        Turno turno = input.getTurno();// ✅ ya es enum
        return registrarHorarioService.updateRegistrarHorario(id, fechaHora, turno, horario, input.getPersonaId());
    }

    @MutationMapping
    public RegistrarHorario deleteRegistrarHorario(@Argument Long id) {
        return registrarHorarioService.deleteRegistrarHorario(id);
    }
}