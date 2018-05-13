package sorteo.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.NumberStringConverter;
import javax.xml.bind.annotation.XmlTransient;
import sorteo.model.dao.ExclusionDao;
import sorteo.model.dao.TipoSorteoDao;
import sorteo.model.entities.Exclusion;
import sorteo.model.entities.TipoSorteo;
import sorteo.utils.AppWindowController;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

public class ExclusionesController extends Controller implements Initializable {

    Exclusion exclusion;
    @XmlTransient
    ObservableList<TipoSorteo> tiposSorteo = FXCollections
       .observableArrayList();

    @XmlTransient
    ObservableList<Exclusion> listaExclusiones = FXCollections
       .observableArrayList();

    @FXML
    private AnchorPane acpRoot;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button jbtnSalir;

    @FXML
    private ComboBox<TipoSorteo> cmbTipoSorteo;

    @FXML
    private DatePicker dtpFecha;

    @FXML
    private RadioButton rdbBloqueo;

    @FXML
    private ToggleGroup grpTipoExclusion;

    @FXML
    private RadioButton rdbNumero;

    @FXML
    private TextField txtNumero;

    @FXML
    private TextField txtApuesta;

    @FXML
    private TableView<Exclusion> tbvExcepciones;

    @FXML
    private TableColumn<Exclusion, String> tbcBloqueo;

    @FXML
    private TableColumn<Exclusion, Integer> tbcNumero;

    @FXML
    private TableColumn<Exclusion, Double> tbcApuesta;

    @FXML
    private JFXButton jbtnEliminar, jbtnBuscar;

    @FXML
    void buscar(ActionEvent event) {
        buscarExclusiones();
    }

    @FXML
    void eliminar(ActionEvent event) {

    }

    @FXML
    void guardar(ActionEvent event) {

    }

    @FXML
    void limpiar(ActionEvent event) {
        clearForm();
    }

    @FXML
    void regresar(ActionEvent event) {
        clearForm();
        AppWindowController.getInstance().goHome();
    }

    @Override
    public void initialize() {
        init();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

    private void init() {
        cargarTiposSorteo();

        cmbTipoSorteo.setItems(this.tiposSorteo);
        if (this.exclusion != null) {
            unbindExclusion();
        }
        nuevaExclusion();
        bindExclusion();
    }

    private void cargarTiposSorteo() {
        tiposSorteo.clear();
        Resultado<ArrayList<TipoSorteo>> sorteosResult = TipoSorteoDao.getInstance().findAllActivos();
        if (sorteosResult.getResultado() == TipoResultado.SUCCESS) {
            sorteosResult.get().stream().forEach(tiposSorteo::add);
        }
    }

    private void bindExclusion() {
        cmbTipoSorteo.valueProperty().bindBidirectional(this.exclusion.getExcCodtiposorteoProperty());
        dtpFecha.valueProperty().bindBidirectional(this.exclusion.getExcFechaProperty());
        txtNumero.textProperty().bindBidirectional(this.exclusion.getExcNumeroProperty(), new NumberStringConverter());
        txtApuesta.textProperty().bindBidirectional(this.exclusion.getExcMontoProperty(), new NumberStringConverter());
    }

    private void unbindExclusion() {
        cmbTipoSorteo.valueProperty().unbindBidirectional(this.exclusion.getExcCodtiposorteoProperty());
        dtpFecha.valueProperty().unbindBidirectional(this.exclusion.getExcFechaProperty());
        txtNumero.textProperty().unbindBidirectional(this.exclusion.getExcNumeroProperty());
        txtApuesta.textProperty().unbindBidirectional(this.exclusion.getExcMontoProperty());
    }

    private void nuevaExclusion() {
        this.exclusion = new Exclusion();
    }

    private void clearForm() {
        unbindExclusion();
        this.tiposSorteo.clear();
        nuevaExclusion();
        bindExclusion();
    }

    private void buscarExclusiones() {
        Resultado<ArrayList<Exclusion>> exclusiones = ExclusionDao.getInstance().findBySorteo(cmbTipoSorteo.getValue(), Date.from(dtpFecha.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        if (!exclusiones.getResultado().equals(TipoResultado.SUCCESS)) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.INFORMATION, "Traer excepciones", exclusiones.getMensaje());
        }
        exclusiones.get().stream().forEach(listaExclusiones::add);
        bindListaExclusiones();
        addListenerTableExclusiones(tbvExcepciones);
    }

    private void bindListaExclusiones() {
        if (this.listaExclusiones != null) {
            tbvExcepciones.setItems(this.listaExclusiones);
            tbvExcepciones.refresh();
        }
        tbcBloqueo.setCellValueFactory(new PropertyValueFactory<>("descripcionBloqueo"));
        tbcNumero.setCellValueFactory(new PropertyValueFactory<>("excNumero"));
        tbcApuesta.setCellValueFactory(new PropertyValueFactory<>("excMonto"));
    }

    private void addListenerTableExclusiones(TableView table) {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            unbindExclusion();
            if (newSelection != null) {
                this.exclusion = (Exclusion) newSelection;
                bindExclusion();
            }
        });
    }

}
