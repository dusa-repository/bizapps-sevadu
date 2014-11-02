package interfacedao.maestros;

import java.util.List;

import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroProducto;
import modelo.maestros.MappingProducto;
import modelo.pk.MappingProductoPK;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IMappingProductoDAO extends JpaRepository<MappingProducto, MappingProductoPK> {

	List<MappingProducto> findByIdMaestroAliado(MaestroAliado aliado);

	List<MappingProducto> findByIdMaestroProductoNotInAndIdMaestroAliado(
			List<MaestroProducto> productos, MaestroAliado aliado);

}
