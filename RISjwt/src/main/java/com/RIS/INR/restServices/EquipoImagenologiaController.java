/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.RIS.INR.restServices;

import com.RIS.MVC.model.DAOSpecialization.EquipoImagenologiaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EquipoImagenologiaController {
    
    @Autowired
    private EquipoImagenologiaManager EquipoIMG;
    
    
    @PostMapping("/SpringEquipoIMGEntity/{CRUD}")
    public ResponseEntity consultarTodos(){
        
        return ResponseEntity.ok("Hola");
        
    }
    
    
    
}
