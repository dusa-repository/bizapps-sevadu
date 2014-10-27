package modelo.pk;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroProducto;

@Embeddable
public class MappingProductoPK implements Serializable {

	private static final long serialVersionUID = -451606454686528706L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_aliado")
	private MaestroAliado maestroAliado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_producto_dusa")
	private MaestroProducto maestroProducto;

	public MappingProductoPK() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MaestroAliado getMaestroAliado() {
		return maestroAliado;
	}

	public void setMaestroAliado(MaestroAliado maestroAliado) {
		this.maestroAliado = maestroAliado;
	}

	public MaestroProducto getMaestroProducto() {
		return maestroProducto;
	}

	public void setMaestroProducto(MaestroProducto maestroProducto) {
		this.maestroProducto = maestroProducto;
	}

}
