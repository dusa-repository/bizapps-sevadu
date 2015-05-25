package controlador.maestros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Cliente;
import modelo.maestros.F0005;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MarcaActivadaVendedor;
import modelo.maestros.TipoCliente;
import modelo.maestros.Venta;
import modelo.seguridad.Arbol;
import modelo.seguridad.Usuario;
import modelo.seguridad.UsuarioAliado;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import componente.Botonera;
import componente.BuscadorUDC;
import componente.Catalogo;
import componente.Mensaje;
import componente.Validador;

public class CCartera extends CGenerico {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7839001273267848710L;
	@Wire
	private Div divBuscadorZona;
	BuscadorUDC buscadorZona;
	@Wire
	private Div divBuscadorVendedor;
	BuscadorUDC buscadorVendedor;
	@Wire
	private Div divBuscadorSupervisor;
	BuscadorUDC buscadorSupervisor;
	@Wire
	private Div divBuscadorCiudad;
	BuscadorUDC buscadorCiudad;
	@Wire
	private Div divBuscadorEstado;
	BuscadorUDC buscadorEstado;
	@Wire
	private Textbox txtCodigo;
	@Wire
	private Textbox txtNombre;
	@Wire
	private Textbox txtRif;
	@Wire
	private Textbox txtDireccion;
	@Wire
	private Textbox txtRuta;
	@Wire
	private Textbox txtAliado;
	@Wire
	private Textbox txtTipo;
	@Wire
	private Textbox txtCampo1;
	@Wire
	private Textbox txtCampo2;
	@Wire
	private Label lblAliado;
	@Wire
	private Label lblTipo;
	@Wire
	private Combobox cmbCanal;
	@Wire
	private Row rowAliado;
	@Wire
	private Div divVCartera;
	@Wire
	private Div botoneraCartera;
	@Wire
	private Div catalogoCartera;
	@Wire
	private Div divCatalogoAliado;
	@Wire
	private Div divCatalogoTipoCliente;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;
	Botonera botonera;
	Catalogo<MaestroAliado> catalogoAliado;
	Catalogo<TipoCliente> catalogoTipo;
	Catalogo<Cliente> catalogo;
	String clave = null;
	List<Cliente> listaGeneral = new ArrayList<Cliente>();
	String idAliado = null;
	private boolean admin = true;

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
		Usuario user = servicioUsuario.buscarPorLogin(nombreUsuarioSesion());
		UsuarioAliado objeto = servicioUsuarioAliado.buscarActivo(user);
		if (objeto != null) {
			idAliado = objeto.getId().getMaestroAliado().getCodigoAliado();
			admin = false;
			rowAliado.setVisible(false);
		}
		String[] titulo = { "Codigo", "Nombre", "Estado", "Vendedor",
				"Tipo Cliente", "Aliado" };
		String[] titulo2 = { "Codigo", "Nombre", "Estado", "Vendedor",
				"Tipo Cliente" };
		if (admin)
			mostrarCatalogo(titulo);
		else
			mostrarCatalogo(titulo2);
		cargarBuscadores();
		txtCodigo.setFocus(true);
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Cliente cliente = catalogo
								.objetoSeleccionadoDelCatalogo();
						clave = cliente.getCodigoCliente();
						txtCodigo.setValue(clave);
						txtNombre.setValue(cliente.getNombre());
						txtDireccion.setValue(cliente.getDireccion());
						txtRif.setValue(cliente.getRif());
						txtRuta.setValue(cliente.getRutaDistribucion());
						cmbCanal.setValue(cliente.getSegmentacion());
						if (cliente.getCampo1() != null)
							txtCampo1.setValue(cliente.getCampo1());
						if (cliente.getCampo2() != null)
							txtCampo2.setValue(cliente.getCampo2());
						buscadorVendedor.settearCampo(servicioF0005.buscar(
								"00", "00", cliente.getVendedor()));
						buscadorZona.settearCampo(servicioF0005.buscar("00",
								"01", cliente.getZona()));
						buscadorEstado.settearCampo(servicioF0005.buscar("00",
								"02", cliente.getEstado()));
						buscadorCiudad.settearCampo(servicioF0005.buscar("00",
								"03", cliente.getCiudad()));
						buscadorSupervisor.settearCampo(servicioF0005.buscar(
								"00", "00", cliente.getSupervisor()));
						if (cliente.getMaestroAliado() != null) {
							if (admin) {
								txtAliado.setValue(cliente.getMaestroAliado()
										.getCodigoAliado());
								lblAliado.setValue(cliente.getMaestroAliado()
										.getNombre());
							}
						}
						if (cliente.getTipoCliente() != null) {
							txtTipo.setValue(cliente.getTipoCliente()
									.getCodigo());
							lblTipo.setValue(cliente.getTipoCliente()
									.getDescripcion());
						}
						txtCodigo.setDisabled(true);
						txtNombre.setFocus(true);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}
			}

			@Override
			public void salir() {
				cerrarVentana(divVCartera, cerrar, tabs, grxGraficoGeneral);
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
					Cliente cliente = new Cliente();
					cliente.setCodigoCliente(txtCodigo.getValue());
					cliente.setNombre(txtNombre.getValue());
					cliente.setCiudad(buscadorCiudad.obtenerCaja());
					cliente.setEstado(buscadorEstado.obtenerCaja());
					cliente.setVendedor(buscadorVendedor.obtenerCaja());
					cliente.setSupervisor(buscadorSupervisor.obtenerCaja());
					cliente.setZona(buscadorZona.obtenerCaja());
					cliente.setDireccion(txtDireccion.getValue());
					cliente.setRif(txtRif.getValue());
					cliente.setRutaDistribucion(txtRuta.getValue());
					cliente.setSegmentacion(cmbCanal.getValue());
					cliente.setCampo1(txtCampo1.getValue());
					cliente.setCampo2(txtCampo2.getValue());
					MaestroAliado aliado = new MaestroAliado();
					if (admin)
						aliado = servicioAliado.buscar(txtAliado.getValue());
					else
						aliado = servicioAliado.buscar(idAliado);
					cliente.setMaestroAliado(aliado);
					TipoCliente tipoCliente = servicioTipoCliente
							.buscarPorCodigo(txtTipo.getValue());
					cliente.setTipoCliente(tipoCliente);
					servicioCliente.guardar(cliente);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					listaGeneral = servicioCliente.buscarTodosOrdenados();
					catalogo.actualizarLista(listaGeneral, true);
				}
			}

			@Override
			public void eliminar() {
				if (gpxDatos.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Cliente> eliminarLista = catalogo
								.obtenerSeleccionados();
						List<MarcaActivadaVendedor> activadas = servicioMarcaActivada
								.buscarPorClientes(eliminarLista);
						List<Venta> ventas = servicioVenta
								.buscarPorClientes(eliminarLista);
						if (activadas.isEmpty() && ventas.isEmpty()) {
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
														servicioCliente
																.eliminarVarios(eliminarLista);
														msj.mensajeInformacion(Mensaje.eliminado);
														listaGeneral = servicioCliente
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
					Cliente cliente = servicioCliente.buscarPorCodigo(clave);
					List<MarcaActivadaVendedor> activadas = servicioMarcaActivada
							.buscarPorCliente(cliente);
					List<Venta> ventas = servicioVenta
							.buscarPorCliente(cliente);
					if (activadas.isEmpty() && ventas.isEmpty()) {
						if (clave != null) {
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
														servicioCliente
																.eliminarUno(clave);
														msj.mensajeInformacion(Mensaje.eliminado);
														limpiar();
														catalogo.actualizarLista(
																servicioCliente
																		.buscarTodosOrdenados(),
																true);
													}
												}
											});
						} else
							msj.mensajeAlerta(Mensaje.noSeleccionoRegistro);
					} else
						msj.mensajeError(Mensaje.noEliminar);
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
		botoneraCartera.appendChild(botonera);
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
				|| txtCampo2.getText().compareTo("") != 0
				|| txtCampo1.getText().compareTo("") != 0
				|| txtNombre.getText().compareTo("") != 0
				|| txtRif.getText().compareTo("") != 0
				|| txtRuta.getText().compareTo("") != 0
				|| txtDireccion.getText().compareTo("") != 0
				|| cmbCanal.getText().compareTo("") != 0
				|| txtAliado.getText().compareTo("") != 0
				|| txtTipo.getText().compareTo("") != 0
				|| buscadorCiudad.obtenerCaja().compareTo("") != 0
				|| buscadorZona.obtenerCaja().compareTo("") != 0
				|| buscadorVendedor.obtenerCaja().compareTo("") != 0
				|| buscadorEstado.obtenerCaja().compareTo("") != 0
				|| buscadorSupervisor.obtenerCaja().compareTo("") != 0) {
			return true;
		} else
			return false;
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

	@Listen("onChange = #txtCodigo")
	public boolean claveExiste() {
		if (servicioCliente.existe(txtCodigo.getValue())) {
			msj.mensajeAlerta(Mensaje.claveUsada);
			txtCodigo.setFocus(true);
			return true;
		} else
			return false;
	}

	private boolean camposLLenos() {
		if (txtCodigo.getText().compareTo("") == 0
				|| txtNombre.getText().compareTo("") == 0
				|| txtRif.getText().compareTo("") == 0
				|| txtRuta.getText().compareTo("") == 0
				|| txtDireccion.getText().compareTo("") == 0
				|| cmbCanal.getText().compareTo("") == 0
				|| (admin && txtAliado.getText().compareTo("") == 0)
				|| txtTipo.getText().compareTo("") == 0
				|| buscadorCiudad.obtenerCaja().compareTo("") == 0
				|| buscadorZona.obtenerCaja().compareTo("") == 0
				|| buscadorVendedor.obtenerCaja().compareTo("") == 0
				|| buscadorEstado.obtenerCaja().compareTo("") == 0
				|| buscadorSupervisor.obtenerCaja().compareTo("") == 0) {
			return false;
		} else
			return true;
	}

	protected void habilitarTextClave() {
		if (txtCodigo.isDisabled())
			txtCodigo.setDisabled(false);
	}

	protected void limpiarCampos() {
		txtCodigo.setValue("");
		txtNombre.setValue("");
		if (admin) {
			txtAliado.setValue("");
			lblAliado.setValue("");
		}
		lblTipo.setValue("");
		txtCampo1.setValue("");
		txtCampo2.setValue("");
		txtDireccion.setValue("");
		txtRif.setValue("");
		txtRuta.setValue("");
		txtTipo.setValue("");
		cmbCanal.setValue("");
		buscadorCiudad.settearCampo(null);
		buscadorEstado.settearCampo(null);
		buscadorVendedor.settearCampo(null);
		buscadorZona.settearCampo(null);
		buscadorSupervisor.settearCampo(null);
		clave = null;
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
		List<Cliente> seleccionados = catalogo.obtenerSeleccionados();
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
				.buscarParaUDCOrdenados("00", "00");
		buscadorVendedor = new BuscadorUDC("Vendedor", 100, listF0005, true,
				false, false, "00", "00", "29%", "18.5%", "6.5%", "28%") {
			@Override
			protected F0005 buscar() {
				return servicioF0005.buscar("00", "00",
						buscadorVendedor.obtenerCaja());
			}
		};
		divBuscadorVendedor.appendChild(buscadorVendedor);

		listF0005 = servicioF0005.buscarParaUDCOrdenados("00", "00");
		buscadorSupervisor = new BuscadorUDC("Supervisor", 100, listF0005,
				true, false, false, "00", "00", "29%", "19%", "6%", "28%") {
			@Override
			protected F0005 buscar() {
				return servicioF0005.buscar("00", "00",
						buscadorSupervisor.obtenerCaja());
			}
		};
		divBuscadorSupervisor.appendChild(buscadorSupervisor);

		listF0005 = servicioF0005.buscarParaUDCOrdenados("00", "01");
		buscadorZona = new BuscadorUDC("Zona", 100, listF0005, true, false,
				false, "00", "01", "29%", "18.5%", "6.5%", "28%") {
			@Override
			protected F0005 buscar() {
				return servicioF0005.buscar("00", "01",
						buscadorZona.obtenerCaja());
			}
		};
		divBuscadorZona.appendChild(buscadorZona);

		listF0005 = servicioF0005.buscarParaUDCOrdenados("00", "02");
		buscadorEstado = new BuscadorUDC("Estado", 100, listF0005, true, false,
				false, "00", "02", "29%", "18.5%", "6.5%", "28%") {
			@Override
			protected F0005 buscar() {
				return servicioF0005.buscar("00", "02",
						buscadorEstado.obtenerCaja());
			}
		};
		divBuscadorEstado.appendChild(buscadorEstado);

		listF0005 = servicioF0005.buscarParaUDCOrdenados("00", "03");
		buscadorCiudad = new BuscadorUDC("Ciudad", 100, listF0005, true, false,
				false, "00", "03", "29%", "18.5%", "6.5%", "28%") {
			@Override
			protected F0005 buscar() {
				return servicioF0005.buscar("00", "03",
						buscadorCiudad.obtenerCaja());
			}
		};
		divBuscadorCiudad.appendChild(buscadorCiudad);
	}

	private void mostrarCatalogo(String[] titulo) {
		if (admin)
			listaGeneral = servicioCliente.buscarTodosOrdenados();
		else
			listaGeneral = servicioCliente.buscarPorIdAliado(idAliado);
		catalogo = new Catalogo<Cliente>(catalogoCartera, "Cartera",
				listaGeneral, false, false, false, titulo) {

			@Override
			protected List<Cliente> buscar(List<String> valores) {

				List<Cliente> lista = new ArrayList<Cliente>();

				for (Cliente objeto : listaGeneral) {
					if (admin) {
						if (objeto.getCodigoCliente().toLowerCase()
								.contains(valores.get(0).toLowerCase())
								&& objeto.getNombre().toLowerCase()
										.contains(valores.get(1).toLowerCase())
								&& objeto.getEstado().toLowerCase()
										.contains(valores.get(2).toLowerCase())
								&& objeto.getVendedor().toLowerCase()
										.contains(valores.get(3).toLowerCase())
								&& objeto.getTipoCliente().getDescripcion()
										.toLowerCase()
										.contains(valores.get(4).toLowerCase())
								&& objeto.getMaestroAliado().getNombre()
										.toLowerCase()
										.contains(valores.get(5).toLowerCase())) {
							lista.add(objeto);
						}
					} else {
						if (objeto.getCodigoCliente().toLowerCase()
								.contains(valores.get(0).toLowerCase())
								&& objeto.getNombre().toLowerCase()
										.contains(valores.get(1).toLowerCase())
								&& objeto.getEstado().toLowerCase()
										.contains(valores.get(2).toLowerCase())
								&& objeto.getVendedor().toLowerCase()
										.contains(valores.get(3).toLowerCase())
								&& objeto.getTipoCliente().getDescripcion()
										.toLowerCase()
										.contains(valores.get(4).toLowerCase())) {
							lista.add(objeto);
						}
					}
				}
				return lista;
			}

			@Override
			protected String[] crearRegistros(Cliente objeto) {
				String[] registros = new String[6];
				registros[0] = objeto.getCodigoCliente();
				registros[1] = objeto.getNombre();
				registros[2] = objeto.getEstado();
				registros[3] = objeto.getVendedor();
				registros[4] = objeto.getTipoCliente().getDescripcion();
				if (admin)
					registros[5] = objeto.getMaestroAliado().getNombre();
				return registros;
			}
		};
		catalogo.setParent(catalogoCartera);

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
		txtAliado.setValue(aliado.getCodigoAliado());
		lblAliado.setValue(aliado.getNombre());
		catalogoAliado.setParent(null);
	}

	@Listen("onChange = #txtAliado; onOK=#txtAliado")
	public void buscarNombreAliado() {
		MaestroAliado aliado = servicioAliado.buscar(txtAliado.getValue());
		if (aliado != null) {
			txtAliado.setValue(aliado.getCodigoAliado());
			lblAliado.setValue(aliado.getNombre());
		} else {
			msj.mensajeAlerta(Mensaje.noHayRegistros);
			txtAliado.setValue("");
			txtAliado.setFocus(true);
		}
	}

	@Listen("onClick = #btnBuscarTipo")
	public void mostrarCatalogoTipo() {
		final List<TipoCliente> listaObjetos = servicioTipoCliente
				.buscarTodos();
		catalogoTipo = new Catalogo<TipoCliente>(divCatalogoTipoCliente,
				"TipoCliente", listaObjetos, true, false, false, "Codigo",
				"Descripcion", "Canal") {

			@Override
			protected List<TipoCliente> buscar(List<String> valores) {

				List<TipoCliente> lista = new ArrayList<TipoCliente>();

				for (TipoCliente tipo : listaObjetos) {
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
		catalogoTipo.setClosable(true);
		catalogoTipo.setWidth("80%");
		catalogoTipo.setParent(divCatalogoTipoCliente);
		catalogoTipo.doModal();
	}

	@Listen("onSeleccion = #divCatalogoTipoCliente")
	public void seleccionTipo() {
		TipoCliente tipo = catalogoTipo.objetoSeleccionadoDelCatalogo();
		txtTipo.setValue(tipo.getCodigo());
		lblTipo.setValue(tipo.getDescripcion());
		catalogoTipo.setParent(null);
	}

	@Listen("onChange = #txtTipo; onOK=#txtTipo")
	public void buscarNombreTipo() {
		TipoCliente tipo = servicioTipoCliente.buscarPorCodigo(txtTipo
				.getValue());
		if (tipo != null) {
			txtTipo.setValue(tipo.getCodigo());
			lblTipo.setValue(tipo.getDescripcion());
		} else {
			msj.mensajeAlerta(Mensaje.noHayRegistros);
			txtTipo.setValue("");
			lblTipo.setValue("");
			txtTipo.setFocus(true);
		}
	}

}
