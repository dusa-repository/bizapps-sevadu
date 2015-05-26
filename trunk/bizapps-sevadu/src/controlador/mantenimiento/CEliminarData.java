package controlador.mantenimiento;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.bitacora.BitacoraEliminacion;
import modelo.maestros.Cliente;
import modelo.maestros.Configuracion;
import modelo.maestros.Existencia;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MarcaActivadaVendedor;
import modelo.maestros.PlanVenta;
import modelo.maestros.Venta;
import modelo.seguridad.Arbol;
import modelo.seguridad.Usuario;
import modelo.seguridad.UsuarioAliado;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import componente.Botonera;
import componente.Catalogo;
import componente.Mensaje;
import componente.Validador;
import controlador.maestros.CGenerico;

public class CEliminarData extends CGenerico {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Row rowAliado;
	@Wire
	private Textbox txtAliado;
	@Wire
	private Label lblAliado;
	@Wire
	private Div botoneraEliminar;
	@Wire
	private Div divVEliminar;
	@Wire
	private Div divCatalogoAliado;
	@Wire
	private Combobox cmbTabla;
	@Wire
	private Comboitem cimVentas;
	@Wire
	private Comboitem cimExistencia;
	@Wire
	private Comboitem cimPlan;
	@Wire
	private Comboitem cimCartera;
	@Wire
	private Comboitem cimActivacion;
	@Wire
	private Datebox dtbDesde;
	@Wire
	private Datebox dtbHasta;
	Catalogo<MaestroAliado> catalogoAliado;
	String idAliado = null;
	protected DateFormat formatoMesAnno = new SimpleDateFormat("yyyyMM");
	protected DateFormat formatoSimple = new SimpleDateFormat("yyyyMMdd");

