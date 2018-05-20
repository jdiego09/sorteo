/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.entities;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(name = "sor_usuario", schema = "sorteo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u ORDER BY u.consecutivo")
    , @NamedQuery(name = "Usuario.findByUsuCodigo", query = "SELECT u FROM Usuario u WHERE u.usuCodigo = :usuCodigo and u.usuEstado = 'A'")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    private SimpleStringProperty usuCodigo;
    @Transient
    private SimpleStringProperty usuDescripcion;
    @Transient
    private SimpleStringProperty usuContrasena;
    @Transient
    private SimpleIntegerProperty consecutivo;

    @Transient
    private SimpleStringProperty consecutivoString;

    @Transient
    private SimpleIntegerProperty usuPin;

    @Transient
    private ObjectProperty<GenValorCombo> usuEstado;
    @JoinColumn(name = "usu_codsucursal", referencedColumnName = "suc_codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Sucursal usuCodsucursal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rxuCodusuario", fetch = FetchType.LAZY)
    private List<RolXUsuario> rolXUsuarioList;

    public Usuario() {
        this.usuCodigo = new SimpleStringProperty();
        this.usuDescripcion = new SimpleStringProperty();
        this.usuContrasena = new SimpleStringProperty();
        this.usuEstado = new SimpleObjectProperty();
        this.consecutivo = new SimpleIntegerProperty();
        this.usuPin = new SimpleIntegerProperty();
    }

    public Usuario(String usuCodigo) {
        this.usuCodigo.set(usuCodigo);
    }

    @Id
    @Basic(optional = false)
    @Column(name = "usu_codigo")
    @Access(AccessType.PROPERTY)
    public String getUsuCodigo() {
        return usuCodigo.get();
    }

    public SimpleStringProperty getUsuCodigoProperty() {
        return usuCodigo;
    }

    public void setUsuCodigo(String usuCodigo) {
        this.usuCodigo.set(usuCodigo);
    }

    @Column(name = "usu_descripcion")
    @Access(AccessType.PROPERTY)

    public String getUsuDescripcion() {
        return usuDescripcion.get();
    }

    public SimpleStringProperty getUsuDescripcionProperty() {
        return usuDescripcion;
    }

    public void setUsuDescripcion(String usuDescripcion) {
        this.usuDescripcion.set(usuDescripcion);
    }

    @Column(name = "usu_contrasena")
    @Access(AccessType.PROPERTY)
    public String getUsuContrasena() {
        return usuContrasena.get();
    }

    public void setUsuContrasena(String usuContrasena) {
        this.usuContrasena.set(usuContrasena);
    }

    @Column(name = "usu_consecutivo")
    @Access(AccessType.PROPERTY)
    public Integer getConsecutivo() {
        return consecutivo.get();
    }

    public SimpleIntegerProperty getConsecutivoProperty() {
        return consecutivo;
    }

    public void setConsecutivo(Integer consecutivo) {
        this.consecutivo.set(consecutivo);
    }

    public String getConsecutivoString() {
        if (this.consecutivoString == null) {
            this.consecutivoString = new SimpleStringProperty();
        }
        String codigo = null;
        if (this.consecutivo.get() < 10) {
            codigo = "00" + String.valueOf(this.consecutivo.get());
        }
        if (this.consecutivo.get() < 100) {
            codigo = "0" + String.valueOf(this.consecutivo.get());
        }

        this.consecutivoString.set(codigo);
        return this.consecutivoString.get();
    }

    @Column(name = "usu_pin")
    @Access(AccessType.PROPERTY)
    public Integer getUsuPin() {
        return usuPin.get();
    }

    public SimpleIntegerProperty getUsuPinProperty() {
        return usuPin;
    }

    public void setUsuPin(Integer usuPin) {
        if (usuPin != null) {
            this.usuPin.set(usuPin);
        } else {
            this.usuPin = new SimpleIntegerProperty();
        }
    }

    @Column(name = "usu_estado")
    @Access(AccessType.PROPERTY)
    public String getUsuEstado() {
        return usuEstado.get().getCodigo();
    }

    public ObjectProperty getUsuEstadoProperty() {
        return usuEstado;
    }

    public void setUsuEstado(String usuEstado) {
        GenValorCombo valorEstado = null;
        if (this.usuEstado == null) {
            this.usuEstado = new SimpleObjectProperty();
        }
        if (usuEstado.equalsIgnoreCase("a")) {
            valorEstado = new GenValorCombo("A", "Activo");
        } else {
            valorEstado = new GenValorCombo("I", "Inactivo");
        }
        this.usuEstado.set(valorEstado);
    }

    public String getDescripcionEstado() {
        if (this.usuEstado == null) {
            this.usuEstado = new SimpleObjectProperty();
        }
        return this.usuEstado.get().getDescripcion();
    }

    public Sucursal getUsuCodsucursal() {
        return usuCodsucursal;
    }

    public void setUsuCodsucursal(Sucursal usuCodsucursal) {
        this.usuCodsucursal = usuCodsucursal;
    }

    @XmlTransient
    public List<RolXUsuario> getRolXUsuarioList() {
        return rolXUsuarioList;
    }

    public void setRolXUsuarioList(List<RolXUsuario> rolXUsuarioList) {
        this.rolXUsuarioList = rolXUsuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuCodigo != null ? usuCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.usuCodigo == null && other.usuCodigo != null) || (this.usuCodigo != null && !this.usuCodigo.equals(other.usuCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sorteo.model.entities.Usuario[ usuCodigo=" + usuCodigo + " ]";
    }

}
