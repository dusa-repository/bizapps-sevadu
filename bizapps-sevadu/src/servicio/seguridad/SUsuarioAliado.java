package servicio.seguridad;

import interfacedao.maestros.IMaestroAliadoDAO;
import interfacedao.seguridad.IUsuarioAliadoDAO;

import java.util.ArrayList;
import java.util.List;

import modelo.maestros.MaestroAliado;
import modelo.seguridad.Usuario;
import modelo.seguridad.UsuarioAliado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SUsuarioAliado")
public class SUsuarioAliado {

	@Autowired
	private IUsuarioAliadoDAO usuarioDAO;
	@Autowired
	private IMaestroAliadoDAO aliadoDAO;

	public List<UsuarioAliado> buscarPorUsuario(String u) {
		return usuarioDAO.findByIdUsuarioLogin(u);
	}

	public UsuarioAliado buscarActivo() {
		return usuarioDAO.findByEstadoTrue();
	}

	public void guardar(UsuarioAliado nuevo) {
		usuarioDAO.saveAndFlush(nuevo);
	}

	public UsuarioAliado buscarActivo(Usuario u) {
		return usuarioDAO.findByIdUsuarioAndEstadoTrue(u);
	}

	public List<MaestroAliado> buscarOcupados(Usuario usuario) {
		List<UsuarioAliado> lista = usuarioDAO.findByIdUsuario(usuario);
		List<MaestroAliado> retorna = new ArrayList<MaestroAliado>();
		for (int i = 0; i < lista.size(); i++) {
			retorna.add(lista.get(i).getId().getMaestroAliado());
		}
		return retorna;
	}

	public List<MaestroAliado> buscarLibres(List<MaestroAliado> aliados) {
		if (aliados.isEmpty())
			return aliadoDAO.findAllOrderByNombreAsc();
		List<String> lista = new ArrayList<String>();
		for (int i = 0; i < aliados.size(); i++) {
			lista.add(aliados.get(i).getCodigoAliado());
		}
		return aliadoDAO.findByCodigoAliadoNotIn(lista);
	}

	public void guardarVarios(List<UsuarioAliado> lista) {
		usuarioDAO.save(lista);
	}

	public void limpiarAliados(Usuario usuario) {
		List<UsuarioAliado> lista = usuarioDAO.findByIdUsuarioLogin(usuario
				.getLogin());
		if (!lista.isEmpty())
			usuarioDAO.delete(lista);
	}
}
