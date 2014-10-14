package controlador.seguridad;

import java.io.IOException;

import modelo.seguridad.Usuario;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componente.Botonera;
import componente.Mensaje;
import componente.Validador;

import controlador.maestros.CGenerico;

public class CReinicioPassword extends CGenerico {

	@Wire
	private Textbox txtCorreoUsuario;
	@Wire
	private Textbox txtCedulaUsuario;
	@Wire
	private Label lblNombreUsuario;
	@Wire
	private Div botoneraReinicio;
	@Wire
	private Window wdwRecordar;
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private static final long serialVersionUID = 6988038390488496987L;

	@Override
	public void inicializar() throws IOException {

		Botonera botonera = new Botonera() {

			@Override
			public void salir() {
				wdwRecordar.onClose();
			}

			@Override
			public void limpiar() {
				txtCorreoUsuario.setValue("");
				txtCedulaUsuario.setValue("");
			}

			@Override
			public void guardar() {
				String password = KeyGenerators.string().generateKey();
				String correo;
				if (validar()) {
					Usuario usuario = servicioUsuario.buscarPorCedulayCorreo(
							txtCedulaUsuario.getValue(),
							txtCorreoUsuario.getValue());
					if (usuario != null) {
						correo = usuario.getEmail();
						enviarEmailNotificacion(
								correo,
								"Ha Solicitado Reiniciar su Password, sus nuevos datos para el inicio de sesion son: "
										+ " Usuario: "
										+ usuario.getLogin()
										+ "  " + " Password: " + password);
						usuario.setPassword(password);
						servicioUsuario.guardar(usuario);
						msj.mensajeInformacion(Mensaje.reinicioContrasenna);
						limpiar();
						salir();
					} else {
						msj.mensajeError(Mensaje.usuarioNoRegistrado);
					}
				}
			}

			@Override
			public void eliminar() {
			}

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub

			}

			@Override
			public void buscar() {
				// TODO Auto-generated method stub

			}

			@Override
			public void annadir() {
				// TODO Auto-generated method stub

			}

			@Override
			public void reporte() {
				// TODO Auto-generated method stub

			}

			@Override
			public void ayuda() {
				// TODO Auto-generated method stub

			}
		};
		botonera.getChildren().get(0).setVisible(false);
		botonera.getChildren().get(2).setVisible(false);
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(4).setVisible(false);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		
		Button guardar = (Button) botonera.getChildren().get(3);
		guardar.setLabel("Enviar");
		botoneraReinicio.appendChild(botonera);
	}

	protected boolean validar() {
		if (txtCedulaUsuario.getText().compareTo("") == 0
				|| txtCorreoUsuario.getText().compareTo("") == 0) {
			msj.mensajeError(Mensaje.camposVacios);
			return false;
		} else
			return true;
	}

	/*
	 * Metodo que permite validar el correo electronico
	 */
	@Listen("onChange = #txtCorreoUsuario")
	public void validarCorreo() throws IOException {
		if (Validador.validarCorreo(txtCorreoUsuario.getValue()) == false) {
			msj.mensajeAlerta(Mensaje.correoInvalido);
		}
	}

	/* Valida la cedula */
	@Listen("onChange = #txtCedulaUsuario")
	public void validarCedula() {
		if (!Validador.validarNumero(txtCedulaUsuario.getValue())) {
			msj.mensajeAlerta(Mensaje.cedulaInvalida);
		}
	}
}
