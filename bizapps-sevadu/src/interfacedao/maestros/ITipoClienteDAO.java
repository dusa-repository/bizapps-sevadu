package interfacedao.maestros;

import modelo.maestros.TipoCliente;
import modelo.seguridad.Arbol;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ITipoClienteDAO extends JpaRepository<TipoCliente, String> {

}
