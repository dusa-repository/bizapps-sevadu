package interfacedao.maestros;

import java.util.List;

import modelo.maestros.F0004;
import modelo.pk.F0004PK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IF0004DAO extends JpaRepository<F0004, F0004PK> {

	@Query("Select f from F0004 f order by f.id.dtsy asc, f.id.dtrt asc")
	List<F0004> findAllOrderByIdDtsy();

	List<F0004> findByIdDtsy(String value);

}
