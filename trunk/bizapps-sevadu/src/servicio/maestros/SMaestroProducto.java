package servicio.maestros;

import java.util.List;

import interfacedao.maestros.IMaestroProductoDAO;
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
}
