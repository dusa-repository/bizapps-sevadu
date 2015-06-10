package servicio.bitacora;

import interfacedao.bitacora.IBitacoraEliminacionDAO;

import java.util.Date;
import java.util.List;

import modelo.bitacora.BitacoraEliminacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SBitacoraEliminacion")
public class SBitacoraEliminacion {

	@Autowired
	private IBitacoraEliminacionDAO bitacoraEliminacion;

	public void guardar(BitacoraEliminacion eliminada) {
		bitacoraEliminacion.save(eliminada);
	}

	public List<BitacoraEliminacion> buscarPorUsarioYFechas(String user,
			Date desde, Date hasta) {
		return bitacoraEliminacion.findByUsuarioLoginLikeAndFechaEliminacionBetween(user,
				desde, hasta);
	}
}
