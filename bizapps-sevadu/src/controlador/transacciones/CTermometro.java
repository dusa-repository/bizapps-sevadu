package controlador.transacciones;

import java.io.IOException;
import java.net.URL;

import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zss.api.Importer;
import org.zkoss.zss.api.Importers;
import org.zkoss.zss.api.model.Book;
import org.zkoss.zss.ui.Spreadsheet;

import controlador.maestros.CGenerico;

public class CTermometro extends CGenerico {

	private static final long serialVersionUID = 589262803508771881L;
	@Wire
	private Spreadsheet ss;

	@Override
	public void inicializar() throws IOException {
		 Importer importer = Importers.getImporter();
	     Book book = importer.imports(getFile(), "articulo");
	     System.out.println(book.getSheetAt(0));
	     System.out.println("hoa"+book.getBookName());
	     ss.setBook(book);
	}
    
   private URL getFile(){
	   URL reportFile = getClass().getResource(
				"/controlador/articulo.xlsx");
	   return reportFile;
   }
}
