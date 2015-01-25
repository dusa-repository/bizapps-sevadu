package modelo.maestros;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import modelo.seguridad.Usuario;

/**
 * The persistent class for the maestro_aliado database table.
 * 
 */
@Entity
@Table(name = "maestro_aliado")
@NamedQuery(name = "MaestroAliado.findAll", query = "SELECT m FROM MaestroAliado m")
public class MaestroAliado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "codigo_aliado")
	private String codigoAliado;

	@OneToMany(mappedBy = "maestroAliado")
	private List<Usuario> usuarios;

	@Column(name = "ciudad_aliado")
	private String ciudadAliado;

	@Column(name = "codigo_padre")
	private String codigoPadre;

	@Column(name = "codigo_vendedor")
	private String codigoVendedor;

	@Column(name = "descripcion_vendedor")
	private String descripcionVendedor;

	@Column(name = "descripcion_zona")
	private String descripcionZona;

	@Column(name = "estado_aliado")
	private String estadoAliado;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_auditoria")
	private Date fechaAuditoria;

	@Column(name = "hora_auditoria")
	private Time horaAuditoria;

	@Column(name = "id_usuario")
	private String idUsuario;

	@Column(name = "lote_upload")
	private String loteUpload;

	private String nombre;

	@Column(name = "tipo_cliente")
	private String tipoCliente;

	private String zona;

	@Column(length = 50, name = "email")
	private String email;

	@OneToMany(mappedBy = "id.maestroAliado")
	private List<MarcaActivadaVendedor> activaciones;

	// bi-directional many-to-one association to Existencia
	@OneToMany(mappedBy = "id.maestroAliado")
	private List<Existencia> existencias;

	// bi-directional many-to-one association to MaestroProducto
	@OneToMany(mappedBy = "maestroAliado")
	private List<MaestroProducto> maestroProductos;

	// bi-directional many-to-one association to MappingProducto
	@OneToMany(mappedBy = "id.maestroAliado")
	private List<MappingProducto> mappingProductos;

	// bi-directional many-to-one association to PlanVenta
	@OneToMany(mappedBy = "id.maestroAliado")
	private List<PlanVenta> planVentas;

	// bi-directional many-to-one association to Venta
	@OneToMany(mappedBy = "maestroAliado")
	private List<Venta> ventas;

	@OneToMany(mappedBy = "maestroAliado")
	private List<Cliente> clientes;

	public MaestroAliado() {
	}

	public String getCodigoAliado() {
		return this.codigoAliado;
	}

	public void setCodigoAliado(String codigoAliado) {
		this.codigoAliado = codigoAliado;
	}

	public String getCiudadAliado() {
		return this.ciudadAliado;
	}

	public void setCiudadAliado(String ciudadAliado) {
		this.ciudadAliado = ciudadAliado;
	}

	public String getCodigoPadre() {
		return this.codigoPadre;
	}

	public void setCodigoPadre(String codigoPadre) {
		this.codigoPadre = codigoPadre;
	}

	public String getCodigoVendedor() {
		return this.codigoVendedor;
	}

	public void setCodigoVendedor(String codigoVendedor) {
		this.codigoVendedor = codigoVendedor;
	}

	public String getDescripcionVendedor() {
		return this.descripcionVendedor;
	}

	public void setDescripcionVendedor(String descripcionVendedor) {
		this.descripcionVendedor = descripcionVendedor;
	}

	public String getDescripcionZona() {
		return this.descripcionZona;
	}

	public void setDescripcionZona(String descripcionZona) {
		this.descripcionZona = descripcionZona;
	}

	public String getEstadoAliado() {
		return this.estadoAliado;
	}

	public void setEstadoAliado(String estadoAliado) {
		this.estadoAliado = estadoAliado;
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

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipoCliente() {
		return this.tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public String getZona() {
		return this.zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public List<Existencia> getExistencias() {
		return this.existencias;
	}

	public void setExistencias(List<Existencia> existencias) {
		this.existencias = existencias;
	}

	// public Existencia addExistencia(Existencia existencia) {
	// getExistencias().add(existencia);
	// existencia.setMaestroAliado(this);
	//
	// return existencia;
	// }
	//
	// public Existencia removeExistencia(Existencia existencia) {
	// getExistencias().remove(existencia);
	// existencia.setMaestroAliado(null);
	//
	// return existencia;
	// }

	public List<MaestroProducto> getMaestroProductos() {
		return this.maestroProductos;
	}

	public void setMaestroProductos(List<MaestroProducto> maestroProductos) {
		this.maestroProductos = maestroProductos;
	}

	public MaestroProducto addMaestroProducto(MaestroProducto maestroProducto) {
		getMaestroProductos().add(maestroProducto);
		maestroProducto.setMaestroAliado(this);

		return maestroProducto;
	}

	public MaestroProducto removeMaestroProducto(MaestroProducto maestroProducto) {
		getMaestroProductos().remove(maestroProducto);
		maestroProducto.setMaestroAliado(null);

		return maestroProducto;
	}

	public List<MappingProducto> getMappingProductos() {
		return this.mappingProductos;
	}

	public void setMappingProductos(List<MappingProducto> mappingProductos) {
		this.mappingProductos = mappingProductos;
	}

	// public MappingProducto addMappingProducto(MappingProducto
	// mappingProducto) {
	// getMappingProductos().add(mappingProducto);
	// mappingProducto.setMaestroAliado(this);
	//
	// return mappingProducto;
	// }
	//
	// public MappingProducto removeMappingProducto(MappingProducto
	// mappingProducto) {
	// getMappingProductos().remove(mappingProducto);
	// mappingProducto.setMaestroAliado(null);
	//
	// return mappingProducto;
	// }

	public List<PlanVenta> getPlanVentas() {
		return this.planVentas;
	}

	public void setPlanVentas(List<PlanVenta> planVentas) {
		this.planVentas = planVentas;
	}

	// public PlanVenta addPlanVenta(PlanVenta planVenta) {
	// getPlanVentas().add(planVenta);
	// planVenta.setMaestroAliado(this);
	//
	// return planVenta;
	// }
	//
	// public PlanVenta removePlanVenta(PlanVenta planVenta) {
	// getPlanVentas().remove(planVenta);
	// planVenta.setMaestroAliado(null);
	//
	// return planVenta;
	// }

	public List<Venta> getVentas() {
		return this.ventas;
	}

	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}

	public Venta addVenta(Venta venta) {
		getVentas().add(venta);
		venta.setMaestroAliado(this);

		return venta;
	}

	public Venta removeVenta(Venta venta) {
		getVentas().remove(venta);
		venta.setMaestroAliado(null);

		return venta;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<MarcaActivadaVendedor> getActivaciones() {
		return activaciones;
	}

	public void setActivaciones(List<MarcaActivadaVendedor> activaciones) {
		this.activaciones = activaciones;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}