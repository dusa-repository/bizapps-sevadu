package interfacedao.maestros;

import java.util.Date;
import java.util.List;

import modelo.maestros.Existencia;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroProducto;
import modelo.pk.ExistenciaPK;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IExistenciaDAO extends JpaRepository<Existencia, ExistenciaPK> {

	List<Existencia> findByIdMaestroAliadoIn(List<MaestroAliado> eliminarLista);

	List<Existencia> findByIdMaestroAliado(MaestroAliado aliado);

	List<Existencia> findByIdMaestroProdIn(List<MaestroProducto> eliminarLista);

	List<Existencia> findByIdMaestroProdCodigoProductoDusa(String clave);

	List<Existencia> findByIdMaestroAliadoCodigoAliadoAndFechaAuditoriaBetween(
			String idAliado, Date desde, Date hasta);

}
