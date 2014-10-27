package servicio.maestros;

import java.util.List;

import interfacedao.maestros.IExistenciaDAO;

import modelo.maestros.Existencia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SExistencia")
public class SExistencia {

	@Autowired
	private IExistenciaDAO existenciaDAO;

	public void guardarVarios(List<Existencia> existencias) {
		existenciaDAO.save(existencias);
	}
}
