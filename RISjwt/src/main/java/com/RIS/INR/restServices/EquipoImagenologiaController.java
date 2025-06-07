/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.RIS.INR.restServices;

import com.RIS.MVC.model.DAOSpecialization.EquipoImagenologiaManager;
import com.fasterxml.jackson.databind.node.ArrayNode;
import static java.lang.System.console;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/SpringEquipoImagenologia")
public class EquipoImagenologiaController {
    
    @Autowired
    private EquipoImagenologiaManager EquipoIMG;
    
    
    @PostMapping("/requestALL")
    public ResponseEntity<ArrayNode> consultarTodos(@RequestBody Map<String, String> body){
        String nombre = body.get("nombre");
        ArrayNode equipos = null; 
        
        if ("*".equals(nombre)) {
            equipos = EquipoIMG.consultarEquipos();
        } else {
            
            System.out.println("Busqueda no válida");
        }

        return ResponseEntity.ok(equipos);
        
    }
    
 
    
}
