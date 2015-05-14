package interfacedao.seguridad;

import java.util.List;

import modelo.maestros.MaestroAliado;
import modelo.pk.UsuarioAliadoPK;
import modelo.seguridad.Usuario;
import modelo.seguridad.UsuarioAliado;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioAliadoDAO extends
		JpaRepository<UsuarioAliado, UsuarioAliadoPK> {

	List<UsuarioAliado> findByIdUsuarioLogin(String u);

	UsuarioAliado findByEstadoTrue();

	UsuarioAliado findByIdUsuarioAndEstadoTrue(Usuario u);

	List<UsuarioAliado> findByIdUsuario(Usuario usuario);

}
