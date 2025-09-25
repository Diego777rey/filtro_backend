package py.edu.facitec.filtro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import py.edu.facitec.filtro.dto.InputVendedor;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Vendedor;
import py.edu.facitec.filtro.service.VendedorService;

import java.util.Optional;

@Controller
public class VendedorController {

    @Autowired
    private VendedorService vendedorService;

    // ------------------- QUERIES -------------------

    @QueryMapping
    public Vendedor findVendedorById(@Argument Long id) {
        Optional<Vendedor> v = vendedorService.findVendedorById(id);
        return v.orElse(null);
    }

    @QueryMapping
    public PaginadorDto<Vendedor> findVendedoresPaginated(
            @Argument int page,
            @Argument int size,
            @Argument String search
    ) {
        return vendedorService.findVendedoresPaginated(page, size, search);
    }

    // ------------------- MUTATIONS -------------------

    @MutationMapping
    public Vendedor createVendedor(@Argument("inputVendedor") InputVendedor input) {
        return vendedorService.createVendedor(input);
    }

    @MutationMapping
    public Vendedor updateVendedor(
            @Argument Long id,
            @Argument("inputVendedor") InputVendedor input
    ) {
        return vendedorService.updateVendedor(id, input);
    }

    @MutationMapping
    public Vendedor deleteVendedor(@Argument Long id) {
        return vendedorService.deleteVendedor(id);
    }
}
