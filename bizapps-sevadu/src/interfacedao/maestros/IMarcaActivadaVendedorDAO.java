package interfacedao.maestros;

import java.util.List;

import modelo.maestros.MaestroAliado;
import modelo.maestros.MarcaActivadaVendedor;
import modelo.pk.MarcaActivadaPK;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IMarcaActivadaVendedorDAO extends JpaRepository<MarcaActivadaVendedor, MarcaActivadaPK> {

	List<MarcaActivadaVendedor> findByIdMaestroAliadoIn(
			List<MaestroAliado> eliminarLista);

	List<MarcaActivadaVendedor> findByIdMaestroAliado(MaestroAliado aliado);

}
