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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jdiego
 */
@Entity
@Table(name = "sor_montos", schema="sorteo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Montos.findAll", query = "SELECT m FROM Montos m ORDER BY m.monto")
    , @NamedQuery(name = "Montos.findByMonCodigo", query = "SELECT m FROM Montos m WHERE m.codigo = :codigo")
    , @NamedQuery(name = "Montos.findByMonMonto", query = "SELECT m FROM Montos m WHERE m.monto = :monto")})
public class Montos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mon_codigo")
    private Integer codigo;
    @Column(name = "mon_monto")
    private Integer monto;

    public Montos() {
    }

    public Montos(Integer monCodigo) {
        this.codigo = monCodigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getMonto() {
        return monto;
    }

    public void setMonto(Integer monto) {
        this.monto = monto;
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
        if (!(object instanceof Montos)) {
            return false;
        }
        Montos other = (Montos) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sorteo.model.entities.Montos[ monCodigo=" + codigo + " ]";
    }
    
}
