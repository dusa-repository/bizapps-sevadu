package interfacedao.bitacora;

import java.util.Date;
import java.util.List;

import modelo.bitacora.BitacoraEliminacion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IBitacoraEliminacionDAO extends JpaRepository<BitacoraEliminacion, Long> {

	List<BitacoraEliminacion> findByUsuarioLoginLikeAndFechaEliminacionBetween(
			String user, Date desde, Date hasta);

}
