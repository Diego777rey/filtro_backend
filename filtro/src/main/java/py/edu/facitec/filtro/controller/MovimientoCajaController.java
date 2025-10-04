package py.edu.facitec.filtro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import py.edu.facitec.filtro.dto.InputMovimientoCaja;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.*;
import py.edu.facitec.filtro.service.CajaService;
import py.edu.facitec.filtro.service.MovimientoCajaService;
import py.edu.facitec.filtro.service.VentaService;

import java.util.List;

@Controller
public class MovimientoCajaController {

    @Autowired
    private MovimientoCajaService movimientoCajaService;
    @Autowired
    private CajaService cajaService;
    @Autowired
    private VentaService ventaService;

    // ------------------ Consultas ------------------

    @QueryMapping
    public List<MovimientoCaja> findAllMovimientosCaja() {
        return movimientoCajaService.findAllMovimientosCaja();
    }

    @QueryMapping
    public MovimientoCaja findMovinmientoCajaById(@Argument("movimientoId") Long movimientoId) {
        return movimientoCajaService.findOneMovimientoCaja(movimientoId);
    }

    @QueryMapping
    public PaginadorDto<MovimientoCaja> findMovimientosCajaPaginated(
            @Argument("page") int page,
            @Argument("size") int size,
            @Argument("search") String search
    ) {
        return movimientoCajaService.findMovimientosCajaPaginated(page, size, search);
    }

    // 🚀 Nueva Query: Ventas pendientes por caja
    @QueryMapping
    public List<Venta> findVentasPendientesPorCaja(@Argument("cajaId") Long cajaId) {
        return movimientoCajaService.findVentasPendientesPorCaja(cajaId);
    }

    // ------------------ Mutaciones manuales ------------------

    @MutationMapping
    public MovimientoCaja createMovimientoCaja(@Argument("inputMovimientoCaja") InputMovimientoCaja inputMovimientoCaja) {
        return movimientoCajaService.saveMovimientoCaja(inputMovimientoCaja);
    }

    @MutationMapping
    public MovimientoCaja updateMovimientoCaja(@Argument("id") Long id,
                                               @Argument("inputMovimientoCaja") InputMovimientoCaja inputMovimientoCaja) {
        return movimientoCajaService.updateMovimientoCaja(id, inputMovimientoCaja);
    }

    @MutationMapping
    public MovimientoCaja deleteMovimientoCaja(@Argument("id") Long id) {
        return movimientoCajaService.deleteMovimientoCaja(id);
    }

    // ------------------ Mutaciones específicas de ventas ------------------

    @MutationMapping
    public MovimientoCaja aceptarVenta(@Argument("ventaId") Long ventaId) {
        return movimientoCajaService.aceptarVenta(ventaId);
    }

    @MutationMapping
    public Venta cancelarVenta(@Argument("ventaId") Long ventaId) {
        return movimientoCajaService.cancelarVenta(ventaId);
    }
}
