package modelo.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroProducto;

@Embeddable
public class PlanVentaPK implements Serializable {
	private static final long serialVersionUID = 3476160652251705212L;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_aliado")
	private MaestroAliado maestroAliado;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_producto", referencedColumnName = "codigo_producto_dusa")
	private MaestroProducto maestroProducto;

	private int anno;

	private int mes;

	@Column(name = "zona_aliado")
	private String zonaAliado;

	@Column(name = "vendedor_aliado")
	private String vendedorAliado;

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


	public int getAnno() {
		return anno;
	}


	public void setAnno(int anno) {
		this.anno = anno;
	}


	public int getMes() {
		return mes;
	}


	public void setMes(int mes) {
		this.mes = mes;
	}


	public String getZonaAliado() {
		return zonaAliado;
	}


	public void setZonaAliado(String zonaAliado) {
		this.zonaAliado = zonaAliado;
	}


	public String getVendedorAliado() {
		return vendedorAliado;
	}


	public void setVendedorAliado(String vendedorAliado) {
		this.vendedorAliado = vendedorAliado;
	}
	
	

}
