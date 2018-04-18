/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.entities;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import sorteo.utils.GenValorCombo;

/**
 *
 * @author jdiego
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "sor_menu", schema = "sorteo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Menu.findAll", query = "SELECT s FROM Menu s")
    , @NamedQuery(name = "Menu.findByCodigo", query = "SELECT s FROM Menu s WHERE s.codigo = :codigo")
    , @NamedQuery(name = "Menu.findByPantalla", query = "SELECT s FROM Menu s WHERE s.pantalla = :pantalla")
    , @NamedQuery(name = "Menu.findByEtiqueta", query = "SELECT s FROM Menu s WHERE s.etiqueta = :etiqueta")})
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    private Integer codigo;
    @Transient
    private SimpleStringProperty pantalla;
    @Transient
    private SimpleStringProperty etiqueta;
    @Transient
    private ObjectProperty<GenValorCombo> estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mxrCodmenu", fetch = FetchType.LAZY)
    private List<MenuXRol> listaRoles;

    public Menu() {
        this.pantalla = new SimpleStringProperty();
        this.etiqueta = new SimpleStringProperty();
        this.estado = new SimpleObjectProperty();
    }

    public Menu(Integer menCodigo) {
        this.codigo = menCodigo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "men_codigo")
    @Access(AccessType.PROPERTY)
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @Access(AccessType.PROPERTY)
    @Column(name = "men_pantalla")
    public String getPantalla() {
        return pantalla.get();
    }

    public void setPantalla(String pantalla) {
        this.pantalla.set(pantalla);
    }

    @Access(AccessType.PROPERTY)
    @Column(name = "men_etiqueta")
    public String getEtiqueta() {
        return etiqueta.get();
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta.set(etiqueta);
    }

    @Access(AccessType.PROPERTY)
    @Column(name = "men_estado")
    public String getEstado() {
        if (this.estado == null) {
            this.estado = new SimpleObjectProperty();
        }
        return estado.get().getCodigo();
    }

    public ObjectProperty getEstadoProperty() {
        if (this.estado == null) {
            this.estado = new SimpleObjectProperty();
        }
        return this.estado;
    }

    public void setEstado(String estado) {
        GenValorCombo valor = null;
        if (estado.equalsIgnoreCase("a")) {
            valor = new GenValorCombo(estado, "Activo");
        } else {
            valor = new GenValorCombo(estado, "Egresado");
        }
        if (this.estado == null) {
            this.estado = new SimpleObjectProperty();
        }
        this.estado.set(valor);
    }

    @XmlTransient
    public List<MenuXRol> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(List<MenuXRol> listaRoles) {
        this.listaRoles = listaRoles;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Menu)) {
            return false;
        }
        Menu other = (Menu) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Menu{" + "codigo=" + codigo + '}';
    }
}
