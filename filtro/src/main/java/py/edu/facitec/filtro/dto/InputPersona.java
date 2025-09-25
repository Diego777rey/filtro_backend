package py.edu.facitec.filtro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import py.edu.facitec.filtro.enums.EstadoPersona;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputPersona {
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private String email;
    private String documento;
    List<Long> rolIds; // Para múltiples roles
    private EstadoPersona estadoPersona;
    private String fechaNacimiento;
}
