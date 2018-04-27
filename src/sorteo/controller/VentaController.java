package sorteo.controller;

import com.jfoenix.controls.JFXTextField;
import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javax.xml.bind.annotation.XmlTransient;
import jfxtras.internal.scene.control.skin.CalendarPickerControlSkin;
import jfxtras.internal.scene.control.skin.CalendarPickerControlSkin.ShowWeeknumbers;
import jfxtras.scene.control.CalendarPicker;
import sorteo.model.dao.FacturaDao;
import sorteo.model.dao.MontosDao;
import sorteo.model.dao.SorteoDao;
import sorteo.model.dao.TipoSorteoDao;
import sorteo.model.entities.DetalleFactura;
import sorteo.model.entities.Factura;
import sorteo.model.entities.Montos;
import sorteo.model.entities.Sorteo;
import sorteo.model.entities.TipoSorteo;
import sorteo.utils.Aplicacion;
import sorteo.utils.AppWindowController;
import sorteo.utils.Formater;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

public class VentaController extends Controller implements Initializable {

    private Double totalFactura;
    private SimpleDoubleProperty totalFacturaProperty;
    private final int CIEN = 100;
    private int posTipoSorteo = -1;

    private int detalleSeleccionado;
    private Sorteo sorteo;
    private Factura factura;
    private final String HOY = Formater.getInstance().formatFechaCorta.format(new Date());
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
    private Label lblSubTotGen;

    @FXML
    private Button btnAplicar;

    @FXML
    private Pane pneTeclado;

    @FXML
    private TextField txtCliente;

    @FXML
    private FlowPane flpNumeros, flpMontos;

    @FXML
    private Label lblUsuario;

    @FXML
    private Label lblFecha;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnSalir;

    @FXML
    private Button num9;

    @FXML
    private Button num8;

    @FXML
    private Button num7;

    @FXML
    private Button num6;

    @FXML
    private JFXTextField txtNumero;

    @FXML
    private Button btnClear;

    @FXML
    private Button num1;

    @FXML
    private Button num0;

    @FXML
    private Button num5;

    @FXML
    private GridPane grdNumPad;

    @FXML
    private Button num4;

    @FXML
    private Button num3;

    @FXML
    private Button num2;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnEnter;

    @XmlTransient
    public ObservableList<TipoSorteo> sorteos = FXCollections
    .observableArrayList();

    @XmlTransient
    public ObservableList<DetalleFactura> detalleFactura = FXCollections
    .observableArrayList();

    @Override
    public void initialize() {
        init();
    }

