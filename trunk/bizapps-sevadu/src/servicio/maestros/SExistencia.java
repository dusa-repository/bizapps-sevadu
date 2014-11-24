package servicio.maestros;

import java.util.List;

import interfacedao.maestros.IExistenciaDAO;
import modelo.maestros.Existencia;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroProducto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SExistencia")
public class SExistencia {

	@Autowired
	private IExistenciaDAO existenciaDAO;

	public void guardarVarios(List<Existencia> existencias) {
		existenciaDAO.save(existencias);
	}

	public List<Existencia> buscarPorAliados(List<MaestroAliado> eliminarLista) {
		return existenciaDAO.findByIdMaestroAliadoIn(eliminarLista);
	}

	public List<Existencia> buscarPorAliado(MaestroAliado aliado) {
		return existenciaDAO.findByIdMaestroAliado(aliado);
	}

	public List<Existencia> buscarPorProductos(
			List<MaestroProducto> eliminarLista) {
		return existenciaDAO.findByIdMaestroProdIn(eliminarLista);
	}

	public List<Existencia> buscarPorProducto(String clave) {
		return existenciaDAO.findByIdMaestroProdCodigoProductoDusa(clave);
	}
}
