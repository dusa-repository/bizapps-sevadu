package modelo.maestros;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="ventas_dusa")
@NamedQuery(name="VentaDusa.findAll", query="SELECT v FROM VentaDusa v")
public class VentaDusa implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idrow;

	//bi-directional many-to-one association to MaestroAliado
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="codigo_aliado")
	private MaestroAliado maestroAliado;

	//bi-directional many-to-one association to MaestroProducto
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "codigo_producto", referencedColumnName = "codigo_producto_dusa")
	private MaestroProducto maestroProducto;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha")
	private Date fecha;

	@Column(name="unidad_medida")
	private String unidadMedida;
	
	private Float cantidad;

	private Float precio;

	public VentaDusa() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getIdrow() {
		return idrow;
	}

	public void setIdrow(int idrow) {
		this.idrow = idrow;
	}

	public MaestroAliado getMaestroAliado() {
		return maestroAliado;
	}

	public void setMaestroAliado(MaestroAliado maestroAliado) {
		this.maestroAliado = maestroAliado;
	}

	public MaestroProducto getMaestroProducto() {
		return maestroProducto;
	}

	public void setMaestroProducto(MaestroProducto maestroProducto) {
		this.maestroProducto = maestroProducto;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public Float getCantidad() {
		return cantidad;
	}

	public void setCantidad(Float cantidad) {
		this.cantidad = cantidad;
	}

	public Float getPrecio() {
		return precio;
	}

	public void setPrecio(Float precio) {
		this.precio = precio;
	}
	
}
