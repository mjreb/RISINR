package com.RIS.MVC.model.DAOSpecialization;

import com.RIS.MVC.model.CRUDrepository.GenericDAOFacade;
import com.RIS.MVC.model.JPA.entities.AreaDeServicio;
import com.RIS.MVC.model.JPA.entities.EquipoImagenologia;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import org.json.JSONArray;

/**
 *
 * @author DDT1
 */
public class EquipoImagenologiaManager extends GenericDAOFacade<EquipoImagenologia>{
    
    
    public EquipoImagenologiaManager() {
        super(EquipoImagenologia.class);
    }
    
    //public Collection<EquipoImagenologia> getAllEquipoImagenologia(){
    public JSONArray getAllEquipoImagenologia(){
        //ObjectMapper mapper = new ObjectMapper();
        TypedQuery<EquipoImagenologia> query;
        query = entityManager.createNamedQuery("EquipoImagenologia.findAll", EquipoImagenologia.class);//JPQL
        Collection<EquipoImagenologia> coleEqpIMG = query.getResultList();
        JSONArray jsonArray = new JSONArray();
        ObjectNode ON=null;
        for (EquipoImagenologia eqp : coleEqpIMG) {
            ON=new ObjectMapper().createObjectNode(); 
            ON.put("nSerie", eqp.getNSerie());
            ON.put("nombreEqp", eqp.getNombre());
            ON.put("marcaEqp", eqp.getMarca());
            ON.put("modeloEqp", eqp.getModelo());
            ON.put("modalidaEqp", eqp.getModalidad());
            ON.put("idArea", eqp.getAreaDeServicioidArea().getIdArea());
            ON.put("nomArea", eqp.getAreaDeServicioidArea().getNombre());
            ON.put("zEdo", eqp.getEstado());
            //ON.put("fInst", eqp.getFechaInstalacion().toString());
            Date  fecha=eqp.getFechaInstalacion();
            if(fecha ==null){
                ON.put("fInst", "");
            }else{
                String feform=toDateFormat(fecha,"yyyy-MM-dd");              
                ON.put("fInst",feform); //Ojo con los nulos.
            }
            jsonArray.put(ON);
        }        
        return jsonArray;          
    }
    
    private String toDateFormat(Date fecha,String formato){
       //DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
       DateFormat formatter = new SimpleDateFormat(formato);
       String fechaformato = formatter.format(fecha); 
       return  fechaformato;    
    }    
    
    public JSONArray createUpdateEquipoImagenologia(String nserie,String nombreeqp,String marcaeqp,String modeloeqp,String modalidaeqp,int idarea,String estadoeqp,String operacion){
      JSONArray jsonArray = new JSONArray();
      EquipoImagenologia eqpimg= new EquipoImagenologia();
      eqpimg.setNSerie(nserie);
      eqpimg.setNombre(nombreeqp);
      eqpimg.setMarca(marcaeqp);
      eqpimg.setModelo(modeloeqp);
      eqpimg.setModalidad(modalidaeqp);
      AreaDeServicio areaeqp= new AreaDeServicio();
      areaeqp.setIdArea(idarea);
      eqpimg.setAreaDeServicioidArea(areaeqp);
      eqpimg.setEstado(estadoeqp);
      EquipoImagenologia newisr=null;
      Date fechaatualSys= new Date(System.currentTimeMillis());
      switch(operacion){
          case "CreateEqp":
                eqpimg.setFechaInstalacion(fechaatualSys);
                newisr=this.save(eqpimg); //se almacena el registro              
              break;
          case "UpdateEqp":
                //agregar campo para fecha de actulización
                //eqpimg.setFechaCambio(fechaatualSys);
                newisr=this.edit(eqpimg); //se almacena el registro                            
              break;              
      }

      jsonArray.put(newisr);
      return jsonArray;  
    }
    
    /*
    public JSONObject deleteRegEqpPK(int nserie){
        //analizar dependencia por constraints en BD
        TypedQuery<EquipoImagenologia> query;
        query = entityManager.createNamedQuery("EquipoImagenologia.findByNSerie", EquipoImagenologia.class);//JPQL
        query.setParameter("nSerie", nserie);  
        JSONObject json = new JSONObject();
        return json;
    }*/    
    
    public static void accesoJPA() {
        //solo para base de datos
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PesitenceREST"); // se encuentra en el archivo persistence.xml
        EntityManager entityManager = emf.createEntityManager();
        EquipoImagenologiaManager EqIMGmanager = new EquipoImagenologiaManager();
        EqIMGmanager.setEntityManager(entityManager);
        //Collection<EquipoImagenologia> coleEqpIMG=EqIMGmanager.getAllEquipoImagenologia();
        JSONArray datoseqp=EqIMGmanager.getAllEquipoImagenologia();
        System.out.println(datoseqp);
        
        // for-each loop
        /*for (EquipoImagenologia eqp : coleEqpIMG) {
            System.out.println("Nombre: "+eqp.getNombre()+", Area: "+eqp.getAreaDeServicioidArea().getNombre());
        }*/
    }    
    
    public static void main(String args[]) {
        accesoJPA();

    }     
}
