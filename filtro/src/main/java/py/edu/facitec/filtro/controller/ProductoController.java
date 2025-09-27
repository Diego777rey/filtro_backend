package py.edu.facitec.filtro.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import py.edu.facitec.filtro.dto.InputProducto;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Categoria;
import py.edu.facitec.filtro.entity.Producto;
import py.edu.facitec.filtro.entity.Proveedor;
import py.edu.facitec.filtro.service.CategoriaService;
import py.edu.facitec.filtro.service.ProductoService;
import py.edu.facitec.filtro.service.ProveedorService;

import java.util.List;

@Controller
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private ProveedorService proveedorService;

    @QueryMapping
    public List<Producto> findAllProductos() {
        return productoService.findAllProductos();
    }

    @QueryMapping
    public Producto findProductoById(@Argument("productoId") Long productoId) {
        return productoService.findOneProducto(productoId);
    }

    @QueryMapping
    public PaginadorDto<Producto> findProductosPaginated(
            @Argument("page") int page,
            @Argument("size") int size,
            @Argument("search") String search
    ) {
        return productoService.findProductosPaginated(page, size, search);
    }

    @MutationMapping
    public Producto createProducto(@Argument("inputProducto") InputProducto inputProducto) {
        return productoService.saveProducto(inputProducto);
    }

    @MutationMapping
    public Producto updateProducto(@Argument("id") Long id,
                                   @Argument("inputProducto") InputProducto inputProducto) {
        return productoService.updateProducto(id, inputProducto);
    }

    @MutationMapping
    public Producto deleteProducto(@Argument("id") Long id) {
        return productoService.deleteProducto(id);
    }
    @SchemaMapping(typeName = "Producto", field = "categoria")
    public Categoria getCategoria(Producto producto) {
        return categoriaService.findOneCategoria(producto.getCategoria().getId());
    }
}