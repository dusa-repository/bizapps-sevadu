package servicio.bitacora;

import interfacedao.bitacora.IBitacoraEliminacionDAO;

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
}
