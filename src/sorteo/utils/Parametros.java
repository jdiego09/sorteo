package sorteo.utils;
//hola
import java.util.HashMap;
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
    private static final EntityManagerFactory ENTITYMANAGERFACTORY = Persistence.createEntityManagerFactory("SorteoPU" );
    public static final PersistenceUnitUtil PERSISTENCEUTIL = ENTITYMANAGERFACTORY.getPersistenceUnitUtil();

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
    
    public void setParametro(String parametro, String valor){
        if (parametros.get(parametro)==null){
            parametros.put(parametro, valor);
        }
    }
    public String getParametro(String parametro) {
        return parametros.get(parametro);
    }

    public static EntityManagerFactory getENTITYMANAGERFACTORY() {
        return ENTITYMANAGERFACTORY;
    }
}
