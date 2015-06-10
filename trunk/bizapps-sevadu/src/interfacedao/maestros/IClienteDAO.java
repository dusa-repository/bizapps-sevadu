package interfacedao.maestros;

import java.util.Date;
import java.util.List;

import modelo.maestros.Cliente;
import modelo.maestros.MaestroAliado;
import modelo.maestros.TipoCliente;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClienteDAO extends JpaRepository<Cliente, String> {

	Cliente findByCodigoClienteAndMaestroAliado(String idCliente,
			MaestroAliado aliado);

	List<Cliente> findByMaestroAliado(MaestroAliado aliado, Sort o);

	List<Cliente> findByMaestroAliadoIn(List<MaestroAliado> eliminarLista);

	List<Cliente> findByTipoClienteIn(List<TipoCliente> eliminarLista);

	List<Cliente> findByTipoClienteCodigo(String id);

	List<Cliente> findByMaestroAliadoCodigoAliado(String aliado);

	List<Cliente> findByMaestroAliadoCodigoAliadoAndFechaAuditoriaBetween(
			String idAliado, Date desde, Date hasta);

	List<Cliente> findByMaestroAliadoCodigoAliado(String idAliado, Sort o);

	List<Cliente> findByMaestroAliadoCodigoAliadoLike(String codigo, Sort o);


}
