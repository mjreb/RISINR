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
public class PerfilPK implements Serializable {

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
    @Column(name = "Rol_idRol")
    private int rolidRol;

    public PerfilPK() {
    }

    public PerfilPK(int usuarioNumEmpleado, String usuarioCURP, int rolidRol) {
        this.usuarioNumEmpleado = usuarioNumEmpleado;
        this.usuarioCURP = usuarioCURP;
        this.rolidRol = rolidRol;
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

    public int getRolidRol() {
        return rolidRol;
    }

    public void setRolidRol(int rolidRol) {
        this.rolidRol = rolidRol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) usuarioNumEmpleado;
        hash += (usuarioCURP != null ? usuarioCURP.hashCode() : 0);
        hash += (int) rolidRol;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PerfilPK)) {
            return false;
        }
        PerfilPK other = (PerfilPK) object;
        if (this.usuarioNumEmpleado != other.usuarioNumEmpleado) {
            return false;
        }
        if ((this.usuarioCURP == null && other.usuarioCURP != null) || (this.usuarioCURP != null && !this.usuarioCURP.equals(other.usuarioCURP))) {
            return false;
        }
        if (this.rolidRol != other.rolidRol) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.PerfilPK[ usuarioNumEmpleado=" + usuarioNumEmpleado + ", usuarioCURP=" + usuarioCURP + ", rolidRol=" + rolidRol + " ]";
    }
    
}
