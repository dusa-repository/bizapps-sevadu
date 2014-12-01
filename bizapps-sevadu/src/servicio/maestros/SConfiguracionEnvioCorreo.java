package servicio.maestros;

import interfacedao.maestros.IConfiguracionEnvioCorreo;

import java.util.List;

import modelo.maestros.ConfiguracionEnvioCorreo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SConfiguracionEnvioCorreo")
public class SConfiguracionEnvioCorreo {

	@Autowired
	private IConfiguracionEnvioCorreo envioDAO;

	public List<ConfiguracionEnvioCorreo> buscarTodosOrdenados() {
		return envioDAO.findAllOrderByDestinatariosAsc();
	}

	public void guardar(ConfiguracionEnvioCorreo configuracion) {
		envioDAO.save(configuracion);
	}

	public void eliminarVarios(List<ConfiguracionEnvioCorreo> eliminarLista) {
		envioDAO.delete(eliminarLista);
	}

	public void eliminarUno(long clave) {
		envioDAO.delete(clave);
	}
}
