package controlador.transacciones;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import modelo.termometro.TermometroCliente;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zss.api.CellOperationUtil;
import org.zkoss.zss.api.Exporter;
import org.zkoss.zss.api.Exporters;
import org.zkoss.zss.api.Range;
import org.zkoss.zss.api.Range.ApplyBorderType;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.api.model.Book;
import org.zkoss.zss.api.model.CellStyle.BorderType;
import org.zkoss.zss.api.model.Font;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Tab;
import org.zkoss.zul.West;

import componente.Botonera;

import controlador.maestros.CGenerico;

public class CTermometroConsolidado extends CGenerico {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Radio rdoMensual;
	@Wire
	private Radio rdoTrimestral;
	@Wire
	private Radio rdoSemestral;
	@Wire
	private Radio rdoAnual;
	@Wire
	private Div botoneraTermometroConsolidado;
	@Wire
	private Div divVTermometroConsolidado;
	@Wire
	private Combobox cmbAnno;
	@Wire
	private Combobox cmbMes;
	@Wire
	private Spreadsheet ss;
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
	String nombre = "";
	West west;

	@Override
	public void inicializar() throws IOException {
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (map != null) {
			if (map.get("tabsGenerales") != null) {
				west = (West) map.get("west");
				tabs = (List<Tab>) map.get("tabsGenerales");
				cerrar = (String) map.get("titulo");
				grxGraficoGeneral = (Groupbox) map.get("grxGraficoGeneral");
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
				west.setOpen(true);
				cerrarVentana(divVTermometroConsolidado, cerrar, tabs,
						grxGraficoGeneral);
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
				nombre = "";
			}

			@Override
			public void guardar() {
				if (!rdoAnual.isChecked() && !rdoMensual.isChecked()
						&& !rdoTrimestral.isChecked()
						&& !rdoSemestral.isChecked())
					msj.mensajeError("Debe marcar una de las opciones de Filtrado");
				else {
					int anno = Integer.parseInt(cmbAnno.getValue());
					int tiempo = 0;
					int periodo = 0;
					int tipo = 0;
					int anno2 = 0;
					int cantidad = 0;
					periodo = obtenerIntDadoString(cmbMes.getValue());
					// periodo rango de arriba tiempo rando de abajo
					if (rdoMensual.isChecked()) {
						cantidad = 1;
						tipo = 1;
						tiempo = periodo;
					}
					if (rdoTrimestral.isChecked()) {
						cantidad = 3;
						tipo = 2;
						tiempo = periodo - 2;
						tiempo = obtenerMes(tiempo);
						if (periodo - 2 <= 0)
							anno2 = anno - 1;
					}
					if (rdoSemestral.isChecked()) {
						cantidad = 6;
						tipo = 3;
						tiempo = periodo - 5;
						tiempo = obtenerMes(tiempo);
						if (periodo - 5 <= 0)
							anno2 = anno - 1;
					}
					if (rdoAnual.isChecked()) {
						cantidad = 12;
						tipo = 4;
						tiempo = periodo - 11;
						tiempo = obtenerMes(tiempo);
						if (periodo - 11 <= 0)
							anno2 = anno - 1;
					}
					switch (tipo) {
					case 1:
						ss.setSrc("/public/plantillaConsolidado.xlsx");
						nombre = "TermometroMensualConsolidado";
						break;
					case 2:
						ss.setSrc("/public/plantilla1Consolidado.xlsx");
						nombre = "TermometroTrimestralConsolidado";
						break;
					case 3:
						ss.setSrc("/public/plantilla2Consolidado.xlsx");
						nombre = "TermometroSemestralConsolidado";
						break;
					case 4:
						ss.setSrc("/public/plantilla3Consolidado.xlsx");
						nombre = "TermometroAnualConsolidado";
						break;
					}
					Range range;
					int columnas = 0;
					if (anno2 != 0) {
						for (int i = tiempo; i < 13; i++) {
							range = Ranges.range(ss.getSelectedSheet(), 2,
									3 + columnas);
							range.setCellValue(mounthStringGivenIntegerValue(i));
							columnas++;
						}
						for (int i = 1; i < periodo + 1; i++) {
							range = Ranges.range(ss.getSelectedSheet(), 2,
									3 + columnas);
							range.setCellValue(mounthStringGivenIntegerValue(i));
							columnas++;
						}
					} else {
						for (int i = tiempo; i < periodo + 1; i++) {
							range = Ranges.range(ss.getSelectedSheet(), 2,
									3 + columnas);
							range.setCellValue(mounthStringGivenIntegerValue(i));
							columnas++;
						}
					}
					range = Ranges.range(ss.getSelectedSheet(), 1, 0);
					range.setCellValue("Aliados/Marcas Consolidados");

					List<TermometroCliente> lista = new ArrayList<TermometroCliente>();
					switch (tipo) {
					case 1:
						lista = servicioTermometro.buscarPorMes(
								tiempo, anno);
						break;
					case 2:
					case 3:
					case 4:
						lista = servicioTermometro.buscarEntreMeses(tiempo, periodo, anno, anno2, tipo);
						break;
					}
					generarTermometro(lista, tipo, tiempo, periodo, anno2);
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
		botoneraTermometroConsolidado.appendChild(botonera);
	}

	protected void generarTermometro(List<TermometroCliente> lista, int tipo,
			int inferior, int superior, int anno2) {
		if (!lista.isEmpty()) {
			ss.setMaxVisibleRows(lista.size() + 10);
			List<Integer> meses = new ArrayList<Integer>();
			if (anno2 != 0) {
				for (int i = inferior; i < 13; i++) {
					meses.add(i);
				}
				for (int i = 1; i < superior + 1; i++) {
					meses.add(i);
				}
			} else {
				for (int i = inferior; i < superior + 1; i++) {
					meses.add(i);
				}
			}
			Range range;
			range = Ranges.range(ss.getSelectedSheet(), 1,
					4 + (meses.size() - 1));
			range.setCellValue(lista.get(0).getRecorridos());
			range = Ranges.range(ss.getSelectedSheet(), 1,
					5 + (meses.size() - 1));
			range.setCellValue(lista.get(0).getHabiles());
			range = Ranges.range(ss.getSelectedSheet(), 1,
					6 + (meses.size() - 1));
			range.setCellValue(lista.get(0).getFaltantes());
			range = Ranges.range(ss.getSelectedSheet(), 0,
					7 + (meses.size() - 1));
			range.setCellValue(lista.get(0).getCampo());
			switch (tipo) {
			case 1:
				ss.setMaxVisibleColumns(12);
				for (int i = 0; i < lista.size(); i++) {
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 0);
					// sombrear footer
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
						CellOperationUtil.applyFontColor(range, "#34CB2C");
					else
						CellOperationUtil.applyFontColor(range, "#F10303");
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
					if (lista.get(i).getZona() == null)
						for (int j = 0; j < 12; j++) {
							range = Ranges.range(ss.getSelectedSheet(), i + 3,
									j);
							CellOperationUtil.applyBackgroundColor(range,
									"#BCE2F6");
							CellOperationUtil.applyFontBoldweight(range,
									Font.Boldweight.BOLD);
							CellOperationUtil.applyBorder(range,
									ApplyBorderType.EDGE_TOP,
									BorderType.DASH_DOT, "#F10303");
							CellOperationUtil.applyBorder(range,
									ApplyBorderType.EDGE_BOTTOM,
									BorderType.DASH_DOT, "#F10303");

						}
				}
				break;
			case 2:
				ss.setMaxVisibleColumns(14);
				for (int i = 0; i < lista.size(); i++) {
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 0);
					// sombrear footer
					range.setCellValue(lista.get(i).getZona());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 1);
					range.setCellValue(lista.get(i).getVendedor());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 2);
					range.setCellValue(lista.get(i).getMarca());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 3);
					range.setCellValue(calcularMes(lista.get(i), meses.get(0)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 4);
					range.setCellValue(calcularMes(lista.get(i), meses.get(1)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 5);
					range.setCellValue(calcularMes(lista.get(i), meses.get(2)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 6);
					range.setCellValue(lista.get(i).getCuota());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 7);
					range.setCellValue(lista.get(i).getVendido());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 8);
					range.setCellValue(lista.get(i).getPorcentaje());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 9);
					range.setCellValue(lista.get(i).getExcendente());
					if (lista.get(i).getExcendente() >= 0)
						CellOperationUtil.applyFontColor(range, "#34CB2C");
					else
						CellOperationUtil.applyFontColor(range, "#F10303");
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 10);
					range.setCellValue(lista.get(i).getSugerido());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 11);
					range.setCellValue(lista.get(i).getMeta());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 12);
					range.setCellValue(lista.get(i).getProyeccion());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 13);
					if (lista.get(i).getVendido() >= lista.get(i).getCuota())
						CellOperationUtil
								.applyBackgroundColor(range, "#34CB2C");
					else
						CellOperationUtil
								.applyBackgroundColor(range, "#F10303");
					if (lista.get(i).getZona() == null)
						for (int j = 0; j < 14; j++) {
							range = Ranges.range(ss.getSelectedSheet(), i + 3,
									j);
							CellOperationUtil.applyBackgroundColor(range,
									"#BCE2F6");
							CellOperationUtil.applyFontBoldweight(range,
									Font.Boldweight.BOLD);
							CellOperationUtil.applyBorder(range,
									ApplyBorderType.EDGE_TOP,
									BorderType.DASH_DOT, "#F10303");
							CellOperationUtil.applyBorder(range,
									ApplyBorderType.EDGE_BOTTOM,
									BorderType.DASH_DOT, "#F10303");

						}
				}

				break;
			case 3:
				ss.setMaxVisibleColumns(17);
				for (int i = 0; i < lista.size(); i++) {
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 0);
					// sombrear footer
					range.setCellValue(lista.get(i).getZona());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 1);
					range.setCellValue(lista.get(i).getVendedor());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 2);
					range.setCellValue(lista.get(i).getMarca());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 3);
					range.setCellValue(calcularMes(lista.get(i), meses.get(0)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 4);
					range.setCellValue(calcularMes(lista.get(i), meses.get(1)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 5);
					range.setCellValue(calcularMes(lista.get(i), meses.get(2)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 6);
					range.setCellValue(calcularMes(lista.get(i), meses.get(3)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 7);
					range.setCellValue(calcularMes(lista.get(i), meses.get(4)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 8);
					range.setCellValue(calcularMes(lista.get(i), meses.get(5)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 9);
					range.setCellValue(lista.get(i).getCuota());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 10);
					range.setCellValue(lista.get(i).getVendido());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 11);
					range.setCellValue(lista.get(i).getPorcentaje());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 12);
					range.setCellValue(lista.get(i).getExcendente());
					if (lista.get(i).getExcendente() >= 0)
						CellOperationUtil.applyFontColor(range, "#34CB2C");
					else
						CellOperationUtil.applyFontColor(range, "#F10303");
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 13);
					range.setCellValue(lista.get(i).getSugerido());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 14);
					range.setCellValue(lista.get(i).getMeta());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 15);
					range.setCellValue(lista.get(i).getProyeccion());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 16);
					if (lista.get(i).getVendido() >= lista.get(i).getCuota())
						CellOperationUtil
								.applyBackgroundColor(range, "#34CB2C");
					else
						CellOperationUtil
								.applyBackgroundColor(range, "#F10303");
					if (lista.get(i).getZona() == null)
						for (int j = 0; j < 17; j++) {
							range = Ranges.range(ss.getSelectedSheet(), i + 3,
									j);
							CellOperationUtil.applyBackgroundColor(range,
									"#BCE2F6");
							CellOperationUtil.applyFontBoldweight(range,
									Font.Boldweight.BOLD);
							CellOperationUtil.applyBorder(range,
									ApplyBorderType.EDGE_TOP,
									BorderType.DASH_DOT, "#F10303");
							CellOperationUtil.applyBorder(range,
									ApplyBorderType.EDGE_BOTTOM,
									BorderType.DASH_DOT, "#F10303");

						}
				}
				break;
			case 4:
				ss.setMaxVisibleColumns(23);
				for (int i = 0; i < lista.size(); i++) {
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 0);
					// sombrear footer
					range.setCellValue(lista.get(i).getZona());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 1);
					range.setCellValue(lista.get(i).getVendedor());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 2);
					range.setCellValue(lista.get(i).getMarca());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 3);
					range.setCellValue(calcularMes(lista.get(i), meses.get(0)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 4);
					range.setCellValue(calcularMes(lista.get(i), meses.get(1)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 5);
					range.setCellValue(calcularMes(lista.get(i), meses.get(2)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 6);
					range.setCellValue(calcularMes(lista.get(i), meses.get(3)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 7);
					range.setCellValue(calcularMes(lista.get(i), meses.get(4)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 8);
					range.setCellValue(calcularMes(lista.get(i), meses.get(5)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 9);
					range.setCellValue(calcularMes(lista.get(i), meses.get(6)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 10);
					range.setCellValue(calcularMes(lista.get(i), meses.get(7)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 11);
					range.setCellValue(calcularMes(lista.get(i), meses.get(8)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 12);
					range.setCellValue(calcularMes(lista.get(i), meses.get(9)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 13);
					range.setCellValue(calcularMes(lista.get(i), meses.get(10)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 14);
					range.setCellValue(calcularMes(lista.get(i), meses.get(11)));
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 15);
					range.setCellValue(lista.get(i).getCuota());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 16);
					range.setCellValue(lista.get(i).getVendido());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 17);
					range.setCellValue(lista.get(i).getPorcentaje());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 18);
					range.setCellValue(lista.get(i).getExcendente());
					if (lista.get(i).getExcendente() >= 0)
						CellOperationUtil.applyFontColor(range, "#34CB2C");
					else
						CellOperationUtil.applyFontColor(range, "#F10303");
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 19);
					range.setCellValue(lista.get(i).getSugerido());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 20);
					range.setCellValue(lista.get(i).getMeta());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 21);
					range.setCellValue(lista.get(i).getProyeccion());
					range = Ranges.range(ss.getSelectedSheet(), i + 3, 22);
					if (lista.get(i).getVendido() >= lista.get(i).getCuota())
						CellOperationUtil
								.applyBackgroundColor(range, "#34CB2C");
					else
						CellOperationUtil
								.applyBackgroundColor(range, "#F10303");
					if (lista.get(i).getZona() == null)
						for (int j = 0; j < 23; j++) {
							range = Ranges.range(ss.getSelectedSheet(), i + 3,
									j);
							CellOperationUtil.applyBackgroundColor(range,
									"#BCE2F6");
							CellOperationUtil.applyFontBoldweight(range,
									Font.Boldweight.BOLD);
							CellOperationUtil.applyBorder(range,
									ApplyBorderType.EDGE_TOP,
									BorderType.DASH_DOT, "#F10303");
							CellOperationUtil.applyBorder(range,
									ApplyBorderType.EDGE_BOTTOM,
									BorderType.DASH_DOT, "#F10303");

						}
				}
				break;
			}
		} else {
			msj.mensajeAlerta("No existen registros para esta seleccion");
			ss.setSrc(null);
		}
	}
	
	private int calcularMes(TermometroCliente termometroCliente, Integer mes) {
		int valor = 0;
		switch (mes) {
		case 1:
			valor = termometroCliente.getMes1();
			break;
		case 2:
			valor = termometroCliente.getMes2();
			break;
		case 3:
			valor = termometroCliente.getMes3();
			break;
		case 4:
			valor = termometroCliente.getMes4();
			break;
		case 5:
			valor = termometroCliente.getMes5();
			break;
		case 6:
			valor = termometroCliente.getMes6();
			break;
		case 7:
			valor = termometroCliente.getMes7();
			break;
		case 8:
			valor = termometroCliente.getMes8();
			break;
		case 9:
			valor = termometroCliente.getMes9();
			break;
		case 10:
			valor = termometroCliente.getMes10();
			break;
		case 11:
			valor = termometroCliente.getMes11();
			break;
		case 12:
			valor = termometroCliente.getMes12();
			break;
		}
		return valor;
	}

	@Listen("onClick=#btnExportar")
	public void expor() {
		if (ss.getBook() != null) {
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
				Filedownload.save(new AMedia(nombre + ".xlsx", null, null,
						file, true));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else
			msj.mensajeAlerta("Debe generar el termometro antes de exportarlo");
	}

}
