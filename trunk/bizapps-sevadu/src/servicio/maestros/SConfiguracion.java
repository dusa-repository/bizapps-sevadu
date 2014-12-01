package servicio.maestros;

import interfacedao.maestros.IConfiguracionDAO;

import java.util.List;

import modelo.maestros.Configuracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SConfiguracion")
public class SConfiguracion {

	@Autowired
	private IConfiguracionDAO configuracionDAO;

	public void guardar(Configuracion confi) {
		configuracionDAO.save(confi);

	}

	public Configuracion buscar(int i) {
		return configuracionDAO.findOne(i);
	}

	public List<Configuracion> buscarTodas() {
		return configuracionDAO.findAll();
	}

}
