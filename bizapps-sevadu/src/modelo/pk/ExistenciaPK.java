package modelo.pk;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroProducto;


@Embeddable
public class ExistenciaPK implements Serializable {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 4878783949834154701L;


	@ManyToOne(optional = false, fetch=FetchType.LAZY)
	@JoinColumn(name="codigo_aliado")
	private MaestroAliado maestroAliado;

	//bi-directional many-to-one association to MaestroProducto

	@ManyToOne(optional = false, fetch=FetchType.LAZY)
	@JoinColumn(name = "codigo_producto", referencedColumnName = "codigo_producto_dusa")
	private MaestroProducto maestroProd;


	@Temporal(TemporalType.DATE)
	@Column(name="fecha_existencia")
	private Date fechaExistencia;

	public ExistenciaPK() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MaestroAliado getMaestroAliado() {
		return maestroAliado;
	}

	public void setMaestroAliado(MaestroAliado maestroAliado) {
		this.maestroAliado = maestroAliado;
	}

	public MaestroProducto getMaestroProd() {
		return maestroProd;
	}

	public void setMaestroProd(MaestroProducto maestroProd) {
		this.maestroProd = maestroProd;
	}

	public Date getFechaExistencia() {
		return fechaExistencia;
	}

	public void setFechaExistencia(Date fechaExistencia) {
		this.fechaExistencia = fechaExistencia;
	}
	
	
	
	

}
