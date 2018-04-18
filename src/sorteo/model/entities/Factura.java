/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "sor_factura", schema = "sorteo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Factura.findAll", query = "SELECT s FROM Factura s")
    , @NamedQuery(name = "Factura.findByCodigo", query = "SELECT s FROM Factura s WHERE s.codigo = :codigo")
    , @NamedQuery(name = "Factura.findByFecha", query = "SELECT s FROM Factura s WHERE s.fecha = :fecha")})
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    private SimpleIntegerProperty codigo;
    @Transient
    private SimpleObjectProperty<LocalDate> fecha;
    @Transient
    private SimpleStringProperty cliente;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "fac_total")
    @Transient
    private SimpleDoubleProperty total;
    @Transient
    private ObjectProperty<GenValorCombo> estado;
    @OneToMany(mappedBy = "dfaCodfac", fetch = FetchType.LAZY)
    private List<DetalleFactura> detalleFactura;
    @JoinColumn(name = "fac_codsorteo", referencedColumnName = "sor_codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Sorteo sorteo;

    public Factura() {
        this.codigo = new SimpleIntegerProperty();
        this.fecha = new SimpleObjectProperty(LocalDate.now());
        this.total = new SimpleDoubleProperty(BigDecimal.ZERO.doubleValue());
        this.estado = new SimpleObjectProperty(new GenValorCombo("A", "Activa"));
    }

    public Factura(Integer facCodigo) {
        this.codigo = new SimpleIntegerProperty(facCodigo);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "fac_codigo")
    @Access(AccessType.PROPERTY)
    public Integer getCodigo() {
        if (this.codigo == null) {
            this.codigo = new SimpleIntegerProperty();
        }
        return codigo.get();
    }

    public SimpleIntegerProperty getCodigoProperty() {
        if (this.codigo == null) {
            this.codigo = new SimpleIntegerProperty();
        }
        return this.codigo;
    }

    public void setCodigo(Integer codigo) {
        if (this.codigo == null) {
            this.codigo = new SimpleIntegerProperty();
        }
        this.codigo.set(codigo);
    }

    @Column(name = "fac_fecha")
    @Temporal(TemporalType.DATE)
    @Access(AccessType.PROPERTY)
    public Date getFecha() {
        if (fecha != null && fecha.get() != null) {
            return Date.from(fecha.get().atStartOfDay(ZoneId.systemDefault()).toInstant());
        } else {
            return null;
        }
    }

    public SimpleObjectProperty<LocalDate> getFechaProperty() {
        if (this.fecha == null) {
            this.fecha = new SimpleObjectProperty();
        }
        return this.fecha;
    }

    public void setFecha(Date fecha) {
        if (fecha != null) {
            this.fecha.set(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
    }

    @Column(name = "fac_cliente")
    @Access(AccessType.PROPERTY)
    public String getCliente() {
        return cliente.get();
    }

    public void setCliente(String cliente) {
        this.cliente.set(cliente);
    }

    public Double getTotal() {
        if (this.total == null) {
            this.total = new SimpleDoubleProperty();
        }
        return this.total.get();
    }

    public SimpleDoubleProperty getTotalProperty() {
        if (this.total == null) {
            this.total = new SimpleDoubleProperty();
        }
        return this.total;
    }

    public void setTotal(Double total) {
        if (this.total == null) {
            this.total = new SimpleDoubleProperty();
        }
        this.total.set(total);
    }

    @Column(name = "fac_estado")
    @Access(AccessType.PROPERTY)
    public String getEstado() {
        if (this.estado == null) {
            this.estado = new SimpleObjectProperty();
        }
        return estado.get().getCodigo();
    }

    public ObjectProperty getEstadoProperty() {
        if (this.estado == null) {
            this.estado = new SimpleObjectProperty();
        }
        return this.estado;
    }

    public void setEstado(String estado) {
        GenValorCombo valor = null;
        if (estado.equalsIgnoreCase("a")) {
            valor = new GenValorCombo(estado, "Activa");
        } else {
            valor = new GenValorCombo(estado, "Nula");
        }
        if (this.estado == null) {
            this.estado = new SimpleObjectProperty();
        }
        this.estado.set(valor);
    }

    @XmlTransient
    public List<DetalleFactura> getDetalleFactura() {
        return detalleFactura;
    }

    public void setDetalleFactura(List<DetalleFactura> detalleFactura) {
        this.detalleFactura = detalleFactura;
    }

    public Sorteo getSorteo() {
        return sorteo;
    }

    public void setSorteo(Sorteo sorteo) {
        this.sorteo = sorteo;
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
        if (!(object instanceof Factura)) {
            return false;
        }
        Factura other = (Factura) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Factura{" + "codigo=" + codigo + '}';
    }
}
