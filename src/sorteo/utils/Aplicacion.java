/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.utils;

import com.jfoenix.controls.JFXHamburger;
import java.sql.Connection;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;
import sorteo.model.entities.Empresa;
import sorteo.model.entities.Sucursal;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;
import sorteo.model.dao.EmpresaDao;
import sorteo.model.entities.DetalleFactura;
import sorteo.model.entities.Usuario;

/**
 *
 * @author jdiego
 */
public class Aplicacion {

    private static Aplicacion INSTANCE;

    private static Usuario usuario;
    private static String rolesUsuario;

    private static Sucursal sucursal;
    private static Empresa empresa;

    private static Resultado<Object> resultadoBusqueda;
    //devolver el monto ingresado en el teclado  numerico
    private static Resultado<DetalleFactura> detalleSeleccionado;

    final String pathDir = System.getProperty("user.dir") + "/src/sorteo/reportes/";

    //parámetros
    private static Integer defaultEmpresa;
    private static Integer defaultSucursal;

    private static String urlBD;
    private static String driverBD;
    private static String usuarioBD;
    private static String passwordBD;

    private static MouseEvent eventoMenu;

    private Aplicacion() {
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            // Sólo se accede a la zona sincronizada
            // cuando la instancia no está creada
            synchronized (Aplicacion.class) {
                // En la zona sincronizada sería necesario volver
                // a comprobar que no se ha creado la instancia
                if (INSTANCE == null) {
                    INSTANCE = new Aplicacion();
                }
            }
        }
    }

    public static Aplicacion getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    public Sucursal getSucursalDefault() {
        if (sucursal == null) {
            Resultado<Sucursal> defSucursal = EmpresaDao.getInstance().findSucursalByCodigo(defaultSucursal);
            if (!defSucursal.getResultado().equals(TipoResultado.SUCCESS)) {
                AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Traer sucursal por defecto", defSucursal.getMensaje());
                return null;
            }
            sucursal = defSucursal.get();
        }
        return sucursal;
    }

    public Empresa getEmpresaDefault() {
        if (empresa == null) {
            Resultado<Empresa> defEmpresa = EmpresaDao.getInstance().findEmpresaByCodigo(defaultEmpresa);
            if (!defEmpresa.getResultado().equals(TipoResultado.SUCCESS)) {
                AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Traer empresa por defecto", defEmpresa.getMensaje());
                return null;
            }
            empresa = defEmpresa.get();
        }
        return empresa;
    }

    public void cargaProperties() {
        Properties prop = new Properties();
        InputStream input = null;

        try {

            String filename = "sorteo.properties";
            input = Aplicacion.class.getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                System.out.println("Error al cargar archivo de configuración" + filename);
                return;
            }

            //load a properties file from class path, inside static method
            prop.load(input);
            if (prop.containsKey("default.empresa")) {
                defaultEmpresa = Integer.valueOf(prop.getProperty("default.empresa"));
            }
            if (prop.containsKey("default.sucursal")) {
                defaultSucursal = Integer.valueOf(prop.getProperty("default.sucursal"));
            }
            if (prop.containsKey("conexion.url")) {
                urlBD = prop.getProperty("conexion.url");
            }
            if (prop.containsKey("conexion.driver")) {
                driverBD = prop.getProperty("conexion.driver");
            }
            if (prop.containsKey("conexion.usuario")) {
                usuarioBD = prop.getProperty("conexion.usuario");
            }
            if (prop.containsKey("conexion.password")) {
                passwordBD = prop.getProperty("conexion.password");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //para que solamente exista una instancia del objeto
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /*
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        Aplicacion.usuario = usuario;
    }
     */
    public String getRolesUsuario() {
        return rolesUsuario;
    }

    public void setRolesUsuario(String rolesUsuario) {
        Aplicacion.rolesUsuario = rolesUsuario;
    }

    public Resultado<Object> getResultadoBusqueda() {
        return resultadoBusqueda;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        Aplicacion.usuario = usuario;
    }

    public void setResultadoBusqueda(Resultado<Object> resultadoBusqueda) {
        Aplicacion.resultadoBusqueda = resultadoBusqueda;
    }

    public Resultado<DetalleFactura> detalleSeleccionado() {
        return detalleSeleccionado;
    }

    public void setMontoIngresado(Resultado<DetalleFactura> detalleSeleccionado) {
        Aplicacion.detalleSeleccionado = detalleSeleccionado;
    }

    public MouseEvent getEventoMenu() {
        return eventoMenu;
    }

    public void setEventoMenu(MouseEvent evento) {
        Aplicacion.eventoMenu = evento;
    }

    public String getUrlBD() {
        return urlBD;
    }

    public String getDriverBD() {
        return driverBD;
    }

    public String getUsuarioBD() {
        return usuarioBD;
    }

    public String getPasswordBD() {
        return passwordBD;
    }

    /*
   los parametros deben cargarse antes de cada llamado a reporte
   param.put("pEncFac", encabezado);
		param.put("pIdVox", id);
		param.put("pDispensador", dispensador);
		param.put("pProducto", producto);
		param.put("pPrecio", precio);
		param.put("pVolumen", volumen);
		param.put("pVenta", venta);
		param.put("pCodCia", Parametros.codcia);
		param.put("pCodInv", Parametros.codinv);
     */
    public void imprimirReporte(String reporte, HashMap<String, Object> parametros) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // Crea la conexión para el reporte
            Connection connection = null;
            connection = DriverManager.getConnection(this.urlBD,
               this.usuarioBD, this.passwordBD); //hacer procedimiento para desencriptar

            if (connection != null) {
                JasperPrint print = JasperFillManager.fillReport(pathDir
                   + reporte + ".jasper", parametros, connection);
                JasperPrintManager.printReport(print, false);
                connection.close();
            }
        } catch (ClassNotFoundException | SQLException | JRException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generarReporte(String reporte, HashMap<String, Object> parametros) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // Crea la conexión para el reporte
            Connection connection = null;
            connection = DriverManager.getConnection(this.urlBD,
               this.usuarioBD, this.passwordBD); //hacer procedimiento para desencriptar

            if (connection != null) {
                JasperPrint print = JasperFillManager.fillReport(pathDir
                   + reporte + ".jasper", parametros, connection);
                JasperViewer.viewReport(print, false);
                connection.close();
            }
        } catch (ClassNotFoundException | SQLException | JRException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
