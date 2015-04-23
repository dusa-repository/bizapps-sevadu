package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
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
import java.util.TimeZone;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import modelo.maestros.MaestroAliado;
import modelo.seguridad.Usuario;

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
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Tab;

import servicio.bitacora.SBitacoraEliminacion;
import servicio.bitacora.SBitacoraLogin;
import servicio.maestros.SCliente;
import servicio.maestros.SConfiguracion;
import servicio.maestros.SConfiguracionEnvioCorreo;
import servicio.maestros.SExistencia;
import servicio.maestros.SF0004;
import servicio.maestros.SF0005;
import servicio.maestros.SMaestroAliado;
import servicio.maestros.SMaestroMarca;
import servicio.maestros.SMaestroProducto;
import servicio.maestros.SMappingProducto;
import servicio.maestros.SMarcaActivadaVendedor;
import servicio.maestros.SPlanVenta;
import servicio.maestros.STipoCliente;
import servicio.maestros.SVenta;
import servicio.seguridad.SArbol;
import servicio.seguridad.SGrupo;
import servicio.seguridad.SUsuario;
import servicio.termometro.STermometro;
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
	@WireVariable("SF0004")
	protected SF0004 servicioF0004;
	@WireVariable("SF0005")
	protected SF0005 servicioF0005;
	@WireVariable("SConfiguracionEnvioCorreo")
	protected SConfiguracionEnvioCorreo servicioEnvio;
	@WireVariable("SExistencia")
	protected SExistencia servicioExistencia;
	@WireVariable("SMaestroAliado")
	protected SMaestroAliado servicioAliado;
	@WireVariable("SMaestroMarca")
	protected SMaestroMarca servicioMarca;
	@WireVariable("SMaestroProducto")
	protected SMaestroProducto servicioProducto;
	@WireVariable("SMappingProducto")
	protected SMappingProducto servicioMapping;
	@WireVariable("SMarcaActivadaVendedor")
	protected SMarcaActivadaVendedor servicioMarcaActivada;
	@WireVariable("SPlanVenta")
	protected SPlanVenta servicioPlan;
	@WireVariable("SVenta")
	protected SVenta servicioVenta;
	@WireVariable("STipoCliente")
	protected STipoCliente servicioTipoCliente;
	@WireVariable("SConfiguracion")
	protected SConfiguracion servicioConfiguracion;
	@WireVariable("SCliente")
	protected SCliente servicioCliente;
	@WireVariable("SBitacoraEliminacion")
	protected SBitacoraEliminacion servicioBitacoraEliminacion;
	@WireVariable("SBitacoraLogin")
	protected SBitacoraLogin servicioBitacoraLogin;
	@WireVariable("STermometro")
	protected STermometro servicioTermometro;
	private static ApplicationContext app = new ClassPathXmlApplicationContext(
			"/META-INF/ConfiguracionAplicacion.xml");

	protected static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	protected static SimpleDateFormat formatoFechaRara = new SimpleDateFormat(
			"yyyyMMdd");
	public List<Tab> tabs = new ArrayList<Tab>();
	public Groupbox grxGraficoGeneral;
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
	public Time tiempo = new Time(fecha.getTime());
	private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
			"/META-INF/PropiedadesBaseDatos.xml");

	public static SMaestroAliado getServicioAliado() {
		return app.getBean(SMaestroAliado.class);
	}

	public static SCliente getServicioCliente() {
		return app.getBean(SCliente.class);
	}

	public static SVenta getServicioVenta() {
		return app.getBean(SVenta.class);
	}

	public static SConfiguracion getServicioConfiguracion() {
		return app.getBean(SConfiguracion.class);
	}

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

	public void cerrarVentana(Div div, String id, List<Tab> tabs2,
			Groupbox group) {
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
		if (tabs.size() == 0) {
			if (group.isVisible() && !group.isOpen()) {
				group.setOpen(true);
			}
		}
	}

	public String nombreUsuarioSesion() {
		Authentication sesion = SecurityContextHolder.getContext()
				.getAuthentication();
		return sesion.getName();
	}

	protected boolean enviarEmailNotificacionEliminacion(MaestroAliado aliado,
			String tablaEliminada, Date fecha, Usuario user) {
		try {

			String cc = "NOTIFICACION DEL SISTEMA SEVADU";
			Properties props = new Properties();
			props.setProperty("mail.smtp.host", "172.23.20.66");
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.smtp.port", "2525");
			props.setProperty("mail.smtp.auth", "true");

			Authenticator auth = new SMTPAuthenticator();
			Session session = Session.getInstance(props, auth);
			String remitente = "cdusa@dusa.com.ve";
			String destino = aliado.getEmail();
			String mensaje = "Se ha eliminado informacion de la tabla: "
					+ tablaEliminada
					+ " ,asociada a su aliado dentro del sistema, dicha "
					+ "accion fue realizada por " + user.getPrimerApellido()
					+ " " + user.getPrimerNombre() + ". El dia "
					+ formatoFecha.format(fecha) + " a las: " + horaAuditoria;
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

	public boolean enviarEmailNotificacion(String correo, String mensajes) {
		try {

			String cc = "NOTIFICACION DEL SISTEMA SEVADU";
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

	public String diaSemanaString(Calendar calendar) {
		int dia = calendar.get(Calendar.DAY_OF_WEEK);
		String diaSemana = "";
		switch (dia) {
		case 2:
			diaSemana = "Lunes";
			break;
		case 3:
			diaSemana = "Martes";
			break;

		case 4:
			diaSemana = "Miercoles";
			break;
		case 5:
			diaSemana = "Jueves";
			break;
		case 6:
			diaSemana = "Viernes";
			break;
		case 7:
			diaSemana = "Sabado";
			break;
		case 1:
			diaSemana = "Domingo";
			break;
		}
		return diaSemana;
	}

	public String mesString(Calendar calendar) {
		int dia = calendar.get(Calendar.MONTH);
		String diaSemana = "";
		switch (dia) {
		case 0:
			diaSemana = "Enero";
			break;
		case 1:
			diaSemana = "Febrero";
			break;
		case 2:
			diaSemana = "Marzo";
			break;
		case 3:
			diaSemana = "Abril";
			break;
		case 4:
			diaSemana = "Mayo";
			break;
		case 5:
			diaSemana = "Junio";
			break;
		case 6:
			diaSemana = "Julio";
			break;
		case 7:
			diaSemana = "Agosto";
			break;
		case 8:
			diaSemana = "Septiembre";
			break;
		case 9:
			diaSemana = "Octubre";
			break;
		case 10:
			diaSemana = "Noviembre";
			break;
		case 11:
			diaSemana = "Diciembre";
			break;
		}
		return diaSemana;
	}

	public int obtenerIntDadoString(String value) {
		int valor = 0;
		switch (value) {
		case "Enero":
			valor = 1;
			break;
		case "Febrero":
			valor = 2;
			break;
		case "Marzo":
			valor = 3;
			break;
		case "Abril":
			valor = 4;
			break;
		case "Mayo":
			valor = 5;
			break;
		case "Junio":
			valor = 6;
			break;
		case "Julio":
			valor = 7;
			break;
		case "Agosto":
			valor = 8;
			break;
		case "Septiembre":
			valor = 9;
			break;
		case "Octubre":
			valor = 10;
			break;
		case "Noviembre":
			valor = 11;
			break;
		case "Diciembre":
			valor = 12;
			break;
		}
		return valor;
	}

	protected int obtenerMes(int tiempo) {
		switch (tiempo) {
		case 0:
			tiempo = 12;
			break;
		case -1:
			tiempo = 11;
			break;
		case -2:
			tiempo = 10;
			break;
		case -3:
			tiempo = 9;
			break;
		case -4:
			tiempo = 8;
			break;
		case -5:
			tiempo = 7;
			break;
		case -6:
			tiempo = 6;
			break;
		case -7:
			tiempo = 5;
			break;
		case -8:
			tiempo = 4;
			break;
		case -9:
			tiempo = 3;
			break;
		case -10:
			tiempo = 2;
			break;
		case -11:
			tiempo = 1;
			break;
		default:
			break;
		}
		return tiempo;
	}

	public String mounthStringGivenIntegerValue(int mes) {
		String mounth = "";
		switch (mes) {
		case 1:
			mounth = "Enero";
			break;
		case 2:
			mounth = "Febrero";
			break;
		case 3:
			mounth = "Marzo";
			break;
		case 4:
			mounth = "Abril";
			break;
		case 5:
			mounth = "Mayo";
			break;
		case 6:
			mounth = "Junio";
			break;
		case 7:
			mounth = "Julio";
			break;
		case 8:
			mounth = "Agosto";
			break;
		case 9:
			mounth = "Septiembre";
			break;
		case 10:
			mounth = "Octubre";
			break;
		case 11:
			mounth = "Noviembre";
			break;
		case 12:
			mounth = "Diciembre";
			break;
		}
		return mounth;
	}

	public int obtenerDiasHabiles(Date fecha, Date fecha2) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
		calendario.setTime(fecha2);
		calendario.add(Calendar.DAY_OF_YEAR, +1);
		calendario.set(Calendar.HOUR, 0);
		calendario.set(Calendar.HOUR_OF_DAY, 0);
		calendario.set(Calendar.SECOND, 0);
		calendario.set(Calendar.MILLISECOND, 0);
		calendario.set(Calendar.MINUTE, 0);
		fecha2 = calendario.getTime();
		calendario.setTime(fecha);
		String fija = formatoFecha.format(fecha2);
		String hoy = "";
		int contador = 0;
		do {
			calendario.setTime(fecha);
			if (calendario.get(Calendar.DAY_OF_WEEK) != 1
					&& calendario.get(Calendar.DAY_OF_WEEK) != 7)
				contador++;
			calendario.add(Calendar.DAY_OF_YEAR, +1);
			fecha = calendario.getTime();
			hoy = formatoFecha.format(fecha);
		} while (!hoy.equals(fija));
		return contador;
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();
		Double valor = value;
		BigDecimal bd = new BigDecimal(value);
		if (!valor.isNaN() && !valor.isInfinite()) {
			bd = new BigDecimal(value);
			bd = bd.setScale(places, RoundingMode.HALF_UP);
		}
		return bd.doubleValue();
	}

	public String lastDay(Date fechaHoy) {
		DateFormat formatoNuevo = new SimpleDateFormat("MM");
		String month = formatoNuevo.format(fechaHoy);
		String last = "30-";
		switch (month) {
		case "01":
		case "03":
		case "05":
		case "07":
		case "08":
		case "10":
		case "12":
			last = "31-";
			break;
		case "02":
			last = "28-";
			break;
		default:
			break;
		}
		return last;
	}

}