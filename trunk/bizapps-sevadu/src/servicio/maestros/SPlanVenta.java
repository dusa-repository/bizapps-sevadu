package servicio.maestros;

import interfacedao.maestros.IPlanVentaDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SPlanVenta")
public class SPlanVenta {

	@Autowired
	private IPlanVentaDAO planVentaDAO;
}
