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
			MaestroAliado aliado, int tiempo, int periodo, int anno, int anno2) {
		List<TermometroCliente> termometro = new ArrayList<TermometroCliente>();
		List<PlanVenta> plan = new ArrayList<PlanVenta>();
		ordenar = new ArrayList<String>();
		ordenar.add("idZonaAliado");
		ordenar.add("idVendedorAliado");
		ordenar.add("idMaestroProductoCodigoProductoDusa");
		o = new Sort(Sort.Direction.ASC, ordenar);
		if (anno2 == 0)
			plan = planDAO
					.findByIdMaestroProductoMaestroMarcaFiltroTermometroAndIdMaestroAliadoAndIdAnnoAndIdMesBetween(
							true, aliado, anno, periodo, tiempo, o);
		else {
			int limiteSup = 1;
			int limiteInf = 12;
			plan = planDAO
					.findByIdMaestroProductoMaestroMarcaFiltroTermometroAndIdMaestroAliadoAndIdAnnoAndIdMesBetweenAndIdAnnoAndIdMesBetween(
							true, aliado, anno2, tiempo, limiteInf, anno,
							limiteSup, periodo, o);
		}
		return termometro;
	}

	public List<TermometroCliente> buscarPorAliadoYMes(MaestroAliado aliado,
			int tiempo, int anno) {
		List<TermometroCliente> termometro = new ArrayList<TermometroCliente>();
		ordenar = new ArrayList<String>();
		ordenar.add("idZonaAliado");
		ordenar.add("idVendedorAliado");
		ordenar.add("idMaestroProductoMaestroMarcaMarcaDusa");
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
		Date fechaHoy = null;
		calendario = Calendar.getInstance();
		calendario.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
		fechaHoy = calendario.getTime();
		int recorridos = 0;
		int faltantes = 0;
		if (fechaHoy.after(fecha)) {
			recorridos = obtenerDiasHabiles(fecha, fechaHoy);
			if (recorridos >= habiles)
				recorridos = 0;
			else
				faltantes = habiles - recorridos;
		} else {
			faltantes = habiles;
			recorridos = 0;
		}
		if (!plan.isEmpty()) {
			String vendedor = plan.get(0).getId().getVendedorAliado();
			String vendedor2 = plan.get(0).getId().getVendedorAliado();
			String marca = plan.get(0).getId().getMaestroProducto()
					.getMaestroMarca().getMarcaDusa();
			Double acumVentas = (double) 0;
			Double acumPlanificadas = (double) 0;
			Double acumProyeccion = (double) 0;
			Double acumVentasFooter = (double) 0;
			Double acumPlanificadasFooter = (double) 0;
			Double acumProyeccionFooter = (double) 0;
			// hacer los footers
			TermometroCliente termo = new TermometroCliente();
			for (int i = 0; i < plan.size(); i++) {
				if (plan.get(i).getId().getVendedorAliado().equals(vendedor)
						&& plan.get(i).getId().getMaestroProducto()
								.getMaestroMarca().getMarcaDusa().equals(marca)) {
					PlanVenta planEspecifico = plan.get(i);
					Double sumaVentas = (Math
							.rint(ventaDAO
									.sumByAliadoAndVendedorAndProductoAndFecha(
											aliado, planEspecifico.getId()
													.getZonaAliado(),
											planEspecifico.getId()
													.getVendedorAliado(),
											planEspecifico.getId()
													.getMaestroProducto(),
											fecha, fecha2) * 1) / 1);
					acumVentas = acumVentas + sumaVentas;
					acumPlanificadas = acumPlanificadas
							+ planEspecifico.getCajasPlanificadas();
					termo.setZona(planEspecifico.getId().getZonaAliado());
					termo.setVendedor(planEspecifico.getId()
							.getVendedorAliado());
					termo.setMarca(planEspecifico.getId().getMaestroProducto()
							.getMaestroMarca().getDescripcion());
					termo.setMes1(acumPlanificadas.intValue());
					termo.setCuota(acumPlanificadas.intValue());
					termo.setVendido(acumVentas.intValue());
					if (acumPlanificadas.intValue() > 0)
						termo.setPorcentaje((acumVentas.intValue() * 100)
								/ acumPlanificadas.intValue());
					termo.setExcendente(acumVentas.intValue()
							- acumPlanificadas.intValue());
					termo.setSugerido(acumPlanificadas.intValue() / habiles);
					if (faltantes > 0)
						termo.setMeta((acumVentas.intValue() - acumPlanificadas
								.intValue()) / faltantes);
					else
						termo.setMeta(0);
					if (recorridos > 0)
						acumProyeccion = acumProyeccion
								+ (double) ((acumVentas.intValue() / recorridos) * habiles);
					termo.setProyeccion(acumProyeccion.intValue());
				} else {
					termometro.add(termo);
					termo = new TermometroCliente();
					vendedor = plan.get(i).getId().getVendedorAliado();
					marca = plan.get(i).getId().getMaestroProducto()
							.getMaestroMarca().getMarcaDusa();
					acumPlanificadasFooter = acumPlanificadasFooter
							+ acumPlanificadas;
					acumProyeccionFooter = acumProyeccionFooter
							+ acumProyeccion;
					acumVentasFooter = acumVentasFooter + acumVentas;
					acumVentas = (double) 0;
					acumPlanificadas = (double) 0;
					acumProyeccion = (double) 0;
					i--;
				}
				if (!plan.get(i).getId().getVendedorAliado().equals(vendedor2)) {
					TermometroCliente footer = new TermometroCliente();
					footer.setMarca(vendedor2);
					footer.setMes1(acumPlanificadasFooter.intValue());
					footer.setCuota(acumPlanificadasFooter.intValue());
					footer.setVendido(acumVentasFooter.intValue());
					if (acumPlanificadasFooter.intValue() > 0)
						footer.setPorcentaje((acumVentasFooter.intValue() * 100)
								/ acumPlanificadasFooter.intValue());
					else footer.setPorcentaje(0);
					footer.setExcendente(acumVentasFooter.intValue()
							- acumPlanificadasFooter.intValue());
					footer.setSugerido(acumPlanificadasFooter.intValue() / habiles);
					if (faltantes > 0)
						footer.setMeta((acumVentasFooter.intValue() - acumPlanificadasFooter
								.intValue()) / faltantes);
					else
						footer.setMeta(0);
					if (recorridos > 0)
						acumProyeccionFooter = (double) ((acumVentasFooter.intValue() / recorridos) * habiles);
					footer.setProyeccion(acumProyeccionFooter.intValue());
					termometro.add(footer);
					acumVentasFooter = (double) 0;
					acumPlanificadasFooter = (double) 0;
					acumProyeccionFooter = (double) 0;
					vendedor2 = plan.get(i).getId().getVendedorAliado();
				}
			}
			if (!plan.isEmpty()){
				termometro.add(termo);
				acumPlanificadasFooter = acumPlanificadasFooter
						+ acumPlanificadas;
				acumProyeccionFooter = acumProyeccionFooter
						+ acumProyeccion;
				acumVentasFooter = acumVentasFooter + acumVentas;
				TermometroCliente footer = new TermometroCliente();
				footer.setZona(null);
				footer.setVendedor(null);
				footer.setMarca(plan.get(plan.size()-1).getId().getVendedorAliado());
				footer.setMes1(acumPlanificadasFooter.intValue());
				footer.setCuota(acumPlanificadasFooter.intValue());
				footer.setVendido(acumVentasFooter.intValue());
				if (acumPlanificadasFooter.intValue() > 0)
					footer.setPorcentaje((acumVentasFooter.intValue() * 100)
							/ acumPlanificadasFooter.intValue());
				else footer.setPorcentaje(0);
				footer.setExcendente(acumVentasFooter.intValue()
						- acumPlanificadasFooter.intValue());
				footer.setSugerido(acumPlanificadasFooter.intValue() / habiles);
				if (faltantes > 0)
					footer.setMeta((acumVentasFooter.intValue() - acumPlanificadasFooter
							.intValue()) / faltantes);
				else
					footer.setMeta(0);
				if (recorridos > 0)
					acumProyeccionFooter = (double) ((acumVentasFooter.intValue() / recorridos) * habiles);
				footer.setProyeccion(acumProyeccionFooter.intValue());
				termometro.add(footer);
			}
		}
		return termometro;
	}

	private int obtenerDiasHabiles(Date fecha, Date fecha2) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
		calendario.setTime(fecha2);
		calendario.set(Calendar.HOUR, 0);
		calendario.set(Calendar.HOUR_OF_DAY, 0);
		calendario.set(Calendar.SECOND, 0);
		calendario.set(Calendar.MILLISECOND, 0);
		calendario.set(Calendar.MINUTE, 0);
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
