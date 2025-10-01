package py.edu.facitec.filtro.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.edu.facitec.filtro.entity.Persona;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputProveedor {
    private String ruc;
    private String razonSocial;
    private String rubro;
    private String telefono;
    private String email;
    private String observaciones;
}
