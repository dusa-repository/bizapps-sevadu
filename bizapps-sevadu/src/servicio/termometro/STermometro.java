package servicio.termometro;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
	protected DateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");

	public List<TermometroCliente> buscarPorAliadoEntreMeses(
			MaestroAliado aliado, int tiempo, int periodo, int anno, int anno2,
			int tipo) {
		List<TermometroCliente> termometro = new ArrayList<TermometroCliente>();
		List<PlanVenta> plan = new ArrayList<PlanVenta>();
		ordenar = new ArrayList<String>();
		int limiteSup = 0;
		int limiteInf = 0;
		if (anno2 == 0) {
			ordenar.add("idZonaAliado");
			ordenar.add("idVendedorAliado");
			ordenar.add("idMaestroProductoMaestroMarcaMarcaDusa");
			ordenar.add("idMes");
			o = new Sort(Sort.Direction.ASC, ordenar);
			plan = planDAO
					.findByIdMaestroProductoMaestroMarcaFiltroTermometroAndIdMaestroAliadoAndIdAnnoAndIdMesBetween(
							true, aliado, anno, tiempo, periodo, o);
			// anno2 = 0;
			// limiteInf = 0;
			// limiteSup = 0;
		} else {
			ordenar.add("id.zonaAliado");
			ordenar.add("id.vendedorAliado");
			ordenar.add("id.maestroProducto.maestroMarca.marcaDusa");
			ordenar.add("id.mes");
			o = new Sort(Sort.Direction.ASC, ordenar);
			limiteSup = 1;
			limiteInf = 12;
			plan = planDAO
					.findByIdMaestroProductoMaestroMarcaFiltroTermometroAndIdMaestroAliadoAndIdAnnoAndIdMesBetweenAndIdAnnoAndIdMesBetween(
							true, aliado, anno2, tiempo, limiteInf, anno,
							limiteSup, periodo, o);
		}

		formato.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
		// mes hasta
		int mesHasta = periodo + 1;
		String fechaString2 = anno + "-" + mesHasta;
		if (mesHasta != 13 && mesHasta != 12 && mesHasta != 11
				&& mesHasta != 10)
			fechaString2 = anno + "-" + "0" + mesHasta;
		Date fechaHasta = new Date();
		try {
			fechaHasta = formato.parse(fechaString2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calendario = new GregorianCalendar();
		calendario.setTime(fechaHasta);
		calendario.add(Calendar.DAY_OF_YEAR, -1);
		calendario.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
		fechaHasta = calendario.getTime();
		// mes desde
		String fechaString = anno + "-" + tiempo;
		if (tiempo != 12 && tiempo != 11 && tiempo != 10)
			fechaString = anno + "-" + "0" + tiempo;

		if (anno2 != 0) {
			fechaString = anno2 + "-" + tiempo;
			if (tiempo != 12 && tiempo != 11 && tiempo != 10)
				fechaString = anno2 + "-" + "0" + tiempo;
		}
		Date fechaDesde = new Date();
		try {
			fechaDesde = formato.parse(fechaString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// mes enero
		Date mesEnero = null;
		// mes diciembre
		Date mesDiciembre = null;

		int habiles = 0;
		if (anno2 == 0)
			habiles = obtenerDiasHabiles(fechaDesde, fechaHasta);
		else {
			mesEnero = dameTuFecha(anno, 1);
			Calendar calendarioFinalMes = new GregorianCalendar();
			calendarioFinalMes.setTime(mesEnero);
			calendarioFinalMes.add(Calendar.DAY_OF_YEAR, -1);
			calendarioFinalMes.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
			mesDiciembre = calendarioFinalMes.getTime();
			habiles = obtenerDiasHabiles(fechaDesde, mesDiciembre);
			habiles = habiles + obtenerDiasHabiles(mesEnero, fechaHasta);
		}
		// probar los dias habiles
		Date fechaHoy = null;
		calendario = Calendar.getInstance();
		calendario.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
		fechaHoy = calendario.getTime();
		int recorridos = 0;
		int faltantes = 0;
		if (fechaHoy.after(fechaDesde) && fechaHoy.before(fechaHasta)) {
			calendario = Calendar.getInstance();
			calendario.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
			calendario.setTime(fechaHoy);
			calendario.set(Calendar.HOUR, 0);
			calendario.set(Calendar.HOUR_OF_DAY, 0);
			calendario.set(Calendar.SECOND, 0);
			calendario.set(Calendar.MILLISECOND, 0);
			calendario.set(Calendar.MINUTE, 0);
			fechaHoy = calendario.getTime();
			faltantes = obtenerDiasHabiles(fechaHoy, fechaHasta);
			recorridos = habiles - faltantes;
		} else {
			if (fechaHoy.before(fechaDesde)) {
				faltantes = habiles;
				recorridos = 0;
			} else {
				if (fechaHoy.after(fechaHasta)) {
					faltantes = 0;
					recorridos = 0;
				}
			}
		}
		if (!plan.isEmpty()) {
			String vendedor = plan.get(0).getId().getVendedorAliado();
			String vendedor2 = plan.get(0).getId().getVendedorAliado();
			String marca = plan.get(0).getId().getMaestroProducto()
					.getMaestroMarca().getMarcaDusa();
			Double acumVentas = (double) 0;
			Double mes1 = (double) 0;
			Double mes2 = (double) 0;
			Double mes3 = (double) 0;
			Double mes4 = (double) 0;
			Double mes5 = (double) 0;
			Double mes6 = (double) 0;
			Double mes7 = (double) 0;
			Double mes8 = (double) 0;
			Double mes9 = (double) 0;
			Double mes10 = (double) 0;
			Double mes11 = (double) 0;
			Double mes12 = (double) 0;
			Double acumPlanificadas = (double) 0;
			Double acumProyeccion = (double) 0;
			Double acumVentasFooter = (double) 0;
			Double acumPlanificadasFooter = (double) 0;
			Double acumProyeccionFooter = (double) 0;
			// hacer los footers
			String stringDesde = formatoFecha.format(fechaDesde);
			String stringHasta = formatoFecha.format(fechaHasta);
			String stringHoy = formatoFecha.format(new Date());
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
											fechaDesde, fechaHasta) * 1) / 1);

					acumVentas = acumVentas + sumaVentas;
					acumPlanificadas = acumPlanificadas
							+ planEspecifico.getCajasPlanificadas();
					termo.setFaltantes(faltantes);
					termo.setRecorridos(recorridos);
					termo.setHabiles(habiles);
					termo.setCampo(stringHoy + "/" + "Rango de Fechas: "
							+ stringDesde + " al " + stringHasta);
					termo.setZona(planEspecifico.getId().getZonaAliado());
					termo.setVendedor(planEspecifico.getId()
							.getVendedorAliado());
					termo.setMarca(planEspecifico.getId().getMaestroProducto()
							.getMaestroMarca().getDescripcion());
					switch (planEspecifico.getId().getMes()) {
					case 1:
						mes1 = mes1 + planEspecifico.getCajasPlanificadas();
						break;
					case 2:
						mes2 = mes2 + planEspecifico.getCajasPlanificadas();
						break;
					case 3:
						mes3 = mes3 + planEspecifico.getCajasPlanificadas();
						break;
					case 4:
						mes4 = mes4 + planEspecifico.getCajasPlanificadas();
						break;
					case 5:
						mes5 = mes5 + planEspecifico.getCajasPlanificadas();
						break;
					case 6:
						mes6 = mes6 + planEspecifico.getCajasPlanificadas();
						break;
					case 7:
						mes7 = mes7 + planEspecifico.getCajasPlanificadas();
						break;
					case 8:
						mes8 = mes8 + planEspecifico.getCajasPlanificadas();
						break;
					case 9:
						mes9 = mes9 + planEspecifico.getCajasPlanificadas();
						break;
					case 10:
						mes10 = mes10 + planEspecifico.getCajasPlanificadas();
						break;
					case 11:
						mes11 = mes11 + planEspecifico.getCajasPlanificadas();
						break;
					case 12:
						mes12 = mes12 + planEspecifico.getCajasPlanificadas();
						break;
					}
					// termo.setMes1(acumPlanificadas.intValue());
					termo.setCuota(acumPlanificadas.intValue());
					termo.setVendido(acumVentas.intValue());
					if (acumPlanificadas.intValue() > 0)
						termo.setPorcentaje(round((acumVentas * 100)
								/ acumPlanificadas, 2));
					termo.setExcendente(acumVentas.intValue()
							- acumPlanificadas.intValue());
					termo.setSugerido(round(acumPlanificadas / habiles, 2));
					if (faltantes > 0)
						termo.setMeta(round((acumVentas - acumPlanificadas)
								/ faltantes, 2));
					else
						termo.setMeta(0);
					if (recorridos > 0)
						acumProyeccion = acumProyeccion
								+ (double) ((acumVentas.intValue() / recorridos) * habiles);
					termo.setProyeccion(acumProyeccion.intValue());
				} else {
					termo.setMes1(mes1.intValue());
					termo.setMes2(mes2.intValue());
					termo.setMes3(mes3.intValue());
					termo.setMes4(mes4.intValue());
					termo.setMes5(mes5.intValue());
					termo.setMes6(mes6.intValue());
					termo.setMes7(mes7.intValue());
					termo.setMes8(mes8.intValue());
					termo.setMes9(mes9.intValue());
					termo.setMes10(mes10.intValue());
					termo.setMes11(mes11.intValue());
					termo.setMes12(mes12.intValue());
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
					mes1 = (double) 0;
					mes2 = (double) 0;
					mes3 = (double) 0;
					mes4 = (double) 0;
					mes5 = (double) 0;
					mes6 = (double) 0;
					mes7 = (double) 0;
					mes8 = (double) 0;
					mes9 = (double) 0;
					mes10 = (double) 0;
					mes11 = (double) 0;
					mes12 = (double) 0;
					i--;
				}
				if (!plan.get(i).getId().getVendedorAliado().equals(vendedor2)) {
					TermometroCliente footer = new TermometroCliente();
					footer.setMarca(vendedor2);
					// check
					// footer.setMes1(acumPlanificadasFooter.intValue());
					footer.setCuota(acumPlanificadasFooter.intValue());
					footer.setVendido(acumVentasFooter.intValue());
					if (acumPlanificadasFooter.intValue() > 0)
						footer.setPorcentaje(round((acumVentasFooter * 100)
								/ acumPlanificadasFooter, 2));
					else
						footer.setPorcentaje(0);
					footer.setExcendente(acumVentasFooter.intValue()
							- acumPlanificadasFooter.intValue());
					footer.setSugerido(round(acumPlanificadasFooter / habiles,
							2));
					if (faltantes > 0)
						footer.setMeta(round(
								(acumVentasFooter - acumPlanificadasFooter)
										/ faltantes, 2));
					else
						footer.setMeta(0);
					if (recorridos > 0)
						acumProyeccionFooter = (double) ((acumVentasFooter
								.intValue() / recorridos) * habiles);
					footer.setProyeccion(acumProyeccionFooter.intValue());
					termometro.add(footer);
					acumVentasFooter = (double) 0;
					acumPlanificadasFooter = (double) 0;
					acumProyeccionFooter = (double) 0;
					vendedor2 = plan.get(i).getId().getVendedorAliado();
				}
			}
			if (!plan.isEmpty()) {
				termometro.add(termo);
				acumPlanificadasFooter = acumPlanificadasFooter
						+ acumPlanificadas;
				acumProyeccionFooter = acumProyeccionFooter + acumProyeccion;
				acumVentasFooter = acumVentasFooter + acumVentas;
				TermometroCliente footer = new TermometroCliente();
				footer.setZona(null);
				footer.setVendedor(null);
				footer.setMarca(plan.get(plan.size() - 1).getId()
						.getVendedorAliado());
				footer.setMes1(acumPlanificadasFooter.intValue());
				footer.setCuota(acumPlanificadasFooter.intValue());
				footer.setVendido(acumVentasFooter.intValue());
				if (acumPlanificadasFooter.intValue() > 0)
					footer.setPorcentaje(round((acumVentasFooter * 100)
							/ acumPlanificadasFooter, 2));
				else
					footer.setPorcentaje(0);
				footer.setExcendente(acumVentasFooter.intValue()
						- acumPlanificadasFooter.intValue());
				footer.setSugerido(round(acumPlanificadasFooter / habiles, 2));
				if (faltantes > 0)
					footer.setMeta(round(
							(acumVentasFooter - acumPlanificadasFooter)
									/ faltantes, 2));
				else
					footer.setMeta(0);
				if (recorridos > 0)
					acumProyeccionFooter = (double) ((acumVentasFooter
							.intValue() / recorridos) * habiles);
				footer.setProyeccion(acumProyeccionFooter.intValue());
				termometro.add(footer);
			}
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

			String stringDesde = formatoFecha.format(fecha);
			String stringHasta = formatoFecha.format(fecha2);
			String stringHoy = formatoFecha.format(new Date());
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
					termo.setFaltantes(faltantes);
					termo.setRecorridos(recorridos);
					termo.setHabiles(habiles);
					termo.setCampo(stringHoy + "/" + "Rango de Fechas: "
							+ stringDesde + " al " + stringHasta);
					termo.setZona(planEspecifico.getId().getZonaAliado());
					termo.setVendedor(planEspecifico.getId()
							.getVendedorAliado());
					termo.setMarca(planEspecifico.getId().getMaestroProducto()
							.getMaestroMarca().getDescripcion());
					termo.setMes1(acumPlanificadas.intValue());
					termo.setCuota(acumPlanificadas.intValue());
					termo.setVendido(acumVentas.intValue());
					if (acumPlanificadas.intValue() > 0)
						termo.setPorcentaje(round((acumVentas * 100)
								/ acumPlanificadas, 2));
					termo.setExcendente(acumVentas.intValue()
							- acumPlanificadas.intValue());
					termo.setSugerido(round(acumPlanificadas / habiles, 2));
					if (faltantes > 0)
						termo.setMeta(round((acumVentas - acumPlanificadas)
								/ faltantes, 2));
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
						footer.setPorcentaje(round((acumVentasFooter * 100)
								/ acumPlanificadasFooter, 2));
					else
						footer.setPorcentaje(0);
					footer.setExcendente(acumVentasFooter.intValue()
							- acumPlanificadasFooter.intValue());
					footer.setSugerido(round(acumPlanificadasFooter / habiles,
							2));
					if (faltantes > 0)
						footer.setMeta(round(
								(acumVentasFooter - acumPlanificadasFooter)
										/ faltantes, 2));
					else
						footer.setMeta(0);
					if (recorridos > 0)
						acumProyeccionFooter = (double) ((acumVentasFooter
								.intValue() / recorridos) * habiles);
					footer.setProyeccion(acumProyeccionFooter.intValue());
					termometro.add(footer);
					acumVentasFooter = (double) 0;
					acumPlanificadasFooter = (double) 0;
					acumProyeccionFooter = (double) 0;
					vendedor2 = plan.get(i).getId().getVendedorAliado();
				}
			}
			if (!plan.isEmpty()) {
				termometro.add(termo);
				acumPlanificadasFooter = acumPlanificadasFooter
						+ acumPlanificadas;
				acumProyeccionFooter = acumProyeccionFooter + acumProyeccion;
				acumVentasFooter = acumVentasFooter + acumVentas;
				TermometroCliente footer = new TermometroCliente();
				footer.setZona(null);
				footer.setVendedor(null);
				footer.setMarca(plan.get(plan.size() - 1).getId()
						.getVendedorAliado());
				footer.setMes1(acumPlanificadasFooter.intValue());
				footer.setCuota(acumPlanificadasFooter.intValue());
				footer.setVendido(acumVentasFooter.intValue());
				if (acumPlanificadasFooter.intValue() > 0)
					footer.setPorcentaje(round((acumVentasFooter * 100)
							/ acumPlanificadasFooter, 2));
				else
					footer.setPorcentaje(0);
				footer.setExcendente(acumVentasFooter.intValue()
						- acumPlanificadasFooter.intValue());
				footer.setSugerido(round(acumPlanificadasFooter / habiles, 2));
				if (faltantes > 0)
					footer.setMeta(round(
							(acumVentasFooter - acumPlanificadasFooter)
									/ faltantes, 2));
				else
					footer.setMeta(0);
				if (recorridos > 0)
					acumProyeccionFooter = (double) ((acumVentasFooter
							.intValue() / recorridos) * habiles);
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

	private Date dameTuFecha(int anno, int mes) {
		String fechaString = anno + "-" + mes;
		if (mes != 12 && mes != 11 && mes != 10)
			fechaString = anno + "-" + "0" + mes;
		Date fecha = new Date();
		try {
			fecha = formato.parse(fechaString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fecha;
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}
