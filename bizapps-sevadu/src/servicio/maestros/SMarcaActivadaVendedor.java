package servicio.maestros;

import interfacedao.maestros.IMarcaActivadaVendedorDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SMarcaActivadaVendedor")
public class SMarcaActivadaVendedor {

	@Autowired
	private IMarcaActivadaVendedorDAO marcaActivadaDAO;
}
