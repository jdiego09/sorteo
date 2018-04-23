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
import javax.persistence.NoResultException;
import javax.persistence.Query;
import sorteo.model.entities.Montos;
import sorteo.model.entities.TipoSorteo;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

/**
 *
 * @author jdiego
 */
public class MontosDao extends BaseDao<Integer, TipoSorteo> {

    private static MontosDao INSTANCE;
    private Montos montos;

    private MontosDao() {
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            // Sólo se accede a la zona sincronizada
            // cuando la instancia no está creada
            synchronized (MontosDao.class) {
                // En la zona sincronizada sería necesario volver
                // a comprobar que no se ha creado la instancia
                if (INSTANCE == null) {
                    INSTANCE = new MontosDao();
                }
            }
        }
    }

    public static MontosDao getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    public void setMontos(Montos montos) {
        this.montos = montos;
    }

    //para que solamente exista una instancia del objeto
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public Resultado<ArrayList<Montos>> findAll() {
        Resultado<ArrayList<Montos>> resultado = new Resultado<>();
        ArrayList<Montos> listaMontosResult = new ArrayList<>();
        List<Montos> listaMontos;
        try {
            Query query = getEntityManager().createNamedQuery("Montos.findAll");
            listaMontos = query.getResultList();
            listaMontos.stream().forEach(listaMontosResult::add);
            resultado.setResultado(TipoResultado.SUCCESS);
            resultado.set(listaMontosResult);
            return resultado;
        } catch (NoResultException nre) {
            resultado.setResultado(TipoResultado.WARNING);
            return resultado;
        } catch (Exception ex) {
            Logger.getLogger(MontosDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error al traer montos predefinidos.");
            return resultado;
        }
    }
}
