package servicio.maestros;

import java.util.List;

import interfacedao.maestros.IVentaDAO;

import modelo.maestros.Venta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SVenta")
public class SVenta {

	@Autowired
	private IVentaDAO ventaDAO;

	public void guardarVarios(List<Venta> ventas) {
		ventaDAO.save(ventas);
	}
}
