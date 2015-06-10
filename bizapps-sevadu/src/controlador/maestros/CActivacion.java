package controlador.maestros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Cliente;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroMarca;
import modelo.maestros.MarcaActivadaVendedor;
import modelo.pk.MarcaActivadaPK;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import componente.Botonera;
import componente.Catalogo;
import componente.Mensaje;

public class CActivacion extends CGenerico {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1427042874437945734L;
	@Wire
	private Textbox txtAliado;
	@Wire
	private Textbox txtCliente;
	@Wire
	private Textbox txtCampo1;
	@Wire
	private Textbox txtCampo2;
	@Wire
	private Label lblAliado;
	@Wire
	private Label lblCliente;
	@Wire
	private Div divVActivacion;
	@Wire
	private Div botoneraActivacion;
	@Wire
	private Div catalogoActivacion;
	@Wire
	private Div divCatalogoAliado;
	@Wire
	private Div divCatalogoCliente;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;
	@Wire
	private Listbox ltbMarcas;
	@Wire
	private Button btnBuscarCliente;
	@Wire
	private Button btnBuscarAliado;
	Botonera botonera;
	Catalogo<MaestroAliado> catalogoAliado;
	Catalogo<Cliente> catalogoCliente;
	Catalogo<MarcaActivadaVendedor> catalogo;
	MaestroAliado aliadoGeneral = null;
	MarcaActivadaPK clave = null;
	List<MarcaActivadaVendedor> listaGeneral = new ArrayList<MarcaActivadaVendedor>();

