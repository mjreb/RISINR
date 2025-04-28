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
@Table(name = "Domicilio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Domicilio.findAll", query = "SELECT d FROM Domicilio d"),
    @NamedQuery(name = "Domicilio.findByPacienteIDPaciente", query = "SELECT d FROM Domicilio d WHERE d.pacienteIDPaciente = :pacienteIDPaciente"),
    @NamedQuery(name = "Domicilio.findByCalle", query = "SELECT d FROM Domicilio d WHERE d.calle = :calle"),
    @NamedQuery(name = "Domicilio.findByNumero", query = "SELECT d FROM Domicilio d WHERE d.numero = :numero"),
    @NamedQuery(name = "Domicilio.findByColonia", query = "SELECT d FROM Domicilio d WHERE d.colonia = :colonia"),
    @NamedQuery(name = "Domicilio.findByAlcaldiaMunicipio", query = "SELECT d FROM Domicilio d WHERE d.alcaldiaMunicipio = :alcaldiaMunicipio"),
    @NamedQuery(name = "Domicilio.findByEstado", query = "SELECT d FROM Domicilio d WHERE d.estado = :estado"),
    @NamedQuery(name = "Domicilio.findByCp", query = "SELECT d FROM Domicilio d WHERE d.cp = :cp"),
    @NamedQuery(name = "Domicilio.findByTiempoDeTraslado", query = "SELECT d FROM Domicilio d WHERE d.tiempoDeTraslado = :tiempoDeTraslado"),
    @NamedQuery(name = "Domicilio.findByTelefono", query = "SELECT d FROM Domicilio d WHERE d.telefono = :telefono")})
public class Domicilio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Paciente_IDPaciente")
    private String pacienteIDPaciente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Calle")
    private String calle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Numero")
    private String numero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Colonia")
    private String colonia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "AlcaldiaMunicipio")
    private String alcaldiaMunicipio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "Estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "CP")
    private String cp;
    @Column(name = "TiempoDeTraslado")
    private Integer tiempoDeTraslado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Telefono")
    private String telefono;
    @JoinColumn(name = "Paciente_IDPaciente", referencedColumnName = "idPaciente", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Paciente paciente;

    public Domicilio() {
    }

    public Domicilio(String pacienteIDPaciente) {
        this.pacienteIDPaciente = pacienteIDPaciente;
    }

    public Domicilio(String pacienteIDPaciente, String calle, String numero, String colonia, String alcaldiaMunicipio, String estado, String cp, String telefono) {
        this.pacienteIDPaciente = pacienteIDPaciente;
        this.calle = calle;
        this.numero = numero;
        this.colonia = colonia;
        this.alcaldiaMunicipio = alcaldiaMunicipio;
        this.estado = estado;
        this.cp = cp;
        this.telefono = telefono;
    }

    public String getPacienteIDPaciente() {
        return pacienteIDPaciente;
    }

    public void setPacienteIDPaciente(String pacienteIDPaciente) {
        this.pacienteIDPaciente = pacienteIDPaciente;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getAlcaldiaMunicipio() {
        return alcaldiaMunicipio;
    }

    public void setAlcaldiaMunicipio(String alcaldiaMunicipio) {
        this.alcaldiaMunicipio = alcaldiaMunicipio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public Integer getTiempoDeTraslado() {
        return tiempoDeTraslado;
    }

    public void setTiempoDeTraslado(Integer tiempoDeTraslado) {
        this.tiempoDeTraslado = tiempoDeTraslado;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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
        hash += (pacienteIDPaciente != null ? pacienteIDPaciente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Domicilio)) {
            return false;
        }
        Domicilio other = (Domicilio) object;
        if ((this.pacienteIDPaciente == null && other.pacienteIDPaciente != null) || (this.pacienteIDPaciente != null && !this.pacienteIDPaciente.equals(other.pacienteIDPaciente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.Domicilio[ pacienteIDPaciente=" + pacienteIDPaciente + " ]";
    }
    
}