	@Override
	public void inicializar() throws IOException {
		Authentication authe = SecurityContextHolder.getContext()
				.getAuthentication();
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(
				authe.getAuthorities());
		Usuario user = servicioUsuario.buscarPorLogin(nombreUsuarioSesion());
		UsuarioAliado objeto = servicioUsuarioAliado.buscarActivo(user);
		if (objeto != null)
			idAliado = objeto.getId().getMaestroAliado().getCodigoAliado();
		boolean eliminarTodo = false;
		for (int i = 0; i < authorities.size(); i++) {
			Arbol arbol;
			String nombre = authorities.get(i).toString();
			if (Validador.validarNumero(nombre)) {
				arbol = servicioArbol.buscar(Long.parseLong(nombre));
				if (arbol.getNombre().equals("Ver Aliados Eliminar Data"))
					rowAliado.setVisible(true);
				if (arbol.getNombre().equals("Eliminar Data Ventas"))
					cimVentas.setVisible(true);
				if (arbol.getNombre().equals("Eliminar Data Plan Ventas"))
					cimPlan.setVisible(true);
				if (arbol.getNombre().equals("Eliminar Data Existencia"))
					cimExistencia.setVisible(true);
				if (arbol.getNombre().equals("Eliminar Data Cartera"))
					cimCartera.setVisible(true);
				if (arbol.getNombre().equals("Eliminar Data Activacion"))
					cimActivacion.setVisible(true);
				if (arbol.getNombre().equals("Eliminar Data sin Restriccion"))
					eliminarTodo = true;
			}
		}
		if (!eliminarTodo) {
			Configuracion actual = servicioConfiguracion.buscar(1);
			Integer mes = 1;
			if (actual != null)
				if (actual.getMes() != null)
					mes = actual.getMes();
			Date hoy = new Date();
			Date ayer = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(ayer);
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - (mes - 1));
			ayer = calendar.getTime();
			dtbDesde.setConstraint("between" + formatoMesAnno.format(ayer)
					+ "01 and" + formatoSimple.format(hoy));
			dtbHasta.setConstraint("between" + formatoMesAnno.format(ayer)
					+ "01 and" + formatoSimple.format(hoy));
		}
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (map != null) {
			if (map.get("tabsGenerales") != null) {
				tabs = (List<Tab>) map.get("tabsGenerales");
				cerrar = (String) map.get("nombre");
				grxGraficoGeneral = (Groupbox) map.get("grxGraficoGeneral");
				map.clear();
				map = null;
			}
		}
		Botonera botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub

			}

			@Override
			public void salir() {
				cerrarVentana(divVEliminar, cerrar, tabs, grxGraficoGeneral);
			}

			@Override
			public void reporte() {
				// TODO Auto-generated method stub

			}

			@Override
			public void limpiar() {
				// TODO Auto-generated method stub

			}

			@Override
			public void guardar() {
				// TODO Auto-generated method stub

			}

			@Override
			public void eliminar() {
				if (validar()) {
					Date desde = dtbDesde.getValue();
					Date hasta = dtbHasta.getValue();
					String tabla = cmbTabla.getValue();
					String tablaEliminada = null;
					switch (tabla) {
					case "Ventas":
						List<Venta> ventas = servicioVenta
								.buscarPorAliadoEntreFechasRegistro(idAliado,
										desde, hasta);
						if (!ventas.isEmpty()) {
							tablaEliminada = tabla;
							servicioVenta.eliminar(ventas);
						}
						break;
					case "Plan de Ventas":
						List<PlanVenta> planes = servicioPlan
								.buscarPorAliadoEntreFechasRegistro(idAliado,
										desde, hasta);
						if (!planes.isEmpty()) {
							tablaEliminada = tabla;
							servicioPlan.eliminar(planes);
						}
						break;
					case "Existencia":
						List<Existencia> existencias = servicioExistencia
								.buscarPorAliadoEntreFechasRegistro(idAliado,
										desde, hasta);
						if (!existencias.isEmpty()) {
							tablaEliminada = tabla;
							servicioExistencia.eliminar(existencias);
						}
						break;
					case "Cartera de Clientes":
						List<Cliente> clientes = servicioCliente
								.buscarPorAliadoEntreFechasRegistro(idAliado,
										desde, hasta);
						if (!clientes.isEmpty()) {
							tablaEliminada = tabla;
							servicioCliente.eliminarVarios(clientes);
						}
						break;
					case "Activacion de Marca":
						List<MarcaActivadaVendedor> marcas = servicioMarcaActivada
								.buscarPorAliadoEntreFechasRegistro(idAliado,
										desde, hasta);
						if (!marcas.isEmpty()) {
							tablaEliminada = tabla;
							servicioMarcaActivada.eliminarVarios(marcas);
						}
						break;
					default:
						break;
					}
					if (tablaEliminada != null) {
						Usuario user = servicioUsuario
								.buscarPorLogin(nombreUsuarioSesion());
						MaestroAliado aliado = servicioAliado.buscar(idAliado);
						BitacoraEliminacion eliminada = new BitacoraEliminacion(
								0, user, fecha, tiempo, desde, hasta,
								aliado.getNombre(), tablaEliminada);
						servicioBitacoraEliminacion.guardar(eliminada);
						if (aliado.getEmail() != null)
							enviarEmailNotificacionEliminacion(aliado,
									tablaEliminada, fecha, user);
						else
							msj.mensajeError("El aliado no tiene asociado ningun correo electronico, por lo tanto no se envio el correo respectivo");
						msj.mensajeInformacion("Data eliminada correctamente");
					} else {
						msj.mensajeError("No existen registros en el intervalo de tiempo seleccionado");
					}
				}
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
		botonera.getChildren().get(0).setVisible(false);
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(2).setVisible(false);
		botonera.getChildren().get(3).setVisible(false);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botoneraEliminar.appendChild(botonera);
	}

	protected boolean validar() {
		if (!rowAliado.isVisible() && idAliado == null) {
			msj.mensajeAlerta("Su usuario no esta asociado a ningun Aliado, "
					+ "pongase en contacto con el Administrador del sistema");
			return false;
		} else {
			if (rowAliado.isVisible() && idAliado == null) {
				msj.mensajeAlerta("Debe seleccionar un Aliado");
				return false;
			} else {
				if (cmbTabla.getText().compareTo("") == 0) {
					msj.mensajeAlerta("Debe seleccionar una Tabla a Eliminar");
					return false;
				} else
					return true;
			}
		}
	}

	@Listen("onClick = #btnBuscarAliado")
	public void mostrarCatalogoAliado() {
		final List<MaestroAliado> listaObjetos = servicioAliado
				.buscarTodosOrdenados();
		catalogoAliado = new Catalogo<MaestroAliado>(divCatalogoAliado,
				"Catalogo de Aliados", listaObjetos, true, false, false,
				"Codigo", "Nombre", "Zona", "Vendedor") {

			@Override
			protected List<MaestroAliado> buscar(List<String> valores) {

				List<MaestroAliado> lista = new ArrayList<MaestroAliado>();

				for (MaestroAliado objeto : listaObjetos) {
					if (objeto.getCodigoAliado().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& objeto.getNombre().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& objeto.getDescripcionZona().toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& objeto.getDescripcionVendedor().toLowerCase()
									.contains(valores.get(3).toLowerCase())) {
						lista.add(objeto);
					}
				}
				return lista;
			}

			@Override
			protected String[] crearRegistros(MaestroAliado objeto) {
				String[] registros = new String[4];
				registros[0] = objeto.getCodigoAliado();
				registros[1] = objeto.getNombre();
				registros[2] = objeto.getDescripcionZona();
				registros[3] = objeto.getDescripcionVendedor();
				return registros;
			}
		};
		catalogoAliado.setClosable(true);
		catalogoAliado.setWidth("80%");
		catalogoAliado.setParent(divCatalogoAliado);
		catalogoAliado.doModal();
	}

	@Listen("onSeleccion = #divCatalogoAliado")
	public void seleccionAliado() {
		MaestroAliado aliado = catalogoAliado.objetoSeleccionadoDelCatalogo();
		txtAliado.setValue(aliado.getCodigoAliado());
		lblAliado.setValue(aliado.getNombre());
		idAliado = aliado.getCodigoAliado();
		catalogoAliado.setParent(null);
	}

	@Listen("onOK = #txtAliado; onChange = #txtAliado")
	public void buscarNombreAliado() {
		MaestroAliado aliado = servicioAliado.buscar(txtAliado.getValue());
		if (aliado != null) {
			txtAliado.setValue(aliado.getCodigoAliado());
			lblAliado.setValue(aliado.getNombre());
			idAliado = aliado.getCodigoAliado();
		} else {
			msj.mensajeAlerta(Mensaje.noHayRegistros);
			txtAliado.setValue("");
			txtAliado.setFocus(true);
			lblAliado.setValue("");
		}
	}

}
