package com.RIS.MVC.model.DAOSpecialization;

import com.RIS.MVC.model.CRUDrepository.GenericDAOFacade;
import com.RIS.MVC.model.JPA.entities.EquipoImagenologia;
import java.util.Collection;
import javax.persistence.Query;

/**
 * Manejador de equipos médicos.
 * @author Xochitl Ketzalli Robledo Rodriguez
 * @version 1.0
 */

public class DeviceManager extends GenericDAOFacade<EquipoImagenologia> {

    /** 
     * Constructor para invocar superclase de tipo EquipoImagenologia
     */
    public DeviceManager() {
        //Acceso de atributos y metodos de la clase EquipoImagenologia
        super(EquipoImagenologia.class);
    }

    /** 
     * Metodo para obtener los equipos médicos del área.
     * @param idestudio Identificador del estudio médico.
     * @return Collection<Object[]> Una colección de objetos con los equipos médicos del área.
     */
    
    public Collection<Object[]> getDevice(String idestudio) {
        
        //Definicion de consulta SQL Nativa para obtener los datos de una cita de estudios agendada.
        String consultasms = "SELECT ei.NSerie, ei.Nombre, eq.Ubicacion\n" +
                "FROM EquipoImagenologia ei\n" +
                "JOIN Equipo eq\n" +
                "ON ei.NSerie = eq.NSerie AND ei.AreaDeServicio_idArea = eq.AreaDeServicio_idArea\n" +
                "JOIN Estudio es\n" +
                "ON eq.AreaDeServicio_idArea = es.AreaDeServicio_idArea\n" +
                "WHERE es.idEstudio = '"+idestudio+"';";
        
        //Impresión del query SQL.
        System.out.println("Query: "+consultasms);
        
        //Ejecución de un query SQL Nativa controlado vía interfaz NativeQuery.
        Query query = entityManager.createNativeQuery(consultasms);
        
        //Retorno el resultado de la consulta SQL Nativa.
        return query.getResultList();
        
    }
    
    /** 
     * Metodo para obtener los equipos médicos del área.
     * @param idarea
     * @return 
     */

    
    public Collection<Object[]> getSala(String idarea) {
        

        //Definicion de consulta SQL Nativa para obtener los datos de una cita de estudios agendada.
        /*
        String consultasms = "SELECT DISTINCT(eq.Ubicacion), CONCAT('Sala ',eq.Ubicacion)\n" +
                "FROM Equipo eq\n" +
                "JOIN AreaDeServicio ads\n" +
                "ON ads.idArea = eq.AreaDeServicio_idArea\n" +
                "WHERE ads.idArea LIKE '%" + idarea + "%';";
        */
        String consultasms ="SELECT DISTINCT(eq.Ubicacion), CONCAT('Sala ',eq.Ubicacion) "
                + "FROM Equipo eq, AreaDeServicio ads  "
                + "WHERE ads.idArea LIKE '%" + idarea + "%' "
                + "and ads.idArea = eq.AreaDeServicio_idArea;";        
        
        //Impresión del query SQL.
        System.out.println("Query: "+consultasms);
        
        //Ejecución de un query SQL Nativa controlado vía interfaz NativeQuery.
        Query query = entityManager.createNativeQuery(consultasms);
        
        //Retorno el resultado de la consulta SQL Nativa.
        return query.getResultList();
        
    }
    
    public boolean getMaintenance(String noserie, String fecha) {
        
        boolean resp = false;
        String consultamant = "SELECT * FROM AgendaDeServicio WHERE EquipoImagenologia_NSerie = '"+noserie+"' AND DATE(FechaControl) = '"+fecha+"';";

        Query querymant = entityManager.createNativeQuery(consultamant);
        
        if (!querymant.getResultList().isEmpty()){
            System.out.println("M");
            resp = true; //Mantenimiento
        }
        
        return resp;
        
    }
    
    public Collection<Object[]> getCalendarManual(String noserie, String fecha) {
        
        String consultahorarios = "SELECT DATE_FORMAT(Fecha,\"%H\"),DATE_FORMAT(Fecha,\"%i\") FROM AsignacionEstudio WHERE EquipoImagenologia_NSerie = '"+noserie+"' AND DATE(Fecha) = '"+fecha+"';";

        Query queryhorarios = entityManager.createNativeQuery(consultahorarios);
        
        return queryhorarios.getResultList();
        
    }
    
    
}