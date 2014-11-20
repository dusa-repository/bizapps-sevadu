package controlador.maestros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.F0004;
import modelo.maestros.F0005;
import modelo.pk.F0005PK;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import componente.Botonera;
import componente.Catalogo;
import componente.Mensaje;

public class CF0005 extends CGenerico {

	private static final long serialVersionUID = 4858496432716297913L;
	@Wire
	private Textbox txtSYF0005;
	@Wire
	private Textbox txtRTF0005;
	@Wire
	private Textbox txtKYF0005;
	@Wire
	private Textbox txtSPHDF0005;
	@Wire
	private Textbox txtHRDCF0005;
	@Wire
	private Textbox txtDL01F0005;
	@Wire
	private Textbox txtDL02F0005;
	@Wire
	private Div divVF0005;
	@Wire
	private Div botoneraF0005;
	@Wire
	private Div catalogoF0005;
	@Wire
	private Div divCatalogoF0004;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;
	@Wire
	private Label lblDescripcionF0004;
	@Wire
	private Button btnBuscarF0004;
	@Wire
	private Label lblSY;
	@Wire
	private Label lblRT;
	@Wire
	private Label lblF0004;

	String idBoton = "";

	Botonera botonera;
	Catalogo<F0005> catalogo;
	Catalogo<F0004> catalogoF0004;
	F0005PK clave = null;

