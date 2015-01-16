package controlador.transacciones;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.MaestroAliado;
import modelo.maestros.MappingProducto;
import modelo.seguridad.Arbol;
import modelo.seguridad.Usuario;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.maestros.SMaestroAliado;
import servicio.maestros.SMappingProducto;
import componente.Botonera;
import componente.Catalogo;
import componente.Mensaje;
import componente.Validador;
import controlador.maestros.CGenerico;

public class CMapping extends CGenerico {

	private static final long serialVersionUID = 9170871477477071451L;
	@Wire
	private Radio rdoTodos;
	@Wire
	private Radio rdoSi;
	@Wire
	private Radio rdoNo;
	@Wire
	private Textbox txtAliado;
	@Wire
	private Div botoneraMapping;
	@Wire
	private Div divMapping;
	@Wire
	private Div catalogoMapping;
	@Wire
	private Div divCatalogoAliado;
	@Wire
	private Label lblAliado;
	@Wire
	private Row row;
	Catalogo<MappingProducto> catalogo;
	Catalogo<MaestroAliado> catalogoAliado;
	List<MappingProducto> lista = new ArrayList<MappingProducto>();
	URL urlSi = getClass().getResource("../seguridad/si.png");
	URL urlNo = getClass().getResource("../seguridad/no.png");

	@Override
	public void inicializar() throws IOException {
		Authentication authe = SecurityContextHolder.getContext()
				.getAuthentication();
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(
				authe.getAuthorities());
		for (int i = 0; i < authorities.size(); i++) {
			Arbol arbol;
			String nombre = authorities.get(i).toString();
			if (Validador.validarNumero(nombre)) {
				arbol = servicioArbol.buscar(Long.parseLong(nombre));
				if (arbol.getNombre().equals("Ver Aliados Mapping"))
					row.setVisible(true);
			}
		}

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (map != null) {
			if (map.get("tabsGenerales") != null) {
				tabs = (List<Tab>) map.get("tabsGenerales");
				cerrar = (String) map.get("nombre");
				grxGraficoGeneral = (Groupbox) map.get("grxGraficoGeneral");
				map.clear();
				map = null;
			}
		}
		mostrarCatalogo();
		Botonera botonera = new Botonera() {

			@Override
			public void seleccionar() {
			}

			@Override
			public void salir() {
				cerrarVentana(divMapping, cerrar, tabs, grxGraficoGeneral);
			}

			@Override
			public void reporte() {
			}

			@Override
			public void limpiar() {
				rdoNo.setChecked(false);
				rdoSi.setChecked(false);
				rdoTodos.setChecked(false);
				txtAliado.setValue("");
				lblAliado.setValue("");
			}

			@Override
			public void guardar() {
			}

			@Override
			public void eliminar() {
			}

			@Override
			public void buscar() {
			}

			@Override
			public void ayuda() {
			}

			@Override
			public void annadir() {
			}
		};
		botonera.getChildren().get(0).setVisible(false);
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(2).setVisible(false);
		botonera.getChildren().get(3).setVisible(false);
		botonera.getChildren().get(4).setVisible(false);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botoneraMapping.appendChild(botonera);
	}

