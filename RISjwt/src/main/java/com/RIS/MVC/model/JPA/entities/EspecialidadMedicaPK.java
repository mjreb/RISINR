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
public class EspecialidadMedicaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Especialidad_idEspecialidad")
    private int especialidadidEspecialidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Medico_NumEmpleado")
    private int medicoNumEmpleado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Medico_CURP")
    private String medicoCURP;

    public EspecialidadMedicaPK() {
    }

    public EspecialidadMedicaPK(int especialidadidEspecialidad, int medicoNumEmpleado, String medicoCURP) {
        this.especialidadidEspecialidad = especialidadidEspecialidad;
        this.medicoNumEmpleado = medicoNumEmpleado;
        this.medicoCURP = medicoCURP;
    }

    public int getEspecialidadidEspecialidad() {
        return especialidadidEspecialidad;
    }

    public void setEspecialidadidEspecialidad(int especialidadidEspecialidad) {
        this.especialidadidEspecialidad = especialidadidEspecialidad;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) especialidadidEspecialidad;
        hash += (int) medicoNumEmpleado;
        hash += (medicoCURP != null ? medicoCURP.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EspecialidadMedicaPK)) {
            return false;
        }
        EspecialidadMedicaPK other = (EspecialidadMedicaPK) object;
        if (this.especialidadidEspecialidad != other.especialidadidEspecialidad) {
            return false;
        }
        if (this.medicoNumEmpleado != other.medicoNumEmpleado) {
            return false;
        }
        if ((this.medicoCURP == null && other.medicoCURP != null) || (this.medicoCURP != null && !this.medicoCURP.equals(other.medicoCURP))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.EspecialidadMedicaPK[ especialidadidEspecialidad=" + especialidadidEspecialidad + ", medicoNumEmpleado=" + medicoNumEmpleado + ", medicoCURP=" + medicoCURP + " ]";
    }
    
}
