package interfacedao.seguridad;
import java.util.List;

import modelo.seguridad.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioDAO extends JpaRepository<Usuario, Long> {

	Usuario findByLogin(String nombre);

	Usuario findByCedula(String value);

	Usuario findByCedulaAndEmail(String value, String value2);

	List<Usuario> findByIdUsuarioNotIn(List<Long> lista);

	Usuario findByLoginAndIdUsuarioNotIn(String value, List<Long> lista);
	
}