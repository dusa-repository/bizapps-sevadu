package modelo.maestros;


import javax.persistence.*;

import modelo.pk.ExistenciaPK;

import java.sql.Time;
import java.util.Date;


/**
 * The persistent class for the existencia database table.
 * 
 */
@Entity
@NamedQuery(name="Existencia.findAll", query="SELECT e FROM Existencia e")
@IdClass(ExistenciaPK.class)
public class Existencia {

	//bi-directional many-to-one association to MaestroAliado
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="codigo_aliado")
	private MaestroAliado maestroAliado;

	//bi-directional many-to-one association to MaestroProducto
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "codigo_producto", referencedColumnName = "codigo_producto_dusa")
	private MaestroProducto maestroProducto;

	@Id
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_existencia")
	private java.util.Date fechaExistencia;
	
	private int cantidad;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_auditoria")
	private Date fechaAuditoria;

	@Column(name="hora_auditoria")
	private Time horaAuditoria;

	@Column(name="id_usuario")
	private String idUsuario;

	@Column(name="lote_upload")
	private String loteUpload;

	public Existencia() {
	}

	public int getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Date getFechaAuditoria() {
		return this.fechaAuditoria;
	}

	public void setFechaAuditoria(Date fechaAuditoria) {
		this.fechaAuditoria = fechaAuditoria;
	}

	public Time getHoraAuditoria() {
		return this.horaAuditoria;
	}

	public void setHoraAuditoria(Time horaAuditoria) {
		this.horaAuditoria = horaAuditoria;
	}

	public String getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getLoteUpload() {
		return this.loteUpload;
	}

	public void setLoteUpload(String loteUpload) {
		this.loteUpload = loteUpload;
	}

	public MaestroAliado getMaestroAliado() {
		return this.maestroAliado;
	}

	public void setMaestroAliado(MaestroAliado maestroAliado) {
		this.maestroAliado = maestroAliado;
	}

	public MaestroProducto getMaestroProducto() {
		return this.maestroProducto;
	}

	public void setMaestroProducto(MaestroProducto maestroProducto) {
		this.maestroProducto = maestroProducto;
	}

}