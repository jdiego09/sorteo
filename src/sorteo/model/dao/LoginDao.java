package sorteo.model.dao;

import javax.persistence.Query;
import sorteo.model.entities.Usuario;

public class LoginDao extends BaseDao {

    public Usuario findByUsuCodigo(String codigo) {
        Usuario usuario = new Usuario();
        try {
            Query query = getEntityManager().createNamedQuery("Usuario.findByUsuCodigo");
            query.setParameter("usuCodigo", codigo);
            usuario = (Usuario) query.getSingleResult();
            return usuario;
        } catch (Exception ex) {
            return usuario;
        }
    }

}
