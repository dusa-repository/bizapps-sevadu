package modelo.maestros;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the marca_activada_vendedor database table.
 * 
 */
@Entity
@Table(name="marca_activada_vendedor")
@NamedQuery(name="MarcaActivadaVendedor.findAll", query="SELECT m FROM MarcaActivadaVendedor m")
public class MarcaActivadaVendedor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private float cantidad;

	@Column(name="codigo_aliado")
	private String codigoAliado;

	@Column(name="nombre_vendedor")
	private String nombreVendedor;

	public MarcaActivadaVendedor() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(float cantidad) {
		this.cantidad = cantidad;
	}

	public String getCodigoAliado() {
		return this.codigoAliado;
	}

	public void setCodigoAliado(String codigoAliado) {
		this.codigoAliado = codigoAliado;
	}

	public String getNombreVendedor() {
		return this.nombreVendedor;
	}

	public void setNombreVendedor(String nombreVendedor) {
		this.nombreVendedor = nombreVendedor;
	}

}