package controlador.maestros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.MaestroMarca;
import modelo.maestros.MaestroProducto;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import componente.Botonera;
import componente.Catalogo;
import componente.Mensaje;

public class CMarca extends CGenerico {

	private static final long serialVersionUID = -5441143657639120753L;
	@Wire
	private Textbox txtCodigo;
	@Wire
	private Textbox txtDescripcion;
	@Wire
	private Radio rdoSi;
	@Wire
	private Radio rdoNo;
	@Wire
	private Radio rdoSiActivacion;
	@Wire
	private Radio rdoNoActivacion;
	@Wire
	private Spinner spnOrden;
	@Wire
	private Div divVMarca;
	@Wire
	private Div botoneraMarca;
	@Wire
	private Div catalogoMarca;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;
	Botonera botonera;
	Catalogo<MaestroMarca> catalogo;
	Integer clave = 0;
	private List<MaestroMarca> listaGeneral = new ArrayList<MaestroMarca>();

	@SuppressWarnings("unchecked")
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
		mostrarCatalogo();
		txtCodigo.setFocus(true);
		botonera = new Botonera() {
			private static final long serialVersionUID = 2405291709627695301L;

			@Override
			public void seleccionar() {
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						MaestroMarca marca = catalogo
								.objetoSeleccionadoDelCatalogo();
						clave = marca.getId();
						txtCodigo.setValue(marca.getMarcaDusa());
						txtDescripcion.setValue(marca.getDescripcion());
						if (marca.isFiltroTermometro())
							rdoSi.setChecked(true);
						else
							rdoNo.setChecked(true);
						if (marca.getOrden() != null)
							spnOrden.setValue(marca.getOrden());
						if (marca.getActivacion() != null) {
							if (marca.getActivacion())
								rdoSiActivacion.setChecked(true);
							else
								rdoNoActivacion.setChecked(true);
						} else {
							rdoNoActivacion.setChecked(false);
							rdoSiActivacion.setChecked(false);
						}

						txtCodigo.setDisabled(true);
						txtDescripcion.setFocus(true);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}
			}

			@Override
			public void salir() {
				cerrarVentana(divVMarca, cerrar, tabs);
			}

			@Override
			public void reporte() {
				// TODO Auto-generated method stub

			}

			@Override
			public void limpiar() {
				mostrarBotones(false);
				limpiarCampos();
				habilitarTextClave();
				clave = 0;
			}

			@Override
			public void guardar() {
				if (validar()) {
					boolean filtro = false;
					if (rdoSi.isChecked())
						filtro = true;
					boolean activacion = false;
					if (rdoSiActivacion.isChecked())
						activacion = true;
					MaestroMarca marca = new MaestroMarca(clave,
							txtCodigo.getValue(), txtDescripcion.getValue(),
							fecha, 0, filtro, tiempo, nombreUsuarioSesion(),
							"", spnOrden.getValue(), activacion);
					servicioMarca.guardar(marca);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					listaGeneral = servicioMarca.buscarTodosOrdenados();
					catalogo.actualizarLista(listaGeneral, true);
				}
			}

