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
public class SesionPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "horaInicio")
    private long horaInicio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Usuario_ID")
    private String usuarioID;
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Usuario_CURP")
    private String usuarioCURP;
    @NotNull
    @Column(name = "Usuario_NumEmpleado")
    private int usuarioNumEmpleado;
    @NotNull
    @Column(name = "Aplicacion_idAplicacion")
    private int aplicacionID;
    

    public SesionPK() {
    }

    public SesionPK(long horaInicio, String usuarioID) {
        this.horaInicio = horaInicio;
        this.usuarioID = usuarioID;
    }
    
    public SesionPK(long horaInicio, String usuarioID, int usuarioNumEmpleado, String usuarioCURP) {
        this.horaInicio = horaInicio;
        this.usuarioID = usuarioID;
        this.usuarioNumEmpleado = usuarioNumEmpleado;
        this.usuarioCURP = usuarioCURP;
    }

    public long getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(long horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(String usuarioID) {
        this.usuarioID = usuarioID;
    }

    public int getusuarioNumEmpleado() {
        return usuarioNumEmpleado;
    }
    
    public void setusuarioNumEmpleado(int usuarioNumEmpleado) {
        this.usuarioNumEmpleado = usuarioNumEmpleado;
    }
    public int getaplicacionID() {
        return aplicacionID;
    }
    
    public void setaplicacionID(int aplicacionID) {
        this.aplicacionID = aplicacionID;
        
    }
    
    public String getusuarioCURP() {
        return usuarioCURP;
    }
    
    public void setusuarioCURP(String usuarioCURP) {
        this.usuarioCURP = usuarioCURP;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) horaInicio;
        hash += (usuarioID != null ? usuarioID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SesionPK)) {
            return false;
        }
        SesionPK other = (SesionPK) object;
        if (this.horaInicio != other.horaInicio) {
            return false;
        }
        if ((this.usuarioID == null && other.usuarioID != null) || (this.usuarioID != null && !this.usuarioID.equals(other.usuarioID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.SesionPK[ horaInicio=" + horaInicio + ", usuarioID=" + usuarioID + " ]";
    }
    
}
