package controlador.reporte;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.maestros.Configuracion;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroMarca;
import modelo.maestros.Venta;
import modelo.seguridad.Arbol;
import modelo.seguridad.Usuario;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componente.Botonera;
import componente.Catalogo;
import componente.Mensaje;
import componente.Validador;
import controlador.maestros.CGenerico;

public class CReporte extends CGenerico {

	private static final long serialVersionUID = 1470992598691077405L;
	@Wire
	private Textbox txtAliado;
	@Wire
	private Div botoneraReporte;
	@Wire
	private Div divVReporte;
	@Wire
	private Div divCatalogoAliado;
	@Wire
	private Combobox cmbReporte;
	@Wire
	private Combobox cmbZona;
	@Wire
	private Combobox cmbCliente;
	@Wire
	private Combobox cmbVendedor;
	@Wire
	private Comboitem itm16;
	@Wire
	private Comboitem itm17;
	@Wire
	private Comboitem itm20;
	@Wire
	private Comboitem itm21;
	@Wire
	private Comboitem itm23;
	@Wire
	private Datebox dtbDesde;
	@Wire
	private Datebox dtbHasta;
	@Wire
	private Row rowAliado;
	@Wire
	private Row rowZona;
	@Wire
	private Row rowVendedor;
	@Wire
	private Hbox box;
	Catalogo<MaestroAliado> catalogoAliado;
	protected Connection conexion;
	String idAliado = null;
	@Wire
	private Listbox ltbMarcas;
	@Wire
	private Listbox ltbMarcasAgregadas;
	@Wire
	private Label lblAliado;
	List<MaestroMarca> marcas = new ArrayList<MaestroMarca>();
	List<MaestroMarca> marcasAgregadas = new ArrayList<MaestroMarca>();

