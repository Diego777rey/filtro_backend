package py.edu.facitec.filtro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.edu.facitec.filtro.dto.InputPersona;
import py.edu.facitec.filtro.entity.Persona;
import py.edu.facitec.filtro.entity.Rol;
import py.edu.facitec.filtro.repository.PersonaRepository;
import py.edu.facitec.filtro.repository.RolRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private RolRepository rolRepository;

    // ---------------- CREAR PERSONA ----------------
    public Persona createPersona(InputPersona input) {
        Set<Rol> roles = new HashSet<>();
        if (input.getRolIds() != null && !input.getRolIds().isEmpty()) {
            roles = new HashSet<>(rolRepository.findAllById(input.getRolIds()));
        }

        // Convertir String a Date
        Date fechaNacimiento = parseFecha(input.getFechaNacimiento());

        Persona persona = Persona.builder()
                .nombre(input.getNombre())
                .apellido(input.getApellido())
                .direccion(input.getDireccion())
                .telefono(input.getTelefono())
                .email(input.getEmail())
                .documento(input.getDocumento())
                .estadoPersona(input.getEstadoPersona())
                .fechaNacimiento(fechaNacimiento) // Usar el Date convertido
                .roles(roles)
                .build();

        return personaRepository.save(persona);
    }

    // ---------------- ACTUALIZAR PERSONA ----------------
    public Persona updatePersona(Long id, InputPersona input) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        // Convertir String a Date
        Date fechaNacimiento = parseFecha(input.getFechaNacimiento());

        persona.setNombre(input.getNombre());
        persona.setApellido(input.getApellido());
        persona.setDireccion(input.getDireccion());
        persona.setTelefono(input.getTelefono());
        persona.setEmail(input.getEmail());
        persona.setDocumento(input.getDocumento());
        persona.setEstadoPersona(input.getEstadoPersona());
        persona.setFechaNacimiento(fechaNacimiento); // Usar el Date convertido

        if (input.getRolIds() != null && !input.getRolIds().isEmpty()) {
            Set<Rol> roles = new HashSet<>(rolRepository.findAllById(input.getRolIds()));
            persona.setRoles(roles);
        }

        return personaRepository.save(persona);
    }

    // ---------------- MÉTODO PARA CONVERTIR STRING A DATE ----------------
    private Date parseFecha(String fechaStr) {
        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            return null;
        }

        try {
            // Intentar con formato ISO (yyyy-MM-dd'T'HH:mm:ss.SSS'Z')
            if (fechaStr.contains("T")) {
                SimpleDateFormat isoFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                return isoFormatter.parse(fechaStr);
            }
            // Intentar con formato simple (yyyy-MM-dd)
            else {
                SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd");
                return simpleFormatter.parse(fechaStr);
            }
        } catch (ParseException e) {
            throw new RuntimeException("Formato de fecha inválido: " + fechaStr + ". Use formato yyyy-MM-dd o yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        }
    }

    // ---------------- ELIMINAR PERSONA ----------------
    public Persona deletePersona(Long id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        personaRepository.delete(persona);
        return persona;
    }

    // ---------------- BUSCAR PERSONA ----------------
    public Optional<Persona> findPersonaById(Long id) {
        return personaRepository.findById(id);
    }

    public List<Persona> findAllPersonas() {
        return personaRepository.findAll();
    }

    // ---------------- BUSCAR PERSONAS POR NOMBRE ----------------
    public List<Persona> findPersonasByNombre(String nombre) {
        return personaRepository.findByNombreContainingIgnoreCase(nombre);
    }
}