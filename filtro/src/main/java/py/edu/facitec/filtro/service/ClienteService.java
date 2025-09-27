package py.edu.facitec.filtro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import py.edu.facitec.filtro.dto.InputCliente;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Persona;
import py.edu.facitec.filtro.entity.Cliente;
import py.edu.facitec.filtro.enums.TipoPersona;
import py.edu.facitec.filtro.repository.ClienteRepository;
import py.edu.facitec.filtro.repository.PersonaRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.function.BiFunction;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

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

    // ---------------- CREAR CLIENTE ----------------
    public Cliente createCliente(InputCliente input) {
        Cliente c = new Cliente();
        c.setCodigoCliente(input.getCodigoCliente());
        c.setFechaRegistro(parseFecha(input.getFechaRegistro()));

        if (input.getPersonaId() != null) {
            Persona persona = personaRepository.findById(input.getPersonaId())
                    .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
            c.setPersona(persona);
        }

        return clienteRepository.save(c);
    }

    // ---------------- ACTUALIZAR CLIENTE ----------------
    public Cliente updateCliente(Long id, InputCliente input) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        c.setCodigoCliente(input.getCodigoCliente());
        c.setFechaRegistro(parseFecha(input.getFechaRegistro()));

        if (input.getPersonaId() != null) {
            Persona persona = personaRepository.findById(input.getPersonaId())
                    .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
            c.setPersona(persona);
        }

        return clienteRepository.save(c);
    }

    // ---------------- ELIMINAR CLIENTE ----------------
    public Cliente deleteCliente(Long id) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        clienteRepository.delete(c);
        return c;
    }

    // ---------------- BUSCAR CLIENTE POR ID ----------------
    public Optional<Cliente> findClienteById(Long id) {
        return clienteRepository.findById(id);
    }

    // ---------------- PAGINACIÓN Y BÚSQUEDA POR ROL CLIENTE ----------------
    public PaginadorDto<Cliente> findClientesPaginated(int page, int size, String search) {

        BiFunction<String, Pageable, Page<Cliente>> searchFunction = (s, pageable) -> {
            java.util.List<TipoPersona> roles = java.util.List.of(TipoPersona.CLIENTE, TipoPersona.VENDEDOR, TipoPersona.ADMINISTRADOR, TipoPersona.CAJERO, TipoPersona.PROVEEDOR, TipoPersona.DEPOSITERO);

            if (s == null || s.isEmpty()) {
                return clienteRepository.findByPersonaRolesIn(roles, pageable);
            } else {
                return clienteRepository.findByPersonaRolesInAndNombreContaining(roles, s, pageable);
            }
        };

        return paginadorService.paginarConFiltro(searchFunction, search, page, size);
    }

}
