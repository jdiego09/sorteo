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
import java.util.Objects;
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
@Table(name = "sor_exclusion", schema = "sorteo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Exclusion.findAll", query = "SELECT e FROM Exclusion e")
    , @NamedQuery(name = "Exclusion.findBySorteo", query = "SELECT e FROM Exclusion e WHERE e.excCodtiposorteo.codigo = :codtiposorteo and e.excFecha = :fechaSorteo")
    , @NamedQuery(name = "Exclusion.findExclusion", query = "SELECT e FROM Exclusion e WHERE e.excCodtiposorteo.codigo = :codtiposorteo and e.excFecha = :fechaSorteo and e.excBloqueo = :bloqueo and e.excNumero = :numero")})
public class Exclusion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Transient
    private Integer excId;
    @Transient
    private SimpleObjectProperty<LocalDate> excFecha;
    @Transient
    private SimpleIntegerProperty excNumero;
    @Transient
    private SimpleStringProperty excBloqueo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Transient
    private SimpleDoubleProperty excMonto;
    @JoinColumn(name = "exc_codtiposorteo", referencedColumnName = "tso_codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoSorteo excCodtiposorteo;

    public Exclusion() {
        this.excBloqueo = new SimpleStringProperty();
        this.excFecha = new SimpleObjectProperty<>();
        this.excMonto = new SimpleDoubleProperty();
        this.excNumero = new SimpleIntegerProperty();

    }

    public Exclusion(Integer excId) {
        this.excId = excId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "exc_id")
    @Access(AccessType.PROPERTY)
    public Integer getExcId() {
        return excId;
    }

    public void setExcId(Integer excId) {
        this.excId = excId;
    }

    @Column(name = "exc_fecha")
    @Temporal(TemporalType.DATE)
    @Access(AccessType.PROPERTY)
    public Date getExcFecha() {
        if (excFecha != null && excFecha.get() != null) {
            return Date.from(excFecha.get().atStartOfDay(ZoneId.systemDefault()).toInstant());
        } else {
            return null;
        }
    }

    public SimpleObjectProperty<LocalDate> getExcFechaProperty() {
        if (this.excFecha == null) {
            this.excFecha = new SimpleObjectProperty();
        }
        return this.excFecha;
    }

    public void setExcFecha(Date excFecha) {
        if (excFecha != null) {
            if (this.excFecha == null) {
                this.excFecha = new SimpleObjectProperty();
            }
            this.excFecha.set(excFecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
    }

    @Column(name = "exc_numero")
    @Access(AccessType.PROPERTY)
    public Integer getExcNumero() {
        return excNumero.get();
    }

    public SimpleIntegerProperty getExcNumeroProperty() {
        return excNumero;
    }

    public void setExcNumero(Integer excNumero) {
        this.excNumero.set(excNumero);
    }

    @Column(name = "exc_bloqueo")
    @Access(AccessType.PROPERTY)
    public String getExcBloqueo() {
        return excBloqueo.get();
    }

    public SimpleStringProperty getExcBloqueoProperty() {
        return excBloqueo;
    }

    public String getDescripcionBloqueo() {
        if (this.excBloqueo == null) {
            this.excBloqueo = new SimpleStringProperty();
        }
        return this.excBloqueo.get().equalsIgnoreCase("s") ? "Ventas bloqueadas" : "Apuesta limitada";
    }

    public void setExcBloqueo(String excBloqueo) {
        this.excBloqueo.set(excBloqueo);
    }

    @Column(name = "exc_monto")
    @Access(AccessType.PROPERTY)
    public Double getExcMonto() {
        return excMonto.get();
    }

    public SimpleDoubleProperty getExcMontoProperty() {
        return excMonto;
    }

    public void setExcMonto(Double excMonto) {
        this.excMonto.set(excMonto);
    }

    public TipoSorteo getExcCodtiposorteo() {
        return excCodtiposorteo;
    }

    public ObjectProperty getExcCodtiposorteoProperty() {
        ObjectProperty<TipoSorteo> tipoSorteoProperty = new SimpleObjectProperty();
        tipoSorteoProperty.set(excCodtiposorteo);
        return tipoSorteoProperty;
    }

    public void setExcCodtiposorteo(TipoSorteo excCodtiposorteo) {
        this.excCodtiposorteo = excCodtiposorteo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.excFecha);
        hash = 89 * hash + Objects.hashCode(this.excNumero);
        hash = 89 * hash + Objects.hashCode(this.excBloqueo);
        hash = 89 * hash + Objects.hashCode(this.excCodtiposorteo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Exclusion other = (Exclusion) obj;
        if (!Objects.equals(this.excFecha, other.excFecha)) {
            return false;
        }
        if (!Objects.equals(this.excNumero, other.excNumero)) {
            return false;
        }
        if (!Objects.equals(this.excBloqueo, other.excBloqueo)) {
            return false;
        }
        if (!Objects.equals(this.excCodtiposorteo, other.excCodtiposorteo)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "sorteo.model.entities.Exclusion[ excId=" + excId + " ]";
    }

}
