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
import sorteo.model.entities.Empresa;
import sorteo.model.entities.Sucursal;
import sorteo.utils.Parametros;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

/**
 *
 * @author Luis Diego
 */
public class EmpresaDao extends BaseDao<Integer, Empresa> {

    private static EmpresaDao INSTANCE;
    private Empresa empresa;

    private EmpresaDao() {
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            // Sólo se accede a la zona sincronizada
            // cuando la instancia no está creada
            synchronized (EmpresaDao.class) {
                // En la zona sincronizada sería necesario volver
                // a comprobar que no se ha creado la instancia
                if (INSTANCE == null) {
                    INSTANCE = new EmpresaDao();
                }
            }
        }
    }

    public static EmpresaDao getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = this.empresa;
    }

    //para que solamente exista una instancia del objeto
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public Resultado<Empresa> getCentro(String cedulaJuridica) {
        Resultado<Empresa> resultado = new Resultado<>();
        try {
            Query query = getEntityManager().createNamedQuery("Empresa.findByCedulajuridica");
            query.setParameter("Cedulajuridica", cedulaJuridica);
            empresa = (Empresa) query.getSingleResult();

            resultado.setResultado(TipoResultado.SUCCESS);
            resultado.set(empresa);
            return resultado;
        } catch (NoResultException nre) {
            resultado.setResultado(TipoResultado.WARNING);
            resultado.setMensaje("La empresa con la cédula jurídica [" + cedulaJuridica + "], no se encuentra registrado.");
            return resultado;
        } catch (Exception ex) {
            Logger.getLogger(EmpresaDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error al traer información de la empresa con la cédula jurídica [" + cedulaJuridica + "].");
            return resultado;
        }
    }

    public Resultado<ArrayList<Sucursal>> getSucursales(Empresa empresa) {
        Resultado<ArrayList<Sucursal>> resultado = new Resultado<>();
        ArrayList<Sucursal> listaSucursales = new ArrayList<>();
        List<Sucursal> sucursales;
        try {
            Query query = getEntityManager().createNamedQuery("Sucursal.findByCodigoEmpresa");
            query.setParameter("codigoEmpresa", empresa.getCodigo());
            sucursales = query.getResultList();
            sucursales.stream().forEach(listaSucursales::add);
            resultado.setResultado(TipoResultado.SUCCESS);
            resultado.set(listaSucursales);
            return resultado;
        } catch (NoResultException nre) {
            resultado.setResultado(TipoResultado.WARNING);
            return resultado;
        } catch (Exception ex) {
            Logger.getLogger(EmpresaDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error al traer las sucursales para la epmresa [" + empresa.getCodigo() + "].");
            return resultado;
        }
    }

    public Resultado<ArrayList<Empresa>> findAll() {
        Resultado<ArrayList<Empresa>> resultado = new Resultado<>();
        ArrayList<Empresa> empresas = new ArrayList<>();
        List<Empresa> resultados;
        try {
            Query query = getEntityManager().createNamedQuery("Empresa.findAll");
            resultados = query.getResultList();
            resultados.forEach(m -> {
                empresas.add(m);
            });
            resultado.setResultado(TipoResultado.SUCCESS);
            resultado.set(empresas);
            return resultado;
        } catch (Exception ex) {
            Logger.getLogger(EmpresaDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error consultando las empresas.");
            return resultado;
        }
    }

    public Resultado<Sucursal> findSucursalByCodigo(Integer codigoSucursal) {
        Sucursal sucursal;
        Resultado<Sucursal> resultado = new Resultado<>();
        try {
            Query query = getEntityManager().createNamedQuery("Sucursal.findByCodigo");
            query.setParameter("codigo", codigoSucursal);
            sucursal = (Sucursal) query.getSingleResult();

            resultado.setResultado(TipoResultado.SUCCESS);
            resultado.set(sucursal);
            return resultado;
        } catch (NoResultException nre) {
            Logger.getLogger(EmpresaDao.class.getName()).log(Level.SEVERE, null, nre);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("No existe la ssucursal con el código [" + String.valueOf(codigoSucursal) + "].");
            return resultado;
        } catch (Exception ex) {
            Logger.getLogger(EmpresaDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error consultando la sucursal con el código [" + String.valueOf(codigoSucursal) + "].");
            return resultado;
        }
    }

    public Resultado<Empresa> findEmpresaByCodigo(Integer codigoEmpresa) {
        Empresa queryResult;
        Resultado<Empresa> resultado = new Resultado<>();
        try {
            Query query = getEntityManager().createNamedQuery("Empresa.findByCodigo");
            query.setParameter("codigo", codigoEmpresa);
            queryResult = (Empresa) query.getSingleResult();

            resultado.setResultado(TipoResultado.SUCCESS);
            resultado.set(queryResult);
            return resultado;
        } catch (NoResultException nre) {
            Logger.getLogger(EmpresaDao.class.getName()).log(Level.SEVERE, null, nre);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("No existe la empresa con el código [" + String.valueOf(codigoEmpresa) + "].");
            return resultado;
        } catch (Exception ex) {
            Logger.getLogger(EmpresaDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error consultando la empresa con el código [" + String.valueOf(codigoEmpresa) + "].");
            return resultado;
        }
    }

    // Procedimiento para guardar la información del empresa.
    public Resultado<Empresa> save() {
        Resultado<Empresa> resultado = new Resultado<>();
        try {

            empresa = (Empresa) super.save(empresa);

            if (empresa.getCodigo() != null) {
                resultado.setResultado(TipoResultado.SUCCESS);
                resultado.set(empresa);
                resultado.setMensaje("Empresa guardada correctamente.");

            } else {
                resultado.setResultado(TipoResultado.ERROR);
                resultado.set(empresa);
                resultado.setMensaje("No se pudo guardar la empresa.");
            }

            return resultado;

        } catch (Exception ex) {
            Logger.getLogger(EmpresaDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error al guardar la empresa [" + this.empresa.getCodigo() + "].");
            return resultado;
        }
    }

    public Resultado<String> deleteSucursal(Sucursal sucursal) {
        Resultado<String> resultado = new Resultado<>();
        Sucursal existe = null;
        try {

            getEntityManager().getTransaction().begin();
            Integer id = (Integer) Parametros.PERSISTENCEUTIL.getIdentifier(sucursal);
            if (id != null) {
                existe = (Sucursal) getEntityManager().find(Sucursal.class, id);
            }

            if (existe != null) {
                if (!getEntityManager().contains(sucursal)) {
                    sucursal = getEntityManager().merge(sucursal);
                }
                getEntityManager().remove(sucursal);
            }
            getEntityManager().getTransaction().commit();
            resultado.setResultado(TipoResultado.SUCCESS);
            return resultado;

        } catch (Exception ex) {
            getEntityManager().getTransaction().rollback();
            Logger.getLogger(EmpresaDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error al eliminar la sucursal [" + sucursal.getNombre() + "].");
            return resultado;
        }
    }
}
