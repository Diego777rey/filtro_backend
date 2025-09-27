package py.edu.facitec.filtro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import py.edu.facitec.filtro.dto.InputDepositero;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Persona;
import py.edu.facitec.filtro.entity.Depositero;
import py.edu.facitec.filtro.enums.TipoPersona;
import py.edu.facitec.filtro.repository.DepositeroRepository;
import py.edu.facitec.filtro.repository.PersonaRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.function.BiFunction;

@Service
public class DepositeroService {

    @Autowired
    private DepositeroRepository depositeroRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PaginadorService paginadorService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // ---------------- MÉTODO PARA CONVERTIR STRING A DATE ----------------
    private Date parseFecha(String fechaStr) {
        if (fechaStr == null || fechaStr.trim().isEmpty()) return null;
        try {
            return dateFormat.parse(fechaStr);
        } catch (ParseException e) {
            throw new RuntimeException("Formato de fecha inválido, debe ser yyyy-MM-dd", e);
        }
    }

    // ---------------- CREAR Depositero ----------------
    public Depositero createDepositero(InputDepositero input) {
        Depositero d = new Depositero();
        d.setCodigoDepositero(input.getCodigoDepositero());
        d.setFechaIngreso(parseFecha(input.getFechaIngreso()));
        d.setTurno(input.getTurno());
        d.setAlmacenAsignado(input.getAlmacenAsignado());
        d.setSupervisor(input.getSupervisor());
        d.setHorario(input.getHorario());
        d.setEstado(input.getEstado());

        if (input.getPersonaId() != null) {
            Persona persona = personaRepository.findById(input.getPersonaId())
                    .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
            d.setPersona(persona);
        }

        return depositeroRepository.save(d);
    }

    // ---------------- ACTUALIZAR Depositero ----------------
    public Depositero updateDepositero(Long id, InputDepositero input) {
        Depositero d = depositeroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Depositero no encontrado"));

        d.setCodigoDepositero(input.getCodigoDepositero());
        d.setFechaIngreso(parseFecha(input.getFechaIngreso()));
        d.setTurno(input.getTurno());
        d.setAlmacenAsignado(input.getAlmacenAsignado());
        d.setSupervisor(input.getSupervisor());
        d.setHorario(input.getHorario());
        d.setEstado(input.getEstado());

        if (input.getPersonaId() != null) {
            Persona persona = personaRepository.findById(input.getPersonaId())
                    .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
            d.setPersona(persona);
        }

        return depositeroRepository.save(d);
    }

    // ---------------- ELIMINAR Depositero ----------------
    public Depositero deleteDepositero(Long id) {
        Depositero c = depositeroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Depositero no encontrado"));
        depositeroRepository.delete(c);
        return c;
    }

    // ---------------- BUSCAR Depositero POR ID ----------------
    public Optional<Depositero> findDepositeroById(Long id) {
        return depositeroRepository.findById(id);
    }

    // ---------------- PAGINACIÓN Y BÚSQUEDA POR ROL Depositero ----------------
    public PaginadorDto<Depositero> findDepositerosPaginated(int page, int size, String search) {

        BiFunction<String, Pageable, Page<Depositero>> searchFunction = (s, pageable) -> {
            java.util.List<TipoPersona> roles = java.util.List.of(TipoPersona.DEPOSITERO, TipoPersona.VENDEDOR);

            if (s == null || s.isEmpty()) {
                return depositeroRepository.findByPersonaRolesIn(roles, pageable);
            } else {
                return depositeroRepository.findByPersonaRolesInAndNombreContaining(roles, s, pageable);
            }
        };

        return paginadorService.paginarConFiltro(searchFunction, search, page, size);
    }

}