	private void mostrarCatalogo() {
		lista = new ArrayList<MappingProducto>();
		catalogo = new Catalogo<MappingProducto>(catalogoMapping, "", lista,
				false, false, true, "Codigo de Producto",
				"Descripcion del Producto", "Codigo Producto Aliado",
				"Codigo Caja Aliado", "Codigo Botella Aliado", "Mapeado") {

			@Override
			protected List<MappingProducto> buscar(List<String> valores) {

				List<MappingProducto> listaMapping = new ArrayList<MappingProducto>();
				for (MappingProducto objeto : lista) {
					String codigoProducto = "";
					if (objeto.getCodigoProductoCliente() != null)
						codigoProducto = objeto.getCodigoProductoCliente();
					String codigoCaja = "";
					if (objeto.getCodigoCajaCliente() != null)
						codigoCaja = objeto.getCodigoCajaCliente();
					String codigoBotella = "";
					if (objeto.getCodigoBotellaCliente() != null)
						codigoBotella = objeto.getCodigoBotellaCliente();
					if (objeto.getId().getMaestroProducto()
							.getCodigoProductoDusa().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& objeto.getId().getMaestroProducto()
									.getDescripcionProducto().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& codigoProducto.toLowerCase().contains(
									valores.get(2).toLowerCase())
							&& codigoCaja.toLowerCase().contains(
									valores.get(3).toLowerCase())
							&& codigoBotella.toLowerCase().contains(
									valores.get(4).toLowerCase())) {
						listaMapping.add(objeto);
					}
				}
				return listaMapping;
			}

			@Override
			protected String[] crearRegistros(MappingProducto objeto) {
				String imagenm = null;
				if (objeto.getLoteUpload().equals("No")) {
					imagenm = urlNo.toString();
				} else
					imagenm = urlSi.toString();
				String[] registros = new String[6];
				registros[0] = objeto.getId().getMaestroProducto()
						.getCodigoProductoDusa();
				registros[1] = objeto.getId().getMaestroProducto()
						.getDescripcionProducto();
				registros[2] = objeto.getCodigoProductoCliente();
				registros[3] = objeto.getCodigoCajaCliente();
				registros[4] = objeto.getCodigoBotellaCliente();
				registros[5] = imagenm;
				return registros;
			}

		};
		catalogo.setParent(catalogoMapping);
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

	@Listen("onChange = #txtAliado; onOK = #txtAliado")
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

	@Listen("onDoubleClick = #catalogoMapping")
	public void seleccionarItem() {
		if (catalogo.objetoSeleccionadoDelCatalogo() != null) {
			MappingProducto mapper = catalogo.objetoSeleccionadoDelCatalogo();
			catalogo.limpiarSeleccion();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("mapping", mapper);
			map.put("catalogo", catalogo);
			map.put("lista", lista);
			map.put("fila", row);
			map.put("text", txtAliado);
			map.put("radio1", rdoNo);
			map.put("radio2", rdoSi);
			map.put("radio3", rdoTodos);
			Sessions.getCurrent().setAttribute("mapear", map);
			Window window = (Window) Executions.createComponents(
					"/vistas/transacciones/VMappear.zul", null, null);
			window.doModal();
		}
	}

	@Listen("onClick = #btnRefrescar")
	public void refrescar() {
		if (row.isVisible() && txtAliado.getText().compareTo("") == 0) {
			msj.mensajeError("Debe seleccionar un Aliado del catalogo");
		} else {
			if (!rdoSi.isChecked() && !rdoNo.isChecked()
					&& !rdoTodos.isChecked())
				msj.mensajeError("Debe marcar una de las opciones de Filtrado");
			else {
				MaestroAliado aliado = new MaestroAliado();
				int tipo = 0;
				if (rdoSi.isChecked())
					tipo = 1;
				if (rdoNo.isChecked())
					tipo = 2;
				if (rdoTodos.isChecked())
					tipo = 3;
				if (row.isVisible())
					aliado = servicioAliado.buscar(txtAliado.getValue());
				else {
					Usuario user = servicioUsuario
							.buscarPorLogin(nombreUsuarioSesion());
					if (user.getMaestroAliado() != null) {
						aliado = user.getMaestroAliado();
					}
				}
				if (aliado != null) {
					switch (tipo) {
					case 1:
						lista = servicioMapping.buscarPorAliado(aliado);
						break;
					case 2:
						lista = servicioMapping.buscarPorAliadoNot(aliado);
						break;
					case 3:
						lista = servicioMapping.buscarTodosMasAliado(aliado);
						break;
					}
					catalogo.actualizarLista(lista, false);
				} else
					msj.mensajeError("Su usuario no esta asociado a ningun Aliado, pongase en contacto con el Administrador del sistema");
			}
		}
	}

	public void recibirLista(Catalogo<MappingProducto> catalogo2,
			List<MappingProducto> cupos, Row fila, Textbox caja,
			SMappingProducto servicioMappingg, SMaestroAliado servicioAliadoo,
			Radio radio1, Radio radio2, Radio radio3) {
		catalogo = catalogo2;
		lista = cupos;
		row = fila;
		txtAliado = caja;
		servicioMapping = servicioMappingg;
		servicioAliado = servicioAliadoo;
		rdoNo = radio1;
		rdoSi = radio2;
		rdoTodos = radio3;
		refrescar();
	}
}
