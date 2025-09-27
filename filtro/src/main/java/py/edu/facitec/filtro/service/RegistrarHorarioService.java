/*package py.edu.facitec.filtro.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.edu.facitec.filtro.entity.RegistrarHorario;
import py.edu.facitec.filtro.entity.Vendedor;
import py.edu.facitec.filtro.enums.Horarios;
import py.edu.facitec.filtro.repository.RegistrarHorarioRepository;
import py.edu.facitec.filtro.repository.VendedorRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RegistrarHorarioService {

    private final RegistrarHorarioRepository registrarHorarioRepository;
    private final VendedorRepository vendedorRepository;

    public RegistrarHorarioService(RegistrarHorarioRepository registrarHorarioRepository,
                                   VendedorRepository vendedorRepository) {
        this.registrarHorarioRepository = registrarHorarioRepository;
        this.vendedorRepository = vendedorRepository;
    }

    // Crear registro
    public RegistrarHorario createRegistrarHorario(LocalDateTime fechaHora, Horarios horario, Integer vendedorId) {
        Vendedor vendedor = vendedorRepository.findById(vendedorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendedor no encontrado"));

        RegistrarHorario registrarHorario = RegistrarHorario.builder()
                .fechaHora(fechaHora)
                .horarios(horario)
                .vendedor(vendedor)
                .build();

        return registrarHorarioRepository.save(registrarHorario);
    }

    // Actualizar registro
    public RegistrarHorario updateRegistrarHorario(Long id, LocalDateTime fechaHora, Horarios horario, Integer vendedorId) {
        RegistrarHorario registro = registrarHorarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro no encontrado"));

        Vendedor vendedor = vendedorRepository.findById(vendedorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendedor no encontrado"));

        registro.setFechaHora(fechaHora);
        registro.setHorarios(horario);
        registro.setVendedor(vendedor);

        return registrarHorarioRepository.save(registro);
    }

    // Eliminar registro
    public RegistrarHorario deleteRegistrarHorario(Long id) {
        RegistrarHorario registro = registrarHorarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro no encontrado"));

        registrarHorarioRepository.delete(registro);
        return registro;
    }

    // Buscar por ID
    public Optional<RegistrarHorario> findRegistrarHorarioById(Long id) {
        return registrarHorarioRepository.findById(id);
    }

    // Listar todos
    public List<RegistrarHorario> findAllRegistrarHorarios() {
        return registrarHorarioRepository.findAll();
    }

    // Listar por vendedor
    public List<RegistrarHorario> findByVendedorId(Long vendedorId) {
        return registrarHorarioRepository.findByVendedorId(vendedorId);
    }
}*/