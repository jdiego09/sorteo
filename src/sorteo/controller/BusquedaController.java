/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import sorteo.model.dao.UsuarioDao;
import sorteo.model.entities.Sucursal;
import sorteo.model.entities.Usuario;
import sorteo.utils.Aplicacion;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

/**
 * FXML Controller class
 *
 * @author jdiego
 */
public class BusquedaController extends Controller implements Initializable {

    //BikPersona personaSeleccionada;
    private static HashMap<String, Object> filtros = new HashMap<>();

    @FXML
    private Label lblTitulo;
    @FXML
    private JFXButton jbtnCancelar;
    @FXML
    private Pane pnlCriterios;
    @FXML
    private JFXButton jbtnLimpiar;
    @FXML
    private BorderPane root;
    @FXML
    private Pane pnlResultados;
    @FXML
    private JFXButton jbtnFiltrar;
    @FXML
    private TableView tbvResultados;
    @FXML
    private JFXButton jbtnAceptar;

    private Object resultado;
    private EventHandler<KeyEvent> keyEnter;
    private Boolean pressEnter = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        keyEnter = (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                pressEnter = true;
                jbtnFiltrar.fire();
                pressEnter = false;

                //sendTabEvent(event);
            } else if (event.getCode() == KeyCode.ESCAPE) {
                //cerrar();
            }
        };

        resultado = null;
    }

    @Override
    public void initialize() {
        Platform.runLater(() -> {
            for (Node object : pnlCriterios.getChildren()) {
                if (object.isFocusTraversable()) {
                    object.requestFocus();
                    break;
                }
            }
        });
        resultado = null;
    }

    public Object getResultado() {
        return resultado;
    }

    @FXML
    private void regresar(ActionEvent event) {
        closeWindow();
    }
