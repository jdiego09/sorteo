/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import sorteo.model.dao.UsuarioDao;
import sorteo.model.entities.Usuario;
import sorteo.utils.Aplicacion;
import sorteo.utils.AppWindowController;
import sorteo.utils.Encriptor;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

public class CambioPinController extends Controller implements Initializable {

    Usuario usuario;

    @FXML
    private AnchorPane acpRoot;

    @FXML
    private Button jbtnSalir;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtDescripcion;

    @FXML
    private PasswordField txtClaveAct;

    @FXML
    private PasswordField txtPinAct;

    @FXML
    private PasswordField txtClaveNue;

    @FXML
    private PasswordField txtPinNue;

    @FXML
    private Button btnCambioClave;

    @FXML
    private Button btnCambioPin;
    @FXML
    private Button btnLimpiar;

    @FXML
    void cambiarClave(ActionEvent event) {
        if (!claveActualValida()) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Contraseña inválida", "La contraseña actual ingresada no coincide con la contraseña actual del usuario, no se puede cambiar la contraseña.");
            return;
        }
        Resultado<String> cambioClave = UsuarioDao.getInstance().cambiaContrasena(usuario, Encriptor.getInstance().encriptar(txtClaveNue.getText()));
        if (!cambioClave.getResultado().equals(TipoResultado.SUCCESS)) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Error al cambiar contraseña", cambioClave.getMensaje());
            return;
        } else {
            traerUsuario(Aplicacion.getInstance().getUsuario().getUsuCodigo());
            AppWindowController.getInstance().mensaje(Alert.AlertType.CONFIRMATION, "Contaseña cambiada", "La contraseña se cambió exitosamente.");
        }
    }

    @FXML
    void cambiarPin(ActionEvent event) {
        if (!pinActualValido()) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "PIN inválido", "El PIN actual ingresada no coincide con el PIN actual del usuario, no se puede cambiar el PIN.");
            return;
        }
        Resultado<String> cambioPin = UsuarioDao.getInstance().cambiaPIN(usuario, Encriptor.getInstance().encriptar(txtPinNue.getText()));
        if (!cambioPin.getResultado().equals(TipoResultado.SUCCESS)) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Error al cambiar PIN", cambioPin.getMensaje());
            return;
        } else {
            traerUsuario(Aplicacion.getInstance().getUsuario().getUsuCodigo());
            AppWindowController.getInstance().mensaje(Alert.AlertType.CONFIRMATION, "PIN cambiado", "El PIN se cambió exitosamente.");
        }
    }

    @FXML
    void codigoUsuarioOnEnterKey(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.TAB)) {
            if (this.usuario.getUsuCodigo() != null) {
                traerUsuario(this.usuario.getUsuCodigo());
            }
        }
    }

    @FXML
    void regresar(ActionEvent event) {
        clearForm();
        AppWindowController.getInstance().goHome();
    }

    @FXML
    void limpiar(ActionEvent event) {
        clearForm();
    }

    @Override
    public void initialize() {
        init();
        if (Aplicacion.getInstance().getRolesUsuario().equalsIgnoreCase("ven")) {
            unbindUsuario();
            this.usuario = Aplicacion.getInstance().getUsuario();
            txtCodigo.setEditable(false);
            bindUsuario();
        }
        txtPinAct.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
               String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtPinAct.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (txtPinAct.getText().length() > 4) {
                    String s = txtPinAct.getText().substring(0, 4);
                    txtPinAct.setText(s);
                }
            }
        });
        txtPinNue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
               String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtPinNue.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (txtPinNue.getText().length() > 4) {
                    String s = txtPinNue.getText().substring(0, 4);
                    txtPinNue.setText(s);
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

    private void init() {
        txtCodigo.setEditable(true);
        if (this.usuario != null) {
            unbindUsuario();
        }
        nuevoUsuario();
        bindUsuario();
    }

    private void nuevoUsuario() {
        this.usuario = new Usuario();
    }

    private void bindUsuario() {
        txtCodigo.textProperty().bindBidirectional(this.usuario.getUsuCodigoProperty());
        txtDescripcion.textProperty().bindBidirectional(this.usuario.getUsuDescripcionProperty());
    }

    private void unbindUsuario() {
        txtCodigo.textProperty().unbindBidirectional(this.usuario.getUsuCodigoProperty());
        txtDescripcion.textProperty().unbindBidirectional(this.usuario.getUsuDescripcionProperty());
    }

    private void traerUsuario(String codigo) {
        unbindUsuario();
        Resultado<Usuario> user = UsuarioDao.getInstance().getUsuarioByCodigo(codigo);
        if (user.getResultado().equals(TipoResultado.ERROR)) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Buscar usuario", user.getMensaje());
            return;
        }
        if (user.get() != null) {
            if (user.get().getUsuCodigo() != null) {
                this.usuario = user.get();
                bindUsuario();
            }
        }
    }

    private void clearForm() {
        unbindUsuario();
        nuevoUsuario();
        bindUsuario();
    }

    private boolean claveActualValida() {
        return usuario.getUsuContrasena().equals(Encriptor.getInstance().encriptar(txtClaveAct.getText()));

    }

    private boolean pinActualValido() {
        return (usuario.getUsuPin() == null || usuario.getUsuPin().toString().isEmpty() || usuario.getUsuPin().equals(Encriptor.getInstance().encriptar(txtPinAct.getText())));

    }

}
