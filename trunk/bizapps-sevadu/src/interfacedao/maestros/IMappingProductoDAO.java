package interfacedao.maestros;

import java.util.List;

import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroProducto;
import modelo.maestros.MappingProducto;
import modelo.pk.MappingProductoPK;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IMappingProductoDAO extends JpaRepository<MappingProducto, MappingProductoPK> {

	List<MappingProducto> findByIdMaestroAliadoAndEstadoMapeo(MaestroAliado aliado, int a);

	List<MappingProducto> findByIdMaestroProductoNotInAndIdMaestroAliado(
			List<MaestroProducto> productos, MaestroAliado aliado);

	List<MappingProducto> findByIdMaestroAliadoIn(
			List<MaestroAliado> eliminarLista);

	List<MappingProducto> findByIdMaestroProductoIn(
			List<MaestroProducto> eliminarLista);

	List<MappingProducto> findByIdMaestroProductoCodigoProductoDusa(String clave);

//	List<MappingProducto> findByIdMaestroAliadoAndCodigoProductoCliente(
//			MaestroAliado aliado, String idProducto);

	List<MappingProducto> findByIdMaestroAliadoAndCodigoProductoClienteAndEstadoMapeo(
			MaestroAliado aliado, String idProducto, int i);

}
