package controlador.maestros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.F0005;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroMarca;
import modelo.maestros.MaestroProducto;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublespinner;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

import componente.Botonera;
import componente.BuscadorUDC;
import componente.Catalogo;
import componente.Mensaje;

public class CProducto extends CGenerico {

	private static final long serialVersionUID = -3715629592945919921L;
	@Wire
	private Div divBuscadorCaja;
	BuscadorUDC buscadorCaja;
	@Wire
	private Div divBuscadorBotella;
	BuscadorUDC buscadorBotella;
	@Wire
	private Div divBuscadorEspecie;
	BuscadorUDC buscadorEspecie;
	@Wire
	private Textbox txtCodigo;
	@Wire
	private Textbox txtDescripcion;
	@Wire
	private Textbox txtMarca;
	@Wire
	private Textbox txtAliado;
	@Wire
	private Button btnBuscarAliado;
	@Wire
	private Label lblAliado;
	@Wire
	private Spinner spnPacking;
	@Wire
	private Doublespinner spnVolumen;
	@Wire
	private Checkbox chkSi;
	@Wire
	private Div divCatalogoMarca;
	@Wire
	private Div divCatalogoAliado;
	@Wire
	private Div divVProducto;
	@Wire
	private Div botoneraProducto;
	@Wire
	private Div catalogoProducto;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;
	Botonera botonera;
	Catalogo<MaestroProducto> catalogo;
	Catalogo<MaestroMarca> catalogoMarca;
	Catalogo<MaestroAliado> catalogoAliado;
	String clave = null;

	@Override
	public void inicializar() throws IOException {
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (map != null) {
			if (map.get("tabsGenerales") != null) {
				tabs = (List<Tab>) map.get("tabsGenerales");
				cerrar = (String) map.get("nombre");
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
						MaestroProducto producto = catalogo
								.objetoSeleccionadoDelCatalogo();
						clave = producto.getCodigoProductoDusa();
						txtCodigo.setValue(clave);
						txtDescripcion.setValue(producto
								.getDescripcionProducto());
						txtMarca.setValue(producto.getMaestroMarca()
								.getMarcaDusa());
						txtMarca.setValue(producto.getMaestroMarca()
								.getMarcaDusa());
						buscadorCaja.settearCampo(servicioF0005.buscar("00",
								"04", producto.getCodigoCajaDusa()));
						buscadorBotella.settearCampo(servicioF0005.buscar("00",
								"05", producto.getCodigoBotellaDusa()));
						buscadorEspecie.settearCampo(servicioF0005.buscar("00",
								"06", producto.getEspecieDusa()));
						spnPacking.setValue(producto.getPackingSizeDusa());
						Float valor = producto.getVolumenDusa();
						if (valor != null)
							spnVolumen.setValue(valor.doubleValue());
						if (producto.getMaestroAliado() != null) {
							chkSi.setChecked(false);
							txtAliado.setVisible(true);
							lblAliado.setVisible(true);
							btnBuscarAliado.setVisible(true);
							txtAliado.setValue(producto.getMaestroAliado()
									.getCodigoAliado());
						}
						txtCodigo.setDisabled(true);
						txtDescripcion.setFocus(true);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}
			}

			@Override
			public void salir() {
				cerrarVentana(divVProducto, cerrar, tabs);
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
					MaestroMarca marca = servicioMarca.buscar(txtMarca
							.getValue());
					Double volumen = spnVolumen.getValue();
					MaestroAliado aliado = new MaestroAliado();
					if (txtAliado.getValue().compareTo("") != 0)
						aliado = servicioAliado.buscar(txtAliado.getValue());
					else
						aliado = null;
					MaestroProducto producto = new MaestroProducto(
							txtCodigo.getValue(),
							buscadorBotella.obtenerCaja(),
							buscadorCaja.obtenerCaja(), marca.getDescripcion(),
							txtDescripcion.getValue(),
							buscadorEspecie.obtenerCaja(), fecha, tiempo,
							nombreUsuarioSesion(), "0", spnPacking.getValue(),
							volumen.floatValue(), aliado, marca);
					servicioProducto.guardar(producto);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					catalogo.actualizarLista(
							servicioProducto.buscarTodosOrdenados(), true);
				}
			}

