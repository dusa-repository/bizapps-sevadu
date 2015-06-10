package controlador.reporte;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import modelo.maestros.Cliente;
import modelo.maestros.Configuracion;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroMarca;
import modelo.maestros.Venta;

import org.zkoss.chart.Chart;
import org.zkoss.chart.Charts;
import org.zkoss.chart.Color;
import org.zkoss.chart.Legend;
import org.zkoss.chart.LinearGradient;
import org.zkoss.chart.PaneBackground;
import org.zkoss.chart.Point;
import org.zkoss.chart.Series;
import org.zkoss.chart.YAxis;
import org.zkoss.chart.model.CategoryModel;
import org.zkoss.chart.model.DefaultCategoryModel;
import org.zkoss.chart.model.DefaultDialModel;
import org.zkoss.chart.model.DialModel;
import org.zkoss.chart.model.DialModelScale;
import org.zkoss.chart.plotOptions.DataLabels;
import org.zkoss.chart.plotOptions.LinePlotOptions;
import org.zkoss.chart.plotOptions.PieDataLabels;
import org.zkoss.chart.plotOptions.PiePlotOptions;
import org.zkoss.chart.plotOptions.SplinePlotOptions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

import controlador.maestros.CGenerico;

public class CGraficador extends CGenerico {

	private static final long serialVersionUID = 1222931146531648336L;
	@Wire
	private Charts chart;
	@Wire
	private Window wdwGraficas;
	private String aliado;
	private Date fechaDesde;
	private Date fechaHasta;
	private String tipoGrafica;
	private boolean dona = false;
	private boolean angular2 = false;
	private List<MaestroMarca> marcas = new ArrayList<MaestroMarca>();
	protected DateFormat formatoFecha = new SimpleDateFormat("MMM");
	protected DateFormat formatoCorrecto = new SimpleDateFormat("dd-MM-yyyy");
	protected DateFormat formatoNuevo = new SimpleDateFormat("yyyy-MM");

	@Override
	public void inicializar() throws IOException {

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("grafica");
		if (map != null) {
			aliado = (String) map.get("idAliado");
			if (aliado.equals("TODOS"))
				aliado = "%";
			fechaDesde = (Date) map.get("desde");
			fechaHasta = (Date) map.get("hasta");
			tipoGrafica = (String) map.get("tipo");
			marcas = (List<MaestroMarca>) map.get("lista");
			if (map.get("dona") != null) {
				dona = true;
			}
			if (map.get("tipo2") != null) {
				angular2 = true;
			}
			map.clear();
			map = null;
		}
		List<String> ids = new ArrayList<String>();
		for (Iterator<MaestroMarca> iterator = marcas.iterator(); iterator
				.hasNext();) {
			MaestroMarca marca = (MaestroMarca) iterator.next();
			ids.add(marca.getMarcaDusa());
		}
		chart.setType(tipoGrafica);
		switch (tipoGrafica) {
		case "line":
			generarGrafica(aliado, fechaDesde, fechaHasta, ids);
			break;
		case "bar":
			generarGraficaColumnas(aliado, fechaDesde, fechaHasta, ids);
			break;
		case "pie":
			if (dona)
				generarGraficaDona(aliado, fechaDesde, fechaHasta, ids);
			else
				generarGraficaPie(aliado, fechaDesde, fechaHasta, ids);
			break;
		case "gauge":
			if (!angular2)
				generarGraficaAngular(aliado, fechaDesde, fechaHasta, ids);
			else
				generarGraficaAngular2(aliado, fechaDesde, fechaHasta);
			break;
		case "mixto":
			generarGraficaMixta(aliado, fechaDesde, fechaHasta, ids);

		default:
			break;
		}
	}

