/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javax.persistence.Query;
import sorteo.model.entities.Menu;
import sorteo.model.entities.MenuXRol;
import sorteo.utils.Aplicacion;
import sorteo.utils.AppWindowController;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

/**
 *
 * @author jdiego
 */
public class SeguridadDao extends BaseDao {

    private static SeguridadDao INSTANCE;

    private SeguridadDao() {
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            // Sólo se accede a la zona sincronizada
            // cuando la instancia no está creada
            synchronized (SorteoDao.class) {
                // oDEn la zona sincronizada sería necesario volver
                // a comprobar que no se ha creado la instancia
                if (INSTANCE == null) {
                    INSTANCE = new SeguridadDao();
                }
            }
        }
    }

    public static SeguridadDao getInstance() {
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

    public Resultado<ArrayList<Menu>> getAccesosUsuario(String codigoRol) {
        Resultado<ArrayList<Menu>> result = new Resultado<>();
        ArrayList<Menu> accesos = new ArrayList<>();
        List<Menu> resultados;
        try {
            Query query = getEntityManager().createNamedQuery("MenuXRol.findByCodRol");
            query.setParameter("codRol", codigoRol);
            resultados = query.getResultList();
            resultados.forEach(accesos::add);
            result.setResultado(TipoResultado.SUCCESS);
            result.set(accesos);
            return result;
        } catch (Exception ex) {
            Logger.getLogger(SeguridadDao.class.getName()).log(Level.SEVERE, null, ex);
            result.setResultado(TipoResultado.ERROR);
            result.setMensaje("No se pudo cargar los accesos del usuario [" + Aplicacion.getInstance().getUsuario().getUsuCodigo() + "].");
            return result;
        }
    }
}
