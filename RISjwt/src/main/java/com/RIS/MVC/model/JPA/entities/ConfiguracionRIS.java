/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.RIS.MVC.model.JPA.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "ConfiguracionRIS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConfiguracionRIS.findAll", query = "SELECT c FROM ConfiguracionRIS c"),
    @NamedQuery(name = "ConfiguracionRIS.findByEquipoNSerie", query = "SELECT c FROM ConfiguracionRIS c WHERE c.equipoNSerie = :equipoNSerie"),
    @NamedQuery(name = "ConfiguracionRIS.findByTipoConfiguracion", query = "SELECT c FROM ConfiguracionRIS c WHERE c.tipoConfiguracion = :tipoConfiguracion"),
    @NamedQuery(name = "ConfiguracionRIS.findByIPAddress", query = "SELECT c FROM ConfiguracionRIS c WHERE c.iPAddress = :iPAddress"),
    @NamedQuery(name = "ConfiguracionRIS.findByPuerto", query = "SELECT c FROM ConfiguracionRIS c WHERE c.puerto = :puerto"),
    @NamedQuery(name = "ConfiguracionRIS.findByNombreEntidad", query = "SELECT c FROM ConfiguracionRIS c WHERE c.nombreEntidad = :nombreEntidad")})
public class ConfiguracionRIS implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Equipo_NSerie")
    private String equipoNSerie;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "TipoConfiguracion")
    private String tipoConfiguracion;
    @Size(max = 15)
    @Column(name = "IPAddress")
    private String iPAddress;
    @Column(name = "Puerto")
    private Integer puerto;
    @Size(max = 16)
    @Column(name = "NombreEntidad")
    private String nombreEntidad;
    @JoinColumn(name = "Equipo_NSerie", referencedColumnName = "NSerie", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Equipo equipo;

    public ConfiguracionRIS() {
    }

    public ConfiguracionRIS(String equipoNSerie) {
        this.equipoNSerie = equipoNSerie;
    }

    public ConfiguracionRIS(String equipoNSerie, String tipoConfiguracion) {
        this.equipoNSerie = equipoNSerie;
        this.tipoConfiguracion = tipoConfiguracion;
    }

    public String getEquipoNSerie() {
        return equipoNSerie;
    }

    public void setEquipoNSerie(String equipoNSerie) {
        this.equipoNSerie = equipoNSerie;
    }

    public String getTipoConfiguracion() {
        return tipoConfiguracion;
    }

    public void setTipoConfiguracion(String tipoConfiguracion) {
        this.tipoConfiguracion = tipoConfiguracion;
    }

    public String getIPAddress() {
        return iPAddress;
    }

    public void setIPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
    }

    public Integer getPuerto() {
        return puerto;
    }

    public void setPuerto(Integer puerto) {
        this.puerto = puerto;
    }

    public String getNombreEntidad() {
        return nombreEntidad;
    }

    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (equipoNSerie != null ? equipoNSerie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConfiguracionRIS)) {
            return false;
        }
        ConfiguracionRIS other = (ConfiguracionRIS) object;
        if ((this.equipoNSerie == null && other.equipoNSerie != null) || (this.equipoNSerie != null && !this.equipoNSerie.equals(other.equipoNSerie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.ConfiguracionRIS[ equipoNSerie=" + equipoNSerie + " ]";
    }
    
}
