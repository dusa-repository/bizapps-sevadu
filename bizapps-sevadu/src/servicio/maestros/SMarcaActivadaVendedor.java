package servicio.maestros;

import interfacedao.maestros.IMarcaActivadaVendedorDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.maestros.Cliente;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MarcaActivadaVendedor;
import modelo.pk.MarcaActivadaPK;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service("SMarcaActivadaVendedor")
public class SMarcaActivadaVendedor {

	@Autowired
	private IMarcaActivadaVendedorDAO marcaActivadaDAO;

	public void guardarVarios(List<MarcaActivadaVendedor> marcas) {
		marcaActivadaDAO.save(marcas);
	}

	public MarcaActivadaVendedor buscar(MarcaActivadaPK clave) {
		return marcaActivadaDAO.findOne(clave);
	}

	public List<MarcaActivadaVendedor> buscarPorAliados(
			List<MaestroAliado> eliminarLista) {
		return marcaActivadaDAO.findByIdMaestroAliadoIn(eliminarLista);
	}

	public List<MarcaActivadaVendedor> buscarPorAliado(MaestroAliado aliado) {
		return marcaActivadaDAO.findByIdMaestroAliado(aliado);
	}

	public List<MarcaActivadaVendedor> buscarPorClientes(
			List<Cliente> eliminarLista) {
		return marcaActivadaDAO.findByIdClienteIn(eliminarLista);
	}

	public List<MarcaActivadaVendedor> buscarPorCliente(Cliente cliente) {
		return marcaActivadaDAO.findByIdCliente(cliente);
	}

	public List<MarcaActivadaVendedor> buscarTodosOrdenados() {
		List<String> ordenar = new ArrayList<String>();
		Sort o;
		ordenar.add("idMaestroAliadoCodigoAliado");
		o = new Sort(Sort.Direction.ASC, ordenar);
		return marcaActivadaDAO.findAll(o);
	}

	public void guardar(MarcaActivadaVendedor marcaActivadaVendedor) {
		marcaActivadaDAO.save(marcaActivadaVendedor);
	}

	public void eliminarVarios(List<MarcaActivadaVendedor> eliminarLista) {
		marcaActivadaDAO.delete(eliminarLista);
	}

	public void eliminarUno(MarcaActivadaPK clave) {
		marcaActivadaDAO.delete(clave);
	}

	public List<MarcaActivadaVendedor> buscarPorAliadoEntreFechasRegistro(
			String idAliado, Date desde, Date hasta) {
		return marcaActivadaDAO
				.findByIdMaestroAliadoCodigoAliadoAndFechaAuditoriaBetween(
						idAliado, desde, hasta);
	}
}
