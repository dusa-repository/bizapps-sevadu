package servicio.seguridad;

import interfacedao.maestros.IMaestroAliadoDAO;
import interfacedao.seguridad.IUsuarioDAO;

import java.util.ArrayList;
import java.util.List;

import modelo.maestros.MaestroAliado;
import modelo.seguridad.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("SUsuario")
public class SUsuario {

	@Autowired
	private IUsuarioDAO usuarioDAO;
	@Autowired
	private IMaestroAliadoDAO aliadoDAO;

	public List<Usuario> buscarTodos() {
		return usuarioDAO.findAll();
	}

	public void guardar(Usuario usuario) {
		usuarioDAO.save(usuario);
	}

	@Transactional
	public Usuario buscarUsuarioPorNombre(String nombre) {
		return usuarioDAO.findByLogin(nombre);
	}

	@Transactional
	public Usuario buscarPorCedula(String nombre) {
		return usuarioDAO.findByCedula(nombre);
	}

	public Usuario buscar(long id) {
		return usuarioDAO.findOne(id);
	}

	public void eliminarVarios(List<Usuario> eliminarLista) {
		usuarioDAO.delete(eliminarLista);
	}

	public void eliminarClave(long id) {
		usuarioDAO.delete(id);
	}

	public Usuario buscarPorCedulayCorreo(String value, String value2) {
		return usuarioDAO.findByCedulaAndEmail(value, value2);
	}

	public Usuario buscarPorLogin(String value) {
		return usuarioDAO.findByLogin(value);
	}

	public List<Usuario> buscarTodosSinAliado() {
		List<MaestroAliado> list = aliadoDAO.findByUsuarioNotNull();
		List<Long> lista = new ArrayList<Long>();
		if (list.isEmpty())
			return usuarioDAO.findAll();
		else {
			for (int i = 0; i < list.size(); i++) {
				lista.add(list.get(i).getUsuario().getIdUsuario());
			}
			return usuarioDAO.findByIdUsuarioNotIn(lista);
		}
	}

	public Usuario buscarPorLoginYUserNull(String value) {
		List<MaestroAliado> list = aliadoDAO.findByUsuarioNotNull();
		List<Long> lista = new ArrayList<Long>();
		if (list.isEmpty())
			return usuarioDAO.findByLogin(value);
		else {
			for (int i = 0; i < list.size(); i++) {
				lista.add(list.get(i).getUsuario().getIdUsuario());
			}
			return usuarioDAO.findByLoginAndIdUsuarioNotIn(value, lista);
		}
	}

}