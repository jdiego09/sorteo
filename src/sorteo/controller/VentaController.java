package sorteo.controller;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javax.xml.bind.annotation.XmlTransient;
import jfxtras.scene.control.CalendarPicker;
import sorteo.model.dao.TipoSorteoDao;
import sorteo.model.entities.DetalleFactura;
import sorteo.model.entities.Factura;
import sorteo.model.entities.Sorteo;
import sorteo.model.entities.TipoSorteo;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

public class VentaController extends Controller implements Initializable {

    private Sorteo sorteo;
    private Factura factura;
    @FXML
    private AnchorPane acpRoot;

    @FXML
    private BorderPane brpSecciones;

    @FXML
    private CalendarPicker calFechaSorteo;

    @FXML
    private VBox vbxSorteos;

    @FXML
    private TableView<DetalleFactura> tbvDetFactura;

    @FXML
    private TableColumn<DetalleFactura, Integer> tbcLinea;

    @FXML
    private TableColumn<DetalleFactura, Integer> tbcNumero;

    @FXML
    private TableColumn<DetalleFactura, Double> tbcMonto;

    @FXML
    private TableColumn<?, ?> tbcBtnEliLinea;

    @FXML
    private Label lblSubTotGen;

    @FXML
    private Button btnAplicar;

    @FXML
    private Pane pneTeclado;

    @FXML
    private JFXTextField txtCliente;

    @FXML
    private FlowPane flpNumeros;

    @FXML
    private Label lblUsuario;

    @FXML
    private Label lblFecha;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnSalir;

    @XmlTransient
    public ObservableList<TipoSorteo> sorteos = FXCollections
            .observableArrayList();

    @Override
    public void initialize() {
        sorteo = new Sorteo();
        factura = new Factura();
        cargarSorteos();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarSorteos();
    }
    
    

    private void cargarSorteos() {
        vbxSorteos.getChildren().clear();
        Resultado<ArrayList<TipoSorteo>> sorteosResult = TipoSorteoDao.getInstance().findAllActivos();
        if (sorteosResult.getResultado() == TipoResultado.SUCCESS) {
            sorteosResult.get().stream().forEach(s -> {
                ToggleButton btn = new ToggleButton();
                btn.setText(s.getDescripcion());
                btn.setId(String.valueOf(s.getCodigo()));
                btn.getStyleClass().add("buttonsorteo");
                btn.setMaxWidth(Integer.MAX_VALUE);
                btn.setMinHeight(80);
                //btn.setOnAction(menuPantallasHandler);
                vbxSorteos.getChildren().add(btn);
            });
        }
        sorteosResult.get().stream().forEach(sorteos::add);
    }

}
