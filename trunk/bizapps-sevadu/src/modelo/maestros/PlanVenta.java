package modelo.maestros;

import javax.persistence.*;

import modelo.pk.PlanVentaPK;

import java.sql.Time;
import java.util.Date;

/**
 * The persistent class for the plan_ventas database table.
 * 
 */
@Entity
@Table(name = "plan_ventas")
@NamedQuery(name = "PlanVenta.findAll", query = "SELECT p FROM PlanVenta p")
@IdClass(PlanVentaPK.class)
public class PlanVenta {

	// bi-directional many-to-one association to MaestroAliado
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_aliado")
	private MaestroAliado maestroAliado;

	// bi-directional many-to-one association to MaestroProducto
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_producto", referencedColumnName = "codigo_producto_dusa")
	private MaestroProducto maestroProducto;

	@Id
	private int anno;

	@Id
	private int mes;

	@Id
	@Column(name = "zona_aliado")
	private String zonaAliado;

	@Id
	@Column(name = "vendedor_aliado")
	private String vendedorAliado;

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

	public int getAnno() {
		return anno;
	}

	public void setAnno(int anno) {
		this.anno = anno;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public String getZonaAliado() {
		return zonaAliado;
	}

	public void setZonaAliado(String zonaAliado) {
		this.zonaAliado = zonaAliado;
	}

	public String getVendedorAliado() {
		return vendedorAliado;
	}

	public void setVendedorAliado(String vendedorAliado) {
		this.vendedorAliado = vendedorAliado;
	}

}