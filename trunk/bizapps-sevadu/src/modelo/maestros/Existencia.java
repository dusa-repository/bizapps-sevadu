package modelo.maestros;


import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import modelo.pk.ExistenciaPK;


/**
 * The persistent class for the existencia database table.
 * 
 */
@Entity
@Table(name="existencia")
@NamedQuery(name="Existencia.findAll", query="SELECT e FROM Existencia e")
public class Existencia implements Serializable {

	private static final long serialVersionUID = -5233763823355941727L;

	@EmbeddedId
	private ExistenciaPK id;
	
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
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Date getFechaAuditoria() {
		return fechaAuditoria;
	}

	public void setFechaAuditoria(Date fechaAuditoria) {
		this.fechaAuditoria = fechaAuditoria;
	}

	public Time getHoraAuditoria() {
		return horaAuditoria;
	}

	public void setHoraAuditoria(Time horaAuditoria) {
		this.horaAuditoria = horaAuditoria;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getLoteUpload() {
		return loteUpload;
	}

	public void setLoteUpload(String loteUpload) {
		this.loteUpload = loteUpload;
	}

	public ExistenciaPK getId() {
		return id;
	}

	public void setId(ExistenciaPK id) {
		this.id = id;
	}
	

}