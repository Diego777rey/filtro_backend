package py.edu.facitec.filtro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Sucursal;
import py.edu.facitec.filtro.service.SucursalService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    // ==== Queries ====
    @QueryMapping
    public List<Sucursal> findAllSucursales() {
        return sucursalService.findAllSucursales();
    }

    @QueryMapping
    public Sucursal findSucursalById(@Argument long sucursalId) {
        return sucursalService.findOneSucursal(sucursalId);
    }
    @QueryMapping
    public PaginadorDto<Sucursal> findSucursalesPaginated(
            @Argument("page") int page,
            @Argument("size") int size,
            @Argument("search") String search
    ) {
        return sucursalService.findSucursalesPaginated(page, size, search);
    }


    // ==== Mutations (adaptados al schema) ====
    @MutationMapping
    public Sucursal createSucursal(@Argument Sucursal inputSucursal) {
        return sucursalService.createSucursal(inputSucursal);
    }

    @MutationMapping
    public Sucursal updateSucursal(@Argument("id") long id,
                                     @Argument("inputSucursal") Sucursal inputSucursal) {
        return sucursalService.updateSucursal(id, inputSucursal);
    }

    @MutationMapping
    public Sucursal deleteSucursal(@Argument("id") Long id) {
        return sucursalService.deleteSucursal(id);
    }

    // ==== Subscriptions ====
    @SubscriptionMapping
    public Flux<Sucursal> findAllSucursalesFlux() {
        return sucursalService.findAllSucursalesFlux();
    }

    @SubscriptionMapping
    public Mono<Sucursal> findOneSucursalMono(@Argument("id") long id) {
        return sucursalService.findOneMono(id);
    }
}
