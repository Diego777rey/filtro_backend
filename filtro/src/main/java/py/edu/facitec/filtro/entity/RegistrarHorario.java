package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import py.edu.facitec.filtro.enums.Horarios;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "registros_horario")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrarHorario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaHora;
    @Column(nullable = false)
    private String turno;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Horarios horarios;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", nullable = false, foreignKey = @ForeignKey(name = "fk_registro_vendedor"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Persona persona;
}
