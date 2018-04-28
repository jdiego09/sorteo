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
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
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

/**
 *
 * @author jdiego
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "sor_sorteo", schema = "sorteo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sorteo.findAll", query = "SELECT s FROM Sorteo s")
    , @NamedQuery(name = "Sorteo.findByCodigo", query = "SELECT s FROM Sorteo s WHERE s.codigo = :codigo")
    , @NamedQuery(name = "Sorteo.findByFecha", query = "select e from Sorteo e join e.tipoSorteo t\n"
    + "where e.fecha = :fechaSorteo\n"
    + "and t.codigo = :tipoSorteo")
    ,
@NamedQuery(name = "Sorteo.getApuestaNumero", query = "select coalesce(sum(d.monto),0)\n"
    + "  from DetalleFactura d \n"
    + "  join d.factura f\n"
    + "  join f.sorteo s\n"
    + "  join s.tipoSorteo t\n"
    + " where t.codigo = :codTSorteo\n"
    + "   and s.codigo = :codSorteo\n"
    + "   and s.fecha = :fecSorteo\n"
    + "   and d.numero = :numero")})
public class Sorteo implements Serializable {

   private static final long serialVersionUID = 1L;

   @Transient
   private Integer codigo;
   @Transient
   SimpleObjectProperty<LocalDate> fecha;
   @OneToMany(mappedBy = "sorteo", fetch = FetchType.LAZY)
   private List<Factura> listaFacturas;
   @JoinColumn(name = "sor_codsucursal", referencedColumnName = "suc_codigo")
   @ManyToOne(fetch = FetchType.LAZY)
   private Sucursal sucursal;
   @JoinColumn(name = "sor_tiposorteo", referencedColumnName = "tso_codigo")
   @ManyToOne(fetch = FetchType.LAZY)
   private TipoSorteo tipoSorteo;

   public Sorteo() {
   }

   public Sorteo(Integer sorCodigo) {
      this.codigo = sorCodigo;
   }

   public Sorteo(Date fecha, TipoSorteo tipoSorteo, Sucursal sucursal) {
      this.fecha = new SimpleObjectProperty<>();
      this.setFecha(fecha);
      this.sucursal = sucursal;
      this.tipoSorteo = tipoSorteo;
   }

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "sor_codigo")
   @Access(AccessType.PROPERTY)
   public Integer getCodigo() {
      return codigo;
   }

   public void setCodigo(Integer codigo) {
      this.codigo = codigo;
   }

   @Column(name = "sor_fecha")
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
         if (this.fecha == null) {
            this.fecha = new SimpleObjectProperty();
         }
         this.fecha.set(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
      }
   }

   @XmlTransient
   public List<Factura> getListaFacturas() {
      return listaFacturas;
   }

   public void setListaFacturas(List<Factura> listaFacturas) {
      this.listaFacturas = listaFacturas;
   }

   public Sucursal getSucursal() {
      return sucursal;
   }

   public void setSucursal(Sucursal sucursal) {
      this.sucursal = sucursal;
   }

   public TipoSorteo getTipoSorteo() {
      return tipoSorteo;
   }

   public void setTipoSorteo(TipoSorteo tipoSorteo) {
      this.tipoSorteo = tipoSorteo;
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
      if (!(object instanceof Sorteo)) {
         return false;
      }
      Sorteo other = (Sorteo) object;
      if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "Sorteo{" + "codigo=" + codigo + '}';
   }

}
