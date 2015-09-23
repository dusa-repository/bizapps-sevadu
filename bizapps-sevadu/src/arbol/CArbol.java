package arbol;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import modelo.bitacora.BitacoraLogin;
import modelo.maestros.Cliente;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroMarca;
import modelo.seguridad.Usuario;
import modelo.seguridad.UsuarioAliado;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.zkoss.chart.Charts;
import org.zkoss.chart.Legend;
import org.zkoss.chart.Series;
import org.zkoss.chart.YAxis;
import org.zkoss.chart.plotOptions.SplinePlotOptions;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Space;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.West;

import security.modelo.Arbol;
import security.modelo.Grupo;
import security.modelo.MArbol;
import security.modelo.Nodos;
import security.modelo.UsuarioSeguridad;
import componente.AngularActivacion;
import componente.AngularVenta;
import componente.Catalogo;
import componente.Validador;
import controlador.maestros.CGenerico;

public class CArbol extends CGenerico {

	private static final long serialVersionUID = -5393608637902961029L;
	@Wire
	private Tree arbolMenu;
	@Wire
	private Include contenido;
	@Wire
	private Label etiqueta;
	@Wire
	private Image imagenes;
	TreeModel<?> _model;
	URL url = getClass().getResource("/controlador/seguridad/usuario.png");
	@Wire
	private Tab tab;
	@Wire
	private Tabbox tabBox;
	@Wire
	private West west;
	@Wire
	private Button btnBuscar;
	@Wire
	private Listbox ltbRoles;
	@Wire
	private Label lblUsuario;
	@Wire
	private Groupbox grxGrafico;
	@Wire
	private Div divGrafico;
	@Wire
	private Div divCatalogoAliado;
	@Wire
	private Center center;
	@Wire
	private A cerrar;
	private AngularActivacion angular;
	private AngularVenta angularVenta;
	private boolean todosAngular = false;
	private boolean unAngular = false;
	HashMap<String, Object> mapGeneral = new HashMap<String, Object>();
	long idSession = 0;
	Catalogo<UsuarioAliado> catalogo;

