package sorteo.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javax.xml.bind.annotation.XmlTransient;
import sorteo.model.dao.EmpresaDao;
import sorteo.model.entities.Empresa;
import sorteo.model.entities.Sucursal;
import sorteo.utils.AppWindowController;
import sorteo.utils.GenValorCombo;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

public class EmpresaController extends Controller implements Initializable {

    Empresa empresa;
    Sucursal sucursal;

    @XmlTransient
    ObservableList<GenValorCombo> estados = FXCollections
       .observableArrayList();

    @XmlTransient
    private ObservableList<Empresa> listaEmpresas = FXCollections
       .observableArrayList();

    @XmlTransient
    private ObservableList<Sucursal> listaSucursales = FXCollections
       .observableArrayList();

    @FXML
    private AnchorPane acpRoot;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnGuardaUsuario;

    @FXML
    private Button btnSalir;

    @FXML
    private JFXTextField jtxfCedJuridica;

    @FXML
    private JFXTextField jtxfDescripcion;

    @FXML
    private JFXTextField jtxfTelefono;

    @FXML
    private JFXTextField jtxfEmail;

    @FXML
    private JFXTextField jtxfLogo;

    @FXML
    private JFXButton jbtnBuscarLogo;

    @FXML
    private TableView<Empresa> tbvEmpresas;

    @FXML
    private TableColumn<Empresa, String> tbcCedula;

    @FXML
    private TableColumn<Empresa, String> tbcDescripcion;

    @FXML
    private JFXTextField jtxfSucDescripcion;

    @FXML
    private JFXComboBox<GenValorCombo> jcmbSucEstado;
    @FXML
    private JFXTextField jtxfSucTelefono;

    @FXML
    private JFXTextField jtxfSucEmail;

    @FXML
    private TableView<Sucursal> tbvSucursales;

    @FXML
    private TableColumn<Sucursal, String> tbcSucDescripcion;

    @FXML
    private TableColumn<Sucursal, String> tbcSucEstado;

    @FXML
    private JFXButton jbtnAgregar;

    @FXML
    private JFXButton jbtnEliminar;

    @FXML
    private TabPane tabEmpresas;

    @FXML
    void buscarArchivoLogo(ActionEvent event) {

    }

    @FXML
    void guardar(ActionEvent event) {

    }

    @FXML
    void limpiar(ActionEvent event) {
        clearForm();
    }

    @FXML
    void salir(ActionEvent event) {
        clearForm();
        AppWindowController.getInstance().goHome();
    }

    @FXML
    void agregar(ActionEvent event) {
        agregarSucursalALista(this.sucursal);
    }

    @FXML
    void eliminar(ActionEvent event) {

    }

