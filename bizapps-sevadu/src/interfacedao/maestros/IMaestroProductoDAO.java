package interfacedao.maestros;

import java.util.List;

import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroMarca;
import modelo.maestros.MaestroProducto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IMaestroProductoDAO extends
		JpaRepository<MaestroProducto, String> {

	@Query("select p from MaestroProducto p order by p.codigoProductoDusa asc")
	List<MaestroProducto> findAllOrderByCodigoProductoDusaAsc();

	List<MaestroProducto> findByCodigoProductoDusaNotInOrderByCodigoProductoDusaAsc(
			List<String> ids);

	List<MaestroProducto> findByMaestroAliadoIn(
			List<MaestroAliado> eliminarLista);

	List<MaestroProducto> findByMaestroAliado(MaestroAliado aliado);

	List<MaestroProducto> findByMaestroMarcaIn(List<MaestroMarca> eliminarLista);

	List<MaestroProducto> findByMaestroMarcaMarcaDusa(String value);

	List<MaestroProducto> findByMaestroMarcaMarcaDusaIn(List<String> ids);

}
