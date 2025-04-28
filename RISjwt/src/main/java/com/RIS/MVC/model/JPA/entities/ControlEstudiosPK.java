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
public class ControlEstudiosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Usuario_NumEmpleado")
    private int usuarioNumEmpleado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Usuario_CURP")
    private String usuarioCURP;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Paciente_IDPaciente")
    private String pacienteIDPaciente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Estudio_idEstudio")
    private int estudioidEstudio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FechaControlPk")
    private long fechaControlPk;

    public ControlEstudiosPK() {
    }

    public ControlEstudiosPK(int usuarioNumEmpleado, String usuarioCURP, String pacienteIDPaciente, int estudioidEstudio, long fechaControlPk) {
        this.usuarioNumEmpleado = usuarioNumEmpleado;
        this.usuarioCURP = usuarioCURP;
        this.pacienteIDPaciente = pacienteIDPaciente;
        this.estudioidEstudio = estudioidEstudio;
        this.fechaControlPk = fechaControlPk;
    }

    public int getUsuarioNumEmpleado() {
        return usuarioNumEmpleado;
    }

    public void setUsuarioNumEmpleado(int usuarioNumEmpleado) {
        this.usuarioNumEmpleado = usuarioNumEmpleado;
    }

    public String getUsuarioCURP() {
        return usuarioCURP;
    }

    public void setUsuarioCURP(String usuarioCURP) {
        this.usuarioCURP = usuarioCURP;
    }

    public String getPacienteIDPaciente() {
        return pacienteIDPaciente;
    }

    public void setPacienteIDPaciente(String pacienteIDPaciente) {
        this.pacienteIDPaciente = pacienteIDPaciente;
    }

    public int getEstudioidEstudio() {
        return estudioidEstudio;
    }

    public void setEstudioidEstudio(int estudioidEstudio) {
        this.estudioidEstudio = estudioidEstudio;
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
        hash += (int) usuarioNumEmpleado;
        hash += (usuarioCURP != null ? usuarioCURP.hashCode() : 0);
        hash += (pacienteIDPaciente != null ? pacienteIDPaciente.hashCode() : 0);
        hash += (int) estudioidEstudio;
        hash += (int) fechaControlPk;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ControlEstudiosPK)) {
            return false;
        }
        ControlEstudiosPK other = (ControlEstudiosPK) object;
        if (this.usuarioNumEmpleado != other.usuarioNumEmpleado) {
            return false;
        }
        if ((this.usuarioCURP == null && other.usuarioCURP != null) || (this.usuarioCURP != null && !this.usuarioCURP.equals(other.usuarioCURP))) {
            return false;
        }
        if ((this.pacienteIDPaciente == null && other.pacienteIDPaciente != null) || (this.pacienteIDPaciente != null && !this.pacienteIDPaciente.equals(other.pacienteIDPaciente))) {
            return false;
        }
        if (this.estudioidEstudio != other.estudioidEstudio) {
            return false;
        }
        if (this.fechaControlPk != other.fechaControlPk) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.ControlEstudiosPK[ usuarioNumEmpleado=" + usuarioNumEmpleado + ", usuarioCURP=" + usuarioCURP + ", pacienteIDPaciente=" + pacienteIDPaciente + ", estudioidEstudio=" + estudioidEstudio + ", fechaControlPk=" + fechaControlPk + " ]";
    }
    
}