    @Override
    public void initialize() {
        init();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

    private void nuevaEmpresa() {
        this.empresa = new Empresa();
        this.empresa.setSucursales(new ArrayList<>());
        unbindEmpresa();
    }

    private void nuevaSucursal() {
        this.sucursal = new Sucursal();
        this.sucursal.setEmpresa(this.empresa);
        unbindSucursal();
    }

    private void init() {
        this.estados.clear();
        this.estados.add(new GenValorCombo("A", "Activo"));
        this.estados.add(new GenValorCombo("I", "Inactivo"));
        jcmbSucEstado.setItems(this.estados);
        if (this.empresa != null) {
            unbindEmpresa();
        }
        nuevaEmpresa();
        bindEmpresa();

        cargarEmpresas();
        bindListaEmpresas();
        addListenerTableEmpresas(tbvEmpresas);

        jcmbSucEstado.getSelectionModel().selectFirst();
        jtxfCedJuridica.requestFocus();
    }

    private void bindEmpresa() {
        jtxfCedJuridica.textProperty().bindBidirectional(this.empresa.getCedJuridicaProperty());
        jtxfDescripcion.textProperty().bindBidirectional(this.empresa.getNombreProperty());
        jtxfTelefono.textProperty().bindBidirectional(this.empresa.getTelefonoProperty());
        jtxfEmail.textProperty().bindBidirectional(this.empresa.getEmailProperty());
        jtxfLogo.textProperty().bindBidirectional(this.empresa.getLogoProperty());
    }

    private void unbindEmpresa() {
        jtxfCedJuridica.textProperty().unbindBidirectional(this.empresa.getCedJuridicaProperty());
        jtxfDescripcion.textProperty().unbindBidirectional(this.empresa.getNombreProperty());
        jtxfTelefono.textProperty().unbindBidirectional(this.empresa.getTelefonoProperty());
        jtxfEmail.textProperty().unbindBidirectional(this.empresa.getEmailProperty());
        jtxfLogo.textProperty().unbindBidirectional(this.empresa.getLogoProperty());
    }

    private void bindSucursal() {
        jtxfSucDescripcion.textProperty().bindBidirectional(this.sucursal.getNombreProperty());
        jtxfSucTelefono.textProperty().bindBidirectional(this.sucursal.getTelefonoProperty());
        jtxfSucEmail.textProperty().bindBidirectional(this.sucursal.getEmailProperty());
        jcmbSucEstado.valueProperty().bindBidirectional(this.sucursal.getEstadoProperty());
    }

    private void unbindSucursal() {
        jtxfSucDescripcion.textProperty().unbindBidirectional(this.sucursal.getNombreProperty());
        jtxfSucTelefono.textProperty().unbindBidirectional(this.sucursal.getTelefonoProperty());
        jtxfSucEmail.textProperty().unbindBidirectional(this.sucursal.getEmailProperty());
        jcmbSucEstado.valueProperty().unbindBidirectional(this.sucursal.getEstadoProperty());
    }

    private void addListenerTableEmpresas(TableView table) {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            unbindEmpresa();
            if (newSelection != null) {
                this.empresa = (Empresa) newSelection;
                this.listaSucursales.clear();
                tbvSucursales.getItems().clear();
                this.listaSucursales = FXCollections.observableArrayList(this.empresa.getSucursales());
                bindListaSucursales();
                addListenerTableSucursales(tbvSucursales);
                bindEmpresa();
                jtxfCedJuridica.setDisable(true);
            }
            jtxfDescripcion.requestFocus();
        });
    }

    private void addListenerTableSucursales(TableView table) {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (this.sucursal != null) {
                unbindSucursal();
            }
            if (newSelection != null) {
                this.sucursal = (Sucursal) newSelection;
                bindListaSucursales();
                addListenerTableSucursales(tbvSucursales);
                bindSucursal();
            }
            jtxfSucDescripcion.requestFocus();
        });
    }

    private void bindListaEmpresas() {
        if (this.listaEmpresas != null) {
            tbvEmpresas.setItems(this.listaEmpresas);
            tbvEmpresas.refresh();
        }
        tbcCedula.setCellValueFactory(new PropertyValueFactory<>("cedJuridica"));
        tbcDescripcion.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    }

    private void bindListaSucursales() {
        if (this.listaSucursales != null) {
            tbvSucursales.setItems(this.listaSucursales);
            tbvSucursales.refresh();
        }
        tbcSucDescripcion.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tbcSucEstado.setCellValueFactory(new PropertyValueFactory<>("descripcionEstado"));

    }

    private void traerEmpresa(Integer codigo) {
        unbindEmpresa();
        Resultado<Empresa> empr = EmpresaDao.getInstance().findEmpresaByCodigo(codigo);
        if (empr.getResultado().equals(TipoResultado.ERROR)) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR,
               "Buscar empresa", empr.getMensaje());
            return;
        }
        if (empr.get() != null) {
            if (empr.get().getCodigo() != null) {
                this.empresa = empr.get();
                jtxfCedJuridica.setDisable(true);
                // Se cargan los roles del usuario.

                this.listaSucursales.clear();
                tbvSucursales.getItems().clear();
                this.empresa.getSucursales().stream().forEach(this.listaSucursales::add);
            }
        }
    }

    private void agregarEmpresaALista(Empresa nueva) {
        if (!this.listaEmpresas.contains(nueva)) {
            this.listaEmpresas.add(nueva);
        } else {
            this.listaEmpresas.set(this.listaEmpresas.indexOf(nueva), nueva);
        }
        tbvEmpresas.refresh();
    }

    private void agregarSucursalALista(Sucursal nueva) {
        if (!this.listaSucursales.contains(nueva)) {
            this.listaSucursales.add(nueva);
        } else {
            this.listaSucursales.set(this.listaSucursales.indexOf(nueva), nueva);
        }
        tbvSucursales.refresh();
    }

    private void cargarEmpresas() {
        Resultado<ArrayList<Empresa>> empresas = EmpresaDao.getInstance().findAll();
        if (!empresas.getResultado().equals(TipoResultado.ERROR)) {
            this.listaEmpresas = FXCollections.observableArrayList(empresas.get());
        } else {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Cargar empresas", empresas.getMensaje());
        }
    }

    private void clearForm() {
        if (tabEmpresas.getSelectionModel().getSelectedItem().getText().toLowerCase().equals("empresa")) {
            unbindEmpresa();
            if (this.sucursal != null) {
                unbindSucursal();
            }
            this.listaEmpresas.clear();
            this.listaSucursales.clear();
            nuevaEmpresa();
            bindEmpresa();

            cargarEmpresas();
            bindListaEmpresas();

            this.jtxfCedJuridica.setDisable(false);
            this.jtxfCedJuridica.requestFocus();
        } else {
            unbindSucursal();
            nuevaSucursal();
            bindSucursal();
            jcmbSucEstado.requestFocus();
            jtxfSucDescripcion.requestFocus();
        }
    }

}
