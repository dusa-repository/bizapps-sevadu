package controlador.maestros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.TipoCliente;
import modelo.seguridad.Usuario;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import componente.Botonera;
import componente.Catalogo;
import componente.Mensaje;

public class CTipoCliente extends CGenerico {

	private static final long serialVersionUID = -6868106910332150746L;
	@Wire
	private Textbox txtCodigo;
	@Wire
	private Textbox txtDescripcion;
	@Wire
	private Textbox txtCanal;
	@Wire
	private Div divTipoCliente;
	@Wire
	private Div botoneraTipoCliente;
	@Wire
	private Div divCatalogoTipoCliente;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;

	Botonera botonera;
	Catalogo<TipoCliente> catalogo;
	String id = "";

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
		txtCodigo.setFocus(true);
		mostrarCatalogo();

		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						TipoCliente tipo = catalogo
								.objetoSeleccionadoDelCatalogo();
						id = tipo.getCodigo();
						txtCanal.setValue(tipo.getCanalVentas());
						txtCodigo.setValue(tipo.getCodigo());
						txtDescripcion.setValue(tipo.getDescripcion());
						txtCodigo.setFocus(true);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}
			}

			@Override
			public void salir() {
				cerrarVentana(divTipoCliente, cerrar, tabs);

			}

			@Override
			public void reporte() {
			}

			@Override
			public void limpiar() {
				mostrarBotones(false);
				limpiarCampos();
				id = "";
			}

			@Override
			public void guardar() {
				boolean guardar = true;
				if (id.equals(""))
					guardar = validar();
				if (guardar) {
					if(buscarPorId())
					{
					String canal = txtCanal.getValue();
					String codigo = txtCodigo.getValue();
					String descripcion = txtDescripcion.getValue();
					TipoCliente tipo = new TipoCliente();
					tipo.setCodigo(codigo);
					tipo.setDescripcion(descripcion);
					tipo.setCanalVentas(canal);
					servicioTipoCliente.guardar(tipo);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					catalogo.actualizarLista(servicioTipoCliente.buscarTodos(),
							true);
					abrirCatalogo();
					}
				}

			}

			@Override
			public void eliminar() {
				if (gpxDatos.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<TipoCliente> eliminarLista = catalogo
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
													servicioTipoCliente
															.eliminarVarios(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(
															servicioTipoCliente
																	.buscarTodos(),
															true);
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (!id.equals("")) {
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

													servicioTipoCliente
															.eliminarUno(id);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(
															servicioTipoCliente
																	.buscarTodos(),
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
		botoneraTipoCliente.appendChild(botonera);

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
		id = "";
		txtCanal.setValue("");
		txtCodigo.setValue("");
		txtDescripcion.setValue("");
	}

	public boolean validarSeleccion() {
		List<TipoCliente> seleccionados = catalogo.obtenerSeleccionados();
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
		if (txtCanal.getText().compareTo("") == 0
				|| txtCodigo.getText().compareTo("") == 0
				|| txtDescripcion.getText().compareTo("") == 0) {
			return false;
		} else
			return true;
	}

	public boolean camposEditando() {
		if (txtCanal.getText().compareTo("") != 0
				|| txtCodigo.getText().compareTo("") != 0
				|| txtDescripcion.getText().compareTo("") != 0) {
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
		final List<TipoCliente> listCientes = servicioTipoCliente.buscarTodos();
		catalogo = new Catalogo<TipoCliente>(divCatalogoTipoCliente,
				"TipoCliente", listCientes, false, false, false, "Codigo",
				"Descripcion", "Canal") {

			@Override
			protected List<TipoCliente> buscar(List<String> valores) {

				List<TipoCliente> lista = new ArrayList<TipoCliente>();

				for (TipoCliente tipo : listCientes) {
					if (tipo.getCodigo().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& tipo.getDescripcion().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& tipo.getCanalVentas().toLowerCase()
									.contains(valores.get(2).toLowerCase())) {
						lista.add(tipo);
					}
				}
				return lista;
			}

			@Override
			protected String[] crearRegistros(TipoCliente tipo) {
				String[] registros = new String[3];
				registros[0] = tipo.getCodigo();
				registros[1] = tipo.getDescripcion();
				registros[2] = tipo.getCanalVentas();
				return registros;
			}
		};
		catalogo.setParent(divCatalogoTipoCliente);
	}
	
	@Listen("onChange = #txtCodigo")
	public boolean buscarPorId() {
		TipoCliente tipo = servicioTipoCliente.buscarPorCodigo(txtCodigo
				.getValue());
		if (tipo == null)
			return true;
		else {
			if (tipo.getCodigo()==id)
				return true;
			else {
				msj.mensajeAlerta(Mensaje.CodigoUsado);
				txtCodigo.setValue("");
				txtCodigo.setFocus(true);
				return false;
			}
		}
	}
}