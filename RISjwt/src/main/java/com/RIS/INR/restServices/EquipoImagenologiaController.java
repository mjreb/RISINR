/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.RIS.INR.restServices;

import com.RIS.MVC.model.DAOSpecialization.EquipoImagenologiaManager;
import com.RIS.MVC.model.daoInterface.ServicesManager;
import javax.ws.rs.Path;



@Path("/EquipoImagenlogia")
public class EquipoImagenologiaController {
    
    private final ServicesManager servicesManager;  
    private final EquipoImagenologiaManager equipoManager;  

    public EquipoImagenologiaController() {
        this.servicesManager = new ServicesManager("AplicationContext.xml"); // Nueva instancia por sesión  
        this.equipoManager = (EquipoImagenologiaManager) servicesManager.getServicio("EquipoImagenologiaManager");  // Id por el servicio ya dado de alta en el aplicaction context
    }
    
    
    
    
    
    
    
}
