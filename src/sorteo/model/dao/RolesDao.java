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
import javax.persistence.Query;
import sorteo.model.entities.Roles;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

/**
 *
 * @author Luis Diego
 */
public class RolesDao extends BaseDao<String, Roles> {

    private static RolesDao INSTANCE;
    private Roles rol;

    public RolesDao() {

    }

    private static void createInstance() {
        if (INSTANCE == null) {
            // Sólo se accede a la zona sincronizada
            // cuando la instancia no está creada
            synchronized (RolesDao.class) {
                // En la zona sincronizada sería necesario volver
                // a comprobar que no se ha creado la instancia
                if (INSTANCE == null) {
                    INSTANCE = new RolesDao();
                }
            }
        }
    }

    public static RolesDao getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    public void setRol(Roles rol) {
        this.rol = rol;
    }

    //para que solamente exista una instancia del objeto
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public Resultado<ArrayList<Roles>> findRolesByEstado() {
        Resultado<ArrayList<Roles>> result = new Resultado<>();
        ArrayList<Roles> roles = new ArrayList<>();
        List<Roles> resultados;
        try {
            Query query = getEntityManager().createNamedQuery("Roles.findByEstado");
            query.setParameter("estado", "A");
            resultados = query.getResultList();
            resultados.forEach(m -> {
                roles.add(m);
            });
            result.setResultado(TipoResultado.SUCCESS);
            result.set(roles);
            return result;
        } catch (Exception ex) {
            Logger.getLogger(RolesDao.class.getName()).log(Level.SEVERE, null, ex);
            result.setResultado(TipoResultado.ERROR);
            result.setMensaje("Error consultando los roles en estado activo.");
            return result;
        }
    }

}
