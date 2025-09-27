package py.edu.facitec.filtro.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.edu.facitec.filtro.dto.UsuarioInput;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.entity.Persona;
import py.edu.facitec.filtro.entity.Rol;
import py.edu.facitec.filtro.entity.Usuario;
import py.edu.facitec.filtro.repository.PersonaRepository;
import py.edu.facitec.filtro.repository.UsuarioRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PaginadorService paginadorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // -------------------------------------------
    // Implementación de UserDetailsService
    // -------------------------------------------
    @Override
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByPersonaNombre(nombre)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + nombre));

        List<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getTipoPersona().name()))
                .collect(Collectors.toList());

        return new User(usuario.getPersona().getNombre(), usuario.getContrasenha(), authorities);
    }

    // -------------------------------------------
    // CRUD normal
    // -------------------------------------------
    public List<Usuario> findAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario findOneUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con id " + id + " no existe"));
    }

    @Transactional
    public Usuario saveUsuario(UsuarioInput dto) {
        if (dto == null) throw new IllegalArgumentException("El objeto Usuario no puede ser nulo");
        if (dto.getPersonaId() == null) throw new IllegalArgumentException("El ID de la persona es obligatorio");
        if (dto.getContrasenha() == null || dto.getContrasenha().trim().isEmpty())
            throw new IllegalArgumentException("La contraseña es obligatoria");

        // Traer la persona existente
        Persona persona = personaRepository.findById(dto.getPersonaId())
                .orElseThrow(() -> new RuntimeException("Persona con id " + dto.getPersonaId() + " no existe"));

        // Validar que no exista un usuario ya creado para esta persona
        if (usuarioRepository.findByPersonaId(persona.getId()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario para esta persona");
        }

        // Copiar los roles de la persona
        Set<Rol> roles = new HashSet<>(persona.getRoles());
        if (roles.isEmpty()) throw new IllegalArgumentException("La persona debe tener al menos un rol asignado");

        Usuario usuario = Usuario.builder()
                .persona(persona)
                .contrasenha(passwordEncoder.encode(dto.getContrasenha()))
                .roles(roles)
                .build();

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario updateUsuario(Long id, UsuarioInput dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con id " + id + " no existe"));

        if (dto.getContrasenha() != null && !dto.getContrasenha().isEmpty()) {
            usuario.setContrasenha(passwordEncoder.encode(dto.getContrasenha()));
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario deleteUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con id " + id + " no existe"));
        usuarioRepository.delete(usuario);
        return usuario;
    }

    public PaginadorDto<Usuario> findUsuariosPaginated(int page, int size, String search) {
        return paginadorService.<Usuario>paginarConFiltro(
                (s, pageable) -> {
                    if (s == null || s.trim().isEmpty()) {
                        return usuarioRepository.findAll(pageable);
                    }
                    return usuarioRepository.findByPersonaNombreContainingIgnoreCase(s, pageable);
                },
                search,
                page,
                size
        );
    }

    // -------------------------------------------
    // Métodos de utilidad
    // -------------------------------------------
    public Optional<Usuario> login(String nombre, String contrasenha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByPersonaNombre(nombre);
        if (usuarioOpt.isPresent() && passwordEncoder.matches(contrasenha, usuarioOpt.get().getContrasenha())) {
            return usuarioOpt;
        }
        return Optional.empty();
    }

    public boolean usuarioTieneRol(Long usuarioId, String rolNombre) {
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> usuario.getRoles().stream()
                        .anyMatch(rol -> rol.getTipoPersona().name().equals(rolNombre)))
                .orElse(false);
    }

    public List<String> obtenerRolesDeUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> usuario.getRoles().stream()
                        .map(rol -> rol.getTipoPersona().name())
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }
}
