package sorteo.controller;

import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.sql.Date;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javax.xml.bind.annotation.XmlTransient;
import sorteo.model.dao.SorteoDao;
import sorteo.model.dao.TipoSorteoDao;
import sorteo.model.dao.UsuarioDao;
import sorteo.model.entities.Sorteo;
import sorteo.model.entities.TipoSorteo;
import sorteo.model.entities.Usuario;
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
    private RadioButton rdbFecha;

    @FXML
    private ToggleGroup reporte;

    @FXML
    private RadioButton rdbSorteo;
    @FXML
    private RadioButton rdbUsuario;
    @FXML
    private RadioButton rdbRVenta;

    @FXML
    private RadioButton rdbExcepciones;

    @FXML
    private Label lblFecha;

    @FXML
    private DatePicker dtpFecha;

    @FXML
    private Label lblSorteo;

    @FXML
    private ComboBox<TipoSorteo> cmbSorteo;

    @FXML
    private Label lblNumero;

    @FXML
    private ComboBox<GenValorCombo> cmbNumero;

    @FXML
    private RadioButton rdbResumen;

    @FXML
    private ToggleGroup tipRep;

    @FXML
    private RadioButton rdbDetalle;

    @FXML
    private Button btnConsultar;

    @XmlTransient
    ObservableList<TipoSorteo> sorteos = FXCollections
       .observableArrayList();

    @XmlTransient
    ObservableList<GenValorCombo> numeros = FXCollections
       .observableArrayList();

    ArrayList<TipoSorteo> tiposSorteo;

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
        reporte.selectedToggleProperty().addListener((l) -> {
            mostrarParametros();
        });
        cmbSorteo.valueProperty().addListener((l) -> {
            cargarNumeros(cmbSorteo.getValue());
        });
    }

    private void cargarSorteos() {
        sorteos.clear();
        sorteos.add(new TipoSorteo(0, "Todos los sorteos"));
        Resultado<ArrayList<TipoSorteo>> sorteosResult = TipoSorteoDao.getInstance().findAllActivos();
        if (sorteosResult.getResultado() == TipoResultado.SUCCESS) {
            sorteosResult.get().stream().forEach(sorteos::add);
        }
        this.cmbSorteo.setItems(sorteos);
        cmbSorteo.getSelectionModel().selectFirst();
    }

    private void cargarNumeros(TipoSorteo sorteo) {
        numeros.clear();
        numeros.add(new GenValorCombo("%", "Todos"));
        for (int i = sorteo.getNumeroMinimo(); i < sorteo.getNumeroMaximo() + 1; i++) {
            numeros.add(new GenValorCombo(String.valueOf(i), i < 10 ? "0" + String.valueOf(i) : String.valueOf(i)));
        }
        this.cmbNumero.setItems(numeros);
        this.cmbNumero.getSelectionModel().selectFirst();
    }

    private void cargarUsuarios() {
        numeros.clear();
        numeros.add(new GenValorCombo("%", "Todos"));
        Resultado<ArrayList<Usuario>> resultado = UsuarioDao.getInstance().findUsuariosSistema();
        if (resultado.getResultado().equals(TipoResultado.SUCCESS)) {
            resultado.get().stream().forEach(u -> numeros.add(new GenValorCombo(u.getUsuCodigo(), u.getUsuDescripcion())));
        }
        this.cmbNumero.setItems(numeros);
        this.cmbNumero.getSelectionModel().selectFirst();
    }

    private void init() {
        tiposSorteo = new ArrayList<>();
    }

    private void consultarVenta() {
        String nombreReporte = null;
        HashMap<String, Object> parametros = new HashMap<>();        
        if (rdbSorteo.isSelected()) {
            parametros.put("fecha", Date.from(this.dtpFecha.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            parametros.put("sorteo", this.cmbSorteo.getValue().getCodigo() != 0 ? this.cmbSorteo.getValue().getCodigo() : "%");
            parametros.put("numero", this.cmbNumero.getValue().getCodigo());
        }
        if (rdbUsuario.isSelected()) {
            parametros.put("fecha", Date.from(this.dtpFecha.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            parametros.put("vendedor", this.cmbNumero.getValue().getCodigo());
        }
        if (rdbFecha.isSelected()) {
            parametros.put("fecha", Date.from(this.dtpFecha.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            if (rdbResumen.isSelected()) {
                nombreReporte = "rpt_resVentasFecha";
            } else {
                nombreReporte = "rpt_detVentasFecha";
            }
        }
        if (rdbSorteo.isSelected()) {
            parametros.put("fecha", Date.from(this.dtpFecha.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            if (rdbResumen.isSelected()) {
                nombreReporte = "rpt_resVentasSorteo";
            } else {
                nombreReporte = "rpt_detVentasSorteo";
            }
        }
        if (rdbUsuario.isSelected()) {
            parametros.put("fecha", Date.from(this.dtpFecha.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            nombreReporte = "rpt_ventasDiaVendedor";
        }
        if (rdbExcepciones.isSelected()) {
            nombreReporte = "rpt_excepciones";
        }
        if (rdbRVenta.isSelected()) {            
            Resultado<Sorteo> sorteo = SorteoDao.getInstance().findByFecha(Date.from(this.dtpFecha.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), this.cmbSorteo.getValue());
            if (sorteo.getResultado().equals(TipoResultado.SUCCESS)) {
                nombreReporte = "rpt_resVentaSorteo";
                parametros.put("sorteo", sorteo.get().getCodigo());
                SorteoDao.getInstance().genListaNumeros(sorteo.get().getTipoSorteo().getCodigo());
                
            } else {
                AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Error", sorteo.getMensaje());
                return;
            }

        }

        Aplicacion.getInstance().generarReporte(nombreReporte, parametros);
    }

    private void mostrarParametros() {
        if (rdbFecha.isSelected()) {
            lblFecha.setText("Fecha de venta");
        }
        if (rdbSorteo.isSelected()) {
            cargarSorteos();
            lblFecha.setText("Fecha del sorteo");
            lblNumero.setText("Número");
            lblNumero.setLayoutX(13);
            lblNumero.setLayoutY(158);
            cmbNumero.setLayoutX(13);
            cmbNumero.setLayoutY(183);
            cargarNumeros(cmbSorteo.getValue());
        }
        if (rdbUsuario.isSelected()) {
            cargarUsuarios();
            lblNumero.setText("Usuario");
            lblNumero.setLayoutX(13);
            lblNumero.setLayoutY(85);
            cmbNumero.setLayoutX(13);
            cmbNumero.setLayoutY(110);
        }
        if (rdbRVenta.isSelected()) {
            cargarSorteos();
            lblFecha.setText("Fecha del sorteo");
        }
        lblSorteo.setVisible(rdbSorteo.isSelected() || rdbRVenta.isSelected());
        cmbSorteo.setVisible(rdbSorteo.isSelected() || rdbRVenta.isSelected());
        lblNumero.setVisible(rdbSorteo.isSelected() || rdbUsuario.isSelected());
        cmbNumero.setVisible(rdbSorteo.isSelected() || rdbUsuario.isSelected());

    }
}
