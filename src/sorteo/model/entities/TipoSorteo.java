/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "sor_tiposorteo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SorTiposorteo.findAll", query = "SELECT s FROM SorTiposorteo s")
    , @NamedQuery(name = "SorTiposorteo.findByTsoCodigo", query = "SELECT s FROM SorTiposorteo s WHERE s.tsoCodigo = :tsoCodigo")
    , @NamedQuery(name = "SorTiposorteo.findByTsoDescripcion", query = "SELECT s FROM SorTiposorteo s WHERE s.tsoDescripcion = :tsoDescripcion")
    , @NamedQuery(name = "SorTiposorteo.findByTsoNumminimo", query = "SELECT s FROM SorTiposorteo s WHERE s.tsoNumminimo = :tsoNumminimo")
    , @NamedQuery(name = "SorTiposorteo.findByTsoNummaximo", query = "SELECT s FROM SorTiposorteo s WHERE s.tsoNummaximo = :tsoNummaximo")
    , @NamedQuery(name = "SorTiposorteo.findByTsoMontomax", query = "SELECT s FROM SorTiposorteo s WHERE s.tsoMontomax = :tsoMontomax")
    , @NamedQuery(name = "SorTiposorteo.findByTsoCantxventa", query = "SELECT s FROM SorTiposorteo s WHERE s.tsoCantxventa = :tsoCantxventa")
    , @NamedQuery(name = "SorTiposorteo.findByTsoDiashabiles", query = "SELECT s FROM SorTiposorteo s WHERE s.tsoDiashabiles = :tsoDiashabiles")
    , @NamedQuery(name = "SorTiposorteo.findByTsoEstado", query = "SELECT s FROM SorTiposorteo s WHERE s.tsoEstado = :tsoEstado")})
public class TipoSorteo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tso_codigo")
    private Integer tsoCodigo;
    @Column(name = "tso_descripcion")
    private String tsoDescripcion;
    @Column(name = "tso_numminimo")
    private Integer tsoNumminimo;
    @Column(name = "tso_nummaximo")
    private Integer tsoNummaximo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "tso_montomax")
    private Double tsoMontomax;
    @Column(name = "tso_cantxventa")
    private Integer tsoCantxventa;
    @Column(name = "tso_diashabiles")
    private String tsoDiashabiles;
    @Column(name = "tso_estado")
    private String tsoEstado;
    @OneToMany(mappedBy = "sorTiposorteo", fetch = FetchType.LAZY)
    private List<Sorteo> sorSorteoList;

    public TipoSorteo() {
    }

    public TipoSorteo(Integer tsoCodigo) {
        this.tsoCodigo = tsoCodigo;
    }

    public Integer getTsoCodigo() {
        return tsoCodigo;
    }

    public void setTsoCodigo(Integer tsoCodigo) {
        this.tsoCodigo = tsoCodigo;
    }

    public String getTsoDescripcion() {
        return tsoDescripcion;
    }

    public void setTsoDescripcion(String tsoDescripcion) {
        this.tsoDescripcion = tsoDescripcion;
    }

    public Integer getTsoNumminimo() {
        return tsoNumminimo;
    }

    public void setTsoNumminimo(Integer tsoNumminimo) {
        this.tsoNumminimo = tsoNumminimo;
    }

    public Integer getTsoNummaximo() {
        return tsoNummaximo;
    }

    public void setTsoNummaximo(Integer tsoNummaximo) {
        this.tsoNummaximo = tsoNummaximo;
    }

    public Double getTsoMontomax() {
        return tsoMontomax;
    }

    public void setTsoMontomax(Double tsoMontomax) {
        this.tsoMontomax = tsoMontomax;
    }

    public Integer getTsoCantxventa() {
        return tsoCantxventa;
    }

    public void setTsoCantxventa(Integer tsoCantxventa) {
        this.tsoCantxventa = tsoCantxventa;
    }

    public String getTsoDiashabiles() {
        return tsoDiashabiles;
    }

    public void setTsoDiashabiles(String tsoDiashabiles) {
        this.tsoDiashabiles = tsoDiashabiles;
    }

    public String getTsoEstado() {
        return tsoEstado;
    }

    public void setTsoEstado(String tsoEstado) {
        this.tsoEstado = tsoEstado;
    }

    @XmlTransient
    public List<Sorteo> getSorSorteoList() {
        return sorSorteoList;
    }

    public void setSorSorteoList(List<Sorteo> sorSorteoList) {
        this.sorSorteoList = sorSorteoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tsoCodigo != null ? tsoCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoSorteo)) {
            return false;
        }
        TipoSorteo other = (TipoSorteo) object;
        if ((this.tsoCodigo == null && other.tsoCodigo != null) || (this.tsoCodigo != null && !this.tsoCodigo.equals(other.tsoCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sorteo.model.entities.SorTiposorteo[ tsoCodigo=" + tsoCodigo + " ]";
    }
    
}
