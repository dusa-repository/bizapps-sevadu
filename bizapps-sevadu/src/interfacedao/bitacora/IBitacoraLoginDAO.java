package interfacedao.bitacora;

import modelo.bitacora.BitacoraLogin;
import modelo.seguridad.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IBitacoraLoginDAO extends JpaRepository<BitacoraLogin, Long> {

	@Query("select coalesce(max(bitacora.idLogin), '0') from BitacoraLogin bitacora where bitacora.usuario = ?1")
	long buscarUltimoLoginDeUsuario(Usuario u);

}
