package interfacedao.maestros;

import modelo.maestros.MappingProducto;
import modelo.pk.MappingProductoPK;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IMappingProductoDAO extends JpaRepository<MappingProducto, MappingProductoPK> {

}
