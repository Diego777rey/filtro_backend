/*package py.edu.facitec.filtro.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import py.edu.facitec.filtro.entity.Rol;
import py.edu.facitec.filtro.enums.TipoPersona;
import py.edu.facitec.filtro.repository.RolRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;

    public DataInitializer(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        for (TipoPersona tipo : TipoPersona.values()) {
            // Solo insertar si no existe
            rolRepository.findByTipoPersona(tipo)
                    .orElseGet(() -> {
                        Rol rol = Rol.builder().tipoPersona(tipo).build();
                        return rolRepository.save(rol);
                    });
        }
    }
}
*/