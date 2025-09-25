package py.edu.facitec.filtro.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.edu.facitec.filtro.enums.Horarios;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputRegistrarHorario {
    private LocalDateTime fechaHora;
    private String turno;
    private Horarios horarios;
    private Long personaId;
}
