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
import sorteo.model.entities.TipoSorteo;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

/**
 *
 * @author jdiego
 */
public class TipoSorteoDao extends BaseDao<Integer, TipoSorteo> {

    private static TipoSorteoDao INSTANCE;
    private TipoSorteo tipoSorteo;

    private TipoSorteoDao() {
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            // Sólo se accede a la zona sincronizada
            // cuando la instancia no está creada
            synchronized (TipoSorteoDao.class) {
                // En la zona sincronizada sería necesario volver
                // a comprobar que no se ha creado la instancia
                if (INSTANCE == null) {
                    INSTANCE = new TipoSorteoDao();
                }
            }
        }
    }

    public static TipoSorteoDao getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    public void setTipoSorteo(TipoSorteo tipoSorteo) {
        this.tipoSorteo = tipoSorteo;
    }

    //para que solamente exista una instancia del objeto
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public Resultado<TipoSorteo> findByCodigo(Integer tipoSorteo) {
        TipoSorteo existe;
        Resultado<TipoSorteo> resultado = new Resultado<>();
        try {
            Query query = getEntityManager().createNamedQuery("TipoSorteo.findByCodigo");
            query.setParameter("codigo", tipoSorteo);
            existe = (TipoSorteo) query.getSingleResult();

            resultado.setResultado(TipoResultado.SUCCESS);
            resultado.set(existe);
            return resultado;
        } catch (NoResultException nre) {
            Logger.getLogger(TipoSorteoDao.class.getName()).log(Level.SEVERE, null, nre);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("No existe el tipo de sorteo con el código [" + String.valueOf(tipoSorteo) + "].");
            return resultado;
        } catch (Exception ex) {
            Logger.getLogger(TipoSorteoDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error consultando sorteo con el código [" + String.valueOf(tipoSorteo) + "].");
            return resultado;
        }
    }

    public Resultado<ArrayList<TipoSorteo>> findAllActivos() {
        Resultado<ArrayList<TipoSorteo>> resultado = new Resultado<>();
        ArrayList<TipoSorteo> listaTiposSorteo = new ArrayList<>();
        List<TipoSorteo> tiposSorteo;
        try {
            Query query = getEntityManager().createNamedQuery("TipoSorteo.findAllActivos");
            tiposSorteo = query.getResultList();
            tiposSorteo.stream().forEach(listaTiposSorteo::add);
            resultado.setResultado(TipoResultado.SUCCESS);
            resultado.set(listaTiposSorteo);
            return resultado;
        } catch (NoResultException nre) {
            resultado.setResultado(TipoResultado.WARNING);
            return resultado;
        } catch (Exception ex) {
            Logger.getLogger(TipoSorteoDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error al traer tipos de sorteo.");
            return resultado;
        }
    }

    public Resultado<TipoSorteo> save() {
        Resultado<TipoSorteo> result = new Resultado<>();
        try {
            tipoSorteo = (TipoSorteo) super.save(tipoSorteo);
            if (this.tipoSorteo.getCodigo() != null) {
                result.setResultado(TipoResultado.SUCCESS);
                result.set(tipoSorteo);
                result.setMensaje("El sorteo se guardó correctamente.");

            } else {
                result.setResultado(TipoResultado.ERROR);
                result.setMensaje("No se pudo guardar el sorteo.");
            }

            return result;

        } catch (Exception ex) {
            Logger.getLogger(SorteoDao.class
               .getName()).log(Level.SEVERE, null, ex);
            result.setResultado(TipoResultado.ERROR);
            result.setMensaje("Error al registrar el sorteo.");
            return result;
        }
    }

}
