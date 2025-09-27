package py.edu.facitec.filtro.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.edu.facitec.filtro.dto.InputProducto;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Categoria;
import py.edu.facitec.filtro.entity.Producto;
import py.edu.facitec.filtro.entity.Proveedor;
import py.edu.facitec.filtro.repository.CategoriaRepository;
import py.edu.facitec.filtro.repository.ProductoRepository;
import py.edu.facitec.filtro.repository.ProveedorRepository;

import java.util.List;

@Service
@Slf4j
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private PaginadorService paginadorService;

    public List<Producto> findAllProductos() {
        return productoRepository.findAll(); // ya trae categoría gracias a @EntityGraph
    }

    public Producto findOneProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto con id " + id + " no existe"));
    }

    public Producto saveProducto(InputProducto dto) {
        validarCamposObligatorios(dto);
        validarNegocio(dto);

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id " + dto.getCategoriaId()));

        Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id " + dto.getProveedorId()));

        Producto producto = Producto.builder()
                .codigoProducto(dto.getCodigoProducto())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precioCompra(dto.getPrecioCompra())
                .precioVenta(dto.getPrecioVenta())
                .stock(dto.getStock())
                .productoEstado(dto.getProductoEstado())
                .categoria(categoria)
                .proveedor(proveedor)
                .build();

        Producto saved = productoRepository.save(producto);
        log.info("Producto creado: {}", saved.getNombre());
        return saved;
    }

    public Producto updateProducto(Long id, InputProducto dto) {
        validarCamposObligatorios(dto);
        validarNegocio(dto);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto con id " + id + " no existe"));
        producto.setCodigoProducto(dto.getCodigoProducto());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioCompra((dto.getPrecioCompra()));
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setStock(dto.getStock());
        producto.setProductoEstado(dto.getProductoEstado());

        Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id " + dto.getProveedorId()));
        producto.setProveedor(proveedor);

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id " + dto.getCategoriaId()));
        producto.setCategoria(categoria);

        Producto updated = productoRepository.save(producto);
        log.info("Producto actualizado: {}", updated.getNombre());
        return updated;
    }

    public Producto deleteProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto con id " + id + " no existe"));
        productoRepository.delete(producto);
        log.info("Producto eliminado: {}", producto.getNombre());
        return producto;
    }

    public PaginadorDto<Producto> findProductosPaginated(int page, int size, String search) {
        return paginadorService.paginarConFiltro(
                (s, pageable) -> {
                    if (s == null || s.trim().isEmpty()) {
                        return productoRepository.findAll(pageable);
                    }
                    return productoRepository.findByNombreContainingIgnoreCase(s, pageable);
                },
                search,
                page,
                size
        );
    }
    private void validarCamposObligatorios(InputProducto dto) {
        if (dto.getDescripcion() == null || dto.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }
    }

    private void validarNegocio(InputProducto dto) {
        if (dto.getStock() != null && dto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }
}