	@Override
	public void inicializar() throws IOException {
		// Clients.confirmClose("Mensaje de la Aplicacion:"); comentarizado
		// mientras se encuentra solucion a los graficos
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Usuario u = servicioUsuario.buscarUsuarioPorNombre(auth.getName());
		if (servicioUsuarioAliado.buscarPorUsuario(nombreUsuarioSesion())
				.size() > 1)
			btnBuscar.setVisible(true);
		UsuarioSeguridad u2 = servicioUsuarioSeguridad.buscarPorLogin(auth
				.getName());
		List<Grupo> grupos = servicioGrupo.buscarGruposUsuario(u2);
		ltbRoles.setModel(new ListModelList<Grupo>(grupos));
		if (u.getImagen() == null) {
			imagenes.setContent(new AImage(url));
		} else {
			try {
				BufferedImage imag;
				imag = ImageIO.read(new ByteArrayInputStream(u.getImagen()));
				imagenes.setContent(imag);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		arbolMenu.setModel(getModel());
		if (tabs.size() != 0) {
			tabs.clear();
		}
		WebAuthenticationDetails details = (WebAuthenticationDetails) auth
				.getDetails();
		BitacoraLogin login = new BitacoraLogin(idSession, u,
				details.getRemoteAddress(), fecha, tiempo, null, null);
		servicioBitacoraLogin.guardar(login);
		login = servicioBitacoraLogin.buscarUltimo(u);
		if (login != null)
			idSession = login.getIdLogin();
		llenarDatosAliado(u);

	}

	private void llenarDatosAliado(Usuario u) {
		UsuarioAliado objeto = servicioUsuarioAliado.buscarActivo(u);
		if (objeto != null) {
			String categoria = "N/A";
			if (objeto.getId().getMaestroAliado().getCategoria() != null)
				categoria = objeto.getId().getMaestroAliado().getCategoria();
			// lblUsuario.setValue("Aliado: "
			// + objeto.getId().getMaestroAliado().getNombre() + "("
			// + categoria + ")");lblUsuario.setValue("Aliado: "
			lblUsuario.setValue("Aliado: " + categoria);
		}
		Date fechaHoy = new Date();
		DateFormat formatoNuevo = new SimpleDateFormat("MM-yyyy");
		String parteFecha = "01-" + formatoNuevo.format(fechaHoy);
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		Date fechaPrimero = new Date();
		try {
			fechaPrimero = formato.parse(parteFecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String parteFecha2 = lastDay(fechaHoy) + formatoNuevo.format(fechaHoy);
		Date fechaUltimo = new Date();
		try {
			fechaUltimo = formato.parse(parteFecha2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String hoy = formato.format(fechaHoy);
		try {
			fechaHoy = formato.parse(hoy);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int habilesHoy = obtenerDiasHabiles(fechaPrimero, fechaHoy);
		int habilesTotal = obtenerDiasHabiles(fechaPrimero, fechaUltimo);
		List<MaestroMarca> marcas = servicioMarca.buscarTodosOrdenados();
		List<String> ids = new ArrayList<String>();
		for (Iterator<MaestroMarca> iterator = marcas.iterator(); iterator
				.hasNext();) {
			MaestroMarca marca = (MaestroMarca) iterator.next();
			ids.add(marca.getMarcaDusa());
		}
		if (unAngular) {
			center.setStyle("background-image: none;");
			if (objeto != null) {
				pintarGraficasAliado(objeto.getId().getMaestroAliado(),
						fechaPrimero, fechaHoy, habilesHoy, habilesTotal, ids);
			}
		}
		if (todosAngular) {
			pintarGraficasAdmin(fechaPrimero, fechaHoy, habilesHoy,
					habilesTotal, ids);
			center.setStyle("background-image: none;");
		}

	}

	private void pintarGraficasAdmin(Date fecha1, Date fecha2, int habilesHoy,
			int habilesTotal, List<String> ids) {
		DateFormat formatoAnno = new SimpleDateFormat("yyyy");
		DateFormat formatoMes = new SimpleDateFormat("MM");
		int annoPlanDesde = Integer.parseInt(formatoAnno.format(fecha1));
		int mesPlanDesde = Integer.parseInt(formatoMes.format(fecha1));

		List<String> listaAliados = servicioVenta.buscarAliadosMasVendedores(
				fecha1, fecha2, annoPlanDesde, mesPlanDesde);
		List<MaestroAliado> restantes = servicioAliado
				.buscarRestantes(listaAliados);
		if (!restantes.isEmpty()) {
			for (Iterator<MaestroAliado> iterator = restantes.iterator(); iterator
					.hasNext();) {
				MaestroAliado maestroAliado = (MaestroAliado) iterator.next();
				listaAliados.add(maestroAliado.getCodigoAliado());
			}
		}
		if (!listaAliados.isEmpty()) {
			int valor = 0;
			boolean entro = false;
			boolean entroSegundo = false;
			Hbox cajaHorizontal = new Hbox();
			for (int i = 0; i < listaAliados.size(); i++) {
				if (i > 2 && i < 6) {
					if (!entro) {
						entro = true;
						valor = listaAliados.size();
					}
					valor = valor - 1;
				} else {
					if (i >= 6) {
						if (!entroSegundo) {
							entroSegundo = true;
							valor = 2;
						}
						valor = valor + 1;
						if (valor == listaAliados.size() - 4)
							i = listaAliados.size();
					} else
						valor = i;
				}
				MaestroAliado aliado = servicioAliado.buscar(listaAliados
						.get(valor));
				angularVenta = new AngularVenta(aliado, servicioVenta,
						servicioPlan, fecha1, fecha2, servicioConfiguracion,
						habilesHoy, habilesTotal);
				Cell celda = new Cell();
				celda.setWidth("33%");
				celda.appendChild(angularVenta);
				cajaHorizontal.appendChild(celda);
				if ((i + 1) % 3 == 0) {
					if (i == listaAliados.size()) {
						Cell celda2 = new Cell();
						celda2.setWidth("33%");
						celda2.appendChild(new Space());
						cajaHorizontal.appendChild(celda2);
					}
					cajaHorizontal.setWidth("100%");
					divGrafico.appendChild(cajaHorizontal);
					cajaHorizontal = new Hbox();
				}
			}
			if ((listaAliados.size() + 1) % 2 != 0) {
				cajaHorizontal.setWidth("100%");
				divGrafico.appendChild(cajaHorizontal);
			}
			cajaHorizontal = new Hbox();
			cajaHorizontal.setWidth("100%");
			Cell celda = new Cell();
			// celda.setWidth("30%");
			// celda.appendChild(new Space());
			// cajaHorizontal.appendChild(celda);

			// celda = new Cell();
			celda.setWidth("100%");
			celda.appendChild(graficoVentas("%", fecha1, fecha2, ids));
			cajaHorizontal.appendChild(celda);

			// celda = new Cell();
			// celda.setWidth("30%");
			// celda.appendChild(new Space());
			// cajaHorizontal.appendChild(celda);
			divGrafico.appendChild(cajaHorizontal);
		}
	}

	private void pintarGraficasAliado(MaestroAliado aliado, Date fecha1,
			Date fecha2, int habilesHoy, int habilesTotal, List<String> ids) {
		List<Cliente> list = servicioCliente.buscarPorAliado(aliado);
		List<MaestroMarca> listMark = servicioMarca.buscarActivasActivacion();
		angular = new AngularActivacion(listMark, list, aliado, servicioVenta,
				fecha1, fecha2, servicioConfiguracion);
		angularVenta = new AngularVenta(aliado, servicioVenta, servicioPlan,
				fecha1, fecha2, servicioConfiguracion, habilesHoy, habilesTotal);
		try {
			fecha1 = formatoFecha.parse("01-01-"
					+ Integer.parseInt(formatoAnno.format(fecha1)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Hbox caja = new Hbox();
		caja.setWidth("80%");
		Cell celda = new Cell();
		celda.setWidth("50%");
		celda.appendChild(angular);
		caja.appendChild(celda);
		celda = new Cell();
		celda.setWidth("50%");
		celda.appendChild(angularVenta);
		caja.appendChild(celda);
		divGrafico.appendChild(caja);
		caja = new Hbox();
		caja.setWidth("80%");
		celda = new Cell();
		celda.setWidth("95%");
		celda.appendChild(graficoVentas(aliado.getCodigoAliado(), fecha1,
				fecha2, ids));
		caja.appendChild(celda);
		celda = new Cell();
		celda.setWidth("5%");
		celda.appendChild(new Space());
		caja.appendChild(celda);
		divGrafico.appendChild(caja);
	}

	/* Permite asignarle los nodos cargados con el metodo getFooRoot() al arbol */
	public TreeModel<?> getModel() {
		if (_model == null) {
			_model = new MArbol(getFooRoot());
		}
		return _model;
	}

	/*
	 * Permite obtener las funcionalidades asociadas al usuario en session y asi
	 * crear un arbol estructurado, segun la distribucion de las mismas
	 */
	private Nodos getFooRoot() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(
				auth.getAuthorities());
		ArrayList<Arbol> arbole = new ArrayList<Arbol>();
		List<Arbol> arboles = new ArrayList<Arbol>();
		ArrayList<Long> ids = new ArrayList<Long>();
		for (int k = 0; k < authorities.size(); k++) {

			Arbol arbol;
			String nombre = authorities.get(k).toString();
			if (Validador.validarNumero(nombre)) {
				arbol = servicioArbol.buscar(Long.parseLong(nombre));
				if (arbol != null
						&& !arbol.getNombre().equals("Ver Aliados Mapping")
						&& !arbol.getNombre().equals("Ver Aliados Termometro")
						&& !arbol.getNombre().equals(
								"Ver Aliados Reporte/Grafica")
						&& !arbol.getNombre().equals(
								"Ver Reportes Administrador")
						&& !arbol.getNombre().contains("Dashboard")
						&& !arbol.getNombre().contains(
								"Ver Aliados Eliminar Data")
						&& !arbol.getNombre().startsWith("Eliminar Data")
						&& !arbol.getNombre().contains(
								"Eliminar Data sin Restriccion"))
					ids.add(arbol.getIdArbol());
				if (arbol != null && arbol.getNombre().contains("Dashboard")) {
					grxGrafico.setVisible(true);
					if (arbol.getNombre().equals("Dashboard Aliado")) {
						unAngular = true;
						todosAngular = false;
					} else {
						unAngular = false;
						todosAngular = true;
					}
				}
				arbole.add(arbol);
			}
		}

		Collections.sort(ids);
		for (int t = 0; t < ids.size(); t++) {
			Arbol a;
			a = servicioArbol.buscarPorId(ids.get(t));
			arboles.add(a);
		}
		List<Long> idsPadre = new ArrayList<Long>();
		List<Nodos> nodos = new ArrayList<Nodos>();
		Nodos root = new Nodos(null, 0, "");
		return crearArbol(root, nodos, arboles, 0, idsPadre);
	}

	private Nodos crearArbol(Nodos roote, List<Nodos> nodos,
			List<Arbol> arboles, int i, List<Long> idsPadre) {
		for (int z = 0; z < arboles.size(); z++) {
			Nodos oneLevelNode = new Nodos(null, 0, "");
			Nodos two = new Nodos(null, 0, "");
			if (arboles.get(z).getPadre() == 0) {
				oneLevelNode = new Nodos(roote, (int) arboles.get(z)
						.getIdArbol(), arboles.get(z).getNombre());
				roote.appendChild(oneLevelNode);
				idsPadre.add(arboles.get(z).getIdArbol());
				nodos.add(oneLevelNode);
			} else {
				for (int j = 0; j < idsPadre.size(); j++) {
					if (idsPadre.get(j) == arboles.get(z).getPadre()) {
						oneLevelNode = nodos.get(j);
						two = new Nodos(oneLevelNode, (int) arboles.get(z)
								.getIdArbol(), arboles.get(z).getNombre());
						oneLevelNode.appendChild(two);
						idsPadre.add(arboles.get(z).getIdArbol());
						nodos.add(two);
					}
				}
			}
		}
		return roote;
	}

	/*
	 * Permite seleccionar un elemento del arbol, mostrandolo en forma de
	 * pestaña y su contenido es cargado en un div
	 */
	@Listen("onClick = #arbolMenu")
	public void selectedNode() {
		if (arbolMenu.getSelectedItem() != null) {
			Treecell celda = (Treecell) arbolMenu.getSelectedItem()
					.getChildren().get(0).getChildren().get(0);
			long item = Long.valueOf(celda.getId());
			boolean abrir = true;
			Tab taba = new Tab();
			final Arbol arbolItem = servicioArbol.buscarPorId(item);
			if (!arbolItem.getUrl().equals("inicio")) {
				mapGeneral.put("titulo", arbolItem.getNombre());
				if (!arbolItem.getUrl().equals("graficas")) {
					for (int i = 0; i < tabs.size(); i++) {
						if (tabs.get(i).getLabel()
								.equals(arbolItem.getNombre())) {
							abrir = false;
							taba = tabs.get(i);
						}
					}
					if (abrir) {
						if (grxGrafico.isVisible() && grxGrafico.isOpen()) {
							grxGrafico.setOpen(false);
						}
						if (arbolItem.getUrl().contains("Termometro")) {
							west.setOpen(false);
							mapGeneral.put("west", west);
						}
						String ruta = "/vistas/" + arbolItem.getUrl() + ".zul";
						contenido = new Include();
						contenido.setSrc(null);
						InputStream url = Sessions.getCurrent().getWebApp()
								.getServletContext().getResourceAsStream(ruta);
						if (url != null) {
							contenido.setSrc(ruta);
							Tab newTab = new Tab(arbolItem.getNombre());
							newTab.setClosable(true);
							newTab.addEventListener(Events.ON_CLOSE,
									new EventListener<Event>() {
										@Override
										public void onEvent(Event arg0)
												throws Exception {
											if (arbolItem.getUrl().contains(
													"Termometro"))
												west.setOpen(true);
											for (int i = 0; i < tabs.size(); i++) {
												if (tabs.get(i)
														.getLabel()
														.equals(arbolItem
																.getNombre())) {
													if (i == (tabs.size() - 1)
															&& tabs.size() > 1) {
														tabs.get(i - 1)
																.setSelected(
																		true);
													}

													tabs.get(i).close();
													tabs.remove(i);
													if (tabs.size() == 0) {
														if (grxGrafico
																.isVisible()
																&& !grxGrafico
																		.isOpen()) {
															grxGrafico
																	.setOpen(true);
														}
													}
												}
											}
										}
									});
							newTab.setSelected(true);
							Tabpanel newTabpanel = new Tabpanel();
							newTabpanel.setWidth("100%");
							newTabpanel.appendChild(contenido);
							tabBox.getTabs().insertBefore(newTab, tab);
							newTabpanel.setParent(tabBox.getTabpanels());
							tabs.add(newTab);
							mapGeneral.put("tabsGenerales", tabs);
							mapGeneral.put("grxGraficoGeneral", grxGrafico);
							mapGeneral.put("nombre", arbolItem.getNombre());
							Sessions.getCurrent().setAttribute("mapaGeneral",
									mapGeneral);
						} else
							msj.mensajeError("Vista no encontrada; contactar soporte");
					} else {
						taba.setSelected(true);
					}
				} else {
					Clients.evalJavaScript("window.open('"
							+ "../../dusa-charts/vistas/inicio.zul?type=2&id="
							+ nombreUsuarioSesion() + "','_blank')");
				}
			} else {
				if (!arbolMenu.getSelectedItem().isOpen())
					arbolMenu.getSelectedItem().setOpen(true);
				else
					arbolMenu.getSelectedItem().setOpen(false);
			}
		}
	}

	/* Metodo que permite abrir la ventana de editar usuario en una pestaña */
	@Listen("onClick = #lblEditarCuenta")
	public void abrirEditarCuenta() {
		boolean abrir = true;
		Tab taba = new Tab();
		for (int i = 0; i < tabs.size(); i++) {
			if (tabs.get(i).getLabel().equals("Editar Usuario")) {
				abrir = false;
				taba = tabs.get(i);
			}
		}
		if (abrir) {
			if (grxGrafico.isVisible() && grxGrafico.isOpen()) {
				grxGrafico.setOpen(false);
			}
			String ruta = "/vistas/seguridad/VEditarUsuario.zul";
			contenido = new Include();
			contenido.setSrc(null);
			contenido.setSrc(ruta);
			Tab newTab = new Tab("Editar Usuario");
			newTab.setClosable(true);
			newTab.addEventListener(Events.ON_CLOSE,
					new EventListener<Event>() {
						@Override
						public void onEvent(Event arg0) throws Exception {
							for (int i = 0; i < tabs.size(); i++) {
								if (tabs.get(i).getLabel()
										.equals("Editar Usuario")) {
									if (i == (tabs.size() - 1)
											&& tabs.size() > 1) {
										tabs.get(i - 1).setSelected(true);
									}

									tabs.get(i).close();
									tabs.remove(i);
									if (tabs.size() == 0) {
										if (grxGrafico.isVisible()
												&& !grxGrafico.isOpen()) {
											grxGrafico.setOpen(true);
										}
									}
								}
							}
						}
					});
			newTab.setSelected(true);
			Tabpanel newTabpanel = new Tabpanel();
			newTabpanel.appendChild(contenido);
			tabBox.getTabs().insertBefore(newTab, tab);
			newTabpanel.setParent(tabBox.getTabpanels());
			tabs.add(newTab);
			mapGeneral.put("tabsGenerales", tabs);
			mapGeneral.put("grxGraficoGeneral", grxGrafico);
			Sessions.getCurrent().setAttribute("mapaGeneral", mapGeneral);
		} else
			taba.setSelected(true);
	}

	@Listen("onClick = #mnuItem")
	public void cerrarTodas() {
		for (int i = 0; i < tabs.size(); i++) {
			tabs.get(i).close();
			tabs.remove(i);
			i--;
		}
		if (tabs.size() == 0) {
			if (grxGrafico.isVisible() && !grxGrafico.isOpen()) {
				grxGrafico.setOpen(true);
			}
		}
	}

	@Listen("onClick = #btnBuscar")
	public void mostrarCatalogoAliado() {
		final List<UsuarioAliado> listaObjetos = servicioUsuarioAliado
				.buscarPorUsuario(nombreUsuarioSesion());
		catalogo = new Catalogo<UsuarioAliado>(divCatalogoAliado,
				"Catalogo de Aliados asociados al Usuario", listaObjetos, true,
				false, false, "Codigo", "Nombre") {

			@Override
			protected List<UsuarioAliado> buscar(List<String> valores) {

				List<UsuarioAliado> lista = new ArrayList<UsuarioAliado>();

				for (UsuarioAliado objeto : listaObjetos) {
					if (objeto.getId().getMaestroAliado().getNombre()
							.toLowerCase()
							.contains(valores.get(1).toLowerCase())
							&& objeto.getId().getMaestroAliado()
									.getCodigoAliado().toLowerCase()
									.contains(valores.get(0).toLowerCase())) {
						lista.add(objeto);
					}
				}
				return lista;
			}

			@Override
			protected String[] crearRegistros(UsuarioAliado objeto) {
				String[] registros = new String[2];
				registros[0] = objeto.getId().getMaestroAliado()
						.getCodigoAliado();
				registros[1] = objeto.getId().getMaestroAliado().getNombre();
				return registros;
			}
		};
		catalogo.setClosable(true);
		catalogo.setWidth("30%");
		catalogo.setParent(divCatalogoAliado);
		catalogo.doModal();
	}

	@Listen("onSeleccion = #divCatalogoAliado")
	public void seleccionAliado() {
		UsuarioAliado aliado = catalogo.objetoSeleccionadoDelCatalogo();
		UsuarioAliado nuevo = servicioUsuarioAliado.buscarActivo(aliado.getId()
				.getUsuario());
		nuevo.setEstado(false);
		servicioUsuarioAliado.guardar(nuevo);
		aliado.setEstado(true);
		servicioUsuarioAliado.guardar(aliado);
		List<Component> componentes = (List<Component>) divGrafico
				.getChildren();
		for (int i = 0; i < componentes.size(); i++) {
			divGrafico.removeChild(componentes.get(i));
		}
		angular = null;
		angularVenta = null;
		llenarDatosAliado(aliado.getId().getUsuario());
		catalogo.setParent(null);
	}

	private Component graficoVentas(String aliado2, Date fechaDesde,
			Date fechaHasta, List<String> ids) {
		Charts chart = new Charts();
		int annoPlanDesde = Integer.parseInt(formatoAnno.format(fechaDesde));
		int mesPlanDesde = Integer.parseInt(formatoMes.format(fechaDesde));
		int annoPlanHasta = Integer.parseInt(formatoAnno.format(fechaHasta));
		int mesPlanHasta = Integer.parseInt(formatoMes.format(fechaHasta));
		DateFormat formatoFechaNuevo = new SimpleDateFormat("MMM");
		List<String> categorias = new ArrayList<String>();
		List<Integer> inventario = new ArrayList<Integer>();
		List<Double> ventas = new ArrayList<Double>();
		List<Double> ventasDusa = new ArrayList<Double>();
		List<Integer> planes = new ArrayList<Integer>();
		do {
			if (mesPlanDesde == 13) {
				mesPlanDesde = 1;
				annoPlanDesde = annoPlanDesde + 1;
			}
			Date fechaInicio = null;
			try {
				fechaInicio = formatoFecha.parse("01-" + mesPlanDesde + "-"
						+ annoPlanDesde);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Date fechaFin = null;
			String ultimoDia = "";
			if (annoPlanDesde == annoPlanHasta && mesPlanDesde == mesPlanHasta)
				ultimoDia = formatoDia.format(fechaHasta) + "-";
			else
				ultimoDia = lastDay(fechaInicio);

			try {
				fechaFin = formatoFecha.parse(ultimoDia + mesPlanDesde + "-"
						+ annoPlanDesde);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			Double valorVenta = servicioVenta
					.sumarPorAliadoEntreFechasYMarcasOrdenadoPorProducto(
							aliado2, fechaInicio, fechaFin, ids);
			Double valorVentaDusa = servicioVentaDusa
					.sumarPorAliadoEntreFechasYMarcasOrdenadoPorProducto(
							aliado2, fechaInicio, fechaFin, ids);
			Integer valorInventario = servicioExistencia
					.sumarPorAliadoEntreFechasYMarcasOrdenadoPorProducto(
							aliado2, fechaInicio, fechaFin, ids);
			Integer planificado = servicioPlan.sumarPorProductosaliadoYFechas(
					aliado2, ids, annoPlanDesde, mesPlanDesde);
			ventas.add(valorVenta);
			ventasDusa.add(valorVentaDusa);
			inventario.add(valorInventario);
			planes.add(planificado);
			categorias.add(formatoFechaNuevo.format(fechaInicio));

			mesPlanDesde = mesPlanDesde + 1;
		} while (annoPlanDesde != annoPlanHasta
				|| mesPlanDesde != mesPlanHasta + 1);

		chart.getXAxis().setCategories(categorias);

		YAxis yAxis1 = chart.getYAxis(1);
		yAxis1.setMin(0);
		yAxis1.getLabels().setFormat("{value} Cajas");
		yAxis1.setTitle("Inventario");
		yAxis1.setOpposite(true);

		YAxis yAxis3 = chart.getYAxis();
		yAxis3.setMin(0);
		yAxis3.setGridLineWidth(0);
		yAxis3.setTitle("Ventas Dusa");
		yAxis3.getLabels().setFormat("{value} Cajas");
		yAxis3.setOpposite(true);
		chart.getTooltip().setShared(true);

		YAxis yAxis2 = chart.getYAxis(2);
		yAxis2.setMin(0);
		yAxis2.setGridLineWidth(0);
		yAxis2.setTitle("Ventas");
		yAxis2.getLabels().setFormat("{value} Cajas");

		YAxis yAxis4 = chart.getYAxis(3);
		yAxis4.setMin(0);
		yAxis4.setTitle("Plan Ventas");
		yAxis4.getLabels().setFormat("{value} Cajas");

		String color1 = chart.getColors().get(0).stringValue();
		String color2 = chart.getColors().get(1).stringValue();
		String color3 = chart.getColors().get(2).stringValue();
		String color4 = chart.getColors().get(3).stringValue();
		yAxis1.getLabels().setStyle("color: '" + color1 + "'");
		yAxis1.getTitle().setStyle("color: '" + color1 + "'");
		yAxis3.getLabels().setStyle("color: '" + color2 + "'");
		yAxis3.getTitle().setStyle("color: '" + color2 + "'");
		yAxis2.getLabels().setStyle("color: '" + color3 + "'");
		yAxis2.getTitle().setStyle("color: '" + color3 + "'");
		yAxis4.getLabels().setStyle("color: '" + color4 + "'");
		yAxis4.getTitle().setStyle("color: '" + color4 + "'");

		Legend legend = chart.getLegend();
		legend.setLayout("vertical");
		legend.setAlign("left");
		legend.setX(165);
		legend.setVerticalAlign("top");
		legend.setY(80);
		legend.setFloating(true);

		Series rainfall = new Series("Inventario");
		rainfall.setName("Inventario");
		rainfall.setType("column");
		rainfall.setYAxis(1);
		rainfall.setData(inventario);
		rainfall.getPlotOptions().getTooltip().setValueSuffix(" cajas");
		chart.addSeries(rainfall);

		Series temperature = new Series("Ventas Dusa");
		temperature.setName("Ventas Dusa");
		temperature.setType("spline");
		temperature.setData(ventasDusa);
		temperature.getPlotOptions().getTooltip().setValueSuffix(" cajas");
		chart.addSeries(temperature);

		Series pressure = new Series("Ventas");
		pressure.setName("Ventas");
		pressure.setType("spline");
		pressure.setYAxis(2);
		pressure.setData(ventas);
		pressure.getMarker().setEnabled(false);
		SplinePlotOptions plotOptions2 = new SplinePlotOptions();
		plotOptions2.setDashStyle("shortdot");
		plotOptions2.getTooltip().setValueSuffix(" cajas");
		pressure.setPlotOptions(plotOptions2);
		chart.addSeries(pressure);

		Series pressure2 = new Series("Plan Ventas");
		pressure2.setName("Plan Ventas");
		pressure2.setType("spline");
		pressure2.setYAxis(3);
		pressure2.setData(planes);
		pressure2.getMarker().setEnabled(false);
		SplinePlotOptions plotOptions22 = new SplinePlotOptions();
		plotOptions22.setDashStyle("dot");
		plotOptions22.getTooltip().setValueSuffix(" cajas");
		pressure2.setPlotOptions(plotOptions22);
		chart.addSeries(pressure2);

		MaestroAliado aliadoBuscar = servicioAliado.buscar(aliado2);
		String nombreAliado = "Todos";
		String codigoAliado = "Todos";
		if (aliadoBuscar != null) {
			nombreAliado = aliadoBuscar.getNombre();
			codigoAliado = aliadoBuscar.getCodigoAliado();
		}
		chart.setTitle("Comparacion entre Ventas/Compras/Inventario/Plan Ventas "
				+ formatoFecha.format(fechaDesde)
				+ " hasta  "
				+ formatoFecha.format(fechaHasta));
		chart.setSubtitle("Aliado: " + nombreAliado + " (" + codigoAliado + ")");
		return chart;
	}
}