/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.model.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jdiego
 */
@Entity
@Table(name = "sor_detventasfecha", schema = "sorteo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detventasfecha.findAll", query = "SELECT d FROM Detventasfecha d")})
public class DetVentasFecha implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "codtiposorteo")
    private int codtiposorteo;
    @Id
    @Column(name = "tiposorteo")
    private String tiposorteo;
    @Id
    @Basic(optional = false)
    @Column(name = "codigofactura")
    private int codigofactura;
    @Id
    @Column(name = "fechaventa")
    @Temporal(TemporalType.DATE)
    private Date fechaventa;
    @Id
    @Column(name = "fechasorteo")
    @Temporal(TemporalType.DATE)
    private Date fechasorteo;
    @Id
    @Column(name = "dfa_numero")
    private Integer dfaNumero;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Column(name = "apuesta")
    private Double apuesta;

    public DetVentasFecha() {
    }

    public int getCodtiposorteo() {
        return codtiposorteo;
    }

    public void setCodtiposorteo(int codtiposorteo) {
        this.codtiposorteo = codtiposorteo;
    }

    public String getTiposorteo() {
        return tiposorteo;
    }

    public void setTiposorteo(String tiposorteo) {
        this.tiposorteo = tiposorteo;
    }

    public Date getFechaventa() {
        return fechaventa;
    }

    public void setFechaventa(Date fechaventa) {
        this.fechaventa = fechaventa;
    }

    public Date getFechasorteo() {
        return fechasorteo;
    }

    public void setFechasorteo(Date fechasorteo) {
        this.fechasorteo = fechasorteo;
    }

    public Integer getDfaNumero() {
        return dfaNumero;
    }

    public void setDfaNumero(Integer dfaNumero) {
        this.dfaNumero = dfaNumero;
    }

    public Double getApuesta() {
        return apuesta;
    }

    public void setApuesta(Double apuesta) {
        this.apuesta = apuesta;
    }

    public int getCodigofactura() {
        return codigofactura;
    }

    public void setCodigofactura(int codigofactura) {
        this.codigofactura = codigofactura;
    }

}
