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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "sor_usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SorUsuario.findAll", query = "SELECT s FROM SorUsuario s")
    , @NamedQuery(name = "SorUsuario.findByUsuCodigo", query = "SELECT s FROM SorUsuario s WHERE s.usuCodigo = :usuCodigo")
    , @NamedQuery(name = "SorUsuario.findByUsuDescripcion", query = "SELECT s FROM SorUsuario s WHERE s.usuDescripcion = :usuDescripcion")
    , @NamedQuery(name = "SorUsuario.findByUsuContrasena", query = "SELECT s FROM SorUsuario s WHERE s.usuContrasena = :usuContrasena")
    , @NamedQuery(name = "SorUsuario.findByUsuEstado", query = "SELECT s FROM SorUsuario s WHERE s.usuEstado = :usuEstado")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "usu_codigo")
    private String usuCodigo;
    @Column(name = "usu_descripcion")
    private String usuDescripcion;
    @Column(name = "usu_contrasena")
    private String usuContrasena;
    @Column(name = "usu_estado")
    private String usuEstado;
    @JoinColumn(name = "usu_codsucursal", referencedColumnName = "suc_codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Sucursal usuCodsucursal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rxuCodusuario", fetch = FetchType.LAZY)
    private List<RolXUsuario> sorRolxusuarioList;

    public Usuario() {
    }

    public Usuario(String usuCodigo) {
        this.usuCodigo = usuCodigo;
    }

    public String getUsuCodigo() {
        return usuCodigo;
    }

    public void setUsuCodigo(String usuCodigo) {
        this.usuCodigo = usuCodigo;
    }

    public String getUsuDescripcion() {
        return usuDescripcion;
    }

    public void setUsuDescripcion(String usuDescripcion) {
        this.usuDescripcion = usuDescripcion;
    }

    public String getUsuContrasena() {
        return usuContrasena;
    }

    public void setUsuContrasena(String usuContrasena) {
        this.usuContrasena = usuContrasena;
    }

    public String getUsuEstado() {
        return usuEstado;
    }

    public void setUsuEstado(String usuEstado) {
        this.usuEstado = usuEstado;
    }

    public Sucursal getUsuCodsucursal() {
        return usuCodsucursal;
    }

    public void setUsuCodsucursal(Sucursal usuCodsucursal) {
        this.usuCodsucursal = usuCodsucursal;
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
        return "sorteo.model.entities.SorUsuario[ usuCodigo=" + usuCodigo + " ]";
    }
    
}
