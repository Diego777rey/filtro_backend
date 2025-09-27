package py.edu.facitec.filtro.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Caja;
import py.edu.facitec.filtro.repository.CajaRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
public class CajaService {
    @Autowired
    private PaginadorService paginadorService;
    @Autowired
    private CajaRepository cajaRepository;

    public List<Caja> findAllCajas() {
        return cajaRepository.findAll();
    }

    public Caja findOneCaja(Long id) {
        return cajaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Caja con id " + id + " no existe"));
    }

    public Flux<Caja> findAllCajasFlux() {
        return Flux.fromIterable(findAllCajas())
                .delayElements(Duration.ofSeconds(1)) // emite uno cada segundo
                .take(10);
    }

    public Mono<Caja> findOneMono(Long id) {
        return Mono.justOrEmpty(cajaRepository.findById(id))
                .switchIfEmpty(Mono.error(new RuntimeException("Caja con id " + id + " no existe")));
    }

    public Caja saveCaja(Caja dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El objeto Caja no puede ser nulo");
        }
        validarCamposObligatorios(dto);

        Caja caja = Caja.builder()
                .codigoCaja(dto.getCodigoCaja())
                .descripcion(dto.getDescripcion())
                .ubicacion(dto.getUbicacion())
                .estadoCaja(dto.getEstadoCaja())
                .saldoInicial(dto.getSaldoInicial())
                .saldoActual(dto.getSaldoActual())
                .build();

        return cajaRepository.save(caja);
    }

    public Caja updateCaja(Long id, Caja dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El objeto Caja no puede ser nulo");
        }
        validarCamposObligatorios(dto);

        Caja caja = cajaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Caja con id " + id + " no existe"));
        caja.setCodigoCaja(dto.getCodigoCaja());
        caja.setDescripcion(dto.getDescripcion());
        caja.setUbicacion(dto.getUbicacion());
        caja.setEstadoCaja(dto.getEstadoCaja());
        caja.setSaldoInicial(dto.getSaldoInicial());
        caja.setSaldoActual(dto.getSaldoActual());

        return cajaRepository.save(caja);
    }

    public Caja deleteCaja(Long id) {
        Caja Caja = cajaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Caja con id " + id + " no existe"));

        cajaRepository.delete(Caja);
        return Caja;
    }

    public PaginadorDto<Caja> findCajasPaginated(int page, int size, String search) {
        return paginadorService.paginarConFiltro(
                (s, pageable) -> {
                    if (s == null || s.trim().isEmpty()) {
                        return cajaRepository.findAll(pageable);
                    }
                    return cajaRepository.findByDescripcionContainingIgnoreCase(s, pageable);
                },
                search,
                page,
                size
        );
    }


    private void validarCamposObligatorios(Caja dto) {
    }
}