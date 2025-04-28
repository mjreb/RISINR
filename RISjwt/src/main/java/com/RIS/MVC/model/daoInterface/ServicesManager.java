package com.RIS.MVC.model.daoInterface;

import com.RIS.MVC.model.CRUDrepository.GenericDAOFacade;
import com.RIS.MVC.model.JPA.entities.Usuario;
import com.RIS.MVC.model.DAOSpecialization.UsersManager;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ServicesManager implements ExtensionInterface {
    private String[] nombredeservicios = null;
    public EntityManager entityManager;
    private ApplicationContext contexto;

    public ServicesManager(String nombreconformancedigital) {
        try {
            System.out.println("Cargando interfaz Spring: " + nombreconformancedigital);
            contexto = new ClassPathXmlApplicationContext(nombreconformancedigital);
            //contexto = new FileSystemXmlApplicationContext(nombreconformancedigital);//de esta forma es necesario dar toda la ruta
        } catch (BeansException e) {
            e.printStackTrace(System.out);
        }
        if (contexto != null) {
            nombredeservicios = contexto.getBeanDefinitionNames();
            if (entityManager == null) {
                System.out.println("Cargando enterprise manager");
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("PesitenceREST"); // se encuentra en el archivo persistence.xml
                entityManager = emf.createEntityManager();
            }
            System.out.println("--Se cargo interfaz spring adecuadamente---");
            System.out.println("--***Se genero el entityManager***---");
        }
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Object getServicio(String id) {
        Object bean = null;
        try {
            bean = contexto.getBean(id);
        } catch (BeansException ex) {
            System.out.println("Error al cargar el bean: "+id+" "+ex.getMessage());
        }
        return bean;
    }

    public String[] getServiciosRegistrados() {
        return nombredeservicios;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public static void main(String args[]) {
    	
   	
    	System.out.println("Probando la busqueda por Periodo de busqueda (STUDYDATE) ");
    	    	
        System.out.println("Ejecutando spring inyección de dependencias");
        ServicesManager admondaos = new ServicesManager("AplicationContext.xml");
        
     
        if (admondaos.getServiciosRegistrados() != null) {
            for (String serviciosRegistrado : admondaos.getServiciosRegistrados()) {
                System.out.println("######Registrando DAO: " + serviciosRegistrado);
            }
            Object userRef = admondaos.getServicio("userdao"); //al regresar el servicio se le agrega su entityManager
            if (userRef != null) {
                if (userRef instanceof UsersManager) {
                    System.out.println("---Accediendo via AdministradorDaos----");
                    ((GenericDAOFacade<?>) userRef).setEntityManager(admondaos.getEntityManager()); //registrar el entitymanager para el bean correpsondiente de datos                    
                    Collection<Usuario> listausrdao = ((UsersManager) userRef).getAllDataUser();
                    for (Usuario o : listausrdao) {
                        System.out.println("Nombre: " + o.getNombre() + " Apellidos: " + o.getApellidoPaterno()+" "+o.getApellidoMaterno());
                    }
                }
            }
        }
    }
}
