/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import sorteo.model.dao.UsuarioDao;
import sorteo.model.entities.Usuario;
import sorteo.utils.Aplicacion;
import sorteo.utils.AppWindowController;
import sorteo.utils.Encriptor;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

public class AutorizaVentaController extends Controller implements Initializable {

    Resultado<String> autorizacion = new Resultado<>();
    @FXML
    private AnchorPane acpRoot;

    @FXML
    private PasswordField txtPin;

    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    private ComboBox<Usuario> cmbUsuario;

    @Override
    public void initialize() {
        init();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
        txtPin.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
               String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtPin.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (txtPin.getText().length() > 4) {
                    String s = txtPin.getText().substring(0, 4);
                    txtPin.setText(s);
                }
            }
        });

        btnAceptar.setOnAction((a) -> {
            if (cmbUsuario.getValue() != null) {
                if (txtPin.getText() != null && !txtPin.getText().isEmpty()) {
                    if (autorizacionValida(cmbUsuario.getValue())) {
                        autorizacion.setResultado(TipoResultado.SUCCESS);
                        closeWindow();
                    } else {
                        autorizacion.set("El PIN ingresado no corresponde con el PIN del administrador.");
                        autorizacion.setResultado(TipoResultado.ERROR);
                    }
                } else {
                    autorizacion.set("No ha indicado el PIN del administrador.");
                    autorizacion.setResultado(TipoResultado.ERROR);
                }
            } else {
                autorizacion.set("No ha indicado el usuario administrador.");
                autorizacion.setResultado(TipoResultado.ERROR);
            }
            Aplicacion.getInstance().setVentaAutorizada(autorizacion);
        });
        btnCancelar.setOnAction((a) -> {
            autorizacion.setResultado(TipoResultado.ERROR);
            Aplicacion.getInstance().setVentaAutorizada(autorizacion);
            closeWindow();
        });
    }

    private void init() {
        cargarAdministradores();
        autorizacion.set(null);
        autorizacion.setResultado(TipoResultado.ERROR);
    }

    private void cargarAdministradores() {
        cmbUsuario.getItems().clear();
        Resultado<ArrayList<Usuario>> result = UsuarioDao.getInstance().getAdministradores();
        if (!result.getResultado().equals(TipoResultado.SUCCESS)) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Traer administradores", result.getMensaje());
            return;
        }
        cmbUsuario.setItems(FXCollections.observableArrayList(result.get()));
    }

    private boolean autorizacionValida(Usuario usuario) {
        if (usuario.getUsuPin() == null || usuario.getUsuPin().isEmpty()) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "PIN no válido", "El usuario no ha definido un PIN, no se puede autorizar la venta.");
            return false;
        }
        return usuario.getUsuPin().equals(Encriptor.getInstance().encriptar(txtPin.getText()));
    }

    private void closeWindow() {
        btnAceptar.getScene().getWindow().hide();
    }

}
