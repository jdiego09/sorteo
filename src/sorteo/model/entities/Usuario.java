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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "sor_usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")
    , @NamedQuery(name = "Usuario.findByUsuCodigo", query = "SELECT u FROM Usuario u WHERE u.usuCodigo = :usuCodigo and u.usuEstado = 'A'")
    , @NamedQuery(name = "Usuario.findByUsuDescripcion", query = "SELECT u FROM Usuario u WHERE u.usuDescripcion = :usuDescripcion")
    , @NamedQuery(name = "Usuario.findByUsuContrasena", query = "SELECT u FROM Usuario u WHERE u.usuContrasena = :usuContrasena")
    , @NamedQuery(name = "Usuario.findByUsuEstado", query = "SELECT u FROM Usuario u WHERE u.usuEstado = :usuEstado")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    private SimpleStringProperty usuCodigo;
    @Transient
    private SimpleStringProperty usuDescripcion;
    @Transient
    private SimpleStringProperty usuContrasena;
    @Transient
    private ObjectProperty<GenValorCombo> usuEstado;
    @JoinColumn(name = "usu_codsucursal", referencedColumnName = "suc_codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Sucursal usuCodsucursal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rxuCodusuario", fetch = FetchType.LAZY)
    private List<RolXUsuario> rolXUsuarioList;

    public Usuario() {
        this.usuCodigo = new SimpleStringProperty();
        this.usuDescripcion = new SimpleStringProperty();
        this.usuContrasena = new SimpleStringProperty();
        this.usuEstado = new SimpleObjectProperty();
    }

    public Usuario(String usuCodigo) {
        this.usuCodigo.set(usuCodigo);
    }

    @Id
    @Basic(optional = false)
    @Column(name = "usu_codigo")
    @Access(AccessType.PROPERTY)
    public String getUsuCodigo() {
        return usuCodigo.get();
    }

    public void setUsuCodigo(String usuCodigo) {
        this.usuCodigo.set(usuCodigo);
    }

    @Column(name = "usu_descripcion")
    @Access(AccessType.PROPERTY)

    public String getUsuDescripcion() {
        return usuDescripcion.get();
    }

    public void setUsuDescripcion(String usuDescripcion) {
        this.usuDescripcion.set(usuDescripcion);
    }

    @Column(name = "usu_contrasena")
    @Access(AccessType.PROPERTY)
    public String getUsuContrasena() {
        return usuContrasena.get();
    }

    public void setUsuContrasena(String usuContrasena) {
        this.usuContrasena.set(usuContrasena);
    }

    @Column(name = "usu_estado")
    @Access(AccessType.PROPERTY)
    public String getUsuEstado() {
        return usuEstado.get().getCodigo();
    }

    public void setUsuEstado(String usuEstado) {
        GenValorCombo valorEstado = null;
        if (this.usuEstado == null) {
            this.usuEstado = new SimpleObjectProperty();
        }
        if (usuEstado.equalsIgnoreCase("a")) {
            valorEstado = new GenValorCombo("A", "Activo");
        } else {
            valorEstado = new GenValorCombo("I", "Inactivo");
        }
        this.usuEstado.set(valorEstado);
    }

    public Sucursal getUsuCodsucursal() {
        return usuCodsucursal;
    }

    public void setUsuCodsucursal(Sucursal usuCodsucursal) {
        this.usuCodsucursal = usuCodsucursal;
    }

    @XmlTransient
    public List<RolXUsuario> getRolXUsuarioList() {
        return rolXUsuarioList;
    }

    public void setRolXUsuarioList(List<RolXUsuario> rolXUsuarioList) {
        this.rolXUsuarioList = rolXUsuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuCodigo != null ? usuCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.usuCodigo == null && other.usuCodigo != null) || (this.usuCodigo != null && !this.usuCodigo.equals(other.usuCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sorteo.model.entities.Usuario[ usuCodigo=" + usuCodigo + " ]";
    }

}
