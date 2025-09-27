package py.edu.facitec.filtro.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.edu.facitec.filtro.dto.InputCajero;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Caja;
import py.edu.facitec.filtro.entity.Cajero;
import py.edu.facitec.filtro.entity.Persona;
import py.edu.facitec.filtro.repository.CajaRepository;
import py.edu.facitec.filtro.repository.CajeroRepository;
import py.edu.facitec.filtro.repository.PersonaRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CajeroService {

    @Autowired
    private CajeroRepository cajeroRepository;

    @Autowired
    private CajaRepository cajaRepository;

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

    public List<Cajero> findAllCajeros() {
        return cajeroRepository.findAll(); // trae caja gracias a @EntityGraph
    }

    public Cajero findOneCajero(Long id) {
        return cajeroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cajero con id " + id + " no existe"));
    }

    public Cajero saveCajero(InputCajero dto) {
        validarCamposObligatorios(dto);

        // Traer Persona desde DB
        Persona persona = personaRepository.findById(dto.getPersonaId())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id " + dto.getPersonaId()));

        // Traer Caja desde DB
        Caja caja = cajaRepository.findById(dto.getCajaId())
                .orElseThrow(() -> new RuntimeException("Caja no encontrada con id " + dto.getCajaId()));

        Cajero cajero = Cajero.builder()
                .codigoCajero(dto.getCodigoCajero())
                .turno(dto.getTurno())
                .fechaIngreso(parseFecha(dto.getFechaIngreso()))
                .estado(dto.getEstado())
                .persona(persona)
                .caja(caja)
                .build();

        Cajero saved = cajeroRepository.save(cajero);
        log.info("Cajero creado: {} {}", saved.getPersona().getNombre(), saved.getPersona().getApellido());
        return saved;
    }

    public Cajero updateCajero(Long id, InputCajero dto) {
        validarCamposObligatorios(dto);

        Cajero cajero = cajeroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cajero con id " + id + " no existe"));

        Persona persona = personaRepository.findById(dto.getPersonaId())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id " + dto.getPersonaId()));

        Caja caja = cajaRepository.findById(dto.getCajaId())
                .orElseThrow(() -> new RuntimeException("Caja no encontrada con id " + dto.getCajaId()));

        cajero.setCodigoCajero(dto.getCodigoCajero());
        cajero.setTurno(dto.getTurno());
        cajero.setFechaIngreso(parseFecha(dto.getFechaIngreso()));
        cajero.setEstado(dto.getEstado());
        cajero.setPersona(persona);
        cajero.setCaja(caja);

        Cajero updated = cajeroRepository.save(cajero);
        log.info("Cajero actualizado: {} {}", updated.getPersona().getNombre(), updated.getPersona().getApellido());
        return updated;
    }

    public Cajero deleteCajero(Long id) {
        Cajero cajero = cajeroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cajero con id " + id + " no existe"));
        cajeroRepository.delete(cajero);
        log.info("Cajero eliminado: {} {}", cajero.getPersona().getNombre(), cajero.getPersona().getApellido());
        return cajero;
    }

    public PaginadorDto<Cajero> findCajerosPaginated(int page, int size, String search) {
        return paginadorService.paginarConFiltro(
                (s, pageable) -> {
                    if (s == null || s.trim().isEmpty()) {
                        return cajeroRepository.findAll(pageable);
                    }
                    return cajeroRepository.findByNombreContainingIgnoreCase(s, pageable);
                },
                search,
                page,
                size
        );
    }

    private void validarCamposObligatorios(InputCajero dto) {
        if (dto.getPersonaId() == null) {
            throw new RuntimeException("El ID de la persona es obligatorio");
        }
        if (dto.getCajaId() == null) {
            throw new RuntimeException("El ID de la caja es obligatorio");
        }
        if (dto.getCodigoCajero() == null || dto.getCodigoCajero().trim().isEmpty()) {
            throw new RuntimeException("El código del cajero es obligatorio");
        }
        if (dto.getFechaIngreso() == null || dto.getFechaIngreso().trim().isEmpty()) {
            throw new RuntimeException("La fecha de ingreso es obligatoria");
        }
        if (dto.getTurno() == null || dto.getTurno().trim().isEmpty()) {
            throw new RuntimeException("El turno es obligatorio");
        }
        if (dto.getEstado() == null || dto.getEstado().trim().isEmpty()) {
            throw new RuntimeException("El estado es obligatorio");
        }
    }
}
