package py.edu.facitec.filtro.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import py.edu.facitec.filtro.dto.InputInventario;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Categoria;
import py.edu.facitec.filtro.entity.Inventario;
import py.edu.facitec.filtro.entity.Producto;
import py.edu.facitec.filtro.service.*;

import java.util.List;

@Controller
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private ProductoService productoService;
    @Autowired
    private DepositeroService depositeroService;

    @QueryMapping
    public List<Inventario> findAllInventarios() {
        return inventarioService.findAllInventarios();
    }

    @QueryMapping
    public Inventario findInventarioById(@Argument Long inventarioId) {
        return inventarioService.findOneInventario(inventarioId);
    }

    @QueryMapping
    public PaginadorDto<Inventario> findInventariosPaginated(
            @Argument("page") int page,
            @Argument("size") int size,
            @Argument("search") String search
    ) {
        return inventarioService.findInventariosPaginated(page, size, search);
    }

    @MutationMapping
    public Inventario createInventario(@Argument InputInventario inputInventario) {
        return inventarioService.saveInventario(inputInventario);
    }

    @MutationMapping
    public Inventario updateInventario(@Argument Long id,
                                   @Argument InputInventario inputInventario) {
        return inventarioService.updateInventario(id, inputInventario);
    }

    @MutationMapping
    public Inventario deleteInventario(@Argument("id") Long id) {
        return inventarioService.deleteInventario(id);
    }
    @SchemaMapping(typeName = "Inventario", field = "producto")
    public Producto getProducto(Inventario inventario) {
        return productoService.findOneProducto(inventario.getProducto().getId());
    }
}