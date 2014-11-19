package controlador.reporte;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.MaestroAliado;
import modelo.maestros.Venta;

import org.zkoss.chart.*;
import org.zkoss.chart.model.*;
import org.zkoss.chart.plotOptions.LinePlotOptions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Wire;

import controlador.maestros.CGenerico;

public class CGraficador extends CGenerico {

	private static final long serialVersionUID = 1222931146531648336L;
	@Wire
	private Charts chart;
	private String aliado;
	private Date fechaDesde;
	private Date fechaHasta;
	private String tipoGrafica;
	protected DateFormat formatoFecha = new SimpleDateFormat("MMM");
	protected DateFormat formatoCorrecto = new SimpleDateFormat("dd-MM-yyyy");

	@Override
	public void inicializar() throws IOException {

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("grafica");
		if (map != null) {
			aliado = (String) map.get("idAliado");
			fechaDesde = (Date) map.get("desde");
			fechaHasta = (Date) map.get("hasta");
			tipoGrafica = (String) map.get("tipo");
			map.clear();
			map = null;
		}
		switch (tipoGrafica) {
		case "Lineal":
			chart.setType("line");
			generarGrafica(aliado, fechaDesde, fechaHasta);
			break;

		default:
			break;
		}
	}

	private void generarGrafica(String aliadoObjeto, Date fechaDesde2,
			Date fechaHasta2) {
		List<Venta> ventas = servicioVenta
				.buscarPorAliadoEntreFechasOrdenadoPorProducto(aliadoObjeto,
						fechaDesde2, fechaHasta2);
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

		chart.getTooltip().setEnabled(false);

		LinePlotOptions linePlotOptions = chart.getPlotData().getPlotOptions()
				.getLine();
		linePlotOptions.setEnableMouseTracking(false);
		linePlotOptions.getDataLabels().setEnabled(true);
	}

}
