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
public class UsuarioInput {
    private String contrasenha;
    private List<Long> roleIds; // Lista de IDs de roles
    private Long personaId;
}