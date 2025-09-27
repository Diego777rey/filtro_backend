package py.edu.facitec.filtro.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import py.edu.facitec.filtro.dto.InputCajero;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Caja;
import py.edu.facitec.filtro.entity.Cajero;
import py.edu.facitec.filtro.service.CajaService;
import py.edu.facitec.filtro.service.CajeroService;

import java.util.List;

@Controller
public class CajeroController {

    @Autowired
    private CajeroService cajeroService;

    @Autowired
    private CajaService cajaService;

    @QueryMapping
    public List<Cajero> findAllCajeros() {
        return cajeroService.findAllCajeros();
    }

    @QueryMapping
    public Cajero findCajeroById(@Argument("CajeroId") Long CajeroId) {
        return cajeroService.findOneCajero(CajeroId);
    }

    @QueryMapping
    public PaginadorDto<Cajero> findCajerosPaginated(
            @Argument("page") int page,
            @Argument("size") int size,
            @Argument("search") String search
    ) {
        return cajeroService.findCajerosPaginated(page, size, search);
    }

    @MutationMapping
    public Cajero createCajero(@Argument("inputCajero") InputCajero inputCajero) {
        return cajeroService.saveCajero(inputCajero);
    }

    @MutationMapping
    public Cajero updateCajero(@Argument("id") Long id,
                                   @Argument("inputCajero") InputCajero inputCajero) {
        return cajeroService.updateCajero(id, inputCajero);
    }

    @MutationMapping
    public Cajero deleteCajero(@Argument("id") Long id) {
        return cajeroService.deleteCajero(id);
    }
    @SchemaMapping(typeName = "Cajero", field = "caja")
    public Caja getCaja(Cajero Cajero) {
        return cajaService.findOneCaja(Cajero.getCaja().getId());
    }
}