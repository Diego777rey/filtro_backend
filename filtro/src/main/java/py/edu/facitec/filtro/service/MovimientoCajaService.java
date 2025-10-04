package py.edu.facitec.filtro.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.dto.InputMovimientoCaja;
import py.edu.facitec.filtro.entity.*;
import py.edu.facitec.filtro.enums.EstadoCaja;
import py.edu.facitec.filtro.enums.EstadoVenta;
import py.edu.facitec.filtro.repository.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MovimientoCajaService {

    @Autowired
    private MovimientoCajaRepository movimientoCajaRepository;

    @Autowired
    private CajaRepository cajaRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private PaginadorService paginadorService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // ------------------ Consultas ------------------

    public List<MovimientoCaja> findAllMovimientosCaja() {
        return movimientoCajaRepository.findAll();
    }

    public MovimientoCaja findOneMovimientoCaja(Long id) {
        return movimientoCajaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MovimientoCaja con id " + id + " no existe"));
    }

    public PaginadorDto<MovimientoCaja> findMovimientosCajaPaginated(int page, int size, String search) {
        return paginadorService.paginarConFiltro(
                (s, pageable) -> {
                    if (s == null || s.trim().isEmpty()) {
                        return movimientoCajaRepository.findAll(pageable);
                    }
                    return movimientoCajaRepository.findByDescripcionContainingIgnoreCase(s, pageable);
                },
                search,
                page,
                size
        );
    }

    // 🚀 Nuevo método: Ventas pendientes por caja
    public List<Venta> findVentasPendientesPorCaja(Long cajaId) {
        Caja caja = cajaRepository.findById(cajaId)
                .orElseThrow(() -> new RuntimeException("Caja no encontrada con id " + cajaId));

        return ventaRepository.findByCajaAndEstadoVenta(caja, EstadoVenta.PENDIENTE);
    }

    // ------------------ Movimientos manuales ------------------

    public MovimientoCaja saveMovimientoCaja(InputMovimientoCaja dto) {
        validarCamposObligatorios(dto);
        validarNegocio(dto);

        Caja caja = cajaRepository.findById(dto.getCajaId())
                .orElseThrow(() -> new RuntimeException("Caja no encontrada con id " + dto.getCajaId()));

        Venta venta = null;
        if (dto.getVentaId() != null) {
            venta = ventaRepository.findById(dto.getVentaId())
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada con id " + dto.getVentaId()));
        }

        MovimientoCaja movimientoCaja = MovimientoCaja.builder()
                .monto(dto.getMonto())
                .tipo(dto.getTipo())
                .fecha(parseFecha(dto.getFecha()))
                .descripcion(dto.getDescripcion())
                .caja(caja)
                .venta(venta)
                .build();

        MovimientoCaja saved = movimientoCajaRepository.save(movimientoCaja);
        log.info("MovimientoCaja creado manualmente: {}", saved.getMonto());
        return saved;
    }

    public MovimientoCaja updateMovimientoCaja(Long id, InputMovimientoCaja dto) {
        validarCamposObligatorios(dto);
        validarNegocio(dto);

        MovimientoCaja movimientoCaja = movimientoCajaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MovimientoCaja con id " + id + " no existe"));

        movimientoCaja.setMonto(dto.getMonto());
        movimientoCaja.setTipo(dto.getTipo());
        movimientoCaja.setFecha(parseFecha(dto.getFecha()));
        movimientoCaja.setDescripcion(dto.getDescripcion());

        Caja caja = cajaRepository.findById(dto.getCajaId())
                .orElseThrow(() -> new RuntimeException("Caja no encontrada con id " + dto.getCajaId()));
        movimientoCaja.setCaja(caja);

        Venta venta = null;
        if (dto.getVentaId() != null) {
            venta = ventaRepository.findById(dto.getVentaId())
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada con id " + dto.getVentaId()));
        }
        movimientoCaja.setVenta(venta);

        MovimientoCaja updated = movimientoCajaRepository.save(movimientoCaja);
        log.info("MovimientoCaja actualizado manualmente: {}", updated.getMonto());
        return updated;
    }

    public MovimientoCaja deleteMovimientoCaja(Long id) {
        MovimientoCaja movimientoCaja = movimientoCajaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MovimientoCaja con id " + id + " no existe"));
        movimientoCajaRepository.delete(movimientoCaja);
        log.info("MovimientoCaja eliminado: {}", movimientoCaja.getMonto());
        return movimientoCaja;
    }

    // ------------------ Lógica de aceptar/cancelar venta ------------------

    public MovimientoCaja aceptarVenta(Long ventaId) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con id " + ventaId));

        if (venta.getEstadoVenta() != EstadoVenta.PENDIENTE) {
            throw new RuntimeException("La venta ya fue procesada");
        }

        venta.setEstadoVenta(EstadoVenta.COMPLETADA);
        ventaRepository.save(venta);

        Caja caja = venta.getCaja();
        if (caja == null || caja.getEstadoCaja() == EstadoCaja.CERRADA) {
            throw new RuntimeException("La caja está cerrada o no asignada");
        }

        // Actualizar saldo de la caja
        caja.setSaldoActual(caja.getSaldoActual().add(venta.getTotal()));
        cajaRepository.save(caja);

        MovimientoCaja movimiento = MovimientoCaja.builder()
                .monto(venta.getTotal())
                .tipo("INGRESO")
                .fecha(new Date())
                .descripcion("Venta confirmada: " + venta.getCodigoVenta())
                .caja(caja)
                .venta(venta)
                .build();

        MovimientoCaja saved = movimientoCajaRepository.save(movimiento);
        log.info("Venta aceptada, MovimientoCaja generado: {}", saved.getMonto());
        return saved;
    }

    public Venta cancelarVenta(Long ventaId) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con id " + ventaId));

        if (venta.getEstadoVenta() != EstadoVenta.PENDIENTE) {
            throw new RuntimeException("La venta ya fue procesada");
        }

        venta.setEstadoVenta(EstadoVenta.CANCELADA);
        Venta saved = ventaRepository.save(venta);
        log.info("Venta cancelada: {}", saved.getCodigoVenta());
        return saved;
    }

    // ------------------ Validaciones ------------------

    private void validarCamposObligatorios(InputMovimientoCaja dto) {
        if (dto.getMonto() == null || dto.getMonto().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        if (dto.getTipo() == null || dto.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo es obligatorio (INGRESO/EGRESO)");
        }
        if (dto.getFecha() == null || dto.getFecha().trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        if (dto.getCajaId() == null) {
            throw new IllegalArgumentException("El ID de la caja es obligatorio");
        }
    }

    private void validarNegocio(InputMovimientoCaja dto) {
        // Validar que el tipo sea INGRESO o EGRESO
        if (!"INGRESO".equalsIgnoreCase(dto.getTipo()) && !"EGRESO".equalsIgnoreCase(dto.getTipo())) {
            throw new IllegalArgumentException("El tipo debe ser INGRESO o EGRESO");
        }

        // Validar que la caja exista y esté abierta
        Caja caja = cajaRepository.findById(dto.getCajaId())
                .orElseThrow(() -> new RuntimeException("Caja no encontrada"));

        if (caja.getEstadoCaja() == EstadoCaja.CERRADA) {
            throw new RuntimeException("No se pueden realizar movimientos en una caja cerrada");
        }
    }

    // ------------------ Métodos auxiliares ------------------

    private Date parseFecha(String fechaStr) {
        try {
            return dateFormat.parse(fechaStr);
        } catch (ParseException e) {
            throw new RuntimeException("Formato de fecha inválido: " + fechaStr + ". Use formato yyyy-MM-dd");
        }
    }
}
