package com.RIS.MVC.model.DAOSpecialization;

import com.RIS.MVC.model.CRUDrepository.GenericDAOFacade;
import com.RIS.MVC.model.JPA.entities.Rol;
import java.util.Collection;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author DVGMetatools
 */
public class RolManager extends GenericDAOFacade<Rol> {

    public RolManager() {
        super(Rol.class);
    }

    public Collection<Rol> getAllDataRol() {
        TypedQuery<Rol> query;
        query = entityManager.createNamedQuery("Rol.findAll", Rol.class);//JPQL
        return query.getResultList();
    }

    /**
     *
     * @param nombre
     * @param descripcion
     * @return
     */
    public boolean createRol(/*Integer idRol,*/String nombre, String descripcion) {
        //SELECT MAX(idRol) AS maximo FROM rol; para verificar el maximo del id
        Query q = entityManager.createNativeQuery("SELECT MAX(idRol) FROM Rol;");
        //System.out.println("Respuesta: "+q);
        Number result = (Number) q.getSingleResult();
        int idRol = result.intValue() + 1;//el sigueinte regristro en la BD, es maximo + 1
        Rol regse = new Rol(idRol, nombre, descripcion);
        Rol regalm = this.save(regse);
        return regalm != null;
    }

    /**
     *
     * @param idRol|
     * @param nombre
     * @param descripcion
     * @return
     */
    public boolean updateRol(Integer idRol, String nombre, String descripcion) {
        Rol regRol = this.find(idRol); //busqueda en la BD.
        regRol.setNombre(nombre);
        regRol.setDescripcion(descripcion);
        //System.out.println(horaInicio+" "+Usuario_ID +" "+horaFin);
        Rol regalm = this.edit(regRol); //actualizacion de reg
        return regalm != null;
    }

    public boolean deleteRol(Integer idRol) {
        Rol regRol = this.find(idRol);
        //System.out.println("REgistro a borrar: " + regRol.getNombre());
        if (regRol.getNombre().equals("Admin")) {
            //no se puede borrar el rol de admin  
            return false;
        }
        return this.delete(regRol);
    }
    
    public boolean resolveCRUD(String operacion,String rolid,String nombre,String descripcion ){
        
        boolean respcrud=false;
        switch(operacion){
            case "CREATE":
               respcrud=createRol(nombre, descripcion);
            break;
            case "UPDATE":
                respcrud=updateRol(Integer.parseInt(rolid), nombre, descripcion);
            break;
            case "DELETE":
                respcrud=deleteRol(Integer.parseInt(rolid));
            break;            
        }
        return respcrud;
    }
      
        
}
