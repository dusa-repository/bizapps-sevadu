package servicio.seguridad;

import interfacedao.seguridad.IArbolDAO;

import java.util.ArrayList;
import java.util.List;

import modelo.seguridad.Arbol;
import modelo.seguridad.Grupo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SArbol")
public class SArbol {

	@Autowired
	private IArbolDAO arbolDAO;

	public void guardar(Arbol arbol) {
		arbolDAO.save(arbol);
	}

	public Arbol buscar(long id) {

		return arbolDAO.findOne(id);
	}

	public List<Arbol> listarArbol() {
		return arbolDAO.buscarTodos();
	}

//	public Arbol buscarPorNombreArbol(String nombre) {
//		Arbol arbol;
//		arbol = arbolDAO.findByNombre(nombre);
//		return arbol;
//	}

	public List<Arbol> ordenarPorID(ArrayList<Long> ids) {

		List<Arbol> arboles;
		arboles = arbolDAO.buscar(ids);
		return arboles;

	}

	public Arbol buscarPorId(Long id) {

		Arbol arbol;
		arbol = arbolDAO.findOne(id);
		return arbol;
	}

	public List<Arbol> buscarporGrupo(Grupo grupo) {
		List<Arbol> arboles;
		arboles = arbolDAO.findByGruposArbol(grupo);
		return arboles;
	}

	public void eliminarUno(long clave) {
		arbolDAO.delete(clave);
		
	}

	public void eliminarVarios(List<Arbol> eliminarLista) {
		arbolDAO.delete(eliminarLista);
		
	}

}
