/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.RIS.MVC.model.JPA.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author DDT1
 */
@Embeddable
public class AsignacionEstudioPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "EquipoImagenologia_NSerie")
    private String equipoImagenologiaNSerie;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Estudio_idEstudio")
    private int estudioidEstudio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FechaPk")
    private long fechaPk;

    public AsignacionEstudioPK() {
    }

    public AsignacionEstudioPK(String equipoImagenologiaNSerie, int estudioidEstudio, long fechaPk) {
        this.equipoImagenologiaNSerie = equipoImagenologiaNSerie;
        this.estudioidEstudio = estudioidEstudio;
        this.fechaPk = fechaPk;
    }

    public String getEquipoImagenologiaNSerie() {
        return equipoImagenologiaNSerie;
    }

    public void setEquipoImagenologiaNSerie(String equipoImagenologiaNSerie) {
        this.equipoImagenologiaNSerie = equipoImagenologiaNSerie;
    }

    public int getEstudioidEstudio() {
        return estudioidEstudio;
    }

    public void setEstudioidEstudio(int estudioidEstudio) {
        this.estudioidEstudio = estudioidEstudio;
    }

    public long getFechaPk() {
        return fechaPk;
    }

    public void setFechaPk(long fechaPk) {
        this.fechaPk = fechaPk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (equipoImagenologiaNSerie != null ? equipoImagenologiaNSerie.hashCode() : 0);
        hash += (int) estudioidEstudio;
        hash += (int) fechaPk;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsignacionEstudioPK)) {
            return false;
        }
        AsignacionEstudioPK other = (AsignacionEstudioPK) object;
        if ((this.equipoImagenologiaNSerie == null && other.equipoImagenologiaNSerie != null) || (this.equipoImagenologiaNSerie != null && !this.equipoImagenologiaNSerie.equals(other.equipoImagenologiaNSerie))) {
            return false;
        }
        if (this.estudioidEstudio != other.estudioidEstudio) {
            return false;
        }
        if (this.fechaPk != other.fechaPk) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.AsignacionEstudioPK[ equipoImagenologiaNSerie=" + equipoImagenologiaNSerie + ", estudioidEstudio=" + estudioidEstudio + ", fechaPk=" + fechaPk + " ]";
    }
    
}
