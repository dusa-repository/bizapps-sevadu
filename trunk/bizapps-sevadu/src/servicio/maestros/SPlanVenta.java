package servicio.maestros;

import java.util.List;

import interfacedao.maestros.IPlanVentaDAO;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroProducto;
import modelo.maestros.PlanVenta;
import modelo.pk.PlanVentaPK;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SPlanVenta")
public class SPlanVenta {

	@Autowired
	private IPlanVentaDAO planVentaDAO;

	public void guardarVarios(List<PlanVenta> planesVentas) {
		planVentaDAO.save(planesVentas);
	}

	public PlanVenta buscar(PlanVentaPK pk) {
		return planVentaDAO.findOne(pk);
	}

	public List<PlanVenta> buscarPorAliados(List<MaestroAliado> eliminarLista) {
		return planVentaDAO.findByIdMaestroAliadoIn(eliminarLista);
	}

	public List<PlanVenta> buscarPorAliado(MaestroAliado aliado) {
		return planVentaDAO.findByIdMaestroAliado(aliado);
	}

	public List<PlanVenta> buscarPorProductos(
			List<MaestroProducto> eliminarLista) {
		return planVentaDAO.findByIdMaestroProductoIn(eliminarLista);
	}

	public List<PlanVenta> buscarPorProducto(String clave) {
		return planVentaDAO.findByIdMaestroProductoCodigoProductoDusa(clave);
	}

	public Double sumarPlanAliado(MaestroAliado maestroAliado,
			MaestroProducto maestroProducto, int anno, int mes) {
		return planVentaDAO
				.sumByIdMaestroAliadoAnIdMaestroProductoAndIdMesAndIdAnno(
						maestroAliado, maestroProducto.getMaestroMarca()
								.getMarcaDusa(), mes, anno);
	}
}
