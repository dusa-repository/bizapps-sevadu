package controlador.transacciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import modelo.maestros.Cliente;
import modelo.maestros.Existencia;
import modelo.maestros.F0005;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroMarca;
import modelo.maestros.MaestroProducto;
import modelo.maestros.MarcaActivadaVendedor;
import modelo.maestros.PlanVenta;
import modelo.maestros.TipoCliente;
import modelo.maestros.Venta;
import modelo.pk.ExistenciaPK;
import modelo.pk.MarcaActivadaPK;
import modelo.pk.PlanVentaPK;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tab;

import componente.Botonera;
import componente.Mensaje;
import componente.Validador;
import controlador.maestros.CGenerico;

public class CCargarArchivo extends CGenerico {

	private static final long serialVersionUID = -9027592729093537968L;
	@Wire
	private Div botoneraCargarRegistro;
	@Wire
	private Groupbox gpxCargarRegistro;
	@Wire
	private Div divVCargarArchivo;
	@Wire
	private Label lblNombre;
	@Wire
	private org.zkoss.zul.Row row;
	@Wire
	private Label lblDescripcion;
	private Media media;
	private String titulo;
	private Integer tipo;
	private String valorNoEncontrado = "El valor que hace referencia no se ha encontrado en la tabla:";
	private String udcNoEncontrada = "La siguiente UDC no fue encontrada.";
	private String errorLongitud = "La siguiente ubicacion excede el limite establecido de longitud:";
	private String archivoConError = "Existe un error en el siguiente archivo adjunto: ";

	@Override
	public void inicializar() throws IOException {
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (map != null) {
			if (map.get("tabsGenerales") != null) {
				tabs = (List<Tab>) map.get("tabsGenerales");
				titulo = (String) map.get("titulo");
				map.clear();
				map = null;
			}
		}
		gpxCargarRegistro.setTitle(titulo);
		switch (titulo) {
		case "Subir Archivo de Ventas":
			tipo = 1;
			break;

		case "Subir Archivo de Plan de Ventas":
			tipo = 2;
			break;

		case "Subir Archivo de Existencias":
			tipo = 3;
			break;

		case "Subir Archivo de Cartera de Clientes":
			tipo = 4;
			break;

		case "Subir Archivo de Activacion de Marca":
			tipo = 5;
			break;
		}
		Botonera botonera = new Botonera() {

			@Override
			public void seleccionar() {
			}

			@Override
			public void salir() {
				cerrarVentana(divVCargarArchivo, titulo, tabs);
			}

			@Override
			public void reporte() {
			}

			@Override
			public void limpiar() {
				if (row.getChildren().size() == 4) {
					A linea = (A) row.getChildren().get(3);
					Events.postEvent("onClick", linea, null);
				}
			}

			@Override
			public void guardar() {
				if (media != null) {
					switch (tipo) {
					case 1:
						importarVentas();
						break;
					case 2:
						importarPlanVentas();
						break;
					case 3:
						importarExistencia();
						break;
					case 4:
						importarCartera();
						break;
					case 5:
						importarActivacion();
						break;
					}
				} else
					msj.mensajeAlerta("El siguiente archivo no posee registros, por lo tanto no fue importado."
							+ " " + lblDescripcion.getValue());
			}

			@Override
			public void eliminar() {
			}

			@Override
			public void buscar() {
			}

			@Override
			public void ayuda() {
			}

			@Override
			public void annadir() {
			}
		};
		botonera.getChildren().get(0).setVisible(false);
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(2).setVisible(false);
		botonera.getChildren().get(4).setVisible(false);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botoneraCargarRegistro.appendChild(botonera);
	}

