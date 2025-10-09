package py.edu.facitec.filtro.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.edu.facitec.filtro.dto.InputInventario;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.*;
import py.edu.facitec.filtro.repository.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private DepositeroRepository depositeroRepository;
    @Autowired
    private VentaDetalleRepository ventaDetalleRepository;

    @Autowired
    private PaginadorService paginadorService;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public List<Inventario> findAllInventarios() {
        return inventarioRepository.findAll();
    }

    public Inventario findOneInventario(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario con id " + id + " no existe"));
    }

    public Inventario saveInventario(InputInventario dto) {
        validarCamposObligatorios(dto);

        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id " + dto.getProductoId()));

        Depositero depositero = depositeroRepository.findById(dto.getDepositeroId())
                .orElseThrow(() -> new RuntimeException("Depositero no encontrado con id " + dto.getDepositeroId()));

        VentaDetalle ventaDetalle = null;
        if (dto.getVentaDetalleId() != null) {
            ventaDetalle = ventaDetalleRepository.findById(dto.getVentaDetalleId())
                    .orElseThrow(() -> new RuntimeException("VentaDetalle no encontrado con id " + dto.getVentaDetalleId()));
        }

        Date fecha;
        try {
            fecha = sdf.parse(dto.getFechaMovimiento());
        } catch (ParseException e) {
            throw new RuntimeException("Formato de fecha inválido. Use yyyy-MM-dd");
        }

        Inventario inventario = Inventario.builder()
                .fechaMovimiento(fecha)
                .tipoMovimiento(dto.getTipoMovimiento())
                .cantidad(dto.getCantidad())
                .stockAnterior(dto.getStockAnterior())
                .stockNuevo(dto.getStockNuevo())
                .motivo(dto.getMotivo())
                .estodoInventario(dto.getEstadoInventario()) // corregido typo
                .producto(producto)
                .depositero(depositero)
                .ventaDetalle(ventaDetalle)
                .build();

        Inventario saved = inventarioRepository.save(inventario);
        log.info("Inventario creado: {}", saved.getMotivo());
        return saved;
    }

    public Inventario updateInventario(Long id, InputInventario dto) {
        validarCamposObligatorios(dto);

        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario con id " + id + " no existe"));

        Date fecha;
        try {
            fecha = sdf.parse(dto.getFechaMovimiento());
        } catch (ParseException e) {
            throw new RuntimeException("Formato de fecha inválido. Use yyyy-MM-dd");
        }

        inventario.setFechaMovimiento(fecha);
        inventario.setTipoMovimiento(dto.getTipoMovimiento());
        inventario.setCantidad(dto.getCantidad());
        inventario.setStockAnterior(dto.getStockAnterior());
        inventario.setStockNuevo(dto.getStockNuevo());
        inventario.setMotivo(dto.getMotivo());
        inventario.setEstodoInventario(dto.getEstadoInventario()); // corregido typo

        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id " + dto.getProductoId()));
        inventario.setProducto(producto);

        Depositero depositero = depositeroRepository.findById(dto.getDepositeroId())
                .orElseThrow(() -> new RuntimeException("Depositero no encontrado con id " + dto.getDepositeroId()));
        inventario.setDepositero(depositero);

        VentaDetalle ventaDetalle = null;
        if (dto.getVentaDetalleId() != null) {
            ventaDetalle = ventaDetalleRepository.findById(dto.getVentaDetalleId())
                    .orElseThrow(() -> new RuntimeException("VentaDetalle no encontrado con id " + dto.getVentaDetalleId()));
        }
        inventario.setVentaDetalle(ventaDetalle);

        Inventario updated = inventarioRepository.save(inventario);
        log.info("Inventario actualizado: {}", updated.getMotivo());
        return updated;
    }

    public Inventario deleteInventario(Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario con id " + id + " no existe"));
        inventarioRepository.delete(inventario);
        log.info("Inventario eliminado: {}", inventario.getMotivo());
        return inventario;
    }

    public PaginadorDto<Inventario> findInventariosPaginated(int page, int size, String search) {
        return paginadorService.paginarConFiltro(
                (s, pageable) -> {
                    if (s == null || s.trim().isEmpty()) {
                        return inventarioRepository.findAll(pageable);
                    }
                    return inventarioRepository.findByMotivoContainingIgnoreCase(s, pageable);
                },
                search,
                page,
                size
        );
    }

    private void validarCamposObligatorios(InputInventario dto) {
        // Podés agregar validaciones aquí si querés
    }
}
