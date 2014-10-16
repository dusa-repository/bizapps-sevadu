package interfacedao.maestros;

import modelo.maestros.PlanVenta;
import modelo.pk.PlanVentaPK;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlanVentaDAO extends JpaRepository<PlanVenta, PlanVentaPK> {

}
