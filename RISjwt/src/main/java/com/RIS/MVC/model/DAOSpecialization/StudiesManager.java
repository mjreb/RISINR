package com.RIS.MVC.model.DAOSpecialization;

import com.RIS.MVC.model.CRUDrepository.GenericDAOFacade;
import com.RIS.MVC.model.JPA.entities.Estudio;
import java.util.Collection;
import java.util.Iterator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;


/** 
 * Manejador de estudios
  */

public class StudiesManager extends GenericDAOFacade<Estudio> {

    /** 
     * Constructor para invocar superclase de tipo Estudio.
     */
    
    public StudiesManager() {
        
        //Acceso de atributos y metodos de la clase Estudio.
        super(Estudio.class);
    }

    /** 
     * Metodo para obtener los estudios médicos.
     * @return 
     */
    
    public Collection<Estudio> getAllDataTable() {
        //Definición tipo de query de tipo Estudio
        TypedQuery<Estudio> query;
        //Referencia de la consulta a nivel clase Estudio utilizando JPQL
        query = entityManager.createNamedQuery("Estudio.findAll", Estudio.class);
        //Retorno el resultado de la consulta con JPQL
        return query.getResultList();
    }
    
    /** 
     * Metodo para obtener los estudios médicos de un área.
     * @param idarea
     * @return Collection<Object[]> Una colección de objetos con los estudios médicos del área.
     */
    
    public Collection<Object[]> getStudies(String idarea) {
        //Definicion de consulta SQL Nativa para obtener los datos de una cita de estudios agendada.
        String consultasms = "SELECT *\n" +
                "FROM Estudio\n" +
                "WHERE AreaDeServicio_idArea = '" + idarea +"';";
        //Impresión del query SQL.
        System.out.println("Query: "+consultasms);
        //Ejecución de un query SQL Nativa controlado vía interfaz NativeQuery.
        Query query = entityManager.createNativeQuery(consultasms);
        //Retorno el resultado de la consulta SQL Nativa.
        return query.getResultList();
    }
    public static void accesoJPA() {
        //solo para base de datos
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PesitenceREST"); // se encuentra en el archivo persistence.xml
        EntityManager entityManager = emf.createEntityManager();
        StudiesManager studiesmanager = new StudiesManager();
        studiesmanager.setEntityManager(entityManager);
        Collection<Estudio> liststudies=studiesmanager.getAllDataTable();
        int i = 0;
        for (Iterator<Estudio> it = liststudies.iterator(); it.hasNext(); i++) {
            Estudio s = it.next();
            System.out.println(i + ": " +s.getNombre() +", "+s.getDescripcion()+", "+s.getAreaDeServicioidArea().getNombre());
        }        

    }    
    
    public static void main(String args[]) {
        accesoJPA();

    }     
}