package com.RIS.MVC.model.CRUDrepository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author Marco
 * @param <T>
 */
public class GenericDAOFacade<T>{

    protected EntityManager entityManager;
    //private Class<T> type;
    private final Class<T> type;

    public GenericDAOFacade(Class<T> type) {
        this.type = type;
    }


    public T save(T emp) {
/*
EntityTransaction tx = entityManager.getTransaction();
tx.begin();
entityManager.persist(emp);
tx.commit(); */

        System.out.println("Preparando Guardado");
        entityManager.getTransaction().begin();
        System.out.println("###Registro por Guardar***");    
        entityManager.persist(emp);
        entityManager.getTransaction().commit();
        System.out.println("Registro Guardado");          
        //entityManager.flush();
        return emp;
    }    
    
    
    /*public T save(T emp) {
        entityManager.getTransaction().begin();
        entityManager.persist(emp);
        entityManager.getTransaction().commit();
        //entityManager.flush();
        return emp;
    }*/

    public T find(Object empId) {
        return (T) entityManager.find(type, empId); //busqueda por clave primaria
    }

   
    
    public T edit(T emp) {
        T nuevo = null;
        System.out.println("En proceso de Actualización");
        entityManager.getTransaction().begin();
        nuevo = entityManager.merge(emp);
        entityManager.getTransaction().commit();
        System.out.println("Registro Actualizado");         
        //entityManager.clear(); //++dvg 10112022
        return nuevo;
    }    
    
    /*public T edit(T emp) {
        entityManager.getTransaction().begin();
        T nuevo = entityManager.merge(emp);
        entityManager.getTransaction().commit();
        return nuevo;
    }*/

    public Boolean delete(T emp) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.merge(emp)); //busca primero el registro y posteriormente lo borra
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
	public List<T> getAll() {
        entityManager.getTransaction().begin();
        String consulta = "SELECT t FROM " + type.getSimpleName() + " t";
        System.out.println("Consulta: " + consulta);
        Query q = (TypedQuery<T>) entityManager.createQuery(consulta, type);
        entityManager.getTransaction().commit();
        return (List<T>) q.getResultList();
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

}
