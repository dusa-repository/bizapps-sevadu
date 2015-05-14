package modelo.maestros;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the configuracion database table.
 * 
 */
@Entity
@NamedQuery(name="Configuracion.findAll", query="SELECT c FROM Configuracion c")
public class Configuracion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.DATE)
	@Column(name="inicio_fy_actual")
	private Date inicioFyActual;

	@Column(name = "porcentaje")
	private Float porcentaje;

	@Column(name = "meses_a_borrar")
	private Integer mes;

	public Configuracion() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getInicioFyActual() {
		return this.inicioFyActual;
	}

	public void setInicioFyActual(Date inicioFyActual) {
		this.inicioFyActual = inicioFyActual;
	}

	public Float getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Float porcentaje) {
		this.porcentaje = porcentaje;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer integer) {
		this.mes = integer;
	}

}