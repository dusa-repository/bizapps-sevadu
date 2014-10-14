package modelo.maestros;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Time;
import java.util.Date;


/**
 * The persistent class for the ventas database table.
 * 
 */
@Entity
@Table(name="ventas")
@NamedQuery(name="Venta.findAll", query="SELECT v FROM Venta v")
public class Venta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idrow;

	@Column(name="campo_aux1")
	private String campoAux1;

	@Column(name="campo_aux2")
	private String campoAux2;

	@Column(name="canal_ventas")
	private String canalVentas;

	private float cantidad;

	@Column(name="ciudad_aliado")
	private String ciudadAliado;

	@Column(name="codigo_cliente")
	private String codigoCliente;

	private String especie;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_auditoria")
	private Date fechaAuditoria;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_factura")
	private Date fechaFactura;

	@Column(name="hora_auditoria")
	private Time horaAuditoria;

	@Column(name="id_usuario")
	private String idUsuario;

	@Column(name="lote_upload")
	private String loteUpload;

	private String marca;

	@Column(name="nombre_cliente")
	private String nombreCliente;

	@Column(name="nombre_vendedor")
	private String nombreVendedor;

	@Column(name="numero_documento")
	private String numeroDocumento;

	@Column(name="ruta_distribucion")
	private String rutaDistribucion;

	@Column(name="unidad_medida")
	private String unidadMedida;

	@Column(name="zona_aliado")
	private String zonaAliado;

	//bi-directional many-to-one association to MaestroAliado
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="codigo_aliado")
	private MaestroAliado maestroAliado;

	//bi-directional many-to-one association to MaestroProducto
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "codigo_producto", referencedColumnName = "codigo_producto_dusa")
	private MaestroProducto maestroProducto;

	//bi-directional many-to-one association to TipoCliente
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="tipo_cliente",  referencedColumnName="codigo")
	private TipoCliente tipoClienteBean;

	public Venta() {
	}

	public int getIdrow() {
		return this.idrow;
	}

	public void setIdrow(int idrow) {
		this.idrow = idrow;
	}

	public String getCampoAux1() {
		return this.campoAux1;
	}

	public void setCampoAux1(String campoAux1) {
		this.campoAux1 = campoAux1;
	}

	public String getCampoAux2() {
		return this.campoAux2;
	}

	public void setCampoAux2(String campoAux2) {
		this.campoAux2 = campoAux2;
	}

	public String getCanalVentas() {
		return this.canalVentas;
	}

	public void setCanalVentas(String canalVentas) {
		this.canalVentas = canalVentas;
	}

	public float getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(float cantidad) {
		this.cantidad = cantidad;
	}

	public String getCiudadAliado() {
		return this.ciudadAliado;
	}

	public void setCiudadAliado(String ciudadAliado) {
		this.ciudadAliado = ciudadAliado;
	}

	public String getCodigoCliente() {
		return this.codigoCliente;
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public String getEspecie() {
		return this.especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public Date getFechaAuditoria() {
		return this.fechaAuditoria;
	}

	public void setFechaAuditoria(Date fechaAuditoria) {
		this.fechaAuditoria = fechaAuditoria;
	}

	public Date getFechaFactura() {
		return this.fechaFactura;
	}

	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
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

	public String getMarca() {
		return this.marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getNombreCliente() {
		return this.nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getNombreVendedor() {
		return this.nombreVendedor;
	}

	public void setNombreVendedor(String nombreVendedor) {
		this.nombreVendedor = nombreVendedor;
	}

	public String getNumeroDocumento() {
		return this.numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getRutaDistribucion() {
		return this.rutaDistribucion;
	}

	public void setRutaDistribucion(String rutaDistribucion) {
		this.rutaDistribucion = rutaDistribucion;
	}

	public String getUnidadMedida() {
		return this.unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public String getZonaAliado() {
		return this.zonaAliado;
	}

	public void setZonaAliado(String zonaAliado) {
		this.zonaAliado = zonaAliado;
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

	public TipoCliente getTipoClienteBean() {
		return this.tipoClienteBean;
	}

	public void setTipoClienteBean(TipoCliente tipoClienteBean) {
		this.tipoClienteBean = tipoClienteBean;
	}

}