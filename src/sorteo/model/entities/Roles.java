/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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
    @NamedQuery(name = "Roles.findAll", query = "SELECT r FROM Roles r WHERE r.rolEstado = 'A' ORDER BY r.rolCodigo")
    , @NamedQuery(name = "Roles.findByUsuario", query = "SELECT r.rxuCodrol \n"
       + "  FROM RolXUsuario r\n"
       + "  JOIN r.rxuCodusuario u \n"
       + " WHERE u.usuCodigo = :codUsuario"
       + " ORDER BY r.rxuCodigo")
    ,
@NamedQuery(name = "Roles.noAsignadosUsuario", query = "SELECT x \n"
       + "  FROM Roles x\n"
       + " WHERE x.rolCodigo NOT IN (\n"
       + "          SELECT r.rxuCodrol.rolCodigo \n"
       + "            FROM RolXUsuario r\n"
       + "            JOIN r.rxuCodusuario u \n"
       + "            WHERE u.usuCodigo = :codUsuario)\n"
       + "   AND x.rolEstado = 'A'"
       + " ORDER BY x.rolCodigo")})
public class Roles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    private SimpleStringProperty rolCodigo;

    @Transient
    private SimpleStringProperty rolDescripcion;

    @Transient
    private ObjectProperty<GenValorCombo> rolEstado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mxrCodrol", fetch = FetchType.LAZY)
    private List<MenuXRol> menuXRollList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rxuCodrol", fetch = FetchType.LAZY)
    private List<RolXUsuario> rolXUsuarioList;

    public Roles() {
        this.rolCodigo = new SimpleStringProperty();
        this.rolDescripcion = new SimpleStringProperty();
        this.rolEstado = new SimpleObjectProperty();
    }

    public Roles(String rolCodigo) {
        this.rolCodigo = new SimpleStringProperty(rolCodigo);
    }

    @Id
    @Basic(optional = false)
    @Column(name = "rol_codigo")
    @Access(AccessType.PROPERTY)
    public String getRolCodigo() {
        return rolCodigo.get();
    }

    public SimpleStringProperty getRolCodigoProperty() {
        return rolCodigo;
    }

    public void setRolCodigo(String rolCodigo) {
        if (this.rolCodigo == null) {
            this.rolCodigo = new SimpleStringProperty();
        }
        this.rolCodigo.set(rolCodigo);
    }

    @Column(name = "rol_descripcion")
    @Access(AccessType.PROPERTY)
    public String getRolDescripcion() {
        return rolDescripcion.get();
    }

    public SimpleStringProperty getRolDescripcionProperty() {
        return rolDescripcion;
    }

    public void setRolDescripcion(String rolDescripcion) {
        if (this.rolDescripcion == null) {
            this.rolDescripcion = new SimpleStringProperty();
        }
        this.rolDescripcion.set(rolDescripcion);
    }

    @Column(name = "rol_estado")
    @Access(AccessType.PROPERTY)
    public String getRolEstado() {
        return rolEstado.get().getCodigo();
    }

    public void setRolEstado(String rolEstado) {
        GenValorCombo valorEstado = null;
        if (this.rolEstado == null) {
            this.rolEstado = new SimpleObjectProperty();
        }
        if (rolEstado.equalsIgnoreCase("a")) {
            valorEstado = new GenValorCombo("A", "Activo");
        } else {
            valorEstado = new GenValorCombo("I", "Inactivo");
        }
        this.rolEstado.set(valorEstado);
    }

    @XmlTransient
    public List<MenuXRol> getMenuXRollList() {
        return menuXRollList;
    }

    public void setMenuXRollList(List<MenuXRol> menuXRollList) {
        this.menuXRollList = menuXRollList;
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
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.rolCodigo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Roles other = (Roles) obj;
        if (!Objects.equals(this.rolCodigo, other.rolCodigo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Roles{" + "rolCodigo=" + rolCodigo + '}';
    }
}
