package sorteo.model.dao;

public interface DaoBase<K, E> {

    E save (E entity);

    void delete(E entity);

    E findById(K id);
}
