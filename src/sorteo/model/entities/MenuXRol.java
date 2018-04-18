/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.entities;

import java.io.Serializable;
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
@Table(name = "sor_menuxrol", schema = "sorteo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MenuXRol.findAll", query = "SELECT s FROM MenuXRol s")
    , @NamedQuery(name = "MenuXRol.findByCodigo", query = "SELECT s FROM MenuXRol s WHERE s.codigo = :codigo")})
public class MenuXRol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    private Integer codigo;
    @JoinColumn(name = "mxr_codmenu", referencedColumnName = "men_codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Menu listaOpciones;
    @JoinColumn(name = "mxr_codrol", referencedColumnName = "rol_codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Roles listaRoles;

    public MenuXRol() {
    }

    public MenuXRol(Integer codigo) {
        this.codigo = codigo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mxr_codigo")
    @Access(AccessType.PROPERTY)
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Menu getListaOpciones() {
        return listaOpciones;
    }

    public void setListaOpciones(Menu listaOpciones) {
        this.listaOpciones = listaOpciones;
    }

    public Roles getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(Roles listaRoles) {
        this.listaRoles = listaRoles;
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
        if (!(object instanceof MenuXRol)) {
            return false;
        }
        MenuXRol other = (MenuXRol) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MenuXRol{" + "codigo=" + codigo + '}';
    }
   
}
