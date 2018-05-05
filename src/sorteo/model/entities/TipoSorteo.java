/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.entities;

import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import sorteo.utils.Formater;
import sorteo.utils.GenValorCombo;

/**
 *
 * @author jdiego
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "sor_tiposorteo", schema = "sorteo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoSorteo.findAll", query = "SELECT s FROM TipoSorteo s ORDER BY s.codigo")
    , @NamedQuery(name = "TipoSorteo.findByCodigo", query = "SELECT s FROM TipoSorteo s WHERE s.codigo = :codigo")
    , @NamedQuery(name = "TipoSorteo.findAllActivos", query = "SELECT s FROM TipoSorteo s WHERE s.estado = 'A' ORDER BY s.codigo")})
public class TipoSorteo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    private Integer codigo;
    @Transient
    private SimpleStringProperty descripcion;
    @Transient
    private SimpleIntegerProperty numeroMinimo;
    @Transient
    private SimpleIntegerProperty numeroMaximo;
    @Transient
    private SimpleDoubleProperty montoMaximo;
    @Transient
    private SimpleIntegerProperty cantNumVenta;
    @Transient
    private SimpleStringProperty diasHabiles;
    @Transient
    private ObjectProperty<GenValorCombo> estado;
    @Transient
    private SimpleObjectProperty<LocalTime> horaCorte;
    @Column(name = "tso_usrcrea")
    private String tsoUsrcrea;
    @Column(name = "tso_feccrea")
    @Temporal(TemporalType.DATE)
    private Date tsoFeccrea;
    @Column(name = "tso_fecmod")
    @Temporal(TemporalType.DATE)
    private Date tsoFecmod;
    @Column(name = "tso_usrmod")
    private String tsoUsrmod;

    @OneToMany(mappedBy = "tipoSorteo", fetch = FetchType.LAZY)
    private List<Sorteo> listaSorteos;

    public TipoSorteo() {
        this.descripcion = new SimpleStringProperty();
        this.numeroMinimo = new SimpleIntegerProperty(0);
        this.numeroMaximo = new SimpleIntegerProperty(0);
        this.montoMaximo = new SimpleDoubleProperty(0);
        this.cantNumVenta = new SimpleIntegerProperty(0);
        this.diasHabiles = new SimpleStringProperty();
        this.estado = new SimpleObjectProperty();
        this.horaCorte = new SimpleObjectProperty(LocalTime.now());
    }

    public TipoSorteo(Integer tsoCodigo) {
        this.codigo = tsoCodigo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tso_codigo")
    @Access(AccessType.PROPERTY)
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @Column(name = "tso_descripcion")
    @Access(AccessType.PROPERTY)
    public String getDescripcion() {
        return descripcion.get();
    }

    public SimpleStringProperty getDescripcionProperty() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    @Column(name = "tso_numminimo")
    @Access(AccessType.PROPERTY)
    public Integer getNumeroMinimo() {
        if (this.numeroMinimo == null) {
            this.numeroMinimo = new SimpleIntegerProperty();
        }
        return numeroMinimo.get();
    }

    public SimpleIntegerProperty getNumeroMinimoProperty() {
        if (this.numeroMinimo == null) {
            this.numeroMinimo = new SimpleIntegerProperty();
        }
        return this.numeroMinimo;
    }

    public void setNumeroMinimo(Integer numeroMinimo) {
        if (this.numeroMinimo == null) {
            this.numeroMinimo = new SimpleIntegerProperty();
        }
        this.numeroMinimo.set(numeroMinimo);
    }

    @Column(name = "tso_nummaximo")
    @Access(AccessType.PROPERTY)
    public Integer getNumeroMaximo() {
        if (this.numeroMaximo == null) {
            this.numeroMaximo = new SimpleIntegerProperty();
        }
        return numeroMaximo.get();
    }

    public SimpleIntegerProperty getNumeroMaximoProperty() {
        if (this.numeroMaximo == null) {
            this.numeroMaximo = new SimpleIntegerProperty();
        }
        return this.numeroMaximo;
    }

    public void setNumeroMaximo(Integer numeroMaximo) {
        if (this.numeroMaximo == null) {
            this.numeroMaximo = new SimpleIntegerProperty();
        }
        this.numeroMaximo.set(numeroMaximo);
    }

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "tso_montomax")
    @Access(AccessType.PROPERTY)
    public Double getMontoMaximo() {
        if (this.montoMaximo == null) {
            this.montoMaximo = new SimpleDoubleProperty();
        }
        return this.montoMaximo.get();
    }

    public SimpleDoubleProperty getMontoMaximoProperty() {
        if (this.montoMaximo == null) {
            this.montoMaximo = new SimpleDoubleProperty();
        }
        return this.montoMaximo;
    }

    public void setMontoMaximo(Double monto) {
        if (this.montoMaximo == null) {
            this.montoMaximo = new SimpleDoubleProperty();
        }
        this.montoMaximo.set(monto);
    }

    @Column(name = "tso_cantxventa")
    @Access(AccessType.PROPERTY)
    public Integer getCantNumVenta() {
        if (this.cantNumVenta == null) {
            this.cantNumVenta = new SimpleIntegerProperty();
        }
        return cantNumVenta.get();
    }

    public SimpleIntegerProperty getCantNumVentaProperty() {
        if (this.cantNumVenta == null) {
            this.cantNumVenta = new SimpleIntegerProperty();
        }
        return this.cantNumVenta;
    }

    public void setCantNumVenta(Integer numeroMinimo) {
        if (this.cantNumVenta == null) {
            this.cantNumVenta = new SimpleIntegerProperty();
        }
        this.cantNumVenta.set(numeroMinimo);
    }

    @Column(name = "tso_diashabiles")
    @Access(AccessType.PROPERTY)
    public String getDiasHabiles() {
        return diasHabiles.get();
    }

    public void setDiasHabiles(String diasHabiles) {
        this.diasHabiles.set(diasHabiles);
    }

    @Column(name = "tso_estado")
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

    public String getDescripcionEstado() {
        if (this.estado == null) {
            this.estado = new SimpleObjectProperty();
        }
        return this.estado.get().getDescripcion();
    }

    public void setEstado(String estado) {
        GenValorCombo valor = null;
        if (estado.equalsIgnoreCase("a")) {
            valor = new GenValorCombo(estado, "Activo");
        } else {
            valor = new GenValorCombo(estado, "Inactivo");
        }
        if (this.estado == null) {
            this.estado = new SimpleObjectProperty();
        }
        this.estado.set(valor);
    }

    @XmlTransient
    public List<Sorteo> getListaSorteos() {
        return listaSorteos;
    }

    public void setListaSorteos(List<Sorteo> listaSorteos) {
        this.listaSorteos = listaSorteos;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.codigo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TipoSorteo)) {
            return false;
        }
        final TipoSorteo other = (TipoSorteo) obj;
        return other.getCodigo().equals(this.getCodigo());
    }

    @Override
    public String toString() {
        return "TipoSorteo{" + "codigo=" + codigo + '}';
    }

    @Column(name = "tso_horacorte")
    @Temporal(TemporalType.TIME)
    @Access(AccessType.PROPERTY)
    public Date getHoraCorte() throws ParseException {
        if (horaCorte != null && horaCorte.get() != null) {
            Instant instant = horaCorte.get().atDate(LocalDate.of(2017, 1, 1)).atZone(ZoneId.systemDefault()).toInstant();
            return Date.from(instant);
        } else {
            return null;
        }
    }

    public SimpleObjectProperty getHoraCorteProperty() {
        if (this.horaCorte == null) {
            this.horaCorte = new SimpleObjectProperty();
        }
        return this.horaCorte;
    }

    public void setHoraCorte(Date horaCorte) {
        if (horaCorte != null) {
            this.horaCorte.set(LocalDateTime.ofInstant(Instant.ofEpochMilli(horaCorte.getTime()), ZoneId.systemDefault()).toLocalTime());
        }
    }

    public String getTsoUsrcrea() {
        return tsoUsrcrea;
    }

    public void setTsoUsrcrea(String tsoUsrcrea) {
        this.tsoUsrcrea = tsoUsrcrea;
    }

    public Date getTsoFeccrea() {
        return tsoFeccrea;
    }

    public void setTsoFeccrea(Date tsoFeccrea) {
        this.tsoFeccrea = tsoFeccrea;
    }

    public Date getTsoFecmod() {
        return tsoFecmod;
    }

    public void setTsoFecmod(Date tsoFecmod) {
        this.tsoFecmod = tsoFecmod;
    }

    public String getTsoUsrmod() {
        return tsoUsrmod;
    }

    public void setTsoUsrmod(String tsoUsrmod) {
        this.tsoUsrmod = tsoUsrmod;
    }

}
