package py.edu.facitec.filtro.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.edu.facitec.filtro.dto.InputMovimientoCaja;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Categoria;
import py.edu.facitec.filtro.entity.MovimientoCaja;
import py.edu.facitec.filtro.entity.Proveedor;
import py.edu.facitec.filtro.repository.CategoriaRepository;
import py.edu.facitec.filtro.repository.MovimientoCajaRepository;
import py.edu.facitec.filtro.repository.ProveedorRepository;

import java.util.List;

@Service
@Slf4j
public class MovimientoCajaService {

    @Autowired
    private MovimientoCajaRepository MovimientoCajaRepository;

    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private CajaRepository cajaRepository;

    @Autowired
    private PaginadorService paginadorService;

    public List<MovimientoCaja> findAllMovimientoCajas() {
        return MovimientoCajaRepository.findAll(); // ya trae categoría gracias a @EntityGraph
    }

    public MovimientoCaja findOneMovimientoCaja(Long id) {
        return MovimientoCajaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MovimientoCaja con id " + id + " no existe"));
    }

    public MovimientoCaja saveMovimientoCaja(InputMovimientoCaja dto) {
        validarCamposObligatorios(dto);
        validarNegocio(dto);

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id " + dto.getCategoriaId()));

        Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id " + dto.getProveedorId()));

        MovimientoCaja MovimientoCaja = MovimientoCaja.builder()
                .codigoMovimientoCaja(dto.getCodigoMovimientoCaja())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precioCompra(dto.getPrecioCompra())
                .precioVenta(dto.getPrecioVenta())
                .stock(dto.getStock())
                .MovimientoCajaEstado(dto.getMovimientoCajaEstado())
                .categoria(categoria)
                .proveedor(proveedor)
                .build();

        MovimientoCaja saved = MovimientoCajaRepository.save(MovimientoCaja);
        log.info("MovimientoCaja creado: {}", saved.getNombre());
        return saved;
    }

    public MovimientoCaja updateMovimientoCaja(Long id, InputMovimientoCaja dto) {
        validarCamposObligatorios(dto);
        validarNegocio(dto);

        MovimientoCaja MovimientoCaja = MovimientoCajaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MovimientoCaja con id " + id + " no existe"));
        MovimientoCaja.setCodigoMovimientoCaja(dto.getCodigoMovimientoCaja());
        MovimientoCaja.setNombre(dto.getNombre());
        MovimientoCaja.setDescripcion(dto.getDescripcion());
        MovimientoCaja.setPrecioCompra((dto.getPrecioCompra()));
        MovimientoCaja.setPrecioVenta(dto.getPrecioVenta());
        MovimientoCaja.setStock(dto.getStock());
        MovimientoCaja.setMovimientoCajaEstado(dto.getMovimientoCajaEstado());

        Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id " + dto.getProveedorId()));
        MovimientoCaja.setProveedor(proveedor);

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id " + dto.getCategoriaId()));
        MovimientoCaja.setCategoria(categoria);

        MovimientoCaja updated = MovimientoCajaRepository.save(MovimientoCaja);
        log.info("MovimientoCaja actualizado: {}", updated.getNombre());
        return updated;
    }

    public MovimientoCaja deleteMovimientoCaja(Long id) {
        MovimientoCaja MovimientoCaja = MovimientoCajaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MovimientoCaja con id " + id + " no existe"));
        MovimientoCajaRepository.delete(MovimientoCaja);
        log.info("MovimientoCaja eliminado: {}", MovimientoCaja.getNombre());
        return MovimientoCaja;
    }

    public PaginadorDto<MovimientoCaja> findMovimientoCajasPaginated(int page, int size, String search) {
        return paginadorService.paginarConFiltro(
                (s, pageable) -> {
                    if (s == null || s.trim().isEmpty()) {
                        return MovimientoCajaRepository.findAll(pageable);
                    }
                    return MovimientoCajaRepository.findByNombreContainingIgnoreCase(s, pageable);
                },
                search,
                page,
                size
        );
    }
    private void validarCamposObligatorios(InputMovimientoCaja dto) {
        if (dto.getDescripcion() == null || dto.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }
    }

    private void validarNegocio(InputMovimientoCaja dto) {
        if (dto.getStock() != null && dto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }
}