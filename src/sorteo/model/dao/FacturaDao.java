/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.dao;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import sorteo.model.entities.Factura;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

/**
 *
 * @author jdiego
 */
public class FacturaDao extends BaseDao<Integer, Factura> {

   private static FacturaDao INSTANCE;
   private Factura factura;

   private FacturaDao() {
   }

   private static void createInstance() {
      if (INSTANCE == null) {
         // Sólo se accede a la zona sincronizada
         // cuando la instancia no está creada
         synchronized (SorteoDao.class) {
            // oDEn la zona sincronizada sería necesario volver
            // a comprobar que no se ha creado la instancia
            if (INSTANCE == null) {
               INSTANCE = new FacturaDao();
            }
         }
      }
   }

   public static FacturaDao getInstance() {
      if (INSTANCE == null) {
         createInstance();
      }
      return INSTANCE;
   }

   public void setFactura(Factura factura) {
      this.factura = factura;
   }

   //para que solamente exista una instancia del objeto
   @Override
   public Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException();
   }

   public Resultado<Factura> findByCodigo(Integer codigoFactura) {
      Factura existe;
      Resultado<Factura> resultado = new Resultado<>();
      try {
         Query query = getEntityManager().createNamedQuery("Factura.findByCodigo");
         query.setParameter("codigo", codigoFactura);
         existe = (Factura) query.getSingleResult();

         resultado.setResultado(TipoResultado.SUCCESS);
         resultado.set(existe);
         return resultado;
      } catch (NoResultException nre) {
         Logger.getLogger(FacturaDao.class.getName()).log(Level.SEVERE, null, nre);
         resultado.setResultado(TipoResultado.ERROR);
         resultado.setMensaje("No existe la factura con el código [" + String.valueOf(codigoFactura) + "].");
         return resultado;
      } catch (Exception ex) {
         Logger.getLogger(FacturaDao.class.getName()).log(Level.SEVERE, null, ex);
         resultado.setResultado(TipoResultado.ERROR);
         resultado.setMensaje("Error consultando la factura el código [" + String.valueOf(codigoFactura) + "].");
         return resultado;
      }
   }

   public Resultado<Factura> save() {
      Resultado<Factura> result = new Resultado<>();
      try {
         if (factura.getCodigo() == null || factura.getCodigo() <= 0) {
            factura.setFecha(new Date());
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

         factura = (Factura) super.save(factura);
         result.setResultado(TipoResultado.SUCCESS);
         result.set(factura);
         result.setMensaje("Venta aplicada exitosamente.");
         return result;
      } catch (Exception ex) {
         Logger.getLogger(FacturaDao.class.getName()).log(Level.SEVERE, null, ex);
         result.setResultado(TipoResultado.ERROR);
         result.setMensaje("Error al aplicar la venta.");
         return result;
      }
   }
   
   
   
}
