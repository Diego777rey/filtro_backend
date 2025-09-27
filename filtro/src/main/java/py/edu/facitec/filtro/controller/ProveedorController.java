package py.edu.facitec.filtro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import py.edu.facitec.filtro.dto.InputProveedor;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Proveedor;
import py.edu.facitec.filtro.service.ProveedorService;

import java.util.Optional;

@Controller
public class ProveedorController {

    @Autowired
    private ProveedorService ProveedorService;

    // ------------------- QUERIES -------------------

    @QueryMapping
    public Proveedor findProveedorById(@Argument Long proveedorId) {
        Optional<Proveedor> v = ProveedorService.findProveedorById(proveedorId);
        return v.orElse(null);
    }

    @QueryMapping
    public PaginadorDto<Proveedor> findProveedoresPaginated(
            @Argument int page,
            @Argument int size,
            @Argument String search
    ) {
        return ProveedorService.findProveedoresPaginated(page, size, search);
    }

    // ------------------- MUTATIONS -------------------

    @MutationMapping
    public Proveedor createProveedor(@Argument("inputProveedor") InputProveedor input) {
        return ProveedorService.createProveedor(input);
    }

    @MutationMapping
    public Proveedor updateProveedor(
            @Argument Long id,
            @Argument("inputProveedor") InputProveedor input
    ) {
        return ProveedorService.updateProveedor(id, input);
    }

    @MutationMapping
    public Proveedor deleteProveedor(@Argument Long id) {
        return ProveedorService.deleteProveedor(id);
    }
}
