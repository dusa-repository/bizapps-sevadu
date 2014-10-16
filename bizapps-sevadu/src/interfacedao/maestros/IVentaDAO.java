package interfacedao.maestros;

import modelo.maestros.Venta;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IVentaDAO extends JpaRepository<Venta, Integer> {

}
