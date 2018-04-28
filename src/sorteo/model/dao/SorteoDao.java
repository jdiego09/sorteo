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
import sorteo.model.entities.Sorteo;
import sorteo.model.entities.TipoSorteo;
import sorteo.utils.Formater;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

/**
 *
 * @author jdiego
 */
public class SorteoDao extends BaseDao<Integer, Sorteo> {

    private static SorteoDao INSTANCE;
    private Sorteo sorteo;

    private SorteoDao() {
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            // Sólo se accede a la zona sincronizada
            // cuando la instancia no está creada
            synchronized (SorteoDao.class) {
                // oDEn la zona sincronizada sería necesario volver
                // a comprobar que no se ha creado la instancia
                if (INSTANCE == null) {
                    INSTANCE = new SorteoDao();
                }
            }
        }
    }

    public static SorteoDao getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    public void setSorteo(Sorteo sorteo) {
        this.sorteo = sorteo;
    }

    //para que solamente exista una instancia del objeto
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public Resultado<Sorteo> findByCodigo(Integer codigoSorteo) {
        Sorteo existe;
        Resultado<Sorteo> resultado = new Resultado<>();
        try {
            Query query = getEntityManager().createNamedQuery("Sorteo.findByCodigo");
            query.setParameter("codigo", codigoSorteo);
            existe = (Sorteo) query.getSingleResult();

            resultado.setResultado(TipoResultado.SUCCESS);
            resultado.set(existe);
            return resultado;
        } catch (NoResultException nre) {
            Logger.getLogger(SorteoDao.class.getName()).log(Level.SEVERE, null, nre);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("No existe el sorteo con el código [" + String.valueOf(codigoSorteo) + "].");
            return resultado;
        } catch (Exception ex) {
            Logger.getLogger(SorteoDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error consultando sorteo con el código [" + String.valueOf(codigoSorteo) + "].");
            return resultado;
        }
    }

    public Resultado<Sorteo> findByFecha(Date fechaSorteo, TipoSorteo tipoSorteo) {
        Sorteo existe;
        Resultado<Sorteo> resultado = new Resultado<>();
        try {
            Query query = getEntityManager().createNamedQuery("Sorteo.findByFecha");
            query.setParameter("fechaSorteo", fechaSorteo);
            query.setParameter("tipoSorteo", tipoSorteo.getCodigo());
            existe = (Sorteo) query.getSingleResult();

            resultado.setResultado(TipoResultado.SUCCESS);
            resultado.set(existe);
            return resultado;
        } catch (NoResultException nre) {
            Logger.getLogger(SorteoDao.class.getName()).log(Level.SEVERE, null, nre);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("No existe el sorteo [" + tipoSorteo.getDescripcion() + "] para la fecha [" + fechaSorteo + "].");
            return resultado;
        } catch (Exception ex) {
            Logger.getLogger(SorteoDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error consultando sorteo con el código [" + tipoSorteo.getDescripcion() + "] para la fecha [" + fechaSorteo + "].");
            return resultado;
        }
    }

    public Resultado<Double> getTotalApostadoNumero(Sorteo sorteo, int numero) {
        Resultado<Double> result = new Resultado<>();
        Query query = getEntityManager().createNamedQuery("Sorteo.getApuestaNumero");

        query.setParameter("codTSorteo", sorteo.getTipoSorteo().getCodigo());
        query.setParameter("codSorteo", sorteo.getCodigo());
        query.setParameter("fecSorteo", sorteo.getFecha());
        query.setParameter("numero", numero);
        try {
            Number resultado = (Number) query.getSingleResult();
            result.setResultado(TipoResultado.SUCCESS);
            result.set(resultado.doubleValue());
            return result;
        } catch (Exception ex) {
            Logger.getLogger(SorteoDao.class.getName()).log(Level.SEVERE, null, ex);
            result.setResultado(TipoResultado.ERROR);
            result.setMensaje("Error consultar monto apostado por número para el sorteo.");
            return result;
        }
    }

    public Resultado<ArrayList<Object>> getVentasDia(String qry, int codigoTipoSorteo, Date fechaVenta) {
        Resultado<ArrayList<Object>> resultado = new Resultado<>();
        ArrayList<Object> resultados = new ArrayList<>();
        List<Object> result;
        try {
            Query query = getEntityManager().createNamedQuery(qry);
            query.setParameter("codTSorteo", codigoTipoSorteo);
            query.setParameter("fecVenta", fechaVenta);
            result = query.getResultList();
            result.stream().forEach(resultados::add);
            resultado.setResultado(TipoResultado.SUCCESS);
            resultado.set(resultados);
            return resultado;
        } catch (NoResultException nre) {
            resultado.setResultado(TipoResultado.WARNING);
            return resultado;
        } catch (Exception ex) {
            Logger.getLogger(TipoSorteoDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error al traer las ventas del día: " + Formater.getInstance().formatFechaCorta.format(fechaVenta)
            + ".");
            return resultado;
        }
    }

    public Resultado<Sorteo> save() {
        Resultado<Sorteo> result = new Resultado<>();
        try {
            if (sorteo.getCodigo() == null || sorteo.getCodigo() <= 0) {
                //expediente.setExpFechaIngreso(new Date());
                //expediente.setExpUsuarioingresa(Aplicacion.getInstance().getUsuario().getUssCodigo());
                //expediente.getExpUsucodigo().setUsuSedcodigo(Aplicacion.getInstance().getDefaultSede());
                ;
            } else {
                /*expediente.setExpFechamodifica(new Date());
                expediente.setExpUsuariomodifica(Aplicacion.getInstance().getUsuario().getUssCodigo());
                expediente.getExpUsucodigo().setUsuUsuariomodifica(Aplicacion.getInstance().getUsuario().getUssCodigo());
                expediente.getExpUsucodigo().setUsuFechamodifica(new Date());*/
                ;
            }

            sorteo = (Sorteo) super.save(sorteo);
            result.setResultado(TipoResultado.SUCCESS);
            result.set(sorteo);
            result.setMensaje("El sorteo se guardó correctamente.");
            return result;
        } catch (Exception ex) {
            Logger.getLogger(SorteoDao.class.getName()).log(Level.SEVERE, null, ex);
            result.setResultado(TipoResultado.ERROR);
            result.setMensaje("Error al registrar el sorteo.");
            return result;
        }
    }
}
