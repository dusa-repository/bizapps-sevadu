package interfacedao.maestros;

import java.util.Date;
import java.util.List;

import modelo.maestros.Cliente;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MarcaActivadaVendedor;
import modelo.pk.MarcaActivadaPK;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IMarcaActivadaVendedorDAO extends JpaRepository<MarcaActivadaVendedor, MarcaActivadaPK> {

	List<MarcaActivadaVendedor> findByIdMaestroAliadoIn(
			List<MaestroAliado> eliminarLista);

	List<MarcaActivadaVendedor> findByIdMaestroAliado(MaestroAliado aliado);

	List<MarcaActivadaVendedor> findByIdClienteIn(List<Cliente> eliminarLista);

	List<MarcaActivadaVendedor> findByIdCliente(Cliente cliente);

	List<MarcaActivadaVendedor> findByIdMaestroAliadoCodigoAliadoAndFechaAuditoriaBetween(
			String idAliado, Date desde, Date hasta);

}
