/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jdiego
 */
@Entity
@Table(name = "sor_menu", schema="sorteo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Menu.findAll", query = "SELECT m FROM Menu m")
    , @NamedQuery(name = "Menu.findByMenCodigo", query = "SELECT m FROM Menu m WHERE m.menCodigo = :menCodigo")
    , @NamedQuery(name = "Menu.findByMenPantalla", query = "SELECT m FROM Menu m WHERE m.menPantalla = :menPantalla")
    , @NamedQuery(name = "Menu.findByMenEtiqueta", query = "SELECT m FROM Menu m WHERE m.menEtiqueta = :menEtiqueta")
    , @NamedQuery(name = "Menu.findByMenEstado", query = "SELECT m FROM Menu m WHERE m.menEstado = :menEstado")})
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "men_codigo")
    private Integer menCodigo;
    @Column(name = "men_pantalla")
    private String menPantalla;
    @Column(name = "men_etiqueta")
    private String menEtiqueta;
    @Column(name = "men_estado")
    private String menEstado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mxrCodmenu", fetch = FetchType.LAZY)
    private List<MenuXRol> menuXRolList;

    public Menu() {
    }

    public Menu(Integer menCodigo) {
        this.menCodigo = menCodigo;
    }

    public Integer getMenCodigo() {
        return menCodigo;
    }

    public void setMenCodigo(Integer menCodigo) {
        this.menCodigo = menCodigo;
    }

    public String getMenPantalla() {
        return menPantalla;
    }

    public void setMenPantalla(String menPantalla) {
        this.menPantalla = menPantalla;
    }

    public String getMenEtiqueta() {
        return menEtiqueta;
    }

    public void setMenEtiqueta(String menEtiqueta) {
        this.menEtiqueta = menEtiqueta;
    }

    public String getMenEstado() {
        return menEstado;
    }

    public void setMenEstado(String menEstado) {
        this.menEstado = menEstado;
    }

    @XmlTransient
    public List<MenuXRol> getMenuXRolList() {
        return menuXRolList;
    }

    public void setMenuXRolList(List<MenuXRol> menuXRolList) {
        this.menuXRolList = menuXRolList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (menCodigo != null ? menCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Menu)) {
            return false;
        }
        Menu other = (Menu) object;
        if ((this.menCodigo == null && other.menCodigo != null) || (this.menCodigo != null && !this.menCodigo.equals(other.menCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sorteo.model.entities.Menu[ menCodigo=" + menCodigo + " ]";
    }
    
}
