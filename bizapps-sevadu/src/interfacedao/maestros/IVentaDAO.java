package interfacedao.maestros;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import modelo.maestros.Cliente;
import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroMarca;
import modelo.maestros.MaestroProducto;
import modelo.maestros.TipoCliente;
import modelo.maestros.Venta;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IVentaDAO extends JpaRepository<Venta, Integer> {

	@Query("select coalesce(sum(cantidad), '0') from Venta v where v.maestroAliado = ?1 and v.zonaAliado = ?2 and v.nombreVendedor = ?3 "
			+ "and v.maestroProducto = ?4 and fechaFactura between ?5 and ?6")
	double sumByAliadoAndVendedorAndProductoAndFecha(MaestroAliado aliado,
			String zonaAliado, String vendedorAliado,
			MaestroProducto maestroProducto, Date fecha, Date fecha2);

	List<Venta> findByMaestroAliadoAndNombreVendedorAndCodigoClienteAndFechaFacturaBetweenAndZonaAliadoAndMaestroProductoMaestroMarcaMarcaDusa(
			MaestroAliado aliado, String vendedor, Cliente cliente, Date desde,
			Date hasta, String zona, String marca);

	@Query("select distinct(v.codigoCliente.codigoCliente) from Venta v where v.maestroAliado.codigoAliado=?1 order by v.codigoCliente.codigoCliente asc")
	List<String> findDistinctCliente(String value);

	@Query("select distinct(v.nombreVendedor) from Venta v where v.maestroAliado.codigoAliado=?1 order by v.nombreVendedor asc")
	List<String> findDistinctVendedor(String value);

	@Query("select distinct(v.zonaAliado) from Venta v where v.maestroAliado.codigoAliado=?1 order by v.zonaAliado asc")
	List<String> findDistinctZona(String value);

	@Query("select distinct(v.codigoCliente.codigoCliente) from Venta v where v.maestroAliado.codigoAliado=?1 and v.fechaFactura between ?2 and ?3")
	List<String> countDistinctByAliadoAndFechaFacturaBetween(String aliado,
			Date desde, Date hasta);

	List<Venta> findByMaestroAliadoCodigoAliadoAndFechaFacturaBetween(
			String aliado, String desde, String hasta, Sort o);

	List<Venta> findByMaestroAliadoCodigoAliadoAndNombreVendedorLikeAndFechaFacturaBetween(
			String aliado, String vendedor, Date fechaDesde, Date fechaHasta,
			Sort o);

	List<Venta> findByMaestroAliadoCodigoAliadoAndFechaFacturaBetween(
			String aliadoObjeto, Date fechaDesde2, Date fechaHasta2, Sort o);

	List<Venta> findByMaestroAliadoCodigoAliadoAndMaestroProductoMaestroMarcaMarcaDusaInAndFechaFacturaBetween(
			String aliadoObjeto, List<String> marcas2, Date fechaDesde2,
			Date fechaHasta2, Sort o);

	@Query("select coalesce(sum(cantidad), '0') from Venta v where v.maestroAliado.codigoAliado = ?1 and v.maestroProducto.maestroMarca.marcaDusa in ?2 "
			+ " and v.fechaFactura between ?3 and ?4")
	Double sumByMaestroAliadoCodigoAliadoAndMaestroProductoMaestroMarcaMarcaDusaInAndFechaFacturaBetween(
			String aliado2, List<String> ids, Date fechaDesde2, Date fechaHasta2);

	List<Venta> findByMaestroAliadoIn(List<MaestroAliado> eliminarLista);

	List<Venta> findByMaestroAliado(MaestroAliado aliado);

	List<Venta> findByTipoClienteBeanIn(List<TipoCliente> eliminarLista);

	List<Venta> findByTipoClienteBeanCodigo(String id);

	List<Venta> findByMaestroProductoIn(List<MaestroProducto> eliminarLista);

	List<Venta> findByMaestroProductoCodigoProductoDusa(String clave);

}
