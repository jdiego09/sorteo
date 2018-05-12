/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.entities;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "sor_sucursal", schema = "sorteo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sucursal.findAll", query = "SELECT s FROM Sucursal s")
    , @NamedQuery(name = "Sucursal.findByCodigo", query = "SELECT s FROM Sucursal s WHERE s.codigo = :codigo")
    , @NamedQuery(name = "Sucursal.findByNombre", query = "SELECT s FROM Sucursal s WHERE s.nombre = :nombre")})
public class Sucursal implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Transient
    private Integer codigo;
    @Transient
    private SimpleStringProperty nombre;
    @Transient
    private SimpleStringProperty telefono;
    @Transient
    private SimpleStringProperty email;
    @Transient
    private ObjectProperty<GenValorCombo> estado;
    
    @OneToMany(mappedBy = "usuCodsucursal", fetch = FetchType.LAZY)
    private List<Usuario> listaUsuarios;
    @OneToMany(mappedBy = "sucursal", fetch = FetchType.LAZY)
    private List<Sorteo> listaSorteos;
    @JoinColumn(name = "suc_codempresa", referencedColumnName = "emp_codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Empresa empresa;

    public Sucursal() {
        this.nombre = new SimpleStringProperty();
        this.telefono = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.estado = new SimpleObjectProperty();        
    }

    public Sucursal(Integer sucCodigo) {
        this.codigo = sucCodigo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "suc_codigo")
    @Access(AccessType.PROPERTY)
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @Column(name = "suc_nombre")
    @Access(AccessType.PROPERTY)
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

    @Column(name = "suc_telefono")
    @Access(AccessType.PROPERTY)
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

    @Column(name = "suc_email")
    @Access(AccessType.PROPERTY)
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

    @Column(name = "suc_estado")
    @Access(AccessType.PROPERTY)
    public String getEstado() {
        if (this.estado == null) {
            this.estado = new SimpleObjectProperty();
        }
        return estado.get().getCodigo();
    }

    public void setEstado(String estado) {
        GenValorCombo valorEstado = null;
        if (this.estado == null) {
            this.estado = new SimpleObjectProperty();
        }
        if (estado.equalsIgnoreCase("a")) {
            valorEstado = new GenValorCombo("A", "Activo");
        } else {
            valorEstado = new GenValorCombo("I", "Inactivo");
        }
        this.estado.set(valorEstado);
    }
    
    public ObjectProperty getEstadoProperty() {
        return estado;
    }
    
    public String getDescripcionEstado() {
        return this.estado.get().getDescripcion();
    }
        
    @XmlTransient
    public List<Sorteo> getListaSorteos() {
        return listaSorteos;
    }

    public void setListaSorteos(List<Sorteo> listaSorteos) {
        this.listaSorteos = listaSorteos;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
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
        if (!(object instanceof Sucursal)) {
            return false;
        }
        Sucursal other = (Sucursal) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }
   
    @XmlTransient
    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    @Override
    public String toString() {
        return "Sucursal{" + "codigo=" + codigo + '}';
    }
    
    

}
