package modelo.maestros;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


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

}