package controlador.transacciones;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import modelo.maestros.MaestroAliado;
import modelo.seguridad.Arbol;
import modelo.termometro.TermometroCliente;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zss.api.CellOperationUtil;
import org.zkoss.zss.api.Exporter;
import org.zkoss.zss.api.Exporters;
import org.zkoss.zss.api.Importer;
import org.zkoss.zss.api.Importers;
import org.zkoss.zss.api.Range;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.api.model.Book;
import org.zkoss.zss.api.model.CellData;
import org.zkoss.zss.api.model.CellStyle;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import componente.Botonera;
import componente.Catalogo;
import componente.Mensaje;
import componente.Validador;
import controlador.maestros.CGenerico;

public class CTermometro extends CGenerico {

	private static final long serialVersionUID = 589262803508771881L;
	@Wire
	private Radio rdoMensual;
	@Wire
	private Radio rdoTrimestral;
	@Wire
	private Radio rdoSemestral;
	@Wire
	private Radio rdoAnual;
	@Wire
	private Textbox txtAliado;
	@Wire
	private Div botoneraTermometro;
	@Wire
	private Div divVTermometro;
	@Wire
	private Div divCatalogoAliado;
	@Wire
	private Row rowTermometro;
	@Wire
	private Combobox cmbAnno;
	@Wire
	private Combobox cmbMes;
	@Wire
	private Spreadsheet ss;
	Catalogo<MaestroAliado> catalogoAliado;
	Calendar calendarioTermometro = Calendar.getInstance();
	int habiles;
	int recorridos;
	int faltantes;
	protected DateFormat formato = new SimpleDateFormat("yyyy-MM");

	private String[] anno = { "2013", "2014", "2015", "2016", "2017", "2018",
			"2019", "2020" };
	private String[] meses = { "Enero", "Febrero", "Marzo", "Abril", "Mayo",
			"Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre",
			"Diciembre" };

	@Override
	public void inicializar() throws IOException {
		Authentication authe = SecurityContextHolder.getContext()
				.getAuthentication();
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(
				authe.getAuthorities());
		for (int i = 0; i < authorities.size(); i++) {
			Arbol arbol;
			String nombre = authorities.get(i).toString();
			if (Validador.validarNumero(nombre)) {
				arbol = servicioArbol.buscar(Long.parseLong(nombre));
				if (arbol.getNombre().equals("Ver Aliados Termometro"))
					rowTermometro.setVisible(true);
			}
		}

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (map != null) {
			if (map.get("tabsGenerales") != null) {
				tabs = (List<Tab>) map.get("tabsGenerales");
				cerrar = (String) map.get("nombre");
				map.clear();
				map = null;
			}
		}

		calendarioTermometro.setTime(fecha);
		cmbAnno.setModel(new ListModelList<String>(anno));
		cmbMes.setModel(new ListModelList<String>(meses));
		cmbMes.setValue(mesString(calendarioTermometro));
		cmbAnno.setValue(String.valueOf(calendarioTermometro.get(Calendar.YEAR)));

		Botonera botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub

			}

			@Override
			public void salir() {
				cerrarVentana(divVTermometro, cerrar, tabs);
			}

			@Override
			public void reporte() {
				// TODO Auto-generated method stub

			}

			@Override
			public void limpiar() {
				calendarioTermometro.setTime(fecha);
				cmbMes.setValue(mesString(calendarioTermometro));
				cmbAnno.setValue(String.valueOf(calendarioTermometro
						.get(Calendar.YEAR)));
				ss.setSrc(null);
			}

