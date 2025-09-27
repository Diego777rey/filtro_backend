package py.edu.facitec.filtro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import py.edu.facitec.filtro.dto.AuthPayload;
import py.edu.facitec.filtro.dto.PaginadorDto;
import py.edu.facitec.filtro.dto.UsuarioInput;
import py.edu.facitec.filtro.entity.Usuario;
import py.edu.facitec.filtro.service.JwtService;
import py.edu.facitec.filtro.service.UsuarioService;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    public PaginadorDto<Usuario> findUsuariosPaginated(@Argument int page,
                                                       @Argument int size,
                                                       @Argument String search) {
        return usuarioService.findUsuariosPaginated(page, size, search);
    }
    @QueryMapping
   @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    public Usuario findUsuarioById(@Argument Long id) {
        return usuarioService.findOneUsuario(id);
    }

    @MutationMapping
    public Usuario crearUsuario(@Argument UsuarioInput input) {
        return usuarioService.saveUsuario(input);
    }

    @MutationMapping
   @PreAuthorize("hasRole('ADMINISTRADOR')")
    public Usuario actualizarUsuario(@Argument Long id, @Argument UsuarioInput input) {
        return usuarioService.updateUsuario(id, input);
    }

    @MutationMapping
   @PreAuthorize("hasRole('ADMINISTRADOR')")
    public Usuario eliminarUsuario(@Argument Long id) {
        return usuarioService.deleteUsuario(id);
    }

    // --- Login con JWT ---

    @MutationMapping
    public AuthPayload login(@Argument String nombre, @Argument String contrasenha) {
        return usuarioService.login(nombre, contrasenha)
                .map(usuario -> {
                    String token = jwtService.generateToken(usuario);
                    return new AuthPayload(usuario, token, null);
                })
                .orElseGet(() -> new AuthPayload(null, null, "Usuario o contraseña incorrectos"));
    }
}
