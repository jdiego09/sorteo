/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javax.xml.bind.annotation.XmlTransient;
import sorteo.model.dao.UsuarioDao;
import sorteo.model.entities.Roles;
import sorteo.model.entities.RolXUsuario;
import sorteo.model.entities.Usuario;
import sorteo.utils.Aplicacion;
import sorteo.utils.AppWindowController;
import sorteo.utils.Encriptor;
import sorteo.utils.GenValorCombo;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

/**
 *
 * @author Luis Diego
 */
public class UsuarioController extends Controller implements Initializable {

    private Usuario usuario;

    @FXML
    private AnchorPane apRoot;

    @FXML
    private JFXButton jbtnSalir, jbtnAgregarRol, jbtnEliminarRol;

    @FXML
    private Button btnLimpiar, btnGuardaUsuario;

    @FXML
    private TextField jtxfCodigo, jtxfDescripcion;

    @FXML
    private ComboBox<GenValorCombo> jcmbEstado;

    @FXML
    private TableView<Usuario> tbvUsuarios;

    @FXML
    private TableColumn<Usuario, String> tbcCodigo, tbcDescipcion, tbcEstado;

    @FXML
    private TableView<Roles> tbvRolesActivos;

    @FXML
    private TableColumn<Roles, String> tbcCodRolActivo;

    @FXML
    private TableView<RolXUsuario> tbvRolesUsuario;

    @FXML
    private TableColumn<RolXUsuario, String> tbcCodRolUsuario;

    @XmlTransient
    ObservableList<GenValorCombo> estados = FXCollections
       .observableArrayList();

    @XmlTransient
    private ObservableList<Usuario> usuariosSistema = FXCollections
       .observableArrayList();

    @XmlTransient
    private ObservableList<Roles> rolesActivos = FXCollections
       .observableArrayList();

    @XmlTransient
    private ObservableList<RolXUsuario> rolesUsuario = FXCollections
       .observableArrayList();

    @Override
    public void initialize() {
        init();
    }

    private void nuevoUsuario() {
        this.usuario = new Usuario();
        this.usuario.setRolXUsuarioList(new ArrayList<>());
        unbindUsuario();
    }

    private void init() {
        this.estados.clear();
        this.estados.add(new GenValorCombo("A", "Activo"));
        this.estados.add(new GenValorCombo("I", "Inactivo"));
        jcmbEstado.setItems(this.estados);
        if (this.usuario != null) {
            unbindUsuario();
        }
        nuevoUsuario();
        bindUsuario();

        cargarUsuarios();
        bindListaUsuarios();
        addListenerTableUsuarios(tbvUsuarios);

        jcmbEstado.getSelectionModel().selectFirst();
        jtxfCodigo.requestFocus();
    }

    private void bindUsuario() {
        jtxfCodigo.textProperty().bindBidirectional(this.usuario.getUsuCodigoProperty());
        jtxfDescripcion.textProperty().bindBidirectional(this.usuario.getUsuDescripcionProperty());
        jcmbEstado.valueProperty().bindBidirectional(this.usuario.getUsuEstadoProperty());
    }

    private void unbindUsuario() {
        jtxfCodigo.textProperty().unbindBidirectional(this.usuario.getUsuCodigoProperty());
        jtxfDescripcion.textProperty().unbindBidirectional(this.usuario.getUsuDescripcionProperty());
        jcmbEstado.valueProperty().unbindBidirectional(this.usuario.getUsuEstadoProperty());
    }

