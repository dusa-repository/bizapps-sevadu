package servicio.maestros;

import interfacedao.maestros.IF0004DAO;

import java.util.List;

import modelo.maestros.F0004;
import modelo.pk.F0004PK;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SF0004")
public class SF0004 {

	@Autowired
	private IF0004DAO f0004DAO;

	public List<F0004> buscarTodosOrdenados() {
		return f0004DAO.findAllOrderByIdDtsy();
	}

	public F0004 buscar(String value, String value2) {
		F0004PK clave = new F0004PK();
		clave.setDtsy(value);
		clave.setDtrt(value2);
		return f0004DAO.findOne(clave);
	}

	public void guardar(F0004 fooo4) {
		f0004DAO.save(fooo4);
	}

	public void eliminarVarios(List<F0004> eliminar) {
		f0004DAO.delete(eliminar);
	}

	public void eliminarUno(F0004PK clave) {
		f0004DAO.delete(clave);
	}

	public List<F0004> buscarSY(String value) {
		return f0004DAO.findByIdDtsy(value);
	}
}
