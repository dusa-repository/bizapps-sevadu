package componente;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Space;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

public abstract class Catalogo<Clase> extends Window {

	private static final long serialVersionUID = 1L;
	Listbox lsbCatalogo;
	Button exportador;
	Button pagineo;
	Mensaje msj = new Mensaje();
	Textbox txtSY;
	Label labelSYNombre;
	Textbox txtRT;
	Label labelRTNombre;
	Label labelBuscado;

	public Catalogo(final Component cGenerico, String titulo,
			List<Clase> lista, boolean emergente, boolean udc, boolean hibrido,
			String... campos) {
		super("", "2", false);
		this.setId("cmpCatalogo" + titulo);
		this.setStyle("background-header:#FF7925; background: #f4f2f2");
		// this.setWidth("auto");
		crearLista(lista, campos, emergente, udc, hibrido);
		lsbCatalogo.addEventListener(Events.ON_SELECT,
				new EventListener<Event>() {

					@Override
					public void onEvent(Event arg0) throws Exception {
						Events.postEvent(cGenerico, new Event("onSeleccion"));
					}
				});
	}

	public void crearLista(List<Clase> lista, String[] campos,
			final boolean emergente, boolean udc, final boolean hibrido) {
		exportador = new Button();
		exportador.setTooltiptext("Exportar los Datos como un Archivo");
		exportador.setSclass("catalogo");
		exportador.setSrc("/public/imagenes/botones/exportar.png");
		// ; float: right
		exportador.addEventListener(Events.ON_CLICK,
				new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						exportar();
					}
				});
		pagineo = new Button();
		pagineo.setTooltiptext("Presione para mostrar todos los registros en una sola lista, sin pagineo");
		pagineo.setSclass("catalogo");
		pagineo.setSrc("/public/imagenes/botones/pagineo.png");
		pagineo.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				pagineo();
			}
		});
		Hbox box = new Hbox();
		final Separator separador1 = new Separator();
		final Separator separador2 = new Separator();
		lsbCatalogo = new Listbox();
		lsbCatalogo.setMold("paging");
		lsbCatalogo.setPagingPosition("top");
		lsbCatalogo.setPageSize(9);
		final Auxhead cabecera = new Auxhead();
		Listhead lhdEncabezado = new Listhead();
		lhdEncabezado.setSizable(true);
		for (int i = 0; i < campos.length; i++) {
			final Textbox cajaTexto = new Textbox();
			cajaTexto.setContext(campos[i]);
			cajaTexto.setHflex("1");
			cajaTexto.setWidth("auto");
			cajaTexto.addEventListener(Events.ON_OK,
					new EventListener<KeyEvent>() {
						@Override
						public void onEvent(KeyEvent e) throws Exception {
							List<String> valores = new ArrayList<>();
							for (int i = 0; i < cabecera.getChildren().size(); i++) {
								Auxheader cabeceraFila = (Auxheader) cabecera
										.getChildren().get(i);
								Textbox te = (Textbox) cabeceraFila
										.getChildren().get(0);
								valores.add(te.getValue());
							}
							;
							String valor = cajaTexto.getValue();
							List<Clase> listaNueva = buscar(valores);
							lsbCatalogo.setModel(new ListModelList<Clase>(
									listaNueva));
							if (!emergente && !hibrido) {
								lsbCatalogo.setMultiple(false);
								lsbCatalogo.setCheckmark(false);
								lsbCatalogo.setMultiple(true);
								lsbCatalogo.setCheckmark(true);
							}
							cajaTexto.setValue(valor);
						}
					});
			cajaTexto.setPlaceholder("Buscar....");
			cajaTexto
					.setTooltiptext("Presione Enter para Filtrar la Informacion");
			Auxheader cabeceraFila = new Auxheader();
			cabeceraFila.appendChild(cajaTexto);
			cabecera.appendChild(cabeceraFila);
			Listheader listheader = new Listheader(campos[i]);
			listheader.setHflex("min");
			lhdEncabezado.appendChild(listheader);
		}
		lsbCatalogo.appendChild(cabecera);
		lsbCatalogo.setWidth("100%");
		lsbCatalogo.appendChild(lhdEncabezado);
