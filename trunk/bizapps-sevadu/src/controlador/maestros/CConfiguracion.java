package controlador.maestros;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Configuracion;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Tab;

import componente.Botonera;
import componente.Mensaje;

public class CConfiguracion extends CGenerico {

	private static final long serialVersionUID = 3394240712277711246L;

	@Wire
	private Div botoneraConfiguracion;
	@Wire
	private Div divConfiguracion;
	@Wire
	private Datebox dtbConfiguracion;
	private String nombre;

	@Override
	public void inicializar() throws IOException {
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (map != null) {
			if (map.get("tabsGenerales") != null) {
				tabs = (List<Tab>) map.get("tabsGenerales");
				nombre = (String) map.get("titulo");
				map.clear();
				map = null;
			}
		}
		actualizarEstado();
		Botonera botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub

			}

			@Override
			public void salir() {
				cerrarVentana(divConfiguracion, nombre, tabs);
			}

			@Override
			public void reporte() {
				// TODO Auto-generated method stub

			}

			@Override
			public void limpiar() {
			}

			@Override
			public void guardar() {
				Configuracion confi = new Configuracion();
				confi.setId(1);
				confi.setInicioFyActual(dtbConfiguracion.getValue());
				servicioConfiguracion.guardar(confi);
				msj.mensajeInformacion(Mensaje.guardado);
				actualizarEstado();
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
		botonera.getChildren().get(0).setVisible(false);
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(5).setVisible(false);
		botonera.getChildren().get(2).setVisible(false);
		botonera.getChildren().get(4).setVisible(false);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botoneraConfiguracion.appendChild(botonera);
	}

	protected void actualizarEstado() {
		Configuracion actual = servicioConfiguracion.buscar(1);
		if (actual != null) {
			dtbConfiguracion.setValue(actual.getInicioFyActual());

		}
	}

}
