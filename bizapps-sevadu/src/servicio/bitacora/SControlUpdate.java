package servicio.bitacora;

import interfacedao.bitacora.IControlUpdate;
import modelo.bitacora.ControlUpdate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SControlUpdate")
public class SControlUpdate {

	@Autowired
	private IControlUpdate controlDAO;

	public void guardar(ControlUpdate control) {
		controlDAO.saveAndFlush(control);
	}

}
