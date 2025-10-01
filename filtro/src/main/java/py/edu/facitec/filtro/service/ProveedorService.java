package py.edu.facitec.filtro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import py.edu.facitec.filtro.dto.InputProveedor;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Proveedor;
import py.edu.facitec.filtro.repository.ProveedorRepository;

import java.util.Optional;
import java.util.function.BiFunction;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private PaginadorService paginadorService;

    // ---------------- CREAR Proveedor ----------------
    public Proveedor createProveedor(InputProveedor input) {
        Proveedor v = new Proveedor();
        v.setRuc(input.getRuc());
        v.setRazonSocial(input.getRazonSocial());
        v.setRubro(input.getRubro());
        v.setTelefono(input.getTelefono());
        v.setEmail(input.getEmail());
        v.setObservaciones(input.getObservaciones());

        return proveedorRepository.save(v);
    }

    // ---------------- ACTUALIZAR Proveedor ----------------
    public Proveedor updateProveedor(Long id, InputProveedor input) {
        Proveedor v = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        v.setRuc(input.getRuc());
        v.setRazonSocial(input.getRazonSocial());
        v.setRubro(input.getRubro());
        v.setTelefono(input.getTelefono());
        v.setEmail(input.getEmail());
        v.setObservaciones(input.getObservaciones());

        return proveedorRepository.save(v);
    }

    // ---------------- ELIMINAR Proveedor ----------------
    public Proveedor deleteProveedor(Long id) {
        Proveedor v = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        proveedorRepository.delete(v);
        return v;
    }

    // ---------------- BUSCAR Proveedor POR ID ----------------
    public Optional<Proveedor> findProveedorById(Long id) {
        return proveedorRepository.findById(id);
    }

    // ---------------- PAGINACIÓN Y BÚSQUEDA ----------------
    public PaginadorDto<Proveedor> findProveedoresPaginated(int page, int size, String search) {
        BiFunction<String, Pageable, Page<Proveedor>> searchFunction = (s, pageable) -> {
            if (s == null || s.isEmpty()) {
                return proveedorRepository.findAll(pageable);
            } else {
                return proveedorRepository.findByRazonSocialContainingIgnoreCase(s, pageable);
            }
        };

        return paginadorService.paginarConFiltro(searchFunction, search, page, size);
    }
}
