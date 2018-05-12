/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXTimePicker;
import java.util.ArrayList;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.NumberStringConverter;
import javax.xml.bind.annotation.XmlTransient;
import sorteo.model.dao.TipoSorteoDao;
import sorteo.model.entities.TipoSorteo;
import sorteo.utils.Aplicacion;
import sorteo.utils.AppWindowController;
import sorteo.utils.GenValorCombo;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

/**
 * FXML Controller class
 *
 * @author jdiego
 */
public class TipoSorteoController extends Controller implements Initializable {

    TipoSorteo tipoSorteo;

    @XmlTransient
    private ObservableList<TipoSorteo> tiposSorteo = FXCollections
       .observableArrayList();
    @XmlTransient
    ObservableList<GenValorCombo> estados = FXCollections
       .observableArrayList();

    @FXML
    private AnchorPane acpRoot;

    @FXML
    private Button jbtnSalir;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnGuardar;

    @FXML
    private TextField jtxfDescripcion;

    @FXML
    private TextField jtxfNumMinimo;

    @FXML
    private TextField jtxfNumMaximo;

    @FXML
    private TextField jtxfMontoMax;

    @FXML
    private TextField jtxfCantMax;

    @FXML
    private JFXTimePicker jtpHoraCorte;

    @FXML
    private ComboBox<GenValorCombo> jcmbEstado;

    @FXML
    private TableView<TipoSorteo> tbvSorteos;

