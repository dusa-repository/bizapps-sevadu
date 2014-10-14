package modelo.pk;

import java.io.Serializable;

import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroProducto;

public class ExistenciaPK implements Serializable {

	private static final long serialVersionUID = 6839257756396387291L;
 

	private MaestroAliado maestroAliado;
	private MaestroProducto maestroProducto;
	private java.util.Date fechaExistencia;
	
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
	public java.util.Date getFechaExistencia() {
		return fechaExistencia;
	}
	public void setFechaExistencia(java.util.Date fechaExistencia) {
		this.fechaExistencia = fechaExistencia;
	}
	
	

}
