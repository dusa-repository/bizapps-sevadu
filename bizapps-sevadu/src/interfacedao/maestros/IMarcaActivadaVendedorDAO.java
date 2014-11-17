package interfacedao.maestros;

import modelo.maestros.MarcaActivadaVendedor;
import modelo.pk.MarcaActivadaPK;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IMarcaActivadaVendedorDAO extends JpaRepository<MarcaActivadaVendedor, MarcaActivadaPK> {

}
