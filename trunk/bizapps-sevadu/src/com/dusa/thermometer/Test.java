package com.dusa.thermometer;

import com.dusa.thermometer.service.to.ActivationTo;
import com.dusa.thermometer.service.to.HeaderTo;
import com.dusa.thermometer.service.transformation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import servicio.maestros.SCliente;
import servicio.maestros.SMaestroMarca;
import servicio.maestros.SMarcaActivadaVendedor;
import servicio.maestros.SVenta;
import modelo.maestros.Cliente;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroMarca;
import modelo.maestros.MarcaActivadaVendedor;
import modelo.maestros.Venta;
import modelo.pk.MarcaActivadaPK;

public class Test {

	private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
			"/META-INF/ConfiguracionAplicacion.xml");

	public static SMaestroMarca getServicioMarca() {
		return applicationContext.getBean(SMaestroMarca.class);
	}

	public static SCliente getServicioCliente() {
		return applicationContext.getBean(SCliente.class);
	}

	public static SMarcaActivadaVendedor getServicioMarcaActivada() {
		return applicationContext.getBean(SMarcaActivadaVendedor.class);
	}

	public static SVenta getServicioVenta() {
		return applicationContext.getBean(SVenta.class);
	}

	public static List<HeaderTo> getTestHeaders() {
		List<HeaderTo> labels = new ArrayList<HeaderTo>();
		labels.add(new HeaderTo("N. de Cliente", true));
		labels.add(new HeaderTo("Cod. Cliente", true));
		labels.add(new HeaderTo("Nombre del Proveedor (Raz&oacute;n Social)",
				false, "min-width: 300px;"));

		List<MaestroMarca> marcas = getServicioMarca()
				.buscarActivasTermometro();
		for (Iterator<MaestroMarca> iterator = marcas.iterator(); iterator
				.hasNext();) {
			MaestroMarca maestroMarca = (MaestroMarca) iterator.next();
			labels.add(new HeaderTo(maestroMarca.getDescripcion(), true));
		}

		// Totals
		labels.add(new HeaderTo("Marcas Activadas Mes Planficadas", true));
		labels.add(new HeaderTo("Marcas Activadas Mes NO Planificadas", true));
		labels.add(new HeaderTo("Obj Marcas Planificadas", true));
		labels.add(new HeaderTo("Obj. Activaci�n Planificado", true));
		labels.add(new HeaderTo("Activaci�n Mes", true));

		return labels;
	}

	public static List<ThermometerData> getTestData(MaestroAliado aliado,
			Date desde, Date hasta) {
		List<ThermometerData> thermometerDatas = new ArrayList<ThermometerData>();

		List<MaestroMarca> marcas = getServicioMarca()
				.buscarActivasTermometro();
		List<Cliente> clientes = getServicioCliente().buscarPorAliado(aliado);

		if (!clientes.isEmpty()) {
			String supervisor = clientes.get(0).getSupervisor();
			String vendedor = clientes.get(0).getVendedor();
			String zona = clientes.get(0).getZona();
			SupervisorData supervisorData = new SupervisorData(1, clientes.get(
					0).getSupervisor());
			SellerData sellerData = new SellerData(1, clientes.get(0)
					.getVendedor());
			ZoneData zoneData = new ZoneData(1, clientes.get(0).getZona());
			for (int i = 0; i < clientes.size(); i++) {
				if (supervisor.equals(clientes.get(i).getSupervisor())) {

					if (vendedor.equals(clientes.get(i).getVendedor())) {

						if (zona.equals(clientes.get(i).getZona())) {

							List<ActivationTo> activations = new ArrayList<ActivationTo>();
							MarcaActivadaPK clave = new MarcaActivadaPK();
							Cliente cliente = getServicioCliente()
									.buscarPorCodigoYAliado(
											clientes.get(i).getCodigoCliente(),
											aliado);
							clave.setCliente(cliente);
							clave.setMaestroAliado(aliado);
							if (cliente != null) {
								MarcaActivadaVendedor marcaActivada = getServicioMarcaActivada()
										.buscar(clave);
								for (int j = 0; j < marcas.size(); j++) {
									boolean primero = false;
									boolean segundo = false;
									List<Venta> ventas = getServicioVenta()
											.buscarPorAliadoYVendedorYClienteYMarcaYZona(
													aliado,
													clientes.get(i)
															.getVendedor(),
													cliente, marcas.get(j).getMarcaDusa(),
													desde, hasta,
													clientes.get(i).getZona());
									if (!ventas.isEmpty()) {
										if (obtenerGet(marcaActivada, j) != null) {
											if (obtenerGet(marcaActivada, j) == 1) {
												primero = true;
												segundo = true;
											} else {
												primero = true;
											}
										}
									}
									ActivationTo activacion = new ActivationTo(
											primero, segundo);
									activations.add(activacion);
								}
							} else {
								for (int j = 0; j < marcas.size(); j++) {
									ActivationTo activacion = new ActivationTo(
											false, false);
									activations.add(activacion);
								}
							}
							ClientData clientData = new ClientData(clientes
									.get(i).getNombre(), clientes.get(i)
									.getCodigoCliente(), activations, 1);
							zoneData.addChild(clientData);
						} else {
							sellerData.addChild(zoneData);
							zoneData = new ZoneData(i, clientes.get(i)
									.getZona());
							zona = clientes.get(i).getZona();
							i--;
						}
					} else {
						sellerData.addChild(zoneData);
						zoneData = new ZoneData(i, clientes.get(i).getZona());
						zona = clientes.get(i).getZona();
						supervisorData.addChild(sellerData);
						sellerData = new SellerData(i, clientes.get(i)
								.getVendedor());
						vendedor = clientes.get(i).getVendedor();
						i--;
					}
				} else {
					sellerData.addChild(zoneData);
					zoneData = new ZoneData(i, clientes.get(i).getZona());
					zona = clientes.get(i).getZona();
					supervisorData.addChild(sellerData);
					sellerData = new SellerData(i, clientes.get(i)
							.getVendedor());
					vendedor = clientes.get(i).getVendedor();
					thermometerDatas.add(supervisorData);
					supervisorData = new SupervisorData(i, clientes.get(i)
							.getSupervisor());
					supervisor = clientes.get(i).getSupervisor();
					i--;
				}
			}
			sellerData.addChild(zoneData);
			supervisorData.addChild(sellerData);
			thermometerDatas.add(supervisorData);
		}else{
			SupervisorData supervisorData = new SupervisorData(1, "No hay informacion que mostrar");
	        thermometerDatas.add(supervisorData);
	        SellerData sellerData = new SellerData(1, "No hay informacion que mostrar");
	        supervisorData.addChild(sellerData);
	        ZoneData zoneData = new ZoneData(1, "No hay informacion que mostrar");
	        sellerData.addChild(zoneData);
	        List<ActivationTo> activations = new ArrayList<ActivationTo>();
	        for (int j = 0; j < marcas.size(); j++) {
	            activations.add(new ActivationTo(false, false));
	        } 
	        ClientData clientData = new ClientData("No hay informacion que mostrar", "0", activations, 1);
	        zoneData.addChild(clientData);
		}
		return thermometerDatas;
	}

	private static Integer obtenerGet(MarcaActivadaVendedor marcaActivada, int i) {
		Integer valor = 0;
		switch (i) {
		case 0:
			valor = marcaActivada.getMarcaA();
			break;
		case 1:
			valor = marcaActivada.getMarcaB();
			break;
		case 2:
			valor = marcaActivada.getMarcaC();
			break;
		case 3:
			valor = marcaActivada.getMarcaD();
			break;
		case 4:
			valor = marcaActivada.getMarcaE();
			break;
		case 5:
			valor = marcaActivada.getMarcaF();
			break;
		case 6:
			valor = marcaActivada.getMarcaG();
			break;
		case 7:
			valor = marcaActivada.getMarcaH();
			break;
		case 8:
			valor = marcaActivada.getMarcaI();
			break;
		case 9:
			valor = marcaActivada.getMarcaJ();
			break;
		case 10:
			valor = marcaActivada.getMarcaK();
			break;
		case 11:
			valor = marcaActivada.getMarcaL();
			break;
		case 12:
			valor = marcaActivada.getMarcaM();
			break;
		case 13:
			valor = marcaActivada.getMarcaN();
			break;
		case 14:
			valor = marcaActivada.getMarcaO();
			break;
		case 15:
			valor = marcaActivada.getMarcaP();
			break;
		case 16:
			valor = marcaActivada.getMarcaQ();
			break;
		case 17:
			valor = marcaActivada.getMarcaR();
			break;
		case 18:
			valor = marcaActivada.getMarcaS();
			break;
		case 19:
			valor = marcaActivada.getMarcaT();
			break;
		case 20:
			valor = marcaActivada.getMarcaU();
			break;
		case 21:
			valor = marcaActivada.getMarcaV();
			break;
		case 22:
			valor = marcaActivada.getMarcaW();
			break;
		case 23:
			valor = marcaActivada.getMarcaX();
			break;
		case 24:
			valor = marcaActivada.getMarcaY();
			break;
		case 25:
			valor = marcaActivada.getMarcaZ();
			break;
		case 26:
			valor = marcaActivada.getMarcaZA();
			break;
		case 27:
			valor = marcaActivada.getMarcaZB();
			break;
		case 28:
			valor = marcaActivada.getMarcaZC();
			break;
		case 29:
			valor = marcaActivada.getMarcaZD();
			break;
		case 30:
			valor = marcaActivada.getMarcaZE();
			break;
		case 31:
			valor = marcaActivada.getMarcaZF();
			break;
		case 32:
			valor = marcaActivada.getMarcaZG();
			break;
		case 33:
			valor = marcaActivada.getMarcaZH();
			break;
		case 34:
			valor = marcaActivada.getMarcaZI();
			break;
		case 35:
			valor = marcaActivada.getMarcaZJ();
			break;
		case 36:
			valor = marcaActivada.getMarcaZK();
			break;
		case 37:
			valor = marcaActivada.getMarcaZL();
			break;
		case 38:
			valor = marcaActivada.getMarcaZM();
			break;
		case 39:
			valor = marcaActivada.getMarcaZN();
			break;
		case 40:
			valor = marcaActivada.getMarcaZO();
			break;
		case 41:
			valor = marcaActivada.getMarcaZP();
			break;
		case 42:
			valor = marcaActivada.getMarcaZQ();
			break;
		case 43:
			valor = marcaActivada.getMarcaZR();
			break;
		case 44:
			valor = marcaActivada.getMarcaZS();
			break;
		case 45:
			valor = marcaActivada.getMarcaZT();
			break;
		case 46:
			valor = marcaActivada.getMarcaZU();
			break;
		case 47:
			valor = marcaActivada.getMarcaZV();
			break;
		case 48:
			valor = marcaActivada.getMarcaZW();
			break;
		case 49:
			valor = marcaActivada.getMarcaZX();
			break;
		default:
			break;
		}
		return valor;
	}
}
