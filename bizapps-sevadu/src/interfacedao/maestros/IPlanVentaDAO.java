package interfacedao.maestros;

import java.util.List;

import modelo.maestros.MaestroAliado;
import modelo.maestros.PlanVenta;
import modelo.pk.PlanVentaPK;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlanVentaDAO extends JpaRepository<PlanVenta, PlanVentaPK> {

	List<PlanVenta> findByIdMaestroProductoMaestroMarcaFiltroTermometroAndIdMaestroAliadoAndIdAnnoAndIdMes(
			boolean b, MaestroAliado aliado, int anno, int tiempo, Sort o);

}
