package controlador.transacciones;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zss.api.Exporter;
import org.zkoss.zss.api.Exporters;
import org.zkoss.zss.api.Importer;
import org.zkoss.zss.api.Importers;
import org.zkoss.zss.api.Range;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.api.model.Book;
import org.zkoss.zss.api.model.CellData;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zul.Filedownload;

import controlador.maestros.CGenerico;

public class CTermometro extends CGenerico {

	private static final long serialVersionUID = 589262803508771881L;
	@Wire
	private Spreadsheet ss;

	@Override
	public void inicializar() throws IOException {
		System.out.println(ss.getSBook().getBookName());
		Range range = Ranges.range(ss.getSelectedSheet(), 0, 0);
		CellData cellData = range.getCellData();
		System.out.println(cellData.getValue());
		cellData.setValue("HOOOOLA");
		cellData = range.getCellData();
		System.out.println(cellData.getValue());
	}

	@Listen("onClick=#btnExportar")
	public void expor() {
		Exporter exporter = Exporters.getExporter();
		Book book = ss.getBook();
		File file = null;
		try {
			file = File.createTempFile(
					Long.toString(System.currentTimeMillis()), "temp");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			exporter.export(book, fos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			Filedownload.save(new AMedia(book.getBookName(), null, null, file,
					true));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
