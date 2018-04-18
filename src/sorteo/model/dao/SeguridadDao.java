/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javax.persistence.Query;
import sorteo.model.entities.Menu;
import sorteo.model.entities.MenuXRol;
import sorteo.utils.Aplicacion;
import sorteo.utils.AppWindowController;

/**
 *
 * @author jdiego
 */
public class SeguridadDao extends BaseDao {

    public ArrayList<Menu> getAccesosUsuario(String codigoRol) {
        ArrayList<Menu> modulos = new ArrayList<>();
        List<Menu> resultados;
        try {
            Query query = getEntityManager().createNamedQuery("MenuXRol.findByCodigoRol");
            query.setParameter("codigoRol", Arrays.asList(codigoRol.split(",")));
            resultados = query.getResultList();
            resultados.forEach(modulos::add);
            return modulos;
        } catch (Exception ex) {
            Logger.getLogger(SeguridadDao.class.getName()).log(Level.SEVERE, null, ex);
            AppWindowController.getInstance().mensaje(Alert.AlertType.ERROR, "Error obteniendo accesos del usuario", "No se pudo cargar los accesos del usuario [" + Aplicacion.getInstance().getUsuario().getCodigo() + "].");
            return modulos;
        }
    }
}
