package interfacedao.maestros;

import java.util.List;

import modelo.maestros.MaestroAliado;
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

}
