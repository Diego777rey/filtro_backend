package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.*;
import py.edu.facitec.filtro.enums.EstadoPersona;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "personas")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private String email;
    private String documento;
    @Enumerated(EnumType.STRING)
    private EstadoPersona estadoPersona;
    private Date fechaNacimiento;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "persona_roles",
            joinColumns = @JoinColumn(name = "persona_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    @Singular("rol")
    private Set<Rol> roles = new HashSet<>();
}
