/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import sorteo.model.entities.Exclusion;
import sorteo.model.entities.TipoSorteo;
import sorteo.utils.Formater;
import sorteo.utils.Parametros;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

/**
 *
 * @author jdiego
 */
public class ExclusionDao extends BaseDao<Integer, Exclusion> {

    private static ExclusionDao INSTANCE;
    private Exclusion exclusion;

    private ExclusionDao() {
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            // Sólo se accede a la zona sincronizada
            // cuando la instancia no está creada
            synchronized (EmpresaDao.class) {
                // En la zona sincronizada sería necesario volver
                // a comprobar que no se ha creado la instancia
                if (INSTANCE == null) {
                    INSTANCE = new ExclusionDao();
                }
            }
        }
    }

    public static ExclusionDao getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    public void setExclusion(Exclusion exclusion) {
        this.exclusion = exclusion;
    }

    //para que solamente exista una instancia del objeto
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public Resultado<ArrayList<Exclusion>> findBySorteo(TipoSorteo tipoSorteo, Date fecha) {
        Resultado<ArrayList<Exclusion>> resultado = new Resultado<>();
        ArrayList<Exclusion> exclusiones = new ArrayList<>();
        List<Exclusion> resultados;
        try {
            Query query = getEntityManager().createNamedQuery("Exclusion.findBySorteo");
            query.setParameter("codtiposorteo", tipoSorteo.getCodigo());
            query.setParameter("fechaSorteo", fecha);
            resultados = query.getResultList();
            resultados.forEach(m -> {
                exclusiones.add(m);
            });
            resultado.setResultado(TipoResultado.SUCCESS);
            resultado.set(exclusiones);
            return resultado;
        } catch (Exception ex) {
            Logger.getLogger(ExclusionDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error consultando las excepciones del sorteo: " + tipoSorteo.getDescripcion()
               + ", del día: " + Formater.getInstance().formatFechaCorta.format(fecha));
            return resultado;
        }
    }

    public Resultado<Exclusion> findExclusion(Exclusion exclusion) {
        Resultado<Exclusion> result = new Resultado<>();
        Exclusion excepcion;
        try {
            Query query = getEntityManager().createNamedQuery("Exclusion.findExclusion");
            query.setParameter("codtiposorteo", exclusion.getExcCodtiposorteo().getCodigo());
            query.setParameter("fechaSorteo", exclusion.getExcFecha());
            query.setParameter("bloqueo", exclusion.getExcBloqueo());
            query.setParameter("numero", exclusion.getExcNumero());
            excepcion = (Exclusion) query.getSingleResult();

            result.setResultado(TipoResultado.SUCCESS);
            result.set(excepcion);
            return result;
        } catch (Exception ex) {
            Logger.getLogger(ExclusionDao.class.getName()).log(Level.SEVERE, null, ex);
            result.setResultado(TipoResultado.ERROR);
            result.setMensaje("Error al consultar excepcion.");
            return result;
        }
    }

    // Procedimiento para guardar la información del empresa.
    public Resultado<Exclusion> save() {
        Resultado<Exclusion> resultado = new Resultado<>();
        try {

            exclusion = (Exclusion) super.save(exclusion);

            if (exclusion.getExcId() != null) {
                resultado.setResultado(TipoResultado.SUCCESS);
                resultado.set(exclusion);
                resultado.setMensaje("Excepción del sorteo guardada exitosamente.");

            } else {
                resultado.setResultado(TipoResultado.ERROR);
                resultado.set(exclusion);
                resultado.setMensaje("No se pudo guardar la excepción para el sorteo.");
            }

            return resultado;

        } catch (Exception ex) {
            Logger.getLogger(ExclusionDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error al guardar la excepcion para el sorteo [" + this.exclusion.getExcCodtiposorteo().getDescripcion() + "].");
            return resultado;
        }
    }

    public Resultado<String> deleteExclusion(Exclusion exclusion) {
        Resultado<String> resultado = new Resultado<>();
        Exclusion existe = null;
        try {

            getEntityManager().getTransaction().begin();
            Integer id = (Integer) Parametros.PERSISTENCEUTIL.getIdentifier(exclusion);
            if (id != null) {
                existe = (Exclusion) getEntityManager().find(Exclusion.class, id);
            }

            if (existe != null) {
                if (!getEntityManager().contains(exclusion)) {
                    exclusion = getEntityManager().merge(exclusion);
                }
                getEntityManager().remove(exclusion);
            }
            getEntityManager().getTransaction().commit();
            resultado.setResultado(TipoResultado.SUCCESS);
            return resultado;

        } catch (Exception ex) {
            getEntityManager().getTransaction().rollback();
            Logger.getLogger(ExclusionDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error al eliminar la exclusión para el sorteo [" + exclusion.getExcCodtiposorteo().getDescripcion() + "].");
            return resultado;
        }
    }
}
