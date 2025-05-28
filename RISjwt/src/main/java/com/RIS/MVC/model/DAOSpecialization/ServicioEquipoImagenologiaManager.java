/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.RIS.MVC.model.DAOSpecialization;

import com.RIS.MVC.model.daoInterface.ServicesManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.util.concurrent.ServiceManager;
import javax.persistence.EntityManager;
import javax.ws.rs.core.MultivaluedMap;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.json.JSONArray;




public class ServicioEquipoImagenologiaManager {
    
    EquipoImagenologiaManager EqIMGmanager;
    ServicesManager sm;

    public ServicioEquipoImagenologiaManager(ServicesManager sm) {
        this.sm = sm;
    }
   
    public ArrayNode consultarCatalogo(String operacion, MultivaluedMap<String, String> formParams){
        // PathParam es la es la ruta dinámica (operación) y 
        // MultivalueMap son los datos que voy a enviar (los que encripté)
        ObjectMapper mapper = new ObjectMapper(); // Esta clase permite leer y escribir objetos JSON
        ArrayNode jsonArray = mapper.createArrayNode();  // Arrays mapeados desde un JSON 
        //EqIMGmanager = new EquipoImagenologiaManager(); // Manejador del equipo de imagenología 
        EqIMGmanager.setEntityManager(sm.getEntityManager()); // Es el núcleo de cualquier operación de persistencia en aplicaciones Java que usan JPA/Hibernate.
        JSONArray datoseqp=null;// Aquí se va a almacenar un arreglo de JSON 
        switch (operacion) { // Evalúa la parresolvte dinpamica de la ruta 
            case "ReadAll": 
                String nombre=formParams.getFirst("nombre"); // Obtiene el contenido de la primera clave que aparezca como nombre 
                datoseqp=EqIMGmanager.getAllEquipoImagenologia();  
                System.out.println(datoseqp);
                jsonArray.addPOJO(datoseqp); // JSON de la librería Jackson
                /* .adddPOJO toma un objeto Java y lo convierte automáticamente a su representación JSON
                    Agrega el resultado convertido como nuevo elemento del ArrayNode
                    Java Object → JSONArray → ArrayNode → String JSON 
                */
            break;
        } 
        
        // Aquí es donde puedo encriptar **
        return jsonArray; 
        
        
    
    }
    
    public ArrayNode crearYeditar(FormDataMultiPart formParams){
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray = mapper.createArrayNode();
        EqIMGmanager = new EquipoImagenologiaManager();
        EqIMGmanager.setEntityManager(sm.getEntityManager());         
        JSONArray datoseqp=null; /*new JSONArray();*/
        //leer datos de la forma
        String oper=formParams.getField("Operation").getValue(); //definido en la forma
        System.out.println("**Operacion: "+oper);
        String nserie=formParams.getField("nserEQP").getValue();
        String nombreeqp=formParams.getField("nomEQP").getValue();
        String marcaeqp=formParams.getField("marcaEQP").getValue();
        String modeloeqp=formParams.getField("modeloEQP").getValue();
        String modalidaeqp=formParams.getField("modalEqp").getValue();
        String idarea=formParams.getField("areEqp").getValue();
        String estadoeqp=formParams.getField("edoEqp").getValue();  
        datoseqp=EqIMGmanager.createUpdateEquipoImagenologia(nserie,nombreeqp,marcaeqp,modeloeqp,modalidaeqp,Integer.parseInt(idarea),estadoeqp,oper);  
        
        if(datoseqp!=null){
         jsonArray.add("1");
        }else{
         jsonArray.add("0");
        }  
        
         return jsonArray; 
        
    }
    
   
        
    
    
}
