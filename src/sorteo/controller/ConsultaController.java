package sorteo.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javax.xml.bind.annotation.XmlTransient;
import sorteo.model.dao.TipoSorteoDao;
import sorteo.model.entities.TipoSorteo;
import sorteo.utils.AppWindowController;
import sorteo.utils.GenValorCombo;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

public class ConsultaController extends Controller implements Initializable {

    @FXML
    private AnchorPane acpRoot;

    @FXML
    private Button btnSalir;

    @FXML
    private DatePicker dtpFecha;

    @FXML
    private ComboBox<GenValorCombo> cmbSorteo, cmbNumero;

    @XmlTransient
    ObservableList<GenValorCombo> sorteos = FXCollections
    .observableArrayList();

    @XmlTransient
    ObservableList<GenValorCombo> numeros = FXCollections
    .observableArrayList();

    @FXML
    private Button btnConsultar;

    @FXML
    private RadioButton rdbResumen;

    @FXML
    private ToggleGroup tipRep;

    @FXML
    private RadioButton rdbDetalle;

    @FXML
    private Label lblReporte;

    @FXML
    private TableView<?> tbvResultados;

    @FXML
    private TableColumn<?, ?> tbcLinea;

    @FXML
    private TableColumn<?, ?> tbcNumero;

    @FXML
    private TableColumn<?, ?> tbcMonto;

    @FXML
    private Label lblTotalVenta;

    @FXML
    void consultar(ActionEvent event) {

    }

    @FXML
    void salir(ActionEvent event) {
        AppWindowController.getInstance().cerrarVentana();
    }

    @Override
    public void initialize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void cargarSorteos() {
        sorteos.clear();
        Resultado<ArrayList<TipoSorteo>> sorteosResult = TipoSorteoDao.getInstance().findAllActivos();
        if (sorteosResult.getResultado() == TipoResultado.SUCCESS) {
            sorteosResult.get().stream().forEach(s -> sorteos.add(new GenValorCombo(String.valueOf(s.getCodigo()), s.getDescripcion())));
        }
        this.cmbSorteo.setItems(sorteos);
    }

    private void cargarNumeros(TipoSorteo sorteo) {
        numeros.clear();

        for (int i = sorteo.getNumeroMinimo(); i < sorteo.getNumeroMaximo() + 1; i++) {
            numeros.add(new GenValorCombo(String.valueOf(i), i < 10 ? "0" + String.valueOf(i) : String.valueOf(i)));
        }
        this.cmbNumero.setItems(numeros);
    }
    
    private void consultarVenta(){
        
    }
}
