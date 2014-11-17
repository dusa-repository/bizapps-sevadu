package modelo.pk;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import modelo.maestros.Cliente;
import modelo.maestros.MaestroAliado;

@Embeddable
public class MarcaActivadaPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2522572846356870961L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_aliado")
	private MaestroAliado maestroAliado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_cliente")
	private Cliente cliente;

	public MarcaActivadaPK() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MaestroAliado getMaestroAliado() {
		return maestroAliado;
	}

	public void setMaestroAliado(MaestroAliado maestroAliado) {
		this.maestroAliado = maestroAliado;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	
}