    private void addListenerTableUsuarios(TableView table) {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            unbindUsuario();
            if (newSelection != null) {
                this.usuario = (Usuario) newSelection;
                this.rolesActivos.clear();
                tbvRolesActivos.getItems().clear();
                Resultado<ArrayList<Roles>> rolesSistema = UsuarioDao.getInstance().getRolesNoAsignados(this.usuario.getUsuCodigo());
                rolesSistema.get().stream().forEach(this.rolesActivos::add);
                bindListaRolesActivos();

                Resultado<ArrayList<RolXUsuario>> roles = UsuarioDao.getInstance().getRolesUsuario(this.usuario.getUsuCodigo());
                this.rolesUsuario.clear();
                tbvRolesUsuario.getItems().clear();
                roles.get().stream().forEach(this.rolesUsuario::add);
                bindListaRolesUsuario();
                jtxfCodigo.setDisable(true);
            }
            bindUsuario();
            jcmbEstado.requestFocus();
            jtxfDescripcion.requestFocus();
            jtxfCodigo.requestFocus();
        });
    }

    private void bindListaUsuarios() {
        if (this.usuariosSistema != null) {
            tbvUsuarios.setItems(this.usuariosSistema);
            tbvUsuarios.refresh();
        }
        tbcCodigo.setCellValueFactory(new PropertyValueFactory<>("usuCodigo"));
        tbcDescipcion.setCellValueFactory(new PropertyValueFactory<>("usuDescripcion"));
        tbcEstado.setCellValueFactory(new PropertyValueFactory<>("descripcionEstado"));
    }

    private void bindListaRolesActivos() {
        if (this.rolesActivos != null) {
            tbvRolesActivos.setItems(this.rolesActivos);
            tbvRolesActivos.refresh();
        }
        tbcCodRolActivo.setCellValueFactory(r -> r.getValue().getRolCodigoProperty());
    }

    private void bindListaRolesUsuario() {
        if (this.rolesUsuario != null) {
            tbvRolesUsuario.setItems(this.rolesUsuario);
            tbvRolesUsuario.refresh();
        }
        tbcCodRolUsuario.setCellValueFactory(b -> b.getValue().getRxuCodrol().getRolCodigoProperty());
    }

    private void traerUsuario(String codigo) {
        unbindUsuario();
        Resultado<Usuario> user = UsuarioDao.getInstance().getUsuarioByCodigo(codigo);
        if (user.getResultado().equals(TipoResultado.ERROR)) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Buscar usuario", user.getMensaje());
            return;
        }
        if (user.get() != null) {
            if (user.get().getUsuCodigo() != null) {
                this.usuario = user.get();
                jtxfCodigo.setDisable(true);
                // Se cargan los roles del usuario.
                Resultado<ArrayList<RolXUsuario>> roles = UsuarioDao.getInstance().getRolesUsuario(codigo);
                this.rolesUsuario.clear();
                tbvRolesUsuario.getItems().clear();
                roles.get().stream().forEach(this.rolesUsuario::add);
            }
        }
    }

    private void agregarUsuarioSistemaALista(Usuario usuarioNuevo) {
        if (!this.usuariosSistema.contains(usuarioNuevo)) {
            this.usuariosSistema.add(usuarioNuevo);
        } else {
            this.usuariosSistema.set(this.usuariosSistema.indexOf(usuarioNuevo), usuarioNuevo);
        }
        tbvUsuarios.refresh();
    }

    @FXML
    private void guardarUsuario(ActionEvent event) {
        this.usuario.setUsuCodsucursal(Aplicacion.getInstance().getSucursalDefault());
        if (this.usuario.getUsuContrasena() == null || this.usuario.getUsuContrasena().isEmpty()) {
            this.usuario.setUsuContrasena(Encriptor.getInstance().encriptar(this.usuario.getUsuCodigo()));
        }
        this.usuario.setRolXUsuarioList(this.rolesUsuario);

        UsuarioDao.getInstance().setUsuarioSistema(this.usuario);
        Resultado<Usuario> resultado = UsuarioDao.getInstance().save();

        if (resultado.getResultado().equals(TipoResultado.ERROR)) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Guardar usuario", resultado.getMensaje());
            return;
        }
        AppWindowController.getInstance().mensaje(Alert.AlertType.INFORMATION, "Guardar usuario", resultado.getMensaje());
        agregarUsuarioSistemaALista(resultado.get());
    }

    @FXML
    void codigoUsuarioOnEnterKey(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.TAB)) {
            if (this.usuario.getUsuCodigo() != null) {
                traerUsuario(this.usuario.getUsuCodigo());
            }
        }
    }

    @FXML
    void regresar(ActionEvent event) {
        clearForm();
        AppWindowController.getInstance().goHome();
    }

    @FXML
    private void limpiar() {
        clearForm();
    }

    @FXML
    private void agregarRol() {
        RolXUsuario rol = new RolXUsuario(this.usuario, tbvRolesActivos.getSelectionModel().getSelectedItem());

        if ((!this.usuario.getRolXUsuarioList().stream().filter(d -> d.getRxuCodrol().getRolCodigo().equals(rol.getRxuCodrol().getRolCodigo())).findFirst().isPresent())) {
            this.rolesUsuario.add(rol);
            tbvRolesUsuario.refresh();
        } else {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Agregar rol", "El usuario [" + this.usuario.getUsuCodigo() + "] ya tiene el rol asignado.");
        }
    }

    @FXML
    private void eliminarRol() {

        RolXUsuario rol = tbvRolesUsuario.getSelectionModel().getSelectedItem();

        if (rol.getRxuCodigo() != null && rol.getRxuCodigo() > 0) {
            Resultado<String> resultado = UsuarioDao.getInstance().deleteRol(rol);

            if (resultado.getResultado().equals(TipoResultado.SUCCESS)) {
                this.usuario.getRolXUsuarioList().remove(rol);
                this.rolesUsuario.remove(rol);
                tbvRolesUsuario.refresh();
            } else {
                AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Eliminar rol", resultado.getMensaje());
            }

        } else {
            this.usuario.getRolXUsuarioList().remove(rol);
            this.rolesUsuario.remove(rol);
            tbvRolesUsuario.refresh();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

    private void cargarUsuarios() {
        Resultado<ArrayList<Usuario>> usuarios = UsuarioDao.getInstance().findUsuariosSistema();
        if (!usuarios.getResultado().equals(TipoResultado.ERROR)) {
            this.usuariosSistema = FXCollections.observableArrayList(usuarios.get());
        } else {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Cargar usuarios", usuarios.getMensaje());
        }
    }

    private void clearForm() {
        unbindUsuario();
        this.rolesUsuario.clear();
        nuevoUsuario();
        bindUsuario();
        this.jtxfCodigo.setDisable(false);
        this.jtxfCodigo.requestFocus();
    }

}
