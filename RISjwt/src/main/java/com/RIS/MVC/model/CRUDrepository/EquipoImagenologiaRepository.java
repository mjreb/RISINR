/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.RIS.MVC.model.CRUDrepository;

import com.RIS.MVC.model.JPA.entities.EquipoImagenologia;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface EquipoImagenologiaRepository extends JpaRepository<EquipoImagenologia, String> {
    
    EquipoImagenologia findByNombre(String Nombre);
    List<EquipoImagenologia> findByMarca(String marca);
    List<EquipoImagenologia> findByModelo(String modelo);
    List<EquipoImagenologia> findByModalidad(String modalidad);
    List<EquipoImagenologia> findByFechaInstalacion(Date fechaInstalacion); 
    List<EquipoImagenologia> findByEstado(String estado);
}
