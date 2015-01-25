package interfacedao.bitacora;

import modelo.bitacora.BitacoraEliminacion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IBitacoraEliminacionDAO extends JpaRepository<BitacoraEliminacion, Long> {

}
