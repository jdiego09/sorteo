package sorteo.controller;

import com.jfoenix.controls.JFXButton;
import java.io.File;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
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
    private TextArea jtxfLeyenda;

    @FXML
    private TextField jtxfDescripcion;

    @FXML
    private TextField jtxfTelefono;

    @FXML
    private TextField jtxfEmail;

    @FXML
    private TextField jtxfLogo;

    @FXML
    private JFXButton jbtnBuscarLogo;

    @FXML
    private TableView<Empresa> tbvEmpresas;

    @FXML
    private TableColumn<Empresa, String> tbcDescripcion;

    @FXML
    private TextField jtxfSucDescripcion;

    @FXML
    private ComboBox<GenValorCombo> jcmbSucEstado;
    @FXML
    private TextField jtxfSucTelefono;

    @FXML
    private TextField jtxfSucEmail;

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
        FileChooser rutaLogo = new FileChooser();
        rutaLogo.setTitle("Seleccione el logo de la empresa");
        rutaLogo.getExtensionFilters().addAll(
           new FileChooser.ExtensionFilter("PNG", "*.png"),
           new FileChooser.ExtensionFilter("JPG", "*.jpg"));

        File archivoLogo = rutaLogo.showOpenDialog(null);

        if (archivoLogo != null) {
            this.empresa.setLogo(archivoLogo.getAbsolutePath());
        }
    }

    @FXML
    void guardar(ActionEvent event) {
        guardarInformacion();
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
        eliminarSucursalDeLista(this.sucursal);
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
        tabEmpresas.getSelectionModel().selectFirst();
        jcmbSucEstado.getSelectionModel().selectFirst();
        jtxfLeyenda.requestFocus();
    }

    private void bindEmpresa() {
        jtxfLeyenda.textProperty().bindBidirectional(this.empresa.getLeyendaProperty());
        jtxfDescripcion.textProperty().bindBidirectional(this.empresa.getNombreProperty());
        jtxfTelefono.textProperty().bindBidirectional(this.empresa.getTelefonoProperty());
        jtxfEmail.textProperty().bindBidirectional(this.empresa.getEmailProperty());
        jtxfLogo.textProperty().bindBidirectional(this.empresa.getLogoProperty());
    }

    private void unbindEmpresa() {
        jtxfLeyenda.textProperty().unbindBidirectional(this.empresa.getLeyendaProperty());
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
                traerEmpresa(this.empresa.getCodigo());
                this.listaSucursales.clear();
                tbvSucursales.getItems().clear();
                this.listaSucursales = FXCollections.observableArrayList(this.empresa.getSucursales());
                bindListaSucursales();
                addListenerTableSucursales(tbvSucursales);
                nuevaSucursal();
                bindSucursal();
                bindEmpresa();
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
        Resultado<Empresa> empr = EmpresaDao.getInstance().findEmpresaByCodigo(codigo);
        if (empr.getResultado().equals(TipoResultado.ERROR)) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR,
               "Buscar empresa", empr.getMensaje());
            return;
        }
        if (empr.get() != null) {
            if (empr.get().getCodigo() != null) {
                this.empresa = empr.get();
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
        if (nueva != null) {
            if (nueva.getEmpresa() == null) {
                nueva.setEmpresa(this.empresa);
            }
            Resultado<String> result = EmpresaDao.getInstance().saveSucursal(nueva);
            if (result.getResultado().equals(TipoResultado.SUCCESS)) {
                if (!this.listaSucursales.contains(nueva)) {
                    this.listaSucursales.add(nueva);
                } else {
                    this.listaSucursales.set(this.listaSucursales.indexOf(nueva), nueva);
                }
                tbvSucursales.refresh();
            } else {
                AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Error al guardar sucursal", result.getMensaje());
            }
        }
    }

    private void eliminarSucursalDeLista(Sucursal sucursal) {
        if (sucursal != null) {
            Resultado<String> result = EmpresaDao.getInstance().deleteSucursal(this.sucursal);
            if (result.getResultado().equals(TipoResultado.SUCCESS)) {
                if (this.listaSucursales.contains(sucursal)) {
                    this.listaSucursales.remove(sucursal);
                }
                tbvSucursales.refresh();
            } else {
                AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Error al eliminar sucursal", result.getMensaje());
            }
        }
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
        if (tabEmpresas.getSelectionModel().getSelectedItem().getText().toLowerCase().equals("empresas")) {
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

            this.jtxfLeyenda.requestFocus();
        } else {
            unbindSucursal();
            nuevaSucursal();
            bindSucursal();
            jcmbSucEstado.requestFocus();
            jtxfSucDescripcion.requestFocus();
        }
    }

    private void guardarInformacion() {
        if (tabEmpresas.getSelectionModel().getSelectedItem().getText().toLowerCase().equals("empresas")) {
            if (this.empresa.getNombre() == null || this.empresa.getNombre().isEmpty()) {
                AppWindowController.getInstance().mensaje(Alert.AlertType.INFORMATION, "Información incompleta", "Debe definir al menos la descripción de la empresa..");
                return;
            }
        } else {
            if (this.sucursal != null && (this.sucursal.getNombre() == null || this.sucursal.getNombre().isEmpty())) {
                AppWindowController.getInstance().mensaje(Alert.AlertType.INFORMATION, "Información incompleta", "Debe definir al menos la descripción de la sucursal.");
                return;
            }
        }

        EmpresaDao.getInstance().setEmpresa(this.empresa);
        Resultado<Empresa> resultado = EmpresaDao.getInstance().save();

        if (resultado.getResultado().equals(TipoResultado.ERROR)) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Guardar empresa", resultado.getMensaje());
            return;
        }
        agregarEmpresaALista(resultado.get());
        AppWindowController.getInstance().mensaje(Alert.AlertType.INFORMATION, "Guardar empresa", resultado.getMensaje());
    }

}
