package modelo.maestros;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "configuracion_envio_correo")
@NamedQuery(name = "ConfiguracionEnvioCorreo.findAll", query = "SELECT m FROM ConfiguracionEnvioCorreo m")
public class ConfiguracionEnvioCorreo implements Serializable {

	private static final long serialVersionUID = -3069229960849762983L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_configuracion_envio_correo", unique = true, nullable = false)
	private long idConfiguracion;

	@Column(length = 50, name = "id_reporte")
	private String reporte;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean estado;

	@Column(length = 1000)
	private String destinatarios;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_auditoria")
	private Date fechaAuditoria;

	@Column(name = "hora_auditoria")
	private Time horaAuditoria;

	@Column(name = "id_usuario")
	private String idUsuario;

	public ConfiguracionEnvioCorreo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConfiguracionEnvioCorreo(long idConfiguracion, String reporte,
			boolean estado, String destinatarios, Date fechaAuditoria,
			Time horaAuditoria, String idUsuario) {
		super();
		this.idConfiguracion = idConfiguracion;
		this.reporte = reporte;
		this.estado = estado;
		this.destinatarios = destinatarios;
		this.fechaAuditoria = fechaAuditoria;
		this.horaAuditoria = horaAuditoria;
		this.idUsuario = idUsuario;
	}

	public Date getFechaAuditoria() {
		return fechaAuditoria;
	}

	public void setFechaAuditoria(Date fechaAuditoria) {
		this.fechaAuditoria = fechaAuditoria;
	}

	public Time getHoraAuditoria() {
		return horaAuditoria;
	}

	public void setHoraAuditoria(Time horaAuditoria) {
		this.horaAuditoria = horaAuditoria;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public long getIdConfiguracion() {
		return idConfiguracion;
	}

	public void setIdConfiguracion(long idConfiguracion) {
		this.idConfiguracion = idConfiguracion;
	}

	public String getReporte() {
		return reporte;
	}

	public void setReporte(String reporte) {
		this.reporte = reporte;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(String destinatarios) {
		this.destinatarios = destinatarios;
	}

}