    private void init() {
        posTipoSorteo = -1;
        totalFactura = Double.valueOf("0.0");
        totalFacturaProperty = new SimpleDoubleProperty(Double.valueOf("0.0"));

        lblSubTotGen.textProperty().bindBidirectional(totalFacturaProperty, Formater.getInstance().decimalFormat);
        lblFecha.setText("Fecha: " + Formater.getInstance().formatFechaHora.format(new Date()));
        Timeline timeline = new Timeline(new KeyFrame(
        Duration.millis(1000),
        ae -> lblFecha.setText("Fecha: " + Formater.getInstance().formatFechaHora.format(new Date()))));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        sorteo = new Sorteo();
        factura = new Factura();
        factura.setDetalleFactura(detalleFactura);
        startCalendar();
        cargarSorteos();
        cargarMontos();

        txtNumero.addEventFilter(KeyEvent.KEY_PRESSED, this::restrictNumbersOnly);
        bindDetalleFactura();
        addListenerDetalleFactura();

        detalleFactura.addListener((ListChangeListener.Change<? extends DetalleFactura> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    tbvDetFactura.refresh();
                }
                if (c.wasPermutated()) {
                    tbvDetFactura.refresh();
                } else if (c.wasUpdated()) {
                    tbvDetFactura.refresh();
                } else {
                    tbvDetFactura.refresh();
                }
            }
            totalFacturaProperty.set(0.0);
            detalleFactura.stream().forEach(d -> totalFacturaProperty.set(totalFacturaProperty.get() + d.getMonto()));
        });

    }

    private void bindDetalleFactura() {
        if (detalleFactura != null) {
            tbvDetFactura.setItems(detalleFactura);
            tbvDetFactura.refresh();
        }
        tbcLinea.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        tbcNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        tbcMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
    }

    private void addListenerDetalleFactura() {
        tbvDetFactura.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                detalleSeleccionado = tbvDetFactura.getSelectionModel().getSelectedIndex();
            }
        });
    }

    void restrictNumbersOnly(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case TAB:
            case BACK_SPACE:
            case DELETE:
                break;
            default:
                if (!keyEvent.getText().matches("\\d")) {
                    keyEvent.consume();
                }
                break;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

    private void startCalendar() {
        CalendarPickerControlSkin calendarPickerControlSkin = new CalendarPickerControlSkin(
        calFechaSorteo);
        calendarPickerControlSkin.setShowWeeknumbers(ShowWeeknumbers.NO);
        calFechaSorteo.setSkin(calendarPickerControlSkin);

        calFechaSorteo.calendarProperty().addListener((observable, oldValue, newValue) -> {
            flpNumeros.getChildren().clear();
            if (posTipoSorteo >= 0) {
                if (isFechaSorteoValida(this.sorteos.get(posTipoSorteo), newValue.getTime())) {
                    obtenerSorteo();
                }
            } else {
                AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Sorteo no indicado", "Debe indicar el sorteo antes.");
                return;
            }

        });
    }

    private void cargarMontos() {
        flpMontos.getChildren().clear();
        Resultado<ArrayList<Montos>> montosResult = MontosDao.getInstance().findAll();
        if (montosResult.getResultado() == TipoResultado.SUCCESS) {
            montosResult.get().stream().forEach(s -> {
                Button btn = new Button();
                btn.setText(Formater.getInstance().integerFormat.format(s.getMonto()));
                btn.setId(String.valueOf(s.getMonto()));
                btn.getStyleClass().add("buttonmonto");
                btn.setPrefSize(75, 40);
                btn.setOnAction(apostarPredefinido);
                flpMontos.getChildren().add(btn);
            });
        }
    }

    private final EventHandler<ActionEvent> apostarPredefinido = (final ActionEvent event) -> {
        Object source = event.getSource();
        detalleFactura.get(tbvDetFactura.getSelectionModel().getSelectedIndex()).setMonto(Double.parseDouble(((Button) source).getId()));
        tbvDetFactura.refresh();
        event.consume();
    };

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
                btn.setOnAction(tipoSorteoHandler);
                vbxSorteos.getChildren().add(btn);
            });
            sorteosResult.get().stream().forEach(sorteos::add);
        }
    }

    private final EventHandler<ActionEvent> tipoSorteoHandler = (final ActionEvent event) -> {
        Object source = event.getSource();
        if (source instanceof ToggleButton) {
            ToggleButton boton = (ToggleButton) source;
            vbxSorteos.getChildren().stream().forEach(s -> {
                if (!((ToggleButton) s).equals(source)) {
                    ((ToggleButton) s).setSelected(false);
                }
            });
            if (boton.isSelected()) {
                TipoSorteo buscado = new TipoSorteo(Integer.parseInt(boton.getId()));
                posTipoSorteo = this.sorteos.indexOf(buscado);
            }
        }
        event.consume();
    };

    private boolean isFechaSorteoValida(TipoSorteo sorteo, Date fecha) {
        try {
            String fechaElegida = Formater.getInstance().formatFechaCorta.format(fecha);
            if (fecha.before(Formater.getInstance().formatFechaCorta.parse(HOY))) {
                AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Seleccionar fecha", "La fecha del sorteo no puede ser menor que hoy (" + Formater.getInstance().formatFechaCorta.format(new Date()) + ").");
                return false;
            }
            if (Formater.getInstance().formatFechaCorta.parse(fechaElegida).equals(Formater.getInstance().formatFechaCorta.parse(HOY))
            && LocalTime.now(ZoneId.systemDefault()).isAfter(LocalDateTime.ofInstant(Instant.ofEpochMilli(sorteo.getHoraCorte().getTime()), ZoneId.systemDefault()).toLocalTime())) {
                AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Hora del sorteo", "No se puede vender números para el sorteo: " + sorteo.getDescripcion() + ", ya pasó la hora de cierre para la venta de números: "
                + Formater.getInstance().formatoHora.format(sorteo.getHoraCorte()) + ").");
                return false;
            }

        } catch (ParseException pe) {
            System.out.println(pe.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    private void setNumeros(Sorteo sorteo) {
        flpNumeros.getChildren().clear();

        for (int i = sorteo.getTipoSorteo().getNumeroMinimo(); i < sorteo.getTipoSorteo().getNumeroMaximo() + 1; i++) {
            Button numero = new Button();
            numero.setText(i < 10 ? "0" + i : Integer.toString(i));
            numero.setId(Integer.toString(i));
            numero.getStyleClass().add("buttonnumero");
            numero.setPrefSize(70, 50);
            numero.setOnAction(numeroHandler);
            flpNumeros.getChildren().add(numero);
        }
    }

    private final EventHandler<ActionEvent> numeroHandler = (final ActionEvent event) -> {
        Object source = event.getSource();
        Button boton = (Button) source;
        DetalleFactura nuevo = new DetalleFactura(Integer.valueOf(boton.getText()), BigDecimal.ZERO.doubleValue());
        nuevo.setFactura(factura);
        if (!detalleFactura.stream().filter(d -> d.getNumero().equals(nuevo.getNumero())).findFirst().isPresent()) {
            if (factura.getDetalleFactura().size() > sorteos.get(posTipoSorteo).getCantNumVenta()) {
                AppWindowController.getInstance().mensaje(Alert.AlertType.WARNING, "", "No puede ingresar más números, ya ha alcanzado la cantidad máxima de números para la venta.");
                return;
            }
            detalleFactura.add(nuevo);
        }
        tbvDetFactura.getSelectionModel().selectLast();
        event.consume();
    };

    @FXML
    void setNumero(ActionEvent event) {
        Button source = (Button) event.getSource();
        txtNumero.appendText(source.getText());
        txtNumero.requestFocus();
    }

    private void returnMonto() {
        if (detalleSeleccionado < 0) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.WARNING, "", "Debe seleccionar un detalle de la venta para modificar el monto de la apuesta.");
            txtNumero.requestFocus();
        }
        try {
            if (txtNumero.getText() != null && !txtNumero.getText().isEmpty() && txtNumero.getText().length() > 0) {
                int numero = Integer.valueOf(txtNumero.getText());
                int residuo = numero % CIEN;
                if (residuo != 0) {
                    AppWindowController.getInstance().mensaje(Alert.AlertType.WARNING, "Monto incorrecto", "El monto ingresado debe ser múltiplo de: " + String.valueOf(CIEN));
                    txtNumero.requestFocus();
                }
                Resultado<Double> montoApostado = SorteoDao.getInstance().getTotalApostadoNumero(this.factura.getSorteo(), detalleFactura.get(detalleSeleccionado).getNumero());
                if (montoApostado.getResultado().equals(TipoResultado.SUCCESS)) {
                    if (montoApostado.get().compareTo(this.factura.getSorteo().getTipoSorteo().getMontoMaximo()) > 0) {
                        //el monto apostado ya superó el máximo
                        AppWindowController.getInstance().mensaje(Alert.AlertType.WARNING, "Monto apuesta", "El monto máximo de la apuesta para el número: "
                        + String.valueOf(detalleFactura.get(detalleSeleccionado).getNumero())
                        + ",  ha sido alcanzado para el sorteo. No se puede aceptar la nueva apuesta.");
                        return;
                    }
                    AppWindowController.getInstance().mensaje(Alert.AlertType.WARNING, "", "No puede ingresar más números, ya ha alcanzado la cantidad máxima de números para la venta.");
                    return;
                }
                Double diferencia = (montoApostado.get() + Double.valueOf(String.valueOf(numero))) - this.factura.getSorteo().getTipoSorteo().getMontoMaximo();
                if (diferencia > 0) {
                    //el monto apostado + la nueva apuesta exceden el máximo permitido
                    AppWindowController.getInstance().mensaje(Alert.AlertType.WARNING, "Monto apuesta", "El monto de la apuesta para el número: "
                    + String.valueOf(detalleFactura.get(detalleSeleccionado).getNumero())
                    + ",  excede por: " + Formater.getInstance().decimalFormat.format(diferencia)
                    + " el monto máximo permitido apostar por número. No se puede aceptar la nueva apuesta.");
                    return;
                }

                detalleFactura.get(detalleSeleccionado).setMonto(Double.valueOf(txtNumero.getText()));
                tbvDetFactura.refresh();
                txtNumero.clear();
            } else {
                AppWindowController.getInstance().mensaje(Alert.AlertType.WARNING, "Monto no indicado", "Debe indicar un monto a apostar");
                txtNumero.requestFocus();
            }
        } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    void btnBack(ActionEvent event) {
        txtNumero.deletePreviousChar();
    }

    @FXML
    void btnClear(ActionEvent event) {
        txtNumero.clear();
    }

    @FXML
    void btnEnter(ActionEvent event) {
        returnMonto();
    }

    @FXML
    void numKeyPressed(KeyEvent event) {
        if (null != event.getCode()) {
            switch (event.getCode()) {
                case NUMPAD0:
                case DIGIT0:
                    break;
                case NUMPAD1:
                case DIGIT1:
                    break;
                case NUMPAD2:
                case DIGIT2:
                    break;
                case NUMPAD3:
                case DIGIT3:
                    break;
                case NUMPAD4:
                case DIGIT4:
                    break;
                case NUMPAD5:
                case DIGIT5:
                    break;
                case NUMPAD6:
                case DIGIT6:
                    break;
                case NUMPAD7:
                case DIGIT7:
                    break;
                case NUMPAD8:
                case DIGIT8:
                    break;
                case NUMPAD9:
                case DIGIT9:
                    break;
                case ENTER:
                    returnMonto();
                    break;
                case BACK_SPACE:
                    txtNumero.deletePreviousChar();
                    break;
                default:
                    break;
            }
        }
        event.consume();
    }

    private void obtenerSorteo() {
        if (calFechaSorteo.getCalendar() != null) {
            Resultado<Sorteo> sorteoResult = SorteoDao.getInstance().findByFecha(calFechaSorteo.getCalendar().getTime(), this.sorteos.get(posTipoSorteo));
            if (sorteoResult.getResultado() == TipoResultado.SUCCESS) {
                this.factura.setSorteo(sorteoResult.get());
            } else {
                if (AppWindowController.getInstance().mensajeConfimacion("Crear nuevo sorteo", "¿Desea crear un nuevo sorteo para la fecha indicada?")) {
                    SorteoDao.getInstance().setSorteo(new Sorteo(calFechaSorteo.getCalendar().getTime(), this.sorteos.get(posTipoSorteo), Aplicacion.getInstance().getSucursalDefault()));
                    Resultado<Sorteo> sorteoSave = SorteoDao.getInstance().save();
                    if (sorteoSave.getResultado().equals(TipoResultado.SUCCESS)) {
                        this.factura.setSorteo(sorteoSave.get());
                    } else {
                        AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Error al crear sorteo", sorteoSave.getMensaje());
                    }
                }
            }
            setNumeros(this.factura.getSorteo());
        }
    }

    @FXML
    void btnAplicar(ActionEvent event) {
        FacturaDao.getInstance().setFactura(this.factura);
        Resultado<Factura> facturaSave = FacturaDao.getInstance().save();
        if (facturaSave.getResultado().equals(TipoResultado.SUCCESS)) {
            this.factura = facturaSave.get();
            AppWindowController.getInstance().mensaje(Alert.AlertType.INFORMATION, "Venta aplicada", "Venta aplicada exitosamente.");
        } else {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Error al aplicar venta", facturaSave.getMensaje());
        }
    }

}
