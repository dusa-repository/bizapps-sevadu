package controlador.seguridad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.seguridad.Arbol;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import componente.Botonera;
import componente.Catalogo;
import componente.Mensaje;

import controlador.maestros.CGenerico;

public class CMenuArbol extends CGenerico {

	private static final long serialVersionUID = -6868106910332150746L;
	@Wire
	private Textbox txtNombre;
	@Wire
	private Longbox txtPadre;
	@Wire
	private Textbox txtUrl;
	@Wire
	private Div divVMenuArbol;
	@Wire
	private Div botoneraMenuArbol;
	@Wire
	private Div divCatalogoMenuArbol;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;

	Botonera botonera;
	Catalogo<Arbol> catalogo;
	long clave = 0;

	@Override
	public void inicializar() throws IOException {
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (map != null) {
			if (map.get("tabsGenerales") != null) {
				tabs = (List<Tab>) map.get("tabsGenerales");
				cerrar = (String) map.get("titulo");
				System.out.println(tabs.size());
				map.clear();
				map = null;
			}
		}
		txtNombre.setFocus(true);
		mostrarCatalogo();

		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Arbol arbol = catalogo.objetoSeleccionadoDelCatalogo();
						clave = arbol.getIdArbol();
						txtUrl.setValue(arbol.getUrl());
						txtNombre.setValue(arbol.getNombre());
						txtPadre.setValue(arbol.getPadre());
						txtNombre.setFocus(true);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}
			}

			@Override
			public void salir() {
				cerrarVentana(divVMenuArbol, cerrar, tabs);

			}

			@Override
			public void reporte() {
			}

			@Override
			public void limpiar() {
				mostrarBotones(false);
				limpiarCampos();
				clave = 0;
			}

			@Override
			public void guardar() {
				boolean guardar = true;
				if (clave == 0)
					guardar = validar();
				if (guardar) {
					String url = txtUrl.getValue();
					String nombre = txtNombre.getValue();
					Long padre = txtPadre.getValue();
					Arbol arbol = new Arbol();
					arbol.setNombre(nombre);
					arbol.setPadre(padre);
					arbol.setUrl(url);
					arbol.setIdArbol(clave);
					servicioArbol.guardar(arbol);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					catalogo.actualizarLista(servicioArbol.listarArbol(), true);
					abrirCatalogo();
				}

			}

			@Override
			public void eliminar() {
				if (gpxDatos.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Arbol> eliminarLista = catalogo
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
													servicioArbol
															.eliminarVarios(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(
															servicioArbol
																	.listarArbol(),
															true);
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (clave != 0) {
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
													servicioArbol
															.eliminarUno(clave);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(
															servicioArbol
																	.listarArbol(),
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
		botoneraMenuArbol.appendChild(botonera);

	}

	public void mostrarBotones(boolean bol) {
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(2).setVisible(bol);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botonera.getChildren().get(0).setVisible(bol);
		botonera.getChildren().get(3).setVisible(!bol);
		botonera.getChildren().get(5).setVisible(!bol);
	}

	public void limpiarCampos() {
		clave = 0;
		txtUrl.setValue("");
		txtNombre.setValue("");
		txtPadre.setValue(null);
	}

	public boolean validarSeleccion() {
		List<Arbol> seleccionados = catalogo.obtenerSeleccionados();
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
		if (!camposLLenos()) {
			msj.mensajeError(Mensaje.camposVacios);
			return false;
		} else
			return true;
	}

	public boolean camposLLenos() {
		if (txtUrl.getText().compareTo("") == 0
				|| txtNombre.getText().compareTo("") == 0
				|| txtPadre.getText().compareTo("") == 0) {
			return false;
		} else
			return true;
	}

	public boolean camposEditando() {
		if (txtUrl.getText().compareTo("") != 0
				|| txtNombre.getText().compareTo("") != 0
				|| txtPadre.getText().compareTo("") != 0) {
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
		final List<Arbol> listArbol = servicioArbol.listarArbol();
		catalogo = new Catalogo<Arbol>(divCatalogoMenuArbol, "Arbol",
				listArbol, false, false, false, "Codigo", "Nombre", "Padre",
				"Url") {

			@Override
			protected List<Arbol> buscar(List<String> valores) {

				List<Arbol> lista = new ArrayList<Arbol>();

				for (Arbol arbol : listArbol) {
					if (String.valueOf(arbol.getIdArbol()).toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& arbol.getNombre().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& String.valueOf(arbol.getPadre()).toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& arbol.getUrl().toLowerCase()
									.contains(valores.get(3).toLowerCase())) {
						lista.add(arbol);
					}
				}
				return lista;
			}

			@Override
			protected String[] crearRegistros(Arbol arbol) {
				String[] registros = new String[6];
				registros[0] = String.valueOf(arbol.getIdArbol());
				registros[1] = arbol.getNombre();
				registros[2] = String.valueOf(arbol.getPadre());
				registros[3] = arbol.getUrl();
				return registros;
			}
		};
		catalogo.setParent(divCatalogoMenuArbol);
	}

}