//		lsbCatalogo.setSizedByContent(true);
		lsbCatalogo.setSpan("true");
		cabecera.setVisible(true);
		lhdEncabezado.setVisible(true);
		lsbCatalogo.setModel(new ListModelList<Clase>(lista));
		lsbCatalogo.setItemRenderer(new ListitemRenderer<Clase>() {

			@Override
			public void render(Listitem fila, Clase objeto, int arg2)
					throws Exception {
				fila.setValue(objeto);
				String[] registros = crearRegistros(objeto);
				for (int i = 0; i < registros.length; i++) {
					Listcell celda = new Listcell(registros[i]);
					celda.setParent(fila);
				}
			}
		});

		if (emergente) {
			this.setClosable(true);
			this.setWidth("80%");
			this.setTitle("Registros");
			if (udc) {
				Div div = new Div();
				Hbox vbox1 = new Hbox();
				vbox1.setWidth("100%");
				vbox1.setHeight("12px");
				vbox1.setAlign("start");
				vbox1.setPack("start");
				labelSYNombre = new Label("Cd producto: ");
				labelSYNombre.setClass("etiqueta");
				vbox1.appendChild(labelSYNombre);
				txtSY = new Textbox();
				txtSY.setDisabled(true);
				Space space = new Space();
				space.setWidth("9px");
				vbox1.appendChild(space);
				vbox1.appendChild(txtSY);
				vbox1.appendChild(new Label());
				Hbox vbox2 = new Hbox();
				vbox2.setWidth("100%");
				vbox2.setHeight("12px");
				labelRTNombre = new Label("Cd def Usuario: ");
				labelRTNombre.setClass("etiqueta");
				vbox2.appendChild(labelRTNombre);
				txtRT = new Textbox();
				txtRT.setDisabled(true);
				vbox2.appendChild(txtRT);
				labelBuscado = new Label();
				vbox2.appendChild(labelBuscado);
				vbox2.setAlign("center");
				vbox2.setPack("center");
				Vbox cajaVertical = new Vbox();
				cajaVertical.setWidth("100%");
				cajaVertical.setAlign("center");
				cajaVertical.setPack("center");
				cajaVertical.appendChild(vbox1);
				cajaVertical.appendChild(vbox2);
				div.appendChild(cajaVertical);

				this.appendChild(div);
			}
			this.appendChild(separador2);
			this.appendChild(lsbCatalogo);
			// this.exportador.setVisible(false);
			// this.pagineo.setVisible(false);
			lsbCatalogo.setMultiple(true);
			lsbCatalogo.setCheckmark(true);
			lsbCatalogo.setMultiple(false);
			lsbCatalogo.setCheckmark(false);
		} else {
			this.setWidth("100%");
			this.setClosable(false);
			// this.appendChild(separador1);
			this.appendChild(box);
			this.appendChild(separador2);
			this.appendChild(lsbCatalogo);
			if (hibrido) {
				lsbCatalogo.setMultiple(true);
				lsbCatalogo.setCheckmark(true);
				lsbCatalogo.setMultiple(false);
				lsbCatalogo.setCheckmark(false);
			} else {
				Space espacio = new Space();
				espacio.setHeight("8px");
				espacio.setStyle("background:white");
				box.appendChild(espacio);
				box.setStyle("background:white");
				box.appendChild(exportador);
				box.appendChild(pagineo);
				box.setWidth("100%");
				box.setAlign("end");
				box.setHeight("8px");
				box.setWidths("96%,2%,2%");
				lsbCatalogo.setMultiple(false);
				lsbCatalogo.setCheckmark(false);
				lsbCatalogo.setMultiple(true);
				lsbCatalogo.setCheckmark(true);
			}
		}
	}

	protected void pagineo() {
		if (lsbCatalogo.getPagingPosition().equals("top")) {
			lsbCatalogo.setMold("default");
			lsbCatalogo.setPagingPosition("both");
			pagineo.setTooltiptext("Presione para mostrar la lista con pagineo");
		} else {
			lsbCatalogo.setMold("paging");
			lsbCatalogo.setPagingPosition("top");
			lsbCatalogo.setPageSize(10);
			pagineo.setTooltiptext("Presione para mostrar todos los registros en una sola lista, sin pagineo");
		}
	}

	protected void exportar() {
		if (lsbCatalogo.getItemCount() != 0) {
			String s = ";";
			final StringBuffer sb = new StringBuffer();

			for (Object head : lsbCatalogo.getHeads()) {
				String h = "";
				if (head instanceof Listhead) {
					for (Object header : ((Listhead) head).getChildren()) {
						h += ((Listheader) header).getLabel() + s;
					}
					sb.append(h + "\n");
				}
			}
			for (Object item : lsbCatalogo.getItems()) {
				String i = "";
				for (Object cell : ((Listitem) item).getChildren()) {
					i += ((Listcell) cell).getLabel() + s;
				}
				sb.append(i + "\n");
			}
			Messagebox.show(Mensaje.exportar, "Alerta", Messagebox.OK
					| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								Filedownload.save(sb.toString().getBytes(),
										"text/plain", "datos.csv");
							}
						}
					});
		} else
			msj.mensajeAlerta(Mensaje.noHayRegistros);
	}

	/**
	 * Metodo que permite llamar un servicio dependiendo el controlador que
	 * busque, es decir que haga un filtro dentro del catalogo, ayudando asi al
	 * usuario a encontrar el registro buscado con mayor facilidad
	 */
	protected abstract List<Clase> buscar(List<String> valores);

	/**
	 * Metodo que permite por cada controlador indicar cuales son los registros
	 * que quiere mostrar en su catalogo, formando una matriz de String
	 */
	protected abstract String[] crearRegistros(Clase objeto);

	public Clase objetoSeleccionadoDelCatalogo() {
		if (lsbCatalogo.getSelectedItem() != null)
			return lsbCatalogo.getSelectedItem().getValue();
		else
			return null;
	}

	public Listbox getListbox() {
		return lsbCatalogo;
	}

	public void actualizarLista(List<Clase> lista, boolean check) {
		lsbCatalogo.setModel(new ListModelList<Clase>(lista));
		if(check)
		{
		 lsbCatalogo.setMultiple(false);
		 lsbCatalogo.setCheckmark(false);
		 lsbCatalogo.setMultiple(true);
		 lsbCatalogo.setCheckmark(true);
		}
	}

	public List<Clase> obtenerSeleccionados() {
		List<Clase> valores = new ArrayList<Clase>();
		boolean entro = false;
		if (lsbCatalogo.getItemCount() != 0) {
			final List<Listitem> list1 = lsbCatalogo.getItems();
			for (int i = 0; i < list1.size(); i++) {
				if (list1.get(i).isSelected()) {
					Clase clase = list1.get(i).getValue();
					entro = true;
					valores.add(clase);
				}
			}
			if (!entro) {
				valores.clear();
				return valores;
			}
			return valores;
		} else
			return null;
	}

	/**
	 * Metodo que permite limpiar los items seleccionados en el catalogo
	 */
	public void limpiarSeleccion() {

		lsbCatalogo.clearSelection();

	}

}