	@Override
	public void inicializar() throws IOException {
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (map != null) {
			if (map.get("tabsGenerales") != null) {
				tabs = (List<Tab>) map.get("tabsGenerales");
				cerrar = (String) map.get("titulo");
				map.clear();
				map = null;
			}
		}
		txtSYF0005.setFocus(true);
		mostrarCatalogo();

		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						F0005 f05 = catalogo.objetoSeleccionadoDelCatalogo();
						clave = f05.getId();
						abrirRegistro();
						F0004 f04 = servicioF0004.buscar(f05.getId().getDrsy(),
								f05.getId().getDrrt());
						txtRTF0005.setValue(f05.getId().getDrrt());
						txtRTF0005.setDisabled(true);
						txtSYF0005.setValue(f05.getId().getDrsy());
						txtSYF0005.setDisabled(true);
						txtKYF0005.setValue(f05.getId().getDrky());
						txtKYF0005.setDisabled(true);
						txtDL01F0005.setValue(f05.getDrdl01());
						txtSPHDF0005.setValue(f05.getDrsphd());
						txtHRDCF0005.setValue(f05.getDrhrdc());
						txtDL02F0005.setValue(f05.getDrdl02());
						lblDescripcionF0004.setValue(f04.getDtdl01());
						txtDL01F0005.setFocus(true);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}
			}

			@Override
			public void salir() {
				cerrarVentana(divVF0005, cerrar, tabs);

			}

			@Override
			public void reporte() {
			}

			@Override
			public void limpiar() {
				clave = null;
				mostrarBotones(false);
				limpiarCampos();
				habilitarTextClave();

			}

			@Override
			public void guardar() {
				if (validar()) {
					String rt = txtRTF0005.getValue();
					String sy = txtSYF0005.getValue();
					String ky = txtKYF0005.getValue();
					String dl01 = txtDL01F0005.getValue();
					String sphd = txtSPHDF0005.getValue();
					String dl02 = txtDL02F0005.getValue();
					String hrdc = txtHRDCF0005.getValue();
					F0005PK clave = new F0005PK();
					clave.setDrrt(rt);
					clave.setDrsy(sy);
					clave.setDrky(ky);
					F0005 fooo5 = new F0005();
					fooo5.setId(clave);
					fooo5.setDrdl01(dl01);
					fooo5.setDrdl02(dl02);
					fooo5.setDrhrdc(hrdc);
					fooo5.setDrsphd(sphd);
					fooo5.setDruser("user");
					// fooo5.setDrupmj(""); //Fecha
					fooo5.setDrupmt(Double.parseDouble(horaAuditoria));
					servicioF0005.guardar(fooo5);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					catalogo.actualizarLista(
							servicioF0005.buscarTodosOrdenados(), true);
				}

			}

			@Override
			public void eliminar() {
				if (gpxDatos.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<F0005> eliminarLista = catalogo
								.obtenerSeleccionados();
						Messagebox
								.show("¿Desea Eliminar los "
										+ eliminarLista.size() + " Registros?",
										"Alerta",
										Messagebox.OK | Messagebox.CANCEL,
										Messagebox.QUESTION,
										new org.zkoss.zk.ui.event.EventListener<Event>() {
											public void onEvent(Event evt)
													throws InterruptedException {
												if (evt.getName()
														.equals("onOK")) {
													servicioF0005
															.eliminarVarios(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(
															servicioF0005
																	.buscarTodosOrdenados(),
															true);
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (clave != null) {
						Messagebox
								.show(Mensaje.deseaEliminar,
										"Alerta",
										Messagebox.OK | Messagebox.CANCEL,
										Messagebox.QUESTION,
										new org.zkoss.zk.ui.event.EventListener<Event>() {
											public void onEvent(Event evt)
													throws InterruptedException {
												if (evt.getName()
														.equals("onOK")) {
													servicioF0005
															.eliminarUno(clave);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(
															servicioF0005
																	.buscarTodosOrdenados(),
															true);
												}
											}
										});
					} else
						msj.mensajeAlerta(Mensaje.noSeleccionoRegistro);
				}

			}

			@Override
			public void buscar() {
				abrirCatalogo();
			}

			@Override
			public void ayuda() {

			}

			@Override
			public void annadir() {
				abrirRegistro();
				mostrarBotones(false);
			}
		};
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(3).setVisible(false);
		botonera.getChildren().get(5).setVisible(false);
		botoneraF0005.appendChild(botonera);

	}

	public void mostrarBotones(boolean bol) {
		botonera.getChildren().get(1).setVisible(!bol);
		botonera.getChildren().get(2).setVisible(bol);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botonera.getChildren().get(0).setVisible(bol);
		botonera.getChildren().get(3).setVisible(!bol);
		botonera.getChildren().get(5).setVisible(!bol);
	}

	public void limpiarCampos() {
		clave = null;
		txtDL01F0005.setValue("");
		txtSPHDF0005.setValue("");
		txtHRDCF0005.setValue("");
		txtRTF0005.setValue("");
		txtSYF0005.setValue("");
		txtDL02F0005.setValue("");
		txtKYF0005.setValue("");
		txtSYF0005.setFocus(true);
		lblDescripcionF0004.setValue("");
		btnBuscarF0004.setVisible(true);
	}

	public void habilitarTextClave() {
		if (txtRTF0005.isDisabled())
			txtRTF0005.setDisabled(false);
		if (txtSYF0005.isDisabled())
			txtSYF0005.setDisabled(false);
		if (txtKYF0005.isDisabled())
			txtKYF0005.setDisabled(false);
	}

	public boolean validarSeleccion() {
		List<F0005> seleccionados = catalogo.obtenerSeleccionados();
		if (seleccionados == null) {
			msj.mensajeAlerta(Mensaje.noHayRegistros);
			return false;
		} else {
			if (seleccionados.isEmpty()) {
				msj.mensajeAlerta(Mensaje.noSeleccionoItem);
				return false;
			} else {
				return true;
			}
		}
	}

	protected boolean validar() {
		if (clave == null && (claveSYExiste() || claveRTExiste())) {
			return false;
		} else {
			if (!camposLLenos()) {
				msj.mensajeError(Mensaje.camposVacios);
				return false;
			} else
				return true;
		}
	}

	@Listen("onChange = #txtSYF0005; onOK = #txtSYF0005")
	public boolean claveSYExiste() {
		if (servicioF0004.buscarSY(txtSYF0005.getValue()).isEmpty()) {
			msj.mensajeAlerta(Mensaje.claveSYNoEsta);
			txtSYF0005.setFocus(true);
			return true;
		} else {
			if (txtRTF0005.getText().compareTo("") != 0) {
				if (servicioF0004.buscar(txtSYF0005.getValue(),
						txtRTF0005.getValue()) == null) {
					msj.mensajeAlerta(Mensaje.claveRTNoEsta);
					lblDescripcionF0004.setValue("");
					txtRTF0005.setFocus(true);
					return true;
				} else
					lblDescripcionF0004.setValue(servicioF0004.buscar(
							txtSYF0005.getValue(), txtRTF0005.getValue())
							.getDtdl01());
			}
		}
		return false;
	}


	@Listen("onChange = #txtRTF0005; onOK = #txtRTF0005")
	public boolean claveRTExiste() {
		if (txtSYF0005.getText().compareTo("") != 0) {
			if (servicioF0004.buscar(txtSYF0005.getValue(),
					txtRTF0005.getValue()) == null) {
				msj.mensajeAlerta(Mensaje.claveRTNoEsta);
				txtRTF0005.setFocus(true);
				lblDescripcionF0004.setValue("");
				return true;
			} else
				lblDescripcionF0004.setValue(servicioF0004.buscar(
						txtSYF0005.getValue(), txtRTF0005.getValue())
						.getDtdl01());
		}
		return false;
	}

	@Listen("onChange = #txtKYF0005")
	public boolean claveKYExiste() {
		if (txtSYF0005.getText().compareTo("") == 0) {
			if (txtSYF0005.getText().compareTo("") == 0) {
				if (servicioF0005.buscar(txtSYF0005.getValue(),
						txtRTF0005.getValue(), txtKYF0005.getValue()) != null) {
					msj.mensajeAlerta(Mensaje.claveUsada);
					txtKYF0005.setFocus(true);
					return true;
				}
			}
		}
		return false;
	}

	public boolean camposLLenos() {
		if (txtDL01F0005.getText().compareTo("") == 0
				|| txtRTF0005.getText().compareTo("") == 0
				|| txtKYF0005.getText().compareTo("") == 0
				|| txtSYF0005.getText().compareTo("") == 0) {
			return false;
		} else
			return true;
	}

	public boolean camposEditando() {
		if (txtDL01F0005.getText().compareTo("") != 0
				|| txtSPHDF0005.getText().compareTo("") != 0
				|| txtHRDCF0005.getText().compareTo("") != 0
				|| txtRTF0005.getText().compareTo("") != 0
				|| txtSYF0005.getText().compareTo("") != 0
				|| txtKYF0005.getText().compareTo("") != 0
				|| txtDL02F0005.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistro")
	public void abrirRegistro() {
		if (clave == null)
			btnBuscarF0004.setVisible(true);
		else
			btnBuscarF0004.setVisible(false);
		gpxDatos.setOpen(false);
		gpxRegistro.setOpen(true);
		mostrarBotones(false);
	}

	@Listen("onOpen = #gpxDatos")
	public void abrirCatalogo() {
		gpxDatos.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatos.setOpen(false);
								gpxRegistro.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatos.setOpen(true);
									gpxRegistro.setOpen(false);
									limpiarCampos();
									habilitarTextClave();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatos.setOpen(true);
			gpxRegistro.setOpen(false);
			mostrarBotones(true);
		}
	}

	public void mostrarCatalogo() {
		final List<F0005> listF0005 = servicioF0005.buscarTodosOrdenados();
		catalogo = new Catalogo<F0005>(catalogoF0005, "F0005", listF0005,
				false, false, false, "SY", "RT", "KY", "Descripcion 01",
				"Descripcion 02", "Gestion Especial", "Codificacion Fija") {

			@Override
			protected List<F0005> buscar(List<String> valores) {

				List<F0005> listF0005_2 = new ArrayList<F0005>();

				for (F0005 f0005 : listF0005) {
					if (f0005.getId().getDrsy().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& f0005.getId().getDrrt().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& f0005.getId().getDrky().toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& f0005.getDrdl01().toLowerCase()
									.contains(valores.get(3).toLowerCase())
							&& f0005.getDrdl02().toLowerCase()
									.contains(valores.get(4).toLowerCase())
							&& f0005.getDrsphd().toLowerCase()
									.contains(valores.get(5).toLowerCase())
							&& f0005.getDrhrdc().toLowerCase()
									.contains(valores.get(6).toLowerCase())) {
						listF0005_2.add(f0005);
					}
				}
				return listF0005_2;
			}

			@Override
			protected String[] crearRegistros(F0005 f0005) {
				String[] registros = new String[7];
				registros[0] = f0005.getId().getDrsy();
				registros[1] = f0005.getId().getDrrt();
				registros[2] = f0005.getId().getDrky();
				registros[3] = f0005.getDrdl01();
				registros[4] = f0005.getDrdl02();
				registros[5] = f0005.getDrsphd();
				registros[6] = f0005.getDrhrdc();
				return registros;
			}
		};
		catalogo.setParent(catalogoF0005);
	}

	@Listen("onClick = #btnBuscarF0004, #btnBuscarF0004Filtro")
	public void mostrarCatalogoF0004(Event evento) {
		
		Button boton = (Button) evento.getTarget();
		idBoton = boton.getId();
		
		final List<F0004> listF0004 = servicioF0004.buscarTodosOrdenados();
		catalogoF0004 = new Catalogo<F0004>(divCatalogoF0004, "Catalogo de Codigos Definidos por el Usuario",
				listF0004, true, false, false, "SY", "RT", "Descripcion",
				"Codigo", "2 Linea", "Numerico") {

			@Override
			protected List<F0004> buscar(List<String> valores) {

				List<F0004> lista = new ArrayList<F0004>();

				for (F0004 f0004 : listF0004) {
					if (f0004.getId().getDtsy().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& f0004.getId().getDtrt().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& f0004.getDtdl01().toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& String.valueOf(f0004.getDtcdl()).toLowerCase()
									.contains(valores.get(3).toLowerCase())
							&& f0004.getDtln2().toLowerCase()
									.contains(valores.get(4).toLowerCase())
							&& f0004.getDtcnum().toLowerCase()
									.contains(valores.get(5).toLowerCase())) {
						lista.add(f0004);
					}
				}
				return lista;
			}

			@Override
			protected String[] crearRegistros(F0004 f0004) {
				String[] registros = new String[6];
				registros[0] = f0004.getId().getDtsy();
				registros[1] = f0004.getId().getDtrt();
				registros[2] = f0004.getDtdl01();
				registros[3] = String.valueOf(f0004.getDtcdl());
				registros[4] = f0004.getDtln2();
				registros[5] = f0004.getDtcnum();
				return registros;
			}
		};
		catalogoF0004.setClosable(true);
		catalogoF0004.setWidth("80%");
		catalogoF0004.setParent(divCatalogoF0004);
		catalogoF0004.doModal();
	}

	@Listen("onSeleccion = #divCatalogoF0004")
	public void seleccion() {
		F0004 f0004 = catalogoF0004.objetoSeleccionadoDelCatalogo();

		switch (idBoton) {
		case "btnBuscarF0004":
			txtSYF0005.setValue(f0004.getId().getDtsy());
			txtRTF0005.setValue(f0004.getId().getDtrt());
			lblDescripcionF0004.setValue(servicioF0004.buscar(
					f0004.getId().getDtsy(), f0004.getId().getDtrt()).getDtdl01());
			break;
		case "btnBuscarF0004Filtro":
			lblSY.setValue(f0004.getId().getDtsy());
			lblRT.setValue(f0004.getId().getDtrt());
			lblF0004.setValue(f0004.getDtdl01());
		catalogo.actualizarLista(servicioF0005.buscarParaUDCOrdenados(f0004.getId().getDtsy(), f0004.getId().getDtrt()), true);
			break;
		default:
			break;
		}
		catalogoF0004.setParent(null);
	}
}