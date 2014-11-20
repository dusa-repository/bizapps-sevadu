package controlador.transacciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.dusa.thermometer.Test;
import com.dusa.thermometer.service.bf.ProcessDataBf;
import com.dusa.thermometer.service.bf.ThermometerBf;
import com.dusa.thermometer.service.to.ThermometerTo;

import componente.Botonera;
import componente.Catalogo;
import componente.Mensaje;
import componente.Validador;
import controlador.maestros.CGenerico;
import modelo.maestros.MaestroAliado;
import modelo.seguridad.Arbol;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.West;
import org.zkoss.zul.Window;

public class CTermometro2 extends CGenerico {

	private static final long serialVersionUID = -7117413922267074045L;
	@Wire
	private Textbox txtAliado;
	@Wire
	private Div botoneraTermometroMarca;
	@Wire
	private Div divVTermometroMarca;
	@Wire
	private Div divCatalogoAliado;
	@Wire
	private Row rowTermometroMarca;
	@Wire
	private Datebox dtbDesde;
	@Wire
	private Datebox dtbHasta;
	@Wire
	private Label lblAliado;
	Botonera botonera = null;
	Catalogo<MaestroAliado> catalogoAliado;
	West west;

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
				if (arbol.getNombre().equals("Ver Aliados Termometro"))
					rowTermometroMarca.setVisible(true);
			}
		}

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (map != null) {
			if (map.get("tabsGenerales") != null) {
				west = (West) map.get("west");
				tabs = (List<Tab>) map.get("tabsGenerales");
				cerrar = (String) map.get("nombre");
				map.clear();
				map = null;
			}
		}

		if (botonera == null) {
			botonera = new Botonera() {

				@Override
				public void seleccionar() {
					// TODO Auto-generated method stub

				}

				@Override
				public void salir() {
					cerrarVentana(divVTermometroMarca, cerrar, tabs);
					west.setOpen(true);
				}

				@Override
				public void reporte() {
					// TODO Auto-generated method stub

				}

				@Override
				public void limpiar() {
					txtAliado.setValue("");
					dtbDesde.setValue(fecha);
					dtbHasta.setValue(fecha);
					lblAliado.setValue("");
				}

				@Override
				public void guardar() {
					if (rowTermometroMarca.isVisible()
							&& txtAliado.getText().compareTo("") == 0) {
						msj.mensajeError("Debe seleccionar un Aliado del catalogo");
					} else {
						Date fechaDesde = dtbDesde.getValue();
						Date fechaHasta = dtbHasta.getValue();
						MaestroAliado aliado = new MaestroAliado();
						if (rowTermometroMarca.isVisible())
							aliado = servicioAliado
									.buscar(txtAliado.getValue());
						else
							aliado = servicioAliado
									.buscarPorLoginUsuario(nombreUsuarioSesion());
						if (aliado != null) {
							
							HashMap<String, Object> mapiin = new HashMap<String, Object>();
							mapiin.put("id", "id");
							mapiin.put("aliado", aliado);
							mapiin.put("desde", fechaDesde);
							mapiin.put("hasta", fechaHasta);
							Sessions.getCurrent().setAttribute("termometro",
									mapiin);
							Window window = (Window) Executions.createComponents(
									"/vistas/transacciones/VTermometro2.zul", null,
									mapiin);
							window.doModal();

						} else
							msj.mensajeError("Su usuario no esta asociado a ningun Aliado, "
									+ "pongase en contacto con el Administrador del sistema");
					}
				}

				@Override
				public void eliminar() {
					// TODO Auto-generated method stub

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
					// TODO Auto-generated method stub

				}
			};
			Button guardar = (Button) botonera.getChildren().get(3);
			guardar.setLabel("Generar");
			guardar.setSrc("/public/imagenes/botones/reporte.png");
			botonera.getChildren().get(0).setVisible(false);
			botonera.getChildren().get(1).setVisible(false);
			botonera.getChildren().get(2).setVisible(false);
			botonera.getChildren().get(4).setVisible(false);
			botonera.getChildren().get(6).setVisible(false);
			botonera.getChildren().get(8).setVisible(false);
			botoneraTermometroMarca.appendChild(botonera);
		}

	}

	@Listen("onClick = #btnBuscarAliado")
	public void mostrarCatalogoAliado() {
		final List<MaestroAliado> listaObjetos = servicioAliado
				.buscarTodosOrdenados();
		catalogoAliado = new Catalogo<MaestroAliado>(divCatalogoAliado,
				"Catalogo de Aliados", listaObjetos, true, false, false, "Codigo", "Nombre",
				"Zona", "Vendedor") {

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
			lblAliado.setValue("");
		}
	}

}
