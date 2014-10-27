package servicio.maestros;

import java.util.List;

import interfacedao.maestros.IClienteDAO;

import modelo.maestros.Cliente;
import modelo.maestros.MaestroAliado;

import org.springframework.beans.factory.annotation.Autowired;
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
}
