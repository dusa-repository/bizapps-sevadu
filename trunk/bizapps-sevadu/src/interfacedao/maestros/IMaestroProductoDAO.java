package interfacedao.maestros;

import java.util.List;

import modelo.maestros.MaestroProducto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IMaestroProductoDAO extends
		JpaRepository<MaestroProducto, String> {

	@Query("select p from MaestroProducto p order by p.codigoProductoDusa asc")
	List<MaestroProducto> findAllOrderByCodigoProductoDusaAsc();

	List<MaestroProducto> findByCodigoProductoDusaNotInOrderByCodigoProductoDusaAsc(
			List<String> ids);

}
