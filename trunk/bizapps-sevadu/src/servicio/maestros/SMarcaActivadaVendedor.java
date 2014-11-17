package servicio.maestros;

import java.util.List;

import interfacedao.maestros.IMarcaActivadaVendedorDAO;
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
}
