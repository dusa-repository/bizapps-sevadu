package interfacedao.maestros;

import modelo.maestros.Configuracion;

import org.springframework.data.jpa.repository.JpaRepository;

	public interface IConfiguracionDAO extends JpaRepository<Configuracion, Integer> {

}
