package servicio.maestros;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import interfacedao.maestros.IVentaDAO;
import modelo.maestros.Cliente;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroMarca;
import modelo.maestros.MaestroProducto;
import modelo.maestros.Venta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service("SVenta")
public class SVenta {

	@Autowired
	private IVentaDAO ventaDAO;

	public void guardarVarios(List<Venta> ventas) {
		ventaDAO.save(ventas);
	}

	public List<Venta> buscarPorAliadoYVendedorYClienteYMarcaYZona(
			MaestroAliado aliado, String vendedor, Cliente cliente,
			String marca, Date desde, Date hasta, String zona) {
		return ventaDAO
				.findByMaestroAliadoAndNombreVendedorAndCodigoClienteAndFechaFacturaBetweenAndZonaAliadoAndMaestroProductoMaestroMarcaMarcaDusa(
						aliado, vendedor, cliente, desde, hasta, zona, marca);
	}

	public List<String> buscarDistinctCliente(String value) {
		return ventaDAO.findDistinctCliente(value);
	}

	public List<String> buscarDistinctVendedor(String value) {
		return ventaDAO.findDistinctVendedor(value);
	}

	public List<String> buscarDistinctZona(String value) {
		return ventaDAO.findDistinctZona(value);
	}

	public Integer contarPorAliadoEntreFechas(String aliado, Date desde,
			Date hasta) {
		List<String> lista = ventaDAO
				.countDistinctByAliadoAndFechaFacturaBetween(aliado, desde,
						hasta);
		if (!lista.isEmpty())
			return lista.size();
		else
			return 1;
	}

	public List<Venta> buscarPorAliadoEntreFechas(String aliado, String desde,
			String hasta) {

		List<String> ordenar = new ArrayList<String>();
		Sort o;
		ordenar.add("codigoClienteCodigoCliente");
		ordenar.add("nombreVendedor");
		o = new Sort(Sort.Direction.ASC, ordenar);
		return ventaDAO.findByMaestroAliadoCodigoAliadoAndFechaFacturaBetween(
				aliado, desde, hasta, o);
	}

	public List<Venta> buscarPorAliadoYVendedorEntreFechas(String aliado,
			String vendedor, Date fechaDesde, Date fechaHasta) {

		List<String> ordenar = new ArrayList<String>();
		Sort o;
		ordenar.add("fechaFactura");
		ordenar.add("maestroProductoCodigoProductoDusa");
		o = new Sort(Sort.Direction.ASC, ordenar);
		return ventaDAO
				.findByMaestroAliadoCodigoAliadoAndNombreVendedorLikeAndFechaFacturaBetween(
						aliado, vendedor, fechaDesde, fechaHasta, o);
	}

	public Double sumar(MaestroAliado maestroAliado, String zonaAliado,
			String nombreVendedor, MaestroProducto maestroProducto,
			Date fechaDesde, Date fechaHasta) {
		return ventaDAO.sumByAliadoAndVendedorAndProductoAndFecha(
				maestroAliado, zonaAliado, nombreVendedor, maestroProducto,
				fechaDesde, fechaHasta);
	}

	public List<Venta> buscarPorAliadoEntreFechasOrdenadoPorProducto(
			String aliadoObjeto, Date fechaDesde2, Date fechaHasta2) {
		List<String> ordenar = new ArrayList<String>();
		Sort o;
		ordenar.add("maestroProductoCodigoProductoDusa");
		ordenar.add("fechaFactura");
		o = new Sort(Sort.Direction.ASC, ordenar);
		return ventaDAO.findByMaestroAliadoCodigoAliadoAndFechaFacturaBetween(
				aliadoObjeto, fechaDesde2, fechaHasta2, o);
	}
}
