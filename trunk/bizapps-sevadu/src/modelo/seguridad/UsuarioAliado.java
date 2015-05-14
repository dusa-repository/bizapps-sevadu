package modelo.seguridad;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import modelo.pk.UsuarioAliadoPK;

@Entity
@Table(name = "usuario_aliado")
@NamedQuery(name = "UsuarioAliado.findAll", query = "SELECT m FROM UsuarioAliado m")
public class UsuarioAliado implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private UsuarioAliadoPK id;
	
	private boolean estado;

	public UsuarioAliado() {
		super();
	}

	public UsuarioAliado(UsuarioAliadoPK id, boolean estado) {
		super();
		this.id = id;
		this.estado = estado;
	}

	public UsuarioAliadoPK getId() {
		return id;
	}

	public void setId(UsuarioAliadoPK id) {
		this.id = id;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
}
