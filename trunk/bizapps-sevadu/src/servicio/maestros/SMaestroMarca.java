package servicio.maestros;

import interfacedao.maestros.IMaestroMarcaDAO;

import java.util.List;

import modelo.maestros.MaestroMarca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SMaestroMarca")
public class SMaestroMarca {

	@Autowired
	private IMaestroMarcaDAO marcaDAO;

	public void guardar(MaestroMarca marca) {
		marcaDAO.save(marca);
	}

	public List<MaestroMarca> buscarTodosOrdenados() {
		return marcaDAO.findAllOrderByMarcaDusaAsc();
	}

	public void eliminarVarios(List<MaestroMarca> eliminarLista) {
		marcaDAO.delete(eliminarLista);
	}

	public void eliminarUno(Integer clave) {
		marcaDAO.delete(clave);
	}

	public MaestroMarca buscar(String value) {
		return marcaDAO.findByMarcaDusa(value);
	}

	public List<MaestroMarca> buscarActivasTermometro() {
		return marcaDAO.findByFiltroTermometroTrueOrderByOrdenAsc();
	}

	public List<MaestroMarca> buscarActivasActivacion() {
		return marcaDAO.findByActivacionTrueOrderByOrdenAsc();
	}
}
