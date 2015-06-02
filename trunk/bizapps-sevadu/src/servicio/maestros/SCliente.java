package servicio.maestros;

import interfacedao.maestros.IClienteDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.maestros.Cliente;
import modelo.maestros.MaestroAliado;
import modelo.maestros.TipoCliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service("SCliente")
public class SCliente {

	@Autowired
	private IClienteDAO clienteDAO;

	public Cliente buscarPorCodigoYAliado(String idCliente, MaestroAliado aliado) {
		return clienteDAO
				.findByCodigoClienteAndMaestroAliado(idCliente, aliado);
	}

	public void guardarVarios(List<Cliente> clientes) {
		clienteDAO.save(clientes);
	}

	public List<Cliente> buscarPorAliado(MaestroAliado aliado) {
		String codigo = "%";
		if (aliado != null)
			codigo = aliado.getCodigoAliado();
		List<String> ordenar = new ArrayList<String>();
		Sort o;
		ordenar.add("supervisor");
		ordenar.add("vendedor");
		ordenar.add("zona");
		ordenar.add("nombre");
		o = new Sort(Sort.Direction.ASC, ordenar);
		return clienteDAO.findByMaestroAliadoCodigoAliadoLike(codigo, o);
	}

	public List<Cliente> buscarPorAliados(List<MaestroAliado> eliminarLista) {
		return clienteDAO.findByMaestroAliadoIn(eliminarLista);
	}

	public List<Cliente> buscarPorTiposCliente(List<TipoCliente> eliminarLista) {
		return clienteDAO.findByTipoClienteIn(eliminarLista);
	}

	public List<Cliente> buscarPorTipoCliente(String id) {
		return clienteDAO.findByTipoClienteCodigo(id);
	}

	public Integer contarPorAliado(String aliado) {
		List<Cliente> lista = clienteDAO
				.findByMaestroAliadoCodigoAliado(aliado);
		return lista.size();
	}

	public List<Cliente> buscarTodosOrdenados() {
		List<String> ordenar = new ArrayList<String>();
		Sort o;
		ordenar.add("codigoCliente");
		ordenar.add("nombre");
		o = new Sort(Sort.Direction.ASC, ordenar);
		return clienteDAO.findAll(o);
	}

	public void guardar(Cliente cliente) {
		clienteDAO.save(cliente);
	}

	public void eliminarVarios(List<Cliente> eliminarLista) {
		clienteDAO.delete(eliminarLista);
	}

	public Cliente buscarPorCodigo(String clave) {
		return clienteDAO.findOne(clave);
	}

	public void eliminarUno(String clave) {
		clienteDAO.delete(clave);
	}

	public boolean existe(String value) {
		return clienteDAO.exists(value);
	}

	public List<Cliente> buscarPorAliadoEntreFechasRegistro(String idAliado,
			Date desde, Date hasta) {
		return clienteDAO
				.findByMaestroAliadoCodigoAliadoAndFechaAuditoriaBetween(
						idAliado, desde, hasta);
	}

	public List<Cliente> buscarPorIdAliado(String idAliado) {
		List<String> ordenar = new ArrayList<String>();
		Sort o;
		ordenar.add("codigoCliente");
		ordenar.add("nombre");
		o = new Sort(Sort.Direction.ASC, ordenar);
		return clienteDAO.findByMaestroAliadoCodigoAliado(idAliado, o);
	}

}
