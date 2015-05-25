package controlador.maestros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.ConfiguracionEnvioCorreo;
import modelo.maestros.F0005;
import modelo.maestros.MaestroMarca;
import modelo.seguridad.Usuario;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
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
	private Listbox ltbCorreosAgregados;
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
	private List<ConfiguracionEnvioCorreo> listaGeneral = new ArrayList<ConfiguracionEnvioCorreo>();
	List<Usuario> correosDisponibles = new ArrayList<Usuario>();
	List<Usuario> correosAgregados = new ArrayList<Usuario>();

	@Override
	public void inicializar() throws IOException {
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (map != null) {
			if (map.get("tabsGenerales") != null) {
				tabs = (List<Tab>) map.get("tabsGenerales");
				grxGraficoGeneral = (Groupbox) map.get("grxGraficoGeneral");
				cerrar = (String) map.get("titulo");
				map.clear();
				map = null;
			}
		}
		llenarListas();
		mostrarCatalogo();
		cargarBuscadores();
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
						llenarListas();
						buscadorReporte.settearCampo(servicioF0005.buscar("00",
								"07", configuracion.getReporte()));
						if (configuracion.isEstado())
							rdoSi.setChecked(true);
						else
							rdoNo.setChecked(true);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}
			}

			@Override
			public void salir() {
				cerrarVentana(divVEnvio, cerrar, tabs, grxGraficoGeneral);
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
					String correos = "";
					for (int i = 0; i < correosAgregados.size(); i++) {
						correos += correosAgregados.get(i).getEmail() + ";";
					}
					ConfiguracionEnvioCorreo configuracion = new ConfiguracionEnvioCorreo(
							clave, buscadorReporte.obtenerCaja(), estado,
							correos, fecha, tiempo, nombreUsuarioSesion());
					servicioEnvio.guardar(configuracion);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					listaGeneral = servicioEnvio.buscarTodosOrdenados();
					catalogo.actualizarLista(listaGeneral, true);
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
													listaGeneral = servicioEnvio
															.buscarTodosOrdenados();
													catalogo.actualizarLista(
															listaGeneral, true);
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
													listaGeneral = servicioEnvio
															.buscarTodosOrdenados();
													catalogo.actualizarLista(
															listaGeneral, true);
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

	private void llenarListas() {
		ConfiguracionEnvioCorreo objeto = servicioEnvio.buscar(clave);
		List<String> lista = new ArrayList<String>();
		if (objeto != null) {
			String destinos[] = objeto.getDestinatarios().split(";");
			lista = Arrays.asList(destinos);
		}
		if (!lista.isEmpty())
			correosAgregados = servicioUsuario.buscarCorreosOcupados(lista);
		else
			correosAgregados.clear();
		correosDisponibles = servicioUsuario.buscarCorreosLibres(lista);
		ltbCorreos.setModel(new ListModelList<Usuario>(correosDisponibles));
		ltbCorreosAgregados.setModel(new ListModelList<Usuario>(
				correosAgregados));
		listasMultiples();
	}

	private void listasMultiples() {
		ltbCorreos.setMultiple(false);
		ltbCorreos.setCheckmark(false);
		ltbCorreos.setMultiple(true);
		ltbCorreos.setCheckmark(true);

		ltbCorreosAgregados.setMultiple(false);
		ltbCorreosAgregados.setCheckmark(false);
		ltbCorreosAgregados.setMultiple(true);
		ltbCorreosAgregados.setCheckmark(true);
	}

	@Listen("onClick = #pasar1")
	public void derecha() {
		List<Listitem> listitemEliminar = new ArrayList<Listitem>();
		List<Listitem> listItem = ltbCorreos.getItems();
		if (listItem.size() != 0) {
			for (int i = 0; i < listItem.size(); i++) {
				if (listItem.get(i).isSelected()) {
					Usuario marca = listItem.get(i).getValue();
					correosDisponibles.remove(marca);
					correosAgregados.add(marca);
					ltbCorreosAgregados.setModel(new ListModelList<Usuario>(
							correosAgregados));
					ltbCorreosAgregados.renderAll();
					listitemEliminar.add(listItem.get(i));
					listItem.get(i).setSelected(false);
				}
			}
		}
		for (int i = 0; i < listitemEliminar.size(); i++) {
			ltbCorreos.removeItemAt(listitemEliminar.get(i).getIndex());
			ltbCorreos.renderAll();
		}
		listasMultiples();
	}

	@Listen("onClick = #pasar2")
	public void izquierda() {
		List<Listitem> listitemEliminar = new ArrayList<Listitem>();
		List<Listitem> listItem2 = ltbCorreosAgregados.getItems();
		if (listItem2.size() != 0) {
			for (int i = 0; i < listItem2.size(); i++) {
				if (listItem2.get(i).isSelected()) {
					Usuario marca = listItem2.get(i).getValue();
					correosAgregados.remove(marca);
					correosDisponibles.add(0, marca);
					ltbCorreos.setModel(new ListModelList<Usuario>(
							correosDisponibles));
					ltbCorreos.renderAll();
					listitemEliminar.add(listItem2.get(i));
					listItem2.get(i).setSelected(false);
				}
			}
		}
		for (int i = 0; i < listitemEliminar.size(); i++) {
			ltbCorreosAgregados
					.removeItemAt(listitemEliminar.get(i).getIndex());
			ltbCorreosAgregados.renderAll();
		}
		listasMultiples();
	}

	protected boolean validar() {
		if (clave == 0 && !camposLLenos()) {
			msj.mensajeError(Mensaje.camposVacios);
			return false;
		} else
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
		if (buscadorReporte.obtenerCaja().compareTo("") != 0
				|| (rdoNo.isChecked() || rdoSi.isChecked())) {
			return true;
		} else
			return false;
	}

	private boolean camposLLenos() {
		if (buscadorReporte.obtenerCaja().compareTo("") == 0
				|| (!rdoNo.isChecked() && !rdoSi.isChecked())) {
			return false;
		} else
			return true;
	}

	protected void limpiarCampos() {
		buscadorReporte.settearCampo(null);
		rdoNo.setChecked(false);
		rdoSi.setChecked(false);
		clave = 0;
		llenarListas();
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
				false, false, "00", "07", "29%", "18.5%", "6.5%", "28%") {
			@Override
			protected F0005 buscar() {
				return servicioF0005.buscar("00", "07",
						buscadorReporte.obtenerCaja());
			}
		};
		divBuscadorReporte.appendChild(buscadorReporte);
	}

	private void mostrarCatalogo() {
		listaGeneral = servicioEnvio.buscarTodosOrdenados();
		catalogo = new Catalogo<ConfiguracionEnvioCorreo>(catalogoEnvio,
				"Aliado", listaGeneral, false, false, false, "Reporte",
				"Destinatarios", "Estado") {

			@Override
			protected List<ConfiguracionEnvioCorreo> buscar(List<String> valores) {

				List<ConfiguracionEnvioCorreo> lista = new ArrayList<ConfiguracionEnvioCorreo>();

				for (ConfiguracionEnvioCorreo objeto : listaGeneral) {
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
}
