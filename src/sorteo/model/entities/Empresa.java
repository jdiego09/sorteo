/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jdiego
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "sor_empresa", schema = "sorteo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empresa.findAll", query = "SELECT s FROM Empresa s")
    , @NamedQuery(name = "Empresa.findByCodigo", query = "SELECT s FROM Empresa s WHERE s.codigo = :codigo")
    , @NamedQuery(name = "Empresa.findByCedJuridica", query = "SELECT s FROM Empresa s WHERE s.cedJuridica = :cedJuridica")})
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    private Integer codigo;
    @Transient
    private SimpleStringProperty cedJuridica;
    @Transient
    private SimpleStringProperty nombre;
    @Transient
    private SimpleStringProperty logo;
    @Transient
    private SimpleStringProperty telefono;
    @Transient
    private SimpleStringProperty email;
    @OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY)
    private List<Sucursal> sucursales;

    public Empresa() {
        this.cedJuridica = new SimpleStringProperty();
        this.nombre = new SimpleStringProperty();
        this.logo = new SimpleStringProperty();
        this.telefono = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
    }

    public Empresa(Integer empCodigo) {
        this.codigo = empCodigo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "emp_codigo")
    @Access(AccessType.PROPERTY)
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @Access(AccessType.PROPERTY)
    @Column(name = "emp_cedjuridica")
    public String getCedJuridica() {
        return cedJuridica.get();
    }

    public SimpleStringProperty getCedJuridicaProperty() {
        if (this.cedJuridica == null) {
            this.cedJuridica = new SimpleStringProperty();
        }
        return this.cedJuridica;
    }

    public void setCedJuridica(String cedJuridica) {
        this.cedJuridica.set(cedJuridica);
    }

    @Access(AccessType.PROPERTY)
    @Column(name = "emp_nombre")
    public String getNombre() {
        return nombre.get();
    }

    public SimpleStringProperty getNombreProperty() {
        if (this.nombre == null) {
            this.nombre = new SimpleStringProperty();
        }
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    @Access(AccessType.PROPERTY)
    @Column(name = "emp_logo")
    public String getLogo() {
        return logo.get();
    }

    public SimpleStringProperty getLogoProperty() {
        if (this.logo == null) {
            this.logo = new SimpleStringProperty();
        }
        return this.logo;
    }

    public void setLogo(String logo) {
        this.logo.set(logo);
    }

    @Access(AccessType.PROPERTY)
    @Column(name = "emp_telefono")
    public String getTelefono() {
        return telefono.get();
    }

    public SimpleStringProperty getTelefonoProperty() {
        if (this.telefono == null) {
            this.telefono = new SimpleStringProperty();
        }
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono.set(telefono);
    }

    @Access(AccessType.PROPERTY)
    @Column(name = "emp_email")
    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty getEmailProperty() {
        if (this.email == null) {
            this.email = new SimpleStringProperty();
        }
        return this.email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    @XmlTransient
    public List<Sucursal> getSucursales() {
        return sucursales;
    }

    public void setSucursales(List<Sucursal> sucursales) {
        this.sucursales = sucursales;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
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
        final Empresa other = (Empresa) obj;
        if (!Objects.equals(this.cedJuridica, other.cedJuridica)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Empresa{" + "codigo=" + codigo + '}';
    }
}
