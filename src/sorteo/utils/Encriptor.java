
package sorteo.utils;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;

public class Encriptor {
    private static Encriptor INSTANCE;
    private static ConfigurablePasswordEncryptor encryptor;

    private Encriptor() {
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            // Sólo se accede a la zona sincronizada
            // cuando la instancia no está creada
            synchronized (Encriptor.class) {
                // En la zona sincronizada sería necesario volver
                // a comprobar que no se ha creado la instancia
                if (INSTANCE == null) {
                    INSTANCE = new Encriptor();
                }
            }
        }
    }

    public static Encriptor getInstance() {
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

    public static String encriptar(String cadena) {
        String encriptado = null;
        try {
            if (cadena != null && !cadena.isEmpty()) {
                if (encryptor == null) {
                    encryptor = new ConfigurablePasswordEncryptor();
                    encryptor.setAlgorithm("SHA-1");
                    encryptor.setPlainDigest(true);
                }
                encriptado = encryptor.encryptPassword(cadena);
            }
        } catch (Exception ex) {
            encriptado = "Error: " + ex.getMessage();
        }
        return encriptado;

    }

}
