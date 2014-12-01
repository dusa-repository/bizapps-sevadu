package controlador.reporte;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
			vendedor = (String) map.get("vendedor");
			fechaDesde = (Date) map.get("desde");
			fechaHasta = (Date) map.get("hasta");
			map.clear();
			map = null;
		}
		if (vendedor.equals(""))
			vendedor = "%";
		List<Venta> ventasFinales = new ArrayList<Venta>();
		List<Venta> ventas = servicioVenta.buscarPorAliadoYVendedorEntreFechas(
				aliado, vendedor, fechaDesde, fechaHasta);
		List<TermometroCliente> lista = new ArrayList<TermometroCliente>();
		if (!ventas.isEmpty()) {
			ventasFinales.add(ventas.get(0));
			String producto = ventas.get(0).getMaestroProducto()
					.getCodigoProductoDusa();
			Date fechaT = ventas.get(0).getFechaFactura();
			for (int i = 0; i < ventas.size(); i++) {
				if (fechaT.equals(ventas.get(i).getFechaFactura())) {
					if (!producto.equals(ventas.get(i).getMaestroProducto()
							.getCodigoProductoDusa())) {
						ventasFinales.add(ventas.get(i));
						producto = ventas.get(i).getMaestroProducto()
								.getCodigoProductoDusa();
					}
				} else {
					fechaT = ventas.get(i).getFechaFactura();
					producto = ventas.get(i).getMaestroProducto()
							.getCodigoProductoDusa();
					i--;
				}
			}
			Double vendedido = (double) 0, porcentaje = (double) 0, vendedidoTotal = (double) 0;
			int planificado = 0, planificadoTotal = 0;
			DateFormat formatoAnno = new SimpleDateFormat("yyyy");
			DateFormat formatoMes = new SimpleDateFormat("MM");
			for (int i = 0; i < ventasFinales.size(); i++) {
				TermometroCliente objeto = new TermometroCliente();
				int anno = Integer.parseInt(formatoAnno.format(ventasFinales
						.get(i).getFechaFactura()));
				int mes = Integer.parseInt(formatoMes.format(ventasFinales.get(
						i).getFechaFactura()));
				PlanVentaPK pk = new PlanVentaPK();
				pk.setAnno(anno);
				pk.setMes(mes);
				pk.setVendedorAliado(ventasFinales.get(i).getNombreVendedor());
				pk.setZonaAliado(ventasFinales.get(i).getZonaAliado());
				pk.setMaestroAliado(ventasFinales.get(i).getMaestroAliado());
				pk.setMaestroProducto(ventasFinales.get(i).getMaestroProducto());
				PlanVenta plan = servicioPlan.buscar(pk);
				if (plan != null) {
					objeto.setMarca(String.valueOf(plan.getId().getAnno()));
					objeto.setZona(String.valueOf(plan.getId().getMes()));
					objeto.setVendedor(plan.getId().getMaestroProducto()
							.getCodigoProductoDusa());
					objeto.setCampo(plan.getId().getMaestroProducto()
							.getDescripcionProducto());
					planificado = plan.getCajasPlanificadas();
					planificadoTotal = planificadoTotal + planificado;
					objeto.setVendido(planificado);
					vendedido = servicioVenta.sumar(ventasFinales.get(i)
							.getMaestroAliado(), ventasFinales.get(i)
							.getZonaAliado(), ventasFinales.get(i)
							.getNombreVendedor(), ventasFinales.get(i)
							.getMaestroProducto(), fechaDesde, fechaHasta);
					vendedidoTotal = vendedidoTotal
							+ (Math.rint(vendedido * 100) / 100);
					objeto.setMeta(Math.rint(vendedido * 100) / 100);
					if (planificado > 0)
						objeto.setPorcentaje(Math
								.rint((vendedido * 100 / planificado) * 100) / 100);
					else
						objeto.setPorcentaje(0);
				} else {
					objeto.setMarca(String.valueOf(anno));
					objeto.setZona(String.valueOf(mes));
					objeto.setVendedor(ventasFinales.get(i).getNombreVendedor());
					objeto.setCampo(ventasFinales.get(i).getMaestroProducto().getDescripcionProducto());
					planificado = 0;
					planificadoTotal = planificadoTotal + planificado;
					objeto.setVendido(planificado);
					vendedido = servicioVenta.sumar(ventasFinales.get(i)
							.getMaestroAliado(), ventasFinales.get(i)
							.getZonaAliado(), ventasFinales.get(i)
							.getNombreVendedor(), ventasFinales.get(i)
							.getMaestroProducto(), fechaDesde, fechaHasta);
					vendedidoTotal = vendedidoTotal
							+ (Math.rint(vendedido * 100) / 100);
					objeto.setMeta(Math.rint(vendedido * 100) / 100);
					if (planificado > 0)
						objeto.setPorcentaje(Math
								.rint((vendedido * 100 / planificado) * 100) / 100);
					else
						objeto.setPorcentaje(0);
				}

				lista.add(objeto);
			}
			// TermometroCliente objeto = new TermometroCliente();
			// objeto.setMarca("");
			// objeto.setZona("");
			// objeto.setVendedor("");
			// objeto.setCampo("Total");
			// objeto.setVendido(planificadoTotal);
			// objeto.setMeta(round(vendedidoTotal, 2));
			// objeto.setPorcentaje(round(vendedidoTotal * 100 /
			// planificadoTotal,
			// 2));
			// lista.add(objeto);
			lblFoot1.setValue(String.valueOf(planificadoTotal));
			lblFoot2.setValue(String.valueOf(Math.rint(vendedidoTotal * 100) / 100));
			if (planificadoTotal > 0)
				lblFoot3.setValue(String.valueOf(Math
						.rint((vendedidoTotal * 100 / planificadoTotal) * 100) / 100));
			else
				lblFoot3.setValue("0");
		} else {
			lblFoot1.setValue("0");
			lblFoot2.setValue("0");
			lblFoot3.setValue("0");
		}
		ltbLista.setModel(new ListModelList<TermometroCliente>(lista));
	}
}
