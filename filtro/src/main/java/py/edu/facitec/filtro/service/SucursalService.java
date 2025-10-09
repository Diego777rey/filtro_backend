package py.edu.facitec.filtro.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Sucursal;
import py.edu.facitec.filtro.repository.SucursalRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
public class SucursalService {
    @Autowired
    private PaginadorService paginadorService;
    @Autowired
    private SucursalRepository sucursalRepository;
    public List<Sucursal> findAllSucursales() {
        return sucursalRepository.findAll();
    }
    public Sucursal findOneSucursal(Long id) {
        return sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal con id " + id + " no existe"));
    }
    public Flux<Sucursal> findAllSucursalesFlux() {
        return Flux.fromIterable(findAllSucursales())
                .delayElements(Duration.ofSeconds(1)) // emite uno cada segundo
                .take(10);
    }
    public Mono<Sucursal> findOneMono(Long id) {
        return Mono.justOrEmpty(sucursalRepository.findById(id))
                .switchIfEmpty(Mono.error(new RuntimeException("Sucursal con id " + id + " no existe")));
    }
    public Sucursal createSucursal(Sucursal dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El objeto Sucursal no puede ser nulo");
        }
        validarCamposObligatorios(dto);

        Sucursal sucursal = Sucursal.builder()
                .nombre(dto.getNombre())
                .pais(dto.getPais())
                .departamento(dto.getDepartamento())
                .ciudad(dto.getCiudad())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .build();

        return sucursalRepository.save(sucursal);
    }
    public Sucursal updateSucursal(Long id, Sucursal dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El objeto Sucursal no puede ser nulo");
        }
        validarCamposObligatorios(dto);

        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal con id " + id + " no existe"));

        sucursal.setNombre(dto.getNombre());
                sucursal.setPais(dto.getPais());
                sucursal.setDepartamento(dto.getDepartamento());
                sucursal.setCiudad(dto.getCiudad());
               sucursal.setDireccion(dto.getDireccion());
                sucursal.setTelefono(dto.getTelefono());

        return sucursalRepository.save(sucursal);
    }
    public Sucursal deleteSucursal(Long id) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal con id " + id + " no existe"));

        sucursalRepository.delete(sucursal);
        return sucursal;
    }
    public PaginadorDto<Sucursal> findSucursalesPaginated(int page, int size, String search) {
        return paginadorService.paginarConFiltro(
                (s, pageable) -> {
                    if (s == null || s.trim().isEmpty()) {
                        return sucursalRepository.findAll(pageable);
                    }
                    return sucursalRepository.findByNombreContainingIgnoreCase(s, pageable);
                },
                search,
                page,
                size
        );
    }


    private void validarCamposObligatorios(Sucursal dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
    }
}