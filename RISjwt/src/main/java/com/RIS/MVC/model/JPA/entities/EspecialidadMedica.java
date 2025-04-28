/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.RIS.MVC.model.JPA.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DDT1
 */
@Entity
@Table(name = "Especialidad_Medica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EspecialidadMedica.findAll", query = "SELECT e FROM EspecialidadMedica e"),
    @NamedQuery(name = "EspecialidadMedica.findByEspecialidadidEspecialidad", query = "SELECT e FROM EspecialidadMedica e WHERE e.especialidadMedicaPK.especialidadidEspecialidad = :especialidadidEspecialidad"),
    @NamedQuery(name = "EspecialidadMedica.findByMedicoNumEmpleado", query = "SELECT e FROM EspecialidadMedica e WHERE e.especialidadMedicaPK.medicoNumEmpleado = :medicoNumEmpleado"),
    @NamedQuery(name = "EspecialidadMedica.findByMedicoCURP", query = "SELECT e FROM EspecialidadMedica e WHERE e.especialidadMedicaPK.medicoCURP = :medicoCURP"),
    @NamedQuery(name = "EspecialidadMedica.findByCedulaProfesional", query = "SELECT e FROM EspecialidadMedica e WHERE e.cedulaProfesional = :cedulaProfesional")})
public class EspecialidadMedica implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EspecialidadMedicaPK especialidadMedicaPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "CedulaProfesional")
    private String cedulaProfesional;
    @JoinColumn(name = "Especialidad_idEspecialidad", referencedColumnName = "idEspecialidad", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Especialidad especialidad;
    @JoinColumns({
        @JoinColumn(name = "Medico_NumEmpleado", referencedColumnName = "NumEmpleado", insertable = false, updatable = false),
        @JoinColumn(name = "Medico_CURP", referencedColumnName = "CURP", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Medico medico;

    public EspecialidadMedica() {
    }

    public EspecialidadMedica(EspecialidadMedicaPK especialidadMedicaPK) {
        this.especialidadMedicaPK = especialidadMedicaPK;
    }

    public EspecialidadMedica(EspecialidadMedicaPK especialidadMedicaPK, String cedulaProfesional) {
        this.especialidadMedicaPK = especialidadMedicaPK;
        this.cedulaProfesional = cedulaProfesional;
    }

    public EspecialidadMedica(int especialidadidEspecialidad, int medicoNumEmpleado, String medicoCURP) {
        this.especialidadMedicaPK = new EspecialidadMedicaPK(especialidadidEspecialidad, medicoNumEmpleado, medicoCURP);
    }

    public EspecialidadMedicaPK getEspecialidadMedicaPK() {
        return especialidadMedicaPK;
    }

    public void setEspecialidadMedicaPK(EspecialidadMedicaPK especialidadMedicaPK) {
        this.especialidadMedicaPK = especialidadMedicaPK;
    }

    public String getCedulaProfesional() {
        return cedulaProfesional;
    }

    public void setCedulaProfesional(String cedulaProfesional) {
        this.cedulaProfesional = cedulaProfesional;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (especialidadMedicaPK != null ? especialidadMedicaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EspecialidadMedica)) {
            return false;
        }
        EspecialidadMedica other = (EspecialidadMedica) object;
        if ((this.especialidadMedicaPK == null && other.especialidadMedicaPK != null) || (this.especialidadMedicaPK != null && !this.especialidadMedicaPK.equals(other.especialidadMedicaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.EspecialidadMedica[ especialidadMedicaPK=" + especialidadMedicaPK + " ]";
    }
    
}
