package interfacedao.maestros;

import modelo.maestros.Cliente;
import modelo.maestros.MaestroAliado;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IClienteDAO extends JpaRepository<Cliente, String> {

	Cliente findByCodigoClienteAndMaestroAliado(String idCliente,
			MaestroAliado aliado);

}
