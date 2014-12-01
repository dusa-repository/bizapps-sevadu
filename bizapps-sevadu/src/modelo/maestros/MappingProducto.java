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

import modelo.pk.MappingProductoPK;

/**
 * The persistent class for the mapping_producto database table.
 * 
 */
@Entity
@Table(name = "mapping_producto")
@NamedQuery(name = "MappingProducto.findAll", query = "SELECT m FROM MappingProducto m")
//@IdClass(MappingProductoPK.class)
public class MappingProducto implements Serializable  {

	private static final long serialVersionUID = -7741985757645361744L;
	
	@EmbeddedId
	private MappingProductoPK id;

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

	public MappingProductoPK getId() {
		return id;
	}

	public void setId(MappingProductoPK id) {
		this.id = id;
	}

}