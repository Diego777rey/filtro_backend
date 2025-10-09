package py.edu.facitec.filtro.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.edu.facitec.filtro.enums.CategoriaEstado;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputCategoria {
    private String nombre;
    private CategoriaEstado categoriaEstado;
    private List<Long> productosIds;
}