    @FXML
    private TableColumn<TipoSorteo, String> tbcDescripcion, tbcEstado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
    }

    @Override
    public void initialize() {
        init();
    }

    private void init() {
        this.estados.clear();
        this.estados.add(new GenValorCombo("A", "Activo"));
        this.estados.add(new GenValorCombo("I", "Inactivo"));
        jcmbEstado.setItems(this.estados);
        if (this.tipoSorteo != null) {
            unbindSorteo();
        }
        nuevoSorteo();
        bindSorteo();

        cargarSorteos();
        bindListaSorteos();
        addListenerTableSorteos(tbvSorteos);

        jcmbEstado.getSelectionModel().selectFirst();
        jtxfDescripcion.requestFocus();
    }

    private void nuevoSorteo() {
        this.tipoSorteo = new TipoSorteo();
        unbindSorteo();
    }

    private void bindSorteo() {
        this.jtxfDescripcion.textProperty().bindBidirectional(this.tipoSorteo.getDescripcionProperty());
        this.jtxfNumMinimo.textProperty().bindBidirectional(this.tipoSorteo.getNumeroMinimoProperty(), new NumberStringConverter());
        this.jtxfNumMaximo.textProperty().bindBidirectional(this.tipoSorteo.getNumeroMaximoProperty(), new NumberStringConverter());
        this.jtxfMontoMax.textProperty().bindBidirectional(this.tipoSorteo.getMontoMaximoProperty(), new NumberStringConverter());
        this.jtxfCantMax.textProperty().bindBidirectional(this.tipoSorteo.getCantNumVentaProperty(), new NumberStringConverter());
        this.jtpHoraCorte.valueProperty().bindBidirectional(this.tipoSorteo.getHoraCorteProperty());
        this.jcmbEstado.valueProperty().bindBidirectional(this.tipoSorteo.getEstadoProperty());
    }

    private void unbindSorteo() {
        this.jtxfDescripcion.textProperty().unbindBidirectional(this.tipoSorteo.getDescripcionProperty());
        this.jtxfNumMinimo.textProperty().unbindBidirectional(this.tipoSorteo.getNumeroMinimoProperty());
        this.jtxfNumMaximo.textProperty().unbindBidirectional(this.tipoSorteo.getNumeroMaximoProperty());
        this.jtxfMontoMax.textProperty().unbindBidirectional(this.tipoSorteo.getMontoMaximoProperty());
        this.jtxfCantMax.textProperty().unbindBidirectional(this.tipoSorteo.getCantNumVentaProperty());
        this.jtpHoraCorte.valueProperty().unbindBidirectional(this.tipoSorteo.getHoraCorteProperty());
        this.jcmbEstado.valueProperty().unbindBidirectional(this.tipoSorteo.getEstadoProperty());
    }

    public void bindListaSorteos() {
        if (this.tiposSorteo != null) {
            tbvSorteos.setItems(this.tiposSorteo);
            tbvSorteos.refresh();
        }
        tbcDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        tbcEstado.setCellValueFactory(new PropertyValueFactory<>("descripcionEstado"));
    }

    private void cargarSorteos() {
        this.tiposSorteo.clear();
        tbvSorteos.getItems().clear();
        Resultado<ArrayList<TipoSorteo>> tipSorteos = TipoSorteoDao.getInstance().findAll();
        tipSorteos.get().stream().forEach(this.tiposSorteo::add);
        bindListaSorteos();
    }

    private void addListenerTableSorteos(TableView table) {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            unbindSorteo();
            if (newSelection != null) {
                this.tipoSorteo = (TipoSorteo) newSelection;
                bindSorteo();
            }
            jcmbEstado.requestFocus();
            this.jtxfDescripcion.requestFocus();
        });
    }

    private void clearForm() {
        unbindSorteo();
        this.tiposSorteo.clear();
        nuevoSorteo();
        bindSorteo();
        this.jtxfDescripcion.requestFocus();
    }

    private void agregarSorteoALista(TipoSorteo nuevo) {
        if (!this.tiposSorteo.contains(nuevo)) {
            this.tiposSorteo.add(nuevo);
        } else {
            this.tiposSorteo.set(this.tiposSorteo.indexOf(nuevo), nuevo);
        }
        tbvSorteos.refresh();
    }

    private void guardarSorteo() {
        if (this.tipoSorteo.getDescripcion() == null || this.tipoSorteo.getDescripcion().isEmpty()) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Guardar sorteo", "Debe indicar la descripción del sorteo.");
            this.jtxfDescripcion.requestFocus();
            return;
        }
        if (this.tipoSorteo.getNumeroMinimo() == null || this.tipoSorteo.getNumeroMinimo()
           < 0) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Guardar sorteo", "Debe indicar el número más bajo permitido para el sorteo.");
            this.jtxfNumMinimo.requestFocus();
            return;
        }
        if (this.tipoSorteo.getNumeroMaximo() == null || this.tipoSorteo.getNumeroMaximo()
           < 0) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Guardar sorteo", "Debe indicar el número más alto permitido para el sorteo.");
            this.jtxfNumMaximo.requestFocus();
            return;
        }
        if (this.tipoSorteo.getCantNumVenta() == null || this.tipoSorteo.getCantNumVenta()
           <= 0) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Guardar sorteo", "Debe indicar la cantidad máxima de números a vender de cada venta.");
            this.jtxfCantMax.requestFocus();
            return;
        }
        if (this.tipoSorteo.getMontoMaximo() == null || this.tipoSorteo.getMontoMaximo()
           <= 0) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Guardar sorteo", "Debe indicar el monto máximo de apuesta por número permitido para el sorteo.");
            this.jtxfMontoMax.requestFocus();
            return;
        }
        if (this.tipoSorteo.getNumeroMinimo() >= this.tipoSorteo.getNumeroMaximo()) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Guardar sorteo", "El número más bajo no puede ser igual ni mayor que el número más alto.");
            this.jtxfNumMinimo.requestFocus();
            return;
        }
        if (this.jcmbEstado.getValue().getCodigo() == null || this.jcmbEstado.getValue().getCodigo().isEmpty()) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Guardar sorteo", "Debe indicar el estado del sorteo.");
            this.jtxfDescripcion.requestFocus();
            return;
        }
        this.tipoSorteo.setTsoFeccrea(new Date());
        this.tipoSorteo.setTsoUsrcrea(Aplicacion.getInstance().getUsuario().getUsuCodigo());

        TipoSorteoDao.getInstance().setTipoSorteo(this.tipoSorteo);
        Resultado<TipoSorteo> resultado = TipoSorteoDao.getInstance().save();

        if (resultado.getResultado().equals(TipoResultado.ERROR)) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Guardar sorteo", resultado.getMensaje());
            return;
        }
        AppWindowController.getInstance().mensaje(Alert.AlertType.INFORMATION, "Guardar usuario", resultado.getMensaje());
        agregarSorteoALista(resultado.get());
    }

    @FXML
    void guardar(ActionEvent event) {
        guardarSorteo();
    }

    @FXML
    void limpiar(ActionEvent event) {
        clearForm();
    }

    @FXML
    void regresar(ActionEvent event) {
        clearForm();
        cargarSorteos();
        AppWindowController.getInstance().goHome();
    }
}
