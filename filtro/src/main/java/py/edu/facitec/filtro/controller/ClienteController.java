package py.edu.facitec.filtro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import py.edu.facitec.filtro.dto.InputCliente;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Cliente;
import py.edu.facitec.filtro.service.ClienteService;

import java.util.Optional;

@Controller
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // ------------------- QUERIES -------------------

    @QueryMapping
    public Cliente findClienteById(@Argument("clienteId") Long clienteId) {
        Optional<Cliente> c = clienteService.findClienteById(clienteId);
        return c.orElse(null);
    }

    @QueryMapping
    public PaginadorDto<Cliente> findClientesPaginated(
            @Argument int page,
            @Argument int size,
            @Argument String search
    ) {
        return clienteService.findClientesPaginated(page, size, search);
    }

    // ------------------- MUTATIONS -------------------

    @MutationMapping
    public Cliente createCliente(@Argument("inputCliente") InputCliente input) {
        return clienteService.createCliente(input);
    }

    @MutationMapping
    public Cliente updateCliente(
            @Argument Long id,
            @Argument("inputCliente") InputCliente input
    ) {
        return clienteService.updateCliente(id, input);
    }

    @MutationMapping
    public Cliente deleteCliente(@Argument Long id) {
        return clienteService.deleteCliente(id);
    }
}
