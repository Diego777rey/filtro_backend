package py.edu.facitec.filtro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import py.edu.facitec.filtro.dto.InputVendedor;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Persona;
import py.edu.facitec.filtro.entity.Vendedor;
import py.edu.facitec.filtro.enums.TipoPersona;
import py.edu.facitec.filtro.repository.PersonaRepository;
import py.edu.facitec.filtro.repository.VendedorRepository;

import java.util.Optional;
import java.util.function.BiFunction;

@Service
public class VendedorService {

    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PaginadorService paginadorService;

    // ---------------- CREAR VENDEDOR ----------------
    public Vendedor createVendedor(InputVendedor input) {
        Vendedor v = new Vendedor();
        v.setCodigoVendedor(input.getCodigoVendedor());
        v.setComision(input.getComision());
        v.setSucursal(input.getSucursal());

        if (input.getPersonaId() != null) {
            Persona persona = personaRepository.findById(input.getPersonaId())
                    .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
            v.setPersona(persona);
        }

        return vendedorRepository.save(v);
    }

    // ---------------- ACTUALIZAR VENDEDOR ----------------
    public Vendedor updateVendedor(Long id, InputVendedor input) {
        Vendedor v = vendedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));

        v.setCodigoVendedor(input.getCodigoVendedor());
        v.setComision(input.getComision());
        v.setSucursal(input.getSucursal());

        if (input.getPersonaId() != null) {
            Persona persona = personaRepository.findById(input.getPersonaId())
                    .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
            v.setPersona(persona);
        }

        return vendedorRepository.save(v);
    }

    // ---------------- ELIMINAR VENDEDOR ----------------
    public Vendedor deleteVendedor(Long id) {
        Vendedor v = vendedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));
        vendedorRepository.delete(v);
        return v;
    }

    // ---------------- BUSCAR VENDEDOR POR ID ----------------
    public Optional<Vendedor> findVendedorById(Long id) {
        return vendedorRepository.findById(id);
    }

    // ---------------- PAGINACIÓN Y BÚSQUEDA ----------------
    public PaginadorDto<Vendedor> findVendedoresPaginated(int page, int size, String search) {
        BiFunction<String, Pageable, Page<Vendedor>> searchFunction = (s, pageable) -> {
            if (s == null || s.isEmpty()) {
                return vendedorRepository.findByPersonaRol(TipoPersona.VENDEDOR, pageable);
            } else {
                return vendedorRepository.findByPersonaRolAndNombreContaining(TipoPersona.VENDEDOR, s, pageable);
            }
        };

        return paginadorService.paginarConFiltro(searchFunction, search, page, size);
    }
}
