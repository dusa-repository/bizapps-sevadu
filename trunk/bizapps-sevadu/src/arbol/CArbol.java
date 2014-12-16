package arbol;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.imageio.ImageIO;

import modelo.maestros.Cliente;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroMarca;
import modelo.seguridad.Arbol;
import modelo.seguridad.Grupo;
import modelo.seguridad.Usuario;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
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

import componente.AngularActivacion;
import componente.AngularVenta;
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
	private Listbox ltbRoles;
	@Wire
	private Groupbox grxGrafico;
	@Wire
	private Div divGrafico;
	@Wire
	private Center center;
	private AngularActivacion angular;
	private AngularVenta angularVenta;
	private boolean todosAngular = false;
	private boolean unAngular = false;
	HashMap<String, Object> mapGeneral = new HashMap<String, Object>();

	@Override
	public void inicializar() throws IOException {
		Clients.confirmClose("Mensaje de la Aplicacion:");
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Usuario u = servicioUsuario.buscarUsuarioPorNombre(auth.getName());
		List<Grupo> grupos = servicioGrupo.buscarGruposUsuario(u);
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
		Date fechaHoy = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
		calendar.setTime(fechaHoy);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		fechaHoy = calendario.getTime();
		DateFormat formatoNuevo = new SimpleDateFormat("MM-yyyy");
		String parteFecha = "01-" + formatoNuevo.format(fechaHoy);
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		Date fechaPrimero = new Date();
		try {
			fechaPrimero = formato.parse(parteFecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (unAngular) {
			center.setStyle("background-image: none;");
			MaestroAliado aliado = servicioAliado
					.buscarPorLoginUsuario(nombreUsuarioSesion());
			if (aliado != null) {
				pintarGraficasAliado(aliado, fechaPrimero, fechaHoy);
			}
		}
		if (todosAngular) {
			pintarGraficasAdmin(fechaPrimero, fechaHoy);
			center.setStyle("background-image: none;");
		}
	}

	private void pintarGraficasAdmin(Date fecha1, Date fecha2) {
		DateFormat formatoAnno = new SimpleDateFormat("yyyy");
		DateFormat formatoMes = new SimpleDateFormat("MM");
		int annoPlanDesde = Integer.parseInt(formatoAnno.format(fecha1));
		int mesPlanDesde = Integer.parseInt(formatoMes.format(fecha1));
		List<MaestroMarca> marcas = servicioMarca.buscarTodosOrdenados();
		List<String> ids = new ArrayList<String>();
		for (Iterator<MaestroMarca> iterator = marcas.iterator(); iterator
				.hasNext();) {
			MaestroMarca marca = (MaestroMarca) iterator.next();
			ids.add(marca.getMarcaDusa());
		}
		List<String> listaAliados = servicioVenta.buscarAliadosMasVendedores(
				fecha1, fecha2, annoPlanDesde, mesPlanDesde);
		List<MaestroAliado> restantes = servicioAliado
				.buscarRestantes(listaAliados);
		for (Iterator<MaestroAliado> iterator = restantes.iterator(); iterator
				.hasNext();) {
			MaestroAliado maestroAliado = (MaestroAliado) iterator.next();
			listaAliados.add(maestroAliado.getCodigoAliado());
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
						servicioPlan, fecha1, fecha2);
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
		}
	}

	private void pintarGraficasAliado(MaestroAliado aliado, Date fecha1,
			Date fecha2) {
		List<Cliente> list = servicioCliente.buscarPorAliado(aliado);
		List<MaestroMarca> listMark = servicioMarca.buscarActivasActivacion();
		angular = new AngularActivacion(listMark, list, aliado, servicioVenta,
				fecha1, fecha2);
		angularVenta = new AngularVenta(aliado, servicioVenta, servicioPlan,
				fecha1, fecha2);
		Hbox caja = new Hbox();
		Cell celda = new Cell();
		celda.setWidth("35%");
		celda.appendChild(angular);
		caja.appendChild(celda);
		celda = new Cell();
		celda.setWidth("35%");
		celda.appendChild(angularVenta);
		caja.appendChild(celda);
		celda = new Cell();
		celda.setWidth("30%");
		celda.appendChild(new Space());
		caja.appendChild(celda);
		caja.setWidth("100%");
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
						&& !arbol.getNombre().contains("Dashboard"))
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
	 * pesta�a y su contenido es cargado en un div
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
				for (int i = 0; i < tabs.size(); i++) {
					if (tabs.get(i).getLabel().equals(arbolItem.getNombre())) {
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
										if (tabs.get(i).getLabel()
												.equals(arbolItem.getNombre())) {
											if (i == (tabs.size() - 1)
													&& tabs.size() > 1) {
												tabs.get(i - 1).setSelected(
														true);
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
				} else {
					taba.setSelected(true);
				}
			} else {
				if (!arbolMenu.getSelectedItem().isOpen())
					arbolMenu.getSelectedItem().setOpen(true);
				else
					arbolMenu.getSelectedItem().setOpen(false);
			}
		}
	}

	/* Metodo que permite abrir la ventana de editar usuario en una pesta�a */
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
}