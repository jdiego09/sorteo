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
import java.util.HashMap;
import java.util.ResourceBundle;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javax.xml.bind.annotation.XmlTransient;
import jfxtras.internal.scene.control.skin.CalendarPickerControlSkin;
import jfxtras.internal.scene.control.skin.CalendarPickerControlSkin.ShowWeeknumbers;
import jfxtras.scene.control.CalendarPicker;
import sorteo.model.dao.ExclusionDao;
import sorteo.model.dao.FacturaDao;
import sorteo.model.dao.MontosDao;
import sorteo.model.dao.SorteoDao;
import sorteo.model.dao.TipoSorteoDao;
import sorteo.model.entities.DetalleFactura;
import sorteo.model.entities.Exclusion;
import sorteo.model.entities.Factura;
import sorteo.model.entities.Montos;
import sorteo.model.entities.Sorteo;
import sorteo.model.entities.TipoSorteo;
import sorteo.utils.Aplicacion;
import sorteo.utils.AppWindowController;
import sorteo.utils.Formater;
import sorteo.utils.Parametros;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

public class VentaController extends Controller implements Initializable {

    private HashMap<String, Exclusion> exclusiones = new HashMap<>();
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
    private TextField txtCliente;

    @FXML
    private FlowPane flpNumeros, flpMontos;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnBorrar;

    @FXML
    private Button btnSalir;

    @FXML
    private TextField txtNumElegido;

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

    private void bindFactura() {
        txtCliente.textProperty().bindBidirectional(factura.getClienteProperty());
    }

    private void unbindFactura() {
        txtCliente.textProperty().unbindBidirectional(factura.getClienteProperty());
    }

