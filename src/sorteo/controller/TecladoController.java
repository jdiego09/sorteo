package sorteo.controller;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import sorteo.utils.Aplicacion;
import sorteo.utils.AppWindowController;
import sorteo.utils.Resultado;

public class TecladoController extends Controller implements Initializable {

    private final int CIEN = 100;
    private static HashMap<String, KeyCode> keyCodes = new HashMap<>();

    @FXML
    private AnchorPane acpRoot;

    @FXML
    private JFXTextField txtNumero;

    @FXML
    private GridPane grdNumPad;

    @FXML
    private Button num0;

    @FXML
    private Button num5;

    @FXML
    private Button btnBack;

    @FXML
    private Button num4;

    @FXML
    private Button num3;

    @FXML
    private Button num7;

    @FXML
    private Button num8;

    @FXML
    private Button num9;

    @FXML
    private Button num6;

    @FXML
    private Button num2;

    @FXML
    private Button num1;

    @FXML
    private Button btnEnter;

    @FXML
    private Button btnClear;

    private void closeWindow() {
        this.getStage().getScene().getWindow().hide();
    }

    @FXML
    void numKeyPressed(KeyEvent event) {
        if (null != event.getCode()) {
            switch (event.getCode()) {
                case NUMPAD0:
                case DIGIT0:
                    break;
                case NUMPAD1:
                case DIGIT1:
                    break;
                case NUMPAD2:
                case DIGIT2:
                    break;
                case NUMPAD3:
                case DIGIT3:
                    break;
                case NUMPAD4:
                case DIGIT4:
                    break;
                case NUMPAD5:
                case DIGIT5:
                    break;
                case NUMPAD6:
                case DIGIT6:
                    break;
                case NUMPAD7:
                case DIGIT7:
                    break;
                case NUMPAD8:
                case DIGIT8:
                    break;
                case NUMPAD9:
                case DIGIT9:
                    break;
                case ENTER:
                    returnMonto();
                    break;
                case BACK_SPACE:
                    txtNumero.deletePreviousChar();
                    if (txtNumero.getText().length() <= 0) {
                        txtNumero.appendText("0");
                    }
                    break;
                default:
                    break;
            }
        }
        event.consume();
    }

    private void init() {
        txtNumero.addEventFilter(KeyEvent.KEY_PRESSED, this::restrictNumbersOnly);
        txtNumero.setFocusTraversable(true);
        txtNumero.requestFocus();
    }

    void restrictNumbersOnly(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case TAB:
            case BACK_SPACE:
            case DELETE:
                break;
            default:
                if (!keyEvent.getText().matches("\\d")) {
                    keyEvent.consume();
                }
                break;
        }
    }

    @Override
    public void initialize() {
        init();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

    @FXML
    void btnBack(ActionEvent event) {
        txtNumero.deletePreviousChar();
    }

    @FXML
    void btnClear(ActionEvent event) {
        txtNumero.clear();
        txtNumero.appendText("0");
    }

    @FXML
    void btnEnter(ActionEvent event) {
        returnMonto();
    }

    @FXML
    void setNumero(ActionEvent event) {
        Button source = (Button) event.getSource();
        txtNumero.appendText(source.getText());
        txtNumero.requestFocus();
    }

    private void returnMonto() {
        if (txtNumero.getText().length() > 0) {
            int numero = Integer.valueOf(txtNumero.getText());
            int residuo = numero % CIEN;
            if (residuo != 0) {
                AppWindowController.getInstance().mensaje(Alert.AlertType.WARNING, "Monto incorrecto", "El monto ingresado debe ser múltiplo de: " + String.valueOf(CIEN));
                return;
            }
            Resultado<Object> resultado = new Resultado<>();
            resultado.set(Integer.valueOf(txtNumero.getText()));
            Aplicacion.getInstance().setNumeroIndicado(resultado);
            closeWindow();
        }
    }

}