			@Override
			public void eliminar() {

				if (gpxDatos.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<MaestroMarca> eliminarLista = catalogo
								.obtenerSeleccionados();
						List<MaestroProducto> productos = servicioProducto
								.buscarPorMarcas(eliminarLista);
						if (productos.isEmpty()) {
							Messagebox
									.show("¿Desea Eliminar los "
											+ eliminarLista.size()
											+ " Registros?",
											"Alerta",
											Messagebox.OK | Messagebox.CANCEL,
											Messagebox.QUESTION,
											new org.zkoss.zk.ui.event.EventListener<Event>() {
												public void onEvent(Event evt)
														throws InterruptedException {
													if (evt.getName().equals(
															"onOK")) {
														servicioMarca
																.eliminarVarios(eliminarLista);
														msj.mensajeInformacion(Mensaje.eliminado);
														listaGeneral = servicioMarca
																.buscarTodosOrdenados();
														catalogo.actualizarLista(
																listaGeneral,
																true);
													}
												}
											});

						} else
							msj.mensajeError(Mensaje.noEliminar);
					}
				} else {
					/* Elimina un solo registro */
					if (clave != 0) {
						List<MaestroProducto> productos = servicioProducto
								.buscarPorMarca(txtCodigo.getValue());
						if (productos.isEmpty()) {
							Messagebox
									.show(Mensaje.deseaEliminar,
											"Alerta",
											Messagebox.OK | Messagebox.CANCEL,
											Messagebox.QUESTION,
											new org.zkoss.zk.ui.event.EventListener<Event>() {
												public void onEvent(Event evt)
														throws InterruptedException {
													if (evt.getName().equals(
															"onOK")) {
														servicioMarca
																.eliminarUno(clave);
														msj.mensajeInformacion(Mensaje.eliminado);
														limpiar();
														listaGeneral = servicioMarca
																.buscarTodosOrdenados();
														catalogo.actualizarLista(
																listaGeneral,
																true);
													}
												}
											});
						} else
							msj.mensajeError(Mensaje.noEliminar);
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
				// TODO Auto-generated method stub

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
		botoneraMarca.appendChild(botonera);
	}

	protected boolean validar() {
		if (clave == null && claveExiste()) {
			return false;
		} else {
			if (!camposLLenos()) {
				msj.mensajeError(Mensaje.camposVacios);
				return false;
			} else
				return true;
		}
	}

	private boolean camposLLenos() {
		if (txtCodigo.getText().compareTo("") == 0
				|| txtDescripcion.getText().compareTo("") == 0
				|| (!rdoNo.isChecked() && !rdoSi.isChecked())
				|| (!rdoNoActivacion.isChecked() && !rdoSiActivacion
						.isChecked())) {
			return false;
		} else
			return true;
	}

	@Listen("onChange = #txtCodigo")
	public boolean claveExiste() {
		if (servicioMarca.buscar(txtCodigo.getValue()) != null) {
			msj.mensajeAlerta(Mensaje.claveUsada);
			txtCodigo.setFocus(true);
			return true;
		} else
			return false;
	}

	protected void habilitarTextClave() {
		if (txtCodigo.isDisabled())
			txtCodigo.setDisabled(false);
	}

	protected void limpiarCampos() {
		txtCodigo.setValue("");
		txtDescripcion.setValue("");
		rdoNo.setChecked(false);
		rdoSi.setChecked(false);
		clave = 0;
		spnOrden.setValue(0);
		rdoNoActivacion.setChecked(false);
		rdoSiActivacion.setChecked(false);
	}

	@Listen("onClick = #gpxRegistro")
	public void abrirRegistro() {
		gpxDatos.setOpen(false);
		gpxRegistro.setOpen(true);
		mostrarBotones(false);
	}

	protected void mostrarBotones(boolean b) {
		botonera.getChildren().get(1).setVisible(!b);
		botonera.getChildren().get(2).setVisible(b);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botonera.getChildren().get(0).setVisible(b);
		botonera.getChildren().get(3).setVisible(!b);
		botonera.getChildren().get(5).setVisible(!b);
	}

	protected boolean validarSeleccion() {
		List<MaestroMarca> seleccionados = catalogo.obtenerSeleccionados();
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

	private boolean camposEditando() {
		if (txtCodigo.getText().compareTo("") != 0
				|| txtDescripcion.getText().compareTo("") != 0
				|| spnOrden.getValue() != 0
				|| (rdoSi.isChecked() || rdoNo.isChecked())
				|| (rdoSiActivacion.isChecked() || rdoNoActivacion.isChecked())) {
			return true;
		} else
			return false;
	}

	private void mostrarCatalogo() {
		listaGeneral = servicioMarca.buscarTodosOrdenados();
		catalogo = new Catalogo<MaestroMarca>(catalogoMarca, "Marca",
				listaGeneral, false, false, false, "Codigo", "Descripcion",
				"Termometro", "Activacion") {
			private static final long serialVersionUID = -4075514893997896082L;

			@Override
			protected List<MaestroMarca> buscar(List<String> valores) {

				List<MaestroMarca> lista = new ArrayList<MaestroMarca>();

				for (MaestroMarca objeto : listaGeneral) {
					String termo = "No";
					if (objeto.isFiltroTermometro())
						termo = "Si";
					String activacion = "N/A";
					if (objeto.getActivacion() != null) {
						if (objeto.getActivacion())
							activacion = "Si";
						else
							activacion = "No";
					}
					if (objeto.getMarcaDusa().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& objeto.getDescripcion().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& termo.toLowerCase().contains(
									valores.get(2).toLowerCase())
							&& activacion.toLowerCase().contains(
									valores.get(3).toLowerCase())) {
						lista.add(objeto);
					}
				}
				return lista;
			}

			@Override
			protected String[] crearRegistros(MaestroMarca objeto) {
				String termo = "No";
				if (objeto.isFiltroTermometro())
					termo = "Si";
				String activacion = "N/A";
				if (objeto.getActivacion() != null) {
					if (objeto.getActivacion())
						activacion = "Si";
					else
						activacion = "No";
				}
				String[] registros = new String[4];
				registros[0] = objeto.getMarcaDusa();
				registros[1] = objeto.getDescripcion();
				registros[2] = termo;
				registros[3] = activacion;
				return registros;
			}
		};
		catalogo.setParent(catalogoMarca);
	}

}
