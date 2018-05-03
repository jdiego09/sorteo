/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.util.Duration;
import sorteo.model.dao.SeguridadDao;
import sorteo.model.entities.MenuXRol;
import sorteo.model.entities.RolXUsuario;
import sorteo.utils.Aplicacion;
import sorteo.utils.AppWindowController;
import sorteo.utils.Formater;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

/**
 *
 * @author jdiego
 */
public class PrincipalController extends Controller implements Initializable {

    @FXML
    private Label lblUsuario;

    @FXML
    private Label lblFecha;

    @FXML
    private MenuBar mnuPrincipal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
    }

    @Override
    public void initialize() {
        init();
    }

    private void init() {
        lblUsuario.setText("Usuario: " + Aplicacion.getInstance().getUsuario().getUsuCodigo());
        lblFecha.setText("Fecha: " + Formater.getInstance().formatFechaHora.format(new Date()));
        Timeline timeline = new Timeline(new KeyFrame(
        Duration.millis(1000),
        ae -> lblFecha.setText("Fecha: " + Formater.getInstance().formatFechaHora.format(new Date()))));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        setMenuPrincipal();
    }

    private void setMenuPrincipal() {
        mnuPrincipal.getMenus().clear();
        for (RolXUsuario r : Aplicacion.getInstance().getUsuario().getRolXUsuarioList()) {
            Menu modulo = new Menu();
            modulo.setId(r.getRxuCodrol().getRolCodigo());
            modulo.setText(r.getRxuCodrol().getRolDescripcion());
            modulo.getStyleClass().add("menu");
            mnuPrincipal.getMenus().add(modulo);

            for (MenuXRol m : r.getRxuCodrol().getMenuXRollList()) {
                MenuItem opcion = new MenuItem();
                opcion.setId(m.getMxrCodmenu().getMenPantalla());
                opcion.setText(m.getMxrCodmenu().getMenEtiqueta());
                opcion.setOnAction(actionMenu);
                modulo.getItems().add(opcion);
            }
        }
        Menu salir = new Menu();
        salir.setId("sal");
        salir.setText("Salir");
        salir.getStyleClass().add("menu");
        MenuItem opcSalir = new MenuItem();
        opcSalir.setId("ext");
        opcSalir.setText("Salir");
        opcSalir.setOnAction(actionMenu);
        salir.getItems().add(opcSalir);
        mnuPrincipal.getMenus().add(salir);
    }

    private final EventHandler<ActionEvent> actionMenu = (final ActionEvent event) -> {
        Object source = event.getSource();
        if (source instanceof MenuItem) {
            MenuItem opcion = (MenuItem) source;
            if (opcion.getId().equalsIgnoreCase("ext")) {
                AppWindowController.getInstance().cerrarAplicacion();
            } else {
                AppWindowController.getInstance().abrirVentanaEnPrincipal(opcion.getId(), "Center");
            }
        }
        event.consume();
    };

}
