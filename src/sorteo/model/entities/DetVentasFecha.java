/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jdiego
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "sor_detventasfecha", schema = "sorteo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detventasfecha.resumenVentaFecha", query = "SELECT d.fechasorteo, d.numero, coalesce(sum(d.apuesta),0) FROM Detventasfecha d"
       + " WHERE d.codtiposorteo = :codTSorteo"
       + "   AND d.fechaventa = :fecVenta")
    ,
@NamedQuery(name = "Detventasfecha.detalleVentaFecha", query = "SELECT d FROM Detventasfecha d"
       + " WHERE d.codtiposorteo = :codTSorteo"
       + "   AND d.fechaventa = :fecVenta")})
public class DetVentasFecha implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    private int codtiposorteo;

    @Transient
    private SimpleStringProperty tiposorteo;
    @Id
    @Basic(optional = false)
    @Column(name = "codigofactura")
    @Transient
    private SimpleIntegerProperty codigofactura;

    @Transient
    private SimpleObjectProperty<LocalDate> fechaventa;
    @Id
    @Column(name = "fechasorteo")
    @Temporal(TemporalType.DATE)
    @Transient
    private SimpleObjectProperty<LocalDate> fechasorteo;
    @Transient
    private SimpleIntegerProperty dfaNumero;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Transient
    private SimpleDoubleProperty apuesta;

    public DetVentasFecha() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codtiposorteo")
    @Access(AccessType.PROPERTY)
    public int getCodtiposorteo() {
        return codtiposorteo;
    }

    public void setCodtiposorteo(int codtiposorteo) {
        this.codtiposorteo = codtiposorteo;
    }

    @Id
    @Column(name = "tiposorteo")
    @Access(AccessType.PROPERTY)
    public String getTiposorteo() {
        return tiposorteo.get();
    }

    public SimpleStringProperty getTiposorteoProperty() {
        return tiposorteo;
    }

    public void setTiposorteo(String tiposorteo) {
        if (this.tiposorteo == null) {
            this.tiposorteo = new SimpleStringProperty();
        }
        this.tiposorteo.set(tiposorteo);
    }

    @Id
    @Column(name = "fechaventa")
    @Temporal(TemporalType.DATE)
    @Access(AccessType.PROPERTY)
    public Date getFechaventa() {
        if (fechaventa != null && fechaventa.get() != null) {
            return Date.from(fechaventa.get().atStartOfDay(ZoneId.systemDefault()).toInstant());
        } else {
            return null;
        }
    }

    public SimpleObjectProperty<LocalDate> getFechaventaProperty() {
        if (this.fechaventa == null) {
            this.fechaventa = new SimpleObjectProperty();
        }
        return this.fechaventa;
    }

    public void setFechaventa(Date fecha) {
        if (fecha != null) {
            if (this.fechaventa == null) {
                this.fechaventa = new SimpleObjectProperty();
            }
            this.fechaventa.set(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
    }

    @Id
    @Column(name = "fechasorteo")
    @Temporal(TemporalType.DATE)
    @Access(AccessType.PROPERTY)
    public Date getFechasorteo() {
        if (fechasorteo != null && fechasorteo.get() != null) {
            return Date.from(fechasorteo.get().atStartOfDay(ZoneId.systemDefault()).toInstant());
        } else {
            return null;
        }
    }

    public SimpleObjectProperty<LocalDate> getFechasorteoProperty() {
        if (this.fechasorteo == null) {
            this.fechasorteo = new SimpleObjectProperty();
        }
        return this.fechasorteo;
    }

    public void setFechasorteo(Date fecha) {
        if (fecha != null) {
            if (this.fechasorteo == null) {
                this.fechasorteo = new SimpleObjectProperty();
            }
            this.fechasorteo.set(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
    }

    @Id
    @Column(name = "dfanumero")
    @Access(AccessType.PROPERTY)
    public Integer getDfaNumero() {
        if (this.dfaNumero == null) {
            this.dfaNumero = new SimpleIntegerProperty();
        }
        return dfaNumero.get();
    }

    public SimpleIntegerProperty getDfaNumeroProperty() {
        if (this.dfaNumero == null) {
            this.dfaNumero = new SimpleIntegerProperty();
        }
        return this.dfaNumero;
    }

    public void setDfaNumero(Integer numero) {
        if (this.dfaNumero == null) {
            this.dfaNumero = new SimpleIntegerProperty();
        }
        this.dfaNumero.set(numero);
    }

    @Id
    @Column(name = "apuesta")
    @Access(AccessType.PROPERTY)
    public Double getApuesta() {
        if (this.apuesta == null) {
            this.apuesta = new SimpleDoubleProperty();
        }
        return this.apuesta.get();
    }

    public SimpleDoubleProperty getApuestaProperty() {
        if (this.apuesta == null) {
            this.apuesta = new SimpleDoubleProperty();
        }
        return this.apuesta;
    }

    public void setApuesta(Double apuesta) {
        if (this.apuesta == null) {
            this.apuesta = new SimpleDoubleProperty();
        }
        this.apuesta.set(apuesta);
    }

    @Access(AccessType.PROPERTY)
    public Integer getCodigofactura() {
        if (this.codigofactura == null) {
            this.codigofactura = new SimpleIntegerProperty();
        }
        return codigofactura.get();
    }

    public SimpleIntegerProperty getCodigoProperty() {
        if (this.codigofactura == null) {
            this.codigofactura = new SimpleIntegerProperty();
        }
        return this.codigofactura;
    }

    public void setCodigofactura(int codigofactura) {
        if (this.codigofactura == null) {
            this.codigofactura = new SimpleIntegerProperty();
        }
        this.codigofactura.set(codigofactura);
    }

}
