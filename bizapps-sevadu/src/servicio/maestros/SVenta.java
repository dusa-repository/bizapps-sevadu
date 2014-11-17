package servicio.maestros;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import interfacedao.maestros.IVentaDAO;
import modelo.maestros.Cliente;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroMarca;
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

	public Collection<? extends String> buscarDistinctCliente(String value) {
		return ventaDAO.findDistinctCliente(value);
	}

	public Collection<? extends String> buscarDistinctVendedor(String value) {
		return ventaDAO.findDistinctVendedor(value);
	}

	public Collection<? extends String> buscarDistinctZona(String value) {
		return ventaDAO.findDistinctZona(value);
	}

	public Integer contarPorAliadoEntreFechas(String aliado, Date desde,
			Date hasta) {
		List<Venta> lista = ventaDAO
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
}
