package servicio.maestros;

import java.util.Date;
import java.util.List;

import interfacedao.maestros.IVentaDusaDAO;
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
}
