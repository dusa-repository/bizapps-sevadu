package modelo.pk;

import java.io.Serializable;

import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroProducto;

public class MappingProductoPK implements Serializable {


	private static final long serialVersionUID = -451606454686528706L;
	private MaestroAliado maestroAliado;
	private MaestroProducto maestroProducto;
	
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
