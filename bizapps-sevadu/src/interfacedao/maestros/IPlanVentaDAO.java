package interfacedao.maestros;

import java.util.List;

import modelo.maestros.MaestroAliado;
import modelo.maestros.MaestroProducto;
import modelo.maestros.PlanVenta;
import modelo.pk.PlanVentaPK;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IPlanVentaDAO extends JpaRepository<PlanVenta, PlanVentaPK> {

	List<PlanVenta> findByIdMaestroProductoMaestroMarcaFiltroTermometroAndIdMaestroAliadoAndIdAnnoAndIdMes(
			boolean b, MaestroAliado aliado, int anno, int tiempo, Sort o);

	List<PlanVenta> findByIdMaestroProductoMaestroMarcaFiltroTermometroAndIdMaestroAliadoAndIdAnnoAndIdMesBetween(
			boolean b, MaestroAliado aliado, int anno, int periodo, int tiempo,
			Sort o);

	@Query("select p from PlanVenta p where p.id.maestroProducto.maestroMarca.filtroTermometro = ?1 and p.id.maestroAliado = ?2 "
			+ "and ((id.anno = ?3 and id.mes between ?4 and ?5) or (id.anno = ?6 and id.mes between ?7 and ?8))")
	List<PlanVenta> findByIdMaestroProductoMaestroMarcaFiltroTermometroAndIdMaestroAliadoAndIdAnnoAndIdMesBetweenAndIdAnnoAndIdMesBetween(
			boolean b, MaestroAliado aliado, int anno2, int tiempo,
			int limiteInf, int anno, int limiteSup, int periodo, Sort o);

	List<PlanVenta> findByIdMaestroAliadoIn(List<MaestroAliado> eliminarLista);

	List<PlanVenta> findByIdMaestroAliado(MaestroAliado aliado);

	List<PlanVenta> findByIdMaestroProductoIn(
			List<MaestroProducto> eliminarLista);

	List<PlanVenta> findByIdMaestroProductoCodigoProductoDusa(String clave);

	PlanVenta findByIdMaestroAliadoAndIdMaestroProductoAndIdAnnoAndIdMes(
			MaestroAliado maestroAliado, MaestroProducto maestroProducto,
			int anno, int mes);

	@Query("select coalesce(sum(v.cajasPlanificadas), '0') from PlanVenta v where v.id.maestroAliado = ?1 " +
			"and v.id.maestroProducto.maestroMarca.marcaDusa = ?2 and v.id.anno = ?4 and v.id.mes=?3")
	Double sumByIdMaestroAliadoAnIdMaestroProductoAndIdMesAndIdAnno(
			MaestroAliado maestroAliado, String maestroProducto,
			int mes, int anno);

}
