package com.RIS.MVC.model.JPA.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DDT1
 */
@Entity
@Table(name = "EquipoImagenologia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EquipoImagenologia.findAll", query = "SELECT e FROM EquipoImagenologia e"),
    @NamedQuery(name = "EquipoImagenologia.findByNSerie", query = "SELECT e FROM EquipoImagenologia e WHERE e.nSerie = :nSerie"),
    @NamedQuery(name = "EquipoImagenologia.findByNombre", query = "SELECT e FROM EquipoImagenologia e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "EquipoImagenologia.findByMarca", query = "SELECT e FROM EquipoImagenologia e WHERE e.marca = :marca"),
    @NamedQuery(name = "EquipoImagenologia.findByModelo", query = "SELECT e FROM EquipoImagenologia e WHERE e.modelo = :modelo"),
    @NamedQuery(name = "EquipoImagenologia.findByModalidad", query = "SELECT e FROM EquipoImagenologia e WHERE e.modalidad = :modalidad"),
    @NamedQuery(name = "EquipoImagenologia.findByFechaInstalacion", query = "SELECT e FROM EquipoImagenologia e WHERE e.fechaInstalacion = :fechaInstalacion"),
    @NamedQuery(name = "EquipoImagenologia.findByEstado", query = "SELECT e FROM EquipoImagenologia e WHERE e.estado = :estado")})
public class EquipoImagenologia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NSerie")
    private String nSerie;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "Nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "Marca")
    private String marca;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "Modelo")
    private String modelo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "Modalidad")
    private String modalidad;
    @Column(name = "FechaInstalacion")
    @Temporal(TemporalType.DATE)
    private Date fechaInstalacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "Estado")
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "equipoImagenologia")
    private Collection<AsignacionEstudio> asignacionEstudioCollection;
    @JoinColumn(name = "AreaDeServicio_idArea", referencedColumnName = "idArea")
    @ManyToOne(optional = false)
    private AreaDeServicio areaDeServicioidArea;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "equipoImagenologia")
    private Collection<AgendaDeServicio> agendaDeServicioCollection;

    public EquipoImagenologia() {
    }

    public EquipoImagenologia(String nSerie) {
        this.nSerie = nSerie;
    }

    public EquipoImagenologia(String nSerie, String nombre, String marca, String modelo, String modalidad, String estado) {
        this.nSerie = nSerie;
        this.nombre = nombre;
        this.marca = marca;
        this.modelo = modelo;
        this.modalidad = modalidad;
        this.estado = estado;
    }

    public String getNSerie() {
        return nSerie;
    }

    public void setNSerie(String nSerie) {
        this.nSerie = nSerie;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public Date getFechaInstalacion() {
        return fechaInstalacion;
    }

    public void setFechaInstalacion(Date fechaInstalacion) {
        this.fechaInstalacion = fechaInstalacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public Collection<AsignacionEstudio> getAsignacionEstudioCollection() {
        return asignacionEstudioCollection;
    }

    public void setAsignacionEstudioCollection(Collection<AsignacionEstudio> asignacionEstudioCollection) {
        this.asignacionEstudioCollection = asignacionEstudioCollection;
    }

    public AreaDeServicio getAreaDeServicioidArea() {
        return areaDeServicioidArea;
    }

    public void setAreaDeServicioidArea(AreaDeServicio areaDeServicioidArea) {
        this.areaDeServicioidArea = areaDeServicioidArea;
    }

    @XmlTransient
    public Collection<AgendaDeServicio> getAgendaDeServicioCollection() {
        return agendaDeServicioCollection;
    }

    public void setAgendaDeServicioCollection(Collection<AgendaDeServicio> agendaDeServicioCollection) {
        this.agendaDeServicioCollection = agendaDeServicioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nSerie != null ? nSerie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquipoImagenologia)) {
            return false;
        }
        EquipoImagenologia other = (EquipoImagenologia) object;
        if ((this.nSerie == null && other.nSerie != null) || (this.nSerie != null && !this.nSerie.equals(other.nSerie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.EquipoImagenologia[ nSerie=" + nSerie + " ]";
    }
    
}
