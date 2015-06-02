package controlador.reporte;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.MaestroProducto;
import modelo.maestros.PlanVenta;
import modelo.maestros.Venta;
import modelo.pk.PlanVentaPK;
import modelo.termometro.TermometroCliente;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import controlador.maestros.CGenerico;

public class CVentaPlan extends CGenerico {

	@Wire
	private Listbox ltbLista;
	@Wire
	private Label lblFoot1;
	@Wire
	private Label lblFoot2;
	@Wire
	private Label lblFoot3;
	String aliado;
	String vendedor;
	Date fechaDesde;
	Date fechaHasta;

	@Override
	public void inicializar() throws IOException {

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("reporte");
		if (map != null) {
			aliado = (String) map.get("idAliado");
			if (aliado.equals("TODOS"))
				aliado = "%";
			vendedor = (String) map.get("vendedor");
			fechaDesde = (Date) map.get("desde");
			fechaHasta = (Date) map.get("hasta");
			map.clear();
			map = null;
		}
		if (vendedor.equals(""))
			vendedor = "%";
		Double porcentaje = (double) 0, vendedidoTotal = (double) 0;
		int planificadoTotal = 0;
		List<MaestroProducto> productos = servicioProducto
				.buscarTodosOrdenados();
		List<TermometroCliente> lista = new ArrayList<TermometroCliente>();
		for (int i = 0; i < productos.size(); i++) {
			int annoPlanDesde = Integer
					.parseInt(formatoAnno.format(fechaDesde));
			int mesPlanDesde = Integer.parseInt(formatoMes.format(fechaDesde));
			int annoPlanHasta = Integer
					.parseInt(formatoAnno.format(fechaHasta));
			int mesPlanHasta = Integer.parseInt(formatoMes.format(fechaHasta));
			do {
				if (mesPlanDesde == 13) {
					mesPlanDesde = 1;
					annoPlanDesde = annoPlanDesde + 1;
				}

				TermometroCliente objeto = new TermometroCliente();
				objeto.setMarca(String.valueOf(annoPlanDesde));
				objeto.setZona(String.valueOf(mesPlanDesde));
				objeto.setVendedor(productos.get(i).getCodigoProductoDusa());
				objeto.setCampo(productos.get(i).getDescripcionProducto());
				Date fechaInicio = null;
				try {
					fechaInicio = formatoFecha.parse("01-" + mesPlanDesde + "-"
							+ annoPlanDesde);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Date fechaFin = null;
				String ultimoDia = "";
				if (annoPlanDesde == annoPlanHasta
						&& mesPlanDesde == mesPlanHasta)
					ultimoDia = formatoDia.format(fechaHasta) + "-";
				else
					ultimoDia = lastDay(fechaInicio);

				try {
					fechaFin = formatoFecha.parse(ultimoDia + mesPlanDesde
							+ "-" + annoPlanDesde);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Double vendedido = (double) 0;
				vendedido = servicioVenta.sumarPorProductoAliadoYFechas(aliado,
						productos.get(i).getCodigoProductoDusa(), fechaInicio,
						fechaFin);
				objeto.setMeta(Math.rint(vendedido * 100) / 100);
				vendedidoTotal = vendedidoTotal
						+ (Math.rint(vendedido * 100) / 100);

				int planificado = 0;
				planificado = servicioPlan.sumarPorProductoaliadoYFechas(
						aliado, productos.get(i).getCodigoProductoDusa(),
						annoPlanDesde, mesPlanDesde);
				objeto.setVendido(planificado);
				planificadoTotal = planificadoTotal + planificado;
				if (planificado > 0)
					objeto.setPorcentaje(Math
							.rint((vendedido * 100 / planificado) * 100) / 100);
				else
					objeto.setPorcentaje(0);
				lista.add(objeto);
				mesPlanDesde = mesPlanDesde + 1;
			} while (annoPlanDesde != annoPlanHasta
					|| mesPlanDesde != mesPlanHasta + 1);
		}
		lblFoot1.setValue(String.valueOf(planificadoTotal));
		lblFoot2.setValue(String.valueOf(Math.rint(vendedidoTotal * 100) / 100));
		if (planificadoTotal > 0)
			lblFoot3.setValue(String.valueOf(Math
					.rint((vendedidoTotal * 100 / planificadoTotal) * 100) / 100));
		else
			lblFoot3.setValue("0");
		ltbLista.setModel(new ListModelList<TermometroCliente>(lista));
	}
}
