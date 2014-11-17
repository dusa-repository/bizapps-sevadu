package interfacedao.maestros;

import java.util.List;

import modelo.maestros.Cliente;
import modelo.maestros.MaestroAliado;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IClienteDAO extends JpaRepository<Cliente, String> {

	Cliente findByCodigoClienteAndMaestroAliado(String idCliente,
			MaestroAliado aliado);

	List<Cliente> findByMaestroAliado(MaestroAliado aliado, Sort o);


}
