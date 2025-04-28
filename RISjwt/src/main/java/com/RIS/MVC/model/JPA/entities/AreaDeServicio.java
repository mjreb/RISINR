package com.RIS.MVC.model.JPA.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DDT1
 */
@Entity
@Table(name = "AreaDeServicio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AreaDeServicio.findAll", query = "SELECT a FROM AreaDeServicio a"),
    @NamedQuery(name = "AreaDeServicio.findByIdArea", query = "SELECT a FROM AreaDeServicio a WHERE a.idArea = :idArea"),
    @NamedQuery(name = "AreaDeServicio.findByNombre", query = "SELECT a FROM AreaDeServicio a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "AreaDeServicio.findByDescripcion", query = "SELECT a FROM AreaDeServicio a WHERE a.descripcion = :descripcion")})
public class AreaDeServicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idArea")
    private Integer idArea;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "Nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 120)
    @Column(name = "Descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "areaidArea")
    private Collection<Usuario> usuarioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "areaDeServicioidArea")
    private Collection<Equipo> equipoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "areaDeServicioidArea")
    private Collection<EquipoImagenologia> equipoImagenologiaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "areaDeServicioidArea")
    private Collection<Estudio> estudioCollection;

    public AreaDeServicio() {
    }

    public AreaDeServicio(Integer idArea) {
        this.idArea = idArea;
    }

    public AreaDeServicio(Integer idArea, String nombre, String descripcion) {
        this.idArea = idArea;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getIdArea() {
        return idArea;
    }

    public void setIdArea(Integer idArea) {
        this.idArea = idArea;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    @XmlTransient
    public Collection<Equipo> getEquipoCollection() {
        return equipoCollection;
    }

    public void setEquipoCollection(Collection<Equipo> equipoCollection) {
        this.equipoCollection = equipoCollection;
    }

    @XmlTransient
    public Collection<EquipoImagenologia> getEquipoImagenologiaCollection() {
        return equipoImagenologiaCollection;
    }

    public void setEquipoImagenologiaCollection(Collection<EquipoImagenologia> equipoImagenologiaCollection) {
        this.equipoImagenologiaCollection = equipoImagenologiaCollection;
    }

    @XmlTransient
    public Collection<Estudio> getEstudioCollection() {
        return estudioCollection;
    }

    public void setEstudioCollection(Collection<Estudio> estudioCollection) {
        this.estudioCollection = estudioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArea != null ? idArea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AreaDeServicio)) {
            return false;
        }
        AreaDeServicio other = (AreaDeServicio) object;
        if ((this.idArea == null && other.idArea != null) || (this.idArea != null && !this.idArea.equals(other.idArea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.AreaDeServicio[ idArea=" + idArea + " ]";
    }
    
}
