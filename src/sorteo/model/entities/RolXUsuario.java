/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jdiego
 */
@Entity
@Table(name = "sor_rolxusuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RolXUsuario.findAll", query = "SELECT r FROM RolXUsuario r")
    , @NamedQuery(name = "RolXUsuario.findByRxuCodigo", query = "SELECT r FROM RolXUsuario r WHERE r.rxuCodigo = :rxuCodigo")})
public class RolXUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rxu_codigo")
    private Integer rxuCodigo;
    @JoinColumn(name = "rxu_codusuario", referencedColumnName = "usu_codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario rxuCodusuario;
    @JoinColumn(name = "rxu_codrol", referencedColumnName = "rol_codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Roles rxuCodrol;

    public RolXUsuario() {
    }

    public RolXUsuario(Integer rxuCodigo) {
        this.rxuCodigo = rxuCodigo;
    }

    public Integer getRxuCodigo() {
        return rxuCodigo;
    }

    public void setRxuCodigo(Integer rxuCodigo) {
        this.rxuCodigo = rxuCodigo;
    }

    public Usuario getRxuCodusuario() {
        return rxuCodusuario;
    }

    public void setRxuCodusuario(Usuario rxuCodusuario) {
        this.rxuCodusuario = rxuCodusuario;
    }

    public Roles getRxuCodrol() {
        return rxuCodrol;
    }

    public void setRxuCodrol(Roles rxuCodrol) {
        this.rxuCodrol = rxuCodrol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rxuCodigo != null ? rxuCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RolXUsuario)) {
            return false;
        }
        RolXUsuario other = (RolXUsuario) object;
        if ((this.rxuCodigo == null && other.rxuCodigo != null) || (this.rxuCodigo != null && !this.rxuCodigo.equals(other.rxuCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sorteo.model.entities.RolXUsuario[ rxuCodigo=" + rxuCodigo + " ]";
    }
    
}
