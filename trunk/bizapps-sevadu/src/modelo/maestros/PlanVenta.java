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

import modelo.pk.PlanVentaPK;

/**
 * The persistent class for the plan_ventas database table.
 * 
 */
@Entity
@Table(name = "plan_ventas")
@NamedQuery(name = "PlanVenta.findAll", query = "SELECT p FROM PlanVenta p")
public class PlanVenta implements Serializable {

	private static final long serialVersionUID = -6350609058294377692L;

	@EmbeddedId
	private PlanVentaPK id;

	@Column(name = "cajas_planificadas")
	private int cajasPlanificadas;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_auditoria")
	private Date fechaAuditoria;

	@Column(name = "hora_auditoria")
	private Time horaAuditoria;

	@Column(name = "id_usuario")
	private String idUsuario;

	@Column(name = "lote_upload")
	private String loteUpload;

	public PlanVenta() {
	}

	public int getCajasPlanificadas() {
		return this.cajasPlanificadas;
	}

	public void setCajasPlanificadas(int cajasPlanificadas) {
		this.cajasPlanificadas = cajasPlanificadas;
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

	public PlanVentaPK getId() {
		return id;
	}

	public void setId(PlanVentaPK id) {
		this.id = id;
	}

}