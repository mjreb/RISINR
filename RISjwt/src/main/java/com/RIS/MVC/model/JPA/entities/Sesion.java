/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.RIS.MVC.model.JPA.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "Sesion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sesion.findAll", query = "SELECT s FROM Sesion s"),
    @NamedQuery(name = "Sesion.findByHoraInicio", query = "SELECT s FROM Sesion s WHERE s.sesionPK.horaInicio = :horaInicio"),
    @NamedQuery(name = "Sesion.findByUsuarioID", query = "SELECT s FROM Sesion s WHERE s.sesionPK.usuarioID = :usuarioID"),
    @NamedQuery(name = "Sesion.findByRolNombre", query = "SELECT s FROM Sesion s WHERE s.rolNombre = :rolNombre"),
    @NamedQuery(name = "Sesion.findByHoraFin", query = "SELECT s FROM Sesion s WHERE s.horaFin = :horaFin"),
    @NamedQuery(name = "Sesion.findByIPDispositivo", query = "SELECT s FROM Sesion s WHERE s.iPDispositivo = :iPDispositivo"),
    @NamedQuery(name = "Sesion.findByTipoCierre", query = "SELECT s FROM Sesion s WHERE s.tipoCierre = :tipoCierre")})
public class Sesion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SesionPK sesionPK;
    @Size(max = 30)
    @Column(name = "Rol_Nombre")
    private String rolNombre;
    @Column(name = "horaFin")
    private BigInteger horaFin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "IPDispositivo")
    private String iPDispositivo;
    @Size(max = 25)
    @Column(name = "TipoCierre")
    private String tipoCierre;

    public Sesion() {
    }

    public Sesion(SesionPK sesionPK) {
        this.sesionPK = sesionPK;
    }

    public Sesion(SesionPK sesionPK, String iPDispositivo) {
        this.sesionPK = sesionPK;
        this.iPDispositivo = iPDispositivo;
    }

    public Sesion(long horaInicio, String usuarioID) {
        this.sesionPK = new SesionPK(horaInicio, usuarioID);
    }
    public Sesion(long horaInicio, String usuarioID, int usuarioNumEmpleado, String usuarioCURP) {
        this.sesionPK = new SesionPK(horaInicio, usuarioID, usuarioNumEmpleado, usuarioCURP);
    }

    public SesionPK getSesionPK() {
        return sesionPK;
    }

    public void setSesionPK(SesionPK sesionPK) {
        this.sesionPK = sesionPK;
    }

    public String getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }

    public BigInteger getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(BigInteger horaFin) {
        this.horaFin = horaFin;
    }

    public String getIPDispositivo() {
        return iPDispositivo;
    }

    public void setIPDispositivo(String iPDispositivo) {
        this.iPDispositivo = iPDispositivo;
    }

    public String getTipoCierre() {
        return tipoCierre;
    }

    public void setTipoCierre(String tipoCierre) {
        this.tipoCierre = tipoCierre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sesionPK != null ? sesionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sesion)) {
            return false;
        }
        Sesion other = (Sesion) object;
        if ((this.sesionPK == null && other.sesionPK != null) || (this.sesionPK != null && !this.sesionPK.equals(other.sesionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.Sesion[ sesionPK=" + sesionPK + " ]";
    }
    
}