	@Override
	public void inicializar() throws IOException {
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (map != null) {
			if (map.get("tabsGenerales") != null) {
				tabs = (List<Tab>) map.get("tabsGenerales");
				cerrar = (String) map.get("titulo");
				grxGraficoGeneral = (Groupbox) map.get("grxGraficoGeneral");
				map.clear();
				map = null;
			}
		}
		mostrarCatalogo();
		llenarLista();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						MarcaActivadaVendedor marca = catalogo
								.objetoSeleccionadoDelCatalogo();
						clave = marca.getId();
						txtAliado.setValue(clave.getMaestroAliado()
								.getCodigoAliado());
						lblAliado
								.setValue(clave.getMaestroAliado().getNombre());
						txtCliente.setValue(clave.getCliente()
								.getCodigoCliente());
						lblCliente.setValue(clave.getCliente().getNombre());
						llenarLista();
						txtAliado.setDisabled(true);
						txtCliente.setDisabled(true);
						btnBuscarAliado.setVisible(false);
						btnBuscarCliente.setVisible(false);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}
			}

			@Override
			public void salir() {
				cerrarVentana(divVActivacion, cerrar, tabs, grxGraficoGeneral);
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
					MarcaActivadaVendedor marcaActivadaVendedor = new MarcaActivadaVendedor();
					if (clave == null) {
						MaestroAliado aliado = servicioAliado.buscar(txtAliado
								.getValue());
						Cliente cliente = servicioCliente
								.buscarPorCodigo(txtCliente.getValue());
						clave = new MarcaActivadaPK();
						clave.setCliente(cliente);
						clave.setMaestroAliado(aliado);
					}
					marcaActivadaVendedor.setId(clave);
					marcaActivadaVendedor.setCampoA(txtCampo1.getValue());
					marcaActivadaVendedor.setCampoB(txtCampo2.getValue());
					int cont = 0;
					Double porcentaje = (double) 0;
					if (ltbMarcas.getItemCount() != 0) {
						for (int i = 0; i < ltbMarcas.getItemCount(); i++) {
							Listitem listItem = ltbMarcas.getItemAtIndex(i);
							if (listItem.isSelected()) {
								cont++;
								guardarMarcasActivas(marcaActivadaVendedor, i,
										true);
							} else {
								guardarMarcasActivas(marcaActivadaVendedor, i,
										false);
							}
						}
						porcentaje = (double) (cont * 100 / ltbMarcas
								.getItemCount());
					}
					marcaActivadaVendedor.setTotal(cont);
					marcaActivadaVendedor
							.setPorcentaje(porcentaje.floatValue());
					servicioMarcaActivada.guardar(marcaActivadaVendedor);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					listaGeneral = servicioMarcaActivada.buscarTodosOrdenados();
					catalogo.actualizarLista(listaGeneral, true);
				}
			}

			@Override
			public void eliminar() {
				if (gpxDatos.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<MarcaActivadaVendedor> eliminarLista = catalogo
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
													servicioMarcaActivada
															.eliminarVarios(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													listaGeneral = servicioMarcaActivada
															.buscarTodosOrdenados();
													catalogo.actualizarLista(
															listaGeneral, true);
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
													servicioMarcaActivada
															.eliminarUno(clave);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(
															servicioMarcaActivada
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
		botoneraActivacion.appendChild(botonera);
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
		if (txtCampo2.getText().compareTo("") != 0
				|| txtCampo1.getText().compareTo("") != 0
				|| txtAliado.getText().compareTo("") != 0
				|| txtCliente.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	protected boolean validar() {
		if (!camposLLenos()) {
			msj.mensajeError(Mensaje.camposVacios);
			return false;
		} else
			return true;
	}

	private boolean camposLLenos() {
		if (txtAliado.getText().compareTo("") == 0
				|| txtCliente.getText().compareTo("") == 0) {
			return false;
		} else
			return true;
	}

	protected void habilitarTextClave() {
		if (txtAliado.isDisabled()) {
			txtAliado.setDisabled(false);
			txtCliente.setDisabled(false);
			btnBuscarAliado.setVisible(true);
			btnBuscarCliente.setVisible(true);
		}

	}

	protected void limpiarCampos() {
		txtAliado.setValue("");
		txtCampo1.setValue("");
		txtCampo2.setValue("");
		txtCliente.setValue("");
		lblAliado.setValue("");
		lblCliente.setValue("");
		for (int i = 0; i < ltbMarcas.getItemCount(); i++) {
			Listitem listItem = ltbMarcas.getItemAtIndex(i);
			if (listItem.isSelected())
				listItem.setSelected(false);
		}
	}

	@Listen("onClick = #gpxRegistro")
	public void abrirRegistro() {
		gpxDatos.setOpen(false);
		gpxRegistro.setOpen(true);
		mostrarBotones(false);
	}

	protected void mostrarBotones(boolean bol) {
		botonera.getChildren().get(1).setVisible(!bol);
		botonera.getChildren().get(2).setVisible(bol);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botonera.getChildren().get(0).setVisible(bol);
		botonera.getChildren().get(3).setVisible(!bol);
		botonera.getChildren().get(5).setVisible(!bol);

	}

	protected boolean validarSeleccion() {
		List<MarcaActivadaVendedor> seleccionados = catalogo
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

	private void llenarLista() {
		List<MaestroMarca> marcas = servicioMarca.buscarActivasActivacion();
		ltbMarcas.setModel(new ListModelList<MaestroMarca>(marcas));
		ltbMarcas.renderAll();
		MarcaActivadaVendedor activada = null;
		if (clave != null)
			activada = servicioMarcaActivada.buscar(clave);

		ltbMarcas.setMultiple(false);
		ltbMarcas.setCheckmark(false);
		ltbMarcas.setMultiple(true);
		ltbMarcas.setCheckmark(true);

		if (ltbMarcas.getItemCount() != 0 && activada != null) {
			for (int j = 0; j < ltbMarcas.getItemCount(); j++) {
				Listitem listItem = ltbMarcas.getItemAtIndex(j);
				verificarMarcaActiva(listItem, activada, j);
			}
		}
	}

	private void mostrarCatalogo() {
		listaGeneral = servicioMarcaActivada.buscarTodosOrdenados();
		catalogo = new Catalogo<MarcaActivadaVendedor>(catalogoActivacion,
				"Activacion", listaGeneral, false, false, false, "Aliado",
				"Cliente", "Total Marcas Activadas") {

			@Override
			protected List<MarcaActivadaVendedor> buscar(List<String> valores) {

				List<MarcaActivadaVendedor> lista = new ArrayList<MarcaActivadaVendedor>();

				for (MarcaActivadaVendedor objeto : listaGeneral) {
					if (objeto.getId().getMaestroAliado().getNombre()
							.toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& objeto.getId().getCliente().getNombre()
									.toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& String.valueOf(objeto.getTotal()).toLowerCase()
									.contains(valores.get(2).toLowerCase())) {
						lista.add(objeto);
					}
				}
				return lista;
			}

			@Override
			protected String[] crearRegistros(MarcaActivadaVendedor objeto) {
				String[] registros = new String[3];
				registros[0] = objeto.getId().getMaestroAliado().getNombre();
				registros[1] = objeto.getId().getCliente().getNombre();
				registros[2] = String.valueOf(objeto.getTotal());
				return registros;
			}
		};
		catalogo.setParent(catalogoActivacion);
	}

	@Listen("onClick = #btnBuscarAliado")
	public void mostrarCatalogoAliado() {
		final List<MaestroAliado> listaObjetos = servicioAliado
				.buscarTodosOrdenados();
		catalogoAliado = new Catalogo<MaestroAliado>(divCatalogoAliado,
				"Catalogo de Aliados", listaObjetos, true, false, false,
				"Codigo", "Nombre", "Zona", "Vendedor") {

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
		catalogoAliado.setParent(divCatalogoAliado);
		catalogoAliado.doModal();
	}

	@Listen("onSeleccion = #divCatalogoAliado")
	public void seleccionAliado() {
		MaestroAliado aliado = catalogoAliado.objetoSeleccionadoDelCatalogo();
		aliadoGeneral = aliado;
		txtAliado.setValue(aliado.getCodigoAliado());
		lblAliado.setValue(aliado.getNombre());
		catalogoAliado.setParent(null);
	}

	@Listen("onChange = #txtAliado; onOK=#txtAliado")
	public void buscarNombreAliado() {
		MaestroAliado aliado = servicioAliado.buscar(txtAliado.getValue());
		if (aliado != null) {
			aliadoGeneral = aliado;
			txtAliado.setValue(aliado.getCodigoAliado());
			lblAliado.setValue(aliado.getNombre());
		} else {
			msj.mensajeAlerta(Mensaje.noHayRegistros);
			txtAliado.setValue("");
			txtAliado.setFocus(true);
		}
	}

	@Listen("onClick = #btnBuscarCliente")
	public void mostrarCatalogoCliente() {
		final List<Cliente> listaObjetos = servicioCliente
				.buscarPorAliado(aliadoGeneral);
		catalogoCliente = new Catalogo<Cliente>(divCatalogoCliente, "Clientes",
				listaObjetos, true, false, false, "Codigo", "Nombre", "Aliado",
				"Estado", "Vendedor", "Tipo Cliente") {

			@Override
			protected List<Cliente> buscar(List<String> valores) {

				List<Cliente> lista = new ArrayList<Cliente>();

				for (Cliente objeto : listaObjetos) {
					if (objeto.getCodigoCliente().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& objeto.getNombre().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& objeto.getMaestroAliado().getNombre()
									.toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& objeto.getEstado().toLowerCase()
									.contains(valores.get(3).toLowerCase())
							&& objeto.getVendedor().toLowerCase()
									.contains(valores.get(4).toLowerCase())
							&& objeto.getTipoCliente().getDescripcion()
									.toLowerCase()
									.contains(valores.get(5).toLowerCase())) {
						lista.add(objeto);
					}
				}
				return lista;
			}

			@Override
			protected String[] crearRegistros(Cliente objeto) {
				String[] registros = new String[6];
				registros[0] = objeto.getCodigoCliente();
				registros[1] = objeto.getNombre();
				registros[2] = objeto.getMaestroAliado().getNombre();
				registros[3] = objeto.getEstado();
				registros[4] = objeto.getVendedor();
				registros[5] = objeto.getTipoCliente().getDescripcion();
				return registros;
			}
		};
		catalogoCliente.setClosable(true);
		catalogoCliente.setWidth("80%");
		catalogoCliente.setParent(divCatalogoCliente);
		catalogoCliente.doModal();
	}

	@Listen("onSeleccion = #divCatalogoCliente")
	public void seleccionCliente() {
		Cliente cliente = catalogoCliente.objetoSeleccionadoDelCatalogo();
		txtCliente.setValue(cliente.getCodigoCliente());
		lblCliente.setValue(cliente.getNombre());
		catalogoCliente.setParent(null);
	}

	@Listen("onChange = #txtCliente; onOK=#txtCliente")
	public void buscarNombreCliente() {
		Cliente cliente = servicioCliente.buscarPorCodigoYAliado(
				txtCliente.getValue(), aliadoGeneral);
		if (cliente != null) {
			txtCliente.setValue(cliente.getCodigoCliente());
			lblCliente.setValue(cliente.getNombre());
		} else {
			msj.mensajeAlerta(Mensaje.noHayRegistros);
			txtCliente.setValue("");
			lblCliente.setValue("");
			txtCliente.setFocus(true);
		}
	}

	private void verificarMarcaActiva(Listitem listItem,
			MarcaActivadaVendedor marcaActivada, int i) {
		switch (i) {
		case 0:
			if (marcaActivada.getMarcaA() == 1)
				listItem.setSelected(true);
			break;
		case 1:
			if (marcaActivada.getMarcaB() == 1)
				listItem.setSelected(true);
			break;
		case 2:
			if (marcaActivada.getMarcaC() == 1)
				listItem.setSelected(true);
			break;
		case 3:
			if (marcaActivada.getMarcaD() == 1)
				listItem.setSelected(true);
			break;
		case 4:
			if (marcaActivada.getMarcaE() == 1)
				listItem.setSelected(true);
			break;
		case 5:
			if (marcaActivada.getMarcaF() == 1)
				listItem.setSelected(true);
			break;
		case 6:
			if (marcaActivada.getMarcaG() == 1)
				listItem.setSelected(true);
			break;
		case 7:
			if (marcaActivada.getMarcaH() == 1)
				listItem.setSelected(true);
			break;
		case 8:
			if (marcaActivada.getMarcaI() == 1)
				listItem.setSelected(true);
			break;
		case 9:
			if (marcaActivada.getMarcaJ() == 1)
				listItem.setSelected(true);
			break;
		case 10:
			if (marcaActivada.getMarcaK() == 1)
				listItem.setSelected(true);
			break;
		case 11:
			if (marcaActivada.getMarcaL() == 1)
				listItem.setSelected(true);
			break;
		case 12:
			if (marcaActivada.getMarcaM() == 1)
				listItem.setSelected(true);
			break;
		case 13:
			if (marcaActivada.getMarcaN() == 1)
				listItem.setSelected(true);
			break;
		case 14:
			if (marcaActivada.getMarcaO() == 1)
				listItem.setSelected(true);
			break;
		case 15:
			if (marcaActivada.getMarcaP() == 1)
				listItem.setSelected(true);
			break;
		case 16:
			if (marcaActivada.getMarcaQ() == 1)
				listItem.setSelected(true);
			break;
		case 17:
			if (marcaActivada.getMarcaR() == 1)
				listItem.setSelected(true);
			break;
		case 18:
			if (marcaActivada.getMarcaS() == 1)
				listItem.setSelected(true);
			break;
		case 19:
			if (marcaActivada.getMarcaT() == 1)
				listItem.setSelected(true);
			break;
		case 20:
			if (marcaActivada.getMarcaU() == 1)
				listItem.setSelected(true);
			break;
		case 21:
			if (marcaActivada.getMarcaV() == 1)
				listItem.setSelected(true);
			break;
		case 22:
			if (marcaActivada.getMarcaW() == 1)
				listItem.setSelected(true);
			break;
		case 23:
			if (marcaActivada.getMarcaX() == 1)
				listItem.setSelected(true);
			break;
		case 24:
			if (marcaActivada.getMarcaY() == 1)
				listItem.setSelected(true);
			break;
		case 25:
			if (marcaActivada.getMarcaZ() == 1)
				listItem.setSelected(true);
			break;
		case 26:
			if (marcaActivada.getMarcaZA() == 1)
				listItem.setSelected(true);
			break;
		case 27:
			if (marcaActivada.getMarcaZB() == 1)
				listItem.setSelected(true);
			break;
		case 28:
			if (marcaActivada.getMarcaZC() == 1)
				listItem.setSelected(true);
			break;
		case 29:
			if (marcaActivada.getMarcaZD() == 1)
				listItem.setSelected(true);
			break;
		case 30:
			if (marcaActivada.getMarcaZE() == 1)
				listItem.setSelected(true);
			break;
		case 31:
			if (marcaActivada.getMarcaZF() == 1)
				listItem.setSelected(true);
			break;
		case 32:
			if (marcaActivada.getMarcaZG() == 1)
				listItem.setSelected(true);
			break;
		case 33:
			if (marcaActivada.getMarcaZH() == 1)
				listItem.setSelected(true);
			break;
		case 34:
			if (marcaActivada.getMarcaZI() == 1)
				listItem.setSelected(true);
			break;
		case 35:
			if (marcaActivada.getMarcaZJ() == 1)
				listItem.setSelected(true);
			break;
		case 36:
			if (marcaActivada.getMarcaZK() == 1)
				listItem.setSelected(true);
			break;
		case 37:
			if (marcaActivada.getMarcaZL() == 1)
				listItem.setSelected(true);
			break;
		case 38:
			if (marcaActivada.getMarcaZM() == 1)
				listItem.setSelected(true);
			break;
		case 39:
			if (marcaActivada.getMarcaZN() == 1)
				listItem.setSelected(true);
			break;
		case 40:
			if (marcaActivada.getMarcaZO() == 1)
				listItem.setSelected(true);
			break;
		case 41:
			if (marcaActivada.getMarcaZP() == 1)
				listItem.setSelected(true);
			break;
		case 42:
			if (marcaActivada.getMarcaZQ() == 1)
				listItem.setSelected(true);
			break;
		case 43:
			if (marcaActivada.getMarcaZR() == 1)
				listItem.setSelected(true);
			break;
		case 44:
			if (marcaActivada.getMarcaZS() == 1)
				listItem.setSelected(true);
			break;
		case 45:
			if (marcaActivada.getMarcaZT() == 1)
				listItem.setSelected(true);
			break;
		case 46:
			if (marcaActivada.getMarcaZU() == 1)
				listItem.setSelected(true);
			break;
		case 47:
			if (marcaActivada.getMarcaZV() == 1)
				listItem.setSelected(true);
			break;
		case 48:
			if (marcaActivada.getMarcaZW() == 1)
				listItem.setSelected(true);
			break;
		case 49:
			if (marcaActivada.getMarcaZX() == 1)
				listItem.setSelected(true);
			break;
		}
	}

	private void guardarMarcasActivas(MarcaActivadaVendedor marcaActivada,
			int i, boolean seleccionado) {
		switch (i) {
		case 0:
			if (seleccionado)
				marcaActivada.setMarcaA(1);
			else
				marcaActivada.setMarcaA(0);
			break;
		case 1:
			if (seleccionado)
				marcaActivada.setMarcaB(1);
			else
				marcaActivada.setMarcaB(0);
			break;
		case 2:
			if (seleccionado)
				marcaActivada.setMarcaC(1);
			else
				marcaActivada.setMarcaC(0);
			break;
		case 3:
			if (seleccionado)
				marcaActivada.setMarcaD(1);
			else
				marcaActivada.setMarcaD(0);
			break;
		case 4:
			if (seleccionado)
				marcaActivada.setMarcaE(1);
			else
				marcaActivada.setMarcaE(0);
			break;
		case 5:
			if (seleccionado)
				marcaActivada.setMarcaF(1);
			else
				marcaActivada.setMarcaF(0);
			break;
		case 6:
			if (seleccionado)
				marcaActivada.setMarcaG(1);
			else
				marcaActivada.setMarcaG(0);
			break;
		case 7:
			if (seleccionado)
				marcaActivada.setMarcaH(1);
			else
				marcaActivada.setMarcaH(0);
			break;
		case 8:
			if (seleccionado)
				marcaActivada.setMarcaI(1);
			else
				marcaActivada.setMarcaI(0);
			break;
		case 9:
			if (seleccionado)
				marcaActivada.setMarcaJ(1);
			else
				marcaActivada.setMarcaJ(0);
			break;
		case 10:
			if (seleccionado)
				marcaActivada.setMarcaK(1);
			else
				marcaActivada.setMarcaK(0);
			break;
		case 11:
			if (seleccionado)
				marcaActivada.setMarcaL(1);
			else
				marcaActivada.setMarcaL(0);
			break;
		case 12:
			if (seleccionado)
				marcaActivada.setMarcaM(1);
			else
				marcaActivada.setMarcaM(0);
			break;
		case 13:
			if (seleccionado)
				marcaActivada.setMarcaN(1);
			else
				marcaActivada.setMarcaN(0);
			break;
		case 14:
			if (seleccionado)
				marcaActivada.setMarcaO(1);
			else
				marcaActivada.setMarcaO(0);
			break;
		case 15:
			if (seleccionado)
				marcaActivada.setMarcaP(1);
			else
				marcaActivada.setMarcaP(0);
			break;
		case 16:
			if (seleccionado)
				marcaActivada.setMarcaQ(1);
			else
				marcaActivada.setMarcaQ(0);
			break;
		case 17:
			if (seleccionado)
				marcaActivada.setMarcaR(1);
			else
				marcaActivada.setMarcaR(0);
			break;
		case 18:
			if (seleccionado)
				marcaActivada.setMarcaS(1);
			else
				marcaActivada.setMarcaS(0);
			break;
		case 19:
			if (seleccionado)
				marcaActivada.setMarcaT(1);
			else
				marcaActivada.setMarcaT(0);
			break;
		case 20:
			if (seleccionado)
				marcaActivada.setMarcaU(1);
			else
				marcaActivada.setMarcaU(0);
			break;
		case 21:
			if (seleccionado)
				marcaActivada.setMarcaV(1);
			else
				marcaActivada.setMarcaV(0);
			break;
		case 22:
			if (seleccionado)
				marcaActivada.setMarcaW(1);
			else
				marcaActivada.setMarcaW(0);
			break;
		case 23:
			if (seleccionado)
				marcaActivada.setMarcaX(1);
			else
				marcaActivada.setMarcaX(0);
			break;
		case 24:
			if (seleccionado)
				marcaActivada.setMarcaY(1);
			else
				marcaActivada.setMarcaY(0);
			break;
		case 25:
			if (seleccionado)
				marcaActivada.setMarcaZ(1);
			else
				marcaActivada.setMarcaZ(0);
			break;
		case 26:
			if (seleccionado)
				marcaActivada.setMarcaZA(1);
			else
				marcaActivada.setMarcaZA(0);
			break;
		case 27:
			if (seleccionado)
				marcaActivada.setMarcaZB(1);
			else
				marcaActivada.setMarcaZB(0);
			break;
		case 28:
			if (seleccionado)
				marcaActivada.setMarcaZC(1);
			else
				marcaActivada.setMarcaZC(0);
			break;
		case 29:
			if (seleccionado)
				marcaActivada.setMarcaZD(1);
			else
				marcaActivada.setMarcaZD(0);
			break;
		case 30:
			if (seleccionado)
				marcaActivada.setMarcaZE(1);
			else
				marcaActivada.setMarcaZE(0);
			break;
		case 31:
			if (seleccionado)
				marcaActivada.setMarcaZF(1);
			else
				marcaActivada.setMarcaZF(0);
			break;
		case 32:
			if (seleccionado)
				marcaActivada.setMarcaZG(1);
			else
				marcaActivada.setMarcaZG(0);
			break;
		case 33:
			if (seleccionado)
				marcaActivada.setMarcaZH(1);
			else
				marcaActivada.setMarcaZH(0);
			break;
		case 34:
			if (seleccionado)
				marcaActivada.setMarcaZI(1);
			else
				marcaActivada.setMarcaZI(0);
			break;
		case 35:
			if (seleccionado)
				marcaActivada.setMarcaZJ(1);
			else
				marcaActivada.setMarcaZJ(0);
			break;
		case 36:
			if (seleccionado)
				marcaActivada.setMarcaZK(1);
			else
				marcaActivada.setMarcaZK(0);
			break;
		case 37:
			if (seleccionado)
				marcaActivada.setMarcaZL(1);
			else
				marcaActivada.setMarcaZL(0);
			break;
		case 38:
			if (seleccionado)
				marcaActivada.setMarcaZM(1);
			else
				marcaActivada.setMarcaZM(0);
			break;
		case 39:
			if (seleccionado)
				marcaActivada.setMarcaZN(1);
			else
				marcaActivada.setMarcaZN(0);
			break;
		case 40:
			if (seleccionado)
				marcaActivada.setMarcaZO(1);
			else
				marcaActivada.setMarcaZO(0);
			break;
		case 41:
			if (seleccionado)
				marcaActivada.setMarcaZP(1);
			else
				marcaActivada.setMarcaZP(0);
			break;
		case 42:
			if (seleccionado)
				marcaActivada.setMarcaZQ(1);
			else
				marcaActivada.setMarcaZQ(0);
			break;
		case 43:
			if (seleccionado)
				marcaActivada.setMarcaZR(1);
			else
				marcaActivada.setMarcaZR(0);
			break;
		case 44:
			if (seleccionado)
				marcaActivada.setMarcaZS(1);
			else
				marcaActivada.setMarcaZS(0);
			break;
		case 45:
			if (seleccionado)
				marcaActivada.setMarcaZT(1);
			else
				marcaActivada.setMarcaZT(0);
			break;
		case 46:
			if (seleccionado)
				marcaActivada.setMarcaZU(1);
			else
				marcaActivada.setMarcaZU(0);
			break;
		case 47:
			if (seleccionado)
				marcaActivada.setMarcaZV(1);
			else
				marcaActivada.setMarcaZV(0);
			break;
		case 48:
			if (seleccionado)
				marcaActivada.setMarcaZW(1);
			else
				marcaActivada.setMarcaZW(0);
			break;
		case 49:
			if (seleccionado)
				marcaActivada.setMarcaZX(1);
			else
				marcaActivada.setMarcaZX(0);
			break;
		}
	}
}
