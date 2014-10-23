package interfacedao.maestros;

import modelo.maestros.MarcaActivadaVendedor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IMarcaActivadaVendedorDAO extends JpaRepository<MarcaActivadaVendedor, Long> {

}
