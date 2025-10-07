package py.edu.facitec.filtro.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import py.edu.facitec.filtro.entity.Venta;
import py.edu.facitec.filtro.entity.VentaDetalle;
import py.edu.facitec.filtro.repository.VentaDetalleRepository;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.io.InputStream;
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
            // 1️⃣ Cargar y compilar JRXML
            InputStream reportStream = getClass().getResourceAsStream("/reportes/ticket_venta.jrxml");
            if (reportStream == null) {
                throw new RuntimeException("No se encontró el archivo JRXML");
            }
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // 2️⃣ Parámetros del ticket
            Map<String, Object> params = new HashMap<>();
            params.put("codigoVenta", venta.getCodigoVenta());
            params.put("fechaVenta", venta.getFechaVenta().toString());
            params.put("cliente", venta.getCliente() != null ? venta.getCliente().getPersona().getNombre() : "Consumidor Final");
            params.put("total", venta.getTotal());

            // 3️⃣ Detalles de venta
            List<VentaDetalle> detalles = ventaDetalleRepository.findByVentaId(venta.getId());
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

            // 4️⃣ Llenar reporte
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            // 5️⃣ Buscar impresora por nombre
            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
            PrintService impresora = Arrays.stream(services)
                    .filter(p -> p.getName().contains("acer_ticket")) // Ajustar al nombre real
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Impresora USB no encontrada"));

            System.out.println("Impresora encontrada: " + impresora.getName());

            // 6️⃣ Imprimir directamente como Java2D
            JasperPrintManager.printReport(jasperPrint, false); // false = no mostrar diálogo de impresión

            System.out.println("Ticket enviado a impresora correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error imprimiendo ticket: " + e.getMessage(), e);
        }
    }
}