	protected void importarActivacion() {
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(media.getStreamData());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		XSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		String mostrarError = "";
		boolean error = false;
		boolean errorLong = false;
		if (rowIterator.hasNext()) {
			List<MarcaActivadaVendedor> marcas = new ArrayList<MarcaActivadaVendedor>();
			int contadorRow = 0;
			boolean entro = false;
			while (rowIterator.hasNext()) {
				contadorRow = contadorRow + 1;
				Row row = rowIterator.next();
				if (!entro) {
					row = rowIterator.next();
					contadorRow = contadorRow + 1;
					entro = true;
				}
				MarcaActivadaVendedor marcaActivada = new MarcaActivadaVendedor();
				MaestroAliado aliado = new MaestroAliado();
				String idAliado = null;
				Double refAliado = (double) 0;
				Cliente cliente = new Cliente();
				String idCliente = null;
				Double refCliente = (double) 0;
				Double marca1 = null, marca2 = null, marca3 = null, marca4 = null, marca5 = null, marca6 = null, marca7 = null, marca8 = null, marca9 = null, marca10 = null, marca11 = null, marca12 = null, marca13 = null, marca14 = null, marca15 = null, marca16 = null, marca17 = null, marca18 = null, marca19 = null, marca20 = null, marca21 = null, marca22 = null, marca23 = null, marca24 = null, marca25 = null, marca26 = null, marca27 = null, marca28 = null, marca29 = null, marca30 = null, marca31 = null, marca32 = null, marca33 = null, marca34 = null, marca35 = null, marca36 = null, marca37 = null, marca38 = null, marca39 = null, marca40 = null, marca41 = null, marca42 = null, marca43 = null, marca44 = null, marca45 = null, marca46 = null, marca47 = null, marca48 = null, marca49 = null, marca50 = null;
				Double total = null;
				Double porcentaje = null;
				String campo1 = null;
				String campo2 = null;
				Iterator<Cell> cellIterator = row.cellIterator();
				int contadorCell = 0;
				while (cellIterator.hasNext()) {
					contadorCell = contadorCell + 1;
					Cell cell = cellIterator.next();
					switch (cell.getColumnIndex()) {
					case 0:
						idAliado = obtenerStringCualquiera(cell, refAliado,
								idAliado);
						if (idAliado != null) {
							if (idAliado.length() > 50) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								aliado = servicioAliado.buscar(idAliado);
								if (aliado == null) {
									mostrarError = mensajeErrorNoEncontrado(
											mostrarError, idAliado,
											contadorRow, contadorCell, "Aliado");
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 1:
						idCliente = obtenerStringCualquiera(cell, refCliente,
								idCliente);
						if (idCliente != null) {
							if (idCliente.length() > 50) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								cliente = servicioCliente
										.buscarPorCodigoYAliado(idCliente,
												aliado);
								if (cliente == null) {
									mostrarError = mensajeErrorNoEncontrado(
											mostrarError, idCliente,
											contadorRow, contadorCell,
											"Cliente");
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 2:
						if (cell.getCellType() == 0) {
							marca1 = cell.getNumericCellValue();
							if (marca1 != 0 && marca1 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 3:
						if (cell.getCellType() == 0) {
							marca2 = cell.getNumericCellValue();
							if (marca2 != 0 && marca2 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 4:
						if (cell.getCellType() == 0) {
							marca3 = cell.getNumericCellValue();
							if (marca3 != 0 && marca3 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 5:
						if (cell.getCellType() == 0) {
							marca4 = cell.getNumericCellValue();
							if (marca4 != 0 && marca4 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 6:
						if (cell.getCellType() == 0) {
							marca5 = cell.getNumericCellValue();
							if (marca5 != 0 && marca5 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 7:
						if (cell.getCellType() == 0) {
							marca6 = cell.getNumericCellValue();
							if (marca6 != 0 && marca6 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 8:
						if (cell.getCellType() == 0) {
							marca7 = cell.getNumericCellValue();
							if (marca7 != 0 && marca7 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 9:
						if (cell.getCellType() == 0) {
							marca8 = cell.getNumericCellValue();
							if (marca8 != 0 && marca8 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 10:
						if (cell.getCellType() == 0) {
							marca9 = cell.getNumericCellValue();
							if (marca9 != 0 && marca9 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 11:
						if (cell.getCellType() == 0) {
							marca10 = cell.getNumericCellValue();
							if (marca10 != 0 && marca10 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 12:
						if (cell.getCellType() == 0) {
							marca11 = cell.getNumericCellValue();
							if (marca11 != 0 && marca11 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 13:
						if (cell.getCellType() == 0) {
							marca12 = cell.getNumericCellValue();
							if (marca12 != 0 && marca12 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 14:
						if (cell.getCellType() == 0) {
							marca13 = cell.getNumericCellValue();
							if (marca13 != 0 && marca13 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 15:
						if (cell.getCellType() == 0) {
							marca14 = cell.getNumericCellValue();
							if (marca14 != 0 && marca14 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 16:
						if (cell.getCellType() == 0) {
							marca15 = cell.getNumericCellValue();
							if (marca15 != 0 && marca15 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 17:
						if (cell.getCellType() == 0) {
							marca16 = cell.getNumericCellValue();
							if (marca16 != 0 && marca16 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 18:
						if (cell.getCellType() == 0) {
							marca17 = cell.getNumericCellValue();
							if (marca17 != 0 && marca17 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 19:
						if (cell.getCellType() == 0) {
							marca18 = cell.getNumericCellValue();
							if (marca18 != 0 && marca18 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 20:
						if (cell.getCellType() == 0) {
							marca19 = cell.getNumericCellValue();
							if (marca19 != 0 && marca19 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 21:
						if (cell.getCellType() == 0) {
							marca20 = cell.getNumericCellValue();
							if (marca20 != 0 && marca20 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 22:
						if (cell.getCellType() == 0) {
							marca21 = cell.getNumericCellValue();
							if (marca21 != 0 && marca21 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 23:
						if (cell.getCellType() == 0) {
							marca22 = cell.getNumericCellValue();
							if (marca22 != 0 && marca22 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 24:
						if (cell.getCellType() == 0) {
							marca23 = cell.getNumericCellValue();
							if (marca23 != 0 && marca23 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 25:
						if (cell.getCellType() == 0) {
							marca24 = cell.getNumericCellValue();
							if (marca24 != 0 && marca24 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 26:
						if (cell.getCellType() == 0) {
							marca25 = cell.getNumericCellValue();
							if (marca25 != 0 && marca25 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 27:
						if (cell.getCellType() == 0) {
							marca26 = cell.getNumericCellValue();
							if (marca26 != 0 && marca26 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 28:
						if (cell.getCellType() == 0) {
							marca27 = cell.getNumericCellValue();
							if (marca27 != 0 && marca27 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 29:
						if (cell.getCellType() == 0) {
							marca28 = cell.getNumericCellValue();
							if (marca28 != 0 && marca28 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 30:
						if (cell.getCellType() == 0) {
							marca29 = cell.getNumericCellValue();
							if (marca29 != 0 && marca29 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 31:
						if (cell.getCellType() == 0) {
							marca30 = cell.getNumericCellValue();
							if (marca30 != 0 && marca30 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 32:
						if (cell.getCellType() == 0) {
							marca31 = cell.getNumericCellValue();
							if (marca31 != 0 && marca31 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 33:
						if (cell.getCellType() == 0) {
							marca32 = cell.getNumericCellValue();
							if (marca32 != 0 && marca32 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 34:
						if (cell.getCellType() == 0) {
							marca33 = cell.getNumericCellValue();
							if (marca33 != 0 && marca33 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 35:
						if (cell.getCellType() == 0) {
							marca34 = cell.getNumericCellValue();
							if (marca34 != 0 && marca34 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 36:
						if (cell.getCellType() == 0) {
							marca35 = cell.getNumericCellValue();
							if (marca35 != 0 && marca35 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 37:
						if (cell.getCellType() == 0) {
							marca36 = cell.getNumericCellValue();
							if (marca36 != 0 && marca36 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 38:
						if (cell.getCellType() == 0) {
							marca37 = cell.getNumericCellValue();
							if (marca37 != 0 && marca37 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 39:
						if (cell.getCellType() == 0) {
							marca38 = cell.getNumericCellValue();
							if (marca38 != 0 && marca38 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 40:
						if (cell.getCellType() == 0) {
							marca39 = cell.getNumericCellValue();
							if (marca39 != 0 && marca39 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 41:
						if (cell.getCellType() == 0) {
							marca40 = cell.getNumericCellValue();
							if (marca40 != 0 && marca40 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 42:
						if (cell.getCellType() == 0) {
							marca41 = cell.getNumericCellValue();
							if (marca41 != 0 && marca41 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 43:
						if (cell.getCellType() == 0) {
							marca42 = cell.getNumericCellValue();
							if (marca42 != 0 && marca42 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 44:
						if (cell.getCellType() == 0) {
							marca43 = cell.getNumericCellValue();
							if (marca43 != 0 && marca43 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 45:
						if (cell.getCellType() == 0) {
							marca44 = cell.getNumericCellValue();
							if (marca44 != 0 && marca44 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 46:
						if (cell.getCellType() == 0) {
							marca45 = cell.getNumericCellValue();
							if (marca45 != 0 && marca45 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 47:
						if (cell.getCellType() == 0) {
							marca46 = cell.getNumericCellValue();
							if (marca46 != 0 && marca46 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 48:
						if (cell.getCellType() == 0) {
							marca47 = cell.getNumericCellValue();
							if (marca47 != 0 && marca47 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 49:
						if (cell.getCellType() == 0) {
							marca48 = cell.getNumericCellValue();
							if (marca48 != 0 && marca48 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 50:
						if (cell.getCellType() == 0) {
							marca49 = cell.getNumericCellValue();
							if (marca49 != 0 && marca49 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 51:
						if (cell.getCellType() == 0) {
							marca50 = cell.getNumericCellValue();
							if (marca50 != 0 && marca50 != 1) {
								mostrarError = mensajeErrorNull(mostrarError,
										contadorRow, contadorCell);
								error = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 52:
						if (cell.getCellType() == 0) {
							total = cell.getNumericCellValue();
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 53:
						if (cell.getCellType() == 0) {
							porcentaje = cell.getNumericCellValue();
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 54:
						if (cell.getCellType() == 1) {
							campo1 = cell.getStringCellValue();
							if (campo1 != null)
								if (campo1.length() > 50) {
									mostrarError = mensajeErrorLongitud(
											mostrarError, contadorRow,
											contadorCell);
									errorLong = true;
								}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 55:
						if (cell.getCellType() == 1) {
							campo2 = cell.getStringCellValue();
							if (campo2 != null)
								if (campo2.length() > 50) {
									mostrarError = mensajeErrorLongitud(
											mostrarError, contadorRow,
											contadorCell);
									errorLong = true;
								}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					}
				}
				if (!error && !errorLong && aliado != null && marca1 != null
						&& marca2 != null && marca3 != null && marca4 != null
						&& marca5 != null && marca6 != null && marca7 != null
						&& marca8 != null && marca9 != null && marca10 != null
						&& marca11 != null && marca12 != null
						&& marca13 != null && marca14 != null
						&& marca15 != null && marca16 != null
						&& marca17 != null && marca18 != null
						&& marca19 != null && marca20 != null
						&& marca21 != null && marca22 != null
						&& marca23 != null && marca24 != null
						&& marca25 != null && marca26 != null
						&& marca27 != null && marca28 != null
						&& marca29 != null && marca30 != null
						&& marca31 != null && marca32 != null
						&& marca33 != null && marca34 != null
						&& marca35 != null && marca36 != null
						&& marca37 != null && marca38 != null
						&& marca39 != null && marca40 != null
						&& marca41 != null && marca42 != null
						&& marca43 != null && marca44 != null
						&& marca45 != null && marca46 != null
						&& marca47 != null && marca48 != null
						&& marca49 != null && marca50 != null && total != null
						&& porcentaje != null && campo1 != null
						&& campo2 != null) {
					MarcaActivadaPK pk = new MarcaActivadaPK();
					pk.setCliente(cliente);
					pk.setMaestroAliado(aliado);
					marcaActivada.setId(pk);
					marcaActivada.setCampoA(campo1);
					marcaActivada.setCampoB(campo2);
					marcaActivada.setMarcaA(marca1.intValue());
					marcaActivada.setMarcaB(marca2.intValue());
					marcaActivada.setMarcaC(marca3.intValue());
					marcaActivada.setMarcaD(marca4.intValue());
					marcaActivada.setMarcaE(marca5.intValue());
					marcaActivada.setMarcaF(marca6.intValue());
					marcaActivada.setMarcaG(marca7.intValue());
					marcaActivada.setMarcaH(marca8.intValue());
					marcaActivada.setMarcaI(marca9.intValue());
					marcaActivada.setMarcaJ(marca10.intValue());
					marcaActivada.setMarcaK(marca11.intValue());
					marcaActivada.setMarcaL(marca12.intValue());
					marcaActivada.setMarcaM(marca13.intValue());
					marcaActivada.setMarcaN(marca14.intValue());
					marcaActivada.setMarcaO(marca15.intValue());
					marcaActivada.setMarcaP(marca16.intValue());
					marcaActivada.setMarcaQ(marca17.intValue());
					marcaActivada.setMarcaR(marca18.intValue());
					marcaActivada.setMarcaS(marca19.intValue());
					marcaActivada.setMarcaT(marca20.intValue());
					marcaActivada.setMarcaU(marca21.intValue());
					marcaActivada.setMarcaV(marca22.intValue());
					marcaActivada.setMarcaW(marca23.intValue());
					marcaActivada.setMarcaX(marca24.intValue());
					marcaActivada.setMarcaY(marca25.intValue());
					marcaActivada.setMarcaZ(marca26.intValue());
					marcaActivada.setMarcaZA(marca27.intValue());
					marcaActivada.setMarcaZB(marca28.intValue());
					marcaActivada.setMarcaZC(marca29.intValue());
					marcaActivada.setMarcaZD(marca30.intValue());
					marcaActivada.setMarcaZE(marca31.intValue());
					marcaActivada.setMarcaZF(marca32.intValue());
					marcaActivada.setMarcaZG(marca33.intValue());
					marcaActivada.setMarcaZH(marca34.intValue());
					marcaActivada.setMarcaZI(marca35.intValue());
					marcaActivada.setMarcaZJ(marca36.intValue());
					marcaActivada.setMarcaZK(marca37.intValue());
					marcaActivada.setMarcaZL(marca38.intValue());
					marcaActivada.setMarcaZM(marca39.intValue());
					marcaActivada.setMarcaZN(marca40.intValue());
					marcaActivada.setMarcaZO(marca41.intValue());
					marcaActivada.setMarcaZP(marca42.intValue());
					marcaActivada.setMarcaZQ(marca43.intValue());
					marcaActivada.setMarcaZR(marca44.intValue());
					marcaActivada.setMarcaZS(marca45.intValue());
					marcaActivada.setMarcaZT(marca46.intValue());
					marcaActivada.setMarcaZU(marca47.intValue());
					marcaActivada.setMarcaZV(marca48.intValue());
					marcaActivada.setMarcaZW(marca49.intValue());
					marcaActivada.setMarcaZX(marca50.intValue());
					marcaActivada.setTotal(total.intValue());
					marcaActivada.setPorcentaje(porcentaje.floatValue());
					marcas.add(marcaActivada);
				}
			}
			if (!error && !errorLong) {
				servicioMarcaActivada.guardarVarios(marcas);
				msj.mensajeInformacion("Archivo importado con exito" + "\n"
						+ "Cantidad de Filas evaluadas:" + (contadorRow - 1)
						+ "\n" + "Cantidad de Filas insertadas:"
						+ (contadorRow - 1));
			} else
				msj.mensajeError("El archivo no ha podido ser importado, causas:"
						+ "\n"
						+ mostrarError
						+ "\n"
						+ "Cantidad de Filas evaluadas:"
						+ (contadorRow - 1)
						+ "\n" + "Cantidad de Filas insertadas: 0");
		}
	}

	protected void importarCartera() {
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(media.getStreamData());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		XSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		String mostrarError = "";
		boolean error = false;
		boolean errorLong = false;
		if (rowIterator.hasNext()) {
			List<Cliente> clientes = new ArrayList<Cliente>();
			int contadorRow = 0;
			boolean entro = false;
			while (rowIterator.hasNext()) {
				contadorRow = contadorRow + 1;
				Row row = rowIterator.next();
				if (!entro) {
					row = rowIterator.next();
					contadorRow = contadorRow + 1;
					entro = true;
				}
				Cliente cliente = new Cliente();
				MaestroAliado aliado = new MaestroAliado();
				String idAliado = null;
				Double refAliado = (double) 0;
				String idCliente = null;
				Double refCliente = (double) 0;
				String nombreCliente = null;
				F0005 zona = new F0005();
				String idZona = null;
				Double refZona = (double) 0;
				F0005 ciudad = new F0005();
				String idCiudad = null;
				Double refCiudad = (double) 0;
				F0005 estado = new F0005();
				String idEstado = null;
				Double refEstado = (double) 0;
				F0005 vendedor = new F0005();
				String idVendedor = null;
				Double refVendedor = (double) 0;
				F0005 supervisor = new F0005();
				String idSupervisor = null;
				Double refSupervisor = (double) 0;
				String segmentacion = null;
				TipoCliente tipoCliente = new TipoCliente();
				String idTipo = null;
				Double refTipo = (double) 0;
				String ruta = null;
				String direccion = null;
				String rif = null;
				Iterator<Cell> cellIterator = row.cellIterator();
				int contadorCell = 0;
				while (cellIterator.hasNext()) {
					contadorCell = contadorCell + 1;
					Cell cell = cellIterator.next();
					switch (cell.getColumnIndex()) {
					case 0:
						idAliado = obtenerStringCualquiera(cell, refAliado,
								idAliado);
						if (idAliado != null) {
							if (idAliado.length() > 50) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								aliado = servicioAliado.buscar(idAliado);
								if (aliado == null) {
									mostrarError = mensajeErrorNoEncontrado(
											mostrarError, idAliado,
											contadorRow, contadorCell, "Aliado");
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 1:
						idCliente = obtenerStringCualquiera(cell, refCliente,
								idCliente);
						if (idCliente != null) {
							if (idCliente.length() > 50) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 2:
						if (cell.getCellType() == 1) {
							nombreCliente = cell.getStringCellValue();
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						if (nombreCliente != null)
							if (nombreCliente.length() > 100) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							}
						break;
					case 3:
						idZona = obtenerStringCualquiera(cell, refZona, idZona);
						if (idZona != null) {
							if (idZona.length() > 50) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								zona = servicioF0005.buscar("00", "01", idZona);
								if (zona == null) {
									mostrarError = mensajeErrorUdcNoEncontrada(
											mostrarError, "00", "01", idZona,
											contadorRow, contadorCell);
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 4:
						idCiudad = obtenerStringCualquiera(cell, refCiudad,
								idCiudad);
						if (idCiudad != null) {
							if (idCiudad.length() > 100) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								ciudad = servicioF0005.buscar("00", "03",
										idCiudad);
								if (ciudad == null) {
									mostrarError = mensajeErrorUdcNoEncontrada(
											mostrarError, "00", "03", idCiudad,
											contadorRow, contadorCell);
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 5:
						idEstado = obtenerStringCualquiera(cell, refEstado,
								idEstado);
						if (idEstado != null) {
							if (idEstado.length() > 50) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								estado = servicioF0005.buscar("00", "02",
										idEstado);
								if (estado == null) {
									mostrarError = mensajeErrorUdcNoEncontrada(
											mostrarError, "00", "02", idEstado,
											contadorRow, contadorCell);
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 6:
						idVendedor = obtenerStringCualquiera(cell, refVendedor,
								idVendedor);
						if (idVendedor != null) {
							if (idVendedor.length() > 100) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								vendedor = servicioF0005.buscar("00", "00",
										idVendedor);
								if (vendedor == null) {
									mostrarError = mensajeErrorUdcNoEncontrada(
											mostrarError, "00", "00",
											idVendedor, contadorRow,
											contadorCell);
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 7:
						idSupervisor = obtenerStringCualquiera(cell,
								refSupervisor, idSupervisor);
						if (idSupervisor != null) {
							if (idSupervisor.length() > 100) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								supervisor = servicioF0005.buscar("00", "00",
										idSupervisor);
								if (supervisor == null) {
									mostrarError = mensajeErrorUdcNoEncontrada(
											mostrarError, "00", "00",
											idSupervisor, contadorRow,
											contadorCell);
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 8:
						if (cell.getCellType() == 1) {
							// Validar ON / OFF
							segmentacion = cell.getStringCellValue();
							if (segmentacion != null)
								if (segmentacion.length() > 3) {
									mostrarError = mensajeErrorLongitud(
											mostrarError, contadorRow,
											contadorCell);
									errorLong = true;
								} else {
									if (!segmentacion.equals("ON")
											&& !segmentacion.equals("OFF")) {
										mostrarError = mensajeErrorNull(
												mostrarError, contadorRow,
												contadorCell);
										error = true;
									}
								}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 9:
						idTipo = obtenerStringCualquiera(cell, refTipo, idTipo);
						if (idTipo != null) {
							if (idTipo.length() > 4) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								tipoCliente = servicioTipoCliente
										.buscarPorCodigo(idTipo);
								if (tipoCliente == null) {
									mostrarError = mensajeErrorNoEncontrado(
											mostrarError, idTipo, contadorRow,
											contadorCell, "Tipo de Cliente");
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 10:
						if (cell.getCellType() == 1) {
							ruta = cell.getStringCellValue();
							if (ruta != null)
								if (ruta.length() > 20) {
									mostrarError = mensajeErrorLongitud(
											mostrarError, contadorRow,
											contadorCell);
									errorLong = true;
								}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 11:
						if (cell.getCellType() == 1) {
							direccion = cell.getStringCellValue();
							if (direccion != null)
								if (direccion.length() > 100) {
									mostrarError = mensajeErrorLongitud(
											mostrarError, contadorRow,
											contadorCell);
									errorLong = true;
								}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 12:
						if (cell.getCellType() == 1) {
							rif = cell.getStringCellValue();
							if (rif != null)
								if (rif.length() > 20) {
									mostrarError = mensajeErrorLongitud(
											mostrarError, contadorRow,
											contadorCell);
									errorLong = true;
								}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					}
				}
				if (!error && !errorLong && aliado != null && idCliente != null
						&& nombreCliente != null && zona != null
						&& ciudad != null && estado != null && vendedor != null
						&& supervisor != null && segmentacion != null
						&& tipoCliente != null && ruta != null
						&& direccion != null && rif != null) {
					cliente.setCampo1("");
					cliente.setCampo2("");
					cliente.setCiudad(idCiudad);
					cliente.setCodigoCliente(idCliente);
					cliente.setDireccion(direccion);
					cliente.setEstado(idEstado);
					cliente.setMaestroAliado(aliado);
					cliente.setNombre(nombreCliente);
					cliente.setRif(rif);
					cliente.setRutaDistribucion(ruta);
					cliente.setSegmentacion(segmentacion);
					cliente.setSupervisor(idSupervisor);
					cliente.setTipoCliente(tipoCliente);
					cliente.setVendedor(idVendedor);
					cliente.setZona(idZona);
					cliente.setHoraAuditoria(tiempo);
					cliente.setIdUsuario(nombreUsuarioSesion());
					cliente.setFechaAuditoria(fecha);
					clientes.add(cliente);
				}
			}
			if (!error && !errorLong) {
				servicioCliente.guardarVarios(clientes);
				msj.mensajeInformacion("Archivo importado con exito" + "\n"
						+ "Cantidad de Filas evaluadas:" + (contadorRow - 1)
						+ "\n" + "Cantidad de Filas insertadas:"
						+ (contadorRow - 1));
			} else
				msj.mensajeError("El archivo no ha podido ser importado, causas:"
						+ "\n"
						+ mostrarError
						+ "\n"
						+ "Cantidad de Filas evaluadas:"
						+ (contadorRow - 1)
						+ "\n" + "Cantidad de Filas insertadas: 0");
		}
	}

	protected void importarExistencia() {
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(media.getStreamData());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		XSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		String mostrarError = "";
		boolean error = false;
		boolean errorLong = false;
		if (rowIterator.hasNext()) {
			List<Existencia> existencias = new ArrayList<Existencia>();
			int contadorRow = 0;
			boolean entro = false;
			while (rowIterator.hasNext()) {
				contadorRow = contadorRow + 1;
				Row row = rowIterator.next();
				if (!entro) {
					row = rowIterator.next();
					contadorRow = contadorRow + 1;
					entro = true;
				}
				Existencia existencia = new Existencia();
				MaestroAliado aliado = new MaestroAliado();
				String idAliado = null;
				Double refAliado = (double) 0;
				MaestroProducto producto = new MaestroProducto();
				String idProducto = null;
				Double refProducto = (double) 0;
				Double cantidad = null;
				Date fechaFactura = null;
				Iterator<Cell> cellIterator = row.cellIterator();
				int contadorCell = 0;
				while (cellIterator.hasNext()) {
					contadorCell = contadorCell + 1;
					Cell cell = cellIterator.next();
					switch (cell.getColumnIndex()) {
					case 0:
						if (cell.getCellType() == 0) {
							fechaFactura = cell.getDateCellValue();
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 1:
						idAliado = obtenerStringCualquiera(cell, refAliado,
								idAliado);
						if (idAliado != null) {
							if (idAliado.length() > 50) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								aliado = servicioAliado.buscar(idAliado);
								if (aliado == null) {
									mostrarError = mensajeErrorNoEncontrado(
											mostrarError, idAliado,
											contadorRow, contadorCell, "Aliado");
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 2:
						idProducto = obtenerStringCualquiera(cell, refProducto,
								idProducto);
						if (idProducto != null) {
							if (idProducto.length() > 50) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								producto = servicioProducto.buscar(idProducto);
								if (producto == null) {
									mostrarError = mensajeErrorNoEncontrado(
											mostrarError, idProducto,
											contadorRow, contadorCell,
											"Producto");
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 3:
						if (cell.getCellType() == 0) {
							cantidad = cell.getNumericCellValue();
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					}
				}
				if (!error && !errorLong && aliado != null && producto != null
						&& cantidad != null && fechaFactura != null) {
					ExistenciaPK existenciapk = new ExistenciaPK();
					existenciapk.setMaestroAliado(aliado);
					existenciapk.setMaestroProd(producto);
					existenciapk.setFechaExistencia(fechaFactura);
					existencia.setId(existenciapk);
					existencia.setCantidad(cantidad.intValue());
					existencia.setLoteUpload("");
					existencia.setFechaAuditoria(fecha);
					existencia.setHoraAuditoria(tiempo);
					existencia.setIdUsuario(nombreUsuarioSesion());
					existencias.add(existencia);
				}
			}
			if (!error && !errorLong) {
				for (int i = 0; i < existencias.size(); i++) {
					System.out.println(existencias.get(i).getId()
							.getMaestroProd().getCodigoProductoDusa());
				}
				servicioExistencia.guardarVarios(existencias);
				msj.mensajeInformacion("Archivo importado con exito" + "\n"
						+ "Cantidad de Filas evaluadas:" + (contadorRow - 1)
						+ "\n" + "Cantidad de Filas insertadas:"
						+ (contadorRow - 1));
			} else
				msj.mensajeError("El archivo no ha podido ser importado, causas:"
						+ "\n"
						+ mostrarError
						+ "\n"
						+ "Cantidad de Filas evaluadas:"
						+ (contadorRow - 1)
						+ "\n" + "Cantidad de Filas insertadas: 0");
		}
	}

	protected void importarPlanVentas() {
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(media.getStreamData());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		XSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		String mostrarError = "";
		boolean error = false;
		boolean errorLong = false;
		if (rowIterator.hasNext()) {
			List<PlanVenta> planesVentas = new ArrayList<PlanVenta>();
			int contadorRow = 0;
			boolean entro = false;
			while (rowIterator.hasNext()) {
				contadorRow = contadorRow + 1;
				Row row = rowIterator.next();
				if (!entro) {
					row = rowIterator.next();
					contadorRow = contadorRow + 1;
					entro = true;
				}
				PlanVenta planVenta = new PlanVenta();
				MaestroAliado aliado = new MaestroAliado();
				String idAliado = null;
				Double refAliado = (double) 0;
				MaestroProducto producto = new MaestroProducto();
				String idProducto = null;
				Double refProducto = (double) 0;
				F0005 zona = new F0005();
				String idZona = null;
				Double refZona = (double) 0;
				F0005 vendedor = new F0005();
				String idVendedor = null;
				Double refVendedor = (double) 0;
				Double cantidad = null;
				Double anno = null;
				Double mes = null;
				Iterator<Cell> cellIterator = row.cellIterator();
				int contadorCell = 0;
				while (cellIterator.hasNext()) {
					contadorCell = contadorCell + 1;
					Cell cell = cellIterator.next();
					switch (cell.getColumnIndex()) {
					case 0:
						if (cell.getCellType() == 0) {
							// Validar Cantidad de numeros
							anno = cell.getNumericCellValue();
							if (anno.toString().length() != 4
									&& (anno.intValue() < 1900 || anno
											.intValue() > 2100)) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 1:
						if (cell.getCellType() == 0) {
							// Validar Cantidad de numeros
							mes = cell.getNumericCellValue();
							if (mes.toString().length() != 2
									&& mes.intValue() > 12) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 2:
						idAliado = obtenerStringCualquiera(cell, refAliado,
								idAliado);
						if (idAliado != null) {
							if (idAliado.length() > 50) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								aliado = servicioAliado.buscar(idAliado);
								if (aliado == null) {
									mostrarError = mensajeErrorNoEncontrado(
											mostrarError, idAliado,
											contadorRow, contadorCell, "Aliado");
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 3:
						idZona = obtenerStringCualquiera(cell, refZona, idZona);
						if (idZona != null) {
							if (idZona.length() > 50) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								zona = servicioF0005.buscar("00", "01", idZona);
								if (zona == null) {
									mostrarError = mensajeErrorUdcNoEncontrada(
											mostrarError, "00", "01", idZona,
											contadorRow, contadorCell);
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 4:
						idVendedor = obtenerStringCualquiera(cell, refVendedor,
								idVendedor);
						if (idVendedor != null) {
							if (idVendedor.length() > 100) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								vendedor = servicioF0005.buscar("00", "00",
										idVendedor);
								if (vendedor == null) {
									mostrarError = mensajeErrorUdcNoEncontrada(
											mostrarError, "00", "00",
											idVendedor, contadorRow,
											contadorCell);
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 5:
						idProducto = obtenerStringCualquiera(cell, refProducto,
								idProducto);
						if (idProducto != null) {
							if (idProducto.length() > 50) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								producto = servicioProducto.buscar(idProducto);
								if (producto == null) {
									mostrarError = mensajeErrorNoEncontrado(
											mostrarError, idProducto,
											contadorRow, contadorCell,
											"Producto");
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 6:
						if (cell.getCellType() == 0) {
							cantidad = cell.getNumericCellValue();
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					}
				}
				if (!error && !errorLong && aliado != null && producto != null
						&& anno != null && mes != null && zona != null
						&& cantidad != null) {
					PlanVentaPK planVentaPK = new PlanVentaPK();
					planVentaPK.setAnno(anno.intValue());
					planVentaPK.setMaestroAliado(aliado);
					planVentaPK.setMaestroProducto(producto);
					planVentaPK.setMes(mes.intValue());
					planVentaPK.setVendedorAliado(idVendedor);
					planVentaPK.setZonaAliado(idZona);
					planVenta.setId(planVentaPK);
					planVenta.setCajasPlanificadas(cantidad.intValue());
					planVenta.setLoteUpload("");
					planVenta.setHoraAuditoria(tiempo);
					planVenta.setFechaAuditoria(fecha);
					planVenta.setIdUsuario(nombreUsuarioSesion());
					planesVentas.add(planVenta);
				}
			}
			if (!error && !errorLong) {
				servicioPlan.guardarVarios(planesVentas);
				msj.mensajeInformacion("Archivo importado con exito" + "\n"
						+ "Cantidad de Filas evaluadas:" + (contadorRow - 1)
						+ "\n" + "Cantidad de Filas insertadas:"
						+ (contadorRow - 1));
			} else
				msj.mensajeError("El archivo no ha podido ser importado, causas:"
						+ "\n"
						+ mostrarError
						+ "\n"
						+ "Cantidad de Filas evaluadas:"
						+ (contadorRow - 1)
						+ "\n" + "Cantidad de Filas insertadas: 0");
		}
	}

	protected void importarVentas() {
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(media.getStreamData());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		XSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		String mostrarError = "";
		boolean error = false;
		boolean errorLong = false;
		if (rowIterator.hasNext()) {
			List<Venta> ventas = new ArrayList<Venta>();
			int contadorRow = 0;
			boolean entro = false;
			while (rowIterator.hasNext()) {
				contadorRow = contadorRow + 1;
				Row row = rowIterator.next();
				if (!entro) {
					row = rowIterator.next();
					contadorRow = contadorRow + 1;
					entro = true;
				}
				Venta venta = new Venta();
				MaestroAliado aliado = new MaestroAliado();
				String idAliado = null;
				Double refAliado = (double) 0;
				MaestroProducto producto = new MaestroProducto();
				String idProducto = null;
				Double refProducto = (double) 0;
				MaestroMarca marca = new MaestroMarca();
				String idMarca = null;
				Double refMarca = (double) 0;
				TipoCliente tipoCliente = new TipoCliente();
				String idTipo = null;
				Double refTipo = (double) 0;
				F0005 zona = new F0005();
				String idZona = null;
				Double refZona = (double) 0;
				F0005 ciudad = new F0005();
				String idCiudad = null;
				Double refCiudad = (double) 0;
				F0005 especie = new F0005();
				String idEspecie = null;
				Double refEspecie = (double) 0;
				F0005 vendedor = new F0005();
				String idVendedor = null;
				Double refVendedor = (double) 0;
				F0005 supervisor = new F0005();
				String idSupervisor = null;
				Double refSupervisor = (double) 0;
				Double cantidad = null;
				Double precio = null;
				Cliente cliente = new Cliente();
				String idCliente = null;
				Double refCliente = (double) 0;
				String nombreCliente = null;
				String unidad = null;
				Date fechaFactura = null;
				Double factura = null;
				String segmentacion = null;
				String ruta = null;
				Iterator<Cell> cellIterator = row.cellIterator();
				int contadorCell = 0;
				while (cellIterator.hasNext()) {
					contadorCell = contadorCell + 1;
					Cell cell = cellIterator.next();
					switch (cell.getColumnIndex()) {
					case 0:
						idAliado = obtenerStringCualquiera(cell, refAliado,
								idAliado);
						if (idAliado != null) {
							if (idAliado.length() > 50) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								aliado = servicioAliado.buscar(idAliado);
								if (aliado == null) {
									mostrarError = mensajeErrorNoEncontrado(
											mostrarError, idAliado,
											contadorRow, contadorCell, "Aliado");
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 1:
						idCliente = obtenerStringCualquiera(cell, refCliente,
								idCliente);
						if (idCliente != null) {
							if (idCliente.length() > 50) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								cliente = servicioCliente
										.buscarPorCodigoYAliado(idCliente,
												aliado);
								if (cliente == null) {
									mostrarError = mensajeErrorNoEncontrado(
											mostrarError, idCliente,
											contadorRow, contadorCell,
											"Cliente");
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 2:
						if (cell.getCellType() == 1) {
							nombreCliente = cell.getStringCellValue();
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						if (nombreCliente != null)
							if (nombreCliente.length() > 100) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							}
						break;
					case 3:
						idZona = obtenerStringCualquiera(cell, refZona, idZona);
						if (idZona != null) {
							if (idZona.length() > 50) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								zona = servicioF0005.buscar("00", "01", idZona);
								if (zona == null) {
									mostrarError = mensajeErrorUdcNoEncontrada(
											mostrarError, "00", "01", idZona,
											contadorRow, contadorCell);
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 4:
						idCiudad = obtenerStringCualquiera(cell, refCiudad,
								idCiudad);
						if (idCiudad != null) {
							if (idCiudad.length() > 100) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								ciudad = servicioF0005.buscar("00", "03",
										idCiudad);
								if (ciudad == null) {
									mostrarError = mensajeErrorUdcNoEncontrada(
											mostrarError, "00", "03", idCiudad,
											contadorRow, contadorCell);
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 5:
						idProducto = obtenerStringCualquiera(cell, refProducto,
								idProducto);
						if (idProducto != null) {
							if (idProducto.length() > 50) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								producto = servicioProducto.buscar(idProducto);
								if (producto == null) {
									mostrarError = mensajeErrorNoEncontrado(
											mostrarError, idProducto,
											contadorRow, contadorCell,
											"Producto");
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 6:
						if (cell.getCellType() == 1) {
							unidad = cell.getStringCellValue();
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						if (unidad != null)
							if (unidad.length() > 20) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							}
						break;
					case 7:
						if (cell.getCellType() == 0) {
							cantidad = cell.getNumericCellValue();
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 8:
						idMarca = obtenerStringCualquiera(cell, refMarca,
								idMarca);
						if (idMarca != null) {
							if (idMarca.length() > 50) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								marca = servicioMarca.buscar(idMarca);
								if (marca == null) {
									mostrarError = mensajeErrorNoEncontrado(
											mostrarError, idMarca, contadorRow,
											contadorCell, "Marca");
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 9:
						idEspecie = obtenerStringCualquiera(cell, refEspecie,
								idEspecie);
						if (idEspecie != null) {
							if (idEspecie.length() > 50) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								especie = servicioF0005.buscar("00", "06",
										idEspecie);
								if (especie == null) {
									mostrarError = mensajeErrorUdcNoEncontrada(
											mostrarError, "00", "06",
											idEspecie, contadorRow,
											contadorCell);
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 10:
						if (cell.getCellType() == 0) {
							fechaFactura = cell.getDateCellValue();
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 11:
						idVendedor = obtenerStringCualquiera(cell, refVendedor,
								idVendedor);
						if (idVendedor != null) {
							if (idVendedor.length() > 100) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								vendedor = servicioF0005.buscar("00", "00",
										idVendedor);
								if (vendedor == null) {
									mostrarError = mensajeErrorUdcNoEncontrada(
											mostrarError, "00", "00",
											idVendedor, contadorRow,
											contadorCell);
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 12:
						if (cell.getCellType() == 0) {
							factura = cell.getNumericCellValue();
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 13:
						if (cell.getCellType() == 1) {
							// Validar ON / OFF
							segmentacion = cell.getStringCellValue();
							if (segmentacion != null) {
								if (segmentacion.length() > 3) {
									mostrarError = mensajeErrorLongitud(
											mostrarError, contadorRow,
											contadorCell);
									errorLong = true;
								} else {
									if (!segmentacion.equalsIgnoreCase("ON")
											&& !segmentacion
													.equalsIgnoreCase("OFF")) {
										mostrarError = mensajeErrorNull(
												mostrarError, contadorRow,
												contadorCell);
										error = true;
									}
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 14:
						idTipo = obtenerStringCualquiera(cell, refTipo, idTipo);
						if (idTipo != null) {
							if (idTipo.length() > 4) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								tipoCliente = servicioTipoCliente
										.buscarPorCodigo(idTipo);
								if (tipoCliente == null) {
									mostrarError = mensajeErrorNoEncontrado(
											mostrarError, idTipo, contadorRow,
											contadorCell, "Tipo de Cliente");
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 15:
						if (cell.getCellType() == 1) {
							ruta = cell.getStringCellValue();
							if (ruta != null)
								if (ruta.length() > 20) {
									mostrarError = mensajeErrorLongitud(
											mostrarError, contadorRow,
											contadorCell);
									errorLong = true;
								}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 16:
						idSupervisor = obtenerStringCualquiera(cell,
								refSupervisor, idSupervisor);
						if (idSupervisor != null) {
							if (idSupervisor.length() > 100) {
								mostrarError = mensajeErrorLongitud(
										mostrarError, contadorRow, contadorCell);
								errorLong = true;
							} else {
								supervisor = servicioF0005.buscar("00", "00",
										idSupervisor);
								if (supervisor == null) {
									mostrarError = mensajeErrorUdcNoEncontrada(
											mostrarError, "00", "00",
											idSupervisor, contadorRow,
											contadorCell);
									error = true;
								}
							}
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					case 17:
						if (cell.getCellType() == 0) {
							precio = cell.getNumericCellValue();
						} else {
							mostrarError = mensajeErrorNull(mostrarError,
									contadorRow, contadorCell);
							error = true;
						}
						break;
					}
				}
				if (!error && !errorLong && aliado != null && producto != null
						&& marca != null && tipoCliente != null && zona != null
						&& ciudad != null && especie != null
						&& vendedor != null && supervisor != null
						&& cliente != null && cantidad != null
						&& precio != null && nombreCliente != null
						&& unidad != null && fechaFactura != null
						&& factura != null && segmentacion != null
						&& ruta != null) {
					venta.setCampoAux1("");
					venta.setCampoAux2("");
					venta.setCanalVentas(segmentacion);
					venta.setCantidad(cantidad.floatValue());
					venta.setCiudadAliado(idCiudad);
					venta.setCodigoCliente(cliente);
					venta.setEspecie(idEspecie);
					venta.setFechaAuditoria(fecha);
					venta.setFechaFactura(fechaFactura);
					venta.setHoraAuditoria(tiempo);
					venta.setIdrow(0);
					venta.setIdUsuario(nombreUsuarioSesion());
					venta.setLoteUpload("");
					venta.setMaestroAliado(aliado);
					venta.setMaestroProducto(producto);
					venta.setMarca(idMarca);
					venta.setNombreCliente(nombreCliente);
					venta.setNombreVendedor(idVendedor);
					venta.setNumeroDocumento(factura.toString());
					venta.setPrecio(precio.floatValue());
					venta.setRutaDistribucion(ruta);
					venta.setSupervisor(idSupervisor);
					venta.setTipoClienteBean(tipoCliente);
					venta.setUnidadMedida(unidad);
					venta.setZonaAliado(idZona);
					ventas.add(venta);
				}
			}
			if (!error && !errorLong) {
				servicioVenta.guardarVarios(ventas);
				msj.mensajeInformacion("Archivo importado con exito" + "\n"
						+ "Cantidad de Filas evaluadas:" + (contadorRow - 1)
						+ "\n" + "Cantidad de Filas insertadas:"
						+ (contadorRow - 1));
			} else
				msj.mensajeError("El archivo no ha podido ser importado, causas:"
						+ "\n"
						+ mostrarError
						+ "\n"
						+ "Cantidad de Filas evaluadas:"
						+ (contadorRow - 1)
						+ "\n" + "Cantidad de Filas insertadas: 0");
		}
	}

	private String mensajeErrorUdcNoEncontrada(String mostrarError, String sy,
			String rt, String idSupervisor, int contadorRow, int contadorCell) {
		return mostrarError + udcNoEncontrada + sy + "," + rt + ","
				+ idSupervisor + ". Fila: " + contadorRow + ". Columna: "
				+ contadorCell + "\n";
	}

	private String mensajeErrorNoEncontrado(String mostrarError,
			String idCliente, int contadorRow, int contadorCell, String nombre) {
		return mostrarError + " El valor " + idCliente
				+ " no se ha encontrado en la tabla  " + nombre + ".  Fila: "
				+ contadorRow + ". Columna: " + contadorCell + "\n";
	}

	private String mensajeErrorNull(String mostrarError, int contadorRow,
			int contadorCell) {
		return mostrarError + archivoConError + ". Fila: " + contadorRow
				+ ". Columna: " + contadorCell + "\n";
	}

	private String mensajeErrorLongitud(String mostrarError, int contadorRow,
			int contadorCell) {
		return mostrarError + errorLongitud + ". Fila: " + contadorRow
				+ ". Columna: " + contadorCell + "\n";
	}

	@Listen("onUpload = #btnImportar")
	public void cargar(UploadEvent event) {
		media = event.getMedia();
		if (media != null && Validador.validarExcel(media)) {
			lblDescripcion.setValue(media.getName());
			final A rm = new A("Remover");
			rm.addEventListener(Events.ON_CLICK,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event event) throws Exception {
							lblDescripcion.setValue("");
							rm.detach();
							media = null;
						}
					});
			row.appendChild(rm);
		} else
			msj.mensajeError(Mensaje.archivoExcel);
	}

	private String obtenerStringCualquiera(Cell cell, Double idReferencia,
			String idRef) {
		if (cell.getCellType() == 0) {
			idReferencia = cell.getNumericCellValue();
			if (idReferencia != null)
				return idRef = String.valueOf(idReferencia.longValue());
			else
				return null;
		} else {
			if (cell.getCellType() == 1) {
				if (!cell.getStringCellValue().equals("NULL"))
					return idRef = cell.getStringCellValue();
				else
					return null;
			}
			return null;
		}
	}

}