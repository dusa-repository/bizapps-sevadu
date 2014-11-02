package servicio.maestros;

import java.util.ArrayList;
import java.util.List;

import interfacedao.maestros.IMaestroProductoDAO;
import interfacedao.maestros.IMappingProductoDAO;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroProducto;
import modelo.maestros.MappingProducto;
import modelo.pk.MappingProductoPK;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SMappingProducto")
public class SMappingProducto {

	@Autowired
	private IMappingProductoDAO mappingDAO;
	@Autowired
	private IMaestroProductoDAO productoDAO;

	public List<MappingProducto> buscarPorAliado(MaestroAliado aliado) {
		return mappingDAO.findByIdMaestroAliado(aliado);
	}

	public List<MappingProducto> buscarPorAliadoNot(MaestroAliado aliado) {

		List<MappingProducto> lista = mappingDAO.findByIdMaestroAliado(aliado);
		List<MaestroProducto> productos = new ArrayList<MaestroProducto>();
		List<String> ids = new ArrayList<String>();
		if (lista.isEmpty())
			productos = productoDAO.findAllOrderByCodigoProductoDusaAsc();
		else {
			for (int i = 0; i < lista.size(); i++) {
				ids.add(lista.get(i).getId().getMaestroProducto()
						.getCodigoProductoDusa());
			}
			productos = productoDAO
					.findByCodigoProductoDusaNotInOrderByCodigoProductoDusaAsc(ids);
		}
		lista = new ArrayList<MappingProducto>();
		for (int i = 0; i < productos.size(); i++) {
			MappingProducto mapper = new MappingProducto();
			MappingProductoPK pk = new MappingProductoPK();
			pk.setMaestroAliado(aliado);
			pk.setMaestroProducto(productos.get(i));
			mapper.setId(pk);
			mapper.setLoteUpload("No");
			lista.add(mapper);
		}
		return lista;
	}

	public List<MappingProducto> buscarTodosMasAliado(MaestroAliado aliado) {
		List<MappingProducto> lista = mappingDAO.findByIdMaestroAliado(aliado);
		lista.addAll(buscarPorAliadoNot(aliado));
		return lista;
	}

	public void guardar(MappingProducto mappeado) {
		mappingDAO.save(mappeado);
	}

	public boolean existe(MappingProductoPK id) {
		return mappingDAO.exists(id);
	}

	public void eliminarUno(MappingProductoPK id) {
		mappingDAO.delete(id);
	}
}
