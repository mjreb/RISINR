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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "ProtocoloEstudio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProtocoloEstudio.findAll", query = "SELECT p FROM ProtocoloEstudio p"),
    @NamedQuery(name = "ProtocoloEstudio.findByIdProtocolo", query = "SELECT p FROM ProtocoloEstudio p WHERE p.idProtocolo = :idProtocolo"),
    @NamedQuery(name = "ProtocoloEstudio.findByNombre", query = "SELECT p FROM ProtocoloEstudio p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "ProtocoloEstudio.findByDescripcion", query = "SELECT p FROM ProtocoloEstudio p WHERE p.descripcion = :descripcion")})
public class ProtocoloEstudio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idProtocolo")
    private Integer idProtocolo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 65)
    @Column(name = "Nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 65)
    @Column(name = "Descripcion")
    private String descripcion;
    @JoinColumn(name = "Estudio_idEstudio", referencedColumnName = "idEstudio")
    @ManyToOne(optional = false)
    private Estudio estudioidEstudio;

    public ProtocoloEstudio() {
    }

    public ProtocoloEstudio(Integer idProtocolo) {
        this.idProtocolo = idProtocolo;
    }

    public ProtocoloEstudio(Integer idProtocolo, String nombre, String descripcion) {
        this.idProtocolo = idProtocolo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getIdProtocolo() {
        return idProtocolo;
    }

    public void setIdProtocolo(Integer idProtocolo) {
        this.idProtocolo = idProtocolo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Estudio getEstudioidEstudio() {
        return estudioidEstudio;
    }

    public void setEstudioidEstudio(Estudio estudioidEstudio) {
        this.estudioidEstudio = estudioidEstudio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProtocolo != null ? idProtocolo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProtocoloEstudio)) {
            return false;
        }
        ProtocoloEstudio other = (ProtocoloEstudio) object;
        if ((this.idProtocolo == null && other.idProtocolo != null) || (this.idProtocolo != null && !this.idProtocolo.equals(other.idProtocolo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.RIS.MVC.model.JPA.entities.ProtocoloEstudio[ idProtocolo=" + idProtocolo + " ]";
    }
    
}
