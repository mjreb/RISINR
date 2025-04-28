package com.RIS.MVC.model.DAOSpecialization;

import com.RIS.MVC.model.CRUDrepository.GenericDAOFacade;
import com.RIS.MVC.model.JPA.entities.AreaDeServicio;
import com.RIS.MVC.model.JPA.entities.Equipo;
import com.RIS.MVC.model.JPA.entities.Estudio;
import com.RIS.MVC.model.JPA.entities.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.json.JSONArray;

/**
 *
 * @author DVGMetatools
 */
public class AreaManager extends GenericDAOFacade<AreaDeServicio>{
    
    
    public AreaManager() {
        super(AreaDeServicio.class);
    }

    public Collection<AreaDeServicio> getAllDataArea() {
        TypedQuery<AreaDeServicio> query;
        query = entityManager.createNamedQuery("AreaDeServicio.findAll", AreaDeServicio.class);//JPQL
        return query.getResultList();
    }
    
    //Consulta compuesta via jpa. ++DVG
    //public AreaDeServicio getAllDataAreabyId(int idarea) {
    public JSONArray getAllDataAreabyId(int idarea) {
        JSONArray jsonArray = new JSONArray();
        ObjectMapper mapper1 = new ObjectMapper();
        ObjectNode ON=null;
        TypedQuery<AreaDeServicio> query;
        query = entityManager.createNamedQuery("AreaDeServicio.findByIdArea", AreaDeServicio.class);//JPQL
        query.setParameter("idArea", idarea); 
        AreaDeServicio area =null;
         try {
            area = (AreaDeServicio) query.getSingleResult();
            System.out.println(area.getNombre());
            System.out.println(area.getDescripcion());
            System.out.println("************************");
            System.out.println("Equipos del área");
            Collection<Equipo> equipoCollection=area.getEquipoCollection();
            ArrayNode NodeArrayEquipo = mapper1.createArrayNode();
            //Autores de la dirección de investigación
            for (Equipo equipok : equipoCollection) {
              ON=new ObjectMapper().createObjectNode();
              ON.put("ubicacion", equipok.getAreaDeServicioidArea().getDescripcion());
              ON.put("sala", equipok.getUbicacion());
              ON.put("nserie",equipok.getNSerie());
              NodeArrayEquipo.addPOJO(ON);
             }
            if(NodeArrayEquipo.size()>0){
                ON=new ObjectMapper().createObjectNode(); 
                ON.putPOJO("Equipos",NodeArrayEquipo);
                jsonArray.put(ON);
            }
            
            System.out.println("************************");            
            System.out.println("Usuarios del área");
            ArrayNode NodeArrayusr = mapper1.createArrayNode();
            Collection<Usuario> usuarioCollection=area.getUsuarioCollection();
            //Autores de la dirección de investigación
            for (Usuario usuariok : usuarioCollection) {
              System.out.println(usuariok.getNombre()+" "+usuariok.getApellidoPaterno());
              ON=new ObjectMapper().createObjectNode();
              ON.put("usrName", usuariok.getNombre()+" "+usuariok.getApellidoPaterno());
              NodeArrayusr.addPOJO(ON);
             }
            if(NodeArrayusr.size()>0){
                ON=new ObjectMapper().createObjectNode(); 
                ON.putPOJO("Usuarios",NodeArrayusr);
                jsonArray.put(ON);
            }            
            System.out.println("************************");
            System.out.println("Estudios del área");
            ArrayNode NodeArrayestudios = mapper1.createArrayNode();
             Collection<Estudio> estudioCollection=area.getEstudioCollection();
             for (Estudio estudiok : estudioCollection) {
                System.out.println(estudiok.getNombre()+" "+estudiok.getDescripcion());
              ON=new ObjectMapper().createObjectNode();
              ON.put("stdyId", estudiok.getIdEstudio());
              ON.put("stdyName", estudiok.getNombre());
              ON.put("stdyDes", estudiok.getDescripcion());
              NodeArrayestudios.addPOJO(ON);                
             }
             if(NodeArrayestudios.size()>0){
                ON=new ObjectMapper().createObjectNode(); 
                ON.putPOJO("Estudios",NodeArrayestudios);
                jsonArray.put(ON);
             } 
        } catch (Exception ex) {
            System.out.println("Articulo no encontrado");
            ex.printStackTrace(System.out);
        } 
        return jsonArray;
    }
    
    public boolean createArea(String nombre, String descripcion) {
        Query q = entityManager.createNativeQuery("SELECT MAX(idArea) FROM AreaDeServicio;");
        Number result = (Number) q.getSingleResult();
        int idArea = result.intValue() + 1;//el sigueinte regristro en la BD, es maximo + 1
        AreaDeServicio regse = new AreaDeServicio(idArea, nombre, descripcion);
        AreaDeServicio regalm = this.save(regse);
        return regalm != null;
    }
    
    public boolean updateArea(Integer idArea, String nombre, String descripcion) {
        AreaDeServicio regRol = this.find(idArea); //busqueda en la BD.
        regRol.setNombre(nombre);
        regRol.setDescripcion(descripcion);
        //System.out.println(horaInicio+" "+Usuario_ID +" "+horaFin);
        AreaDeServicio regalm = this.edit(regRol); //actualizacion de reg
        return regalm != null;
    }

    public boolean deleteArea(Integer idArea) {
        AreaDeServicio regRol = this.find(idArea);
        //System.out.println("REgistro a borrar: " + regRol.getNombre());
        return this.delete(regRol);
    }
    
    public boolean resolveCRUD(String operacion,String rolid,String nombre,String descripcion ){
        
        boolean respcrud=false;
        switch(operacion){
            case "CREATE":
               respcrud=createArea(nombre, descripcion);
            break;
            case "UPDATE":
                respcrud=updateArea(Integer.parseInt(rolid), nombre, descripcion);
            break;
            case "DELETE":
                respcrud=deleteArea(Integer.parseInt(rolid));
            break;            
        }
        return respcrud;
    }    

    
    public static void accesoJPA() {
        //solo para base de datos
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PesitenceREST"); // se encuentra en el archivo persistence.xml
        EntityManager entityManager = emf.createEntityManager();
        AreaManager areamanager = new AreaManager();
        areamanager.setEntityManager(entityManager);
        areamanager.getAllDataAreabyId(1);
    }    
    
    public static void main(String args[]) {
        accesoJPA();

    }     
    
}
