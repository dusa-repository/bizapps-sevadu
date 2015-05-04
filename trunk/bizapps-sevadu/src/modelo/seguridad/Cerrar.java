package modelo.seguridad;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.bitacora.BitacoraLogin;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.zkoss.zk.ui.Executions;

import controlador.maestros.CGenerico;

public class Cerrar implements LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest arg0,
			HttpServletResponse arg1, Authentication arg2) throws IOException,
			ServletException {

		Usuario u = CGenerico.getServicioUsuario().buscarUsuarioPorNombre(
				arg2.getName());
		BitacoraLogin login = CGenerico.getServicioBitacora().buscarUltimo(u);
		login.setFechaEgreso(new Date());
		login.setHoraEgreso(new Time(new Date().getTime()));
		CGenerico.getServicioBitacora().guardar(login);
		try {
			arg1.sendRedirect(arg0.getContextPath() + "/index.zul");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			SecurityContextHolder.clearContext();
			arg0.logout();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}

}
