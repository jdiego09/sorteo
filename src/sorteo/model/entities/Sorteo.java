/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jdiego
 */
@Entity
@Table(name = "sor_sorteo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SorSorteo.findAll", query = "SELECT s FROM SorSorteo s")
    , @NamedQuery(name = "SorSorteo.findBySorCodigo", query = "SELECT s FROM SorSorteo s WHERE s.sorCodigo = :sorCodigo")
    , @NamedQuery(name = "SorSorteo.findBySorFecha", query = "SELECT s FROM SorSorteo s WHERE s.sorFecha = :sorFecha")})
public class Sorteo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sor_codigo")
    private Integer sorCodigo;
    @Column(name = "sor_fecha")
    @Temporal(TemporalType.DATE)
    private Date sorFecha;
    @OneToMany(mappedBy = "sorteo", fetch = FetchType.LAZY)
    private List<Factura> sorFacturaList;
    @JoinColumn(name = "sor_codsucursal", referencedColumnName = "suc_codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Sucursal sorCodsucursal;
    @JoinColumn(name = "sor_tiposorteo", referencedColumnName = "tso_codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoSorteo sorTiposorteo;

    public Sorteo() {
    }

    public Sorteo(Integer sorCodigo) {
        this.sorCodigo = sorCodigo;
    }

    public Integer getSorCodigo() {
        return sorCodigo;
    }

    public void setSorCodigo(Integer sorCodigo) {
        this.sorCodigo = sorCodigo;
    }

    public Date getSorFecha() {
        return sorFecha;
    }

    public void setSorFecha(Date sorFecha) {
        this.sorFecha = sorFecha;
    }

    @XmlTransient
    public List<Factura> getSorFacturaList() {
        return sorFacturaList;
    }

    public void setSorFacturaList(List<Factura> sorFacturaList) {
        this.sorFacturaList = sorFacturaList;
    }

    public Sucursal getSorCodsucursal() {
        return sorCodsucursal;
    }

    public void setSorCodsucursal(Sucursal sorCodsucursal) {
        this.sorCodsucursal = sorCodsucursal;
    }

    public TipoSorteo getSorTiposorteo() {
        return sorTiposorteo;
    }

    public void setSorTiposorteo(TipoSorteo sorTiposorteo) {
        this.sorTiposorteo = sorTiposorteo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sorCodigo != null ? sorCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sorteo)) {
            return false;
        }
        Sorteo other = (Sorteo) object;
        if ((this.sorCodigo == null && other.sorCodigo != null) || (this.sorCodigo != null && !this.sorCodigo.equals(other.sorCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sorteo.model.entities.SorSorteo[ sorCodigo=" + sorCodigo + " ]";
    }
    
}
