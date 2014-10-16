package servicio.maestros;

import interfacedao.maestros.IVentaDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SVenta")
public class SVenta {

	@Autowired
	private IVentaDAO ventaDAO;
}
