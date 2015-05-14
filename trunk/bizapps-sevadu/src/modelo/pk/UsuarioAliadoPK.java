package modelo.pk;

import java.io.Serializable;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import modelo.maestros.MaestroAliado;
import modelo.seguridad.Usuario;

public class UsuarioAliadoPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_aliado")
	private MaestroAliado maestroAliado;

	public UsuarioAliadoPK() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public MaestroAliado getMaestroAliado() {
		return maestroAliado;
	}

	public void setMaestroAliado(MaestroAliado maestroAliado) {
		this.maestroAliado = maestroAliado;
	}
	
}
