/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sorteo.utils.Aplicacion;
import sorteo.utils.AppWindowController;
import sorteo.utils.Parametros;

/**
 *
 * @author jdiego
 */
public class Sorteo extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {        
        Aplicacion.getInstance().cargaProperties();
        Parametros.getInstance().setEntityManager();
        Parametros.getInstance().setParametro("pathViews", "/sorteo/view/");
        AppWindowController.getInstance().abrirVentana("sor_login", "Sorteos - Inicio de sesión", false);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
