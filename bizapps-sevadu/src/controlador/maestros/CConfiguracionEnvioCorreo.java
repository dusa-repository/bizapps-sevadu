package controlador.maestros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import modelo.maestros.ConfiguracionEnvioCorreo;
import modelo.maestros.F0005;
import modelo.maestros.MaestroAliado;
import modelo.seguridad.Usuario;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import componente.Botonera;
import componente.BuscadorUDC;
import componente.Catalogo;
import componente.Mensaje;
import componente.Validador;

public class CConfiguracionEnvioCorreo extends CGenerico {

	private static final long serialVersionUID = 4282559861064134361L;
	@Wire
	private Div divBuscadorReporte;
	BuscadorUDC buscadorReporte;
	@Wire
	private Radio rdoSi;
	@Wire
	private Radio rdoNo;
	@Wire
	private Textbox txtDestinatarios;
	@Wire
	private Div divVEnvio;
	@Wire
	private Div botoneraEnvio;
	@Wire
	private Div catalogoEnvio;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;
	@Wire
	private Listbox ltbCorreos;
	Botonera botonera;
	Catalogo<ConfiguracionEnvioCorreo> catalogo;
	long clave = 0;

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
		ltbCorreos.setModel(new ListModelList<Usuario>(servicioUsuario
				.buscarTodos()));
		mostrarCatalogo();
		cargarBuscadores();
		txtDestinatarios.setFocus(true);
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						ConfiguracionEnvioCorreo configuracion = catalogo
								.objetoSeleccionadoDelCatalogo();
						clave = configuracion.getIdConfiguracion();
						txtDestinatarios.setValue(configuracion
								.getDestinatarios());
						buscadorReporte.settearCampo(servicioF0005.buscar("00",
								"07", configuracion.getReporte()));
						if (configuracion.isEstado())
							rdoSi.setChecked(true);
						else
							rdoNo.setChecked(true);
						txtDestinatarios.setFocus(true);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}
			}

			@Override
			public void salir() {
				cerrarVentana(divVEnvio, cerrar, tabs);
			}

			@Override
			public void reporte() {
				// TODO Auto-generated method stub

			}

			@Override
			public void limpiar() {
				mostrarBotones(false);
				limpiarCampos();
				clave = 0;
			}

			@Override
			public void guardar() {
				if (validar()) {
					boolean estado = false;
					if (rdoSi.isChecked())
						estado = true;
					ConfiguracionEnvioCorreo configuracion = new ConfiguracionEnvioCorreo(
							clave, buscadorReporte.obtenerCaja(), estado,
							txtDestinatarios.getValue(), fecha, tiempo,
							nombreUsuarioSesion());
					servicioEnvio.guardar(configuracion);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					catalogo.actualizarLista(
							servicioEnvio.buscarTodosOrdenados(), true);
				}
			}

			@Override
			public void eliminar() {
				if (gpxDatos.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<ConfiguracionEnvioCorreo> eliminarLista = catalogo
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
													servicioEnvio
															.eliminarVarios(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(
															servicioEnvio
																	.buscarTodosOrdenados(),
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
													servicioEnvio
															.eliminarUno(clave);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(
															servicioEnvio
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
		botoneraEnvio.appendChild(botonera);
	}

	protected boolean validar() {
		if (clave == 0 && !camposLLenos()) {
			msj.mensajeError(Mensaje.camposVacios);
			return false;
		} else {
			if (!validarCorreos()) {
				msj.mensajeError(Mensaje.correoInvalido);
				return false;
			} else
				return true;
		}
	}

	private boolean validarCorreos() {
		String destinos[] = txtDestinatarios.getValue().split(";");
		int j = 0;
		while (j < destinos.length) {
			if (!Validador.validarCorreo(destinos[j]))
				return false;
			j++;
		}
		return true;
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

	private boolean camposEditando() {
		if (txtDestinatarios.getText().compareTo("") != 0
				|| buscadorReporte.obtenerCaja().compareTo("") != 0
				|| (rdoNo.isChecked() || rdoSi.isChecked())) {
			return true;
		} else
			return false;
	}

	private boolean camposLLenos() {
		if (txtDestinatarios.getText().compareTo("") == 0
				|| buscadorReporte.obtenerCaja().compareTo("") == 0
				|| (!rdoNo.isChecked() && !rdoSi.isChecked())) {
			return false;
		} else
			return true;
	}

	protected void limpiarCampos() {
		txtDestinatarios.setValue("");
		buscadorReporte.settearCampo(null);
		rdoNo.setChecked(false);
		rdoSi.setChecked(false);
		clave = 0;
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
		List<ConfiguracionEnvioCorreo> seleccionados = catalogo
				.obtenerSeleccionados();
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

	private void cargarBuscadores() {
		List<F0005> listF0005 = servicioF0005
				.buscarParaUDCOrdenados("00", "07");
		buscadorReporte = new BuscadorUDC("Reporte", 50, listF0005, true,
				false,false, "00", "04", "29%", "18.5%", "6.5%", "28%") 
//				false, false, "00", "07", "32%", "8%", "10%", "50%") 
		{
			@Override
			protected F0005 buscar() {
				return servicioF0005.buscar("00", "07",
						buscadorReporte.obtenerCaja());
			}
		};
		divBuscadorReporte.appendChild(buscadorReporte);
	}

	private void mostrarCatalogo() {
		final List<ConfiguracionEnvioCorreo> listaObjetos = servicioEnvio
				.buscarTodosOrdenados();
		catalogo = new Catalogo<ConfiguracionEnvioCorreo>(catalogoEnvio,
				"Aliado", listaObjetos, false, false, false, "Reporte",
				"Destinatarios", "Estado") {

			@Override
			protected List<ConfiguracionEnvioCorreo> buscar(List<String> valores) {

				List<ConfiguracionEnvioCorreo> lista = new ArrayList<ConfiguracionEnvioCorreo>();

				for (ConfiguracionEnvioCorreo objeto : listaObjetos) {
					String estado = "Inactivo";
					if (objeto.isEstado())
						estado = "Activo";
					if (objeto.getReporte().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& objeto.getDestinatarios().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& estado.toLowerCase().contains(
									valores.get(2).toLowerCase())) {
						lista.add(objeto);
					}
				}
				return lista;
			}

			@Override
			protected String[] crearRegistros(ConfiguracionEnvioCorreo objeto) {
				String estado = "Inactivo";
				if (objeto.isEstado())
					estado = "Activo";
				String[] registros = new String[3];
				registros[0] = objeto.getReporte();
				registros[1] = objeto.getDestinatarios();
				registros[2] = estado;
				return registros;
			}
		};
		catalogo.setParent(catalogoEnvio);
	}

	@Listen("onDoubleClick = #ltbCorreos")
	public void pasar() {
		String anteriores = txtDestinatarios.getValue();
		Usuario usuario = ltbCorreos.getSelectedItem().getValue();
		anteriores += usuario.getEmail() + ";";
		txtDestinatarios.setValue(anteriores);
	}
}