    private void init() {
        posTipoSorteo = -1;
        totalFactura = Double.valueOf("0.0");
        totalFacturaProperty = new SimpleDoubleProperty(Double.valueOf("0.0"));

        lblSubTotGen.textProperty().bindBidirectional(totalFacturaProperty, Formater.getInstance().decimalFormat);

        sorteo = new Sorteo();
        factura = new Factura();
        bindFactura();
        factura.setDetalleFactura(detalleFactura);
        startCalendar();
        cargarSorteos();
        cargarMontos();

        txtNumero.addEventFilter(KeyEvent.KEY_PRESSED, this::restrictNumbersOnly);
        bindDetalleFactura();
        addListenerDetalleFactura();

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
                tbvDetFactura.getSelectionModel().focus(detalleSeleccionado);
            }
        });
        detalleFactura.addListener((ListChangeListener.Change<? extends DetalleFactura> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    calcularTotal();
                }
                if (c.wasPermutated()) {
                    calcularTotal();
                } else if (c.wasUpdated()) {
                    calcularTotal();
                } else {
                    calcularTotal();
                }
            }
        });
    }

    private void calcularTotal() {
        totalFacturaProperty.set(detalleFactura.stream().mapToDouble(d -> d.getMonto()).sum());
        tbvDetFactura.refresh();
    }

    private void reiniciar() {
        unbindFactura();
        sorteos.clear();
        cargarSorteos();
        if (calFechaSorteo.getCalendar() != null) {
            calFechaSorteo.getCalendar().clear();
        }
        sorteo = new Sorteo();
        factura = new Factura();
        bindFactura();
        factura.setDetalleFactura(detalleFactura);
        flpNumeros.getChildren().clear();

        posTipoSorteo = -1;
        totalFactura = Double.valueOf("0.0");
        totalFacturaProperty = new SimpleDoubleProperty(Double.valueOf("0.0"));

        detalleFactura.clear();
        cargarMontos();
        calcularTotal();
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
                    Resultado<Boolean> ventaBloqueada = ExclusionDao.getInstance().ventaBloqueada(this.sorteos.get(posTipoSorteo), newValue.getTime());
                    if (ventaBloqueada.get()) {
                        AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Ventas bloqueadas", ventaBloqueada.getMensaje());
                        return;
                    }
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
                btn.setFocusTraversable(false);
                btn.setOnAction(apostarPredefinido);
                flpMontos.getChildren().add(btn);
            });
        }
    }

    private final EventHandler<ActionEvent> apostarPredefinido = (final ActionEvent event) -> {
        Object source = event.getSource();
        //valida el monto apostado
        if (apuestaValida(Integer.valueOf(((Button) source).getId()))) {
            detalleFactura.get(tbvDetFactura.getSelectionModel().getSelectedIndex()).setMonto(Double.parseDouble(((Button) source).getId()));
        }
        calcularTotal();
        event.consume();
    };

    private void cargarSorteos() {
        vbxSorteos.getChildren().clear();
        sorteos.clear();
        Resultado<ArrayList<TipoSorteo>> sorteosResult = TipoSorteoDao.getInstance().findAllActivos();
        if (sorteosResult.getResultado() == TipoResultado.SUCCESS) {
            sorteosResult.get().stream().forEach(s -> {
                ToggleButton btn = new ToggleButton();
                btn.setText(s.getDescripcion());
                btn.setId(String.valueOf(s.getCodigo()));
                btn.getStyleClass().add("buttonsorteo");
                btn.setFocusTraversable(false);
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
            Resultado<Double> montoApostado = SorteoDao.getInstance().getTotalApostadoNumero(this.factura.getSorteo(), i);
            Double montoMaximo = exclusiones.get(String.valueOf(i)) != null ? ((Exclusion) exclusiones.get(String.valueOf(i))).getExcMonto() : this.factura.getSorteo().getTipoSorteo().getMontoMaximo();
            Button numero = new Button();
            numero.setText(i < 10 ? "0" + i : Integer.toString(i));
            numero.setId(Integer.toString(i));
            numero.getStyleClass().add("buttonnumero");
            numero.setPrefSize(70, 50);
            numero.setFocusTraversable(false);
            if (montoApostado.getResultado().equals(TipoResultado.SUCCESS)) {
                if (montoApostado.get().compareTo(montoMaximo) >= 0) {
                    numero.setDisable(true);
                    numero.getStyleClass().add("apuestaExcedida");
                }
            }
            numero.setOnAction(numeroHandler);
            flpNumeros.getChildren().add(numero);

        }
    }

    private final EventHandler<ActionEvent> numeroHandler = (final ActionEvent event) -> {
        Object source = event.getSource();
        Button boton = (Button) source;
        agregarNumeroADetalle(Integer.valueOf(boton.getId()));
        event.consume();
    };

    @FXML
    void setNumero(ActionEvent event) {
        Button source = (Button) event.getSource();
        txtNumero.appendText(source.getText());
        txtNumero.requestFocus();
    }

    private void returnMonto() {
        detalleSeleccionado = tbvDetFactura.getSelectionModel().getSelectedIndex();
        if (detalleSeleccionado < 0) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.WARNING, "", "Debe seleccionar un detalle de la venta para modificar el monto de la apuesta.");
            txtNumero.requestFocus();
            return;
        }
        try {
            if (txtNumero.getText() != null && !txtNumero.getText().isEmpty() && txtNumero.getText().length() > 0) {
                int numero = Integer.valueOf(txtNumero.getText());
                int residuo = numero % CIEN;
                //valida que el monto ingresado sea multiplo de 100.
                if (residuo != 0) {
                    AppWindowController.getInstance().mensaje(Alert.AlertType.WARNING, "Monto incorrecto", "El monto ingresado debe ser múltiplo de: " + String.valueOf(CIEN));
                    txtNumero.requestFocus();
                    return;
                }
                //valida el monto apostado
                if (apuestaValida(numero)) {
                    detalleFactura.get(detalleSeleccionado).setMonto(Double.valueOf(txtNumero.getText()));
                    calcularTotal();
                    txtNumero.clear();
                }
            } else {
                AppWindowController.getInstance().mensaje(Alert.AlertType.WARNING, "Monto no indicado", "Debe indicar un monto a apostar");
                txtNumero.requestFocus();
            }
            txtNumElegido.requestFocus();
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
        txtNumElegido.requestFocus();
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
                    txtNumElegido.requestFocus();
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
                cargarExcepciones();
                setNumeros(this.factura.getSorteo());
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
                cargarExcepciones();
                setNumeros(this.factura.getSorteo());
                txtNumElegido.requestFocus();
            }

        }
    }

    @FXML
    void btnAplicar(ActionEvent event) {
        if (this.detalleFactura.size() <= 0) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.WARNING, "Aplicar", "No hay ningún número seleccionado para aplicar la venta.");
            return;
        }
        this.factura.setHechaPor(Parametros.getInstance().getParametro("usuario"));
        this.factura.setTotal(totalFacturaProperty.get());
        if (this.factura.getTotal() <= 0) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.WARNING, "Aplicar", "Debe indicar el monto para aplicar la venta.");
            return;
        }
        if (!AppWindowController.getInstance().mensajeConfimacion("Confirmar venta", "¿Desea aplicar la venta?")) {
            return;
        }
        String detalleVenta = null;
        for (DetalleFactura d : this.detalleFactura) {
            detalleVenta = detalleVenta + "\nNúmero:  " + String.valueOf(d.getNumero()) + ", Monto: " + Formater.getInstance().decimalFormat.format(d.getMonto());
        }
        if (!AppWindowController.getInstance().mensajeConfimacion("Confirmar venta", "¿Seguro que desea aplicar la venta:\n"
           + detalleVenta + "?")) {
            return;
        }

        FacturaDao.getInstance().setFactura(this.factura);
        Resultado<Factura> facturaSave = FacturaDao.getInstance().save();
        if (facturaSave.getResultado().equals(TipoResultado.SUCCESS)) {
            this.factura = facturaSave.get();
            HashMap<String, Object> params = new HashMap<>();
            params.put("idFactura", this.factura.getCodigo());
            Aplicacion.getInstance().imprimirReporte("rptTicket", params);

            reiniciar();
            AppWindowController.getInstance().mensaje(Alert.AlertType.INFORMATION, "Venta aplicada", "Venta aplicada exitosamente.");
        } else {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Error al aplicar venta", facturaSave.getMensaje());
        }
    }

    private void cargarExcepciones() {
        exclusiones.clear();
        Resultado<ArrayList<Exclusion>> resultado = ExclusionDao.getInstance().findBySorteo(this.factura.getSorteo().getTipoSorteo(), calFechaSorteo.getCalendar().getTime());
        if (!resultado.getResultado().equals(TipoResultado.SUCCESS)) {
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Traer excepciones", resultado.getMensaje());
            return;
        }
        resultado.get().stream().forEach((e) -> {
            exclusiones.put(String.valueOf(e.getExcNumero()), e);
        });
    }

    private boolean apuestaValida(int numero) {
        Double diferencia = -1.0;
        Double montoMaximo = exclusiones.get(String.valueOf(numero)) != null ? ((Exclusion) exclusiones.get(String.valueOf(numero))).getExcMonto() : this.factura.getSorteo().getTipoSorteo().getMontoMaximo();
        Resultado<Double> montoApostado = SorteoDao.getInstance().getTotalApostadoNumero(this.factura.getSorteo(), detalleFactura.get(detalleSeleccionado).getNumero());
        if (montoApostado.getResultado().equals(TipoResultado.SUCCESS)) {
            if ((montoApostado.get()).compareTo(montoMaximo) >= 0) {
                AppWindowController.getInstance().mensaje(Alert.AlertType.WARNING, "Monto apuesta", "El monto máximo de la apuesta para el número: "
                   + String.valueOf(numero)
                   + ",  ha sido alcanzado para el sorteo. No se puede aceptar la nueva apuesta.");
                return false;
            }
        }
        diferencia = (montoApostado.get() + Double.valueOf(String.valueOf(numero))) - montoMaximo;
        if (diferencia > 0) {
            //el monto apostado + la nueva apuesta exceden el máximo permitido
            AppWindowController.getInstance().mensaje(Alert.AlertType.WARNING, "Monto apuesta", "El monto de la apuesta para el número: "
               + String.valueOf(detalleFactura.get(detalleSeleccionado).getNumero())
               + ",  excede por: " + Formater.getInstance().decimalFormat.format(diferencia)
               + " el monto máximo permitido apostar por número. No se puede aceptar la nueva apuesta.");
            return false;
        }
        return true;
    }

    @FXML
    void limpiarVenta(ActionEvent event) {
        reiniciar();
    }

    @FXML
    void salir(ActionEvent event) {
        if (Aplicacion.getInstance().getRolesUsuario().equalsIgnoreCase("ven")) {
            AppWindowController.getInstance().cerrarAplicacion();
        } else {
            AppWindowController.getInstance().goHome();
        }
    }

    @FXML
    void eliminarDetalle(ActionEvent event) {
        detalleSeleccionado = tbvDetFactura.getSelectionModel().getSelectedIndex();
        if (detalleSeleccionado >= 0) {
            this.factura.getDetalleFactura().remove(detalleSeleccionado);
        }
    }

    @FXML
    void enterNumero(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            agregarNumeroADetalle(Integer.valueOf(txtNumElegido.getText()));
            txtNumElegido.clear();
            txtNumero.requestFocus();

        }
    }

    private void agregarNumeroADetalle(Integer num) {
        DetalleFactura nuevo = new DetalleFactura(num, BigDecimal.ZERO.doubleValue());
        nuevo.setFactura(factura);
        if (!detalleFactura.stream().filter(d -> d.getNumero().equals(nuevo.getNumero())).findFirst().isPresent()) {
            //valida la cantidad de numeros por venta para el sorteo
            if (factura.getDetalleFactura().size() >= sorteos.get(posTipoSorteo).getCantNumVenta()) {
                AppWindowController.getInstance().mensaje(Alert.AlertType.WARNING, "", "No puede ingresar más números, ya ha alcanzado la cantidad máxima de números para la venta.");
                return;
            }
            detalleFactura.add(nuevo);
        }
        tbvDetFactura.getSelectionModel().selectLast();
        txtNumero.requestFocus();
    }

}
