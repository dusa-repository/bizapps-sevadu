package controlador.portal;

import java.io.IOException;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.Window;

import controlador.maestros.CGenerico;

public class CIndex extends CGenerico {

	private static final long serialVersionUID = -2911126596875079981L;

	@Override
	public void inicializar() throws IOException {
	}
	
	@Listen("onClick = #lblOlvidoClave")
	public void abrirVentana(){
		Window window = (Window) Executions.createComponents(

				"/vistas/seguridad/VReinicioPassword.zul", null, null);

				window.doModal();
	}
}

