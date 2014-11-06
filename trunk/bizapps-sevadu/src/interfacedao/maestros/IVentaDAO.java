package interfacedao.maestros;

import java.util.Date;

import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroProducto;
import modelo.maestros.Venta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IVentaDAO extends JpaRepository<Venta, Integer> {

	@Query("select coalesce(sum(cantidad), '0') from Venta v where v.maestroAliado = ?1 and v.zonaAliado = ?2 and v.nombreVendedor = ?3 "
			+ "and v.maestroProducto = ?4 and fechaFactura between ?5 and ?6")
	double sumByAliadoAndVendedorAndProductoAndFecha(MaestroAliado aliado,
			String zonaAliado, String vendedorAliado,
			MaestroProducto maestroProducto, Date fecha, Date fecha2);

}
