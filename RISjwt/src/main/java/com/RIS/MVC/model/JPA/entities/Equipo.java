/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.RIS.MVC.model.JPA.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DDT1
 */
@Entity
@Table(name = "Equipo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Equipo.findAll", query = "SELECT e FROM Equipo e"),
    @NamedQuery(name = "Equipo.findByNSerie", query = "SELECT e FROM Equipo e WHERE e.nSerie = :nSerie"),
    @NamedQuery(name = "Equipo.findByUbicacion", query = "SELECT e FROM Equipo e WHERE e.ubicacion = :ubicacion")})
public class Equipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NSerie")
    private String nSerie;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "Ubicacion")
    private String ubicacion;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "equipo")
    private ConfiguracionRIS configuracionRIS;
    @JoinColumn(name = "AreaDeServicio_idArea", referencedColumnName = "idArea")
    @ManyToOne(optional = false)
    private AreaDeServicio areaDeServicioidArea;

    public Equipo() {
    }

    public Equipo(String nSerie) {
        this.nSerie = nSerie;
    }

    public Equipo(String nSerie, String ubicacion) {
        this.nSerie = nSerie;
        this.ubicacion = ubicacion;
    }

    public String getNSerie() {
        return nSerie;
    }

    public void setNSerie(String nSerie) {
        this.nSerie = nSerie;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public ConfiguracionRIS getConfiguracionRIS() {
        return configuracionRIS;
    }

    public void setConfiguracionRIS(ConfiguracionRIS configuracionRIS) {
        this.configuracionRIS = configuracionRIS;
    }

    public AreaDeServicio getAreaDeServicioidArea() {
        return areaDeServicioidArea;
    }

    public void setAreaDeServicioidArea(AreaDeServicio areaDeServicioidArea) {
        this.areaDeServicioidArea = areaDeServicioidArea;
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
        if (!(object instanceof Equipo)) {
            return false;
        }
        Equipo other = (Equipo) object;
        if ((this.nSerie == null && other.nSerie != null) || (this.nSerie != null && !this.nSerie.equals(other.nSerie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.Equipo[ nSerie=" + nSerie + " ]";
    }
    
}
