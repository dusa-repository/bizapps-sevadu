package servicio.maestros;

import java.util.List;

import interfacedao.maestros.IPlanVentaDAO;

import modelo.maestros.PlanVenta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SPlanVenta")
public class SPlanVenta {

	@Autowired
	private IPlanVentaDAO planVentaDAO;

	public void guardarVarios(List<PlanVenta> planesVentas) {
		planVentaDAO.save(planesVentas);
	}
}