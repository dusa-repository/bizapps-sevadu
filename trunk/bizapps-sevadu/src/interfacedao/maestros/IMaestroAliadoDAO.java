package interfacedao.maestros;

import java.util.List;

import modelo.maestros.MaestroAliado;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IMaestroAliadoDAO extends JpaRepository<MaestroAliado, String> {

	@Query("select m from MaestroAliado m order by m.nombre asc")
	List<MaestroAliado> findAllOrderByNombreAsc();

	List<MaestroAliado> findByCodigoAliadoNotIn(List<String> listaAliados);

}
