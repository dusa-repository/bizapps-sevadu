package modelo.maestros;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;


/**
 * The persistent class for the maestro_marca database table.
 * 
 */
@Entity
@Table(name="maestro_marca")
@NamedQuery(name="MaestroMarca.findAll", query="SELECT m FROM MaestroMarca m")
public class MaestroMarca implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="marca_dusa")
	private String marcaDusa;
	
	private String descripcion;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_auditoria")
	private Date fechaAuditoria;

	@Column(name="filtro_impresion")
	private int filtroImpresion;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name="filtro_termometro")
	private boolean filtroTermometro;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name="filtro_activacion")
	private Boolean activacion;

	@Column(name="hora_auditoria")
	private Time horaAuditoria;

	@Column(name="id_usuario")
	private String idUsuario;

	@Column(name="lote_upload")
	private String loteUpload;

	@Column(name="orden_termometro")
	private Integer orden;

	//bi-directional many-to-one association to MaestroProducto
	@OneToMany(mappedBy="maestroMarca")
	private List<MaestroProducto> maestroProductos;

	public MaestroMarca() {
	}

	public MaestroMarca(int id, String marcaDusa, String descripcion,
			Date fechaAuditoria, int filtroImpresion, boolean filtroTermometro,
			Time horaAuditoria, String idUsuario, String loteUpload, Integer orden, Boolean activacion) {
		super();
		this.id = id;
		this.marcaDusa = marcaDusa;
		this.descripcion = descripcion;
		this.fechaAuditoria = fechaAuditoria;
		this.filtroImpresion = filtroImpresion;
		this.filtroTermometro = filtroTermometro;
		this.horaAuditoria = horaAuditoria;
		this.idUsuario = idUsuario;
		this.loteUpload = loteUpload;
		this.orden = orden;
		this.activacion = activacion;
	}



	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMarcaDusa() {
		return marcaDusa;
	}

	public void setMarcaDusa(String marcaDusa) {
		this.marcaDusa = marcaDusa;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public boolean isFiltroTermometro() {
		return filtroTermometro;
	}

	public void setFiltroTermometro(boolean filtroTermometro) {
		this.filtroTermometro = filtroTermometro;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaAuditoria() {
		return this.fechaAuditoria;
	}

	public void setFechaAuditoria(Date fechaAuditoria) {
		this.fechaAuditoria = fechaAuditoria;
	}

	public int getFiltroImpresion() {
		return this.filtroImpresion;
	}

	public void setFiltroImpresion(int filtroImpresion) {
		this.filtroImpresion = filtroImpresion;
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

	public List<MaestroProducto> getMaestroProductos() {
		return this.maestroProductos;
	}

	public void setMaestroProductos(List<MaestroProducto> maestroProductos) {
		this.maestroProductos = maestroProductos;
	}

	public MaestroProducto addMaestroProducto(MaestroProducto maestroProducto) {
		getMaestroProductos().add(maestroProducto);
		maestroProducto.setMaestroMarca(this);

		return maestroProducto;
	}

	public MaestroProducto removeMaestroProducto(MaestroProducto maestroProducto) {
		getMaestroProductos().remove(maestroProducto);
		maestroProducto.setMaestroMarca(null);

		return maestroProducto;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public Boolean getActivacion() {
		return activacion;
	}

	public void setActivacion(Boolean activacion) {
		this.activacion = activacion;
	}

}