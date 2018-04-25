package sorteo.controller;

import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javax.xml.bind.annotation.XmlTransient;
import jfxtras.internal.scene.control.skin.CalendarPickerControlSkin;
import jfxtras.internal.scene.control.skin.CalendarPickerControlSkin.ShowWeeknumbers;
import jfxtras.scene.control.CalendarPicker;
import sorteo.model.dao.MontosDao;
import sorteo.model.dao.TipoSorteoDao;
import sorteo.model.entities.DetalleFactura;
import sorteo.model.entities.Factura;
import sorteo.model.entities.Montos;
import sorteo.model.entities.Sorteo;
import sorteo.model.entities.TipoSorteo;
import sorteo.utils.AppWindowController;
import sorteo.utils.Formater;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

public class VentaController extends Controller implements Initializable {

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
    private TableColumn<?, ?> tbcBtnEliLinea;

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
        lblFecha.setText("Fecha: " + Formater.getInstance().formatFechaHora.format(new Date()));
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> lblFecha.setText("Fecha: " + Formater.getInstance().formatFechaHora.format(new Date()))));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        sorteo = new Sorteo();
        factura = new Factura();
        startCalendar();
        cargarSorteos();
        cargarMontos();
        //flpNumeros.prefWidthProperty().bind(this.getStage().widthProperty());
        //flpNumeros.prefHeightProperty().bind(this.getStage().heightProperty());
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
            if (!isFechaSorteoValida(sorteo, newValue.getTime())) {;
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
                btn.setId(String.valueOf(s.getCodigo()));
                btn.getStyleClass().add("buttonmonto");
                btn.setPrefSize(75, 40);
                //btn.setOnAction(tipoSorteoHandler);
                flpMontos.getChildren().add(btn);
            });
        }
        Button btn = new Button();
        btn.setText("Otro");
        btn.setId("*");
        btn.getStyleClass().add("buttonmonto");
        btn.setPrefSize(75, 40);
        btn.setOnAction(callKeyPad);
        flpMontos.getChildren().add(btn);
    }

    final EventHandler<ActionEvent> callKeyPad = (final ActionEvent event) -> {
        Button source = (Button) event.getSource();
        TecladoController tecladoController = (TecladoController) AppWindowController.getInstance().getController("sor_teclado");
        AppWindowController.getInstance().goViewInWindowModal("sor_teclado", getStage());
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

    final EventHandler<ActionEvent> tipoSorteoHandler = (final ActionEvent event) -> {
        Object source = event.getSource();
        if (source instanceof ToggleButton) {
            ToggleButton boton = (ToggleButton) source;
            if (boton.isSelected()) {
                TipoSorteo buscado = new TipoSorteo(Integer.parseInt(boton.getId()));
                int posicion = this.sorteos.indexOf(buscado);
                if (posicion > -1) {
                    this.sorteo.setTipoSorteo(this.sorteos.get(posicion));
                    setNumeros(this.sorteo);
                }
            }
        }
        event.consume();
    };

    private boolean isFechaSorteoValida(Sorteo sorteo, Date fecha) {
        try {
            if (fecha.before(Formater.getInstance().formatFechaCorta.parse(HOY))) {
                AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Seleccionar fecha", "La fecha del sorteo no puede ser menor que hoy (" + Formater.getInstance().formatFechaCorta.format(new Date()) + ").");
                return false;
            }
        } catch (ParseException pe) {
            ;
        }
        /*if (LocalTime.now(ZoneId.systemDefault()).isBefore(LocalDateTime.ofInstant(Instant.ofEpochMilli(s.getHoraCorte().getTime()), ZoneId.systemDefault()).toLocalTime()) || LocalTime.now(ZoneId.systemDefault()).equals(LocalDateTime.ofInstant(Instant.ofEpochMilli(s.getHoraCorte().getTime()), ZoneId.systemDefault()).toLocalTime())) {

        }*/
        return true;
    }

    private void setNumeros(Sorteo sorteo) {
        flpNumeros.getChildren().clear();

        for (int i = sorteo.getTipoSorteo().getNumeroMinimo(); i < sorteo.getTipoSorteo().getNumeroMaximo() + 1; i++) {
            Button numero = new Button();
            numero.setText(i < 10 ? "0" + i : Integer.toString(i));
            numero.setId(Integer.toString(i));
            numero.getStyleClass().add("buttonnumero");
            numero.setPrefSize(50, 50);
            flpNumeros.getChildren().add(numero);
        }
    }

}
