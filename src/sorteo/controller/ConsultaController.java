package sorteo.controller;

import java.net.URL;
import java.time.LocalDate;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javax.xml.bind.annotation.XmlTransient;
import sorteo.model.dao.SorteoDao;
import sorteo.model.dao.TipoSorteoDao;
import sorteo.model.entities.DetalleFactura;
import sorteo.model.entities.Sorteo;
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
        String queryName = null;
        boolean esResumen = rdbResumen.isSelected();

        if (rdbResumen.isSelected()) {
            queryName = "Sorteo.resumenVentaFecha";

        }
        if (rdbDetalle.isSelected()) {
            queryName = "Sorteo.detalleVentaFecha";
        }
        Resultado<ArrayList<Object>> resultado = SorteoDao.getInstance().getVentasDia(queryName, tiposSorteo.get(cmbSorteo.getSelectionModel().getSelectedIndex()).getCodigo(), Date.from(dtpFecha.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        if (!resultado.getResultado().equals(TipoResultado.SUCCESS)) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Consultar ventas", resultado.getMensaje());
        }
        setDatosTableView(esResumen, resultado.get());
    }

    private void setDatosTableView(boolean esResumen, ArrayList<Object> datos) {
        HashMap<String, TableColumn> columnas = new HashMap<>();
        if (esResumen) {

            TableColumn<Sorteo, LocalDate> tbcFecSorteo = new TableColumn<>("Fec. Sorteo");
            tbcFecSorteo.setPrefWidth(100);
            tbcFecSorteo.setCellValueFactory(cd -> cd.getValue().getFechaProperty());
            columnas.put("fecha", tbcFecSorteo);

//            TableColumn<BikPersona, String> tbcNombre = new TableColumn<>("Nombre");
//            tbcNombre.setPrefWidth(400);
//            tbcNombre.setCellValueFactory(cd -> cd.getValue().getNombreCompletoProperty());
        }

//        tbvResultados.getColumns()
//           .clear();
//        tbvResultados.getItems()
//           .clear();
//        tbvResultados.getColumns()
//           .add(tbcCedula);
//        tbvResultados.getColumns()
//           .add(tbcNombre);
//        tbvResultados.refresh();
    }
}
