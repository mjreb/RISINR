package com.RIS.MVC.model.JPA.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DDT1
 */
@Entity
@Table(name = "Medico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Medico.findAll", query = "SELECT m FROM Medico m"),
    @NamedQuery(name = "Medico.findByNumEmpleado", query = "SELECT m FROM Medico m WHERE m.medicoPK.numEmpleado = :numEmpleado"),
    @NamedQuery(name = "Medico.findByCurp", query = "SELECT m FROM Medico m WHERE m.medicoPK.curp = :curp")})
public class Medico implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MedicoPK medicoPK;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medico")
    private Collection<SolicitudDeEstudio> solicitudDeEstudioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medico")
    private Collection<EspecialidadMedica> especialidadMedicaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medico")
    private Collection<AgendaDeServicio> agendaDeServicioCollection;

    public Medico() {
    }

    public Medico(MedicoPK medicoPK) {
        this.medicoPK = medicoPK;
    }

    public Medico(int numEmpleado, String curp) {
        this.medicoPK = new MedicoPK(numEmpleado, curp);
    }

    public MedicoPK getMedicoPK() {
        return medicoPK;
    }

    public void setMedicoPK(MedicoPK medicoPK) {
        this.medicoPK = medicoPK;
    }

    @XmlTransient
    public Collection<SolicitudDeEstudio> getSolicitudDeEstudioCollection() {
        return solicitudDeEstudioCollection;
    }

    public void setSolicitudDeEstudioCollection(Collection<SolicitudDeEstudio> solicitudDeEstudioCollection) {
        this.solicitudDeEstudioCollection = solicitudDeEstudioCollection;
    }

    @XmlTransient
    public Collection<EspecialidadMedica> getEspecialidadMedicaCollection() {
        return especialidadMedicaCollection;
    }

    public void setEspecialidadMedicaCollection(Collection<EspecialidadMedica> especialidadMedicaCollection) {
        this.especialidadMedicaCollection = especialidadMedicaCollection;
    }

    @XmlTransient
    public Collection<AgendaDeServicio> getAgendaDeServicioCollection() {
        return agendaDeServicioCollection;
    }

    public void setAgendaDeServicioCollection(Collection<AgendaDeServicio> agendaDeServicioCollection) {
        this.agendaDeServicioCollection = agendaDeServicioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (medicoPK != null ? medicoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medico)) {
            return false;
        }
        Medico other = (Medico) object;
        if ((this.medicoPK == null && other.medicoPK != null) || (this.medicoPK != null && !this.medicoPK.equals(other.medicoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.Medico[ medicoPK=" + medicoPK + " ]";
    }
    
}
