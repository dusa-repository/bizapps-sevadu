package servicio.maestros;

import interfacedao.maestros.IVentaDusaDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SVentaDusa")
public class SVentaDusa {

	@Autowired
	private IVentaDusaDAO vendaDusaDAO;
}
