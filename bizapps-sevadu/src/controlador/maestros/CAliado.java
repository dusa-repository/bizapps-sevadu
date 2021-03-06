package controlador.maestros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Cliente;
import modelo.maestros.Existencia;
import modelo.maestros.F0005;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroProducto;
import modelo.maestros.MappingProducto;
import modelo.maestros.MarcaActivadaVendedor;
import modelo.maestros.PlanVenta;
import modelo.maestros.Venta;
import modelo.pk.MappingProductoPK;

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
import componente.BuscadorUDC;
import componente.Catalogo;
import componente.Mensaje;

public class CAliado extends CGenerico {

	private static final long serialVersionUID = -7934953817552412985L;
	@Wire
	private Div divBuscadorZona;
	BuscadorUDC buscadorZona;
	@Wire
	private Div divBuscadorVendedor;
	BuscadorUDC buscadorVendedor;
	@Wire
	private Div divBuscadorCiudad;
	BuscadorUDC buscadorCiudad;
	@Wire
	private Div divBuscadorCategoria;
	BuscadorUDC buscadorCategoria;
	@Wire
	private Div divBuscadorEstado;
	BuscadorUDC buscadorEstado;
	@Wire
	private Textbox txtCodigo;
	@Wire
	private Textbox txtNombre;
	@Wire
	private Div divVAliado;
	@Wire
	private Div botoneraAliado;
	@Wire
	private Div catalogoAliado;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;
	Botonera botonera;
	Catalogo<MaestroAliado> catalogo;
	String clave = null;
	List<MaestroAliado> listaGeneral = new ArrayList<MaestroAliado>();

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
		cargarBuscadores();
		txtCodigo.setFocus(true);
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						MaestroAliado aliado = catalogo
								.objetoSeleccionadoDelCatalogo();
						clave = aliado.getCodigoAliado();
						txtCodigo.setValue(clave);
						txtNombre.setValue(aliado.getNombre());
						buscadorVendedor.settearCampo(servicioF0005.buscar(
								"00", "00", aliado.getCodigoVendedor()));
						buscadorZona.settearCampo(servicioF0005.buscar("00",
								"01", aliado.getZona()));
						buscadorEstado.settearCampo(servicioF0005.buscar("00",
								"02", aliado.getEstadoAliado()));
						buscadorCiudad.settearCampo(servicioF0005.buscar("00",
								"03", aliado.getCiudadAliado()));
						buscadorCategoria.settearCampo(servicioF0005.buscar(
								"00", "08", aliado.getCategoria()));
						txtCodigo.setDisabled(true);
						txtNombre.setFocus(true);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}
			}

			@Override
			public void salir() {
				cerrarVentana(divVAliado, cerrar, tabs, grxGraficoGeneral);
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
				clave = null;
			}

			@Override
			public void guardar() {

				if (validar()) {
					MaestroAliado aliado = new MaestroAliado();
					aliado.setCodigoAliado(txtCodigo.getValue());
					aliado.setNombre(txtNombre.getValue());
					aliado.setCiudadAliado(buscadorCiudad.obtenerCaja());
					aliado.setCategoria(buscadorCategoria.obtenerCaja());
					aliado.setEstadoAliado(buscadorEstado.obtenerCaja());
					aliado.setCodigoVendedor(buscadorVendedor.obtenerCaja());
					aliado.setZona(buscadorZona.obtenerCaja());
					aliado.setDescripcionVendedor(buscadorVendedor
							.obtenerLabel());
					aliado.setDescripcionZona(buscadorZona.obtenerLabel());
					aliado.setCodigoPadre("");
					aliado.setFechaAuditoria(fecha);
					aliado.setHoraAuditoria(tiempo);
					aliado.setIdUsuario(nombreUsuarioSesion());
					aliado.setLoteUpload("");
					aliado.setTipoCliente("");
					servicioAliado.guardar(aliado);
					aliado = servicioAliado.buscar(txtCodigo.getValue());
					mappearProductos(aliado);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					listaGeneral = servicioAliado.buscarTodosOrdenados();
					catalogo.actualizarLista(listaGeneral, true);
				}
			}

			@Override
			public void eliminar() {
				if (gpxDatos.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<MaestroAliado> eliminarLista = catalogo
								.obtenerSeleccionados();
						List<MaestroProducto> productos = servicioProducto
								.buscarPorAliados(eliminarLista);
						List<Cliente> clientes = servicioCliente
								.buscarPorAliados(eliminarLista);
						List<Existencia> existencias = servicioExistencia
								.buscarPorAliados(eliminarLista);
						List<MappingProducto> mappings = servicioMapping
								.buscarPorAliados(eliminarLista);
						List<MarcaActivadaVendedor> activadas = servicioMarcaActivada
								.buscarPorAliados(eliminarLista);
						List<PlanVenta> planes = servicioPlan
								.buscarPorAliados(eliminarLista);
						List<Venta> ventas = servicioVenta
								.buscarPorAliados(eliminarLista);
						if (productos.isEmpty() && existencias.isEmpty()
								&& clientes.isEmpty() && mappings.isEmpty()
								&& activadas.isEmpty() && planes.isEmpty()
								&& ventas.isEmpty()) {
							Messagebox
									.show("�Desea Eliminar los "
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
														servicioAliado
																.eliminarVarios(eliminarLista);
														msj.mensajeInformacion(Mensaje.eliminado);
														listaGeneral = servicioAliado
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
					MaestroAliado aliado = servicioAliado.buscar(clave);
					List<MaestroProducto> productos = servicioProducto
							.buscarPorAliado(aliado);
					List<Cliente> clientes = servicioCliente
							.buscarPorAliado(aliado);
					List<Existencia> existencias = servicioExistencia
							.buscarPorAliado(aliado);
					List<MappingProducto> mappings = servicioMapping
							.buscarPorAliado(aliado);
					List<MarcaActivadaVendedor> activadas = servicioMarcaActivada
							.buscarPorAliado(aliado);
					List<PlanVenta> planes = servicioPlan
							.buscarPorAliado(aliado);
					List<Venta> ventas = servicioVenta.buscarPorAliado(aliado);
					if (productos.isEmpty() && existencias.isEmpty()
							&& clientes.isEmpty() && mappings.isEmpty()
							&& activadas.isEmpty() && planes.isEmpty()
							&& ventas.isEmpty()) {
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
														servicioAliado
																.eliminarUno(clave);
														msj.mensajeInformacion(Mensaje.eliminado);
														limpiar();
														catalogo.actualizarLista(
																servicioAliado
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
		botoneraAliado.appendChild(botonera);
	}

	protected void mappearProductos(MaestroAliado aliado) {
		List<MaestroProducto> productos = servicioProducto
				.buscarComunesTodosAliados();
		if (!productos.isEmpty()) {
			List<MappingProducto> lista = new ArrayList<MappingProducto>();
			MappingProducto mappeado = new MappingProducto();
			MappingProductoPK claveMapping = new MappingProductoPK();
			for (int i = 0; i < productos.size(); i++) {
				claveMapping = new MappingProductoPK();
				claveMapping.setMaestroAliado(aliado);
				claveMapping.setMaestroProducto(productos.get(i));
				if (!servicioMapping.existe(claveMapping)) {
					mappeado = new MappingProducto();
					String botella = "BT";
					String cajas = "CA";
					String codigo = productos.get(i).getCodigoProductoDusa();
					mappeado.setId(claveMapping);
					mappeado.setCodigoBotellaCliente(botella);
					mappeado.setCodigoCajaCliente(cajas);
					mappeado.setCodigoProductoCliente(codigo);
					mappeado.setEstadoMapeo(1);
					mappeado.setFechaAuditoria(fecha);
					mappeado.setHoraAuditoria(tiempo);
					mappeado.setIdUsuario(nombreUsuarioSesion());
					mappeado.setLoteUpload("0");
					lista.add(mappeado);
				}
			}
			if (!lista.isEmpty())
				servicioMapping.guardarVarios(lista);
		}
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
		if (servicioAliado.existe(txtCodigo.getValue())) {
			msj.mensajeAlerta(Mensaje.claveUsada);
			txtCodigo.setFocus(true);
			return true;
		} else
			return false;
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

	public boolean camposLLenos() {
		if (txtCodigo.getText().compareTo("") == 0
				|| txtNombre.getText().compareTo("") == 0
				|| buscadorCategoria.obtenerCaja().compareTo("") == 0
				|| buscadorCiudad.obtenerCaja().compareTo("") == 0
				|| buscadorZona.obtenerCaja().compareTo("") == 0
				|| buscadorVendedor.obtenerCaja().compareTo("") == 0
				|| buscadorEstado.obtenerCaja().compareTo("") == 0) {
			return false;
		} else
			return true;
	}

	public boolean camposEditando() {
		if (txtCodigo.getText().compareTo("") != 0
				|| txtNombre.getText().compareTo("") != 0
				|| buscadorCategoria.obtenerCaja().compareTo("") != 0
				|| buscadorCiudad.obtenerCaja().compareTo("") != 0
				|| buscadorZona.obtenerCaja().compareTo("") != 0
				|| buscadorVendedor.obtenerCaja().compareTo("") != 0
				|| buscadorEstado.obtenerCaja().compareTo("") != 0) {
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
		txtNombre.setValue("");
		buscadorCiudad.settearCampo(null);
		buscadorCategoria.settearCampo(null);
		buscadorEstado.settearCampo(null);
		buscadorVendedor.settearCampo(null);
		buscadorZona.settearCampo(null);
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
		List<MaestroAliado> seleccionados = catalogo.obtenerSeleccionados();
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

		listF0005 = servicioF0005.buscarParaUDCOrdenados("00", "08");
		buscadorCategoria = new BuscadorUDC("Categoria", 100, listF0005, true,
				false, false, "00", "08", "29%", "18.5%", "6.5%", "28%") {
			@Override
			protected F0005 buscar() {
				return servicioF0005.buscar("00", "08",
						buscadorCategoria.obtenerCaja());
			}
		};
		divBuscadorCategoria.appendChild(buscadorCategoria);
	}

	private void mostrarCatalogo() {
		listaGeneral = servicioAliado.buscarTodosOrdenados();
		catalogo = new Catalogo<MaestroAliado>(catalogoAliado, "Aliado",
				listaGeneral, false, false, false, "Codigo", "Nombre", "Zona",
				"Vendedor") {

			@Override
			protected List<MaestroAliado> buscar(List<String> valores) {

				List<MaestroAliado> lista = new ArrayList<MaestroAliado>();

				for (MaestroAliado objeto : listaGeneral) {
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
		catalogo.setParent(catalogoAliado);
	}
}
