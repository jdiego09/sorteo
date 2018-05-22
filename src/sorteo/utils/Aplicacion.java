/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.utils;

import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
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
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PrinterName;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;

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
    private static Resultado<String> ventaAutorizada;

    final String pathDir = System.getProperty("user.dir") + "/src/sorteo/reportes/";

    //parámetros
    private static Integer defaultEmpresa;
    private static Integer defaultSucursal;

    private static String urlBD;
    private static String driverBD;
    private static String usuarioBD;
    private static String passwordBD;

    private static String pathReportes;
    private static String imprTicket;

    private static String appProductId;
    private static String appLicenceKey;
    private static String appAuthorizedHost;

    private static Connection connection;

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
            if (prop.containsKey("path.reportes")) {
                pathReportes = prop.getProperty("path.reportes");
            }
            if (prop.containsKey("default.impTicket")) {
                imprTicket = prop.getProperty("default.impTicket");
            }
            if (prop.containsKey("app.productid")) {
                appProductId = prop.getProperty("app.productid");
            }
            if (prop.containsKey("app.licencekey")) {
                appLicenceKey = prop.getProperty("app.licencekey");
            }
            if (prop.containsKey("app.authorizedHost")) {
                appAuthorizedHost = prop.getProperty("app.authorizedHost");
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

    public Resultado<String> getVentaAutorizada() {
        if (ventaAutorizada == null) {
            ventaAutorizada = new Resultado<>();
        }
        return ventaAutorizada;
    }

    public void setVentaAutorizada(Resultado<String> autorizacion) {
        Aplicacion.ventaAutorizada = autorizacion;
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

    public String getPathReportes() {
        return pathReportes;
    }

    public String getImprTicket() {
        return imprTicket;
    }

    public String getAppProductId() {
        return appProductId;
    }

    public String getAppLicenceKey() {
        return appLicenceKey;
    }

    public String getAppAuthorizedHost() {
        return appAuthorizedHost;
    }

    private Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(getUrlBD(),
                   getUsuarioBD(), getPasswordBD()); //hacer procedimiento para desencriptar
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return connection;
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

        if (connection != null) {
            System.out.println(pathDir);
            JasperPrint print;
            try {
                print = JasperFillManager.fillReport(pathDir
                   + reporte + ".jasper", parametros, getConnection());
                JasperPrintManager.printReport(print, false);
            } catch (JRException ex) {
                Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void generarReporte(String reporte, HashMap<String, Object> parametros) {
        try {
            if (connection != null) {
                System.out.println(pathDir);
                JasperPrint print = JasperFillManager.fillReport(pathReportes
                   + reporte + ".jasper", parametros, getConnection());
                JasperViewer.viewReport(print, false);
            }
        } catch (JRException ex) {
            Logger.getLogger(Aplicacion.class
               .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void print(String reporte, HashMap<String, Object> parametros) {
        try {

            JasperPrint jasperPrint = JasperFillManager.fillReport(pathReportes
               + reporte + ".jasper", parametros, getConnection());

            if (getImprTicket() != null || !getImprTicket().isEmpty()) {
                PrintService[] services = PrintServiceLookup
                   .lookupPrintServices(null, null);
                int selectedService = 0;
                /* Scan found services to see if anyone suits our needs */
                for (int i = 0; i < services.length; i++) {
                    if (services[i].getName().toUpperCase()
                       .contains(getImprTicket().toUpperCase())) {
                        selectedService = i;
                    }
                }

                JRPrintServiceExporter exporter;
                exporter = new JRPrintServiceExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT,
                   jasperPrint);
                /* We set the selected service and pass it as a paramenter */
                exporter.setParameter(
                   JRPrintServiceExporterParameter.PRINT_SERVICE,
                   services[selectedService]);
                exporter.setParameter(
                   JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG,
                   Boolean.FALSE);
                exporter.setParameter(
                   JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,
                   Boolean.FALSE);
                exporter.exportReport();
            } else {
                JasperPrintManager.printReport(jasperPrint, false);
            }

        } catch (JRException e) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public boolean appValidUse() {
        if (getAppProductId() == null || getAppProductId().isEmpty()) {
            return false;
        }
        if (getAppAuthorizedHost() == null || getAppAuthorizedHost().isEmpty()) {
            return false;
        }
        if (getAppLicenceKey() == null || getAppLicenceKey().isEmpty()) {
            return false;
        }
        String key = Encriptor.getInstance().encriptar(getMac());
        return (getAppLicenceKey().equals(Encriptor.getInstance().encriptar(getAppProductId() + key)));
    }

    private static String getMac() {
        Process p;
        try {
            p = Runtime.getRuntime().exec("getmac /fo csv /nh");
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()));
            String line;
            line = in.readLine();
            String[] result = line.split(",");
            return (result[0].replace('"', ' ').trim());
        } catch (IOException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
