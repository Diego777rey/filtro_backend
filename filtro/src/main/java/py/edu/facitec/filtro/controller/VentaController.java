package py.edu.facitec.filtro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import py.edu.facitec.filtro.dto.InputVenta;
import py.edu.facitec.filtro.dto.InputVentaDetalle;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.*;
import py.edu.facitec.filtro.enums.EstadoVenta;
import py.edu.facitec.filtro.service.*;

import java.util.List;

@Controller
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VendedorService vendedorService;

    @Autowired
    private CajeroService cajeroService;

    @Autowired
    private CajaService cajaService;

    @QueryMapping
    public List<Venta> findAllVentas() {
        return ventaService.findAllVentas();
    }

    @QueryMapping
    public Venta findVentaById(Long ventaId) {
        return ventaService.findOneVenta(ventaId);
    }

    @QueryMapping
    public PaginadorDto<Venta> findVentasPaginated(
            @Argument int page, @Argument int size, @Argument String search) {
        return ventaService.findVentasPaginated(page, size, search);
    }

    @MutationMapping
    public Venta createVenta(@Argument("input") InputVenta inputVenta) {
        return ventaService.createVenta(inputVenta);
    }

    @MutationMapping
    public Venta updateVenta(Long ventaId, @Argument("input") InputVenta inputVenta) {
        return ventaService.updateVenta(ventaId, inputVenta);
    }

    @MutationMapping
    public Venta updateVentaStatus(Long ventaId, @Argument("estado") EstadoVenta estado) {
        return ventaService.updateVentaStatus(ventaId, estado);
    }

    @MutationMapping
    public Venta deleteVenta(Long ventaId) {
        return ventaService.deleteVenta(ventaId);
    }

    @MutationMapping
    public VentaDetalle createVentaDetalle(@Argument("ventaId") Long ventaId, @Argument("detalle") InputVentaDetalle detalle) {
        return ventaService.createVentaDetalle(ventaId, detalle);
    }

    @MutationMapping
    public VentaDetalle updateVentaDetalle(@Argument("ventaDetalleId") Long ventaDetalleId, @Argument("cantidad") int cantidad) {
        return ventaService.updateVentaDetalle(ventaDetalleId, cantidad);
    }

    @MutationMapping
    public VentaDetalle deleteVentaDetalle(@Argument("ventaDetalleId") Long ventaDetalleId) {
        return ventaService.deleteVentaDetalle(ventaDetalleId);
    }

    @SchemaMapping(typeName = "Venta", field = "cliente")
    public Cliente getCliente(Venta venta) {
        return clienteService.findOneCliente(venta.getCliente().getId());
    }

    @SchemaMapping(typeName = "Venta", field = "vendedor")
    public Vendedor getVendedor(Venta venta) {
        return vendedorService.findOneVendedor(venta.getVendedor().getId());
    }

    @SchemaMapping(typeName = "Venta", field = "cajero")
    public Cajero getCajero(Venta venta) {
        return cajeroService.findOneCajero(venta.getCajero().getId());
    }

    @SchemaMapping(typeName = "Venta", field = "caja")
    public Caja getCaja(Venta venta) {
        return cajaService.findOneCaja(venta.getCaja().getId());
    }
}