package servicio.maestros;

import interfacedao.maestros.IClienteDAO;

import java.util.ArrayList;
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
		List<String> ordenar = new ArrayList<String>();
		Sort o;
		ordenar.add("supervisor");
		ordenar.add("vendedor");
		ordenar.add("zona");
		ordenar.add("nombre");
		o = new Sort(Sort.Direction.ASC, ordenar);
		return clienteDAO.findByMaestroAliado(aliado,o);
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

}