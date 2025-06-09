/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.RIS.INR.restServices;

import com.RIS.MVC.model.DAOSpecialization.EquipoImagenologiaManager;
import com.RIS.MVC.model.daoInterface.ServicesManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.json.JSONArray;

/**
 *
 * @author vsfs2
 */
@Path("/EquipoImagenologia")
public class EquipoImagenologiaController {
    
 private final ServicesManager servicesManager;  
    private final EquipoImagenologiaManager equipoManager;  

    
    public EquipoImagenologiaController() {
        this.servicesManager = new ServicesManager("AplicationContext.xml"); // Nueva instancia por sesión  
        this.equipoManager = (EquipoImagenologiaManager) servicesManager.getServicio("EquipoImagenologiaManager");  // Id por el servicio ya dado de alta en el aplicaction context
    }

    
    @POST
    @Path("/EquipoIMGEntity/{CRUD}")
    @Produces(MediaType.APPLICATION_JSON)
    //@Consumes(MediaType.APPLICATION_JSON) // Lo que recibe 
    public Response manageEquipoIMG(@PathParam("CRUD") String operacion, MultivaluedMap<String, String> formParams) { // 
        try{
            
            if (operacion == null || formParams == null) {
            return Response.status(400)     // HTTP 400 si hay datos inválidos
                .entity("{\"error\": \"Parámetros inválidos\"}").build();
            }
            
            if (equipoManager == null) {
            System.out.println("equipoManager es NULL");
            return Response.status(500).entity("{\"error\":\"equipoManager no está inyectado\"}").build();
            } else {
                System.out.println("equipoManager inyectado correctamente");
            }
            
            ObjectMapper mapper = new ObjectMapper();
            String nombre=formParams.getFirst("nombre");
            JSONArray jsonArray = null;
            
            System.out.println("LLEGA AL EQUIPO IMAGENOLOGIA");
            switch (operacion) {
                case "ReadAll": 
                    System.out.println("LLEGO ANTES E LA PERSISTENCIA");
                   try{
                       jsonArray = equipoManager.getAllEquipoImagenologia(); 
                       
                   }catch(Exception e){
                       e.printStackTrace(); // Esto te imprimirá la excepción en el servidor
    return Response.status(500).entity("{\"error\":\"Excepción al consultar equipos: " + e.getMessage() + "\"}").build();
}
                      
                     System.out.println("REGRESO AL CONTROLADOR");
                     System.out.println(jsonArray);
                break;

            }
            
            return Response.status(Response.Status.OK).entity(jsonArray.toString()).build();
        
        }catch(Exception e){
            return Response.status(500)         // HTTP 500 para errores inesperados
            .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
            
        }
    } 
}
