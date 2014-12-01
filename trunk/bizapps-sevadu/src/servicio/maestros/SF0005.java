package servicio.maestros;


import interfacedao.maestros.IF0005DAO;

import java.util.List;

import modelo.maestros.F0005;
import modelo.pk.F0005PK;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SF0005")
public class SF0005 {
	
	@Autowired
	private IF0005DAO f0005DAO;

	public void guardar(F0005 fooo5) {
		 f0005DAO.save(fooo5);
		
	}

	public List<F0005> buscarTodosOrdenados() {
		return f0005DAO.findAllOrderBySYAndRTAndKY();
	}

	public void eliminarVarios(List<F0005> eliminarLista) {
		f0005DAO.delete(eliminarLista);
		
	}

	public void eliminarUno(F0005PK clave) {
		f0005DAO.delete(clave);
		
	}

	public F0005 buscar(String value, String value2, String value3) {
		F0005PK clave = new F0005PK();
		clave.setDrsy(value);
		clave.setDrrt(value2);
		clave.setDrky(value3);
		return f0005DAO.findOne(clave);
	}

	public List<F0005> buscarParaUDCOrdenados(String string, String string2) {
		return f0005DAO.findByIdDrsyAndIdDrrtOrderByIdDrsyAscIdDrrtAsc(string, string2);
	}

}
