package interfacedao.maestros;

import java.util.List;

import modelo.maestros.MaestroMarca;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IMaestroMarcaDAO extends JpaRepository<MaestroMarca, Integer> {

	@Query("select m from MaestroMarca m order by m.marcaDusa asc")
	List<MaestroMarca> findAllOrderByMarcaDusaAsc();

	MaestroMarca findByMarcaDusa(String value);

	List<MaestroMarca> findByFiltroTermometroTrueOrderByOrdenAsc();

}
