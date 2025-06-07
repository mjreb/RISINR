package com.RIS.MVC.model.DAOSpecialization;

import com.RIS.MVC.model.CRUDrepository.EquipoImagenologiaRepository;
import com.RIS.MVC.model.CRUDrepository.GenericDAOFacade;
import com.RIS.MVC.model.JPA.entities.AreaDeServicio;
import com.RIS.MVC.model.JPA.entities.EquipoImagenologia;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import org.json.JSONArray;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class EquipoImagenologiaManager{
    
    @Autowired
    private EquipoImagenologiaRepository repository; 
    
   
    // ----------------- Nuevo -------------------------------
    
    public ArrayNode consultarEquipos(){
    
        List<EquipoImagenologia> equipos = repository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray = mapper.createArrayNode();
       
        for (EquipoImagenologia eqp : equipos) {
            ObjectNode obj = mapper.createObjectNode(); 
            obj.put("nSerie", eqp.getNSerie());
            obj.put("nombreEqp", eqp.getNombre());
            obj.put("marcaEqp", eqp.getMarca());
            obj.put("modeloEqp", eqp.getModelo());
            obj.put("modalidaEqp", eqp.getModalidad());
            obj.put("idArea", eqp.getAreaDeServicioidArea().getIdArea());
            obj.put("nomArea", eqp.getAreaDeServicioidArea().getNombre());
            obj.put("zEdo", eqp.getEstado());
            //ON.put("fInst", eqp.getFechaInstalacion().toString());
            Date  fecha=eqp.getFechaInstalacion();
            if(fecha ==null){
                obj.put("fInst", "");
            }else{
                String feform=toDateFormat(fecha,"yyyy-MM-dd");              
                obj.put("fInst",feform); //Ojo con los nulos.
            }
            jsonArray.add(obj);
        }        
        
        return jsonArray;
        
    }
    
       private String toDateFormat(Date fecha,String formato){
       //DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
       DateFormat formatter = new SimpleDateFormat(formato);
       String fechaformato = formatter.format(fecha); 
       return  fechaformato;    
    }
    
    
    
    
}
