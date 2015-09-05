package interfacedao.maestros;

import java.util.Date;
import java.util.List;

import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroProducto;
import modelo.maestros.VentaDusa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IVentaDusaDAO extends JpaRepository<VentaDusa, Integer> {

	@Query("select coalesce(sum(cantidad), '0') from VentaDusa v where v.maestroAliado.codigoAliado like ?1 "
			+ "and v.maestroProducto.maestroMarca.marcaDusa in ?2 and v.fecha between ?3 and ?4")
	Double sumByMaestroAliadoCodigoAliadoAndMaestroProductoMaestroMarcaMarcaDusaInAndFechaFacturaBetween(
			String aliado2, List<String> ids, Date fechaInicio, Date fechaFin);

	List<VentaDusa> findByMaestroAliadoAndMaestroProductoAndFecha(
			MaestroAliado aliado, MaestroProducto producto, Date fechaFactura);

	List<VentaDusa> findByMaestroAliadoCodigoAliadoAndFechaAuditoriaBetween(
			String idAliado, Date desde, Date hasta);

}
