package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.edu.facitec.filtro.enums.TipoPersona;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "usuarios")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String contrasenha;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_rol",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    @Builder.Default
    private Set<Rol> roles = new HashSet<>();

    @ManyToOne
    private Persona persona;

    // Método helper para verificar roles
    public boolean tieneRol(TipoPersona tipoPersona) {
        return roles.stream()
                .anyMatch(rol -> rol.getTipoPersona() == tipoPersona);
    }
}