			@Override
			public void eliminar() {
				if (gpxDatos.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<MaestroProducto> eliminarLista = catalogo
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
													servicioProducto
															.eliminarVarios(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(
															servicioProducto
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
													servicioProducto
															.eliminarUno(clave);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(
															servicioProducto
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
				// TODO Auto-generated method stub

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
		botoneraProducto.appendChild(botonera);
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
				|| txtMarca.getText().compareTo("") == 0
				|| spnPacking.getValue() == 0
				|| spnVolumen.getValue() == 0
				|| buscadorCaja.obtenerCaja().compareTo("") == 0
				|| buscadorBotella.obtenerCaja().compareTo("") == 0
				|| buscadorEspecie.obtenerCaja().compareTo("") == 0
				|| (!chkSi.isChecked() && txtAliado.getText().compareTo("") == 0)) {
			return false;
		} else
			return true;
	}

	@Listen("onChange = #txtCodigo")
	public boolean claveExiste() {
		if (servicioProducto.existe(txtCodigo.getValue())) {
			msj.mensajeAlerta(Mensaje.claveUsada);
			txtCodigo.setFocus(true);
			return true;
		} else
			return false;
	}

	public boolean camposEditando() {
		if (txtCodigo.getText().compareTo("") != 0
				|| txtDescripcion.getText().compareTo("") != 0
				|| txtMarca.getText().compareTo("") != 0
				|| spnPacking.getValue() != 0 || spnVolumen.getValue() != 0
				|| buscadorCaja.obtenerCaja().compareTo("") != 0
				|| buscadorBotella.obtenerCaja().compareTo("") != 0
				|| buscadorEspecie.obtenerCaja().compareTo("") != 0) {
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
		txtMarca.setValue("");
		txtAliado.setValue("");
		chkSi.setChecked(true);
		spnPacking.setValue(0);
		spnVolumen.setValue((double) 0);
		txtAliado.setVisible(false);
		lblAliado.setVisible(false);
		btnBuscarAliado.setVisible(false);
		buscadorCaja.settearCampo(null);
		buscadorBotella.settearCampo(null);
		buscadorEspecie.settearCampo(null);
		clave = null;
	}

	@Listen("onClick = #gpxRegistro")
	public void abrirRegistro() {
		gpxDatos.setOpen(false);
		gpxRegistro.setOpen(true);
		mostrarBotones(false);
	}

	protected void mostrarBotones(boolean b) {
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(2).setVisible(b);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botonera.getChildren().get(0).setVisible(b);
		botonera.getChildren().get(3).setVisible(!b);
		botonera.getChildren().get(5).setVisible(!b);
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

	protected boolean validarSeleccion() {
		List<MaestroProducto> seleccionados = catalogo.obtenerSeleccionados();
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
				.buscarParaUDCOrdenados("00", "04");
		buscadorCaja = new BuscadorUDC("Caja", 100, listF0005, true, false,
				false, "00", "04", "33%", "8%", "10%", "49%") {
			@Override
			protected F0005 buscar() {
				return servicioF0005.buscar("00", "04",
						buscadorCaja.obtenerCaja());
			}
		};
		divBuscadorCaja.appendChild(buscadorCaja);

		listF0005 = servicioF0005.buscarParaUDCOrdenados("00", "05");
		buscadorBotella = new BuscadorUDC("Botella", 100, listF0005, true,
				false, false, "00", "05", "33%", "8%", "10%", "49%") {
			@Override
			protected F0005 buscar() {
				return servicioF0005.buscar("00", "05",
						buscadorBotella.obtenerCaja());
			}
		};
		divBuscadorBotella.appendChild(buscadorBotella);

		listF0005 = servicioF0005.buscarParaUDCOrdenados("00", "06");
		buscadorEspecie = new BuscadorUDC("Especie", 100, listF0005, true,
				false, false, "00", "06", "33%", "8%", "10%", "49%") {
			@Override
			protected F0005 buscar() {
				return servicioF0005.buscar("00", "06",
						buscadorEspecie.obtenerCaja());
			}
		};
		divBuscadorEspecie.appendChild(buscadorEspecie);
	}

	private void mostrarCatalogo() {
		final List<MaestroProducto> listaObjetos = servicioProducto
				.buscarTodosOrdenados();
		catalogo = new Catalogo<MaestroProducto>(catalogoProducto, "Productos",
				listaObjetos, false, false, false, "Codigo", "Nombre", "Marca",
				"Descripcion Marca") {

			@Override
			protected List<MaestroProducto> buscar(List<String> valores) {

				List<MaestroProducto> lista = new ArrayList<MaestroProducto>();

				for (MaestroProducto objeto : listaObjetos) {
					if (objeto.getCodigoProductoDusa().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& objeto.getDescripcionProducto().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& objeto.getMaestroMarca().getMarcaDusa()
									.toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& objeto.getMaestroMarca().getDescripcion()
									.toLowerCase()
									.contains(valores.get(3).toLowerCase())) {
						lista.add(objeto);
					}
				}
				return lista;
			}

			@Override
			protected String[] crearRegistros(MaestroProducto objeto) {
				String[] registros = new String[4];
				registros[0] = objeto.getCodigoProductoDusa();
				registros[1] = objeto.getDescripcionProducto();
				registros[2] = objeto.getMaestroMarca().getMarcaDusa();
				registros[3] = objeto.getMaestroMarca().getDescripcion();
				return registros;
			}
		};
		catalogo.setParent(catalogoProducto);
	}

	@Listen("onClick = #btnBuscarMarca")
	public void mostrarCatalogoMarca() {
		final List<MaestroMarca> listaObjetos = servicioMarca
				.buscarTodosOrdenados();
		catalogoMarca = new Catalogo<MaestroMarca>(divCatalogoMarca, "Marca",
				listaObjetos, true, false, false, "Codigo", "Descripcion",
				"Termometro") {

			@Override
			protected List<MaestroMarca> buscar(List<String> valores) {

				List<MaestroMarca> lista = new ArrayList<MaestroMarca>();

				for (MaestroMarca objeto : listaObjetos) {
					String termo = "No";
					if (objeto.isFiltroTermometro())
						termo = "Si";
					if (objeto.getMarcaDusa().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& objeto.getDescripcion().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& termo.toLowerCase().contains(
									valores.get(2).toLowerCase())) {
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
				String[] registros = new String[3];
				registros[0] = objeto.getMarcaDusa();
				registros[1] = objeto.getDescripcion();
				registros[2] = termo;
				return registros;
			}
		};
		catalogoMarca.setClosable(true);
		catalogoMarca.setWidth("80%");
		catalogoMarca.setTitle("Marcas");
		catalogoMarca.setParent(divCatalogoMarca);
		catalogoMarca.doModal();
	}

	@Listen("onSeleccion = #divCatalogoMarca")
	public void seleccionMarca() {
		MaestroMarca marca = catalogoMarca.objetoSeleccionadoDelCatalogo();
		txtMarca.setValue(marca.getMarcaDusa());
		catalogoMarca.setParent(null);
	}

	@Listen("onChange = #txtMarca")
	public void buscarNombreMarca() {
		MaestroMarca marca = servicioMarca.buscar(txtMarca.getValue());
		if (marca != null) {
			txtMarca.setValue(marca.getMarcaDusa());
		} else {
			msj.mensajeAlerta(Mensaje.noHayRegistros);
			txtMarca.setValue("");
			txtMarca.setFocus(true);
		}
	}

	@Listen("onClick = #btnBuscarAliado")
	public void mostrarCatalogoAliado() {
		final List<MaestroAliado> listaObjetos = servicioAliado
				.buscarTodosOrdenados();
		catalogoAliado = new Catalogo<MaestroAliado>(divCatalogoAliado,
				"Aliado", listaObjetos, true, false, false, "Codigo",
				"Nombre", "Zona", "Vendedor") {

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
		catalogoAliado.setTitle("Registros");
		catalogoAliado.setParent(divCatalogoAliado);
		catalogoAliado.doModal();
	}

	@Listen("onSeleccion = #divCatalogoAliado")
	public void seleccionAliado() {
		MaestroAliado aliado = catalogoAliado.objetoSeleccionadoDelCatalogo();
		txtAliado.setValue(aliado.getCodigoAliado());
		catalogoAliado.setParent(null);
	}

	@Listen("onChange = #txtAliado")
	public void buscarNombreAliado() {
		MaestroAliado aliado = servicioAliado.buscar(txtAliado.getValue());
		if (aliado != null) {
			txtAliado.setValue(aliado.getCodigoAliado());
		} else {
			msj.mensajeAlerta(Mensaje.noHayRegistros);
			txtAliado.setValue("");
			txtAliado.setFocus(true);
		}
	}

	@Listen("onCheck = #chkSi")
	public void checkealo() {
		if (chkSi.isChecked()) {
			txtAliado.setVisible(false);
			lblAliado.setVisible(false);
			btnBuscarAliado.setVisible(false);
		} else {
			txtAliado.setVisible(true);
			lblAliado.setVisible(true);
			btnBuscarAliado.setVisible(true);
		}
	}
}
