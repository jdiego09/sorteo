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
@Table(name = "sor_menuxrol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SorMenuxrol.findAll", query = "SELECT s FROM SorMenuxrol s")
    , @NamedQuery(name = "SorMenuxrol.findByMxrCodigo", query = "SELECT s FROM SorMenuxrol s WHERE s.mxrCodigo = :mxrCodigo")})
public class MenuXRol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mxr_codigo")
    private Integer mxrCodigo;
    @JoinColumn(name = "mxr_codmenu", referencedColumnName = "men_codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Menu mxrCodmenu;
    @JoinColumn(name = "mxr_codrol", referencedColumnName = "rol_codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Roles mxrCodrol;

    public MenuXRol() {
    }

    public MenuXRol(Integer mxrCodigo) {
        this.mxrCodigo = mxrCodigo;
    }

    public Integer getMxrCodigo() {
        return mxrCodigo;
    }

    public void setMxrCodigo(Integer mxrCodigo) {
        this.mxrCodigo = mxrCodigo;
    }

    public Menu getMxrCodmenu() {
        return mxrCodmenu;
    }

    public void setMxrCodmenu(Menu mxrCodmenu) {
        this.mxrCodmenu = mxrCodmenu;
    }

    public Roles getMxrCodrol() {
        return mxrCodrol;
    }

    public void setMxrCodrol(Roles mxrCodrol) {
        this.mxrCodrol = mxrCodrol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mxrCodigo != null ? mxrCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MenuXRol)) {
            return false;
        }
        MenuXRol other = (MenuXRol) object;
        if ((this.mxrCodigo == null && other.mxrCodigo != null) || (this.mxrCodigo != null && !this.mxrCodigo.equals(other.mxrCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sorteo.model.entities.SorMenuxrol[ mxrCodigo=" + mxrCodigo + " ]";
    }
    
}
