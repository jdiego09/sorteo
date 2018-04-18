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
@Table(name = "sor_roles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SorRoles.findAll", query = "SELECT s FROM SorRoles s")
    , @NamedQuery(name = "SorRoles.findByRolCodigo", query = "SELECT s FROM SorRoles s WHERE s.rolCodigo = :rolCodigo")
    , @NamedQuery(name = "SorRoles.findByRolDescripcion", query = "SELECT s FROM SorRoles s WHERE s.rolDescripcion = :rolDescripcion")
    , @NamedQuery(name = "SorRoles.findByRolEstado", query = "SELECT s FROM SorRoles s WHERE s.rolEstado = :rolEstado")})
public class Roles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "rol_codigo")
    private String rolCodigo;
    @Column(name = "rol_descripcion")
    private String rolDescripcion;
    @Column(name = "rol_estado")
    private String rolEstado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mxrCodrol", fetch = FetchType.LAZY)
    private List<MenuXRol> sorMenuxrolList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rxuCodrol", fetch = FetchType.LAZY)
    private List<RolXUsuario> sorRolxusuarioList;

    public Roles() {
    }

    public Roles(String rolCodigo) {
        this.rolCodigo = rolCodigo;
    }

    public String getRolCodigo() {
        return rolCodigo;
    }

    public void setRolCodigo(String rolCodigo) {
        this.rolCodigo = rolCodigo;
    }

    public String getRolDescripcion() {
        return rolDescripcion;
    }

    public void setRolDescripcion(String rolDescripcion) {
        this.rolDescripcion = rolDescripcion;
    }

    public String getRolEstado() {
        return rolEstado;
    }

    public void setRolEstado(String rolEstado) {
        this.rolEstado = rolEstado;
    }

    @XmlTransient
    public List<MenuXRol> getSorMenuxrolList() {
        return sorMenuxrolList;
    }

    public void setSorMenuxrolList(List<MenuXRol> sorMenuxrolList) {
        this.sorMenuxrolList = sorMenuxrolList;
    }

    @XmlTransient
    public List<RolXUsuario> getSorRolxusuarioList() {
        return sorRolxusuarioList;
    }

    public void setSorRolxusuarioList(List<RolXUsuario> sorRolxusuarioList) {
        this.sorRolxusuarioList = sorRolxusuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolCodigo != null ? rolCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Roles)) {
            return false;
        }
        Roles other = (Roles) object;
        if ((this.rolCodigo == null && other.rolCodigo != null) || (this.rolCodigo != null && !this.rolCodigo.equals(other.rolCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sorteo.model.entities.SorRoles[ rolCodigo=" + rolCodigo + " ]";
    }
    
}
