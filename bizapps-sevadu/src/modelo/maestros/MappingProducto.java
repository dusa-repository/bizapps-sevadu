package modelo.maestros;

import java.io.Serializable;

import javax.persistence.*;

import modelo.pk.MappingProductoPK;

import java.sql.Time;
import java.util.Date;

/**
 * The persistent class for the mapping_producto database table.
 * 
 */
@Entity
@Table(name = "mapping_producto")
@NamedQuery(name = "MappingProducto.findAll", query = "SELECT m FROM MappingProducto m")
@IdClass(MappingProductoPK.class)
public class MappingProducto {

	// bi-directional many-to-one association to MaestroAliado
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_aliado")
	private MaestroAliado maestroAliado;

	// bi-directional many-to-one association to MaestroProducto
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_producto_dusa")
	private MaestroProducto maestroProducto;

	@Column(name = "codigo_botella_cliente")
	private String codigoBotellaCliente;

	@Column(name = "codigo_caja_cliente")
	private String codigoCajaCliente;

	@Column(name = "codigo_producto_cliente")
	private String codigoProductoCliente;

	@Column(name = "estado_mapeo")
	private int estadoMapeo;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_auditoria")
	private Date fechaAuditoria;

	@Column(name = "hora_auditoria")
	private Time horaAuditoria;

	@Column(name = "id_usuario")
	private String idUsuario;

	@Column(name = "lote_upload")
	private String loteUpload;

	public MappingProducto() {
	}

	public String getCodigoBotellaCliente() {
		return this.codigoBotellaCliente;
	}

	public void setCodigoBotellaCliente(String codigoBotellaCliente) {
		this.codigoBotellaCliente = codigoBotellaCliente;
	}

	public String getCodigoCajaCliente() {
		return this.codigoCajaCliente;
	}

	public void setCodigoCajaCliente(String codigoCajaCliente) {
		this.codigoCajaCliente = codigoCajaCliente;
	}

	public String getCodigoProductoCliente() {
		return this.codigoProductoCliente;
	}

	public void setCodigoProductoCliente(String codigoProductoCliente) {
		this.codigoProductoCliente = codigoProductoCliente;
	}

	public int getEstadoMapeo() {
		return this.estadoMapeo;
	}

	public void setEstadoMapeo(int estadoMapeo) {
		this.estadoMapeo = estadoMapeo;
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