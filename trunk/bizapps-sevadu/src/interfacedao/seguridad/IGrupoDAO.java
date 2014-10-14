package interfacedao.seguridad;

import java.util.List;

import modelo.seguridad.Grupo;
import modelo.seguridad.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IGrupoDAO extends JpaRepository<Grupo, Long> {
	
	public List<Grupo> findByUsuarios(Usuario usuario);

	public List<Grupo> findByEstadoTrue();

	public List<Grupo> findByIdGrupoNotInAndEstadoTrue(List<Long> ids);

	public Grupo findByNombre(String nombreGrupo);

	public List<Grupo> findByNombreStartingWithAllIgnoreCase(String valor);

	@Query("Select g from Grupo g order by g.idGrupo asc")
	public List<Grupo> findAllOrderById();

	public List<Grupo> findByUsuariosOrderByNombreAsc(Usuario u);
}
