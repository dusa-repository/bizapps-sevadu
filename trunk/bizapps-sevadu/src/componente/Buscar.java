package componente;

import java.util.List;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

public abstract class Buscar<Clase> {

	public Buscar(Listbox lista2, Textbox txtBuscar) {
		llenarLista(lista2, txtBuscar);
	}

	/**
	 * Metodo de busqueda
	 */
	protected abstract List<Clase> buscar(String valor);

	public void llenarLista(final Listbox lista2, Textbox txtBuscar) {
//		final Combobox cmbBuscar = new Combobox();
//		cmbBuscar.setPlaceholder("Seleccione el Campo");
		txtBuscar.addEventListener(Events.ON_CHANGING,
				new EventListener<InputEvent>() {
					@Override
					public void onEvent(InputEvent e) throws Exception {
						List<Clase> listaNueva = buscar(e.getValue());
						lista2.setModel(new ListModelList<Clase>(listaNueva));
						lista2.setMultiple(false);
						lista2.setCheckmark(false);
						lista2.setMultiple(true);
						lista2.setCheckmark(true);
					}
				});
//		cmbBuscar.setModel(new ListModelList<String>(campos));
	}
}