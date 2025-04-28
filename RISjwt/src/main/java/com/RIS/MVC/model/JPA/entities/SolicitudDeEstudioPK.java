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
public class SolicitudDeEstudioPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Paciente_idPaciente")
    private String pacienteidPaciente;
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
    @Column(name = "FechaSolicitudPk")
    private long fechaSolicitudPk;

    public SolicitudDeEstudioPK() {
    }

    public SolicitudDeEstudioPK(String pacienteidPaciente, int medicoNumEmpleado, String medicoCURP, long fechaSolicitudPk) {
        this.pacienteidPaciente = pacienteidPaciente;
        this.medicoNumEmpleado = medicoNumEmpleado;
        this.medicoCURP = medicoCURP;
        this.fechaSolicitudPk = fechaSolicitudPk;
    }

    public String getPacienteidPaciente() {
        return pacienteidPaciente;
    }

    public void setPacienteidPaciente(String pacienteidPaciente) {
        this.pacienteidPaciente = pacienteidPaciente;
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

    public long getFechaSolicitudPk() {
        return fechaSolicitudPk;
    }

    public void setFechaSolicitudPk(long fechaSolicitudPk) {
        this.fechaSolicitudPk = fechaSolicitudPk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pacienteidPaciente != null ? pacienteidPaciente.hashCode() : 0);
        hash += (int) medicoNumEmpleado;
        hash += (medicoCURP != null ? medicoCURP.hashCode() : 0);
        hash += (int) fechaSolicitudPk;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SolicitudDeEstudioPK)) {
            return false;
        }
        SolicitudDeEstudioPK other = (SolicitudDeEstudioPK) object;
        if ((this.pacienteidPaciente == null && other.pacienteidPaciente != null) || (this.pacienteidPaciente != null && !this.pacienteidPaciente.equals(other.pacienteidPaciente))) {
            return false;
        }
        if (this.medicoNumEmpleado != other.medicoNumEmpleado) {
            return false;
        }
        if ((this.medicoCURP == null && other.medicoCURP != null) || (this.medicoCURP != null && !this.medicoCURP.equals(other.medicoCURP))) {
            return false;
        }
        if (this.fechaSolicitudPk != other.fechaSolicitudPk) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.SolicitudDeEstudioPK[ pacienteidPaciente=" + pacienteidPaciente + ", medicoNumEmpleado=" + medicoNumEmpleado + ", medicoCURP=" + medicoCURP + ", fechaSolicitudPk=" + fechaSolicitudPk + " ]";
    }
    
}
