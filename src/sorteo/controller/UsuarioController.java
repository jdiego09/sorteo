/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javax.xml.bind.annotation.XmlTransient;
import sorteo.model.dao.UsuarioDao;
import sorteo.model.entities.Roles;
import sorteo.model.entities.RolXUsuario;
import sorteo.model.entities.Usuario;
import sorteo.utils.AppWindowController;
import sorteo.utils.GenValorCombo;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

/**
 *
 * @author Luis Diego
 */
public class UsuarioController extends Controller {

    private Usuario usuario;

    @FXML
    private AnchorPane apRoot;

    @FXML
    private JFXButton jbtnSalir, jbtnAgregarRol, jbtnEliminarRol;

    @FXML
    private Button btnLimpiar, btnGuardaUsuario;

    @FXML
    private JFXTextField jtxfCodigo, jtxfDescripcion;

    @FXML
    private JFXComboBox<GenValorCombo> jcmbEstado;

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
        iniciarPantalla();
    }

    private void nuevoUsuarioSistema() {
        this.usuario = new Usuario();
        this.usuario.setRolXUsuarioList(new ArrayList<>());
    }

    private void iniciarPantalla() {

        nuevoUsuarioSistema();

        this.estados.clear();
        this.estados.add(new GenValorCombo("A", "Activo"));
        this.estados.add(new GenValorCombo("I", "Inactivo"));
        jcmbEstado.setItems(this.estados);

        if (this.usuario != null) {
            unbindUsuarioSistema();
        }

        bindUsuarioSistema();

        bindListaUsuariosSistema();
        bindListaRolesActivos();
        bindListaRolesUsuario();

        jtxfCodigo.setDisable(false);

        jcmbEstado.getSelectionModel().selectFirst();

        addListenerTableUsuariosSistema(tbvUsuarios);

        this.rolesActivos.clear();
        tbvRolesActivos.getItems().clear();
        Resultado<ArrayList<Roles>> roles = UsuarioDao.getInstance().getRolesNoAsignados(this.usuario.getUsuCodigo());
        roles.get().stream().forEach(this.rolesActivos::add);

        this.usuariosSistema.clear();
        tbvUsuarios.getItems().clear();
        Resultado<ArrayList<Usuario>> usuarios = UsuarioDao.getInstance().findUsuariosSistema();
        usuarios.get().stream().forEach(this.usuariosSistema::add);

    }

    private void bindUsuarioSistema() {
        jtxfCodigo.textProperty().bindBidirectional(this.usuario.getUsuCodigoProperty());
        jtxfDescripcion.textProperty().bindBidirectional(this.usuario.getUsuDescripcionProperty());
        //jcmbEstado.valueProperty().bindBidirectional(this.usuario.getUsuEstadoProperty());
        // jtpfContrasena.textProperty().bindBidirectional(this.usuarioSistema.getUssContrasenaProperty());
    }

    private void unbindUsuarioSistema() {
        jtxfCodigo.textProperty().unbindBidirectional(this.usuario.getUsuCodigoProperty());
        jtxfDescripcion.textProperty().unbindBidirectional(this.usuario.getUsuDescripcionProperty());
        //jcmbEstado.valueProperty().unbindBidirectional(this.usuario.getUsuEstadoProperty());
        // jtpfContrasena.textProperty().unbindBidirectional(this.usuarioSistema.getUssContrasenaProperty());
    }

    private void addListenerTableUsuariosSistema(TableView table) {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {

                unbindUsuarioSistema();

                this.usuario = (Usuario) newSelection;

                Resultado<ArrayList<RolXUsuario>> roles = UsuarioDao.getInstance().getRolesUsuario(this.usuario.getUsuCodigo());
                this.rolesUsuario.clear();
                tbvRolesUsuario.getItems().clear();
                roles.get().stream().forEach(this.rolesUsuario::add);

                bindUsuarioSistema();

            }
        });
    }

    private void bindListaUsuariosSistema() {
        if (this.usuariosSistema != null) {
            tbvUsuarios.setItems(this.usuariosSistema);
            tbvUsuarios.refresh();
        }
        tbcCodigo.setCellValueFactory(new PropertyValueFactory<>("usuCodigo"));
        tbcEstado.setCellValueFactory(new PropertyValueFactory<>("descripcionEstado"));
    }

    private void bindListaRolesActivos() {
        if (this.rolesActivos != null) {
            tbvRolesActivos.setItems(this.rolesActivos);
            tbvRolesActivos.refresh();
        }
        tbcCodRolActivo.setCellValueFactory(new PropertyValueFactory<>("rolCodigo"));
    }

    private void bindListaRolesUsuario() {
        if (this.rolesUsuario != null) {
            tbvRolesUsuario.setItems(this.rolesUsuario);
            tbvRolesUsuario.refresh();
        }
        tbcCodRolUsuario.setCellValueFactory(b -> b.getValue().getRxuCodrol().getRolCodigoProperty());
    }

    private void traerUsuario(String codigo) {

        unbindUsuarioSistema();

        Resultado<Usuario> usuario = UsuarioDao.getInstance().getUsuarioByCodigo(codigo);
        if (usuario.getResultado().equals(TipoResultado.ERROR)) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Buscar usuario", usuario.getMensaje());
            return;
        }
        if (usuario.get() != null) {
            if (usuario.get().getUsuCodigo() != null) {
                this.usuario = usuario.get();
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
    private void guardarUsuarioSistema(ActionEvent event) {
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
        AppWindowController.getInstance().goHome();
    }

    @FXML
    private void limpiarUsuario() {
        unbindUsuarioSistema();
        this.rolesUsuario.clear();
        nuevoUsuarioSistema();
        bindUsuarioSistema();
        this.jtxfCodigo.requestFocus();
    }

    @FXML
    private void agregarRolUsuario() {

        RolXUsuario rol = new RolXUsuario(this.usuario, tbvRolesActivos.getSelectionModel().getSelectedItem());

        if (!this.usuario.getRolXUsuarioList().contains(rol)) {
            this.usuario.getRolXUsuarioList().add(rol);
            this.rolesUsuario.add(rol);
            tbvRolesUsuario.refresh();
        } else {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Agregar rol", "El usuario [" + this.usuario.getUsuCodigo() + "] ya tiene el rol asignado.");
        }

    }

    @FXML
    private void eliminarRolUsuario() {

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

}
