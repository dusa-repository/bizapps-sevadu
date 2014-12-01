package controlador.transacciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.MappingProducto;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componente.Botonera;
import componente.Catalogo;
import componente.Mensaje;

import controlador.maestros.CGenerico;

public class CMappear extends CGenerico {

	private static final long serialVersionUID = 8830573238319000839L;
	@Wire
	private Div botoneraMappear;
	@Wire
	private Textbox txtCodigo;
	@Wire
	private Textbox txtCaja;
	@Wire
	private Textbox txtBotella;
	@Wire
	private Window wdwMappear;
	@Wire
	private Label lblDescripcion;
	@Wire
	private Label lblCodigo;

	private CMapping controlador = new CMapping();
	Catalogo<MappingProducto> catalogo;
	List<MappingProducto> cupos = new ArrayList<MappingProducto>();
	MappingProducto mapping = null;
	private Row fila;
	private Textbox caja;
	private Radio radio1;
	private Radio radio2;
	private Radio radio3;

	@Override
	public void inicializar() throws IOException {
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapear");
		if (map != null) {
			mapping = (MappingProducto) map.get("mapping");
			catalogo = (Catalogo<MappingProducto>) map.get("catalogo");
			fila = (Row) map.get("fila");
			caja = (Textbox) map.get("text");
			radio1 = (Radio) map.get("radio1");
			radio2 = (Radio) map.get("radio2");
			radio3 = (Radio) map.get("radio3");
			lblCodigo.setValue(mapping.getId().getMaestroProducto()
					.getCodigoProductoDusa());
			lblDescripcion.setValue(mapping.getId().getMaestroProducto()
					.getDescripcionProducto());
			if (mapping.getCodigoProductoCliente() != null)
				txtCodigo.setValue(mapping.getCodigoProductoCliente());
			else
				txtCodigo.setValue("");
			if (mapping.getCodigoCajaCliente() != null)
				txtCaja.setValue(mapping.getCodigoCajaCliente());
			else
				txtCaja.setValue("");
			if (mapping.getCodigoBotellaCliente() != null)
				txtBotella.setValue(mapping.getCodigoBotellaCliente());
			else
				txtBotella.setValue("");
			map.clear();
			map = null;
		}
		Botonera botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub

			}

			@Override
			public void salir() {
				wdwMappear.onClose();
			}

			@Override
			public void reporte() {
				// TODO Auto-generated method stub

			}

			@Override
			public void limpiar() {
				txtCaja.setValue("");
				txtCodigo.setValue("");
				txtBotella.setValue("");
			}

			@Override
			public void guardar() {
				if (validar()) {
					MappingProducto mappeado = new MappingProducto();
					String botella = txtBotella.getValue();
					String cajas = txtCaja.getValue();
					String codigo = txtCodigo.getValue();
					mappeado.setId(mapping.getId());
					mappeado.setCodigoBotellaCliente(botella);
					mappeado.setCodigoCajaCliente(cajas);
					mappeado.setCodigoProductoCliente(codigo);
					mappeado.setEstadoMapeo(1);
					mappeado.setFechaAuditoria(fecha);
					mappeado.setHoraAuditoria(tiempo);
					mappeado.setIdUsuario(nombreUsuarioSesion());
					mappeado.setLoteUpload("0");
					servicioMapping.guardar(mappeado);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					salir();
					controlador.recibirLista(catalogo, cupos, fila, caja,
							servicioMapping, servicioAliado, radio1, radio2,
							radio3);

				}
			}

			@Override
			public void eliminar() {
				Messagebox.show("¿Desea Eliminar el Mapping?", "Alerta",
						Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener<Event>() {
							public void onEvent(Event evt)
									throws InterruptedException {
								if (evt.getName().equals("onOK")) {
									servicioMapping.eliminarUno(mapping.getId());
									msj.mensajeInformacion(Mensaje.eliminado);
									limpiar();
									salir();
									controlador.recibirLista(catalogo, cupos,
											fila, caja, servicioMapping,
											servicioAliado, radio1, radio2,
											radio3);
								}
							}
						});
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
		botonera.getChildren().get(2).setVisible(false);
		if (!servicioMapping.existe(mapping.getId()))
			botonera.getChildren().get(4).setVisible(false);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botonera.getChildren().get(5).setVisible(false);
		botoneraMappear.appendChild(botonera);
	}

	protected boolean validar() {

		if (txtBotella.getText().compareTo("") == 0
				|| txtBotella.getText().compareTo("") == 0
				|| txtBotella.getText().compareTo("") == 0) {
			msj.mensajeError(Mensaje.camposVacios);
			return false;
		} else
			return true;

	}

}
