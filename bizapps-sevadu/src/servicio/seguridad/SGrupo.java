package servicio.seguridad;

import interfacedao.seguridad.IGrupoDAO;

import java.util.List;

import modelo.seguridad.Grupo;
import modelo.seguridad.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SGrupo")
public class SGrupo {

	@Autowired
	private IGrupoDAO grupoDAO;
	
	public void guardarGrupo(Grupo grupo){
		grupoDAO.save(grupo);
	}
	
	public List<Grupo> buscarTodos(){
		return grupoDAO.findByEstadoTrue();
	}
	public Grupo buscarGrupo(long id){
		return grupoDAO.findOne(id);
	}
	
	public List<Grupo> buscarGruposDelUsuario(Usuario usuario){
		return grupoDAO.findByUsuarios(usuario);
	}
	
	public List<Grupo> buscarGruposDisponibles(List<Long> ids){
		return grupoDAO.findByIdGrupoNotInAndEstadoTrue(ids);
	}
	
	public Grupo buscarPorNombre(String nombreGrupo){
		return grupoDAO.findByNombre(nombreGrupo);
	}

	public List<Grupo> filtroNombre(String valor) {
		return grupoDAO.findByNombreStartingWithAllIgnoreCase(valor);
	}

	public void eliminarUno(long id) {
		grupoDAO.delete(id);
	}

	public List<Grupo> buscarTodosOrdenados() {
		return grupoDAO.findAllOrderById();
	}

	public void eliminarVarios(List<Grupo> eliminarLista) {
		grupoDAO.delete(eliminarLista);
		
	}

	public List<Grupo> buscarGruposUsuario(Usuario u) {
		return grupoDAO.findByUsuariosOrderByNombreAsc(u);
	}

}