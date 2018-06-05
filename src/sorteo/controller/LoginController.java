/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import sorteo.model.dao.LoginDao;
import sorteo.model.entities.RolXUsuario;
import sorteo.model.entities.Usuario;
import sorteo.utils.Aplicacion;
import sorteo.utils.AppWindowController;
import sorteo.utils.Encriptor;
import sorteo.utils.Parametros;

/**
 * FXML Controller class
 *
 * @author Anayansy
 */
public class LoginController extends Controller {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtClave;
    @FXML
    private Button btnIniciarSesion;

    @FXML
    private void iniciarSesion(ActionEvent event) {
        login();
    }

    private void login() {
        if (txtUsuario.getText() != null && !txtUsuario.getText().isEmpty()) {
            if (txtClave.getText() == null || txtClave.getText().isEmpty()) {
                AppWindowController.getInstance().mensaje(AlertType.WARNING, "Error", "Debe indicar la contraseña de acceso.");
                txtClave.requestFocus();
                return;
            }

            LoginDao login = new LoginDao();
            Usuario usuario = login.findByUsuCodigo(txtUsuario.getText());
            if (usuario.getUsuCodigo() != null && !usuario.getUsuCodigo().isEmpty()) {
                if (usuario.getUsuContrasena().equals(Encriptor.getInstance().encriptar(txtClave.getText()))) {
                    Parametros.getInstance().setParametro("usuario", txtUsuario.getText());
                    Aplicacion.getInstance().setUsuario(usuario);
                    String roles = "";
                    String sep = "";
                    for (RolXUsuario r : Aplicacion.getInstance().getUsuario().getRolXUsuarioList()) {
                        if (roles == null || roles.isEmpty()) {
                            sep = "";
                        } else {
                            sep = ",";
                        }
                        roles = roles + sep + r.getRxuCodrol().getRolCodigo();
                    }
                    Aplicacion.getInstance().setRolesUsuario(roles);
                    AppWindowController.getInstance().initApplication();
                    if (roles.equalsIgnoreCase("ven")) {
                        AppWindowController.getInstance().abrirVentanaEnPrincipal("sor_venta", "Center");
                    } else {
                        AppWindowController.getInstance().abrirVentanaEnPrincipal("sor_main", "Center");
                    }
                } else {
                    AppWindowController.getInstance().mensaje(AlertType.ERROR, "Acceso denegado", "Contraseña incorrecta.");
                    txtClave.requestFocus();
                    return;
                }
            } else {
                AppWindowController.getInstance().mensaje(AlertType.ERROR, "Acceso denegado", "El usuario: " + txtUsuario.getText() + ", no se encuentra registrado.");
                txtUsuario.requestFocus();
                return;
            }
        } else {
            AppWindowController.getInstance().mensaje(AlertType.WARNING, "Error", "Debe indicar el código de usuario.");
            txtUsuario.requestFocus();
            return;
        }
    }

    @Override
    public void initialize() {

    }

    @FXML
    void onKeyPressLogin(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            login();
        }
    }
}
