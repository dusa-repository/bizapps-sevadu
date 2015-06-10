package controlador.mantenimiento;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.bitacora.BitacoraEliminacion;
import modelo.bitacora.BitacoraLogin;
import modelo.seguridad.Usuario;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import componente.Botonera;
import componente.Catalogo;
import componente.Mensaje;

import controlador.maestros.CGenerico;

public class CConsultarBitacora extends CGenerico {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Textbox txtUsuario;
	@Wire
	private Label lblUsuario;
	@Wire
	private Div botoneraConsultar;
	@Wire
	private Div divCatalogoUsuario;
	@Wire
	private Div catalogoDelete;
	@Wire
	private Div catalogoLogin;
	@Wire
	private Combobox cmbReporte;
	@Wire
	private Datebox dtbDesde;
	@Wire
	private Datebox dtbHasta;
	List<BitacoraLogin> listaLoggin = new ArrayList<BitacoraLogin>();
	List<BitacoraEliminacion> listaEliminacion = new ArrayList<BitacoraEliminacion>();
	Catalogo<Usuario> catalogoUsuario;
	Catalogo<BitacoraLogin> catalogoLoggeo;
	Catalogo<BitacoraEliminacion> catalogoEliminacion;
	Botonera botonera;
	String idUser = "TODOS";

	@Override
	public void inicializar() throws IOException {
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (map != null) {
			if (map.get("tabsGenerales") != null) {
				tabs = (List<Tab>) map.get("tabsGenerales");
				cerrar = (String) map.get("titulo");
				grxGraficoGeneral = (Groupbox) map.get("grxGraficoGeneral");
				map.clear();
				map = null;
			}
		}
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub

			}

			@Override
			public void salir() {
				cerrarVentana(divCatalogoUsuario, cerrar, tabs,
						grxGraficoGeneral);
			}

			@Override
			public void reporte() {
				// TODO Auto-generated method stub

			}

			@Override
			public void limpiar() {
				txtUsuario.setValue("TODOS");
				lblUsuario.setValue("TODOS");
				dtbDesde.setValue(fecha);
				dtbHasta.setValue(fecha);
				idUser = "TODOS";
			}

			@Override
			public void guardar() {
				String user = "%";
				if (!idUser.equals("TODOS"))
					user = idUser;
				Date desde = dtbDesde.getValue();
				Date hasta = dtbHasta.getValue();
				Calendar calendario = Calendar.getInstance();
				calendario.setTime(hasta);
				calendario.set(Calendar.HOUR, 11);
				calendario.set(Calendar.HOUR_OF_DAY, 23);
				calendario.set(Calendar.SECOND, 59);
				calendario.set(Calendar.MILLISECOND, 59);
				calendario.set(Calendar.MINUTE, 59);
				hasta = calendario.getTime();
				if (cmbReporte.getValue().equals("Ingreso al Sistema")) {
					listaLoggin = servicioBitacoraLogin.buscarPorUsarioYFechas(
							user, desde, hasta);
					catalogoDelete.setVisible(false);
					if (catalogoLoggeo != null) {
						catalogoLogin.setVisible(true);
						catalogoLoggeo.actualizarLista(listaLoggin, true);
					} else
						mostrarCatalogoLogin();
				} else {
					listaEliminacion = servicioBitacoraEliminacion
							.buscarPorUsarioYFechas(user, desde, hasta);
					catalogoLogin.setVisible(false);
					if (catalogoEliminacion != null) {
						catalogoDelete.setVisible(true);
						catalogoEliminacion.actualizarLista(listaEliminacion,
								true);
					} else
						mostrarCatalogoEliminacion();
				}
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub

			}

			@Override
			public void buscar() {
				// TODO Auto-generated method stub

			}

			@Override
			public void ayuda() {
				// TODO Auto-generated method stub

			}

