package controlador.reporte;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroMarca;
import modelo.maestros.PlanVenta;
import modelo.maestros.Venta;
import modelo.pk.PlanVentaPK;
import modelo.termometro.TermometroCliente;

import org.zkoss.chart.*;
import org.zkoss.chart.model.*;
import org.zkoss.chart.plotOptions.DataLabels;
import org.zkoss.chart.plotOptions.LinePlotOptions;
import org.zkoss.chart.plotOptions.PieDataLabels;
import org.zkoss.chart.plotOptions.PiePlotOptions;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

import componente.Mensaje;
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
			fechaDesde = (Date) map.get("desde");
			fechaHasta = (Date) map.get("hasta");
			tipoGrafica = (String) map.get("tipo");
			marcas = (List<MaestroMarca>) map.get("lista");
			if (map.get("dona") != null) {
				dona = true;
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
		case "column":
			generarGraficaColumnas(aliado, fechaDesde, fechaHasta, ids);
			break;
		case "pie":
			if (dona)
				generarGraficaDona(aliado, fechaDesde, fechaHasta, ids);
			else
				generarGraficaPie(aliado, fechaDesde, fechaHasta, ids);
			break;
		case "gauge":
			generarGraficaAngular(aliado, fechaDesde, fechaHasta, ids);
			break;

		default:
			break;
		}
	}

	private void generarGraficaAngular(String aliado2, Date fechaDesde2,
			Date fechaHasta2, List<String> ids) {
		List<Venta> ventas = servicioVenta
				.buscarPorAliadoEntreFechasYMarcasOrdenadoPorFecha(aliado2,
						fechaDesde2, fechaHasta2, ids);
		if (!ventas.isEmpty()) {
			String marca = ventas.get(0).getMaestroProducto().getMaestroMarca()
					.getMarcaDusa();
			String fecha = formatoFecha.format(ventas.get(0).getFechaFactura());
			double vendido = 0;
			double planificado = 0;
			DateFormat formatoAnno = new SimpleDateFormat("yyyy");
			DateFormat formatoMes = new SimpleDateFormat("MM");
			int anno = Integer.parseInt(formatoAnno.format(ventas.get(0)
					.getFechaFactura()));
			int mes = Integer.parseInt(formatoMes.format(ventas.get(0)
					.getFechaFactura()));
			Integer plan2 = servicioPlan.sumarPlanAliado(ventas.get(0)
					.getMaestroAliado(), ventas.get(0).getMaestroProducto(),
					anno, mes);
			System.out.println(plan2);
			planificado = plan2;
			for (int i = 0; i < ventas.size(); i++) {
				if (formatoFecha.format(ventas.get(i).getFechaFactura())
						.equals(fecha))
					vendido = vendido + ventas.get(i).getCantidad();
				else {
					fecha = formatoFecha
							.format(ventas.get(i).getFechaFactura());
					anno = Integer.parseInt(formatoAnno.format(ventas.get(i)
							.getFechaFactura()));
					mes = Integer.parseInt(formatoMes.format(ventas.get(i)
							.getFechaFactura()));
					Integer plan = servicioPlan.sumarPlanAliado(ventas.get(i)
							.getMaestroAliado(), ventas.get(i)
							.getMaestroProducto(), anno, mes);
					System.out.println(plan);
					planificado = planificado + plan;
					i--;
				}
			}
			anno = Integer.parseInt(formatoAnno.format(ventas.get(
					ventas.size() - 1).getFechaFactura()));
			mes = Integer.parseInt(formatoMes.format(ventas.get(
					ventas.size() - 1).getFechaFactura()));
			vendido = Math.rint(vendido * 100) / 100;

			DialModel dialmodel = new DefaultDialModel();
			dialmodel.setFrameBgColor(null);
			dialmodel.setFrameBgColor1(null);
			dialmodel.setFrameBgColor2(null);
			dialmodel.setFrameFgColor(null);

			Double primero = planificado * 5 / 100;
			Double segundo = primero / 25;

			DialModelScale scale = dialmodel.newScale(0, planificado, -150,
					-300, primero.intValue(), segundo.intValue());
			scale.setText("Cajas");
			scale.setTickColor("#666666");
			System.out.println("plan" + planificado);
			System.out.println("vent" + vendido);
			double cantidad = planificado - vendido;
			if (cantidad < 0)
				cantidad = planificado;
			else
				cantidad = vendido;
			// chart.getSeries().addPoint(cantidad);
			scale.setValue(cantidad);
			Double valor = planificado / 3;
			int minimo = valor.intValue();
			scale.newRange(0, minimo, "#DF5353", 0.9, 1); // green
			scale.newRange(minimo, minimo * 2, "#DDDF0D", 0.9, 1); // yellow
			scale.newRange(minimo * 2, planificado, "#55BF3B", 0.9, 1); // red
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

			// default background
			backgrounds.add(new PaneBackground());

			PaneBackground background3 = new PaneBackground();
			background3.setBackgroundColor("#DDD");
			background3.setBorderWidth(0);
			background3.setOuterRadius("105%");
			background3.setInnerRadius("103%");
			backgrounds.add(background3);

			chart.getPane().setBackground(backgrounds);

			// the value axis
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

			System.out.println("paso");
			chart.getYAxis().getTitle().setText("Numero de Cajas");
			chart.setTitle("Vendido VS Planificado de la Marca:"
					+ servicioMarca.buscar(ids.get(0)).getDescripcion()
					+ " desde " + formatoCorrecto.format(fechaDesde2)
					+ " hasta  " + formatoCorrecto.format(fechaHasta2));

			chart.setSubtitle("Aliado: " + aliadoBuscar.getNombre() + " ("
					+ aliado2 + ")");

			System.out.println("paso");
		}

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

	private void generarGraficaPie(String aliado2, Date fechaDesde2,
			Date fechaHasta2, List<String> ids) {

		List<Venta> ventas = servicioVenta
				.buscarPorAliadoEntreFechasYMarcasOrdenadoPorProducto(aliado2,
						fechaDesde2, fechaHasta2, ids);
		chart.getYAxis().setTitle("Total porcentaje de Ventas por Marca");
		// chart.getPlotOptions().getPie().setShadow(false);
		// chart.getPlotOptions().getPie().setCenter("50%", "50%");
		// chart.getTooltip().setValueSuffix("%");
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
					// vendidoMarca = Math.rint(vendidoMarca * 10) / 10;
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
		chart.setTitle("Porcentaje de Participacion en Ventas(Marcas)"
				+ " desde " + formatoCorrecto.format(fechaDesde2) + " hasta  "
				+ formatoCorrecto.format(fechaHasta2));
		chart.setSubtitle("Aliado: " + aliadoBuscar.getNombre() + " ("
				+ aliado2 + ")");

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
						fechaDesde2, fechaHasta2, ids);
		chart.getYAxis().setTitle("Total porcentaje de Ventas por Marca");
		chart.getPlotOptions().getPie().setShadow(false);
		// chart.getPlotOptions().getPie().setCenter("50%", "50%");
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
						// modelo = new DefaultCategoryModel();
						vendidoMarca = vendidoMarca + vendidoProducto;
						Point versionPoint = new Point(producto,
								Math.rint(vendidoProducto * 100) / 100);
						versionPoint.setColor(colors.get(j));
						// if (versionValue < 1) {
						// versionPoint.getDataLabels().setEnabled(false);
						// }
						serieProducto.addPoint(versionPoint);
						producto = ventas.get(i).getMaestroProducto()
								.getCodigoProductoDusa();
						vendidoProducto = 0;
						i--;
						// j++;
					}
				} else {

					Point browserPoint = new Point(marca,
							Math.rint(vendidoMarca * 100) / 100);
					browserPoint.setColor(colors.get(j));
					// if (serieMarca < 5)
					// browserPoint.getDataLabels().setEnabled(false);
					serieMarca.addPoint(browserPoint);

					vendidoMarca = 0;
					marca = ventas.get(i).getMaestroProducto()
							.getMaestroMarca().getMarcaDusa();
					i--;
					j++;
				}
			}
			// modelo = new DefaultCategoryModel();
			Point browserPoint = new Point(marca,
					Math.rint(vendidoMarca * 100) / 100);
			browserPoint.setColor(colors.get(j));
			serieMarca.addPoint(browserPoint);
		}
		MaestroAliado aliadoBuscar = servicioAliado.buscar(aliado2);
		chart.setTitle("Volumen de Ventas por Marca y Producto" + " desde "
				+ formatoCorrecto.format(fechaDesde2) + " hasta  "
				+ formatoCorrecto.format(fechaHasta2));

		chart.setSubtitle("Aliado: " + aliadoBuscar.getNombre() + " ("
				+ aliado2 + ")");
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
						fechaDesde2, fechaHasta2, ids);
		CategoryModel modelo = new DefaultCategoryModel();
		if (!ventas.isEmpty()) {
			String marca = ventas.get(0).getMaestroProducto().getMaestroMarca()
					.getMarcaDusa();
			String fecha = formatoFecha.format(ventas.get(0).getFechaFactura());
			double vendido = 0;
			DateFormat formatoAnno = new SimpleDateFormat("yyyy");
			DateFormat formatoMes = new SimpleDateFormat("MM");
			for (int i = 0; i < ventas.size(); i++) {
				if (ventas.get(i).getMaestroProducto().getMaestroMarca()
						.getMarcaDusa().equals(marca)) {
					if (formatoFecha.format(ventas.get(i).getFechaFactura())
							.equals(fecha))
						vendido = vendido + ventas.get(i).getCantidad();
					else {
						modelo.setValue("Vendido", fecha,
								Math.rint(vendido * 100) / 100);
						fecha = formatoFecha.format(ventas.get(i)
								.getFechaFactura());
						vendido = 0;

						int anno = Integer.parseInt(formatoAnno.format(ventas
								.get(i).getFechaFactura()));
						int mes = Integer.parseInt(formatoMes.format(ventas
								.get(i).getFechaFactura()));
						PlanVentaPK pk = new PlanVentaPK();
						pk.setAnno(anno);
						pk.setMes(mes);
						pk.setVendedorAliado(ventas.get(i).getNombreVendedor());
						pk.setZonaAliado(ventas.get(i).getZonaAliado());
						pk.setMaestroAliado(ventas.get(i).getMaestroAliado());
						pk.setMaestroProducto(ventas.get(i)
								.getMaestroProducto());
						Integer plan = servicioPlan.sumarPlanAliado(
								ventas.get(i).getMaestroAliado(), ventas.get(i)
										.getMaestroProducto(), anno, mes);
						// if (plan != null) {
						modelo.setValue("Planificado", fecha, plan);
						// } else {
						// modelo.setValue("Planificado", fecha, 0);
						// }
						i--;
					}
				} else {
					marca = ventas.get(i).getMaestroProducto()
							.getMaestroMarca().getMarcaDusa();
					i--;
				}
			}
			modelo.setValue("Vendido", fecha, Math.rint(vendido * 100) / 100);
			int anno = Integer.parseInt(formatoAnno.format(ventas.get(
					ventas.size() - 1).getFechaFactura()));
			int mes = Integer.parseInt(formatoMes.format(ventas.get(
					ventas.size() - 1).getFechaFactura()));
			PlanVentaPK pk = new PlanVentaPK();
			pk.setAnno(anno);
			pk.setMes(mes);
			pk.setVendedorAliado(ventas.get(ventas.size() - 1)
					.getNombreVendedor());
			pk.setZonaAliado(ventas.get(ventas.size() - 1).getZonaAliado());
			pk.setMaestroAliado(ventas.get(ventas.size() - 1)
					.getMaestroAliado());
			pk.setMaestroProducto(ventas.get(ventas.size() - 1)
					.getMaestroProducto());
			Integer plan = servicioPlan.sumarPlanAliado(
					ventas.get(ventas.size() - 1).getMaestroAliado(), ventas
							.get(ventas.size() - 1).getMaestroProducto(), anno,
					mes);
			// if (plan != null) {
			modelo.setValue("Planificado", fecha, plan);
			// } else {
			// modelo.setValue("Planificado", fecha, 0);
			// }

		}
		MaestroAliado aliadoBuscar = servicioAliado.buscar(aliado2);
		chart.setModel(modelo);
		chart.getYAxis().getTitle().setText("Numero de Cajas");
		chart.setTitle("Vendido VS Planificado de la Marca:"
				+ servicioMarca.buscar(ids.get(0)).getDescripcion() + " desde "
				+ formatoCorrecto.format(fechaDesde2) + " hasta  "
				+ formatoCorrecto.format(fechaHasta2));

		chart.setSubtitle("Aliado: " + aliadoBuscar.getNombre() + " ("
				+ aliado2 + ")");

		chart.getXAxis().setMin(0);
		Tooltip tooltip = chart.getTooltip();
		tooltip.setHeaderFormat("<span style=\"font-size:10px\">{point.key}</span><table>");
		tooltip.setPointFormat("<tr><td style=\"color:{series.color};padding:0\">{series.name}: </td>"
				+ "<td style=\"padding:0\"><b>{point.y:.0f} Cajas</b></td></tr>");
		tooltip.setFooterFormat("</table>");
		tooltip.setShared(true);
		tooltip.setUseHTML(true);

		chart.getPlotOptions().getColumn().setPointPadding(0.2);
		chart.getPlotOptions().getColumn().setBorderWidth(0);

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
						aliadoObjeto, fechaDesde2, fechaHasta2, marcas2);
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
						// modelo = new DefaultCategoryModel();
						modelo.setValue(marca, fecha,
								Math.rint(vendido * 100) / 100);
						fecha = formatoFecha.format(ventas.get(i)
								.getFechaFactura());
						vendido = 0;
						i--;
					}
				} else {
					marca = ventas.get(i).getMaestroProducto()
							.getMaestroMarca().getMarcaDusa();
					i--;
				}
			}
			// modelo = new DefaultCategoryModel();
			modelo.setValue(marca, fecha, Math.rint(vendido * 100) / 100);
		}

		MaestroAliado aliadoBuscar = servicioAliado.buscar(aliadoObjeto);
		chart.setModel(modelo);
		chart.getYAxis().getTitle().setText("Venta de Cajas");
		chart.setTitle("Ventas Mensuales por Marca desde "
				+ formatoCorrecto.format(fechaDesde2) + " hasta  "
				+ formatoCorrecto.format(fechaHasta2));
		chart.setSubtitle("Aliado: " + aliadoBuscar.getNombre() + " ("
				+ aliadoObjeto + ")");

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

}
