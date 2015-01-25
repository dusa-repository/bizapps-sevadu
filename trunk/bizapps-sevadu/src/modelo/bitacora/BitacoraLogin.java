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

import modelo.seguridad.Usuario;

@Entity
@Table(name = "bitacora_login")
@NamedQuery(name = "BitacoraLogin.findAll", query = "SELECT m FROM BitacoraLogin m")
public class BitacoraLogin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8293438454758355194L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_bitacora_login", unique = true, nullable = false)
	private long idLogin;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@Column(name = "direccion_ip", length = 100)
	private String direccionIp;

	@Column(name = "fecha_ingreso")
	private Date fechaIngreso;

	@Column(name = "hora_ingreso", length = 10)
	private Time horaIngreso;

	@Column(name = "fecha_egreso")
	private Date fechaEgreso;

	@Column(name = "hora_egreso", length = 10)
	private Time horaEgreso;

	public BitacoraLogin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BitacoraLogin(long idLogin, Usuario usuario, String direccionIp,
			Date fechaIngreso, Time horaIngreso, Date fechaEgreso,
			Time horaEgreso) {
		super();
		this.idLogin = idLogin;
		this.usuario = usuario;
		this.direccionIp = direccionIp;
		this.fechaIngreso = fechaIngreso;
		this.horaIngreso = horaIngreso;
		this.fechaEgreso = fechaEgreso;
		this.horaEgreso = horaEgreso;
	}

	public long getIdLogin() {
		return idLogin;
	}

	public void setIdLogin(long idLogin) {
		this.idLogin = idLogin;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getDireccionIp() {
		return direccionIp;
	}

	public void setDireccionIp(String direccionIp) {
		this.direccionIp = direccionIp;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Time getHoraIngreso() {
		return horaIngreso;
	}

	public void setHoraIngreso(Time horaIngreso) {
		this.horaIngreso = horaIngreso;
	}

	public Date getFechaEgreso() {
		return fechaEgreso;
	}

	public void setFechaEgreso(Date fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}

	public Time getHoraEgreso() {
		return horaEgreso;
	}

	public void setHoraEgreso(Time horaEgreso) {
		this.horaEgreso = horaEgreso;
	}

}