			@Override
			public void annadir() {
				// TODO Auto-generated method stub

			}
		};
		Button guardar = (Button) botonera.getChildren().get(3);
		guardar.setLabel("Buscar");
		guardar.setImage("/public/imagenes/botones/buscar.png");
		botonera.getChildren().get(0).setVisible(false);
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(2).setVisible(false);
		botonera.getChildren().get(4).setVisible(false);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botoneraConsultar.appendChild(botonera);
	}

	protected void mostrarCatalogoEliminacion() {
		catalogoEliminacion = new Catalogo<BitacoraEliminacion>(catalogoDelete,
				"Eliminar", listaEliminacion, false, false, false, "Usuario",
				"Fecha Eliminacion", "Hora Eliminacion", "Desde", "Hasta",
				"Aliado Afectado", "Tabla") {
			private static final long serialVersionUID = -4075514893997896082L;

			@Override
			protected List<BitacoraEliminacion> buscar(List<String> valores) {

				List<BitacoraEliminacion> lista = new ArrayList<BitacoraEliminacion>();

				for (BitacoraEliminacion objeto : listaEliminacion) {
					if (objeto.getUsuario().getLogin()
							.contains(valores.get(0).toLowerCase())
							&& formatoFecha
									.format(objeto.getFechaEliminacion())
									.toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& df.format(objeto.getHoraEliminacion())
									.toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& formatoFecha.format(objeto.getFechaInicio())
									.toLowerCase()
									.contains(valores.get(3).toLowerCase())
							&& formatoFecha.format(objeto.getFechaFin())
									.toLowerCase()
									.contains(valores.get(4).toLowerCase())
							&& objeto.getAliadoAfectado().toLowerCase()
									.contains(valores.get(5).toLowerCase())
							&& objeto.getTablaAfectada().toLowerCase()
									.contains(valores.get(6).toLowerCase())) {
						lista.add(objeto);
					}
				}
				return lista;
			}

			@Override
			protected String[] crearRegistros(BitacoraEliminacion objeto) {
				String[] registros = new String[7];
				registros[0] = objeto.getUsuario().getLogin();
				registros[1] = formatoFecha
						.format(objeto.getFechaEliminacion());
				registros[2] = df.format(objeto.getHoraEliminacion());
				registros[3] = formatoFecha.format(objeto.getFechaInicio());
				registros[4] = formatoFecha.format(objeto.getFechaFin());
				registros[5] = objeto.getAliadoAfectado();
				registros[6] = objeto.getTablaAfectada();
				return registros;
			}
		};
		catalogoDelete.setVisible(true);
		catalogoEliminacion.setParent(catalogoDelete);
	}

	protected void mostrarCatalogoLogin() {
		catalogoLoggeo = new Catalogo<BitacoraLogin>(catalogoLogin, "Logger",
				listaLoggin, false, false, false, "Usuario", "Fecha Ingreso",
				"Hora Ingreso", "Fecha Egreso", "Hora Egreso", "Direccion IP") {
			private static final long serialVersionUID = -4075514893997896082L;

			@Override
			protected List<BitacoraLogin> buscar(List<String> valores) {
				List<BitacoraLogin> lista = new ArrayList<BitacoraLogin>();
				for (BitacoraLogin objeto : listaLoggin) {
					String fecha = "";
					if (objeto.getFechaEgreso() != null)
						fecha = formatoFecha.format(objeto.getFechaEgreso());
					String hora = "";
					if (objeto.getHoraEgreso() != null)
						hora = df.format(objeto.getHoraEgreso());
					if (objeto.getUsuario().getLogin().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& formatoFecha.format(objeto.getFechaIngreso())
									.toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& df.format(objeto.getHoraIngreso()).toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& fecha.toLowerCase().contains(
									valores.get(3).toLowerCase())
							&& hora.toLowerCase().contains(
									valores.get(4).toLowerCase())
							&& objeto.getDireccionIp().toLowerCase()
									.contains(valores.get(5).toLowerCase())) {
						lista.add(objeto);
					}
				}
				return lista;
			}

			@Override
			protected String[] crearRegistros(BitacoraLogin objeto) {
				String fecha = "";
				if (objeto.getFechaEgreso() != null)
					fecha = formatoFecha.format(objeto.getFechaEgreso());
				String hora = "";
				if (objeto.getHoraEgreso() != null)
					hora = df.format(objeto.getHoraEgreso());
				String[] registros = new String[6];
				registros[0] = objeto.getUsuario().getLogin();
				registros[1] = formatoFecha.format(objeto.getFechaIngreso());
				registros[2] = df.format(objeto.getHoraIngreso());
				registros[3] = fecha;
				registros[4] = hora;
				registros[5] = objeto.getDireccionIp();
				return registros;
			}
		};
		catalogoLogin.setVisible(true);
		catalogoLoggeo.setParent(catalogoLogin);
	}

	@Listen("onClick = #btnBuscarUsuario")
	public void mostrarCatalogo() {
		Usuario usuario = new Usuario();
		usuario.setLogin("TODOS");
		usuario.setCedula("TODOS");
		usuario.setEmail("TODOS");
		usuario.setPrimerNombre("TODOS");
		usuario.setPrimerApellido("TODOS");
		usuario.setSexo("TODOS");
		usuario.setTelefono("TODOS");
		List<Usuario> usuarios = new ArrayList<Usuario>();
		usuarios.add(usuario);
		usuarios.addAll(servicioUsuario.buscarTodos());
		final List<Usuario> users = usuarios;
		catalogoUsuario = new Catalogo<Usuario>(divCatalogoUsuario, "Usuario",
				users, true, false, false, "Login", "Cedula", "Correo",
				"Primer Nombre", "Primer Apellido", "Sexo", "Telefono") {

			@Override
			protected List<Usuario> buscar(List<String> valores) {

				List<Usuario> user = new ArrayList<Usuario>();

				for (Usuario actividadord : users) {
					if (actividadord.getLogin().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& actividadord.getCedula().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& actividadord.getEmail().toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& actividadord.getPrimerNombre().toLowerCase()
									.contains(valores.get(3).toLowerCase())
							&& actividadord.getPrimerApellido().toLowerCase()
									.contains(valores.get(4).toLowerCase())
							&& actividadord.getSexo().toLowerCase()
									.contains(valores.get(5).toLowerCase())
							&& actividadord.getTelefono().toLowerCase()
									.contains(valores.get(6).toLowerCase())) {
						user.add(actividadord);
					}
				}
				return user;
			}

			@Override
			protected String[] crearRegistros(Usuario usuarios) {
				String[] registros = new String[7];
				registros[0] = usuarios.getLogin();
				registros[1] = usuarios.getCedula();
				registros[2] = usuarios.getEmail();
				registros[3] = usuarios.getPrimerNombre();
				registros[4] = usuarios.getPrimerApellido();
				registros[5] = usuarios.getSexo();
				registros[6] = usuarios.getTelefono();
				return registros;
			}

		};
		catalogoUsuario.setClosable(true);
		catalogoUsuario.setWidth("80%");
		catalogoUsuario.setParent(divCatalogoUsuario);
		catalogoUsuario.doModal();
	}

	@Listen("onSeleccion = #divCatalogoUsuario")
	public void seleccionAliado() {
		Usuario aliado = catalogoUsuario.objetoSeleccionadoDelCatalogo();
		llenarCamposUser(aliado);
		catalogoUsuario.setParent(null);
	}

	private void llenarCamposUser(Usuario aliado) {
		txtUsuario.setValue(aliado.getLogin());
		lblUsuario.setValue(aliado.getPrimerNombre() + " "
				+ aliado.getPrimerApellido());
		idUser = aliado.getLogin();
	}

	/* Busca si existe un usuario con el mismo login */
	@Listen("onChange = #txtUsuario; onOK = #txtUsuario")
	public void buscarPorLogin() {
		Usuario usuario = servicioUsuario.buscarPorLogin(txtUsuario.getValue());
		if (usuario != null)
			llenarCamposUser(usuario);
		else {
			msj.mensajeAlerta(Mensaje.noHayRegistros);
			idUser = "TODOS";
			txtUsuario.setValue("TODOS");
			lblUsuario.setValue("TODOS");
			txtUsuario.setFocus(true);
		}
	}

}
