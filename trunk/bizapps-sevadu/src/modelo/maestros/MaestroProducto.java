package modelo.maestros;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the maestro_producto database table.
 * 
 */
@Entity
@Table(name = "maestro_producto")
@NamedQuery(name = "MaestroProducto.findAll", query = "SELECT m FROM MaestroProducto m")
public class MaestroProducto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "codigo_producto_dusa")
	private String codigoProductoDusa;

	@Column(name = "codigo_botella_dusa")
	private String codigoBotellaDusa;

	@Column(name = "codigo_caja_dusa")
	private String codigoCajaDusa;

	@Column(name = "descripcion_marca")
	private String descripcionMarca;

	@Column(name = "descripcion_producto")
	private String descripcionProducto;

	@Column(name = "especie_dusa")
	private String especieDusa;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_auditoria")
	private Date fechaAuditoria;

	@Column(name = "hora_auditoria")
	private Time horaAuditoria;

	@Column(name = "id_usuario")
	private String idUsuario;

	@Column(name = "lote_upload")
	private String loteUpload;

	@Column(name = "packing_size_dusa")
	private int packingSizeDusa;

	@Column(name = "volumen_dusa")
	private float volumenDusa;

	// bi-directional many-to-one association to Existencia
	@OneToMany(mappedBy = "maestroProducto")
	private List<Existencia> existencias;

	// bi-directional many-to-one association to MaestroAliado
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_aliado")
	private MaestroAliado maestroAliado;

	// bi-directional many-to-one association to MaestroMarca
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "marca_dusa", referencedColumnName = "marca_dusa", unique = false)
	private MaestroMarca maestroMarca;

	// bi-directional many-to-one association to MappingProducto
	@OneToMany(mappedBy = "maestroProducto")
	private List<MappingProducto> mappingProductos;

	// bi-directional many-to-one association to PlanVenta
	@OneToMany(mappedBy = "maestroProducto")
	private List<PlanVenta> planVentas;

	// bi-directional many-to-one association to Venta
	@OneToMany(mappedBy = "maestroProducto")
	private List<Venta> ventas;

	public MaestroProducto() {
	}

	public MaestroProducto(String codigoProductoDusa, String codigoBotellaDusa,
			String codigoCajaDusa, String descripcionMarca,
			String descripcionProducto, String especieDusa,
			Date fechaAuditoria, Time horaAuditoria, String idUsuario,
			String loteUpload, int packingSizeDusa, float volumenDusa,
			MaestroAliado maestroAliado, MaestroMarca maestroMarca) {
		super();
		this.codigoProductoDusa = codigoProductoDusa;
		this.codigoBotellaDusa = codigoBotellaDusa;
		this.codigoCajaDusa = codigoCajaDusa;
		this.descripcionMarca = descripcionMarca;
		this.descripcionProducto = descripcionProducto;
		this.especieDusa = especieDusa;
		this.fechaAuditoria = fechaAuditoria;
		this.horaAuditoria = horaAuditoria;
		this.idUsuario = idUsuario;
		this.loteUpload = loteUpload;
		this.packingSizeDusa = packingSizeDusa;
		this.volumenDusa = volumenDusa;
		this.maestroAliado = maestroAliado;
		this.maestroMarca = maestroMarca;
	}

	public String getCodigoProductoDusa() {
		return this.codigoProductoDusa;
	}

	public void setCodigoProductoDusa(String codigoProductoDusa) {
		this.codigoProductoDusa = codigoProductoDusa;
	}

	public String getCodigoBotellaDusa() {
		return this.codigoBotellaDusa;
	}

	public void setCodigoBotellaDusa(String codigoBotellaDusa) {
		this.codigoBotellaDusa = codigoBotellaDusa;
	}

	public String getCodigoCajaDusa() {
		return this.codigoCajaDusa;
	}

	public void setCodigoCajaDusa(String codigoCajaDusa) {
		this.codigoCajaDusa = codigoCajaDusa;
	}

	public String getDescripcionMarca() {
		return this.descripcionMarca;
	}

	public void setDescripcionMarca(String descripcionMarca) {
		this.descripcionMarca = descripcionMarca;
	}

	public String getDescripcionProducto() {
		return this.descripcionProducto;
	}

	public void setDescripcionProducto(String descripcionProducto) {
		this.descripcionProducto = descripcionProducto;
	}

	public String getEspecieDusa() {
		return this.especieDusa;
	}

	public void setEspecieDusa(String especieDusa) {
		this.especieDusa = especieDusa;
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

	public int getPackingSizeDusa() {
		return this.packingSizeDusa;
	}

	public void setPackingSizeDusa(int packingSizeDusa) {
		this.packingSizeDusa = packingSizeDusa;
	}

	public float getVolumenDusa() {
		return this.volumenDusa;
	}

	public void setVolumenDusa(float volumenDusa) {
		this.volumenDusa = volumenDusa;
	}

	public List<Existencia> getExistencias() {
		return this.existencias;
	}

	public void setExistencias(List<Existencia> existencias) {
		this.existencias = existencias;
	}

	public Existencia addExistencia(Existencia existencia) {
		getExistencias().add(existencia);
		existencia.setMaestroProducto(this);

		return existencia;
	}

	public Existencia removeExistencia(Existencia existencia) {
		getExistencias().remove(existencia);
		existencia.setMaestroProducto(null);

		return existencia;
	}

	public MaestroAliado getMaestroAliado() {
		return this.maestroAliado;
	}

	public void setMaestroAliado(MaestroAliado maestroAliado) {
		this.maestroAliado = maestroAliado;
	}

	public MaestroMarca getMaestroMarca() {
		return this.maestroMarca;
	}

	public void setMaestroMarca(MaestroMarca maestroMarca) {
		this.maestroMarca = maestroMarca;
	}

	public List<MappingProducto> getMappingProductos() {
		return this.mappingProductos;
	}

	public void setMappingProductos(List<MappingProducto> mappingProductos) {
		this.mappingProductos = mappingProductos;
	}

	public MappingProducto addMappingProducto(MappingProducto mappingProducto) {
		getMappingProductos().add(mappingProducto);
		mappingProducto.setMaestroProducto(this);

		return mappingProducto;
	}

	public MappingProducto removeMappingProducto(MappingProducto mappingProducto) {
		getMappingProductos().remove(mappingProducto);
		mappingProducto.setMaestroProducto(null);

		return mappingProducto;
	}

	public List<PlanVenta> getPlanVentas() {
		return this.planVentas;
	}

	public void setPlanVentas(List<PlanVenta> planVentas) {
		this.planVentas = planVentas;
	}

	public PlanVenta addPlanVenta(PlanVenta planVenta) {
		getPlanVentas().add(planVenta);
		planVenta.setMaestroProducto(this);

		return planVenta;
	}

	public PlanVenta removePlanVenta(PlanVenta planVenta) {
		getPlanVentas().remove(planVenta);
		planVenta.setMaestroProducto(null);

		return planVenta;
	}

	public List<Venta> getVentas() {
		return this.ventas;
	}

	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}

	public Venta addVenta(Venta venta) {
		getVentas().add(venta);
		venta.setMaestroProducto(this);

		return venta;
	}

	public Venta removeVenta(Venta venta) {
		getVentas().remove(venta);
		venta.setMaestroProducto(null);

		return venta;
	}

}