/*
    public void busquedaPersonas() {
        lblTitulo.setText("Búsqueda de personas");
        ArrayList<JFXTextField> criterios = new ArrayList<>();

        JFXTextField jtxfCedula = new JFXTextField();
        JFXTextField jtxfNombre = new JFXTextField();
        JFXTextField jtxfPrimerApellido = new JFXTextField();
        JFXTextField jtxfSegundoApellido = new JFXTextField();
        jtxfCedula.getStyleClass().add("edittext");
        jtxfCedula.setId("cedula");
        jtxfCedula.setText("%");
        jtxfCedula.setPromptText("Cédula");
        jtxfCedula.setLabelFloat(true);
        jtxfCedula.prefWidth(210);
        jtxfCedula.prefHeight(35);
        jtxfCedula.setLayoutX(8);
        jtxfCedula.setLayoutY(20);
        criterios.add(jtxfCedula);

        jtxfNombre.getStyleClass().add("edittext");
        jtxfNombre.setId("nombre");
        jtxfNombre.setPromptText("Nombre");
        jtxfNombre.setText("%");
        jtxfNombre.setLabelFloat(true);
        jtxfNombre.prefWidth(210);
        jtxfNombre.prefHeight(35);
        jtxfNombre.setLayoutX(8);
        jtxfNombre.setLayoutY(80);
        criterios.add(jtxfNombre);

        jtxfPrimerApellido.getStyleClass().add("edittext");
        jtxfPrimerApellido.setId("apellido1");
        jtxfPrimerApellido.setPromptText("Primer apellido");
        jtxfPrimerApellido.setText("%");
        jtxfPrimerApellido.setLabelFloat(true);
        jtxfPrimerApellido.prefWidth(210);
        jtxfPrimerApellido.prefHeight(35);
        jtxfPrimerApellido.setLayoutX(8);
        jtxfPrimerApellido.setLayoutY(140);
        criterios.add(jtxfPrimerApellido);

        jtxfSegundoApellido.getStyleClass().add("edittext");
        jtxfSegundoApellido.setId("apellido2");
        jtxfSegundoApellido.setPromptText("Segundo apellido");
        jtxfSegundoApellido.setText("%");
        jtxfSegundoApellido.setLabelFloat(true);
        jtxfSegundoApellido.prefWidth(210);
        jtxfSegundoApellido.prefHeight(35);
        jtxfSegundoApellido.setLayoutX(8);
        jtxfSegundoApellido.setLayoutY(200);
        criterios.add(jtxfSegundoApellido);
        pnlCriterios.getChildren().clear();
        criterios.stream().forEach(pnlCriterios.getChildren()::add);

        TableColumn<BikPersona, String> tbcCedula = new TableColumn<>("Cédula");
        tbcCedula.setPrefWidth(100);
        tbcCedula.setCellValueFactory(cd -> cd.getValue().getPerCedulaProperty());
        TableColumn<BikPersona, String> tbcNombre = new TableColumn<>("Nombre");
        tbcNombre.setPrefWidth(400);
        tbcNombre.setCellValueFactory(cd -> cd.getValue().getNombreCompletoProperty());
        tbvResultados.getColumns().clear();
        tbvResultados.getItems().clear();
        tbvResultados.getColumns().add(tbcCedula);
        tbvResultados.getColumns().add(tbcNombre);
        tbvResultados.refresh();

        tbvResultados.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                this.resultado = newSelection;
            }
        });

        setBuscaPersonasListener(jtxfCedula);
        setBuscaPersonasListener(jtxfNombre);
        setBuscaPersonasListener(jtxfPrimerApellido);
        setBuscaPersonasListener(jtxfSegundoApellido);

        jbtnFiltrar.setOnAction((ActionEvent event) -> {
            filtros.clear();
            filtros.put(jtxfCedula.getId(), jtxfCedula.getText());
            filtros.put(jtxfNombre.getId(), jtxfNombre.getText());
            filtros.put(jtxfPrimerApellido.getId(), jtxfPrimerApellido.getText());
            filtros.put(jtxfSegundoApellido.getId(), jtxfSegundoApellido.getText());
            buscarPersonas();
        });
        jbtnLimpiar.setOnAction((ActionEvent event) -> {
            filtros.clear();
            jtxfCedula.setText("%");
            jtxfNombre.setText("%");
            jtxfPrimerApellido.setText("%");
            jtxfSegundoApellido.setText("%");
            buscarPersonas();
        });
    }

    private void buscarPersonas() {
        if (filtros.get("cedula") == null) {
            filtros.put("cedula", "");
        }
        if (filtros.get("nombre") == null) {
            filtros.put("nombre", "");
        }
        if (filtros.get("apellido1") == null) {
            filtros.put("apellido1", "");
        }
        if (filtros.get("apellido2") == null) {
            filtros.put("apellido2", "");
        }
        Resultado<ArrayList<BikPersona>> personas = PersonaDao.getInstance().getPersonasFiltro(filtros.get("cedula").toString(), filtros.get("nombre").toString(), filtros.get("apellido1").toString(), filtros.get("apellido2").toString());
        if (personas.getResultado().equals(TipoResultado.SUCCESS)) {
            tbvResultados.setItems(FXCollections.observableArrayList(personas.get()));
            tbvResultados.refresh();
        }
    }

    public void busquedaUsuarios() {
        lblTitulo.setText("Búsqueda de usuarios");
        ArrayList<JFXTextField> criterios = new ArrayList<>();

        JFXTextField jtxfCedula = new JFXTextField();
        JFXTextField jtxfNombre = new JFXTextField();
        JFXTextField jtxfPrimerApellido = new JFXTextField();
        JFXTextField jtxfSegundoApellido = new JFXTextField();
        jtxfCedula.getStyleClass().add("edittext");
        jtxfCedula.setId("cedula");
        jtxfCedula.setText("%");
        jtxfCedula.setPromptText("Cédula");
        jtxfCedula.setLabelFloat(true);
        jtxfCedula.prefWidth(210);
        jtxfCedula.prefHeight(35);
        jtxfCedula.setLayoutX(8);
        jtxfCedula.setLayoutY(20);
        criterios.add(jtxfCedula);

        jtxfNombre.getStyleClass().add("edittext");
        jtxfNombre.setId("nombre");
        jtxfNombre.setPromptText("Nombre");
        jtxfNombre.setText("%");
        jtxfNombre.setLabelFloat(true);
        jtxfNombre.prefWidth(210);
        jtxfNombre.prefHeight(35);
        jtxfNombre.setLayoutX(8);
        jtxfNombre.setLayoutY(80);
        criterios.add(jtxfNombre);

        jtxfPrimerApellido.getStyleClass().add("edittext");
        jtxfPrimerApellido.setId("apellido1");
        jtxfPrimerApellido.setPromptText("Primer apellido");
        jtxfPrimerApellido.setText("%");
        jtxfPrimerApellido.setLabelFloat(true);
        jtxfPrimerApellido.prefWidth(210);
        jtxfPrimerApellido.prefHeight(35);
        jtxfPrimerApellido.setLayoutX(8);
        jtxfPrimerApellido.setLayoutY(140);
        criterios.add(jtxfPrimerApellido);

        jtxfSegundoApellido.getStyleClass().add("edittext");
        jtxfSegundoApellido.setId("apellido2");
        jtxfSegundoApellido.setPromptText("Segundo apellido");
        jtxfSegundoApellido.setText("%");
        jtxfSegundoApellido.setLabelFloat(true);
        jtxfSegundoApellido.prefWidth(210);
        jtxfSegundoApellido.prefHeight(35);
        jtxfSegundoApellido.setLayoutX(8);
        jtxfSegundoApellido.setLayoutY(200);
        criterios.add(jtxfSegundoApellido);
        pnlCriterios.getChildren().clear();
        criterios.stream().forEach(pnlCriterios.getChildren()::add);

        TableColumn<BikPersona, String> tbcCedula = new TableColumn<>("Cédula");
        tbcCedula.setPrefWidth(100);
        tbcCedula.setCellValueFactory(cd -> cd.getValue().getPerCedulaProperty());
        TableColumn<BikPersona, String> tbcNombre = new TableColumn<>("Nombre");
        tbcNombre.setPrefWidth(400);
        tbcNombre.setCellValueFactory(cd -> cd.getValue().getNombreCompletoProperty());
        tbvResultados.getColumns().clear();
        tbvResultados.getItems().clear();
        tbvResultados.getColumns().add(tbcCedula);
        tbvResultados.getColumns().add(tbcNombre);
        tbvResultados.refresh();

        tbvResultados.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                this.resultado = newSelection;
            }
        });

        setBuscaUsuariosListener(jtxfCedula);
        setBuscaUsuariosListener(jtxfNombre);
        setBuscaUsuariosListener(jtxfPrimerApellido);
        setBuscaUsuariosListener(jtxfSegundoApellido);

        jbtnFiltrar.setOnAction((ActionEvent event) -> {
            filtros.clear();
            filtros.put(jtxfCedula.getId(), jtxfCedula.getText());
            filtros.put(jtxfNombre.getId(), jtxfNombre.getText());
            filtros.put(jtxfPrimerApellido.getId(), jtxfPrimerApellido.getText());
            filtros.put(jtxfSegundoApellido.getId(), jtxfSegundoApellido.getText());
            buscarUsuarios();
        });
        jbtnLimpiar.setOnAction((ActionEvent event) -> {
            filtros.clear();
            jtxfCedula.setText("%");
            jtxfNombre.setText("%");
            jtxfPrimerApellido.setText("%");
            jtxfSegundoApellido.setText("%");
            buscarUsuarios();
        });
    }

    private void buscarUsuarios() {
        if (filtros.get("cedula") == null) {
            filtros.put("cedula", "");
        }
        if (filtros.get("nombre") == null) {
            filtros.put("nombre", "");
        }
        if (filtros.get("apellido1") == null) {
            filtros.put("apellido1", "");
        }
        if (filtros.get("apellido2") == null) {
            filtros.put("apellido2", "");
        }
        Resultado<ArrayList<BikPersona>> usuarios = UsuarioDao.getInstance().getUsuarioFiltro(filtros.get("cedula").toString(), filtros.get("nombre").toString(), filtros.get("apellido1").toString(), filtros.get("apellido2").toString());
        if (usuarios.getResultado().equals(TipoResultado.SUCCESS)) {
            tbvResultados.setItems(FXCollections.observableArrayList(usuarios.get()));
            tbvResultados.refresh();
        }
    }

    private void setBuscaUsuariosListener(TextField textfield) {
        textfield.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                filtros.put(textfield.getId(), textfield.getText());
                buscarUsuarios();
            }
        });
    }

    public void busquedaFuncionarios() {
        lblTitulo.setText("Búsqueda de funcionarios");
        ArrayList<JFXTextField> criterios = new ArrayList<>();

        JFXTextField jtxfCedula = new JFXTextField();
        JFXTextField jtxfNombre = new JFXTextField();
        JFXTextField jtxfPrimerApellido = new JFXTextField();
        JFXTextField jtxfSegundoApellido = new JFXTextField();
        jtxfCedula.getStyleClass().add("edittext");
        jtxfCedula.setId("cedula");
        jtxfCedula.setText("%");
        jtxfCedula.setPromptText("Cédula");
        jtxfCedula.setLabelFloat(true);
        jtxfCedula.prefWidth(210);
        jtxfCedula.prefHeight(35);
        jtxfCedula.setLayoutX(8);
        jtxfCedula.setLayoutY(20);
        criterios.add(jtxfCedula);

        jtxfNombre.getStyleClass().add("edittext");
        jtxfNombre.setId("nombre");
        jtxfNombre.setPromptText("Nombre");
        jtxfNombre.setText("%");
        jtxfNombre.setLabelFloat(true);
        jtxfNombre.prefWidth(210);
        jtxfNombre.prefHeight(35);
        jtxfNombre.setLayoutX(8);
        jtxfNombre.setLayoutY(80);
        criterios.add(jtxfNombre);

        jtxfPrimerApellido.getStyleClass().add("edittext");
        jtxfPrimerApellido.setId("apellido1");
        jtxfPrimerApellido.setPromptText("Primer apellido");
        jtxfPrimerApellido.setText("%");
        jtxfPrimerApellido.setLabelFloat(true);
        jtxfPrimerApellido.prefWidth(210);
        jtxfPrimerApellido.prefHeight(35);
        jtxfPrimerApellido.setLayoutX(8);
        jtxfPrimerApellido.setLayoutY(140);
        criterios.add(jtxfPrimerApellido);

        jtxfSegundoApellido.getStyleClass().add("edittext");
        jtxfSegundoApellido.setId("apellido2");
        jtxfSegundoApellido.setPromptText("Segundo apellido");
        jtxfSegundoApellido.setText("%");
        jtxfSegundoApellido.setLabelFloat(true);
        jtxfSegundoApellido.prefWidth(210);
        jtxfSegundoApellido.prefHeight(35);
        jtxfSegundoApellido.setLayoutX(8);
        jtxfSegundoApellido.setLayoutY(200);
        criterios.add(jtxfSegundoApellido);
        pnlCriterios.getChildren().clear();
        criterios.stream().forEach(pnlCriterios.getChildren()::add);

        TableColumn<BikPersona, String> tbcCedula = new TableColumn<>("Cédula");
        tbcCedula.setPrefWidth(100);
        tbcCedula.setCellValueFactory(cd -> cd.getValue().getPerCedulaProperty());
        TableColumn<BikPersona, String> tbcNombre = new TableColumn<>("Nombre");
        tbcNombre.setPrefWidth(400);
        tbcNombre.setCellValueFactory(cd -> cd.getValue().getNombreCompletoProperty());
        tbvResultados.getColumns().clear();
        tbvResultados.getItems().clear();
        tbvResultados.getColumns().add(tbcCedula);
        tbvResultados.getColumns().add(tbcNombre);
        tbvResultados.refresh();

        tbvResultados.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                this.resultado = newSelection;
            }
        });

        setBuscaFuncionariosListener(jtxfCedula);
        setBuscaFuncionariosListener(jtxfNombre);
        setBuscaFuncionariosListener(jtxfPrimerApellido);
        setBuscaFuncionariosListener(jtxfSegundoApellido);

        jbtnFiltrar.setOnAction((ActionEvent event) -> {
            filtros.clear();
            filtros.put(jtxfCedula.getId(), jtxfCedula.getText());
            filtros.put(jtxfNombre.getId(), jtxfNombre.getText());
            filtros.put(jtxfPrimerApellido.getId(), jtxfPrimerApellido.getText());
            filtros.put(jtxfSegundoApellido.getId(), jtxfSegundoApellido.getText());
            buscarFuncionarios();
        });
        jbtnLimpiar.setOnAction((ActionEvent event) -> {
            filtros.clear();
            jtxfCedula.setText("%");
            jtxfNombre.setText("%");
            jtxfPrimerApellido.setText("%");
            jtxfSegundoApellido.setText("%");
            buscarFuncionarios();
        });
    }

    private void buscarFuncionarios() {
        if (filtros.get("cedula") == null) {
            filtros.put("cedula", "");
        }
        if (filtros.get("nombre") == null) {
            filtros.put("nombre", "");
        }
        if (filtros.get("apellido1") == null) {
            filtros.put("apellido1", "");
        }
        if (filtros.get("apellido2") == null) {
            filtros.put("apellido2", "");
        }
        Resultado<ArrayList<BikPersona>> funcionarios = FuncionarioDao.getInstance().getFuncionarioFiltro(filtros.get("cedula").toString(), filtros.get("nombre").toString(), filtros.get("apellido1").toString(), filtros.get("apellido2").toString());
        if (funcionarios.getResultado().equals(TipoResultado.SUCCESS)) {
            tbvResultados.setItems(FXCollections.observableArrayList(funcionarios.get()));
            tbvResultados.refresh();
        }
    }

    private void setBuscaFuncionariosListener(TextField textfield) {
        textfield.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                filtros.put(textfield.getId(), textfield.getText());
                buscarFuncionarios();
            }
        });
    }

    private void setBuscaPersonasListener(TextField textfield) {
        textfield.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                filtros.put(textfield.getId(), textfield.getText());
                buscarPersonas();
            }
        });
    }

    public void busquedaPuestos() {
        lblTitulo.setText("Búsqueda de puestos");
        ArrayList<JFXTextField> criterios = new ArrayList<>();

        JFXTextField jtxfNombre = new JFXTextField();
        
        jtxfNombre.getStyleClass().add("edittext");
        jtxfNombre.setId("nombre");
        jtxfNombre.setPromptText("Descripción");
        jtxfNombre.setText("%");
        jtxfNombre.setLabelFloat(true);
        jtxfNombre.prefWidth(210);
        jtxfNombre.prefHeight(35);
        jtxfNombre.setLayoutX(8);
        jtxfNombre.setLayoutY(20);
        criterios.add(jtxfNombre);

        pnlCriterios.getChildren().clear();
        criterios.stream().forEach(pnlCriterios.getChildren()::add);

        TableColumn<BikPuesto, String> tbcCodigo = new TableColumn<>("Código");
        tbcCodigo.setPrefWidth(100);
        tbcCodigo.setCellValueFactory(cd -> cd.getValue().getCodigoStringProperty());
        TableColumn<BikPuesto, String> tbcNombre = new TableColumn<>("Descripción");
        tbcNombre.setPrefWidth(400);
        tbcNombre.setCellValueFactory(cd -> cd.getValue().getPueDescripcionProperty());
        tbvResultados.getColumns().clear();
        tbvResultados.getItems().clear();
        tbvResultados.getColumns().add(tbcCodigo);
        tbvResultados.getColumns().add(tbcNombre);
        tbvResultados.refresh();

        tbvResultados.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                this.resultado = newSelection;
            }
        });

        setBuscaPuestosListener(jtxfNombre);

        jbtnFiltrar.setOnAction((ActionEvent event) -> {
            filtros.clear();
            filtros.put(jtxfNombre.getId(), jtxfNombre.getText());
            buscarPuestos();
        });
        jbtnLimpiar.setOnAction((ActionEvent event) -> {
            filtros.clear();
            jtxfNombre.setText("%");
            buscarPuestos();
        });
    }

    private void buscarPuestos() {
        if (filtros.get("nombre") == null) {
            filtros.put("nombre", "");
        }
        Resultado<ArrayList<BikPuesto>> puestos = FuncionarioDao.getInstance().getPuestoFiltro(filtros.get("nombre").toString());
        if (puestos.getResultado().equals(TipoResultado.SUCCESS)) {
            tbvResultados.setItems(FXCollections.observableArrayList(puestos.get()));
            tbvResultados.refresh();
        }
    }

    private void setBuscaPuestosListener(TextField textfield) {
        textfield.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                filtros.put(textfield.getId(), textfield.getText());
                buscarPuestos();
            }
        });
    }

    public void busquedaSedes() {
        lblTitulo.setText("Búsqueda de sedes");
        ArrayList<JFXTextField> criterios = new ArrayList<>();

        JFXTextField jtxfNombre = new JFXTextField();
        
        jtxfNombre.getStyleClass().add("edittext");
        jtxfNombre.setId("nombre");
        jtxfNombre.setPromptText("Descripción");
        jtxfNombre.setText("%");
        jtxfNombre.setLabelFloat(true);
        jtxfNombre.prefWidth(210);
        jtxfNombre.prefHeight(35);
        jtxfNombre.setLayoutX(8);
        jtxfNombre.setLayoutY(20);
        criterios.add(jtxfNombre);

        pnlCriterios.getChildren().clear();
        criterios.stream().forEach(pnlCriterios.getChildren()::add);

        TableColumn<BikSede, String> tbcCodigo = new TableColumn<>("Código");
        tbcCodigo.setPrefWidth(100);
        tbcCodigo.setCellValueFactory(cd -> cd.getValue().getCodigoStringProperty());
        TableColumn<BikSede, String> tbcNombre = new TableColumn<>("Descripción");
        tbcNombre.setPrefWidth(400);
        tbcNombre.setCellValueFactory(cd -> cd.getValue().getSedDescripcionProperty());
        tbvResultados.getColumns().clear();
        tbvResultados.getItems().clear();
        tbvResultados.getColumns().add(tbcCodigo);
        tbvResultados.getColumns().add(tbcNombre);
        tbvResultados.refresh();

        tbvResultados.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                this.resultado = newSelection;
            }
        });

        setBuscaSedesListener(jtxfNombre);

        jbtnFiltrar.setOnAction((ActionEvent event) -> {
            filtros.clear();
            filtros.put(jtxfNombre.getId(), jtxfNombre.getText());
            buscarSedes();
        });
        jbtnLimpiar.setOnAction((ActionEvent event) -> {
            filtros.clear();
            jtxfNombre.setText("%");
            buscarSedes();
        });
    }

    private void buscarSedes() {
        if (filtros.get("nombre") == null) {
            filtros.put("nombre", "");
        }
        Resultado<ArrayList<Sucursal>> sucursales = FuncionarioDao.getInstance().getSedeFiltro(filtros.get("nombre").toString());
        if (sucursales.getResultado().equals(TipoResultado.SUCCESS)) {
            tbvResultados.setItems(FXCollections.observableArrayList(sucursales.get()));
            tbvResultados.refresh();
        }
    }

    private void setBuscaSucursalesListener(TextField textfield) {
        textfield.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                filtros.put(textfield.getId(), textfield.getText());
                buscarSedes();
            }
        });
    }
*/
    @FXML
    void aceptarBusqueda(ActionEvent event) {
        Resultado<Object> resultado = new Resultado<>();
        resultado.set(tbvResultados.getSelectionModel().getSelectedItem());
        Aplicacion.getInstance().setResultadoBusqueda(resultado);
        closeWindow();
    }

    private void closeWindow() {

        jbtnAceptar.getScene().getWindow().hide();
    }
}
