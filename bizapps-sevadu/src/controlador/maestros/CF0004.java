package controlador.maestros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.F0004;
import modelo.pk.F0004PK;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Doublespinner;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import componente.Botonera;
import componente.Catalogo;
import componente.Mensaje;

public class CF0004 extends CGenerico {

	private static final long serialVersionUID = -8680709288839148461L;
	@Wire
	private Textbox txtSYF0004;
	@Wire
	private Textbox txtRTF0004;
	@Wire
	private Textbox txtLNF0004;
	@Wire
	private Textbox txtNUMF0004;
	@Wire
	private Textbox txtDL01F0004;
	@Wire
	private Doublebox txtCDLF0004;
	@Wire
	private Div divVF0004;
	@Wire
	private Div botoneraF0004;
	@Wire
	private Div catalogoF0004;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;

	Botonera botonera;
	Catalogo<F0004> catalogo;
	F0004PK clave = null;

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
		txtSYF0004.setFocus(true);
		mostrarCatalogo();

		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						F0004 f04 = catalogo.objetoSeleccionadoDelCatalogo();
						clave = f04.getId();
						txtRTF0004.setValue(f04.getId().getDtrt());
						txtRTF0004.setDisabled(true);
						txtSYF0004.setValue(f04.getId().getDtsy());
						txtSYF0004.setDisabled(true);
						txtDL01F0004.setValue(f04.getDtdl01());
						txtLNF0004.setValue(f04.getDtln2());
						txtNUMF0004.setValue(f04.getDtcnum());
						if (f04.getDtcdl() != null)
							txtCDLF0004.setValue(f04.getDtcdl());
						txtDL01F0004.setFocus(true);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}
			}

			@Override
			public void salir() {
				cerrarVentana(divVF0004, cerrar, tabs);

			}

			@Override
			public void reporte() {
			}

			@Override
			public void limpiar() {
				mostrarBotones(false);
				limpiarCampos();
				habilitarTextClave();
				clave = null;
			}

			@Override
			public void guardar() {
				if (validar()) {

					F0004 fooo4 = new F0004();
					String rt = txtSYF0004.getValue();
					String sy = txtRTF0004.getValue();
					String dl = txtDL01F0004.getValue();
					String ln = txtLNF0004.getValue();
					Double a;
					if (txtCDLF0004.getValue() != null) {
						a = txtCDLF0004.getValue();
						fooo4.setDtcdl(a);
					}
					String num = txtNUMF0004.getValue();
					F0004PK clave = new F0004PK();
					clave.setDtsy(rt);
					clave.setDtrt(sy);

					fooo4.setId(clave);
					fooo4.setDtdl01(dl);
					fooo4.setDtln2(ln);
					fooo4.setDtcnum(num);

					fooo4.setDtjobn("5");
					fooo4.setDtuseq((double) 45);
					fooo4.setDtuser("jDE");
					// fooo4.setDtupmj(dtupmj); //Fecha
					fooo4.setDtupmt(Double.parseDouble(horaAuditoria));
					servicioF0004.guardar(fooo4);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					catalogo.actualizarLista(
							servicioF0004.buscarTodosOrdenados(), true);
				}

			}

			@Override
			public void eliminar() {
				if (gpxDatos.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<F0004> eliminarLista = catalogo
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
													servicioF0004
															.eliminarVarios(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(
															servicioF0004
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
													servicioF0004
															.eliminarUno(clave);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(
															servicioF0004
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
		botoneraF0004.appendChild(botonera);

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
		txtDL01F0004.setValue("");
		txtLNF0004.setValue("");
		txtNUMF0004.setValue("");
		txtRTF0004.setValue("");
		txtSYF0004.setValue("");
		txtCDLF0004.setValue(null);
		txtSYF0004.setFocus(true);
	}

	public void habilitarTextClave() {
		if (txtRTF0004.isDisabled())
			txtRTF0004.setDisabled(false);
		if (txtSYF0004.isDisabled())
			txtSYF0004.setDisabled(false);
	}

	public boolean validarSeleccion() {
		List<F0004> seleccionados = catalogo.obtenerSeleccionados();
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

	@Listen("onChange = #txtSYF0004")
	public boolean claveSYExiste() {
		if (servicioF0004.buscar(txtSYF0004.getValue(), txtRTF0004.getValue()) != null) {
			msj.mensajeAlerta(Mensaje.claveUsada);
			txtSYF0004.setFocus(true);
			return true;
		} else
			return false;
	}

	@Listen("onChange = #txtRTF0004")
	public boolean claveRTExiste() {
		if (servicioF0004.buscar(txtSYF0004.getValue(), txtRTF0004.getValue()) != null) {
			msj.mensajeAlerta(Mensaje.claveUsada);
			txtRTF0004.setFocus(true);
			return true;
		} else
			return false;
	}

	public boolean camposLLenos() {
		if (txtDL01F0004.getText().compareTo("") == 0
				|| txtRTF0004.getText().compareTo("") == 0
				|| txtSYF0004.getText().compareTo("") == 0) {
			return false;
		} else
			return true;
	}

	public boolean camposEditando() {
		if (txtDL01F0004.getText().compareTo("") != 0
				|| txtLNF0004.getText().compareTo("") != 0
				|| txtNUMF0004.getText().compareTo("") != 0
				|| txtRTF0004.getText().compareTo("") != 0
				|| txtSYF0004.getText().compareTo("") != 0
				|| txtCDLF0004.getValue() != null) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistro")
	public void abrirRegistro() {
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
		final List<F0004> listF0004 = servicioF0004.buscarTodosOrdenados();
		catalogo = new Catalogo<F0004>(catalogoF0004, "F0004", listF0004,
				false, false, false, "SY", "RT", "Descripcion", "Codigo",
				"2 Linea", "Numerico") {

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
		catalogo.setParent(catalogoF0004);
	}
}