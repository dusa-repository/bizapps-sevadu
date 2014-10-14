package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;
import org.zkoss.zul.Tab;

import servicio.seguridad.SArbol;
import servicio.seguridad.SGrupo;
import servicio.seguridad.SUsuario;

import componente.Mensaje;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public abstract class CGenerico extends SelectorComposer<Component> {

	private static final long serialVersionUID = -3701148488846104476L;

	@WireVariable("SArbol")
	protected SArbol servicioArbol;
	@WireVariable("SGrupo")
	protected SGrupo servicioGrupo;
	@WireVariable("SUsuario")
	protected SUsuario servicioUsuario;

	protected static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	protected static SimpleDateFormat formatoFechaRara = new SimpleDateFormat(
			"yyyyMMdd");
	public List<Tab> tabs = new ArrayList<Tab>();
	protected DateFormat df = new SimpleDateFormat("HH:mm:ss");
	public Calendar calendario = Calendar.getInstance();
	// Cambio en la hora borrados los :
	public String horaAuditoria = String.valueOf(calendario
			.get(Calendar.HOUR_OF_DAY))
			+ String.valueOf(calendario.get(Calendar.MINUTE))
			+ String.valueOf(calendario.get(Calendar.SECOND));
	public java.util.Date fecha = new Date();
	public Timestamp fechaHora = new Timestamp(fecha.getTime());
	public Mensaje msj = new Mensaje();
	public String cerrar;
	private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
			"/META-INF/PropiedadesBaseDatos.xml");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		inicializar();
	}

	public BigDecimal transformarGregorianoAJulia(Date fecha) {
		String valor = "";

		calendario = new GregorianCalendar();
		calendario.setTime(fecha);
		String dia = "";
		if (calendario.get(Calendar.DAY_OF_YEAR) < 10)
			dia = "00";
		else {
			if (calendario.get(Calendar.DAY_OF_YEAR) >= 10
					&& calendario.get(Calendar.DAY_OF_YEAR) < 100)
				dia = "0";
		}
		if ((fecha.getYear() + 1900) < 2000)
			valor = "";
		else
			valor = "1";
		long al = Long.valueOf(valor
				+ String.valueOf(calendario.get(Calendar.YEAR)).substring(2)
				+ dia + String.valueOf(calendario.get(Calendar.DAY_OF_YEAR)));
		BigDecimal a = BigDecimal.valueOf(al);
		return a;
	}

	public Date transformarJulianaAGregoria(BigDecimal valor) {
		String j = valor.toString();
		Date date = new Date();
		String primerValor = "";
		if (j.length() == 5) {
			try {
				date = new SimpleDateFormat("yyD").parse(j);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			primerValor = j.substring(0, 1);
			if (primerValor.equals("1")) {
				String anno = j.substring(1, 3);
				date.setYear(Integer.valueOf("20" + anno) - 1900);
				String s = j.substring(3);
				Date fecha = new Date();
				try {
					fecha = new SimpleDateFormat("D").parse(s);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fecha.setYear(date.getYear());
				return fecha;
			}
		}

		return date;

	}

	public Date transformarJulianaAGregoriadeLong(Long valor) {
		String j = valor.toString();
		Date date = new Date();
		String primerValor = "";
		if (j.length() == 5) {
			try {
				date = new SimpleDateFormat("yyD").parse(j);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			primerValor = j.substring(0, 1);
			if (primerValor.equals("1")) {
				String anno = j.substring(1, 3);
				date.setYear(Integer.valueOf("20" + anno) - 1900);
				String s = j.substring(3);
				Date fecha = new Date();
				try {
					fecha = new SimpleDateFormat("D").parse(s);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fecha.setYear(date.getYear());
				return fecha;
			}
		}

		return date;

	}

	public abstract void inicializar() throws IOException;

	public void cerrarVentana(Div div, String id, List<Tab> tabs2) {
		div.setVisible(false);
		tabs = tabs2;
		for (int i = 0; i < tabs.size(); i++) {
			if (tabs.get(i).getLabel().equals(id)) {
				if (i == (tabs.size() - 1) && tabs.size() > 1) {
					tabs.get(i - 1).setSelected(true);
				}
				tabs.get(i).onClose();
				tabs.remove(i);
			}
		}
	}

	public String nombreUsuarioSesion() {
		Authentication sesion = SecurityContextHolder.getContext()
				.getAuthentication();
		return sesion.getName();
	}

	public boolean enviarEmailNotificacion(String correo, String mensajes) {
		try {

			String cc = "CAMBIO DE CONTRASEÑA SEVADU";
			Properties props = new Properties();
			props.setProperty("mail.smtp.host", "172.23.20.66");
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.smtp.port", "2525");
			props.setProperty("mail.smtp.auth", "true");

			Authenticator auth = new SMTPAuthenticator();
			Session session = Session.getInstance(props, auth);
			String remitente = "cdusa@dusa.com.ve";
			String destino = correo;
			String mensaje = mensajes;
			String destinos[] = destino.split(",");
			Message message = new MimeMessage(session);

			message.setFrom(new InternetAddress(remitente));

			Address[] receptores = new Address[destinos.length];
			int j = 0;
			while (j < destinos.length) {
				receptores[j] = new InternetAddress(destinos[j]);
				j++;
			}

			message.addRecipients(Message.RecipientType.TO, receptores);
			message.setSubject(cc);
			message.setText(mensaje);

			Transport.send(message);

			return true;
		}

		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Date traerFech(String fechaString) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

		Date fecha = null;
		try {
			fecha = formatter.parse(fechaString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fecha;
	}

	public String damePath() {
		return Executions.getCurrent().getContextPath() + "/";
	}

	public List<String> obtenerPropiedades() {
		List<String> arreglo = new ArrayList<String>();
		DriverManagerDataSource ds = (DriverManagerDataSource) applicationContext
				.getBean("dataSource");
		arreglo.add(ds.getUsername());
		arreglo.add(ds.getPassword());
		arreglo.add(ds.getUrl());
		return arreglo;
	}
	
	class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication("cdusa", "cartucho");
		}
	}
}