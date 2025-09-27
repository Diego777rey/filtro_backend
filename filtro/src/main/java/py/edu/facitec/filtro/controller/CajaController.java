package py.edu.facitec.filtro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Caja;
import py.edu.facitec.filtro.service.CajaService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
public class CajaController {

    @Autowired
    private CajaService cajaService;

    // ==== Queries ====
    @QueryMapping
    public List<Caja> findAllCajas() {
        return cajaService.findAllCajas();
    }

    @QueryMapping
    public Caja findCajaById(long cajaId) {
        return cajaService.findOneCaja(cajaId);
    }
    @QueryMapping
    public PaginadorDto<Caja> findCajasPaginated(
            @Argument("page") int page,
            @Argument("size") int size,
            @Argument("search") String search
    ) {
        return cajaService.findCajasPaginated(page, size, search);
    }


    // ==== Mutations (adaptados al schema) ====
    @MutationMapping
    public Caja saveCaja(@Argument("inputCaja") Caja inputCaja) {
        return cajaService.saveCaja(inputCaja);
    }

    @MutationMapping
    public Caja updateCaja(@Argument("id") long id,
                                     @Argument("inputCaja") Caja inputCaja) {
        return cajaService.updateCaja(id, inputCaja);
    }

    @MutationMapping
    public Caja deleteCaja(@Argument("id") Long id) {
        return cajaService.deleteCaja(id);
    }

    // ==== Subscriptions ====
    @SubscriptionMapping
    public Flux<Caja> findAllCajasFlux() {
        return cajaService.findAllCajasFlux();
    }

    @SubscriptionMapping
    public Mono<Caja> findOneCajaMono(@Argument("id") long id) {
        return cajaService.findOneMono(id);
    }
}
