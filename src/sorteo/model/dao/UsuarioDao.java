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
import sorteo.model.entities.Roles;
import sorteo.model.entities.RolXUsuario;
import sorteo.model.entities.Usuario;
import sorteo.utils.Aplicacion;
import sorteo.utils.Parametros;
import sorteo.utils.Resultado;
import sorteo.utils.TipoResultado;

/**
 *
 * @author Luis Diego
 */
public class UsuarioDao extends BaseDao<Integer, Usuario> {

    private static UsuarioDao INSTANCE;
    private Usuario usuarioSistema;

    public UsuarioDao() {

    }

    private static void createInstance() {
        if (INSTANCE == null) {
            // Sólo se accede a la zona sincronizada
            // cuando la instancia no está creada
            synchronized (UsuarioDao.class) {
                // En la zona sincronizada sería necesario volver
                // a comprobar que no se ha creado la instancia
                if (INSTANCE == null) {
                    INSTANCE = new UsuarioDao();
                }
            }
        }
    }

    public static UsuarioDao getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    public void setUsuarioSistema(Usuario usuarioSistema) {
        this.usuarioSistema = usuarioSistema;
    }

    //para que solamente exista una instancia del objeto
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public Resultado<Usuario> getUsuarioByCodigo(String codigo) {
        Resultado<Usuario> result = new Resultado<>();
        Usuario usuario;
        try {
            Query query = getEntityManager().createNamedQuery("Usuario.findByUsuCodigo");
            query.setParameter("usuCodigo", codigo);
            usuario = (Usuario) query.getSingleResult();

            result.setResultado(TipoResultado.SUCCESS);
            result.set(usuario);
            return result;

        } catch (NoResultException nre) {
            result.setResultado(TipoResultado.WARNING);
            result.setMensaje("El usuario con la código [" + codigo + "], no se encuentra registrado.");
            return result;
        } catch (Exception ex) {
            Logger.getLogger(UsuarioDao.class.getName()).log(Level.SEVERE, null, ex);
            result.setResultado(TipoResultado.ERROR);
            result.setMensaje("Error al traer información del usuario con código [" + codigo + "].");
            return result;
        }
    }

    public Resultado<ArrayList<Usuario>> findUsuariosSistema() {
        Resultado<ArrayList<Usuario>> result = new Resultado<>();
        ArrayList<Usuario> usuarios = new ArrayList<>();
        List<Usuario> resultados;
        try {
            Query query = getEntityManager().createNamedQuery("Usuario.findAll");
            resultados = query.getResultList();
            resultados.forEach(m -> {
                usuarios.add(m);
            });
            result.setResultado(TipoResultado.SUCCESS);
            result.set(usuarios);
            return result;
        } catch (Exception ex) {
            Logger.getLogger(UsuarioDao.class.getName()).log(Level.SEVERE, null, ex);
            result.setResultado(TipoResultado.ERROR);
            result.setMensaje("Error consultando los usuarios del sistema.");
            return result;
        }
    }

    public Resultado<ArrayList<RolXUsuario>> getRolesUsuario(String usuario) {
        Resultado<ArrayList<RolXUsuario>> resultado = new Resultado<>();
        ArrayList<RolXUsuario> listaRoles = new ArrayList<>();
        List<RolXUsuario> roles;
        try {
            Query query = getEntityManager().createNamedQuery("RolXUsuario.findByUsuario");
            query.setParameter("codUsuario", usuario);
            roles = query.getResultList();
            roles.stream().forEach(listaRoles::add);
            resultado.setResultado(TipoResultado.SUCCESS);
            resultado.set(listaRoles);
            return resultado;
        } catch (NoResultException nre) {
            resultado.setResultado(TipoResultado.WARNING);
            return resultado;
        } catch (Exception ex) {
            Logger.getLogger(UsuarioDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error al traer los roles del usuario [" + usuario + "].");
            return resultado;
        }
    }

    public Resultado<ArrayList<Roles>> getRolesNoAsignados(String usuario) {
        Resultado<ArrayList<Roles>> resultado = new Resultado<>();
        ArrayList<Roles> listaRoles = new ArrayList<>();
        List<Roles> roles;
        try {
            Query query = getEntityManager().createNamedQuery("Roles.noAsignadosUsuario");
            query.setParameter("codUsuario", usuario);
            roles = query.getResultList();
            roles.stream().forEach(listaRoles::add);
            resultado.setResultado(TipoResultado.SUCCESS);
            resultado.set(listaRoles);
            return resultado;
        } catch (NoResultException nre) {
            resultado.setResultado(TipoResultado.WARNING);
            return resultado;
        } catch (Exception ex) {
            Logger.getLogger(UsuarioDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error al traer los roles.");
            return resultado;
        }
    }

    public Resultado<String> deleteRol(RolXUsuario rolUsuario) {
        Resultado<String> resultado = new Resultado<>();
        try {

            getEntityManager().getTransaction().begin();
            Integer id = (Integer) Parametros.PERSISTENCEUTIL.getIdentifier(rolUsuario);
            RolXUsuario existe = (RolXUsuario) getEntityManager().find(RolXUsuario.class, id);

            if (existe != null) {
                if (!getEntityManager().contains(rolUsuario)) {
                    rolUsuario = getEntityManager().merge(rolUsuario);
                }
                getEntityManager().remove(rolUsuario);
            }
            getEntityManager().getTransaction().commit();
            resultado.setResultado(TipoResultado.SUCCESS);
            return resultado;
        } catch (Exception ex) {
            getEntityManager().getTransaction().rollback();
            Logger.getLogger(UsuarioDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error al eliminar el rol para el usuario " + rolUsuario.getRxuCodrol().getRolCodigo() + ".");
            return resultado;
        }
    }

    // Procedimiento para guardar la información del usuario del sistema.
    public Resultado<Usuario> save() {
        Resultado<Usuario> resultado = new Resultado<>();
        try {
            this.usuarioSistema = (Usuario) super.save(this.usuarioSistema);

            if (this.usuarioSistema.getUsuCodigo() != null) {
                resultado.setResultado(TipoResultado.SUCCESS);
                resultado.set(this.usuarioSistema);
                resultado.setMensaje("Usuario guardado correctamente.");

            } else {
                resultado.setResultado(TipoResultado.ERROR);
                resultado.set(this.usuarioSistema);
                resultado.setMensaje("No se pudo guardar la persona.");
            }

            return resultado;

        } catch (Exception ex) {
            Logger.getLogger(UsuarioDao.class.getName()).log(Level.SEVERE, null, ex);
            resultado.setResultado(TipoResultado.ERROR);
            resultado.setMensaje("Error al guardar el usuario.");
            return resultado;
        }
    }

}
