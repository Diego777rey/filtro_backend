package py.edu.facitec.filtro.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.edu.facitec.filtro.enums.Horarios;
import py.edu.facitec.filtro.enums.Turno;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputRegistrarHorario {
    private String fechaHora;
    private Turno turno;
    private Horarios horarios;
    private Long personaId;
}