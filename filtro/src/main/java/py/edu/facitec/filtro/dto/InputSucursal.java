package py.edu.facitec.filtro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputSucursal {
        private String nombre;
        private String direccion;
        private String telefono;
        private List<Long> transferenciasOrigenIds;
        private List<Long> transferenciasDestinoIds;
}