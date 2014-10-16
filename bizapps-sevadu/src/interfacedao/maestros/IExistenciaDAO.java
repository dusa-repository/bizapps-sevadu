package interfacedao.maestros;

import modelo.maestros.Existencia;
import modelo.pk.ExistenciaPK;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IExistenciaDAO extends JpaRepository<Existencia, ExistenciaPK> {

}
