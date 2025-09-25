package py.edu.facitec.filtro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "vendedor")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendedor {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String codigoVendedor;
        private Double comision;
        private String sucursal;
       /* @OneToOne
        @JoinColumn(name = "persona_id")*/
        @ManyToOne
        private Persona persona;
    }