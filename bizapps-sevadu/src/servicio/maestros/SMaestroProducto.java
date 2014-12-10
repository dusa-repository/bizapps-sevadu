package servicio.maestros;

import interfacedao.maestros.IMaestroProductoDAO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroMarca;
import modelo.maestros.MaestroProducto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SMaestroProducto")
public class SMaestroProducto {

	@Autowired
	private IMaestroProductoDAO productoDAO;

	public List<MaestroProducto> buscarTodosOrdenados() {
		return productoDAO.findAllOrderByCodigoProductoDusaAsc();
	}

	public boolean existe(String value) {
		return productoDAO.exists(value);
	}

	public void guardar(MaestroProducto producto) {
		productoDAO.save(producto);
	}

	public void eliminarVarios(List<MaestroProducto> eliminarLista) {
		productoDAO.delete(eliminarLista);
	}

	public void eliminarUno(String clave) {
		productoDAO.delete(clave);
	}

	public MaestroProducto buscar(String idProducto) {
		return productoDAO.findOne(idProducto);
	}

	public List<MaestroProducto> buscarPorAliados(
			List<MaestroAliado> eliminarLista) {
		return productoDAO.findByMaestroAliadoIn(eliminarLista);
	}

	public List<MaestroProducto> buscarPorAliado(MaestroAliado aliado) {
		return productoDAO.findByMaestroAliado(aliado);
	}

	public List<MaestroProducto> buscarPorMarcas(
			List<MaestroMarca> eliminarLista) {
		List<String> ids = new ArrayList<String>();
		for (Iterator<MaestroMarca> iterator = eliminarLista.iterator(); iterator
				.hasNext();) {
			MaestroMarca maestroMarca = (MaestroMarca) iterator.next();
			ids.add(maestroMarca.getMarcaDusa());
		}
		return productoDAO.findByMaestroMarcaMarcaDusaIn(ids);
	}

	public List<MaestroProducto> buscarPorMarca(String value) {
		return productoDAO.findByMaestroMarcaMarcaDusa(value);
	}

	public void guardarVarios(List<MaestroProducto> productos) {
		productoDAO.save(productos);
	}
}
