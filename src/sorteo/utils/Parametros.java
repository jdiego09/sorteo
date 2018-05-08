package sorteo.utils;
//hola

import java.util.HashMap;
import java.util.Properties;
import javafx.scene.Parent;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;

/*import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;*/
public class Parametros {

    private String pathViews = "view/";
    private static HashMap<String, String> parametros = new HashMap<>();
    private static Parametros INSTANCE;
    private static EntityManagerFactory ENTITYMANAGERFACTORY; // = Persistence.createEntityManagerFactory("SorteoPU");
    public static PersistenceUnitUtil PERSISTENCEUTIL;// = ENTITYMANAGERFACTORY.getPersistenceUnitUtil();

    private Parametros() {
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            // Sólo se accede a la zona sincronizada
            // cuando la instancia no está creada
            synchronized (Parametros.class) {
                // En la zona sincronizada sería necesario volver
                // a comprobar que no se ha creado la instancia
                if (INSTANCE == null) {
                    INSTANCE = new Parametros();
                }
            }
        }
    }

    public static Parametros getInstance() {
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

    public void setParametro(String parametro, String valor) {
        if (parametros.get(parametro) == null) {
            parametros.put(parametro, valor);
        }
    }

    public String getParametro(String parametro) {
        return parametros.get(parametro);
    }

    public static EntityManagerFactory getENTITYMANAGERFACTORY() {
        return ENTITYMANAGERFACTORY;
    }
    
    public void setEntityManager(){
        String url = Aplicacion.getInstance().getUrlBD();
        String driver = Aplicacion.getInstance().getDriverBD();
        String usuario = Aplicacion.getInstance().getUsuarioBD();
        String password = Aplicacion.getInstance().getPasswordBD();
        
        Properties props = new Properties();
        props.put("javax.persistence.jdbc.url", url);
        props.put("javax.persistence.jdbc.driver", driver);
        props.put("javax.persistence.jdbc.user", usuario);
        props.put("javax.persistence.jdbc.password", password);
       ENTITYMANAGERFACTORY = Persistence.createEntityManagerFactory("SorteoPU",props);
       PERSISTENCEUTIL = ENTITYMANAGERFACTORY.getPersistenceUnitUtil();
    }
}
