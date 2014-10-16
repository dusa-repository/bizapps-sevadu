package servicio.maestros;


import interfacedao.maestros.IExistenciaDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SExistencia")
public class SExistencia {

	@Autowired
	private IExistenciaDAO existenciaDAO;
}
