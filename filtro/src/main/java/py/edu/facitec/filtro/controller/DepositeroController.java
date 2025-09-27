package py.edu.facitec.filtro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import py.edu.facitec.filtro.dto.InputDepositero;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Depositero;
import py.edu.facitec.filtro.service.DepositeroService;

import java.util.Optional;

@Controller
public class DepositeroController {

    @Autowired
    private DepositeroService depositeroService;

    // ------------------- QUERIES -------------------

    @QueryMapping
    public Depositero findDepositeroById(@Argument("DepositeroId") Long depositeroId) {
        Optional<Depositero> c = depositeroService.findDepositeroById(depositeroId);
        return c.orElse(null);
    }

    @QueryMapping
    public PaginadorDto<Depositero> findDepositerosPaginated(
            @Argument int page,
            @Argument int size,
            @Argument String search
    ) {
        return depositeroService.findDepositerosPaginated(page, size, search);
    }

    // ------------------- MUTATIONS -------------------

    @MutationMapping
    public Depositero createDepositero(@Argument("inputDepositero") InputDepositero input) {
        return depositeroService.createDepositero(input);
    }

    @MutationMapping
    public Depositero updateDepositero(
            @Argument Long id,
            @Argument("inputDepositero") InputDepositero input
    ) {
        return depositeroService.updateDepositero(id, input);
    }

    @MutationMapping
    public Depositero deleteDepositero(@Argument Long id) {
        return depositeroService.deleteDepositero(id);
    }
}
