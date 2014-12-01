package servicio.maestros;

import interfacedao.maestros.IMarcaActivadaVendedorDAO;

import java.util.List;

import modelo.maestros.MaestroAliado;
import modelo.maestros.MarcaActivadaVendedor;
import modelo.pk.MarcaActivadaPK;

import org.springframework.beans.factory.annotation.Autowired;
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
}
