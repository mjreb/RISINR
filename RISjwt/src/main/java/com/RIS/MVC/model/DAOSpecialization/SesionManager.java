package com.RIS.MVC.model.DAOSpecialization;

import com.RIS.MVC.model.CRUDrepository.GenericDAOFacade;
import com.RIS.MVC.model.JPA.entities.Sesion;
import com.RIS.MVC.model.JPA.entities.SesionPK;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author DVGMetatools
 */
public class SesionManager extends GenericDAOFacade<Sesion> {

    public SesionManager() {
        super(Sesion.class);
    }
    
    //Read
    public Collection<Sesion> getAllDataSesion() {
        TypedQuery<Sesion> query;
        query = entityManager.createNamedQuery("Sesion.findAll", Sesion.class);//JPQL
        return query.getResultList();
    }

    //Create  
    //public boolean createSesion(long horaInicio, String Usuario_ID,String IPADDRS) {
        //Sesion regse=new Sesion(horaInicio,Usuario_ID);
        //regse.setIPDispositivo(IPADDRS);
        //Sesion regalm=this.save(regse);
        //return regalm != null;
    //}
    public boolean createSesion(long horaInicio, String Usuario_ID, String IPADDRS, int numempl, String usuarioCURP){
        Sesion regse=new Sesion(horaInicio,Usuario_ID, numempl, usuarioCURP);
        regse.setIPDispositivo(IPADDRS);
        SesionPK auxiliarprimaria = regse.getSesionPK();
        auxiliarprimaria.setaplicacionID(1);
        Sesion regalm=this.save(regse);
        return regalm != null;
    }
    
    
    //Update horafin
    public boolean updateSesion(long horaInicio, String Usuario_ID,long horaFin) {
        SesionPK regfind=new SesionPK(horaInicio, Usuario_ID);//llave primaria
        Sesion regse=this.find(regfind); //busqueda en la BD.
        regse.setHoraFin(BigInteger.valueOf(horaFin));
        Sesion regalm=this.edit(regse);
        return regalm == null;
    } 
    
    //Update rol
    public boolean updateRol(long horaInicio, String Usuario_ID,String nombrerol) {
        SesionPK regfind=new SesionPK(horaInicio, Usuario_ID);//llave primaria
        Sesion regse=this.find(regfind);
        regse.setRolNombre(nombrerol);
        Sesion regalm=this.edit(regse); //actualizacion de reg
        return regalm == null;
    }
    
    //Delete
    public boolean deleteSesion(long horaInicio, String Usuario_ID) {
        SesionPK regfind=new SesionPK(horaInicio, Usuario_ID);//llave primaria
        Sesion refsesion=this.find(regfind);        
        return this.delete(refsesion);
    }
    
    public static void accesoJPA() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PesitenceREST"); // se encuentra en el archivo persistence.xml
        EntityManager entityManager = emf.createEntityManager();
        SesionManager sesion = new SesionManager();
        sesion.setEntityManager(entityManager);
        //long horainicio=new Long("1615318447813"); //llave primaria de fecha de sesion
        long horainicio=new BigInteger("1615313005021").longValue();//llave primaria de fecha de sesion
        Boolean regBorrado=sesion.deleteSesion(horainicio, "carlos");
        System.out.println("Registro borrado: "+regBorrado);
        /*
        Query tmp= entityManager.createQuery("DELETE FROM Perfil WHERE Usuario_NumEmpleado=:empleado and Usuario_CURP=:curp");
        tmp.setParameter("empleado", 11);
        tmp.setParameter("curp", "aop000");
        int result =tmp.executeUpdate();
        System.out.println("Perfil Delete Status="+result);
        */
    }    
    
    public static void main(String args[]) {
        boolean accesojpa = true;
        //boolean accesojpa=false;
        if (accesojpa) {
            //Metodo 1 acceso directo a entidades 
            accesoJPA();
        }/* else {
            //Metodo 1 acceso por medio de Spring 
            accesoInyeccionDependencias();
        }*/
    }    
    
}
