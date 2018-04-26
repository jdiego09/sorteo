/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.entities;

import java.io.Serializable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jdiego
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "sor_detfactura", schema = "sorteo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleFactura.findAll", query = "SELECT s FROM DetalleFactura s")
    , @NamedQuery(name = "DetalleFactura.findByCodFactura", query = "SELECT s FROM DetalleFactura s WHERE s.codigo = :codigo")
    , @NamedQuery(name = "DetalleFactura.findByDfaNumero", query = "SELECT s FROM DetalleFactura s WHERE s.numero = :numero")
    , @NamedQuery(name = "DetalleFactura.findByDfaMonto", query = "SELECT s FROM DetalleFactura s WHERE s.monto = :monto")})
public class DetalleFactura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Transient
    private Integer codigo;
    @Transient
    private SimpleIntegerProperty numero;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation    
    @Transient
    private SimpleDoubleProperty monto;
    @JoinColumn(name = "dfa_codfac", referencedColumnName = "fac_codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Factura factura;

    public DetalleFactura() {
        this.numero = new SimpleIntegerProperty();
        this.monto = new SimpleDoubleProperty();
    }

    public DetalleFactura(Integer dfaCodigo) {
        this.codigo = dfaCodigo;
    }

    public DetalleFactura(Integer numero, Double monto) {
        this.numero = new SimpleIntegerProperty(numero);
        this.monto = new SimpleDoubleProperty(monto);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dfa_codigo")
    @Access(AccessType.PROPERTY)
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @Column(name = "dfa_numero")
    @Access(AccessType.PROPERTY)
    public Integer getNumero() {
        if (this.numero == null) {
            this.numero = new SimpleIntegerProperty();
        }
        return numero.get();
    }

    public SimpleIntegerProperty getNumeroProperty() {
        if (this.numero == null) {
            this.numero = new SimpleIntegerProperty();
        }
        return this.numero;
    }

    public void setNumero(Integer numero) {
        if (this.numero == null) {
            this.numero = new SimpleIntegerProperty();
        }
        this.numero.set(numero);
    }

    @Column(name = "dfa_monto")
    @Access(AccessType.PROPERTY)
    public Double getMonto() {
        if (this.monto == null) {
            this.monto = new SimpleDoubleProperty();
        }
        return this.monto.get();
    }

    public SimpleDoubleProperty getMontoProperty() {
        if (this.monto == null) {
            this.monto = new SimpleDoubleProperty();
        }
        return this.monto;
    }

    public void setMonto(Double monto) {
        if (this.monto == null) {
            this.monto = new SimpleDoubleProperty();
        }
        this.monto.set(monto);
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
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
        if (!(object instanceof DetalleFactura)) {
            return false;
        }
        DetalleFactura other = (DetalleFactura) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DetalleFactura{" + "codigo=" + codigo + '}';
    }

}
