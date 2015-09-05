package servicio.maestros;

import java.util.Date;
import java.util.List;

import interfacedao.maestros.IVentaDusaDAO;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroProducto;
import modelo.maestros.Venta;
import modelo.maestros.VentaDusa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SVentaDusa")
public class SVentaDusa {

	@Autowired
	private IVentaDusaDAO vendaDusaDAO;

	public void guardarVarios(List<VentaDusa> ventas) {
		vendaDusaDAO.save(ventas);
	}

	public Double sumarPorAliadoEntreFechasYMarcasOrdenadoPorProducto(
			String aliado2, Date fechaInicio, Date fechaFin, List<String> ids) {
		return vendaDusaDAO
				.sumByMaestroAliadoCodigoAliadoAndMaestroProductoMaestroMarcaMarcaDusaInAndFechaFacturaBetween(
						aliado2, ids, fechaInicio, fechaFin);
	}

	public List<VentaDusa> buscarPorAliadoProductoFecha(MaestroAliado aliado,
			MaestroProducto producto, Date fechaFactura) {
		List<VentaDusa> lista = vendaDusaDAO
				.findByMaestroAliadoAndMaestroProductoAndFecha(aliado,
						producto, fechaFactura);
		return lista;
	}

	public void eliminar(List<VentaDusa> ventasRepetidas) {
		vendaDusaDAO.delete(ventasRepetidas);
	}

	public List<VentaDusa> buscarPorAliadoEntreFechasRegistro(String idAliado,
			Date desde, Date hasta) {
		return vendaDusaDAO
				.findByMaestroAliadoCodigoAliadoAndFechaAuditoriaBetween(
						idAliado, desde, hasta);
	}
}
