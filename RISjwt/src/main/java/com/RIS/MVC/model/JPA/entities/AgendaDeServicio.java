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
@Table(name = "AgendaDeServicio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AgendaDeServicio.findAll", query = "SELECT a FROM AgendaDeServicio a"),
    @NamedQuery(name = "AgendaDeServicio.findByEquipoImagenologiaNSerie", query = "SELECT a FROM AgendaDeServicio a WHERE a.agendaDeServicioPK.equipoImagenologiaNSerie = :equipoImagenologiaNSerie"),
    @NamedQuery(name = "AgendaDeServicio.findByMedicoNumEmpleado", query = "SELECT a FROM AgendaDeServicio a WHERE a.agendaDeServicioPK.medicoNumEmpleado = :medicoNumEmpleado"),
    @NamedQuery(name = "AgendaDeServicio.findByMedicoCURP", query = "SELECT a FROM AgendaDeServicio a WHERE a.agendaDeServicioPK.medicoCURP = :medicoCURP"),
    @NamedQuery(name = "AgendaDeServicio.findByFechaControlPk", query = "SELECT a FROM AgendaDeServicio a WHERE a.agendaDeServicioPK.fechaControlPk = :fechaControlPk"),
    @NamedQuery(name = "AgendaDeServicio.findByFechaControl", query = "SELECT a FROM AgendaDeServicio a WHERE a.fechaControl = :fechaControl"),
    @NamedQuery(name = "AgendaDeServicio.findByTipoMantenimiento", query = "SELECT a FROM AgendaDeServicio a WHERE a.tipoMantenimiento = :tipoMantenimiento"),
    @NamedQuery(name = "AgendaDeServicio.findByEstadoDeManto", query = "SELECT a FROM AgendaDeServicio a WHERE a.estadoDeManto = :estadoDeManto"),
    @NamedQuery(name = "AgendaDeServicio.findByDescrpcion", query = "SELECT a FROM AgendaDeServicio a WHERE a.descrpcion = :descrpcion")})
public class AgendaDeServicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AgendaDeServicioPK agendaDeServicioPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FechaControl")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaControl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "TipoMantenimiento")
    private String tipoMantenimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "EstadoDeManto")
    private String estadoDeManto;
    @Size(max = 100)
    @Column(name = "Descrpcion")
    private String descrpcion;
    @JoinColumn(name = "EquipoImagenologia_NSerie", referencedColumnName = "NSerie", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EquipoImagenologia equipoImagenologia;
    @JoinColumns({
        @JoinColumn(name = "Medico_NumEmpleado", referencedColumnName = "NumEmpleado", insertable = false, updatable = false),
        @JoinColumn(name = "Medico_CURP", referencedColumnName = "CURP", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Medico medico;

    public AgendaDeServicio() {
    }

    public AgendaDeServicio(AgendaDeServicioPK agendaDeServicioPK) {
        this.agendaDeServicioPK = agendaDeServicioPK;
    }

    public AgendaDeServicio(AgendaDeServicioPK agendaDeServicioPK, Date fechaControl, String tipoMantenimiento, String estadoDeManto) {
        this.agendaDeServicioPK = agendaDeServicioPK;
        this.fechaControl = fechaControl;
        this.tipoMantenimiento = tipoMantenimiento;
        this.estadoDeManto = estadoDeManto;
    }

    public AgendaDeServicio(String equipoImagenologiaNSerie, int medicoNumEmpleado, String medicoCURP, long fechaControlPk) {
        this.agendaDeServicioPK = new AgendaDeServicioPK(equipoImagenologiaNSerie, medicoNumEmpleado, medicoCURP, fechaControlPk);
    }

    public AgendaDeServicioPK getAgendaDeServicioPK() {
        return agendaDeServicioPK;
    }

    public void setAgendaDeServicioPK(AgendaDeServicioPK agendaDeServicioPK) {
        this.agendaDeServicioPK = agendaDeServicioPK;
    }

    public Date getFechaControl() {
        return fechaControl;
    }

    public void setFechaControl(Date fechaControl) {
        this.fechaControl = fechaControl;
    }

    public String getTipoMantenimiento() {
        return tipoMantenimiento;
    }

    public void setTipoMantenimiento(String tipoMantenimiento) {
        this.tipoMantenimiento = tipoMantenimiento;
    }

    public String getEstadoDeManto() {
        return estadoDeManto;
    }

    public void setEstadoDeManto(String estadoDeManto) {
        this.estadoDeManto = estadoDeManto;
    }

    public String getDescrpcion() {
        return descrpcion;
    }

    public void setDescrpcion(String descrpcion) {
        this.descrpcion = descrpcion;
    }

    public EquipoImagenologia getEquipoImagenologia() {
        return equipoImagenologia;
    }

    public void setEquipoImagenologia(EquipoImagenologia equipoImagenologia) {
        this.equipoImagenologia = equipoImagenologia;
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
        hash += (agendaDeServicioPK != null ? agendaDeServicioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AgendaDeServicio)) {
            return false;
        }
        AgendaDeServicio other = (AgendaDeServicio) object;
        if ((this.agendaDeServicioPK == null && other.agendaDeServicioPK != null) || (this.agendaDeServicioPK != null && !this.agendaDeServicioPK.equals(other.agendaDeServicioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.AgendaDeServicio[ agendaDeServicioPK=" + agendaDeServicioPK + " ]";
    }
    
}
