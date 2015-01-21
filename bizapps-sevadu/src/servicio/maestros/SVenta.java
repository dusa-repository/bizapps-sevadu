package servicio.maestros;

import interfacedao.maestros.IVentaDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.maestros.Cliente;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroProducto;
import modelo.maestros.TipoCliente;
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

	public List<Venta> buscarPorAliadoEntreFechasYMarcasOrdenadoPorProducto(
			String aliadoObjeto, Date fechaDesde2, Date fechaHasta2,
			List<String> marcas2, boolean ordenarPorMarca) {

		List<String> ordenar = new ArrayList<String>();
		Sort o;
		if (!ordenarPorMarca)
			ordenar.add("maestroProductoCodigoProductoDusa");
		else
			ordenar.add("maestroProductoMaestroMarcaMarcaDusa");
		ordenar.add("fechaFactura");
		o = new Sort(Sort.Direction.ASC, ordenar);
		return ventaDAO
				.findByMaestroAliadoCodigoAliadoAndMaestroProductoMaestroMarcaMarcaDusaInAndFechaFacturaBetween(
						aliadoObjeto, marcas2, fechaDesde2, fechaHasta2, o);
	}

	public Double sumarPorAliadoEntreFechasYMarcasOrdenadoPorProducto(
			String aliado2, Date fechaDesde2, Date fechaHasta2, List<String> ids) {
		return ventaDAO
				.sumByMaestroAliadoCodigoAliadoAndMaestroProductoMaestroMarcaMarcaDusaInAndFechaFacturaBetween(
						aliado2, ids, fechaDesde2, fechaHasta2);
	}

	public List<Venta> buscarPorAliados(List<MaestroAliado> eliminarLista) {
		return ventaDAO.findByMaestroAliadoIn(eliminarLista);
	}

	public List<Venta> buscarPorAliado(MaestroAliado aliado) {
		return ventaDAO.findByMaestroAliado(aliado);
	}

	public List<Venta> buscarPorTiposCliente(List<TipoCliente> eliminarLista) {
		return ventaDAO.findByTipoClienteBeanIn(eliminarLista);
	}

	public List<Venta> buscarPorTipoCliente(String id) {
		return ventaDAO.findByTipoClienteBeanCodigo(id);
	}

	public List<Venta> buscarPorProductos(List<MaestroProducto> eliminarLista) {
		return ventaDAO.findByMaestroProductoIn(eliminarLista);
	}

	public List<Venta> buscarPorProducto(String clave) {
		return ventaDAO.findByMaestroProductoCodigoProductoDusa(clave);
	}

	public List<Venta> buscarPorAliadoEntreFechasYMarcasOrdenadoPorFecha(
			String aliado2, Date fechaDesde2, Date fechaHasta2,
			List<String> ids, boolean columnas) {
		List<String> ordenar = new ArrayList<String>();
		Sort o;
		if (columnas)
			ordenar.add("maestroProductoMaestroMarcaMarcaDusa");
		ordenar.add("fechaFactura");
		o = new Sort(Sort.Direction.ASC, ordenar);
		return ventaDAO
				.findByMaestroAliadoCodigoAliadoAndMaestroProductoMaestroMarcaMarcaDusaInAndFechaFacturaBetween(
						aliado2, ids, fechaDesde2, fechaHasta2, o);
	}

	public List<Venta> buscarPorAliadoEntreFechasYMarcasOrdenadoPorFecha2(
			String aliado2, Date fechaDesde2, Date fechaHasta2) {
		List<String> ordenar = new ArrayList<String>();
		Sort o;
		ordenar.add("fechaFactura");
		o = new Sort(Sort.Direction.ASC, ordenar);
		return ventaDAO.findByMaestroAliadoCodigoAliadoAndFechaFacturaBetween(
				aliado2, fechaDesde2, fechaHasta2, o);
	}

	public Integer buscarVentasDeMarcasActivas(String aliado2,
			Date fechaDesde2, Date fechaHasta2, String cliente) {
		return ventaDAO
				.countDistinctMaestroProductoMaestroMarcaCodigoDusaBetween(
						aliado2, fechaDesde2, fechaHasta2, cliente);
	}

	public List<String> buscarAliadosMasVendedores(Date fecha1, Date fecha2,
			int anno, int mes) {
		return ventaDAO.findByCodigoAliadoMostSellerBetween(fecha1, fecha2,
				anno, mes);
	}

	public List<Venta> buscarPorClientes(List<Cliente> eliminarLista) {
		return ventaDAO.findByCodigoClienteIn(eliminarLista);
	}

	public List<Venta> buscarPorCliente(Cliente cliente) {
		return ventaDAO.findByCodigoCliente(cliente);
	}
}
