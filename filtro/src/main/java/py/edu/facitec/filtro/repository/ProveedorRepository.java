package py.edu.facitec.filtro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import py.edu.facitec.filtro.entity.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    // Buscar proveedores por nombre o razón social
    Page<Proveedor> findByRazonSocialContainingIgnoreCase(String search, Pageable pageable);

    // También podrías tener más filtros si quieres
    Page<Proveedor> findByRucContainingIgnoreCase(String search, Pageable pageable);
}
