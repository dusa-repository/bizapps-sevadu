package componente;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import modelo.maestros.Cliente;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroMarca;

import org.zkoss.chart.Charts;
import org.zkoss.chart.LinearGradient;
import org.zkoss.chart.PaneBackground;
import org.zkoss.chart.YAxis;
import org.zkoss.chart.model.DefaultDialModel;
import org.zkoss.chart.model.DialModel;
import org.zkoss.chart.model.DialModelScale;

import servicio.maestros.SVenta;

public class AngularActivacion extends Charts {

	private static final long serialVersionUID = -142373619572772555L;
	private DateFormat formatoFecha = new SimpleDateFormat("MMM");
	private DateFormat formatoCorrecto = new SimpleDateFormat("dd-MM-yyyy");
	private DateFormat formatoNuevo = new SimpleDateFormat("yyyy-MM");

	public AngularActivacion(List<MaestroMarca> listMark, List<Cliente> list,
			MaestroAliado aliado, SVenta servicioVenta, Date fechaDesde2,
			Date fechaHasta2) {
		super();
		this.setType("gauge");
		int cantidadClientes = list.size();
		int cantidadMarcasActivas = listMark.size();
		int cantidadVentasActivas = 0;
		for (int i = 0; i < list.size(); i++) {
			cantidadVentasActivas = cantidadVentasActivas
					+ servicioVenta.buscarVentasDeMarcasActivas(
							aliado.getCodigoAliado(), fechaDesde2, fechaHasta2,
							list.get(i).getCodigoCliente());
		}
		int total = cantidadClientes * cantidadMarcasActivas;
		DialModel dialmodel = new DefaultDialModel();
		dialmodel.setFrameBgColor(null);
		dialmodel.setFrameBgColor1(null);
		dialmodel.setFrameBgColor2(null);
		dialmodel.setFrameFgColor(null);
		Double primero = (double) (total * 5 / 100);
		Double segundo = total * 0.01 / 100;
		DialModelScale scale = dialmodel.newScale(0, total, -150, -300,
				primero.intValue(), segundo.intValue());
		scale.setText("Cajas");
		scale.setTickColor("#666666");
		if (cantidadVentasActivas > total)
			cantidadVentasActivas = total;
		scale.setValue(cantidadVentasActivas);
		Double valor = (double) (total / 2);
		int minimo = valor.intValue();
		scale.newRange(0, minimo, "#DF5353", 0.9, 1); // green
		scale.newRange(minimo, minimo + minimo * 0.5, "#DDDF0D", 0.9, 1); // yellow
		scale.newRange(minimo + minimo * 0.5, total, "#55BF3B", 0.9, 1); // red
		this.setModel(dialmodel);
		List<PaneBackground> backgrounds = new LinkedList<PaneBackground>();
		PaneBackground background1 = new PaneBackground();
		LinearGradient linearGradient1 = new LinearGradient(0, 0, 0, 1);
		linearGradient1.setStops("#FFF", "#333");
		background1.setBackgroundColor(linearGradient1);
		background1.setBorderWidth(0);
		background1.setOuterRadius("109%");
		backgrounds.add(background1);
		PaneBackground background2 = new PaneBackground();
		LinearGradient linearGradient2 = new LinearGradient(0, 0, 0, 1);
		linearGradient2.setStops("#333", "#FFF");
		background2.setBackgroundColor(linearGradient2);
		background2.setBorderWidth(1);
		background2.setOuterRadius("107%");
		backgrounds.add(background2);
		backgrounds.add(new PaneBackground());
		PaneBackground background3 = new PaneBackground();
		background3.setBackgroundColor("#DDD");
		background3.setBorderWidth(0);
		background3.setOuterRadius("105%");
		background3.setInnerRadius("103%");
		backgrounds.add(background3);
		this.getPane().setBackground(backgrounds);
		YAxis yAxis = this.getYAxis();
		yAxis.setMinorTickWidth(1);
		yAxis.setMinorTickPosition("inside");
		yAxis.setMinorTickColor("#666");
		yAxis.setTickPixelInterval(30);
		yAxis.setTickWidth(2);
		yAxis.setTickPosition("inside");
		yAxis.setTickLength(10);
		yAxis.getLabels().setStep(2);
		yAxis.getLabels().setRotation("auto");
		this.getPlotOptions().getGauge().getTooltip()
				.setValueSuffix(" marcas");
		this.getSeries().setName("Marcas Vendidas");
		this.getYAxis().getTitle().setText("Marcas Vendidas");
		this.setTitle("Marcas Activadas VS Marcas Vendidas" + " desde "
				+ formatoCorrecto.format(fechaDesde2) + " hasta  "
				+ formatoCorrecto.format(fechaHasta2));
		this.setSubtitle("Aliado: " + aliado.getNombre() + " ("
				+ aliado.getCodigoAliado() + ")");
	}
}
