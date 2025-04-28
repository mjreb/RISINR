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
@Table(name = "SolicitudDeEstudio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SolicitudDeEstudio.findAll", query = "SELECT s FROM SolicitudDeEstudio s"),
    @NamedQuery(name = "SolicitudDeEstudio.findByPacienteidPaciente", query = "SELECT s FROM SolicitudDeEstudio s WHERE s.solicitudDeEstudioPK.pacienteidPaciente = :pacienteidPaciente"),
    @NamedQuery(name = "SolicitudDeEstudio.findByMedicoNumEmpleado", query = "SELECT s FROM SolicitudDeEstudio s WHERE s.solicitudDeEstudioPK.medicoNumEmpleado = :medicoNumEmpleado"),
    @NamedQuery(name = "SolicitudDeEstudio.findByMedicoCURP", query = "SELECT s FROM SolicitudDeEstudio s WHERE s.solicitudDeEstudioPK.medicoCURP = :medicoCURP"),
    @NamedQuery(name = "SolicitudDeEstudio.findByFechaSolicitudPk", query = "SELECT s FROM SolicitudDeEstudio s WHERE s.solicitudDeEstudioPK.fechaSolicitudPk = :fechaSolicitudPk"),
    @NamedQuery(name = "SolicitudDeEstudio.findByFechaSolicitud", query = "SELECT s FROM SolicitudDeEstudio s WHERE s.fechaSolicitud = :fechaSolicitud"),
    @NamedQuery(name = "SolicitudDeEstudio.findByAreaProcedencia", query = "SELECT s FROM SolicitudDeEstudio s WHERE s.areaProcedencia = :areaProcedencia"),
    @NamedQuery(name = "SolicitudDeEstudio.findByFechaProximaCita", query = "SELECT s FROM SolicitudDeEstudio s WHERE s.fechaProximaCita = :fechaProximaCita"),
    @NamedQuery(name = "SolicitudDeEstudio.findByDiagnostico", query = "SELECT s FROM SolicitudDeEstudio s WHERE s.diagnostico = :diagnostico"),
    @NamedQuery(name = "SolicitudDeEstudio.findByObservaciones", query = "SELECT s FROM SolicitudDeEstudio s WHERE s.observaciones = :observaciones"),
    @NamedQuery(name = "SolicitudDeEstudio.findByEstado", query = "SELECT s FROM SolicitudDeEstudio s WHERE s.estado = :estado")})
public class SolicitudDeEstudio implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SolicitudDeEstudioPK solicitudDeEstudioPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FechaSolicitud")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSolicitud;
    @Size(max = 45)
    @Column(name = "AreaProcedencia")
    private String areaProcedencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FechaProximaCita")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaProximaCita;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "Diagnostico")
    private String diagnostico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "Observaciones")
    private String observaciones;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Estado")
    private String estado;
    @JoinColumns({
        @JoinColumn(name = "Medico_NumEmpleado", referencedColumnName = "NumEmpleado", insertable = false, updatable = false),
        @JoinColumn(name = "Medico_CURP", referencedColumnName = "CURP", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Medico medico;
    @JoinColumn(name = "Paciente_idPaciente", referencedColumnName = "idPaciente", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Paciente paciente;

    public SolicitudDeEstudio() {
    }

    public SolicitudDeEstudio(SolicitudDeEstudioPK solicitudDeEstudioPK) {
        this.solicitudDeEstudioPK = solicitudDeEstudioPK;
    }

    public SolicitudDeEstudio(SolicitudDeEstudioPK solicitudDeEstudioPK, Date fechaSolicitud, String areaProcedencia, Date fechaProximaCita, String diagnostico, String observaciones, String estado) {
        this.solicitudDeEstudioPK = solicitudDeEstudioPK;
        this.fechaSolicitud = fechaSolicitud;
        this.areaProcedencia = areaProcedencia;
        this.fechaProximaCita = fechaProximaCita;
        this.diagnostico = diagnostico;
        this.observaciones = observaciones;
        this.estado = estado;
    }    
    
    public SolicitudDeEstudio(SolicitudDeEstudioPK solicitudDeEstudioPK, Date fechaSolicitud, Date fechaProximaCita, String diagnostico, String observaciones, String estado) {
        this.solicitudDeEstudioPK = solicitudDeEstudioPK;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaProximaCita = fechaProximaCita;
        this.diagnostico = diagnostico;
        this.observaciones = observaciones;
        this.estado = estado;
    }

    public SolicitudDeEstudio(String pacienteidPaciente, int medicoNumEmpleado, String medicoCURP, long fechaSolicitudPk) {
        this.solicitudDeEstudioPK = new SolicitudDeEstudioPK(pacienteidPaciente, medicoNumEmpleado, medicoCURP, fechaSolicitudPk);
    }

    public SolicitudDeEstudioPK getSolicitudDeEstudioPK() {
        return solicitudDeEstudioPK;
    }

    public void setSolicitudDeEstudioPK(SolicitudDeEstudioPK solicitudDeEstudioPK) {
        this.solicitudDeEstudioPK = solicitudDeEstudioPK;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getAreaProcedencia() {
        return areaProcedencia;
    }

    public void setAreaProcedencia(String areaProcedencia) {
        this.areaProcedencia = areaProcedencia;
    }

    public Date getFechaProximaCita() {
        return fechaProximaCita;
    }

    public void setFechaProximaCita(Date fechaProximaCita) {
        this.fechaProximaCita = fechaProximaCita;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (solicitudDeEstudioPK != null ? solicitudDeEstudioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SolicitudDeEstudio)) {
            return false;
        }
        SolicitudDeEstudio other = (SolicitudDeEstudio) object;
        if ((this.solicitudDeEstudioPK == null && other.solicitudDeEstudioPK != null) || (this.solicitudDeEstudioPK != null && !this.solicitudDeEstudioPK.equals(other.solicitudDeEstudioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.SolicitudDeEstudio[ solicitudDeEstudioPK=" + solicitudDeEstudioPK + " ]";
    }
    
}
