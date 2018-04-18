/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.entities;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "sor_roles", schema = "sorteo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Roles.findAll", query = "SELECT s FROM Roles s")
    , @NamedQuery(name = "Roles.findByCodigo", query = "SELECT s FROM Roles s WHERE s.rolCodigo = :codigo")
    , @NamedQuery(name = "Roles.findByDescripcion", query = "SELECT s FROM Roles s WHERE s.rolDescripcion = :descripcion")})
public class Roles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    private SimpleStringProperty codigo;
    @Transient
    private SimpleStringProperty descripcion;
    @Transient
    private ObjectProperty<GenValorCombo> estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "listaRoles", fetch = FetchType.LAZY)
    private List<MenuXRol> listaMenus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "listaRoles", fetch = FetchType.LAZY)
    private List<RolXUsuario> listaUsuariosRol;

    public Roles() {
        this.codigo = new SimpleStringProperty();
        this.descripcion = new SimpleStringProperty();
    }

    public Roles(String rolCodigo) {
        this.codigo = new SimpleStringProperty(rolCodigo);
    }

    @Id
    @Basic(optional = false)
    @Column(name = "rol_codigo")
    @Access(AccessType.PROPERTY)
    public String getCodigo() {
        return codigo.get();
    }

    public void setCodigo(String codigo) {
        this.codigo.set(codigo);
    }

    @Column(name = "rol_descripcion")
    @Access(AccessType.PROPERTY)
    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    @Column(name = "rol_estado")
    @Access(AccessType.PROPERTY)
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
    public List<MenuXRol> getListaMenus() {
        return listaMenus;
    }

    public void setListaMenus(List<MenuXRol> listaMenus) {
        this.listaMenus = listaMenus;
    }

    @XmlTransient
    public List<RolXUsuario> getListaUsuariosRol() {
        return listaUsuariosRol;
    }

    public void setListaUsuariosRol(List<RolXUsuario> listaUsuariosRol) {
        this.listaUsuariosRol = listaUsuariosRol;
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
        if (!(object instanceof Roles)) {
            return false;
        }
        Roles other = (Roles) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Roles{" + "codigo=" + codigo + '}';
    }

}
