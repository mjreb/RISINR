/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.RIS.MVC.model.JPA.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DDT1
 */
@Entity
@Table(name = "AsignacionEstudio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AsignacionEstudio.findAll", query = "SELECT a FROM AsignacionEstudio a"),
    @NamedQuery(name = "AsignacionEstudio.findByEquipoImagenologiaNSerie", query = "SELECT a FROM AsignacionEstudio a WHERE a.asignacionEstudioPK.equipoImagenologiaNSerie = :equipoImagenologiaNSerie"),
    @NamedQuery(name = "AsignacionEstudio.findByEstudioidEstudio", query = "SELECT a FROM AsignacionEstudio a WHERE a.asignacionEstudioPK.estudioidEstudio = :estudioidEstudio"),
    @NamedQuery(name = "AsignacionEstudio.findByFechaPk", query = "SELECT a FROM AsignacionEstudio a WHERE a.asignacionEstudioPK.fechaPk = :fechaPk"),
    @NamedQuery(name = "AsignacionEstudio.findByFecha", query = "SELECT a FROM AsignacionEstudio a WHERE a.fecha = :fecha")})
public class AsignacionEstudio implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AsignacionEstudioPK asignacionEstudioPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "EquipoImagenologia_NSerie", referencedColumnName = "NSerie", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EquipoImagenologia equipoImagenologia;
    @JoinColumn(name = "Estudio_idEstudio", referencedColumnName = "idEstudio", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Estudio estudio;

    public AsignacionEstudio() {
    }

    public AsignacionEstudio(AsignacionEstudioPK asignacionEstudioPK) {
        this.asignacionEstudioPK = asignacionEstudioPK;
    }

    public AsignacionEstudio(AsignacionEstudioPK asignacionEstudioPK, Date fecha) {
        this.asignacionEstudioPK = asignacionEstudioPK;
        this.fecha = fecha;
    }

    public AsignacionEstudio(String equipoImagenologiaNSerie, int estudioidEstudio, long fechaPk) {
        this.asignacionEstudioPK = new AsignacionEstudioPK(equipoImagenologiaNSerie, estudioidEstudio, fechaPk);
    }

    public AsignacionEstudioPK getAsignacionEstudioPK() {
        return asignacionEstudioPK;
    }

    public void setAsignacionEstudioPK(AsignacionEstudioPK asignacionEstudioPK) {
        this.asignacionEstudioPK = asignacionEstudioPK;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public EquipoImagenologia getEquipoImagenologia() {
        return equipoImagenologia;
    }

    public void setEquipoImagenologia(EquipoImagenologia equipoImagenologia) {
        this.equipoImagenologia = equipoImagenologia;
    }

    public Estudio getEstudio() {
        return estudio;
    }

    public void setEstudio(Estudio estudio) {
        this.estudio = estudio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (asignacionEstudioPK != null ? asignacionEstudioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsignacionEstudio)) {
            return false;
        }
        AsignacionEstudio other = (AsignacionEstudio) object;
        if ((this.asignacionEstudioPK == null && other.asignacionEstudioPK != null) || (this.asignacionEstudioPK != null && !this.asignacionEstudioPK.equals(other.asignacionEstudioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.AsignacionEstudio[ asignacionEstudioPK=" + asignacionEstudioPK + " ]";
    }
    
}
