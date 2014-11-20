package componente;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import modelo.maestros.F0004;
import modelo.maestros.F0005;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Space;
import org.zkoss.zul.Textbox;

public abstract class BuscadorUDC extends Hbox {

	private static final long serialVersionUID = 1L;
	Mensaje msj = new Mensaje();
	Catalogo<F0005> catalogo;
	Div divCatalogo;
	List<F0005> lista;
	Textbox cajaTexto;
	Label nombre;

	public BuscadorUDC(String etiqueta, int longitud, List<F0005> lista2,
			boolean requerido, boolean param1, boolean param2,
			final String valor1, final String valor2, final String width1,
			final String width2, final String width3, final String width4) {
		super();
		this.setWidth("100%");
		// this.setWidths("32%,8%,10%,50%");
		this.setWidths(width1 + "," + width2 + "," + width3 + "," + width4);
		this.setAlign("center");
		this.setPack("center");
		Label label = new Label(etiqueta + " :");
		label.setClass("etiqueta");

		cajaTexto = new Textbox();
		cajaTexto.setWidth("100%");
		// cajaTexto.setReadonly(true);
		cajaTexto.setMaxlength(longitud);
		cajaTexto
		.setTooltiptext("Código Definido por el Usuario (DRKY de "+valor1+","+valor2+")");
		// cajaTexto.setWidth("8px");
		// cajaTexto.setHflex("1");

		Button boton = new Button();
		boton.setAutodisable("true");
		boton.setTooltiptext("Catalogo de Codigo Definidos por el Usuario"
				+ " " + "(" + valor1 + "," + valor2 + ")");
		boton.setLabel("Buscar");
		boton.setSrc("/public/imagenes/botones/buscar.png");
		boton.setClass("btn");

		nombre = new Label();
		nombre.setWidth("100%");

		divCatalogo = new Div();
		divCatalogo.setTooltiptext("Click para Seleccionar un Codigo");

		if (requerido) {
			Hbox caja = new Hbox();
			Label lbl = new Label("*");
			lbl.setStyle("font-weight:bold;color:red");
			caja.appendChild(label);
			caja.appendChild(new Space());
			caja.appendChild(lbl);
			caja.setWidth("100%");
			this.appendChild(caja);
		} else {
			this.appendChild(label);
			label.setWidth("100%");
			// label.setHflex("1");
		}
		this.appendChild(cajaTexto);
		this.appendChild(boton);
		this.appendChild(nombre);
		this.appendChild(divCatalogo);

		cajaTexto.addEventListener(Events.ON_CHANGE,
				new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						buscarPorTexto();
					}
				});

		boton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				mostrarCatalogo(valor1, valor2);
			}
		});
		divCatalogo.addEventListener("onSeleccion", new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				seleccionarItem();
			}
		});
		lista = lista2;
	}

	protected void buscarPorTexto() {
		F0005 f0005 = buscar();
		if (f0005 != null) {
			cajaTexto.setValue(f0005.getId().getDrky());
			nombre.setValue(f0005.getDrdl01());
		} else {
			msj.mensajeAlerta(Mensaje.noHayRegistros);
			cajaTexto.setValue("");
			nombre.setValue("");
			cajaTexto.setFocus(true);
		}
	}

	// Debe contener un servicio que busca un valor de la clave
	protected abstract F0005 buscar();

	public void seleccionarItem() {
		F0005 f0005 = catalogo.objetoSeleccionadoDelCatalogo();
		cajaTexto.setValue(f0005.getId().getDrky());
		nombre.setValue(f0005.getDrdl01());
		catalogo.setParent(null);
	}

	public void inhabilitarCampo() {
		cajaTexto.setDisabled(true);
	}

	public void focus() {
		cajaTexto.setFocus(true);
	}

	public void habilitarCampos() {
		if (cajaTexto.isDisabled()) {
			cajaTexto.setDisabled(false);
		}
	}

	public String obtenerCaja() {
		return cajaTexto.getValue();
	}

	public String obtenerLabel() {
		return nombre.getValue();
	}

	public void settearCampo(F0005 f0005) {
		if (f0005 != null) {
			cajaTexto.setValue(f0005.getId().getDrky());
			nombre.setValue(f0005.getDrdl01());
		} else {
			cajaTexto.setValue("");
			nombre.setValue("");
		}
	}

	private void mostrarCatalogo(String valor1, String valor2) {

		final List<F0005> listF0005 = lista;

		catalogo = new Catalogo<F0005>(divCatalogo, "Catalogo de Codigos Definidos por el Usuario", listF0005,
				true, true, false, "KY", "Descripcion 01", "Descripcion 02",
				"Gestion Especial", "Codificacion Fija") {

			@Override
			protected List<F0005> buscar(List<String> valores) {

				List<F0005> listF0005_2 = new ArrayList<F0005>();

				for (F0005 f0005 : listF0005) {
					if (f0005.getId().getDrky().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& f0005.getDrdl01().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& f0005.getDrdl02().toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& f0005.getDrsphd().toLowerCase()
									.contains(valores.get(3).toLowerCase())
							&& f0005.getDrhrdc().toLowerCase()
									.contains(valores.get(4).toLowerCase())) {
						listF0005_2.add(f0005);
					}
				}
				return listF0005_2;
			}

			@Override
			protected String[] crearRegistros(F0005 f0005) {
				String[] registros = new String[5];
				registros[0] = f0005.getId().getDrky();
				registros[1] = f0005.getDrdl01();
				registros[2] = f0005.getDrdl02();
				registros[3] = f0005.getDrsphd();
				registros[4] = f0005.getDrhrdc();
				return registros;
			}
		};
		catalogo.settearCamposUdc(valor1, valor2);
		catalogo.setParent(divCatalogo);
		catalogo.doModal();
	}

}
