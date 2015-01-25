package modelo.bitacora;

import java.io.Serializable;
import java.sql.Time;
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

import modelo.maestros.MaestroAliado;
import modelo.seguridad.Usuario;

@Entity
@Table(name = "bitacora_eliminacion")
@NamedQuery(name = "BitacoraEliminacion.findAll", query = "SELECT m FROM BitacoraEliminacion m")
public class BitacoraEliminacion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2036825452840733270L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_bitacora_eliminacion", unique = true, nullable = false)
	private long idEliminacion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@Column(name = "fecha_eliminacion")
	private Date fechaEliminacion;

	@Column(name = "hora_eliminacion", length = 10)
	private Time horaEliminacion;

	@Column(name = "inico_periodo_eliminacion")
	private Date fechaInicio;

	@Column(name = "fin_periodo_eliminacion")
	private Date fechaFin;

	@Column(name = "aliado_afectado",length = 100)
	private String aliadoAfectado;

	@Column(name = "tabla_afectada",length = 100)
	private String tablaAfectada;

	public BitacoraEliminacion() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BitacoraEliminacion(long idEliminacion, Usuario usuario,
			Date fechaEliminacion, Time horaEliminacion, Date fechaInicio,
			Date fechaFin, String aliadoAfectado, String tablaAfectada) {
		super();
		this.idEliminacion = idEliminacion;
		this.usuario = usuario;
		this.fechaEliminacion = fechaEliminacion;
		this.horaEliminacion = horaEliminacion;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.aliadoAfectado = aliadoAfectado;
		this.tablaAfectada = tablaAfectada;
	}

	public long getIdEliminacion() {
		return idEliminacion;
	}

	public void setIdEliminacion(long idEliminacion) {
		this.idEliminacion = idEliminacion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getFechaEliminacion() {
		return fechaEliminacion;
	}

	public void setFechaEliminacion(Date fechaEliminacion) {
		this.fechaEliminacion = fechaEliminacion;
	}

	public Time getHoraEliminacion() {
		return horaEliminacion;
	}

	public void setHoraEliminacion(Time horaEliminacion) {
		this.horaEliminacion = horaEliminacion;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getAliadoAfectado() {
		return aliadoAfectado;
	}

	public void setAliadoAfectado(String aliadoAfectado) {
		this.aliadoAfectado = aliadoAfectado;
	}

	public String getTablaAfectada() {
		return tablaAfectada;
	}

	public void setTablaAfectada(String tablaAfectada) {
		this.tablaAfectada = tablaAfectada;
	}
	
}
