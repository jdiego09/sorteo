package sorteo.model.dao;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;
import sorteo.utils.Aplicacion;

public class BaseDao<K, E> implements DaoBase<K, E> {

   private Class<E> entityClass;
   private EntityManager entityManager;
   private static PersistenceUnitUtil PERSISTENCEUTIL;

   public EntityManager getEntityManager() {
      String url = Aplicacion.getInstance().getUrlBD();
      String driver = Aplicacion.getInstance().getDriverBD();
      String usuario = Aplicacion.getInstance().getUsuarioBD();
      String password = Aplicacion.getInstance().getPasswordBD();

      Properties propiedades = new Properties();
      if (usuario != null) {
         propiedades.put("javax.persistence.jdbc.user", usuario);
      }
      if (driver != null) {
         propiedades.put("javax.persistence.jdbc.driver", driver);
      }
      if (password != null) {
         propiedades.put("javax.persistence.jdbc.password", password);
      }
      if (url != null) {
         propiedades.put("javax.persistence.jdbc.url", url);
      }
      EntityManagerFactory emf = null;
      try {
         emf = Persistence.createEntityManagerFactory("SorteoPU",
                 propiedades);
         if (entityManager == null) {
            entityManager = emf.createEntityManager();
         }
         PERSISTENCEUTIL = emf.getPersistenceUnitUtil();
      } catch (Exception e) {
         System.out.println(e.getMessage());
      }
      return entityManager;
   }

   public static PersistenceUnitUtil getPERSISTENCEUTIL() {
      return PERSISTENCEUTIL;
   }

   @Override
   public E save(E entity) {
      try {
         E existe = null;
         getEntityManager().getTransaction().begin();
         K id = (K) PERSISTENCEUTIL.getIdentifier(entity);
         if (id != null) {
            existe = (E) getEntityManager().find(entity.getClass(), id);
         }
         if (existe != null) {
            getEntityManager().merge(entity);
         } else {
            getEntityManager().persist(entity);
         }
         getEntityManager().getTransaction().commit();
         return entity;
      } catch (Exception ex) {
         getEntityManager().getTransaction().rollback();
         Logger.getLogger(BaseDao.class.getName()).log(Level.SEVERE, null, ex);
         return entity;
      }
   }

   @Override
   public void delete(E entity) {
      try {
         getEntityManager().getTransaction().begin();
         K id = (K) PERSISTENCEUTIL.getIdentifier(entity);
         E existe = null;
         if (id != null) {
            existe = (E) getEntityManager().find(entity.getClass(), id);
         }
         if (existe != null) {
            if (!getEntityManager().contains(entity)) {
               entity = getEntityManager().merge(entity);
            }
            getEntityManager().remove(entity);
         }
         getEntityManager().getTransaction().commit();

      } catch (Exception ex) {
         getEntityManager().getTransaction().rollback();
         Logger.getLogger(BaseDao.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @Override
   public E findById(K id) {
      try {
         return getEntityManager().find(entityClass, id);
      } catch (Exception ex) {
         Logger.getLogger(BaseDao.class.getName()).log(Level.SEVERE, null, ex);
         return null;
      }
   }

}
