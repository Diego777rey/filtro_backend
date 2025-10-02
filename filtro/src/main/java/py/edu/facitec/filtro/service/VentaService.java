package py.edu.facitec.filtro.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import py.edu.facitec.filtro.dto.InputVenta;
import py.edu.facitec.filtro.dto.InputVentaDetalle;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.*;
import py.edu.facitec.filtro.enums.EstadoVenta;
import py.edu.facitec.filtro.repository.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class VentaService {

    private final VentaRepository ventaRepository;
    private final VentaDetalleRepository ventaDetalleRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;
    private final CajeroRepository cajeroRepository;
    private final CajaRepository cajaRepository;
    private final PaginadorService paginadorService;

    public VentaService(
            VentaRepository ventaRepository,
            VentaDetalleRepository ventaDetalleRepository,
            ProductoRepository productoRepository,
            ClienteRepository clienteRepository,
            VendedorRepository vendedorRepository,
            CajeroRepository cajeroRepository,
            CajaRepository cajaRepository,
            PaginadorService paginadorService
    ) {
        this.ventaRepository = ventaRepository;
        this.ventaDetalleRepository = ventaDetalleRepository;
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
        this.vendedorRepository = vendedorRepository;
        this.cajeroRepository = cajeroRepository;
        this.cajaRepository = cajaRepository;
        this.paginadorService = paginadorService;
    }

    /* ==================== Helper para buscar entidades ==================== */
    private Cliente getCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    private Vendedor getVendedor(Long id) {
        return vendedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));
    }

    private Cajero getCajero(Long id) {
        return cajeroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cajero no encontrado"));
    }

    private Caja getCaja(Long id) {
        return cajaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Caja no encontrada"));
    }

    private Producto getProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    /* ==================== Ventas ==================== */
    public List<Venta> findAllVentas() {
        return ventaRepository.findAll();
    }

    public Venta findOneVenta(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }

    @Transactional
    public Venta createVenta(InputVenta inputVenta) {
        Cliente cliente = getCliente(inputVenta.getClienteId());
        Vendedor vendedor = getVendedor(inputVenta.getVendedorId());
        Cajero cajero = getCajero(inputVenta.getCajeroId());
        Caja caja = getCaja(inputVenta.getCajaId());

        // 1️⃣ Guardar primero la venta
        Venta venta = Venta.builder()
                .codigoVenta(UUID.randomUUID().toString().substring(0, 8))
                .fechaVenta(new Date())
                .tipoVenta(inputVenta.getTipoVenta())
                .estadoVenta(EstadoVenta.PENDIENTE)
                .cliente(cliente)
                .vendedor(vendedor)
                .cajero(cajero)
                .caja(caja)
                .total(BigDecimal.ZERO)
                .build();
        Venta savedVenta = ventaRepository.save(venta);

        // 2️⃣ Crear los detalles de venta y calcular total
        BigDecimal totalVenta = BigDecimal.ZERO;
        for (InputVentaDetalle detalleDto : inputVenta.getDetalles()) {
            Producto producto = getProducto(detalleDto.getProductoId());
            if (producto.getStock() < detalleDto.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }
            producto.setStock(producto.getStock() - detalleDto.getCantidad());
            productoRepository.save(producto);

            BigDecimal descuento = detalleDto.getDescuento() != null ? detalleDto.getDescuento() : BigDecimal.ZERO;
            BigDecimal subtotal = producto.getPrecioVenta()
                    .multiply(BigDecimal.valueOf(detalleDto.getCantidad()))
                    .subtract(descuento);

            VentaDetalle detalle = VentaDetalle.builder()
                    .venta(savedVenta)
                    .producto(producto)
                    .cantidad(detalleDto.getCantidad())
                    .precioUnitario(producto.getPrecioVenta())
                    .descuento(descuento)
                    .subtotal(subtotal)
                    .build();
            ventaDetalleRepository.save(detalle);
            totalVenta = totalVenta.add(subtotal);
        }

        // 3️⃣ Actualizar total de la venta y caja
        savedVenta.setTotal(totalVenta);
        caja.setSaldoActual(caja.getSaldoActual().add(totalVenta));
        cajaRepository.save(caja);

        log.info("Venta creada: {}", savedVenta.getCodigoVenta());
        return ventaRepository.save(savedVenta);
    }

    @Transactional
    public Venta updateVenta(Long id, InputVenta inputVenta) {
        Venta venta = findOneVenta(id);

        if (venta.getEstadoVenta() == EstadoVenta.CANCELADA) {
            throw new RuntimeException("No se puede modificar una venta anulada.");
        }

        if (inputVenta.getClienteId() != null) venta.setCliente(getCliente(inputVenta.getClienteId()));
        if (inputVenta.getVendedorId() != null) venta.setVendedor(getVendedor(inputVenta.getVendedorId()));
        if (inputVenta.getCajeroId() != null) venta.setCajero(getCajero(inputVenta.getCajeroId()));
        if (inputVenta.getCajaId() != null) venta.setCaja(getCaja(inputVenta.getCajaId()));
        if (inputVenta.getTipoVenta() != null) venta.setTipoVenta(inputVenta.getTipoVenta());

        log.info("Venta {} actualizada.", venta.getCodigoVenta());
        return ventaRepository.save(venta);
    }

    public Venta updateVentaStatus(Long id, EstadoVenta estado) {
        Venta venta = findOneVenta(id);
        venta.setEstadoVenta(estado);
        log.info("Venta {} actualizada a estado {}", venta.getCodigoVenta(), estado);
        return ventaRepository.save(venta);
    }

    @Transactional
    public Venta deleteVenta(Long id) {
        Venta venta = findOneVenta(id);

        if (venta.getEstadoVenta() == EstadoVenta.CANCELADA) {
            throw new RuntimeException("La venta ya ha sido anulada.");
        }

        List<VentaDetalle> detalles = ventaDetalleRepository.findByVentaId(id);
        for (VentaDetalle detalle : detalles) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        }

        Caja caja = venta.getCaja();
        caja.setSaldoActual(caja.getSaldoActual().subtract(venta.getTotal()));
        cajaRepository.save(caja);

        venta.setEstadoVenta(EstadoVenta.CANCELADA);
        log.info("Venta {} anulada.", venta.getCodigoVenta());
        return ventaRepository.save(venta);
    }

    /* ==================== Ventas Paginated ==================== */
    public PaginadorDto<Venta> findVentasPaginated(int page, int size, String search) {
        return paginadorService.paginarConFiltro(
                (s, pageable) -> {
                    if (s == null || s.trim().isEmpty()) return ventaRepository.findAll(pageable);
                    return ventaRepository.findByCodigoVentaContainingIgnoreCase(s, pageable);
                },
                search, page, size
        );
    }

    /* ==================== VentaDetalle ==================== */
    @Transactional
    public VentaDetalle createVentaDetalle(Long ventaId, InputVentaDetalle detalleDto) {
        Venta venta = findOneVenta(ventaId);
        validarEstadoPendiente(venta);

        Producto producto = getProducto(detalleDto.getProductoId());
        actualizarStockProducto(producto, detalleDto.getCantidad(), true);

        BigDecimal descuento = detalleDto.getDescuento() != null ? detalleDto.getDescuento() : BigDecimal.ZERO;
        BigDecimal subtotal = producto.getPrecioVenta()
                .multiply(BigDecimal.valueOf(detalleDto.getCantidad()))
                .subtract(descuento);

        VentaDetalle detalle = VentaDetalle.builder()
                .venta(venta)
                .producto(producto)
                .cantidad(detalleDto.getCantidad())
                .precioUnitario(producto.getPrecioVenta())
                .descuento(descuento)
                .subtotal(subtotal)
                .build();
        VentaDetalle savedDetalle = ventaDetalleRepository.save(detalle);

        actualizarTotalesVenta(venta, subtotal);

        log.info("Detalle agregado a la venta {}.", venta.getCodigoVenta());
        return savedDetalle;
    }

    @Transactional
    public VentaDetalle updateVentaDetalle(Long ventaDetalleId, int nuevaCantidad, BigDecimal nuevoDescuento) {
        VentaDetalle detalle = ventaDetalleRepository.findById(ventaDetalleId)
                .orElseThrow(() -> new RuntimeException("Detalle de venta no encontrado"));
        Venta venta = detalle.getVenta();
        validarEstadoPendiente(venta);

        Producto producto = detalle.getProducto();
        int diferencia = nuevaCantidad - detalle.getCantidad();
        actualizarStockProducto(producto, diferencia, true);

        BigDecimal descuento = nuevoDescuento != null ? nuevoDescuento : BigDecimal.ZERO;
        BigDecimal nuevoSubtotal = producto.getPrecioVenta()
                .multiply(BigDecimal.valueOf(nuevaCantidad))
                .subtract(descuento);

        BigDecimal diferenciaSubtotal = nuevoSubtotal.subtract(detalle.getSubtotal());

        detalle.setCantidad(nuevaCantidad);
        detalle.setDescuento(descuento);
        detalle.setSubtotal(nuevoSubtotal);
        ventaDetalleRepository.save(detalle);

        actualizarTotalesVenta(venta, diferenciaSubtotal);

        log.info("Detalle {} actualizado en la venta {}.", detalle.getId(), venta.getCodigoVenta());
        return detalle;
    }

    @Transactional
    public VentaDetalle deleteVentaDetalle(Long ventaDetalleId) {
        VentaDetalle detalle = ventaDetalleRepository.findById(ventaDetalleId)
                .orElseThrow(() -> new RuntimeException("Detalle de venta no encontrado"));
        Venta venta = detalle.getVenta();
        validarEstadoPendiente(venta);

        Producto producto = detalle.getProducto();
        actualizarStockProducto(producto, detalle.getCantidad(), false);

        BigDecimal subtotalDetalle = detalle.getSubtotal();
        actualizarTotalesVenta(venta, subtotalDetalle.negate());

        ventaDetalleRepository.delete(detalle);
        log.info("Detalle {} eliminado de la venta {}.", detalle.getId(), venta.getCodigoVenta());
        return detalle;
    }

    /* ==================== Métodos Helper ==================== */
    private void validarEstadoPendiente(Venta venta) {
        if (venta.getEstadoVenta() != EstadoVenta.PENDIENTE) {
            throw new RuntimeException("Solo se pueden modificar ventas en estado PENDIENTE.");
        }
    }

    private void actualizarStockProducto(Producto producto, int cantidad, boolean restar) {
        int nuevoStock = restar ? producto.getStock() - cantidad : producto.getStock() + cantidad;
        if (nuevoStock < 0) throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
        producto.setStock(nuevoStock);
        productoRepository.save(producto);
    }

    private void actualizarTotalesVenta(Venta venta, BigDecimal diferenciaTotal) {
        venta.setTotal(venta.getTotal().add(diferenciaTotal));
        Caja caja = venta.getCaja();
        caja.setSaldoActual(caja.getSaldoActual().add(diferenciaTotal));
        cajaRepository.save(caja);
        ventaRepository.save(venta);
    }
}
