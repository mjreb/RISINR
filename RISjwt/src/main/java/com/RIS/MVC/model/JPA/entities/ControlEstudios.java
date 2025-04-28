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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DDT1
 */
@Entity
@Table(name = "ControlEstudios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ControlEstudios.findAll", query = "SELECT c FROM ControlEstudios c"),
    @NamedQuery(name = "ControlEstudios.findByUsuarioNumEmpleado", query = "SELECT c FROM ControlEstudios c WHERE c.controlEstudiosPK.usuarioNumEmpleado = :usuarioNumEmpleado"),
    @NamedQuery(name = "ControlEstudios.findByUsuarioCURP", query = "SELECT c FROM ControlEstudios c WHERE c.controlEstudiosPK.usuarioCURP = :usuarioCURP"),
    @NamedQuery(name = "ControlEstudios.findByPacienteIDPaciente", query = "SELECT c FROM ControlEstudios c WHERE c.controlEstudiosPK.pacienteIDPaciente = :pacienteIDPaciente"),
    @NamedQuery(name = "ControlEstudios.findByEstudioidEstudio", query = "SELECT c FROM ControlEstudios c WHERE c.controlEstudiosPK.estudioidEstudio = :estudioidEstudio"),
    @NamedQuery(name = "ControlEstudios.findByFechaControlPk", query = "SELECT c FROM ControlEstudios c WHERE c.controlEstudiosPK.fechaControlPk = :fechaControlPk"),
    @NamedQuery(name = "ControlEstudios.findByFechaControl", query = "SELECT c FROM ControlEstudios c WHERE c.fechaControl = :fechaControl"),
    @NamedQuery(name = "ControlEstudios.findByEstado", query = "SELECT c FROM ControlEstudios c WHERE c.estado = :estado"),
    @NamedQuery(name = "ControlEstudios.findByCerrado", query = "SELECT c FROM ControlEstudios c WHERE c.cerrado = :cerrado"),
    @NamedQuery(name = "ControlEstudios.findByObservaciones", query = "SELECT c FROM ControlEstudios c WHERE c.observaciones = :observaciones")})
public class ControlEstudios implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ControlEstudiosPK controlEstudiosPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FechaControl")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaControl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Cerrado")
    private boolean cerrado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "Observaciones")
    private String observaciones;
    @JoinColumn(name = "Estudio_idEstudio", referencedColumnName = "idEstudio", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Estudio estudio;
    @JoinColumn(name = "Paciente_IDPaciente", referencedColumnName = "idPaciente", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Paciente paciente;
    @JoinColumns({
        @JoinColumn(name = "Usuario_NumEmpleado", referencedColumnName = "NumEmpleado", insertable = false, updatable = false),
        @JoinColumn(name = "Usuario_CURP", referencedColumnName = "CURP", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Usuario usuario;

    public ControlEstudios() {
    }

    public ControlEstudios(ControlEstudiosPK controlEstudiosPK) {
        this.controlEstudiosPK = controlEstudiosPK;
    }

    public ControlEstudios(ControlEstudiosPK controlEstudiosPK, Date fechaControl, String estado, boolean cerrado, String observaciones) {
        this.controlEstudiosPK = controlEstudiosPK;
        this.fechaControl = fechaControl;
        this.estado = estado;
        this.cerrado = cerrado;
        this.observaciones = observaciones;
    }

    public ControlEstudios(int usuarioNumEmpleado, String usuarioCURP, String pacienteIDPaciente, int estudioidEstudio, long fechaControlPk) {
        this.controlEstudiosPK = new ControlEstudiosPK(usuarioNumEmpleado, usuarioCURP, pacienteIDPaciente, estudioidEstudio, fechaControlPk);
    }

    public ControlEstudiosPK getControlEstudiosPK() {
        return controlEstudiosPK;
    }

    public void setControlEstudiosPK(ControlEstudiosPK controlEstudiosPK) {
        this.controlEstudiosPK = controlEstudiosPK;
    }

    public Date getFechaControl() {
        return fechaControl;
    }

    public void setFechaControl(Date fechaControl) {
        this.fechaControl = fechaControl;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean getCerrado() {
        return cerrado;
    }

    public void setCerrado(boolean cerrado) {
        this.cerrado = cerrado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Estudio getEstudio() {
        return estudio;
    }

    public void setEstudio(Estudio estudio) {
        this.estudio = estudio;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (controlEstudiosPK != null ? controlEstudiosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ControlEstudios)) {
            return false;
        }
        ControlEstudios other = (ControlEstudios) object;
        if ((this.controlEstudiosPK == null && other.controlEstudiosPK != null) || (this.controlEstudiosPK != null && !this.controlEstudiosPK.equals(other.controlEstudiosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.ControlEstudios[ controlEstudiosPK=" + controlEstudiosPK + " ]";
    }
    
}
