package com.RIS.MVC.model.DAOSpecialization;

import com.RIS.MVC.model.CRUDrepository.GenericDAOFacade;
import com.RIS.MVC.model.JPA.entities.SolicitudDeEstudio;
import com.RIS.MVC.model.JPA.entities.SolicitudDeEstudioPK;
import java.util.Date;

/**
 * Manejador de solicitudes de estudio.
 * @author Xochitl Ketzalli Robledo Rodriguez
 * @version 1.0
 */

public class StudyRequestManager extends GenericDAOFacade<SolicitudDeEstudio> {

    /** 
     * Constructor para invocar superclase de tipo SolicitudDeEstudio.
     */
    public StudyRequestManager() {
        
        //Acceso de atributos y metodos de la clase SolicitudDeEstudio.
        super(SolicitudDeEstudio.class);
    }
    
    /** 
     * Metodo para resolver operación CRUD para una solicitud de estudios.
     * @param operacion Identificación operación CRUD.
     * @param pacienteidPaciente Número de registro de un paciente.
     * @param medicoNumEmpleado Número de registro del empleado médico solicitante.
     * @param medicoCURP Registro CURP empleado del médico solicitante.
     * @param fechaSolicitudPk Identificador de la fecha y hora de acuerdo al estado de la solicitud de estudio en formato UNIX.
     * @param fechaSolicitud Fecha y hora de acuerdo al estado de la cita de estudios.
     * @param areaProcedencia Área de Procedencia del paciente en la solicitud de estudios.
     * @param fechaProximaCita Fecha y hora de la proxima cita médica del paciente.
     * @param diagnostico Diagnostico del paciente en la solicitud de estudios.
     * @param observaciones Observaciones del paciente en la solicitud de estudios.
     * @param estado Estado de la solicitud de estudios Solicitado, Programado, Reprogramado, Cancelado.
     * @return boolean Objeto booleano verdadero o falso de acuerdo al resultado de la operación CRUD.
     */
    
    public boolean ResolveCRUDStudyRequest(String operacion, String pacienteidPaciente, int medicoNumEmpleado, String medicoCURP, long fechaSolicitudPk, Date fechaSolicitud, String areaProcedencia, Date fechaProximaCita, String diagnostico, String observaciones, String estado){
        
        //Declaración de variable booleana.
        boolean respcrud = false;
        
        //Casos para determinar la operación CRUD.
        switch(operacion){
            //Operación CRUD Crear.
            case "CREATE":
                //Se invoca método para crear una solicitud de estudios.
                respcrud = CreateStudyRequest(pacienteidPaciente, medicoNumEmpleado, medicoCURP, fechaSolicitudPk, fechaSolicitud, areaProcedencia, fechaProximaCita, diagnostico, observaciones, estado);
            //Se finaliza la secuencia de instrucción.
            break;
        }
        
        //Retorno verdadero o falso de acuerdo al resultado de la operación CRUD.
        return respcrud;
    }
    
    /** 
     * Metodo CRUD para crear una solicitud de estudios.
     * @param pacienteidPaciente Número de registro de un paciente.
     * @param medicoNumEmpleado Número de registro del empleado médico solicitante.
     * @param medicoCURP Registro CURP empleado del médico solicitante.
     * @param fechaSolicitudPk Identificador de la fecha y hora de acuerdo al estado de la solicitud de estudio en formato UNIX.
     * @param fechaSolicitud Fecha y hora de acuerdo al estado de la cita de estudios.
     * @param areaProcedencia Área de Procedencia del paciente en la solicitud de estudios.
     * @param fechaProximaCita Fecha y hora de la proxima cita médica del paciente.
     * @param diagnostico Diagnostico del paciente en la solicitud de estudios.
     * @param observaciones Observaciones del paciente en la solicitud de estudios.
     * @param estado Estado de la solicitud de estudios Solicitado, Programado, Reprogramado, Cancelado.
     * @return boolean Objeto booleano verdadero o falso que identifica si fue insertado el registro de una solicitud de estudios.
     */
    
    public boolean CreateStudyRequest(String pacienteidPaciente, int medicoNumEmpleado, String medicoCURP, long fechaSolicitudPk, Date fechaSolicitud, String areaProcedencia, Date fechaProximaCita, String diagnostico, String observaciones, String estado){

        //Creación de objeto de tipo SolicitudDeEstudioPK, pasando el valor de parametros primarios de la entidad SolicitudDeEstudio.
        SolicitudDeEstudioPK regsepk = new SolicitudDeEstudioPK(pacienteidPaciente, medicoNumEmpleado, medicoCURP, fechaSolicitudPk);
        
        //Creación de objeto de tipo SolicitudDeEstudio, pasando el valor de parametros la referencia del objeto de tipo SolicitudDeEstudioPK y los valores de atributo simple de una solicitud de estudios.
        SolicitudDeEstudio regse = new SolicitudDeEstudio(regsepk, fechaSolicitud, areaProcedencia,fechaProximaCita, diagnostico, observaciones, estado);
        
        //Definicion de obtejo de tipo SolicitudDeEstudio
        SolicitudDeEstudio regsem = null;
        
        try{
            
            //Invocación operación CRUD para guardar el registro de una solicitud de estudios a la Base de Datos.
            regsem = this.save(regse);
            
        }catch(Exception ex){
            
            //Se imprime mensaje de error.
            System.out.println("No se pudo realizar la insercción a BD");            
            
            //Se forma mensaje de error para la excepción.
            ex.printStackTrace(System.out);
            
            //Retorno falso en caso de excepción.
            return false;
            
        }
        
        //Retorno verdadero si la inserción fue correcta y falso si fue incorrecta.
        return regsem != null;
        
    }
    
}