			@Override
			public void guardar() {
				if (rowTermometro.isVisible()
						&& txtAliado.getText().compareTo("") == 0) {
					msj.mensajeError("Debe seleccionar un Aliado del catalogo");
				} else {
					if (!rdoAnual.isChecked() && !rdoMensual.isChecked()
							&& !rdoTrimestral.isChecked()
							&& !rdoSemestral.isChecked())
						msj.mensajeError("Debe marcar una de las opciones de Filtrado");
					else {
						MaestroAliado aliado = new MaestroAliado();
						int anno = Integer.parseInt(cmbAnno.getValue());
						int tiempo = 0;
						int periodo = 0;
						int tipo = 0;
						int anno2 = 0;
						periodo = obtenerIntDadoString(cmbMes.getValue());
						// periodo rango de arriba tiempo rando de abajo
						if (rdoMensual.isChecked()) {
							tipo = 1;
							tiempo = periodo;
						}
						if (rdoTrimestral.isChecked()) {
							tipo = 2;
							tiempo = periodo - 2;
							tiempo = obtenerMes(tiempo);
							if (periodo - 2 <= 0)
								anno2 = anno - 1;
						}
						if (rdoSemestral.isChecked()) {
							tipo = 3;
							tiempo = periodo - 5;
							tiempo = obtenerMes(tiempo);
							if (periodo - 5 <= 0)
								anno2 = anno - 1;
						}
						if (rdoAnual.isChecked()) {
							tipo = 4;
							tiempo = periodo - 11;
							tiempo = obtenerMes(tiempo);
							if (periodo - 11 <= 0)
								anno2 = anno - 1;
						}
						if (rowTermometro.isVisible())
							aliado = servicioAliado
									.buscar(txtAliado.getValue());
						else
							aliado = servicioAliado
									.buscarPorLoginUsuario(nombreUsuarioSesion());
						if (aliado != null) {
							calcularDias(anno, tiempo);
							ss.setSrc("/public/plantilla.xlsx");
							Range range = Ranges.range(ss.getSelectedSheet(),
									1, 4);
							range.setCellValue(recorridos);
							range = Ranges.range(ss.getSelectedSheet(), 1, 5);
							range.setCellValue(habiles);
							range = Ranges.range(ss.getSelectedSheet(), 1, 6);
							range.setCellValue(faltantes);
							range = Ranges.range(ss.getSelectedSheet(), 2, 3);
							range.setCellValue(cmbMes.getValue());
							range = Ranges.range(ss.getSelectedSheet(), 1, 0);
							range.setCellValue(aliado.getNombre());
							List<TermometroCliente> lista = new ArrayList<TermometroCliente>();
							switch (tipo) {
							case 1:
								lista = servicioTermometro.buscarPorAliadoYMes(
										aliado, tiempo, anno);
								break;
							case 2:
							case 3:
							case 4:
								lista = servicioTermometro
										.buscarPorAliadoEntreMeses(aliado,
												tiempo, periodo, anno, anno2);
								break;
							}
							generarTermometro(lista, tipo);
						} else
							msj.mensajeError("Su usuario no esta asociado a ningun Aliado, "
									+ "pongase en contacto con el Administrador del sistema");
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
		guardar.setSrc("/public/imagenes/botones/reporte.png");
		botonera.getChildren().get(0).setVisible(false);
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(2).setVisible(false);
		botonera.getChildren().get(4).setVisible(false);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botoneraTermometro.appendChild(botonera);
	}

	protected void calcularDias(int anno2, int tiempo) {
		String fechaString = anno2 + "-" + tiempo;
		formato.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
		if (tiempo != 12 && tiempo != 11 && tiempo != 10)
			fechaString = anno2 + "-" + "0" + tiempo;
		Date fecha = new Date();
		try {
			fecha = formato.parse(fechaString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int tiempo2 = tiempo + 1;
		String fechaString2 = anno2 + "-" + tiempo2;
		if (tiempo2 != 13 && tiempo2 != 12 && tiempo2 != 11 && tiempo2 != 10)
			fechaString2 = anno2 + "-" + "0" + tiempo2;
		Date fecha2 = new Date();
		try {
			fecha2 = formato.parse(fechaString2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calendario = new GregorianCalendar();
		calendario.setTime(fecha2);
		calendario.add(Calendar.DAY_OF_YEAR, -1);
		calendario.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
		fecha2 = calendario.getTime();
		habiles = obtenerDiasHabiles(fecha, fecha2);
		Date fechaHoy = null;
		calendario = Calendar.getInstance();
		calendario.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
		fechaHoy = calendario.getTime();
		recorridos = 0;
		faltantes = 0;
		if (fechaHoy.after(fecha)) {
			recorridos = obtenerDiasHabiles(fecha, fechaHoy);
			if (recorridos >= habiles)
				recorridos = 0;
			else
				faltantes = habiles - recorridos;
		} else {
			faltantes = habiles;
			recorridos = 0;
		}
	}

	protected void generarTermometro(List<TermometroCliente> lista, int tipo) {
		if (!lista.isEmpty()) {
			ss.setMaxVisibleRows(lista.size() + 10);
			switch (tipo) {
			case 1:
				for (int i = 0; i < lista.size(); i++) {
					Range range = Ranges.range(ss.getSelectedSheet(), i + 3, 0);
//					sombrear footer
					range.setCellValue(lista.get(i).getZona());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 1);
					range.setCellValue(lista.get(i).getVendedor());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 2);
					range.setCellValue(lista.get(i).getMarca());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 3);
					range.setCellValue(lista.get(i).getMes1());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 4);
					range.setCellValue(lista.get(i).getCuota());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 5);
					range.setCellValue(lista.get(i).getVendido());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 6);
					range.setCellValue(lista.get(i).getPorcentaje());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 7);
					range.setCellValue(lista.get(i).getExcendente());
					if (lista.get(i).getExcendente() >= 0)
						CellOperationUtil
								.applyFontColor(range, "#34CB2C");
					else
						CellOperationUtil
								.applyFontColor(range, "#F10303");
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 8);
					range.setCellValue(lista.get(i).getSugerido());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 9);
					range.setCellValue(lista.get(i).getMeta());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 10);
					range.setCellValue(lista.get(i).getProyeccion());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 11);
					if (lista.get(i).getVendido() >= lista.get(i).getCuota())
						CellOperationUtil
								.applyBackgroundColor(range, "#34CB2C");
					else
						CellOperationUtil
								.applyBackgroundColor(range, "#F10303");
				}
				break;
			case 2:

				break;
			case 3:

				break;
			case 4:

				break;
			}
		} else
			msj.mensajeAlerta("No existen registros para esta seleccion");
	}

	@Listen("onClick = #btnBuscarAliado")
	public void mostrarCatalogoAliado() {
		final List<MaestroAliado> listaObjetos = servicioAliado
				.buscarTodosOrdenados();
		catalogoAliado = new Catalogo<MaestroAliado>(divCatalogoAliado,
				"Aliado", listaObjetos, false, false, false, "Codigo",
				"Nombre", "Zona", "Vendedor") {

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
		catalogoAliado.setTitle("Registros");
		catalogoAliado.setParent(divCatalogoAliado);
		catalogoAliado.doModal();
	}

	@Listen("onSeleccion = #divCatalogoAliado")
	public void seleccionAliado() {
		MaestroAliado aliado = catalogoAliado.objetoSeleccionadoDelCatalogo();
		txtAliado.setValue(aliado.getCodigoAliado());
		catalogoAliado.setParent(null);
	}

	@Listen("onChange = #txtAliado")
	public void buscarNombreAliado() {
		MaestroAliado aliado = servicioAliado.buscar(txtAliado.getValue());
		if (aliado != null) {
			txtAliado.setValue(aliado.getCodigoAliado());
		} else {
			msj.mensajeAlerta(Mensaje.noHayRegistros);
			txtAliado.setValue("");
			txtAliado.setFocus(true);
		}
	}

	@Listen("onClick=#btnExportar")
	public void expor() {
		Exporter exporter = Exporters.getExporter();
		Book book = ss.getBook();
		File file = null;
		try {
			file = File.createTempFile(
					Long.toString(System.currentTimeMillis()), "temp");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			exporter.export(book, fos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			Filedownload.save(new AMedia(book.getBookName(), null, null, file,
					true));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
