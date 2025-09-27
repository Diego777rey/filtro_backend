package py.edu.facitec.filtro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import py.edu.facitec.filtro.dto.InputPersona;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Persona;
import py.edu.facitec.filtro.entity.Rol;
import py.edu.facitec.filtro.repository.PersonaRepository;
import py.edu.facitec.filtro.repository.RolRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiFunction;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PaginadorService paginadorService;
    public Persona createPersona(InputPersona input) {
        Set<Rol> roles = new HashSet<>();
        if (input.getRolIds() != null && !input.getRolIds().isEmpty()) {
            roles = new HashSet<>(rolRepository.findAllById(input.getRolIds()));
        }

        Date fechaNacimiento = parseFecha(input.getFechaNacimiento());

        Persona persona = Persona.builder()
                .nombre(input.getNombre())
                .apellido(input.getApellido())
                .direccion(input.getDireccion())
                .telefono(input.getTelefono())
                .email(input.getEmail())
                .documento(input.getDocumento())
                .estadoPersona(input.getEstadoPersona())
                .fechaNacimiento(fechaNacimiento)
                .roles(roles)
                .build();

        return personaRepository.save(persona);
    }
    public Persona updatePersona(Long id, InputPersona input) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        Date fechaNacimiento = parseFecha(input.getFechaNacimiento());

        persona.setNombre(input.getNombre());
        persona.setApellido(input.getApellido());
        persona.setDireccion(input.getDireccion());
        persona.setTelefono(input.getTelefono());
        persona.setEmail(input.getEmail());
        persona.setDocumento(input.getDocumento());
        persona.setEstadoPersona(input.getEstadoPersona());
        persona.setFechaNacimiento(fechaNacimiento);

        if (input.getRolIds() != null && !input.getRolIds().isEmpty()) {
            Set<Rol> roles = new HashSet<>(rolRepository.findAllById(input.getRolIds()));
            persona.setRoles(roles);
        }

        return personaRepository.save(persona);
    }
    private Date parseFecha(String fechaStr) {
        if (fechaStr == null || fechaStr.trim().isEmpty()) return null;

        try {
            if (fechaStr.contains("T")) {
                SimpleDateFormat isoFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                return isoFormatter.parse(fechaStr);
            } else {
                SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd");
                return simpleFormatter.parse(fechaStr);
            }
        } catch (ParseException e) {
            throw new RuntimeException("Formato de fecha inválido: " + fechaStr +
                    ". Use formato yyyy-MM-dd o yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        }
    }
    public Persona deletePersona(Long id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        personaRepository.delete(persona);
        return persona;
    }
    public Optional<Persona> findPersonaById(Long id) {
        return personaRepository.findById(id);
    }

    public List<Persona> findAllPersonas() {
        return personaRepository.findAll();
    }

    public List<Persona> findPersonasByNombre(String nombre) {
        return personaRepository.findByNombreContainingIgnoreCase(nombre);
    }
    public PaginadorDto<Persona> findAllPaginated(int page, int size, String search) {
        BiFunction<String, Pageable, Page<Persona>> searchFunction = (s, pageable) -> {
            if (s == null || s.isEmpty()) {
                return personaRepository.findAll(pageable);
            } else {
                return personaRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrDocumentoContainingIgnoreCase(
                        s, s, s, pageable
                );
            }
        };

        return paginadorService.paginarConFiltro(searchFunction, search, page, size);
    }
}
