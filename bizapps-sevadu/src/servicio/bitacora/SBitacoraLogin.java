package servicio.bitacora;

import interfacedao.bitacora.IBitacoraLoginDAO;

import modelo.bitacora.BitacoraLogin;
import modelo.seguridad.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SBitacoraLogin")
public class SBitacoraLogin {

	@Autowired
	private IBitacoraLoginDAO bitacoraLogin;

	public void guardar(BitacoraLogin login) {
		bitacoraLogin.save(login);
	}

	public BitacoraLogin buscarUltimo(Usuario u) {
		long id = bitacoraLogin.buscarUltimoLoginDeUsuario(u);
		if(id !=0)
			return bitacoraLogin.findOne(id);
		return null;
	}

	public BitacoraLogin buscarPorId(long idSession) {
		return bitacoraLogin.findOne(idSession);
	}
}
