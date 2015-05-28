package modelo.bitacora;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "tmp_control_upload")
@NamedQuery(name = "ControlUpdate.findAll", query = "SELECT m FROM ControlUpdate m")
public class ControlUpdate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ukid", unique = true, nullable = false)
	private Integer id;

	@Column(name = "codigo_aliado", length = 50)
	private String codigoAliado;

	@Column(name = "nombre_aliado", length = 1000)
	private String nombreAliado;

	@Column(name = "ventas", length = 50)
	private String ventas;

	@Column(name = "plan_ventas", length = 50)
	private String planVentas;

	@Column(name = "existencia", length = 50)
	private String existencia;

	@Column(name = "cartera_clientes", length = 50)
	private String carteraClientes;

	@Column(name = "pvp", length = 50)
	private String pvp;

	@Column(name = "activacion", length = 50)
	private String activacion;

	@Column(name = "mapping", length = 50)
	private String mapping;

	@Column(name = "fecha")
	private Date fecha;

	public ControlUpdate() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ControlUpdate(Integer id, String codigoAliado, String nombreAliado,
			String ventas, String planVentas, String existencia,
			String carteraClientes, String pvp, String activacion, Date fecha, String mapping) {
		super();
		this.id = id;
		this.codigoAliado = codigoAliado;
		this.nombreAliado = nombreAliado;
		this.ventas = ventas;
		this.planVentas = planVentas;
		this.existencia = existencia;
		this.carteraClientes = carteraClientes;
		this.pvp = pvp;
		this.activacion = activacion;
		this.fecha = fecha;
		this.mapping = mapping;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigoAliado() {
		return codigoAliado;
	}

	public void setCodigoAliado(String codigoAliado) {
		this.codigoAliado = codigoAliado;
	}

	public String getNombreAliado() {
		return nombreAliado;
	}

	public void setNombreAliado(String nombreAliado) {
		this.nombreAliado = nombreAliado;
	}

	public String getVentas() {
		return ventas;
	}

	public void setVentas(String ventas) {
		this.ventas = ventas;
	}

	public String getPlanVentas() {
		return planVentas;
	}

	public void setPlanVentas(String planVentas) {
		this.planVentas = planVentas;
	}

	public String getExistencia() {
		return existencia;
	}

	public void setExistencia(String existencia) {
		this.existencia = existencia;
	}

	public String getCarteraClientes() {
		return carteraClientes;
	}

	public void setCarteraClientes(String carteraClientes) {
		this.carteraClientes = carteraClientes;
	}

	public String getPvp() {
		return pvp;
	}

	public void setPvp(String pvp) {
		this.pvp = pvp;
	}

	public String getActivacion() {
		return activacion;
	}

	public void setActivacion(String activacion) {
		this.activacion = activacion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getMapping() {
		return mapping;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

}
