package py.edu.facitec.filtro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import py.edu.facitec.filtro.dto.InputPersona;
import py.edu.facitec.filtro.entity.Persona;
import py.edu.facitec.filtro.service.PersonaService;

import java.util.List;

@Controller
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    // ---------------- QUERIES ----------------

    @QueryMapping
    public Persona findPersonaById(@Argument("personaId") Long id) {
        return personaService.findPersonaById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
    }


    @QueryMapping
    public List<Persona> findAllPersonas() {
        return personaService.findAllPersonas();
    }

    @QueryMapping
    public List<Persona> findPersonasByNombre(@Argument String nombre) {
        return personaService.findPersonasByNombre(nombre);
    }

    // ---------------- MUTATIONS ----------------

    @MutationMapping
    public Persona createPersona(@Argument("inputPersona") InputPersona inputPersona) {
        return personaService.createPersona(inputPersona);
    }

    @MutationMapping
    public Persona updatePersona(@Argument Long id, @Argument("inputPersona") InputPersona inputPersona) {
        return personaService.updatePersona(id, inputPersona);
    }

    @MutationMapping
    public Persona deletePersona(@Argument Long id) {
        return personaService.deletePersona(id);
    }
}
