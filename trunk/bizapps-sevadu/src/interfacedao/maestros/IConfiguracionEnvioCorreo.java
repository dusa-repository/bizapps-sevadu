package interfacedao.maestros;

import java.util.List;

import modelo.maestros.ConfiguracionEnvioCorreo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IConfiguracionEnvioCorreo extends
		JpaRepository<ConfiguracionEnvioCorreo, Long> {

	@Query("select c from ConfiguracionEnvioCorreo c order by c.destinatarios asc")
	List<ConfiguracionEnvioCorreo> findAllOrderByDestinatariosAsc();

}
