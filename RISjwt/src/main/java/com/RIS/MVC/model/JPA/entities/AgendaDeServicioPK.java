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
public class AgendaDeServicioPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "EquipoImagenologia_NSerie")
    private String equipoImagenologiaNSerie;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Medico_NumEmpleado")
    private int medicoNumEmpleado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Medico_CURP")
    private String medicoCURP;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FechaControlPk")
    private long fechaControlPk;

    public AgendaDeServicioPK() {
    }

    public AgendaDeServicioPK(String equipoImagenologiaNSerie, int medicoNumEmpleado, String medicoCURP, long fechaControlPk) {
        this.equipoImagenologiaNSerie = equipoImagenologiaNSerie;
        this.medicoNumEmpleado = medicoNumEmpleado;
        this.medicoCURP = medicoCURP;
        this.fechaControlPk = fechaControlPk;
    }

    public String getEquipoImagenologiaNSerie() {
        return equipoImagenologiaNSerie;
    }

    public void setEquipoImagenologiaNSerie(String equipoImagenologiaNSerie) {
        this.equipoImagenologiaNSerie = equipoImagenologiaNSerie;
    }

    public int getMedicoNumEmpleado() {
        return medicoNumEmpleado;
    }

    public void setMedicoNumEmpleado(int medicoNumEmpleado) {
        this.medicoNumEmpleado = medicoNumEmpleado;
    }

    public String getMedicoCURP() {
        return medicoCURP;
    }

    public void setMedicoCURP(String medicoCURP) {
        this.medicoCURP = medicoCURP;
    }

    public long getFechaControlPk() {
        return fechaControlPk;
    }

    public void setFechaControlPk(long fechaControlPk) {
        this.fechaControlPk = fechaControlPk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (equipoImagenologiaNSerie != null ? equipoImagenologiaNSerie.hashCode() : 0);
        hash += (int) medicoNumEmpleado;
        hash += (medicoCURP != null ? medicoCURP.hashCode() : 0);
        hash += (int) fechaControlPk;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AgendaDeServicioPK)) {
            return false;
        }
        AgendaDeServicioPK other = (AgendaDeServicioPK) object;
        if ((this.equipoImagenologiaNSerie == null && other.equipoImagenologiaNSerie != null) || (this.equipoImagenologiaNSerie != null && !this.equipoImagenologiaNSerie.equals(other.equipoImagenologiaNSerie))) {
            return false;
        }
        if (this.medicoNumEmpleado != other.medicoNumEmpleado) {
            return false;
        }
        if ((this.medicoCURP == null && other.medicoCURP != null) || (this.medicoCURP != null && !this.medicoCURP.equals(other.medicoCURP))) {
            return false;
        }
        if (this.fechaControlPk != other.fechaControlPk) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.AgendaDeServicioPK[ equipoImagenologiaNSerie=" + equipoImagenologiaNSerie + ", medicoNumEmpleado=" + medicoNumEmpleado + ", medicoCURP=" + medicoCURP + ", fechaControlPk=" + fechaControlPk + " ]";
    }
    
}