	@Override
	public void inicializar() throws IOException {
		Authentication authe = SecurityContextHolder.getContext()
				.getAuthentication();
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(
				authe.getAuthorities());
		Usuario user = servicioUsuario.buscarPorLogin(nombreUsuarioSesion());
		if (user.getMaestroAliado() != null) {
			idAliado = user.getMaestroAliado().getCodigoAliado();
		}
		for (int i = 0; i < authorities.size(); i++) {
			Arbol arbol;
			String nombre = authorities.get(i).toString();
			if (Validador.validarNumero(nombre)) {
				arbol = servicioArbol.buscar(Long.parseLong(nombre));
				if (arbol.getNombre().equals("Ver Aliados Reporte/Grafica"))
					rowAliado.setVisible(true);
				if (arbol.getNombre().equals("Ver Reportes Administrador")) {
					itm16.setVisible(true);
					itm17.setVisible(true);
					itm20.setVisible(true);
					itm21.setVisible(true);
					itm23.setVisible(true);
				}
			}
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
		llenarLista();
		Botonera botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub

			}

			@Override
			public void salir() {
				cerrarVentana(divVReporte, cerrar, tabs, grxGraficoGeneral);
			}

			@Override
			public void reporte() {
				// TODO Auto-generated method stub

			}

			@Override
			public void limpiar() {
				cmbReporte.setValue("");
				cmbCliente.setValue("TODOS");
				cmbVendedor.setValue("TODOS");
				cmbZona.setValue("TODAS");
				dtbDesde.setValue(fecha);
				dtbHasta.setValue(fecha);
				txtAliado.setValue("");
				rowVendedor.setVisible(true);
				rowZona.setVisible(true);
				box.setVisible(false);
				lblAliado.setValue("");
				llenarLista();
			}

			@Override
			public void guardar() {
				if (validar()) {

					Date desde = dtbDesde.getValue();
					Date hasta = dtbHasta.getValue();
					DateFormat fechaF = new SimpleDateFormat("yyyy-MM-dd");
					String fecha1 = fechaF.format(desde);
					String fecha2 = fechaF.format(hasta);
					String aliado = idAliado;
					String vendedor = cmbVendedor.getValue();
					String zona = cmbZona.getValue();
					String cliente = cmbCliente.getValue();
					if (vendedor.equals("TODOS"))
						vendedor = "";
					if (zona.equals("TODAS"))
						zona = "";
					if (cliente.equals("TODOS"))
						cliente = "";
					int tipo = 0;
					String tipoReporte = "PDF";
					Window ventana = new Window();
					HashMap<String, Object> mapaGrafica = new HashMap<String, Object>();
					switch (cmbReporte.getValue()) {
					case "(R55420001)Ventas Consolidadas por Zona y Cliente":
						tipo = 1;
						break;
					case "(R55420002)Ventas Consolidadas por Zona/Cliente/Marca":
						tipo = 2;
						break;
					case "(R55420003)Ventas Consolidadas por Zona/Marca/Cliente":
						tipo = 3;
						break;
					case "(R55420005)Activacion de Marcas por Zona/Vendedor/Cliente":
						tipo = 4;
						break;
					case "(R55420006)Activacion de Marcas por Vendedor Aliado":
						tipo = 5;
						break;
					case "(R55420007)Activacion por Marca":
						tipo = 6;
						break;
					case "(R55420008)Activacion por Marca/Cliente":
						tipo = 7;
						break;
					case "(R55420009)Ventas 80/20 por Cliente":
						tipo = 8;
						break;
					case "(R55420010)Ventas 80/20 por Marca":
						tipo = 9;
						break;
					case "(R55420011)Ventas 80/20 por Marca/Cliente":
						tipo = 10;
						break;
					case "(R55420014)Inventario por Marca":
						tipo = 11;
						break;
					case "(R55420016)Existencia Consolidada por Aliado/Marca":
						tipo = 12;
						break;
					case "(R55420017)Ventas Consolidada por Aliados":
						tipo = 13;
						break;
					case "(R55420018)Ventas por Segmentacion ON":
						tipo = 14;
						break;
					case "(R55420019)Ventas por Segmentacion OFF":
						tipo = 15;
						break;
					case "(R55420020)Ventas Consolidada por Zona Dusa/Aliado/Marca":
						tipo = 16;
						break;
					case "(R55420021)Ventas Consolidada Marca":
						tipo = 17;
						break;
					case "(R55420022)Ventas Consolidada Aliados/Marca ( Comparativos )":
						tipo = 18;
						break;
					case "(R55420023)Ventas Consolidada Aliados ( Comparativos )":
						tipo = 19;
						break;
					case "(R55420024)Inventario con Compras Calculadas Aliado/Marca":
						tipo = 20;
						break;
					case "(R55420026)Reporte de Control de Subida de Archivos":
						tipo = 21;
						break;
					case "(G55420001)Grafico de Activacion/Marca":
						tipo = 22;
						break;
					case "(R55420004)Comparativo Plan de Ventas Vs Ventas":
						tipo = 23;
						HashMap<String, Object> mapa = new HashMap<String, Object>();
						mapa.put("idAliado", aliado);
						mapa.put("vendedor", vendedor);
						mapa.put("desde", desde);
						mapa.put("hasta", hasta);
						Sessions.getCurrent().setAttribute("reporte", mapa);
						Window window = (Window) Executions.createComponents(
								"/vistas/reportes/VReporteVentasPlan.zul",
								null, mapa);
						window.doModal();
						break;
					case "(R55420025)Generar Objetivos/Marca/Clientes (EXCEL)":
						tipoReporte = "EXCEL";
						tipo = 24;
						break;
					case "Grafico Venta de Marcas":
						tipo = 25;
						mapaGrafica = new HashMap<String, Object>();
						mapaGrafica.put("idAliado", aliado);
						mapaGrafica.put("desde", desde);
						mapaGrafica.put("hasta", hasta);
						mapaGrafica.put("tipo", "line");
						mapaGrafica.put("lista", marcasAgregadas);
						Sessions.getCurrent().setAttribute("grafica",
								mapaGrafica);
						ventana = (Window) Executions.createComponents(
								"/vistas/reportes/VGrafica.zul", null,
								mapaGrafica);
						ventana.doModal();
						break;
					case "Grafico Vendido VS Planificado Marcas":
						tipo = 25;
						mapaGrafica = new HashMap<String, Object>();
						mapaGrafica.put("idAliado", aliado);
						mapaGrafica.put("desde", desde);
						mapaGrafica.put("hasta", hasta);
						mapaGrafica.put("tipo", "column");
						mapaGrafica.put("lista", marcasAgregadas);
						Sessions.getCurrent().setAttribute("grafica",
								mapaGrafica);
						ventana = (Window) Executions.createComponents(
								"/vistas/reportes/VGrafica.zul", null,
								mapaGrafica);
						ventana.doModal();
						break;
					case "Grafico Volumen de Ventas por Marca y Producto":
						tipo = 25;
						mapaGrafica = new HashMap<String, Object>();
						mapaGrafica.put("idAliado", aliado);
						mapaGrafica.put("desde", desde);
						mapaGrafica.put("hasta", hasta);
						mapaGrafica.put("tipo", "pie");
						mapaGrafica.put("dona", true);
						mapaGrafica.put("lista", marcasAgregadas);
						Sessions.getCurrent().setAttribute("grafica",
								mapaGrafica);
						ventana = (Window) Executions.createComponents(
								"/vistas/reportes/VGrafica.zul", null,
								mapaGrafica);
						ventana.doModal();
						break;
					case "Grafico Porcentaje de Participacion en Ventas(Marcas)":
						tipo = 25;
						mapaGrafica = new HashMap<String, Object>();
						mapaGrafica.put("idAliado", aliado);
						mapaGrafica.put("desde", desde);
						mapaGrafica.put("hasta", hasta);
						mapaGrafica.put("tipo", "pie");
						mapaGrafica.put("lista", marcasAgregadas);
						Sessions.getCurrent().setAttribute("grafica",
								mapaGrafica);
						ventana = (Window) Executions.createComponents(
								"/vistas/reportes/VGrafica.zul", null,
								mapaGrafica);
						ventana.doModal();
						break;
					case "Grafico Vendido VS Planificado Marcas (Angular)":
						tipo = 25;
						mapaGrafica = new HashMap<String, Object>();
						mapaGrafica.put("idAliado", aliado);
						mapaGrafica.put("desde", desde);
						mapaGrafica.put("hasta", hasta);
						mapaGrafica.put("tipo", "gauge");
						mapaGrafica.put("lista", marcasAgregadas);
						Sessions.getCurrent().setAttribute("grafica",
								mapaGrafica);
						ventana = (Window) Executions.createComponents(
								"/vistas/reportes/VGrafica.zul", null,
								mapaGrafica);
						ventana.doModal();
						break;
					case "Grafico Vendido VS Activado Marcas (Angular)":
						tipo = 25;
						mapaGrafica = new HashMap<String, Object>();
						mapaGrafica.put("idAliado", aliado);
						mapaGrafica.put("desde", desde);
						mapaGrafica.put("hasta", hasta);
						mapaGrafica.put("tipo", "gauge");
						mapaGrafica.put("tipo2", "activado");
						mapaGrafica.put("lista", marcasAgregadas);
						Sessions.getCurrent().setAttribute("grafica",
								mapaGrafica);
						ventana = (Window) Executions.createComponents(
								"/vistas/reportes/VGrafica.zul", null,
								mapaGrafica);
						ventana.doModal();
						break;
					}

					if (tipo != 23 && tipo != 25) {
						Clients.evalJavaScript("window.open('"
								+ damePath()
								+ "Generador?valor1="
								+ tipo
								+ "&valor2="
								+ aliado
								+ "&valor3="
								+ zona
								+ "&valor4="
								+ cliente
								+ "&valor5="
								+ vendedor
								+ "&valor6="
								+ fecha1
								+ "&valor7="
								+ fecha2
								+ "&valor8="
								+ tipoReporte
								+ "','','top=100,left=200,height=600,width=800,scrollbars=1,resizable=1')");
					}
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
		guardar.setLabel("Generar");
		guardar.setImage("/public/imagenes/botones/reporte.png");
		botonera.getChildren().get(0).setVisible(false);
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(2).setVisible(false);
		botonera.getChildren().get(4).setVisible(false);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botoneraReporte.appendChild(botonera);
	}

	private void llenarLista() {
		marcasAgregadas.clear();
		ltbMarcasAgregadas.getItems().clear();
		marcas = servicioMarca.buscarTodosOrdenados();
		ltbMarcas.setModel(new ListModelList<MaestroMarca>(marcas));
		listasMultiples();
	}

	private void listasMultiples() {
		ltbMarcas.setMultiple(false);
		ltbMarcas.setCheckmark(false);
		ltbMarcas.setMultiple(true);
		ltbMarcas.setCheckmark(true);

		ltbMarcasAgregadas.setMultiple(false);
		ltbMarcasAgregadas.setCheckmark(false);
		ltbMarcasAgregadas.setMultiple(true);
		ltbMarcasAgregadas.setCheckmark(true);
	}

	public byte[] reporte(String tipo, String aliado, String zona,
			String cliente, String vendedor, String desde, String hasta) {
		byte[] fichero = null;
		conexion = null;
		try {
			MaestroAliado aliadoObjeto = getServicioAliado().buscar(aliado);
			ClassLoader cl = this.getClass().getClassLoader();
			InputStream fis = null;

			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("aliado", aliado);
			parameters.put("cliente", cliente);
			parameters.put("fecha_desde", desde);
			parameters.put("fecha_hasta", hasta);
			parameters.put("naliado", aliadoObjeto.getNombre());
			parameters.put("vendedor", vendedor);
			parameters.put("zona", zona);

			List<String> lista = obtenerPropiedades();
			String user = lista.get(0);
			String password = lista.get(1);
			String url = lista.get(2);
			List<Configuracion> configuracion = new ArrayList<Configuracion>();
			Integer numeroClientes = 0;
			Integer sugerido = 0;
			DateFormat fechaF = new SimpleDateFormat("yyyy-MM-dd");

			Date fecha1 = new Date();
			Date fecha2 = new Date();
			try {
				fecha1 = fechaF.parse(desde);
				fecha2 = fechaF.parse(hasta);
			} catch (ParseException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			switch (tipo) {
			case "1":
				fis = (cl.getResourceAsStream("/reporte/R55420001.jasper"));
				break;
			case "2":
				fis = (cl.getResourceAsStream("/reporte/R55420002.jasper"));
				break;
			case "3":
				fis = (cl.getResourceAsStream("/reporte/R55420003.jasper"));
				break;
			case "4":
				numeroClientes = getServicioVenta().contarPorAliadoEntreFechas(
						aliado, fecha1, fecha2);
				parameters.put("numclientes", numeroClientes);
				fis = (cl.getResourceAsStream("/reporte/R55420005.jasper"));
				break;
			case "5":
				numeroClientes = getServicioVenta().contarPorAliadoEntreFechas(
						aliado, fecha1, fecha2);
				parameters.put("numclientes", numeroClientes);
				fis = (cl.getResourceAsStream("/reporte/R55420006.jasper"));
				break;
			case "6":
				numeroClientes = getServicioVenta().contarPorAliadoEntreFechas(
						aliado, fecha1, fecha2);
				parameters.put("numclientes", numeroClientes);
				fis = (cl.getResourceAsStream("/reporte/R55420007.jasper"));
				break;
			case "7":
				numeroClientes = getServicioVenta().contarPorAliadoEntreFechas(
						aliado, fecha1, fecha2);
				parameters.put("numclientes", numeroClientes);
				fis = (cl.getResourceAsStream("/reporte/R55420008.jasper"));
				break;
			case "8":
				fis = (cl.getResourceAsStream("/reporte/R55420009.jasper"));
				break;
			case "9":
				fis = (cl.getResourceAsStream("/reporte/R55420010.jasper"));
				break;
			case "10":
				fis = (cl.getResourceAsStream("/reporte/R55420011.jasper"));
				break;
			case "11":
				fis = (cl.getResourceAsStream("/reporte/R55420014.jasper"));
				break;
			case "12":
				fis = (cl.getResourceAsStream("/reporte/R55420016.jasper"));
				break;
			case "13":
				fis = (cl.getResourceAsStream("/reporte/R55420017.jasper"));
				break;
			case "14":
				fis = (cl.getResourceAsStream("/reporte/R55420018.jasper"));
				break;
			case "15":
				fis = (cl.getResourceAsStream("/reporte/R55420019.jasper"));
				break;
			case "16":
				fis = (cl.getResourceAsStream("/reporte/R55420020.jasper"));
				break;
			case "17":
				fis = (cl.getResourceAsStream("/reporte/R55420021.jasper"));
				break;
			case "18":
				configuracion = getServicioConfiguracion().buscarTodas();
				if (!configuracion.isEmpty())
					parameters.put("fy_actual", fechaF.format(configuracion
							.get(0).getInicioFyActual()));
				else
					parameters.put("fy_actual", fechaF.format(fecha));
				fis = (cl.getResourceAsStream("/reporte/R55420022.jasper"));
				break;
			case "19":
				configuracion = getServicioConfiguracion().buscarTodas();
				if (!configuracion.isEmpty())
					parameters.put("fy_actual", fechaF.format(configuracion
							.get(0).getInicioFyActual()));
				else
					parameters.put("fy_actual", fechaF.format(fecha));
				fis = (cl.getResourceAsStream("/reporte/R55420023.jasper"));
				break;
			case "20":
				fis = (cl.getResourceAsStream("/reporte/R55420024.jasper"));
				break;
			case "21":
				fis = (cl.getResourceAsStream("/reporte/R55420026.jasper"));
				break;
			case "22":
				fis = (cl.getResourceAsStream("/reporte/R55420012.jasper"));
				break;
			case "24":
				// reporte excel
				List<Venta> ventas = getServicioVenta()
						.buscarPorAliadoEntreFechas(aliado, desde, hasta);
				// fis = (cl.getResourceAsStream("/reporte/R55420025.jasper"));
				JasperReport repor = null;
				try {
					repor = (JasperReport) JRLoader.loadObject(getClass()
							.getResource("/reporte/R55420025.jasper"));
				} catch (JRException e1) {
					e1.printStackTrace();
				}
				JasperPrint jasperPrint = null;
				try {
					jasperPrint = JasperFillManager.fillReport(repor,
							parameters, new JRBeanCollectionDataSource(ventas));
				} catch (JRException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
				JRXlsxExporter exporter = new JRXlsxExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
						xlsReport);
				try {
					exporter.exportReport();
				} catch (JRException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return xlsReport.toByteArray();
			default:
				break;
			}
			conexion = java.sql.DriverManager
					.getConnection(url, user, password);
			try {

				if (fichero == null) {
					fichero = JasperRunManager.runReportToPdf(fis, parameters,
							conexion);
				}

			} catch (JRException ex) {
				ex.printStackTrace();
			}

			if (conexion != null) {
				conexion.close();
			}

		} catch (SQLException e) {
			System.exit(4);
		}
		return fichero;
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
				if (cmbReporte.getText().compareTo("") == 0) {
					msj.mensajeAlerta("Debe seleccionar un Reporte");
					return false;
				} else {
					if (box.isVisible() && marcasAgregadas.isEmpty()) {
						msj.mensajeAlerta("Debe seleccionar al menos una Marca para generar el grafico");
						return false;
					} else {
						if ((cmbReporte.getValue().equals(
								"Grafico Vendido VS Planificado Marcas") || cmbReporte
								.getValue()
								.equals("Grafico Vendido VS Planificado Marcas (Angular)"))
								&& marcasAgregadas.size() != 1) {
							msj.mensajeAlerta("Para este Grafico solo debe agregar una Marca");
							return false;
						} else
							return true;
					}
				}
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
		cmbCliente.setValue("TODOS");
		cmbVendedor.setValue("TODOS");
		cmbZona.setValue("TODAS");
	}

	@Listen("onOpen = #cmbCliente")
	public void buscarClientes() {
		List<String> lista = new ArrayList<String>();
		lista.add("TODOS");
		lista.addAll(servicioVenta.buscarDistinctCliente(idAliado));
		cmbCliente.setModel(new ListModelList<String>(lista));
	}

	@Listen("onOpen = #cmbVendedor")
	public void buscarVendedores() {
		List<String> lista = new ArrayList<String>();
		lista.add("TODOS");
		lista.addAll(servicioVenta.buscarDistinctVendedor(idAliado));
		cmbVendedor.setModel(new ListModelList<String>(lista));
	}

	@Listen("onOpen = #cmbZona")
	public void buscarZoonas() {
		List<String> lista = new ArrayList<String>();
		lista.add("TODAS");
		lista.addAll(servicioVenta.buscarDistinctZona(idAliado));
		cmbZona.setModel(new ListModelList<String>(lista));
	}

	@Listen("onClick = #pasar1")
	public void derecha() {
		List<Listitem> listitemEliminar = new ArrayList<Listitem>();
		List<Listitem> listItem = ltbMarcas.getItems();
		if (listItem.size() != 0) {
			for (int i = 0; i < listItem.size(); i++) {
				if (listItem.get(i).isSelected()) {
					MaestroMarca marca = listItem.get(i).getValue();
					marcas.remove(marca);
					marcasAgregadas.add(marca);
					ltbMarcasAgregadas
							.setModel(new ListModelList<MaestroMarca>(
									marcasAgregadas));
					ltbMarcasAgregadas.renderAll();
					listitemEliminar.add(listItem.get(i));
					listItem.get(i).setSelected(false);
				}
			}
		}
		for (int i = 0; i < listitemEliminar.size(); i++) {
			ltbMarcas.removeItemAt(listitemEliminar.get(i).getIndex());
			ltbMarcas.renderAll();
		}
		listasMultiples();
	}

	@Listen("onClick = #pasar2")
	public void izquierda() {
		List<Listitem> listitemEliminar = new ArrayList<Listitem>();
		List<Listitem> listItem2 = ltbMarcasAgregadas.getItems();
		if (listItem2.size() != 0) {
			for (int i = 0; i < listItem2.size(); i++) {
				if (listItem2.get(i).isSelected()) {
					MaestroMarca marca = listItem2.get(i).getValue();
					marcasAgregadas.remove(marca);
					marcas.add(marca);
					ltbMarcas.setModel(new ListModelList<MaestroMarca>(marcas));
					ltbMarcas.renderAll();
					listitemEliminar.add(listItem2.get(i));
					listItem2.get(i).setSelected(false);
				}
			}
		}
		for (int i = 0; i < listitemEliminar.size(); i++) {
			ltbMarcasAgregadas.removeItemAt(listitemEliminar.get(i).getIndex());
			ltbMarcasAgregadas.renderAll();
		}
		listasMultiples();
	}

	@Listen("onSelect = #cmbReporte")
	public void ocultar() {
		if (cmbReporte.getValue().startsWith("Grafico")) {
			if (!cmbReporte.getValue().equals(
					"Grafico Vendido VS Activado Marcas (Angular)")) {
				llenarLista();
				rowVendedor.setVisible(false);
				rowZona.setVisible(false);
				box.setVisible(true);
			}
		} else {
			rowVendedor.setVisible(true);
			rowZona.setVisible(true);
			box.setVisible(false);
		}
	}

}
