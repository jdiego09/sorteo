package sorteo.controller;

import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javax.xml.bind.annotation.XmlTransient;
import sorteo.model.dao.TipoSorteoDao;
import sorteo.model.entities.TipoSorteo;
import sorteo.utils.Aplicacion;
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

    ArrayList<TipoSorteo> tiposSorteo;

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
    private TableView tbvResultados;

    @FXML
    private Label lblTotalVenta;

    @FXML
    void consultar(ActionEvent event) {
        consultarVenta();
    }

    @FXML
    void salir(ActionEvent event) {
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

    private void cargarSorteos() {
        sorteos.clear();
        Resultado<ArrayList<TipoSorteo>> sorteosResult = TipoSorteoDao.getInstance().findAllActivos();
        if (sorteosResult.getResultado() == TipoResultado.SUCCESS) {
            sorteosResult.get().stream().forEach(s -> sorteos.add(new GenValorCombo(String.valueOf(s.getCodigo()), s.getDescripcion())));
        }
        this.cmbSorteo.setItems(sorteos);
        cmbSorteo.getSelectionModel().selectFirst();
    }

    private void cargarNumeros(TipoSorteo sorteo) {
        numeros.clear();

        for (int i = sorteo.getNumeroMinimo(); i < sorteo.getNumeroMaximo() + 1; i++) {
            numeros.add(new GenValorCombo(String.valueOf(i), i < 10 ? "0" + String.valueOf(i) : String.valueOf(i)));
        }
        this.cmbNumero.setItems(numeros);
    }

    private void init() {
        tiposSorteo = new ArrayList<>();
        cargarSorteos();
        cargarNumeros(tiposSorteo.get(cmbSorteo.getSelectionModel().getSelectedIndex()));
    }

    private void consultarVenta() {
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("fechasorteo", Date.from(this.dtpFecha.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        parametros.put("tipoSorteo", this.cmbSorteo.getValue().getCodigo());
        parametros.put("numero", this.cmbNumero.getValue().getCodigo());

        if (rdbResumen.isSelected()) {
            Aplicacion.getInstance().generarReporte("rpt_resVentasFecha", parametros);
        }
        if (rdbDetalle.isSelected()) {
            Aplicacion.getInstance().generarReporte("rpt_detVentasFecha", parametros);
        }
    }
}
