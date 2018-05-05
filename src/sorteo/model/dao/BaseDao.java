package sorteo.model.dao;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;
import sorteo.utils.Aplicacion;
import sorteo.utils.Parametros;

public class BaseDao<K, E> implements DaoBase<K, E> {

    private Class<E> entityClass;
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = Parametros.getENTITYMANAGERFACTORY().createEntityManager();
        }
        return entityManager;
    }

    @Override
    public E save(E entity) {
        try {
            E existe = null;
            getEntityManager().getTransaction().begin();
            K id = (K) Parametros.PERSISTENCEUTIL.getIdentifier(entity);
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
            K id = (K) Parametros.PERSISTENCEUTIL.getIdentifier(entity);
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