	private void generarGraficaAngular2(String aliado2, Date fechaDesde2,
			Date fechaHasta2) {
		MaestroAliado aliado = servicioAliado.buscar(aliado2);
		List<Cliente> list = servicioCliente.buscarPorAliado(aliado);
		List<MaestroMarca> listMark = servicioMarca.buscarActivasActivacion();
		int cantidadClientes = list.size();
		int cantidadMarcasActivas = listMark.size();
		int cantidadVentasActivas = 0;
		for (int i = 0; i < list.size(); i++) {
			cantidadVentasActivas = cantidadVentasActivas
					+ servicioVenta.buscarVentasDeMarcasActivas(aliado2,
							fechaDesde2, fechaHasta2, list.get(i)
									.getCodigoCliente());
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
		// int minimo = valor.intValue();
		// scale.newRange(0, minimo, "#DF5353", 0.9, 1); // green
		// scale.newRange(minimo, minimo + minimo * 0.5, "#DDDF0D", 0.9, 1); //
		// yellow
		// scale.newRange(minimo + minimo * 0.5, total, "#55BF3B", 0.9, 1); //
		// red
		Double minimo = valor;
		Configuracion actual = servicioConfiguracion.buscar(1);
		Double valorPorcentual = (double) minimo;
		if (actual != null) {
			if (actual.getPorcentaje() != null) {
				valorPorcentual = actual.getPorcentaje().doubleValue();
				minimo = valorPorcentual;
			}
		}
		scale.newRange(0, total * (minimo / 100), "#DF5353", 0.9, 1); // green
		scale.newRange(total * (minimo / 100),
				(total - (total * (minimo / 100))) / 1.5
						+ (total * (minimo / 100)), "#DDDF0D", 0.9, 1); // yellow
		scale.newRange((total - (total * (minimo / 100))) / 1.5
				+ (total * (minimo / 100)), total, "#55BF3B", 0.9, 1); // red
		chart.setModel(dialmodel);
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
		chart.getPane().setBackground(backgrounds);
		YAxis yAxis = chart.getYAxis();
		yAxis.setMinorTickWidth(1);
		yAxis.setMinorTickPosition("inside");
		yAxis.setMinorTickColor("#666");
		yAxis.setTickPixelInterval(30);
		yAxis.setTickWidth(2);
		yAxis.setTickPosition("inside");
		yAxis.setTickLength(10);
		yAxis.getLabels().setStep(2);
		yAxis.getLabels().setRotation("auto");
		chart.getPlotOptions().getGauge().getTooltip()
				.setValueSuffix(" marcas");
		chart.getSeries().setName("Marcas Vendidas");
		MaestroAliado aliadoBuscar = servicioAliado.buscar(aliado2);
		String nombreAliado = "Todos";
		String codigoAliado = "Todos";
		if (aliadoBuscar != null) {
			nombreAliado = aliadoBuscar.getNombre();
			codigoAliado = aliadoBuscar.getCodigoAliado();
		}
		chart.getYAxis().getTitle().setText("Marcas Vendidas");
		chart.setTitle("Marcas a Activar(Obj) VS Marcas Vendidas" + " desde "
				+ formatoCorrecto.format(fechaDesde2) + " hasta  "
				+ formatoCorrecto.format(fechaHasta2));
		chart.setSubtitle("Aliado: " + nombreAliado + " (" + codigoAliado + ")");
	}

	private void generarGraficaAngular(String aliado2, Date fechaDesde2,
			Date fechaHasta2, List<String> ids) {
		List<Venta> ventas = servicioVenta
				.buscarPorAliadoEntreFechasYMarcasOrdenadoPorFecha(aliado2,
						fechaDesde2, fechaHasta2, ids, false);
		if (!ventas.isEmpty()) {
			double vendido = 0;
			DateFormat formatoAnno = new SimpleDateFormat("yyyy");
			DateFormat formatoMes = new SimpleDateFormat("MM");

			int annoPlanDesde = Integer.parseInt(formatoAnno
					.format(fechaDesde2));
			int mesPlanDesde = Integer.parseInt(formatoMes.format(fechaDesde2));
			int annoPlanHasta = Integer.parseInt(formatoAnno
					.format(fechaHasta2));
			int mesPlanHasta = Integer.parseInt(formatoMes.format(fechaHasta2));
			Double suma = (double) 0;
			do {
				if (mesPlanDesde == 13) {
					mesPlanDesde = 1;
					annoPlanDesde = annoPlanDesde + 1;
				}
				Double plan2 = servicioPlan.sumarPlanAliado(ventas.get(0)
						.getMaestroAliado(),
						ventas.get(0).getMaestroProducto(), annoPlanDesde,
						mesPlanDesde);
				suma = suma + plan2;
				mesPlanDesde = mesPlanDesde + 1;
			} while (annoPlanDesde != annoPlanHasta
					|| mesPlanDesde != mesPlanHasta + 1);

			for (int i = 0; i < ventas.size(); i++) {
				vendido = vendido + ventas.get(i).getCantidad();
			}
			vendido = Math.rint(vendido * 100) / 100;

			DialModel dialmodel = new DefaultDialModel();
			dialmodel.setFrameBgColor(null);
			dialmodel.setFrameBgColor1(null);
			dialmodel.setFrameBgColor2(null);
			dialmodel.setFrameFgColor(null);

			Double minimo = (double) 10;
			Configuracion actual = servicioConfiguracion.buscar(1);
			Double valorPorcentual = (double) minimo;
			if (actual != null) {
				if (actual.getPorcentaje() != null) {
					valorPorcentual = actual.getPorcentaje().doubleValue();
					minimo = valorPorcentual;
				}
			}
			double limiteSuperior = suma;
			double cantidad = suma - vendido;
			Double tope = (double) 0;
			if (cantidad < 0)
				tope = vendido;
			else
				tope = suma;

			Double primero = tope * 5 / 100;
			Double segundo = tope * 0.01 / 100;
			DialModelScale scale = dialmodel.newScale(0, tope, -150, -300,
					primero.intValue(), segundo.intValue());
			scale.setText("Cajas");
			scale.setTickColor("#666666");
			scale.setValue(vendido);
			scale.newRange(0, tope * (minimo / 100), "#DF5353", 0.9, 1); // green
			scale.newRange(tope * (minimo / 100),
					(tope - (tope * (minimo / 100))) / 1.5
							+ (tope * (minimo / 100)), "#DDDF0D", 0.9, 1); // yellow
			scale.newRange((tope - (tope * (minimo / 100))) / 1.5
					+ (tope * (minimo / 100)), tope, "#55BF3B", 0.9, 1); // red

			// scale.newRange(0, limiteSuperior - (limiteSuperior * minimo /
			// 100),
			// "#DF5353", 0.9, 1);
			// scale.newRange(limiteSuperior - (limiteSuperior * minimo / 100),
			// limiteSuperior, "#DDDF0D", 0.9, 1);
			// scale.newRange(limiteSuperior, tope.intValue(), "#55BF3B", 0.9,
			// 1);

			chart.setModel(dialmodel);
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
			chart.getPane().setBackground(backgrounds);
			YAxis yAxis = chart.getYAxis();
			yAxis.setMinorTickWidth(1);
			yAxis.setMinorTickPosition("inside");
			yAxis.setMinorTickColor("#666");
			yAxis.setTickPixelInterval(30);
			yAxis.setTickWidth(2);
			yAxis.setTickPosition("inside");
			yAxis.setTickLength(10);
			yAxis.getLabels().setStep(2);
			yAxis.getLabels().setRotation("auto");
			chart.getPlotOptions().getGauge().getTooltip()
					.setValueSuffix(" cajas");
			chart.getSeries().setName("Cajas Vendidas");
			MaestroAliado aliadoBuscar = servicioAliado.buscar(aliado2);
			String nombreAliado = "Todos";
			String codigoAliado = "Todos";
			if (aliadoBuscar != null) {
				nombreAliado = aliadoBuscar.getNombre();
				codigoAliado = aliadoBuscar.getCodigoAliado();
			}
			chart.getYAxis().getTitle().setText("Numero de Cajas");
			chart.setTitle("Vendido VS Planificado(Obj) de la Marca:"
					+ servicioMarca.buscar(ids.get(0)).getDescripcion()
					+ " desde " + formatoCorrecto.format(fechaDesde2)
					+ " hasta  " + formatoCorrecto.format(fechaHasta2));
			chart.setSubtitle("Aliado: " + nombreAliado + " (" + codigoAliado
					+ ")");
		}

		if (ventas.isEmpty())
			chart.setTitle("Vendido VS Planificado(Obj) de la Marca:"
					+ servicioMarca.buscar(ids.get(0)).getDescripcion()
					+ " desde " + formatoCorrecto.format(fechaDesde2)
					+ " hasta  " + formatoCorrecto.format(fechaHasta2) + "\n"
					+ "NO EXISTEN DATOS EN ESTE INTERVALO DE TIEMPO");

	}

	private void generarGraficaPie(String aliado2, Date fechaDesde2,
			Date fechaHasta2, List<String> ids) {

		List<Venta> ventas = servicioVenta
				.buscarPorAliadoEntreFechasYMarcasOrdenadoPorProducto(aliado2,
						fechaDesde2, fechaHasta2, ids, false);
		chart.getYAxis().setTitle("Total porcentaje de Ventas por Marca");
		Series serieMarca = chart.getSeries();
		serieMarca.setName("Porcentaje Participacion:");
		if (!ventas.isEmpty()) {
			Double valorTotal = servicioVenta
					.sumarPorAliadoEntreFechasYMarcasOrdenadoPorProducto(
							aliado2, fechaDesde2, fechaHasta2, ids);
			String marca = ventas.get(0).getMaestroProducto().getMaestroMarca()
					.getMarcaDusa();
			double vendidoMarca = 0;

			List<Color> colors = chart.getColors();
			int j = 0;
			for (int i = 0; i < ventas.size(); i++) {
				if (j == colors.size() - 1)
					j = 0;
				if (ventas.get(i).getMaestroProducto().getMaestroMarca()
						.getMarcaDusa().equals(marca)) {
					vendidoMarca = vendidoMarca + ventas.get(i).getCantidad();
				} else {
					double porcentaje = 0;
					if (valorTotal > 0)
						porcentaje = Math
								.rint((vendidoMarca * 100 / valorTotal) * 10) / 10;
					Point browserPoint = new Point(marca, porcentaje);
					browserPoint.setColor(colors.get(j));
					serieMarca.addPoint(browserPoint);

					vendidoMarca = 0;
					marca = ventas.get(i).getMaestroProducto()
							.getMaestroMarca().getMarcaDusa();
					i--;
					j++;
				}
			}
			double porcentaje = 0;
			if (valorTotal > 0)
				porcentaje = Math.rint((vendidoMarca * 100 / valorTotal) * 10) / 10;
			Point browserPoint = new Point(marca, porcentaje);
			browserPoint.setSelected(true);
			browserPoint.setSliced(true);
			browserPoint.setColor(colors.get(j));
			serieMarca.addPoint(browserPoint);
		}
		MaestroAliado aliadoBuscar = servicioAliado.buscar(aliado2);

		String nombreAliado = "Todos";
		String codigoAliado = "Todos";
		if (aliadoBuscar != null) {
			nombreAliado = aliadoBuscar.getNombre();
			codigoAliado = aliadoBuscar.getCodigoAliado();
		}

		chart.setTitle("Porcentaje de Participacion en Ventas(Marcas)"
				+ " desde " + formatoCorrecto.format(fechaDesde2) + " hasta  "
				+ formatoCorrecto.format(fechaHasta2));
		chart.setSubtitle("Aliado: " + nombreAliado + " (" + codigoAliado + ")");

		Chart chartOptional = chart.getChart();
		chartOptional.setBackgroundColor("");
		chartOptional.setPlotBorderWidth(0);
		chartOptional.setPlotShadow(false);

		chart.getTooltip().setPointFormat(
				"{series.name}: <b>{point.percentage:.1f}%</b>");
		PiePlotOptions plotOptions = chart.getPlotOptions().getPie();
		plotOptions.setAllowPointSelect(true);
		plotOptions.setCursor("pointer");
		PieDataLabels dataLabels = (PieDataLabels) plotOptions.getDataLabels();
		dataLabels.setEnabled(true);
		dataLabels.setColor("#000000");
		dataLabels.setConnectorColor("#000000");
		dataLabels.setFormat("<b>{point.name}</b>: {point.percentage:.1f} %");

		if (ventas.isEmpty())
			chart.setTitle("Porcentaje de Participacion en Ventas(Marcas)"
					+ " desde "
					+ formatoCorrecto.format(fechaDesde2)
					+ " hasta  "
					+ formatoCorrecto.format(fechaHasta2)
					+ "\n"
					+ " NO EXISTEN VENTAS REGISTRADAS EN ESTE INTERVALO DE TIEMPO");

	}

	private void generarGraficaDona(String aliado2, Date fechaDesde2,
			Date fechaHasta2, List<String> ids) {

		List<Venta> ventas = servicioVenta
				.buscarPorAliadoEntreFechasYMarcasOrdenadoPorProducto(aliado2,
						fechaDesde2, fechaHasta2, ids, false);
		chart.getYAxis().setTitle("Total porcentaje de Ventas por Marca");
		chart.getPlotOptions().getPie().setShadow(false);
		chart.getTooltip().setValueSuffix(" Cajas");
		Series serieMarca = chart.getSeries();
		Series serieProducto = chart.getSeries(1);
		serieMarca.setName("Marca");
		serieProducto.setName("Producto");
		if (!ventas.isEmpty()) {
			String marca = ventas.get(0).getMaestroProducto().getMaestroMarca()
					.getMarcaDusa();
			String producto = ventas.get(0).getMaestroProducto()
					.getCodigoProductoDusa();
			double vendidoMarca = 0;
			double vendidoProducto = 0;

			List<Color> colors = chart.getColors();
			int j = 0;
			for (int i = 0; i < ventas.size(); i++) {
				if (j == colors.size() - 1)
					j = 0;
				if (ventas.get(i).getMaestroProducto().getMaestroMarca()
						.getMarcaDusa().equals(marca)) {
					if (ventas.get(i).getMaestroProducto()
							.getCodigoProductoDusa().equals(producto))
						vendidoProducto = vendidoProducto
								+ ventas.get(i).getCantidad();
					else {
						vendidoMarca = vendidoMarca + vendidoProducto;
						Point versionPoint = new Point(producto,
								Math.rint(vendidoProducto * 100) / 100);
						versionPoint.setColor(colors.get(j));
						serieProducto.addPoint(versionPoint);
						producto = ventas.get(i).getMaestroProducto()
								.getCodigoProductoDusa();
						vendidoProducto = 0;
						i--;
					}
				} else {
					vendidoMarca = vendidoMarca + vendidoProducto;
					Point versionPoint = new Point(producto,
							Math.rint(vendidoProducto * 100) / 100);
					versionPoint.setColor(colors.get(j));
					serieProducto.addPoint(versionPoint);
					producto = ventas.get(i).getMaestroProducto()
							.getCodigoProductoDusa();
					vendidoProducto = 0;

					Point browserPoint = new Point(marca,
							Math.rint(vendidoMarca * 100) / 100);
					browserPoint.setColor(colors.get(j));
					serieMarca.addPoint(browserPoint);
					vendidoMarca = 0;
					marca = ventas.get(i).getMaestroProducto()
							.getMaestroMarca().getMarcaDusa();
					i--;
					j++;
				}
			}
			vendidoMarca = vendidoMarca + vendidoProducto;
			Point versionPoint = new Point(producto,
					Math.rint(vendidoProducto * 100) / 100);
			versionPoint.setColor(colors.get(j));
			serieProducto.addPoint(versionPoint);

			Point browserPoint = new Point(marca,
					Math.rint(vendidoMarca * 100) / 100);
			browserPoint.setColor(colors.get(j));
			serieMarca.addPoint(browserPoint);
		}
		MaestroAliado aliadoBuscar = servicioAliado.buscar(aliado2);
		String nombreAliado = "Todos";
		String codigoAliado = "Todos";
		if (aliadoBuscar != null) {
			nombreAliado = aliadoBuscar.getNombre();
			codigoAliado = aliadoBuscar.getCodigoAliado();
		}
		chart.setTitle("Volumen de Ventas por Marca y Producto" + " desde "
				+ formatoCorrecto.format(fechaDesde2) + " hasta  "
				+ formatoCorrecto.format(fechaHasta2));

		chart.setSubtitle("Aliado: " + nombreAliado + " (" + codigoAliado + ")");
		PiePlotOptions browserPlotOptions = new PiePlotOptions();
		browserPlotOptions.setSize("60%");
		serieMarca.setPlotOptions(browserPlotOptions);
		DataLabels browserLabels = serieMarca.getDataLabels();
		browserLabels.setFormat("{point.name}");
		browserLabels.setColor("white");
		browserLabels.setDistance(-30);
		PiePlotOptions versionPlotOptions = new PiePlotOptions();
		versionPlotOptions.setSize("80%");
		versionPlotOptions.setInnerSize("60%");
		serieProducto.setPlotOptions(versionPlotOptions);
		serieProducto.getDataLabels().setFormat(
				"<b>{point.name}:</b> {point.y:.2f} Cajas");

		if (ventas.isEmpty())
			chart.setTitle("Volumen de Ventas por Marca y Producto"
					+ " desde "
					+ formatoCorrecto.format(fechaDesde2)
					+ " hasta  "
					+ formatoCorrecto.format(fechaHasta2)
					+ "\n"
					+ " NO EXISTEN VENTAS REGISTRADAS EN ESTE INTERVALO DE TIEMPO");

	}

	private void generarGraficaColumnas(String aliado2, Date fechaDesde2,
			Date fechaHasta2, List<String> ids) {
		List<Venta> ventas = servicioVenta
				.buscarPorAliadoEntreFechasYMarcasOrdenadoPorFecha(aliado2,
						fechaDesde2, fechaHasta2, ids, true);
		CategoryModel modelo = new DefaultCategoryModel();
		if (!ventas.isEmpty()) {

			double vendido = 0;
			DateFormat formatoAnno = new SimpleDateFormat("yyyy");
			DateFormat formatoMes = new SimpleDateFormat("MM");
			Collections.sort(ids);
			for (int i = 0; i < ids.size(); i++) {
				String marca = ids.get(i);
				int annoPlanDesde = Integer.parseInt(formatoAnno
						.format(fechaDesde2));
				int mesPlanDesde = Integer.parseInt(formatoMes
						.format(fechaDesde2));
				int annoPlanHasta = Integer.parseInt(formatoAnno
						.format(fechaHasta2));
				int mesPlanHasta = Integer.parseInt(formatoMes
						.format(fechaHasta2));
				Double planTotal = (double) 0;
				do {
					if (mesPlanDesde == 13) {
						mesPlanDesde = 1;
						annoPlanDesde = annoPlanDesde + 1;
					}
					Double plan2 = servicioPlan.sumarPlanAliado2(ventas.get(0)
							.getMaestroAliado(), marca, annoPlanDesde,
							mesPlanDesde);
					planTotal = planTotal + plan2;
					mesPlanDesde = mesPlanDesde + 1;
				} while (annoPlanDesde != annoPlanHasta
						|| mesPlanDesde != mesPlanHasta + 1);
				modelo.setValue("Planificado", marca, planTotal);
			}
			String marca = ventas.get(0).getMaestroProducto().getMaestroMarca()
					.getMarcaDusa();
			for (int i = 0; i < ventas.size(); i++) {
				if (marca.equals(ventas.get(i).getMaestroProducto()
						.getMaestroMarca().getMarcaDusa()))
					vendido = vendido + ventas.get(i).getCantidad();
				else {
					modelo.setValue("Vendido", marca,
							Math.rint(vendido * 100) / 100);
					marca = ventas.get(i).getMaestroProducto()
							.getMaestroMarca().getMarcaDusa();
					vendido = 0;
					i--;
				}
			}
			modelo.setValue("Vendido", marca, Math.rint(vendido * 100) / 100);
		}
		MaestroAliado aliadoBuscar = servicioAliado.buscar(aliado2);

		String nombreAliado = "Todos";
		String codigoAliado = "Todos";
		if (aliadoBuscar != null) {
			nombreAliado = aliadoBuscar.getNombre();
			codigoAliado = aliadoBuscar.getCodigoAliado();
		}

		chart.setModel(modelo);
		chart.getXAxis().getTitle().setText("Numero de Cajas");
		chart.setTitle("Vendido VS Planificado de la Marca:"
				+ servicioMarca.buscar(ids.get(0)).getDescripcion() + " desde "
				+ formatoCorrecto.format(fechaDesde2) + " hasta  "
				+ formatoCorrecto.format(fechaHasta2));

		chart.setSubtitle("Aliado: " + nombreAliado + " (" + codigoAliado + ")");
		YAxis yAxis = chart.getYAxis();
		yAxis.setMin(0);
		yAxis.setTitle("Cajas");
		yAxis.getTitle().setAlign("high");
		yAxis.getLabels().setOverflow("justfy");

		chart.getTooltip().setValueSuffix(" cajas");

		chart.getPlotOptions().getBar().getDataLabels().setEnabled(true);

		Legend legend = chart.getLegend();
		legend.setLayout("vertical");
		legend.setAlign("right");
		legend.setVerticalAlign("top");
		legend.setX(-40);
		legend.setY(100);
		legend.setFloating(true);
		legend.setBorderWidth(1);
		legend.setShadow(true);

		chart.getCredits().setEnabled(false);
		// chart.getXAxis().setMin(0);
		// Tooltip tooltip = chart.getTooltip();
		// tooltip.setHeaderFormat("<span style=\"font-size:10px\">{point.key}</span><table>");
		// tooltip.setPointFormat("<tr><td style=\"color:{series.color};padding:0\">{series.name}: </td>"
		// + "<td style=\"padding:0\"><b>{point.y:.0f} Cajas</b></td></tr>");
		// tooltip.setFooterFormat("</table>");
		// tooltip.setShared(true);
		// tooltip.setUseHTML(true);
		//
		// chart.getPlotOptions().getColumn().setPointPadding(0.2);
		// chart.getPlotOptions().getColumn().setBorderWidth(0);

		if (ventas.isEmpty())
			chart.setTitle("Vendido VS Planificado de la Marca:"
					+ servicioMarca.buscar(ids.get(0)).getDescripcion()
					+ " desde "
					+ formatoCorrecto.format(fechaDesde2)
					+ " hasta  "
					+ formatoCorrecto.format(fechaHasta2)
					+ "\n"
					+ "NO EXISTEN VENTAS REGISTRADAS EN ESTE INTERVALO DE TIEMPO");
	}

	private void generarGrafica(String aliadoObjeto, Date fechaDesde2,
			Date fechaHasta2, List<String> marcas2) {
		List<Venta> ventas = servicioVenta
				.buscarPorAliadoEntreFechasYMarcasOrdenadoPorProducto(
						aliadoObjeto, fechaDesde2, fechaHasta2, marcas2, true);
		CategoryModel modelo = new DefaultCategoryModel();
		if (!ventas.isEmpty()) {
			String marca = ventas.get(0).getMaestroProducto().getMaestroMarca()
					.getMarcaDusa();
			String fecha = formatoFecha.format(ventas.get(0).getFechaFactura());
			double vendido = 0;
			for (int i = 0; i < ventas.size(); i++) {
				if (ventas.get(i).getMaestroProducto().getMaestroMarca()
						.getMarcaDusa().equals(marca)) {
					if (formatoFecha.format(ventas.get(i).getFechaFactura())
							.equals(fecha))
						vendido = vendido + ventas.get(i).getCantidad();
					else {
						modelo.setValue(marca, fecha,
								Math.rint(vendido * 100) / 100);
						fecha = formatoFecha.format(ventas.get(i)
								.getFechaFactura());
						vendido = 0;
						i--;
					}
				} else {
					modelo.setValue(marca, fecha,
							Math.rint(vendido * 100) / 100);
					vendido = 0;
					fecha = formatoFecha
							.format(ventas.get(i).getFechaFactura());
					marca = ventas.get(i).getMaestroProducto()
							.getMaestroMarca().getMarcaDusa();
					i--;
				}
			}
			modelo.setValue(marca, fecha, Math.rint(vendido * 100) / 100);
		}

		MaestroAliado aliadoBuscar = servicioAliado.buscar(aliadoObjeto);

		String nombreAliado = "Todos";
		String codigoAliado = "Todos";
		if (aliadoBuscar != null) {
			nombreAliado = aliadoBuscar.getNombre();
			codigoAliado = aliadoBuscar.getCodigoAliado();
		}
		chart.setModel(modelo);
		chart.getYAxis().getTitle().setText("Venta de Cajas");
		chart.setTitle("Ventas Mensuales por Marca desde "
				+ formatoCorrecto.format(fechaDesde2) + " hasta  "
				+ formatoCorrecto.format(fechaHasta2));
		chart.setSubtitle("Aliado: " + nombreAliado + " (" + codigoAliado + ")");

		chart.getXAxis().setMin(0);
		chart.getTooltip().setEnabled(true);
		chart.getTooltip().setValueSuffix(" Cajas");

		LinePlotOptions linePlotOptions = chart.getPlotData().getPlotOptions()
				.getLine();
		linePlotOptions.setEnableMouseTracking(true);
		linePlotOptions.getDataLabels().setEnabled(true);

		if (ventas.isEmpty())
			chart.setTitle("Ventas Mensuales por Marca desde "
					+ formatoCorrecto.format(fechaDesde2)
					+ " hasta  "
					+ formatoCorrecto.format(fechaHasta2)
					+ "\n"
					+ "NO EXISTEN VENTAS REGISTRADAS EN ESTE INTERVALO DE TIEMPO");
	}

	private void generarGraficaMixta(String aliado2, Date fechaDesde2,
			Date fechaHasta2, List<String> ids) {
		int annoPlanDesde = Integer.parseInt(formatoAnno.format(fechaDesde));
		int mesPlanDesde = Integer.parseInt(formatoMes.format(fechaDesde));
		int annoPlanHasta = Integer.parseInt(formatoAnno.format(fechaHasta));
		int mesPlanHasta = Integer.parseInt(formatoMes.format(fechaHasta));
		List<String> categorias = new ArrayList<String>();
		List<Integer> inventario = new ArrayList<Integer>();
		List<Double> ventas = new ArrayList<Double>();
		List<Double> ventasDusa = new ArrayList<Double>();
		// List<Double> compras = new ArrayList<Double>();
		// List<Double> comprasDusa = new ArrayList<Double>();
		do {
			if (mesPlanDesde == 13) {
				mesPlanDesde = 1;
				annoPlanDesde = annoPlanDesde + 1;
			}
			Date fechaInicio = null;
			try {
				fechaInicio = formatoCorrecto.parse("01-" + mesPlanDesde + "-"
						+ annoPlanDesde);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Date fechaFin = null;
			String ultimoDia = "";
			if (annoPlanDesde == annoPlanHasta && mesPlanDesde == mesPlanHasta)
				ultimoDia = formatoDia.format(fechaHasta) + "-";
			else
				ultimoDia = lastDay(fechaInicio);

			try {
				fechaFin = formatoCorrecto.parse(ultimoDia + mesPlanDesde + "-"
						+ annoPlanDesde);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			Double valorVenta = servicioVenta
					.sumarPorAliadoEntreFechasYMarcasOrdenadoPorProducto(
							aliado2, fechaInicio, fechaFin, ids);
			Double valorVentaDusa = valorVenta + 10;
			Integer valorInventario = servicioExistencia
					.sumarPorAliadoEntreFechasYMarcasOrdenadoPorProducto(
							aliado2, fechaInicio, fechaFin, ids);
			// Double valorCompra = (double) 0;
			// Double valorCompraDusa = (double) 0;
			ventas.add(valorVenta);
			ventasDusa.add(valorVentaDusa);
			inventario.add(valorInventario);
			categorias.add(formatoFecha.format(fechaInicio));

			mesPlanDesde = mesPlanDesde + 1;
		} while (annoPlanDesde != annoPlanHasta
				|| mesPlanDesde != mesPlanHasta + 1);

		chart.getXAxis().setCategories(categorias);

		YAxis yAxis3 = chart.getYAxis();
		yAxis3.setMin(0);
		yAxis3.setGridLineWidth(0);
		yAxis3.setTitle("Ventas Dusa");
		yAxis3.getLabels().setFormat("{value} Cajas");
		yAxis3.setOpposite(true);
		chart.getTooltip().setShared(true);

		YAxis yAxis1 = chart.getYAxis(1);
		yAxis1.setMin(0);
		yAxis1.getLabels().setFormat("{value} Cajas");
		yAxis1.setTitle("Inventario");
		yAxis1.setOpposite(true);

		YAxis yAxis2 = chart.getYAxis(2);
		yAxis2.setMin(0);
		yAxis2.setGridLineWidth(0);
		yAxis2.setTitle("Ventas");
		yAxis2.getLabels().setFormat("{value} Cajas");

		String color1 = chart.getColors().get(0).stringValue();
		String color2 = chart.getColors().get(1).stringValue();
		String color3 = chart.getColors().get(2).stringValue();
		yAxis1.getLabels().setStyle("color: '" + color1 + "'");
		yAxis1.getTitle().setStyle("color: '" + color1 + "'");
		yAxis2.getLabels().setStyle("color: '" + color2 + "'");
		yAxis2.getTitle().setStyle("color: '" + color2 + "'");
		yAxis3.getLabels().setStyle("color: '" + color3 + "'");
		yAxis3.getTitle().setStyle("color: '" + color3 + "'");

		Legend legend = chart.getLegend();
		legend.setLayout("vertical");
		legend.setAlign("left");
		legend.setX(120);
		legend.setVerticalAlign("top");
		legend.setY(80);
		legend.setFloating(true);

		Series rainfall = new Series("Inventario");
		rainfall.setName("Inventario");
		rainfall.setType("column");
		rainfall.setYAxis(1);
		rainfall.setData(inventario);
		rainfall.getPlotOptions().getTooltip().setValueSuffix(" cajas");
		chart.addSeries(rainfall);

		Series pressure = new Series("Ventas");
		pressure.setName("Ventas");
		pressure.setType("spline");
		pressure.setYAxis(2);
		pressure.setData(ventas);
		pressure.getMarker().setEnabled(false);
		SplinePlotOptions plotOptions2 = new SplinePlotOptions();
		plotOptions2.setDashStyle("shortdot");
		plotOptions2.getTooltip().setValueSuffix(" cajas");
		pressure.setPlotOptions(plotOptions2);
		chart.addSeries(pressure);

		Series temperature = new Series("Ventas Dusa");
		temperature.setName("Ventas Dusa");
		temperature.setType("spline");
		temperature.setData(ventasDusa);
		temperature.getPlotOptions().getTooltip().setValueSuffix(" cajas");
		chart.addSeries(temperature);

		MaestroAliado aliadoBuscar = servicioAliado.buscar(aliado2);
		String nombreAliado = "Todos";
		String codigoAliado = "Todos";
		if (aliadoBuscar != null) {
			nombreAliado = aliadoBuscar.getNombre();
			codigoAliado = aliadoBuscar.getCodigoAliado();
		}
		chart.setTitle("Comparacion entre Ventas/Compras vs Inventario desde "
				+ formatoCorrecto.format(fechaDesde2) + " hasta  "
				+ formatoCorrecto.format(fechaHasta2));
		chart.setSubtitle("Aliado: " + nombreAliado + " (" + codigoAliado + ")");
	}

}
