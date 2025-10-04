package py.edu.facitec.filtro.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import org.springframework.stereotype.Service;
import py.edu.facitec.filtro.entity.Venta;
import py.edu.facitec.filtro.entity.VentaDetalle;
import py.edu.facitec.filtro.repository.VentaDetalleRepository;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JasperTicketService {

    private final VentaDetalleRepository ventaDetalleRepository;

    public JasperTicketService(VentaDetalleRepository ventaDetalleRepository) {
        this.ventaDetalleRepository = ventaDetalleRepository;
    }

    public void imprimirTicket(Venta venta) {
        try {
            // 1️⃣ Compilar JRXML
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/reports/ticket_venta.jrxml")
            );

            // 2️⃣ Parámetros generales
            Map<String, Object> params = new HashMap<>();
            params.put("codigoVenta", venta.getCodigoVenta());
            params.put("fechaVenta", venta.getFechaVenta().toString());
            params.put("cliente", venta.getCliente() != null ? venta.getCliente().getPersona().getNombre() : "Consumidor Final");
            params.put("total", venta.getTotal());

            // 3️⃣ Obtener detalles desde repositorio
            List<VentaDetalle> detalles = ventaDetalleRepository.findByVentaId(venta.getId());

            // 4️⃣ Convertir detalles a Map si el reporte espera campos específicos
            List<Map<String, Object>> detallesMap = detalles.stream().map(det -> {
                Map<String, Object> map = new HashMap<>();
                map.put("producto", det.getProducto().getNombre());
                map.put("cantidad", det.getCantidad());
                map.put("precioUnitario", det.getPrecioUnitario());
                map.put("descuento", det.getDescuento());
                map.put("subtotal", det.getSubtotal());
                return map;
            }).collect(Collectors.toList());

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(detallesMap);

            // 5️⃣ Llenar reporte
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            // 6️⃣ Buscar impresora USB
            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
            PrintService impresora = Arrays.stream(services)
                    .filter(p -> p.getName().contains("acer_ticket")) // ajusta el nombre de tu impresora
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Impresora USB no encontrada"));

            // 7️⃣ Exportar a impresora
            JRPrintServiceExporter exporter = new JRPrintServiceExporter();
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, impresora);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
            exporter.setParameter(JRPrintServiceExporterParameter.JASPER_PRINT, jasperPrint);

            exporter.exportReport();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error imprimiendo ticket: " + e.getMessage(), e);
        }
    }
}
