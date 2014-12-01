package interfacedao.maestros;

import modelo.maestros.TipoCliente;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ITipoClienteDAO extends JpaRepository<TipoCliente, String> {

}
