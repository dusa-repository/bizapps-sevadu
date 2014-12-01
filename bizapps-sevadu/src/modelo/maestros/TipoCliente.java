package modelo.maestros;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the tipo_cliente database table.
 * 
 */
@Entity
@Table(name="tipo_cliente")
@NamedQuery(name="TipoCliente.findAll", query="SELECT t FROM TipoCliente t")
public class TipoCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String codigo;

	@Column(name="canal_ventas")
	private String canalVentas;

	private String descripcion;

	//bi-directional many-to-one association to Venta
	@OneToMany(mappedBy="tipoClienteBean")
	private List<Venta> ventas;
	
	@OneToMany(mappedBy="tipoCliente")
	private List<Cliente> clientes;

	public TipoCliente() {
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCanalVentas() {
		return this.canalVentas;
	}

	public void setCanalVentas(String canalVentas) {
		this.canalVentas = canalVentas;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Venta> getVentas() {
		return this.ventas;
	}

	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}

	public Venta addVenta(Venta venta) {
		getVentas().add(venta);
		venta.setTipoClienteBean(this);

		return venta;
	}

	public Venta removeVenta(Venta venta) {
		getVentas().remove(venta);
		venta.setTipoClienteBean(null);

		return venta;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

}