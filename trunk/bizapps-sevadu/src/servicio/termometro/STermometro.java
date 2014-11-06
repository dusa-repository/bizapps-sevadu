package servicio.termometro;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import interfacedao.maestros.IPlanVentaDAO;
import interfacedao.maestros.IVentaDAO;
import modelo.maestros.MaestroAliado;
import modelo.maestros.PlanVenta;
import modelo.termometro.TermometroCliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service("STermometro")
public class STermometro {

	@Autowired
	private IPlanVentaDAO planDAO;

	@Autowired
	private IVentaDAO ventaDAO;
	List<String> ordenar = new ArrayList<String>();
	Sort o;
	protected DateFormat formato = new SimpleDateFormat("yyyy-MM");

	public List<TermometroCliente> buscarPorAliadoEntreMeses(
			MaestroAliado aliado, int tiempo, int periodo, int anno) {
		return null;
	}

	public List<TermometroCliente> buscarPorAliadoYMes(MaestroAliado aliado,
			int tiempo, int anno) {
		List<TermometroCliente> termometro = new ArrayList<TermometroCliente>();
		ordenar = new ArrayList<String>();
		ordenar.add("idZonaAliado");
		ordenar.add("idVendedorAliado");
		ordenar.add("idMaestroProductoCodigoProductoDusa");
		o = new Sort(Sort.Direction.ASC, ordenar);
		List<PlanVenta> plan = planDAO
				.findByIdMaestroProductoMaestroMarcaFiltroTermometroAndIdMaestroAliadoAndIdAnnoAndIdMes(
						true, aliado, anno, tiempo, o);
		String fechaString = anno + "-" + tiempo;
		formato.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
		if (tiempo != 12 && tiempo != 11 && tiempo != 10)
			fechaString = anno + "-" + "0" + tiempo;
		Date fecha = new Date();
		try {
			fecha = formato.parse(fechaString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int tiempo2 = tiempo + 1;
		String fechaString2 = anno + "-" + tiempo2;
		if (tiempo2 != 13 && tiempo2 != 12 && tiempo2 != 11 && tiempo2 != 10)
			fechaString2 = anno + "-" + "0" + tiempo2;
		Date fecha2 = new Date();
		try {
			fecha2 = formato.parse(fechaString2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calendario = new GregorianCalendar();
		calendario.setTime(fecha2);
		calendario.add(Calendar.DAY_OF_YEAR, -1);
		calendario.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
		fecha2 = calendario.getTime();
		int habiles = obtenerDiasHabiles(fecha, fecha2);
		for (int i = 0; i < plan.size(); i++) {
			TermometroCliente termo = new TermometroCliente();
			PlanVenta planEspecifico = plan.get(i);
			Double sumaVentas = (Math.rint(ventaDAO
					.sumByAliadoAndVendedorAndProductoAndFecha(aliado,
							planEspecifico.getId().getZonaAliado(),
							planEspecifico.getId().getVendedorAliado(),
							planEspecifico.getId().getMaestroProducto(), fecha,
							fecha2) * 100) / 100);
			termo.setZona(planEspecifico.getId().getZonaAliado());
			termo.setVendedor(planEspecifico.getId().getVendedorAliado());
			termo.setMarca(planEspecifico.getId().getMaestroProducto()
					.getMaestroMarca().getMarcaDusa());
			termo.setMes1(planEspecifico.getCajasPlanificadas());
			termo.setCuota(planEspecifico.getCajasPlanificadas());
			termo.setVendido(sumaVentas.intValue());
			termo.setPorcentaje((planEspecifico.getCajasPlanificadas() * 100)
					/ sumaVentas.intValue());
			termo.setExcendente(sumaVentas.intValue()
					- planEspecifico.getCajasPlanificadas());
//			aqui voy

		}
		return termometro;
	}

	private int obtenerDiasHabiles(Date fecha, Date fecha2) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
		calendario.setTime(fecha2);
		calendario.add(Calendar.DAY_OF_YEAR, +1);
		fecha2 = calendario.getTime();
		calendario.setTime(fecha);
		int contador = 0;
		do {
			calendario.setTime(fecha);
			if (calendario.get(Calendar.DAY_OF_WEEK) != 1
					&& calendario.get(Calendar.DAY_OF_WEEK) != 7)
				contador++;

			calendario.add(Calendar.DAY_OF_YEAR, +1);
			fecha = calendario.getTime();
		} while (!fecha.equals(fecha2));
		return contador;
	}
}
