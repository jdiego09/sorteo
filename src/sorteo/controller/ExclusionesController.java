package sorteo.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
import sorteo.utils.Formater;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

public class ExclusionesController extends Controller implements Initializable {

    private final String HOY = Formater.getInstance().formatFechaCorta.format(new Date());
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
    private Button btnGuardar, jbtnEliminar, jbtnSalir;

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
    private JFXButton jbtnBuscar;

    @FXML
    private Label lblApuesta, lblNumero;

    @FXML
    void rdbBloqueaVenta(ActionEvent event) {
        mostrarOcultarCampos();
        this.exclusion.setExcTipoBloqueo("T");
    }

    @FXML
    void rdbNumero(ActionEvent event) {
        mostrarOcultarCampos();
        this.exclusion.setExcTipoBloqueo("N");
    }

    @FXML
    void buscar(ActionEvent event) {
        buscarExclusiones();
    }

    @FXML
    void eliminar(ActionEvent event) {
        if (tbvExcepciones.getSelectionModel().getSelectedItem().getExcId() != null && tbvExcepciones.getSelectionModel().getSelectedItem().getExcId() > 0) {
            Resultado<String> resultado = ExclusionDao.getInstance().deleteExclusion(tbvExcepciones.getSelectionModel().getSelectedItem());

            if (resultado.getResultado().equals(TipoResultado.SUCCESS)) {
                this.listaExclusiones.remove(tbvExcepciones.getSelectionModel().getSelectedItem());
                tbvExcepciones.refresh();
            } else {
                AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Eliminar excepción", resultado.getMensaje());
            }
        }
    }

    @FXML
    void guardar(ActionEvent event) {
        try {
            if (this.exclusion.getExcFecha().before(Formater.getInstance().formatFechaCorta.parse(HOY))) {
                AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Fecha no válida", "La fecha debe ser mayor que hoy.");
                return;
            }
        } catch (ParseException ex) {
            Logger.getLogger(ExclusionesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.exclusion.setExcCodtiposorteo(cmbTipoSorteo.getValue());

        if (!exclusionValida()) {
            return;
        }
        ExclusionDao.getInstance().setExclusion(this.exclusion);
        Resultado<Exclusion> resultado = ExclusionDao.getInstance().save();

        if (!resultado.getResultado().equals(TipoResultado.SUCCESS)) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Guardar excepción", resultado.getMensaje());
            return;
        } else {
            AppWindowController.getInstance().mensaje(Alert.AlertType.INFORMATION, "Guardar excepción", resultado.getMensaje());
            agregarALista(resultado.get());
        }
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
        this.listaExclusiones.clear();
        nuevaExclusion();
        bindExclusion();
    }

    private void buscarExclusiones() {
        listaExclusiones.clear();
        Resultado<ArrayList<Exclusion>> exclusiones = ExclusionDao.getInstance().findBySorteo(cmbTipoSorteo.getValue(), Date.from(dtpFecha.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        if (!exclusiones.getResultado().equals(TipoResultado.SUCCESS)) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.INFORMATION, "Traer excepciones", exclusiones.getMensaje());
        }
        exclusiones.get().stream().forEach(listaExclusiones::add);
        bindListaExclusiones();
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

    private void mostrarOcultarCampos() {
        lblNumero.setVisible(rdbNumero.isSelected());
        lblApuesta.setVisible(rdbNumero.isSelected());
        txtNumero.setVisible(rdbNumero.isSelected());
        txtApuesta.setVisible(rdbNumero.isSelected());
    }

    private void agregarALista(Exclusion nueva) {
        if (!this.listaExclusiones.contains(nueva)) {
            this.listaExclusiones.add(nueva);
        } else {
            this.listaExclusiones.set(this.listaExclusiones.indexOf(nueva), nueva);
        }
        tbvExcepciones.refresh();
    }

    private boolean exclusionValida() {
        Resultado<Exclusion> result = ExclusionDao.getInstance().findExclusion(this.exclusion);
        if (!result.getResultado().equals(TipoResultado.ERROR)) {
            return result.get() == null;
        } else {
            AppWindowController.getInstance().mensaje(Alert.AlertType.INFORMATION, "Validar excepción", result.getMensaje());
        }
        return true;
    }

}
