package servicio.maestros;

import interfacedao.maestros.IMappingProductoDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SMappingProducto")
public class SMappingProducto {
	
	@Autowired
	private IMappingProductoDAO mappingDAO;
}
