package sorteo.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import sorteo.controller.ConsultaController;
import sorteo.controller.Controller;

public class AppWindowController {

    private static final String APP_FORMINI = "sor_main";

    private static HashMap<String, FXMLLoader> loaders = new HashMap<>();
    private static HashMap<String, Pane> roots = new HashMap<>();
    private static AppWindowController INSTANCE;

    private Stage mainStage;
    private BorderPane mainRoot;

    private AppWindowController() {
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            // Sólo se accede a la zona sincronizada
            // cuando la instancia no está creada
            synchronized (AppWindowController.class) {
                // En la zona sincronizada sería necesario volver
                // a comprobar que no se ha creado la instancia
                if (INSTANCE == null) {
                    INSTANCE = new AppWindowController();
                }
            }
        }
    }

    public static AppWindowController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    //para que solamente exista una instancia del objeto
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public BorderPane getMainRoot() {
        return mainRoot;
    }

    public void setMainRoot(BorderPane mainRoot) {
        this.mainRoot = mainRoot;
    }

    public FXMLLoader getLoader(String view) {
        return loaders.get(view);
    }

    public Parent getViewRoot(String view) {
        return roots.get(view);
    }

    public boolean cargarLogin(String ventana) {
        if (loaders.get(ventana) == null) {
            loaders.put(ventana, new FXMLLoader(getClass().getResource(Parametros.getInstance().getParametro("pathLogin") + ventana + ".fxml")));
        }
        try {
            if (roots.get(ventana) == null) {
                roots.put(ventana, loaders.get(ventana).load());
            }
        } catch (IOException ex) {
            Logger.getLogger(AppWindowController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean cargarView(String ventana) {
        if (loaders.get(ventana) == null) {
            loaders.put(ventana, new FXMLLoader(getClass().getResource(Parametros.getInstance().getParametro("pathViews") + ventana + ".fxml")));
        }
        try {
            if (roots.get(ventana) == null) {
                roots.put(ventana, loaders.get(ventana).load());
            }
        } catch (IOException ex) {
            Logger.getLogger(AppWindowController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(AppWindowController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public Pane getView(String ventana) {
        if (roots.get(ventana) == null) {
            cargarView(ventana);
        }
        return roots.get(ventana);
    }

    public void initApplication() {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        if (cargarView(APP_FORMINI)) {
            Scene scene = new Scene(roots.get(APP_FORMINI));
            if (mainStage == null) {
                mainStage = new Stage();
                mainStage.initStyle(StageStyle.UNIFIED);
            }

            setMainRoot((BorderPane) scene.getRoot());
            mainStage.setX(bounds.getMinX());
            mainStage.setY(bounds.getMinY());
            mainStage.setWidth(bounds.getWidth());
            mainStage.setHeight(bounds.getHeight());
            mainStage.setMaximized(true);
            mainStage.setResizable(true);
            mainStage.centerOnScreen();
            mainStage.setScene(scene);
            mainStage.getIcons().add(new Image(getClass().getResourceAsStream("/sorteo.png")));
            mainStage.setTitle("Sorteos");
            mainStage.show();
        }
    }

    public void abrirVentana(String ventana, String titulo, boolean resize) {
        if (cargarView(ventana)) {
            try {
                Scene scene = new Scene(roots.get(ventana));

                if (mainStage == null) {
                    mainStage = new Stage();
                }

                mainStage.initStyle(StageStyle.DECORATED);
                mainStage.setScene(scene);
                mainStage.centerOnScreen();
                mainStage.setScene(scene);
                mainStage.setTitle(titulo);
                mainStage.setResizable(resize);
                mainStage.getIcons().add(new Image(getClass().getResourceAsStream("/sorteo.png")));
                mainStage.show();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void abrirVentanaEnPrincipal(String ventana, String location, String funcion) {
        if (cargarView(ventana)) {
            Controller controller = loaders.get(ventana).getController();
            if (funcion != null && !funcion.isEmpty()) {
                controller.setAccion(funcion);
            }
            controller.initialize();
            switch (location) {
                case "Center":
                    ((VBox) ((BorderPane) mainStage.getScene().getRoot()).getCenter()).getChildren().clear();
                    ((VBox) ((BorderPane) mainStage.getScene().getRoot()).getCenter()).getChildren().add(loaders.get(ventana).getRoot());
                    ((VBox) ((BorderPane) mainStage.getScene().getRoot()).getCenter()).setAlignment(Pos.CENTER);
                    break;
                case "Top":
                    ((BorderPane) mainStage.getScene().getRoot()).setTop(getViewRoot(ventana));
                    break;
                case "Bottom":
                    ((BorderPane) mainStage.getScene().getRoot()).setBottom(getViewRoot(ventana));
                    break;
                case "Right":
                    ((BorderPane) mainStage.getScene().getRoot()).setRight(getViewRoot(ventana));
                    break;
                case "Left":
                    ((BorderPane) mainStage.getScene().getRoot()).setLeft(getViewRoot(ventana));
                    break;
                default:
                    ((BorderPane) mainStage.getScene().getRoot()).setCenter(getViewRoot(ventana));
            }
        }
    }

    public void abrirVentanaModal(String ventana, String titulo) {
        if (cargarView(ventana)) {
            FXMLLoader loader = getLoader(ventana);
            Controller controller = loader.getController();
            controller.initialize();
            Stage stage = new Stage();
            stage.getIcons().addAll(mainStage.getIcons());
            stage.setTitle(mainStage.getTitle());
            stage.setResizable(false);
            stage.setOnHidden((WindowEvent event) -> {
                controller.setStage(null);
            });
            controller.setStage(stage);
            Scene scene = new Scene(roots.get(ventana));
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStage);
            stage.centerOnScreen();
            stage.showAndWait();
        }
    }

    public void abrirVentanaEnPrincipal(String ventana, String location) {
        if (cargarView(ventana)) {
            Controller controller = loaders.get(ventana).getController();

            controller.initialize();
            switch (location) {
                case "Center":
                    ((VBox) ((BorderPane) mainStage.getScene().getRoot()).getCenter()).getChildren().clear();
                    ((VBox) ((BorderPane) mainStage.getScene().getRoot()).getCenter()).getChildren().add(loaders.get(ventana).getRoot());
                    ((VBox) ((BorderPane) mainStage.getScene().getRoot()).getCenter()).setAlignment(Pos.CENTER);
                    break;
                case "Top":
                    ((BorderPane) mainStage.getScene().getRoot()).setTop(getViewRoot(ventana));
                    break;
                case "Bottom":
                    ((BorderPane) mainStage.getScene().getRoot()).setBottom(getViewRoot(ventana));
                    break;
                case "Right":
                    ((BorderPane) mainStage.getScene().getRoot()).setRight(getViewRoot(ventana));
                    break;
                case "Left":
                    ((BorderPane) mainStage.getScene().getRoot()).setLeft(getViewRoot(ventana));
                    break;
                default:
                    ((BorderPane) mainStage.getScene().getRoot()).setCenter(getViewRoot(ventana));
            }
        }
    }

    public void loadHome() {
        AppWindowController.getInstance().abrirVentanaEnPrincipal("sor_inicio", "Center");
    }

    public void goHome() {
        loadHome();
    }

    public void cerrarVentana() {
        mainStage.setScene(null);
    }

    public void mensaje(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public boolean mensajeConfimacion(String titulo, String mensaje) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle(titulo);
        dialog.setHeaderText(null);
        dialog.setContentText(mensaje);
        final Optional<ButtonType> result = dialog.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public void cerrarAplicacion() {
        if (getInstance().mensajeConfimacion("Salir del sistema", "¿Cerrar la aplicación de sorteos?")) {
            Platform.exit();
        }
    }

    public void goViewInWindowModal(String viewName, Stage parentStage) {
        VBox panel = new VBox();
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.initialize();
        Stage stage = new Stage();
        stage.getIcons().addAll(mainStage.getIcons());
        stage.setTitle(mainStage.getTitle());
        stage.setResizable(false);
        stage.setOnHidden((WindowEvent event) -> {
            controller.setStage(null);
        });
        controller.setStage(stage);
        panel.getChildren().add(loader.getRoot());
        Scene scene = new Scene(panel);
        scene.getStylesheets().addAll(mainStage.getScene().getRoot().getStylesheets());
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainStage);
        stage.centerOnScreen();
        stage.showAndWait();
    }

    public void goView(String viewName, String location, String accion) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.setAccion(accion);
        controller.initialize();
        Stage stage = controller.getStage();
        if (stage == null) {
            stage = mainStage;
            controller.setStage(stage);
        }
        switch (location) {
            case "Center":
                ((VBox) ((BorderPane) stage.getScene().getRoot()).getCenter()).getChildren().clear();
                ((VBox) ((BorderPane) stage.getScene().getRoot()).getCenter()).getChildren().add(loader.getRoot());
                break;
            case "Top":
                ((BorderPane) stage.getScene().getRoot()).setTop(loader.getRoot());
                break;
            case "Bottom":
                ((BorderPane) stage.getScene().getRoot()).setBottom(loader.getRoot());
                break;
            case "Right":
                ((BorderPane) stage.getScene().getRoot()).setRight(loader.getRoot());
                break;
            case "Left":
                ((BorderPane) stage.getScene().getRoot()).setLeft(loader.getRoot());
                break;
            default:
                ((BorderPane) stage.getScene().getRoot()).setCenter(loader.getRoot());
        }
    }

    public Controller getController(String viewName) {
        FXMLLoader loader = null;
        if (cargarView(viewName)) {
            loader = getLoader(viewName);
        }
        return loader.getController();
    }

    /**
     * Carga la vista indicada en una ventana nueva
     *
     * @param viewName Nombre de la vista
     */
    public void goViewInWindow(String viewName) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.initialize();
        Stage stage = new Stage();
        stage.getIcons().add(new Image("unaplanilla/resources/Icono-96.png"));
        stage.setOnHidden((WindowEvent event) -> {
            controller.getStage().getScene().setRoot(new Pane());
            controller.setStage(null);
        });
        controller.setStage(stage);
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

    }

    /**
     * Carga la vista indicada en una ventana nueva de forma modal
     *
     * @param viewName Nombre de la vista
     * @param parentStage Stage padre al que le pertenece la ventana modal
     */
    public void goViewInWindowModal(String viewName, Stage parentStage, Boolean resizable) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.initialize();
        Stage stage = new Stage();
        stage.getIcons().add(new Image("unaplanilla/resources/Icono-96.png"));
        stage.setResizable(resizable);
        stage.setOnHidden((WindowEvent event) -> {
            controller.getStage().getScene().setRoot(new Pane());
            controller.setStage(null);
        });
        controller.setStage(stage);
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parentStage);
        stage.centerOnScreen();
        stage.showAndWait();

    }

}
