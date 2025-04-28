package com.RIS.MVC.model.DAOSpecialization;

import com.RIS.MVC.model.CRUDrepository.GenericDAOFacade;
import com.RIS.MVC.model.JPA.entities.ControlEstudios;
import com.RIS.MVC.model.JPA.entities.ControlEstudiosPK;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * Manejador de agenda de citas de estudio.
 *
 * @author Xochitl Ketzalli Robledo Rodriguez
 * @version 1.0
 */
public class AppointmentManager extends GenericDAOFacade<ControlEstudios> {

    /**
     * Constructor para invocar superclase de tipo ControlEstudios.
     */
    public AppointmentManager() {

        //Acceso de atributos y metodos de la clase ControlEstudios.
        super(ControlEstudios.class);
    }

    /**
     * Metodo para obtener los datos de una cita de estudios agendada.
     *
     * @param idpaciente Número de registro de un paciente.
     * @param nombrepaciente Nombre del paciente.
     * @param apellidopaternopaciente Apellido paterno del paciente.
     * @param apellidomaternopaciente Apellido materno del paciente.
     * @param idestudio Identificador de estudio.
     * @param fechamin Intervalo de fecha mínima.
     * @param fechamax Intervalo de fecha máxima.
     * @param idarea Identificador de área de estudio.
     * @param sala Sala del equipo.
     * @param estado Estado de las citas.
     * @return Collection<Object[]> Una colección de objetos con los datos de
     * una cita de estudios agendada.
     */
    public Collection<Object[]> getAppointment(String idpaciente, String nombrepaciente, String apellidopaternopaciente, String apellidomaternopaciente, String idestudio, String fechamin, String fechamax, String idarea, String sala, String estado) {

        //Declaración de variables String.
        String consultaestado = null, consultafecha = null;

        //Identifica las busquedas de acuerdo a los estados recibidos.
        switch (estado) {
            case "programadoreprocanc":
                consultaestado = "((ce.Cerrado = 0 AND ce.Estado IN ('Programado','Reprogramado')\n"
                        + "AND ae.EquipoImagenologia_NSerie IS NOT NULL AND ae.Estudio_idEstudio IS NOT NULL\n"
                        + "AND ae.FechaPk IS NOT NULL AND ae.Fecha IS NOT NULL)\n"
                        + "OR (ce.Cerrado = 1 AND ce.Estado = 'Cancelado'))\n";
                break;
            case "programadorepro":
                consultaestado = "((ce.Cerrado = 0 AND ce.Estado IN ('Programado','Reprogramado')\n"
                        + "AND ae.EquipoImagenologia_NSerie IS NOT NULL AND ae.Estudio_idEstudio IS NOT NULL\n"
                        + "AND ae.FechaPk IS NOT NULL AND ae.Fecha IS NOT NULL))\n";
                break;
            case "programado":
                consultaestado = "(ce.Cerrado = 0 AND ce.Estado = 'Programado'\n"
                        + "AND ae.EquipoImagenologia_NSerie IS NOT NULL AND ae.Estudio_idEstudio IS NOT NULL\n"
                        + "AND ae.FechaPk IS NOT NULL AND ae.Fecha IS NOT NULL)\n";
                break;
            case "reprogramado":
                consultaestado = "(ce.Cerrado = 0 AND ce.Estado = 'Reprogramado'\n"
                        + "AND ae.EquipoImagenologia_NSerie IS NOT NULL AND ae.Estudio_idEstudio IS NOT NULL\n"
                        + "AND ae.FechaPk IS NOT NULL AND ae.Fecha IS NOT NULL)\n";
                break;
            case "cancelado":
                consultaestado = "((ce.Cerrado = 1 AND ce.Estado = 'Cancelado'))\n";
                break;
        }

        //Validación de intervalo de fecha minima vacia.
        if (fechamin.isEmpty()) {
            consultafecha = "";
        } else {
            if (fechamax.isEmpty()) {
                consultafecha = "AND (ce.FechaControl BETWEEN '" + fechamin + "' AND SYSDATE())\n";
            } else if ((fechamax.compareTo(fechamin)) == 0) {
                consultafecha = "AND (ce.FechaControl LIKE '" + fechamin + "%')\n";
            } else {
                consultafecha = "AND (ce.FechaControl BETWEEN '" + fechamin + "' AND '" + fechamax + "')\n";
            }
        }

        //Definicion de consulta SQL Nativa para obtener los datos de una cita de estudios agendada.
        String consultasms = "SELECT ce.FechaControl, IFNULL(eq.Ubicacion,''), us.ApellidoPaterno, us.ApellidoMaterno,\n"
                + "us.Nombre, us.CURP, ce.Estado, pa.ApellidoPaterno, pa.ApellidoMaterno, pa.Nombre, se.*,\n"
                + "IFNULL(ae.EquipoImagenologia_NSerie,''), IFNULL(ae.Estudio_idEstudio,''),\n"
                + "IFNULL(ae.FechaPk,''), IFNULL(ae.Fecha,'0000-00-00 00:00:00'),\n"
                + "do.TiempoDeTraslado, IFNULL(es.Nombre,''), ce.Usuario_NumEmpleado, se.Medico_NumEmpleado,\n"
                + "IFNULL(es.AreaDeServicio_idArea,''), IFNULL(es.idEstudio,''),\n"
                + "do.Calle, do.Numero, do.Colonia, do.CP, do.AlcaldiaMunicipio, do.Estado, IFNULL(ad.Nombre,'')\n"
                + "FROM ControlEstudios ce\n"
                + "LEFT JOIN AsignacionEstudio ae\n"
                + "ON (ce.FechaControlPk = ae.FechaPk AND ce.Estudio_idEstudio = ae.Estudio_idEstudio)\n"
                + "LEFT JOIN Equipo eq\n"
                + "ON ae.EquipoImagenologia_NSerie = eq.NSerie\n"
                + "LEFT JOIN Usuario us\n"
                + "ON ce.Usuario_NumEmpleado = us.NumEmpleado\n"
                + "LEFT JOIN Paciente pa\n"
                + "ON ce.Paciente_IDPaciente = pa.idPaciente\n"
                + "LEFT JOIN SolicitudDeEstudio se\n"
                + "ON (se.FechaSolicitudPk = ce.FechaControlPk AND se.Paciente_IDPaciente = ce.Paciente_IDPaciente)\n"
                + "LEFT JOIN Domicilio do\n"
                + "ON do.Paciente_IDPaciente = pa.idPaciente\n"
                + "LEFT JOIN Estudio es\n"
                + "ON es.idEstudio = ce.Estudio_idEstudio\n"
                + "LEFT JOIN AreaDeServicio ad\n"
                + "ON ad.idArea = es.AreaDeServicio_idArea\n"
                + "WHERE " + consultaestado
                + (idpaciente.isEmpty() == true ? "" : "AND ce.Paciente_IDPaciente = '" + idpaciente + "'\n")
                + "AND pa.Nombre LIKE '%" + nombrepaciente + "%'\n"
                + "AND pa.ApellidoPaterno LIKE '%" + apellidopaternopaciente + "%'\n"
                + "AND pa.ApellidoMaterno LIKE '%" + apellidomaternopaciente + "%'\n"
                + (idarea.isEmpty() == true ? "" : "AND IFNULL(es.AreaDeServicio_idArea,'') = '" + idarea + "'\n")
                + "AND IFNULL(eq.Ubicacion,'') LIKE '%" + sala + "%'\n"
                + (idestudio.isEmpty() == true ? "" : "AND IFNULL(es.idEstudio,'') = '" + idestudio + "'\n")
                + consultafecha
                + "ORDER BY se.Estado DESC;\n";

        //Impresión del query SQL.
        System.out.println("Query: " + consultasms);

        //Ejecución de un query SQL Nativa controlado vía interfaz NativeQuery.
        Query query = entityManager.createNativeQuery(consultasms);

        //Retorno el resultado de la consulta SQL Nativa.
        return query.getResultList();

    }

    /**
     * Metodo para obtener los datos de una solicitud de estudios
     *
     * @param idpaciente Número de registro de un paciente.
     * @param nombrepaciente Nombre del paciente.
     * @param apellidopaternopaciente Apellido paterno del paciente.
     * @param apellidomaternopaciente Apellido materno del paciente.
     * @param idestudio Identificador de estudio.
     * @param fechamin Intervalo de fecha mínima.
     * @param fechamax Intervalo de fecha máxima.
     * @param idarea Identificador de área de estudio.
     * @return Collection<Object[]> Una colección de objetos con los datos de
     * una solicitud de estudios.
     */
    public Collection<Object[]> getStudyOrders(String idpaciente, String nombrepaciente, String apellidopaternopaciente, String apellidomaternopaciente, String idestudio, String fechamin, String fechamax, String idarea) {

        //Definicion de consulta SQL Nativa para obtener los datos de una cita de estudios agendada.
        String consultasms = "SELECT us.ApellidoPaterno, us.ApellidoMaterno, us.Nombre, pa.ApellidoPaterno,\n"
                + "pa.ApellidoMaterno, pa.Nombre, sa.Nombre, es.Nombre, es.Descripcion, es.AreaDeServicio_idArea,\n"
                + "do.Calle, do.Numero, do.Colonia, do.CP, do.AlcaldiaMunicipio, do.Estado, do.TiempoDeTraslado, ce.*,\n"
                + "se.AreaProcedencia, se.FechaProximaCita, se.Diagnostico\n"
                + "FROM SolicitudDeEstudio se\n"
                + "JOIN ControlEstudios ce\n"
                + "ON (ce.FechaControlPk = se.FechaSolicitudPk AND ce.Paciente_IDPaciente = se.Paciente_IDPaciente\n"
                + "AND ce.Usuario_NumEmpleado = se.Medico_NumEmpleado AND ce.Usuario_CURP = se.Medico_CURP)\n"
                + "JOIN Usuario us\n"
                + "ON us.NumEmpleado = ce.Usuario_NumEmpleado\n"
                + "JOIN Paciente pa\n"
                + "ON pa.idPaciente = ce.Paciente_IDPaciente\n"
                + "JOIN Estudio es\n"
                + "ON es.idEstudio = ce.Estudio_idEstudio\n"
                + "JOIN AreaDeServicio sa\n"
                + "ON sa.idArea = es.AreaDeServicio_idArea\n"
                + "JOIN Domicilio do\n"
                + "ON do.Paciente_IDPaciente = pa.idPaciente\n"
                + "WHERE ce.Estado = 'Solicitado'\n"
                + "AND ce.Cerrado = 0\n"
                + (idpaciente.isEmpty() == true ? "" : "AND ce.Paciente_IDPaciente = '" + idpaciente + "'\n")
                + "AND pa.Nombre LIKE '%" + nombrepaciente + "%'\n"
                + "AND pa.ApellidoPaterno LIKE '%" + apellidopaternopaciente + "%'\n"
                + "AND pa.ApellidoMaterno LIKE '%" + apellidomaternopaciente + "%'\n"
                + (idarea.isEmpty() == true ? "" : "AND es.AreaDeServicio_idArea = '" + idarea + "'\n")
                + (idestudio.isEmpty() == true ? ";" : "AND es.idEstudio = '" + idestudio + "';\n")
                + (fechamin.isEmpty() ? ";" : fechamax.isEmpty() ? "\nAND (ce.FechaControl BETWEEN '" + fechamin + "' AND SYSDATE());" : "\nAND (ce.FechaControl BETWEEN '" + fechamin + "' AND '" + fechamax + "');");

        //Impresión del query SQL.
        System.out.println("Query: " + consultasms);

        //Ejecución de un query SQL Nativa controlado vía interfaz NativeQuery.
        Query query = entityManager.createNativeQuery(consultasms);

        //Retorno el resultado de la consulta SQL Nativa.
        return query.getResultList();

    }

    /**
     * Metodo para obtener las áreas de servicio.
     *
     * @return Collection<Object[]> Una colección de objetos con las área de
     * servicio.
     */
    public Collection<Object[]> getAllAreas() {

        //Definicion de consulta SQL Nativa para obtener los datos de una cita de estudios agendada.
        String consultasms = "SELECT idArea, Nombre FROM AreaDeServicio "
                + "WHERE idArea <> 5 AND idArea <> 8;";

        //Impresión del query SQL.
        System.out.println("Query: " + consultasms);

        //Ejecución de un query SQL Nativa controlado vía interfaz NativeQuery.
        Query query = entityManager.createNativeQuery(consultasms);

        //Retorno el resultado de la consulta SQL Nativa.
        return query.getResultList();

    }

    /**
     * Metodo CRUD para crear una cita de estudios.
     *
     * @param usuarioNumEmpleado Número de registro del empleado médico
     * solicitante.
     * @param usuarioCURP Registro CURP empleado del médico solicitante.
     * @param pacienteIDPaciente Número de registro de un paciente.
     * @param estudioidEstudio Número de registro de un estudio médico.
     * @param fechaControlPk Identificador de la fecha y hora de acuerdo al
     * estado de la solicitud de estudio en formato UNIX.
     * @param fechaControl Fecha y hora de acuerdo al estado de la solicitud de
     * estudios.
     * @param estado Estado de la cita de estudios Solicitado, Programado,
     * Reprogramado, Cancelado.
     * @param cerrado Bandera para identificar si la cita esta disponible.
     * @param observaciones Registro Observaciones en caso de cancelar la cita.
     * @return boolean Objeto booleano verdadero o falso que identifica si fue
     * insertado el registro de la cita de estudios.
     */
    public boolean CreateStudyControl(int usuarioNumEmpleado, String usuarioCURP, String pacienteIDPaciente, int estudioidEstudio, long fechaControlPk, Date fechaControl, String estado, boolean cerrado, String observaciones) {

        //Creación de objeto de tipo ControlEstudiosPK, pasando el valor de parametros primarios de la entidad ControlEstudios.
        ControlEstudiosPK regcepk = new ControlEstudiosPK(usuarioNumEmpleado, usuarioCURP, pacienteIDPaciente, estudioidEstudio, fechaControlPk);

        //Creación de objeto de tipo ControlEstudios, pasando el valor de parametros la referencia del objeto de tipo ControlEstudiosPK y los valores de atributo simple de una cita de estudios.
        ControlEstudios regce = new ControlEstudios(regcepk, fechaControl, estado, cerrado, observaciones);

        //Definicion de obtejo de tipo ControlEstudios
        ControlEstudios regcem = null;

        try {

            //Invocación operación CRUD para guardar el registro de una cita de estudios a la Base de Datos.
            regcem = this.save(regce);

        } catch (Exception ex) {

            //Se imprime mensaje de error.
            System.out.println("No se pudo realizar la insercción a BD");

            //Se forma mensaje de error para la excepción.
            ex.printStackTrace(System.out);

            //Retorno falso en caso de excepción.
            return false;

        }

        //Retorno verdadero si la inserción fue correcta y falso si fue incorrecta.
        return regcem != null;

    }

    /**
     * Metodo CRUD para crear una cita de estudios.
     *
     * @param usuarioNumEmpleado Número de registro del empleado médico
     * solicitante.
     * @param usuarioCURP Registro CURP empleado del médico solicitante.
     * @param pacienteIDPaciente Número de registro de un paciente.
     * @param estudioidEstudio Número de registro de un estudio médico.
     * @param fechaControlPk Identificador de la fecha y hora de acuerdo al
     * estado de la solicitud de estudio en formato UNIX.
     * @param cerrado Bandera para identificar si la cita esta disponible.
     * @return boolean Objeto booleano verdadero o falso que identifica si fue
     * insertado el registro de la cita de estudios.
     */
    public boolean UpdateStudyControlClosed(int usuarioNumEmpleado, String usuarioCURP, String pacienteIDPaciente, int estudioidEstudio, long fechaControlPk, boolean cerrado) {

        //Creación de objeto de tipo ControlEstudiosPK, pasando el valor de parametros primarios de la entidad ControlEstudios.
        ControlEstudiosPK regcepk = new ControlEstudiosPK(usuarioNumEmpleado, usuarioCURP, pacienteIDPaciente, estudioidEstudio, fechaControlPk);

        //Definicion de obtejo de tipo ControlEstudios
        ControlEstudios regcem = null;

        //Excepción en caso de fallo durante la transacción de actualización.
        try {

            //Se genera un objeto de tipo ControlEstudios.
            ControlEstudios regce = this.find(regcepk);

            //Se actualiza el valor del atributo cerrado.
            regce.setCerrado(cerrado);

            //Invocación operación CRUD para guardar el registro de una cita de estudios a la Base de Datos.
            regcem = this.edit(regce);

        } catch (Exception ex) {

            //Se imprime mensaje de error.
            System.out.println("No se pudo realizar la insercción a BD");

            //Se forma mensaje de error para la excepción.
            ex.printStackTrace(System.out);

            //Retorno falso en caso de excepción.
            return false;

        }

        //Creación de objeto de tipo ControlEstudios, pasando el valor de parametros la referencia del objeto de tipo ControlEstudiosPK y los valores de atributo simple de una cita de estudios.
        //Retorno verdadero si la inserción fue correcta y falso si fue incorrecta.
        return regcem != null;
    }

    /**
     * Metodo para resolver operación CRUD para una cita de estudios.
     *
     * @param operacion Identificación operación CRUD.
     * @param usuarioNumEmpleado Número de registro del empleado médico
     * solicitante.
     * @param usuarioCURP Registro CURP empleado del médico solicitante.
     * @param pacienteIDPaciente Número de registro de un paciente.
     * @param estudioidEstudio Número de registro de un estudio médico.
     * @param fechaControlPk Identificador de la fecha y hora de acuerdo al
     * estado de la cita de estudio en formato UNIX.
     * @param fechaControl Fecha y hora de acuerdo al estado de la cita de
     * estudios.
     * @param estado Estado de la cita de estudios Solicitado, Programado,
     * Reprogramado, Cancelado.
     * @param cerrado Bandera para identificar si la cita esta disponible.
     * @param observaciones Registro Observaciones en caso de cancelar la cita.
     * @return boolean Objeto booleano verdadero o falso de acuerdo al resultado
     * de la operación CRUD.
     */
    public boolean ResolveCRUDStudyControl(String operacion, int usuarioNumEmpleado, String usuarioCURP, String pacienteIDPaciente, int estudioidEstudio, long fechaControlPk, Date fechaControl, String estado, boolean cerrado, String observaciones) {

        //Declaración de variable booleana.
        boolean respcrud = false;

        //Casos para determinar la operación CRUD.
        switch (operacion) {
            //Operación CRUD Crear.
            case "CREATE":
                //Se invoca método para crear una cita de estudios.
                respcrud = CreateStudyControl(usuarioNumEmpleado, usuarioCURP, pacienteIDPaciente, estudioidEstudio, fechaControlPk, fechaControl, estado, cerrado, observaciones);
                //Se finaliza la secuencia de instrucción.
                break;
            //Operación CRUD Actualizar Atributo Cerrado.
            case "UPDATECLOSED":
                //Se invoca método para actualizar el atributo cerrado en una cita de estudios.
                respcrud = UpdateStudyControlClosed(usuarioNumEmpleado, usuarioCURP, pacienteIDPaciente, estudioidEstudio, fechaControlPk, cerrado);
                //Se finaliza la secuencia de instrucción.
                break;
        }

        //Retorno verdadero o falso de acuerdo al resultado de la operación CRUD.
        return respcrud;
    }

    /**
     * Metodo CRUD para crear el registro para la asignación de citas de
     * estudios médicos.
     *
     * @param equipoImagenologiaNSerie Número de registro del equipo médico.
     * @param estudioidEstudio Número de registro de un estudio médico.
     * @param fechaPk Identificador de la fecha y hora de asignación de estudios
     * médicos en formato UNIX.
     * @param fecha Fecha y Hora de asignación de estudios médicos.
     * @return boolean Objeto booleano verdadero o falso de acuerdo al resultado
     * de la operación CRUD.
     */
    public boolean CreateStudyAssignment(String equipoImagenologiaNSerie, String estudioidEstudio, String fechaPk, String fecha) {

        //Definicion de consulta SQL Nativa para obtener las fechas de las citas de estudios agendadas.
        Query query = entityManager.createNativeQuery("INSERT INTO AsignacionEstudio(EquipoImagenologia_NSerie, Estudio_idEstudio, FechaPk, Fecha) VALUES (\"" + equipoImagenologiaNSerie + "\",\"" + estudioidEstudio + "\",\"" + fechaPk + "\",\"" + fecha + "\");");

        //Excepción en caso de fallo durante la transacción de insercción.
        try {
            //A partir del Entity Manager se inicia una transacción.
            entityManager.getTransaction().begin();

            //Se ejecuta el query SQL Nativo a traves de una transacción.
            query.executeUpdate();

            //Se cierra la transacción.
            entityManager.getTransaction().commit();

        } catch (Exception ex) {
            //Se imprime mensaje de error.
            System.out.println("No se pudo realizar la insercción a BD");

            //Se forma mensaje de error para la excepción.
            ex.printStackTrace(System.out);

            //Retorno falso en caso de excepción.
            return false;
        }

        //Retorno verdadero si la operación se realizo correctamente
        return true;
    }

    /**
     * Metodo CRUD para eliminar el registro para la asignación de citas de
     * estudios médicos.
     *
     * @param equipoImagenologiaNSerie Número de registro del equipo médico.
     * @param estudioidEstudio Número de registro de un estudio médico.
     * @param fechaPk Identificador de la fecha y hora de asignación de estudios
     * médicos en formato UNIX.
     * @param equipoImagenologiaNSerie_new
     * @param estudioidEstudio_new
     * @param fechaPk_new
     * @param fecha_new
     * @return boolean Objeto booleano verdadero o falso de acuerdo al resultado
     * de la operación CRUD.
     */
    public boolean UpdateStudyAssignment(String equipoImagenologiaNSerie, String estudioidEstudio, String fechaPk, String equipoImagenologiaNSerie_new, String estudioidEstudio_new, String fechaPk_new, String fecha_new) {

        //Definicion de consulta SQL Nativa para eliminar el registro de la asignación de una cita de estudios médicos.
        Query query = entityManager.createNativeQuery("UPDATE AsignacionEstudio\n"
                + "SET EquipoImagenologia_NSerie = '" + equipoImagenologiaNSerie_new + "', Estudio_idEstudio = '" + estudioidEstudio_new + "', FechaPk = '" + fechaPk_new + "', Fecha = '" + fecha_new + "'\n"
                + "WHERE (EquipoImagenologia_NSerie = '" + equipoImagenologiaNSerie + "') and (Estudio_idEstudio = '" + estudioidEstudio + "') and (FechaPk = '" + fechaPk + "');");

        System.out.println(query);

        //Excepción en caso de fallo durante la transacción de insercción.
        try {
            //A partir del Entity Manager se inicia una transacción.
            entityManager.getTransaction().begin();

            //Se ejecuta el query SQL Nativo a traves de una transacción.
            query.executeUpdate();

            //Se cierra la transacción.
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            //Se imprime mensaje de error.
            System.out.println("No se pudo realizar la eliminación del registro en BD");

            //Retorno falso en caso de excepción.
            ex.printStackTrace(System.out);
            return false;
        }

        //Retorno verdadero si la operación se realizo correctamente
        return true;
    }

    /**
     * Metodo CRUD para eliminar el registro para la asignación de citas de
     * estudios médicos.
     *
     * @param equipoImagenologiaNSerie Número de registro del equipo médico.
     * @param estudioidEstudio Número de registro de un estudio médico.
     * @param fechaPk Identificador de la fecha y hora de asignación de estudios
     * médicos en formato UNIX.
     * @return boolean Objeto booleano verdadero o falso de acuerdo al resultado
     * de la operación CRUD.
     */
    public boolean DeleteStudyAssignment(String equipoImagenologiaNSerie, String estudioidEstudio, String fechaPk) {

        //Definicion de consulta SQL Nativa para eliminar el registro de la asignación de una cita de estudios médicos.
        Query query = entityManager.createNativeQuery("DELETE FROM AsignacionEstudio WHERE (EquipoImagenologia_NSerie = \"" + equipoImagenologiaNSerie + "\") AND (Estudio_idEstudio = \"" + estudioidEstudio + "\") and (FechaPk = \"" + fechaPk + "\");");

        //Excepción en caso de fallo durante la transacción de insercción.
        try {
            //A partir del Entity Manager se inicia una transacción.
            entityManager.getTransaction().begin();

            //Se ejecuta el query SQL Nativo a traves de una transacción.
            query.executeUpdate();

            //Se cierra la transacción.
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            //Se imprime mensaje de error.
            System.out.println("No se pudo realizar la eliminación del registro en BD");

            //Retorno falso en caso de excepción.
            ex.printStackTrace(System.out);
            return false;
        }

        //Retorno verdadero si la operación se realizo correctamente
        return true;
    }

    /**
     * Metodo para resolver operación CRUD para la asignación de citas de
     * estudios médicos.
     *
     * @param operacion Identificación operación CRUD.
     * @param equipoImagenologiaNSerie Número de registro del equipo médico.
     * @param estudioidEstudio Número de registro de un estudio médico.
     * @param fecha Fecha y Hora de asignación de estudios médicos.
     * @param fechaPk Identificador de la fecha y hora de asignación de estudios
     * médicos en formato UNIX.
     * @param equipoImagenologiaNSerie_new Número de registro del equipo médico
     * a actualizar en el registro.
     * @param estudioidEstudio_new Número de registro de un estudio médico a
     * actualizar en el registro.
     * @param fechaPk_new Identificador de la fecha y hora de asignación de
     * estudios médicos en formato UNIX a actualizar en el registro.
     * @return boolean Objeto booleano verdadero o falso de acuerdo al resultado
     * de la operación CRUD.
     */
    public boolean ResolveCRUDStudyAssignment(String operacion, String equipoImagenologiaNSerie, String estudioidEstudio, String fecha, String fechaPk, String equipoImagenologiaNSerie_new, String estudioidEstudio_new, String fechaPk_new) {

        //Declaración de variable booleana.
        boolean respcrud = false;

        //Casos para determinar la operación CRUD.
        switch (operacion) {
            //Operación CRUD Crear
            case "CREATE":
                //Se invoca método para crear el registro para la asignación de citas de estudios médicos.
                respcrud = CreateStudyAssignment(equipoImagenologiaNSerie, estudioidEstudio, fechaPk, fecha);
                //Se finaliza la secuencia de instrucción.
                break;
            //Operación CRUD Eliminar.
            case "DELETE":
                //Se invoca método para eliminar el registro para la asignación de citas de estudios médicos.
                respcrud = DeleteStudyAssignment(equipoImagenologiaNSerie, estudioidEstudio, fechaPk);
                //Se finaliza la secuencia de instrucción.
                break;
            //Operación CRUD Eliminar.
            case "UPDATE":
                //Se invoca método para eliminar el registro para la asignación de citas de estudios médicos.
                respcrud = UpdateStudyAssignment(equipoImagenologiaNSerie, estudioidEstudio, fechaPk, equipoImagenologiaNSerie_new, estudioidEstudio_new, fechaPk_new, fecha);
                //Se finaliza la secuencia de instrucción.
                break;
        }

        //Retorno verdadero o falso de acuerdo al resultado de la operación CRUD.
        return respcrud;
    }

    /**
     * Metodo para determinar si un equipo se encuentra en mantenimiento
     *
     * @param noserie Número de registro del equipo médico.
     * @param fecha Fecha para validar si un equipo se encuentra en
     * mantenimiento.
     * @return boolean Objeto booleano verdado o falso si el equipo se encuentra
     * en mantenimiento.
     */
    public boolean getMaintenance(String noserie, String fecha) {

        //Declaración de variable booleana
        boolean resp = false;

        //Definicion de consulta SQL Nativa para obtener las fechas de las solicitudes de estudios agendadas.
        String consultamant = "SELECT * FROM AgendaDeServicio WHERE EquipoImagenologia_NSerie = '" + noserie + "' AND DATE(FechaProgramada) = '" + fecha + "';";

        //Ejecución de un query SQL Nativa controlado vía interfaz NativeQuery
        Query querymant = entityManager.createNativeQuery(consultamant);

        //Validación del resultado query SQL, si es diferente a vacio entra a la sentencia.
        if (!querymant.getResultList().isEmpty()) {

            //Asignación de valor verdadero a la variable resp.
            resp = true; //Mantenimiento
        }

        //Retorno verdadero si hay mantenimiento asignados al equipo y falso lo contrario.
        return resp;
    }

    /**
     * Metodo para obtener los horarios de citas de estudios de manera manual.
     *
     * @param fechaproxima Fecha de la proxima cita medica.
     * @param fechacita Fecha de la cita seleccionada
     * @param idestudio Número de registro de un estudio médico.
     * @param noserie Número de serie del equipo médico.
     * @return
     * @throws java.text.ParseException
     */
    public ArrayList<String> getCalendarManual(String fechaproxima, String fechacita, String idestudio, String noserie) throws ParseException {

        //Se definen ArrayList para guardar los horarios y las citas.
        ArrayList<String> horarios = new ArrayList();
        ArrayList<String> cita = new ArrayList();

        //Declaración de variables String.
        String fechamin = null, fechamax = null, salida = null, limmin = null, limmax = null;

        //Creación de objeto para definir el formato de Fecha y Hora
        DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Creación de objeto para definir el formato de Hora
        DateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
        //Creación de objeto para definir el formato de Fecha
        DateFormat dateformats = new SimpleDateFormat("yyyy-MM-dd");

        //Creación de objeto para manipular fechas y horas.
        Calendar calendar = Calendar.getInstance();

        //Seteo de la fecha de la cita a solicitar
        calendar.setTime(dateformat.parse(fechacita + " 0:00:00"));

        //Validaciín días no laborables Sabado y Domingo.
        if (calendar.get(Calendar.DAY_OF_WEEK) == 7 || calendar.get(Calendar.DAY_OF_WEEK) == 1) {

            //Asignación Salida Día no laborable
            salida = "DiaNoLaborable";

        } else { //Días laborables.

            //Valida si hay mantenimiento correctivo y preventivo
            if (getMaintenanceCorrectivo(noserie) == false && getMaintenancePreventivo(noserie, fechacita) == false) {

                //Se obtiene la fecha actual
                calendar = Calendar.getInstance();

                //Se obtiene la fecha actual.
                String fechaactual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

                //Valida fecha proxima menor a la fecha cita.
                if (fechaproxima.split(" ")[0].compareTo(fechacita) >= 0) {

                    //Seteo de la fecha proxima.
                    calendar.setTime(dateformat.parse(fechaproxima));

                    //Resta 30 minutos a la fecha proxima.
                    calendar.add(Calendar.MINUTE, -30);

                    //Se obtiene fecha máxima como limite
                    fechamax = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

                    //Valida fecha actual menor a las 07:00 de la mañana
                    if ("07:00:00".compareTo(fechaactual.split(" ")[1]) >= 0) {

                        //Definir fecha minima
                        fechamin = fechaactual + " 07:00:00";

                    } else {

                        //Seteo fecha actual
                        calendar.setTime(timeformat.parse(fechaactual.split(" ")[1].split(":")[0] + ":" + fechaactual.split(" ")[1].split(":")[1] + ":00"));

                        /**
                         * Valida los minutos de la hora actual Entre 0 y 7.5
                         * minutos -> 15 - mod(minutos/15) -> Regresa el cuarto
                         * de hora, Entre 7.5 y 15 minutos -> [15 -
                         * mod(minutos/15)] + 15 -> Regresa el cuarto de hora y
                         * le suma 15 minutos más.
                         */
                        calendar.add(Calendar.MINUTE, Integer.parseInt(fechaactual.split(" ")[1].split(":")[1]) % 15 < 8 ? (15 - Integer.parseInt(fechaactual.split(" ")[1].split(":")[1]) % 15) : ((15 - Integer.parseInt(fechaactual.split(" ")[1].split(":")[1]) % 15) + 15));

                        //Asignación de intervalo de tiempo minimo para realizar la consulta de horarios asignados.
                        fechamin = fechaactual.split(" ")[0] + " " + new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());

                    }

                    //Validación de la fecha cita solicitada con los intervalos de tiempo mínimo y máximo.
                    if ((fechamin.split(" ")[0].compareTo(fechacita) <= 0) && (fechamax.split(" ")[0].compareTo(fechacita) >= 0)) {

                        //Si la fecha minimo es igual a la fecha actual
                        if (fechamin.split(" ")[0].compareTo(fechacita) == 0) {
                            limmin = fechamin;
                            limmax = fechamin.split(" ")[0] + " 20:45:00";
                        } else if (fechamax.split(" ")[0].compareTo(fechacita) == 0) { //Si la fecha maxima es igual a la fecha actual
                            limmin = fechamin.split(" ")[0] + " 07:00:00";
                            limmax = fechamax;
                        } else { //Fecha intermedia
                            limmin = fechacita + " 07:00:00";
                            limmax = fechacita + " 20:45:00";
                        }
                    }

                    //Valida limite mínimo y máximo
                    if (limmin.compareTo(limmax) <= 0) {

                        //Definicion de consulta SQL Nativa para obtener los horarios agendados.
                        String consultahorarios = "SELECT DATE_FORMAT(Fecha,\"%T\"), Fecha FROM AsignacionEstudio\n"
                                + "WHERE Fecha BETWEEN '" + limmin + "' AND '" + limmax + "'\n"
                                + "AND EquipoImagenologia_NSerie = '" + noserie + "'\n"
                                + "AND Estudio_idEstudio = '" + idestudio + "'\n"
                                + "ORDER BY Fecha ASC;";

                        //Ejecución de un query SQL Nativa controlado vía interfaz NativeQuery.
                        Query queryconsulta = entityManager.createNativeQuery(consultahorarios);

                        //Recolección de los horarios de las citas agendadas.
                        Collection<Object[]> horarioscitas = queryconsulta.getResultList();

                        //Declaración de contador de citas.
                        int contcitas = 0;

                        //Inicialización de iterador de fecha, por el intervalo minimo de fecha.
                        String fechaiterador = limmin.split(" ")[1];

                        //Ciclo While de iteracción de hora en el intervalo de consulta.
                        while (limmax.split(" ")[1].compareTo(fechaiterador) >= 0) {

                            //Exclución de hora de receso.
                            if (("15:00:00".compareTo(fechaiterador) <= 0) || ("13:45:00".compareTo(fechaiterador) >= 0)) {

                                //Declaración de contador de horas iteradas.
                                int contfecha = 0;

                                //Ciclo for para obtener las horas de la consulta.
                                for (Object[] columna : horarioscitas) {
                                    contfecha = (columna[0].equals(fechaiterador) ? contfecha + 1 : contfecha);//En caso de coincidir una hora de la consulta y la iterada, incrementa en uno el contador de hora, caso contrario no incrementa.
                                }
                                //Si no hay coincidencia de la hora iterada y alguna hora de la consulta, para agregar la hora iterada al json.
                                if (contfecha == 0) {

                                    cita.clear();
                                    cita.add(fechaiterador.split(":")[0] + ":" + fechaiterador.split(":")[1]);
                                    horarios.addAll(cita);

                                    //Incremento de contador de citas.
                                    contcitas = contcitas + 1;

                                }
                            }

                            //Seteo de la hora iterada.
                            calendar.setTime(timeformat.parse(fechaiterador));

                            //Incremento de 15 minutos al iterador de hora.
                            calendar.add(Calendar.MINUTE, 15);

                            //Asignación de la hora iterada + 15 minutos.
                            fechaiterador = new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());

                        }

                        //En caso de que no haya ningun hora disponible
                        if (contcitas == 0) {

                            //Asignación Salida Sin Citas
                            salida = "SinCitas";

                        }

                    } else {

                        //Asignación Fuera de Horario
                        salida = "FueraDeHorario";
                    }

                } else {

                    //Asignación Salida Fuera de Horario
                    salida = "FueraDeHorario";
                }

            } else {

                //Asignación Salida Mantenimiento de Equipo
                salida = "MantenimientoDeEquipo";

            }

        }

        //Valida cadena tring salida
        if (salida != null) {

            //Limpiar array cita
            cita.clear();

            //Agrega cadena string mensaje salida
            cita.add("salida;" + salida);

            //Agrega cadena string cita en array horarios
            horarios.addAll(cita);

        }

        //Retorno array horarios
        return horarios;

    }

    /**
     * Metodo para obtener los horarios de citas de estudios de manera
     * automatica.
     *
     * @param fechaproxima Fecha de la proxima cita medica.
     * @param tipo_paciente Tipo de paciente Foraneo/Local.
     * @param idestudio Número de registro de un estudio médico.
     * @return
     * @throws java.text.ParseException
     */
    public ArrayList<String> getCalendarAutomatic(String fechaproxima, String tipo_paciente, String idestudio) throws ParseException {

        //Se definen ArrayList para guardar los horarios y las citas.
        ArrayList<String> horarios = new ArrayList();
        ArrayList<String> cita = new ArrayList();

        //Declaración de variables String.
        String fechamin = null, fechamax = null, salida = null;

        //Creación de objeto para definir el formato de Fecha y Hora
        DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Creación de objeto para definir el formato de Hora
        DateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
        //Creación de objeto para definir el formato de Fecha
        DateFormat dateformats = new SimpleDateFormat("yyyy-MM-dd");

        //Creación de objeto para manipular fechas y horas.
        Calendar calendar = Calendar.getInstance();

        //Se obtiene la fecha actual.
        String fechaactual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        //Valida horario de servicio de una cita
        if (("09:00:00".compareTo(fechaproxima.split(" ")[1]) <= 0) && ("17:45:00".compareTo(fechaproxima.split(" ")[1]) >= 0)) {

            //Valida horario y tipo de paciente
            if (("09:00:00".compareTo(fechaproxima.split(" ")[1]) == 0) && (tipo_paciente.compareTo("local") == 0)) {

                //Seteo de la fecha proxima.
                calendar.setTime(dateformat.parse(fechaproxima));

                //Resta 1 dia a la fecha proxima.
                calendar.add(Calendar.DAY_OF_YEAR, -1);

                //Se define fecha max
                fechamax = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()) + " 20:45:00";

                //Seteo de la fecha max.
                calendar.setTime(dateformat.parse(fechamax));

                //Resta 7 días a la fecha proxima.
                calendar.add(Calendar.DAY_OF_YEAR, -7);

                //Se obtiene fecha minima
                fechamin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

            } else {

                //Seteo de la fecha proxima.
                calendar.setTime(dateformat.parse(fechaproxima));

                //Valida tipo paciente
                switch (tipo_paciente) {
                    case "Local":

                        //Resta 135 minutos a la hora de la fecha proxima cita
                        calendar.add(Calendar.MINUTE, -135);

                        //Se obtiene fecha maxima
                        fechamax = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

                        //Seteo de la fecha proxima.
                        calendar.setTime(dateformat.parse(fechamax));

                        //Resta 7 días a la fecha proxima.
                        calendar.add(Calendar.DAY_OF_YEAR, -7);

                        //Obtener fecha minima
                        fechamin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

                        break;
                    case "Foraneo":

                        //Resta 15 minutos a la hora de la fecha proxima cita
                        calendar.add(Calendar.MINUTE, -15);

                        //Se obtiene fecha maxima
                        fechamax = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

                        //Obtener fecha minima
                        fechamin = fechamax.split(" ")[0] + " 07:00:00";

                        break;
                }
            }
        }

        //Valida que la fecha actual coincida con la fecha proxima cita
        if ((fechaactual.split(" ")[0]).compareTo(fechaproxima.split(" ")[0]) == 0) {

            //Valida fecha actual menor a las 06:45:00
            if ("06:45:00".compareTo(fechaactual.split(" ")[1]) > 0) {

                //Define limite fecha minima
                fechamin = fechaactual.split(" ")[0] + " 07:00:00";

                //Valida tipo de paciente
                switch (tipo_paciente) {
                    case "Foraneo":

                        //Seteo de la fecha proxima.
                        calendar.setTime(dateformat.parse(fechaproxima));

                        //Resta 15 minutos a la hora de la fecha proxima cita
                        calendar.add(Calendar.MINUTE, -15);

                        //Se obtiene fecha maxima
                        fechamax = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

                        break;
                    case "Local":

                        //Seteo de la fecha proxima.
                        calendar.setTime(dateformat.parse(fechaproxima));

                        //Resta 135 minutos a la hora de la fecha proxima cita
                        calendar.add(Calendar.MINUTE, -135);

                        //Se obtiene fecha maxima
                        fechamax = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

                        break;
                }

            } else if ((("06:45:00".compareTo(fechaactual.split(" ")[1]) <= 0) && ("15:22:30".compareTo(fechaactual.split(" ")[1]) >= 0)) && tipo_paciente.compareTo("Local") == 0) { //LOCAL

                //Seteo de la hora actual.
                calendar.setTime(timeformat.parse(fechaactual.split(" ")[1].split(":")[0] + ":" + fechaactual.split(" ")[1].split(":")[1] + ":00"));

                /**
                 * Valida los minutos de la hora actual Entre 0 y 7.5 minutos ->
                 * 15 - mod(minutos/15) -> Regresa el cuarto de hora, Entre 7.5
                 * y 15 minutos -> [15 - mod(minutos/15)] + 15 -> Regresa el
                 * cuarto de hora y le suma 15 minutos más.
                 */
                calendar.add(Calendar.MINUTE, Integer.parseInt(fechaactual.split(" ")[1].split(":")[1]) % 15 < 8 ? (15 - Integer.parseInt(fechaactual.split(" ")[1].split(":")[1]) % 15) : ((15 - Integer.parseInt(fechaactual.split(" ")[1].split(":")[1]) % 15) + 15));

                //Asignación de intervalo de tiempo minimo para realizar la consulta de horarios asignados.
                fechamin = fechaactual.split(" ")[0] + " " + new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());

                //Seteo de la fecha proxima.
                calendar.setTime(dateformat.parse(fechaproxima));

                //Resta 135 minutos a la hora de la fecha proxima cita
                calendar.add(Calendar.MINUTE, -135);

                //Se obtiene fecha maxima
                fechamax = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

                //Seteo de la fecha proxima.
                calendar.setTime(dateformat.parse(fechamax));

            } else if ((("06:45:00".compareTo(fechaactual.split(" ")[1]) <= 0) && ("17:07:30".compareTo(fechaactual.split(" ")[1]) >= 0)) && tipo_paciente.compareTo("Foraneo") == 0) {

                //Seteo de la hora actual.
                calendar.setTime(timeformat.parse(fechaactual.split(" ")[1].split(":")[0] + ":" + fechaactual.split(" ")[1].split(":")[1] + ":00"));

                /**
                 * Valida los minutos de la hora actual Entre 0 y 7.5 minutos ->
                 * 15 - mod(minutos/15) -> Regresa el cuarto de hora, Entre 7.5
                 * y 15 minutos -> [15 - mod(minutos/15)] + 15 -> Regresa el
                 * cuarto de hora y le suma 15 minutos más.
                 */
                calendar.add(Calendar.MINUTE, Integer.parseInt(fechaactual.split(" ")[1].split(":")[1]) % 15 < 8 ? (15 - Integer.parseInt(fechaactual.split(" ")[1].split(":")[1]) % 15) : ((15 - Integer.parseInt(fechaactual.split(" ")[1].split(":")[1]) % 15) + 15));

                //Asignación de intervalo de tiempo minimo para realizar la consulta de horarios asignados.
                fechamin = fechaactual.split(" ")[0] + " " + new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());

                //Seteo de la fecha proxima.
                calendar.setTime(dateformat.parse(fechaproxima));

                //Resta 15 minutos a la hora de la fecha proxima cita
                calendar.add(Calendar.MINUTE, -15);

                //Se obtiene fecha maxima
                fechamax = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

                //Seteo de la fecha proxima.
                calendar.setTime(dateformat.parse(fechamax));

            }

        }

        //Se identifican los dispositivos correspondientes al estudio.
        Collection<Object[]> listdevice = getDeviceStudies(idestudio);

        //Se definen ArrayList para el numero de serie y sala de un equipo
        ArrayList<String> equipos_serie = new ArrayList();
        ArrayList<String> equipos_salas = new ArrayList();

        //Valida lista de equipo referentes a un estudio
        if (!listdevice.isEmpty()) {

            //Se identifican los equipos y salas disponibles.
            for (Object[] columna : listdevice) {

                System.out.println(columna[4] + " : " + getMaintenanceCorrectivo((String) columna[4]));

                //Valida que no haya equipos en mantenimiento correctivo
                if (getMaintenanceCorrectivo((String) columna[4]) == false) {

                    //Agrega el numero de serie y salas de un equipo
                    equipos_serie.add((String) columna[4]);
                    equipos_salas.add((String) columna[6]);

                }
            }

            //Se valida que la fecha min sea menor a la fecha max
            if (fechamin.compareTo(fechamax) <= 0) {

                //Se declara la fecha temporal con la fecha maxima
                String fechatmp = fechamax.split(" ")[0];

                //Se declara arreglo para guardar las fechas de las citas posibles.
                ArrayList<String> fechas_citas = new ArrayList();

                //Se identifica las fechas posibles para agendar citas.
                while (((fechamin.split(" ")[0]).compareTo(fechatmp) <= 0) && ((fechamax.split(" ")[0]).compareTo(fechatmp) >= 0)) {

                    //Seteo de la fecha temporal.
                    calendar.setTime(dateformats.parse(fechatmp));

                    //Valida dias no laborables y agrega las fechas laborables a un array de fechas de cita.
                    if (calendar.get(Calendar.DAY_OF_WEEK) != 7 && calendar.get(Calendar.DAY_OF_WEEK) != 1) {
                        fechas_citas.add(fechatmp);
                    }

                    //Seteo de la fecha temporal.
                    calendar.setTime(dateformats.parse(fechatmp));

                    //Se resta un dia
                    calendar.add(Calendar.DAY_OF_YEAR, -1);

                    //Se da formato a la fecha temporal
                    fechatmp = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

                }

                //Se asigna la fecha maxima en temporal
                fechatmp = fechamax;

                //Se definen ArrayList para el numero de serie y sala de un equipo disponibles para la asignacion automatica
                ArrayList<String> equipos_serie_disponibles = new ArrayList();
                ArrayList<String> equipos_salas_disponibles = new ArrayList();

                //Definicion de contadores
                int contador_opciones = 0, contador_disponibles = 0, contador_fecha_disp = 0;

                //Determina las fechas disponibles
                if (fechas_citas.get(contador_fecha_disp).compareTo(fechamax.split(" ")[0]) != 0) {
                    fechatmp = fechas_citas.get(contador_fecha_disp) + " 21:00:00";
                } else if (("14:00:00".compareTo(fechas_citas.get(contador_fecha_disp).split(" ")[0]) <= 0) && ("15:00:00".compareTo(fechas_citas.get(contador_fecha_disp).split(" ")[0]) >= 0)) {
                    fechatmp = fechas_citas.get(contador_fecha_disp) + " 13:45:00";
                }

                //Determina las series y salas disponibles de los equipos con disponibilidad
                for (int contador = 0; contador < equipos_serie.size(); contador++) {
                    if (getMaintenancePreventivo(equipos_serie.get(contador), fechatmp.split(" ")[0]) == false) {
                        equipos_serie_disponibles.add(equipos_serie.get(contador));
                        equipos_salas_disponibles.add(equipos_salas.get(contador));
                    }
                }

                //Seteo de la fecha temporal.
                calendar.setTime(dateformat.parse(fechatmp));

                //Se resta 15 minutos
                calendar.add(Calendar.MINUTE, -15);

                //Obtiene la fecha tmp para ser iterada
                fechatmp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

                //Valida que el contador de opciones sea menos a 3 y que la fecha minima sea menor que la temporal a iterar
                while (contador_opciones < 3 && fechamin.compareTo(fechatmp) < 0) {

                    //Valida que la hora de la fecha temporal sea igual a las 06:45 para restar un dia
                    if (fechatmp.split(" ")[1].compareTo("06:45:00") == 0) {

                        //Se inicializa el contador de equipos disponibles
                        contador_disponibles = 0;

                        //Incrementa el contador de fechas disponibles
                        contador_fecha_disp = contador_fecha_disp + 1;

                        //Obtiene la fecha temporal
                        fechatmp = fechas_citas.get(contador_fecha_disp) + "20:45:00";

                        //Limpiar los array de las series y salas de los equipos
                        equipos_serie_disponibles.clear();
                        equipos_salas_disponibles.clear();

                        //Se obtiene las series y salas de los equipos disponibles, se guardan en un array
                        for (int contador = 0; contador < equipos_serie.size(); contador++) {
                            if (getMaintenancePreventivo(equipos_serie.get(contador), fechatmp.split(" ")[0]) == false) {
                                equipos_serie_disponibles.add(equipos_serie.get(contador));
                                equipos_salas_disponibles.add(equipos_serie.get(contador));
                            }
                        }
                    }

                    //Validacion disponibilidad de los equipos
                    if (contador_disponibles >= equipos_serie_disponibles.size() && fechatmp.split(" ")[1].compareTo("06:45:00") != 0) {

                        //Seteo de la fecha temporal.
                        calendar.setTime(dateformat.parse(fechatmp));

                        //Se resta 15 minutos
                        calendar.add(Calendar.MINUTE, -15);

                        fechatmp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

                        //Se excluye el receso.
                        if (("14:00:00".compareTo(fechatmp.split(" ")[1]) <= 0) && ("15:00:00".compareTo(fechatmp.split(" ")[1]) >= 0)) {
                            fechatmp = fechas_citas.get(contador_fecha_disp) + " 13:45:00";
                        }

                        //Se inicializa el contador de equipos disponibles
                        contador_disponibles = 0;

                    } else {

                        //Definicion de consulta SQL Nativa para obtener los horarios agendados.
                        String consultaagenda = "SELECT *\n"
                                + "FROM AsignacionEstudio\n"
                                + "WHERE EquipoImagenologia_NSerie = '" + equipos_serie_disponibles.get(contador_disponibles) + "'\n"
                                + "AND Fecha = '" + fechatmp + "';";

                        //Ejecución de un query SQL Nativa controlado vía interfaz NativeQuery.
                        Query queryagenda = entityManager.createNativeQuery(consultaagenda);

                        System.out.println(consultaagenda);

                        //Valida si existen horarios agendados.
                        if (queryagenda.getResultList().isEmpty()) {

                            //Limpiar el array para guardar las citas
                            cita.clear();

                            //Agrega el primer fecha, serie, sala, horario y turno
                            cita.add(fechatmp.split(" ")[0] + ";" + fechatmp.split(" ")[1] + ";" + equipos_salas_disponibles.get(contador_disponibles) + ";" + equipos_serie_disponibles.get(contador_disponibles) + ";" + ("13:45:00".compareTo(fechatmp.split(" ")[1]) >= 0 ? "Matutino" : "Vespertino"));
                            horarios.addAll(cita);

                            //Se incrementa el contador de opciones
                            contador_opciones = contador_opciones + 1;

                        }

                        //Se incrementa el contador de equipos disponibles
                        contador_disponibles = contador_disponibles + 1;

                    }
                }

                //Valida el contador de opciones igual a cero
                if (contador_opciones == 0) {

                    //Asignación Salida no hay disponibilidad de citas
                    salida = "No hay citas disponibles";

                }

            } else if (fechamin.compareTo(fechamax) >= 0) {

                //Asignación Salida Fuera de Horario
                salida = "FueraDeHorario";

            }
        } else {

            //Asignación Salida No hay Equipos
            salida = "SinEquipos";

        }

        //Valida cadena string salida
        if (salida != null) {

            //Limpiar array cita
            cita.clear();

            //Agrega cadena string mensaje salida
            cita.add("salida;" + salida);

            //Agrega cadena string cita en array horarios
            horarios.addAll(cita);
        }

        //Retorno array horarios
        return horarios;

    }

    /**
     * Metodo para determinar si un equipo se encuentra en mantenimiento
     * preventivo
     *
     * @param noserie Número de registro del equipo médico.
     * @param fecha Fecha para validar si un equipo se encuentra en
     * mantenimiento.
     * @return boolean Objeto booleano verdado o falso si el equipo se encuentra
     * en mantenimiento.
     */
    public boolean getMaintenancePreventivo(String noserie, String fecha) {

        //Declaración de variable booleana
        boolean resp = false;

        //Definicion de consulta SQL Nativa para obtener las fechas de las solicitudes de estudios agendadas.
        String consultamant = "SELECT * FROM AgendaDeServicio "
                + "WHERE TipoMantenimiento = 'Preventivo' "
                + "AND EstadoDeManto = 'Programado' "
                + "AND EquipoImagenologia_NSerie = '" + noserie + "' "
                + "AND FechaControl LIKE '%" + fecha + "%';";

        //Ejecución de un query SQL Nativa controlado vía interfaz NativeQuery
        Query querymant = entityManager.createNativeQuery(consultamant);

        //Validación del resultado query SQL, si es diferente a vacio entra a la sentencia.
        if (!querymant.getResultList().isEmpty()) {

            //Asignación de valor verdadero a la variable resp.
            resp = true; //Mantenimiento
        }

        //Retorno verdadero si hay mantenimiento asignados al equipo y falso lo contrario.
        return resp;
    }

    /**
     * Metodo para determinar si un equipo se encuentra en mantenimiento
     * correctivo.
     *
     * @param noserie Número de registro del equipo médico.
     * @return boolean Objeto booleano verdado o falso si el equipo se encuentra
     * en mantenimiento.
     */
    public boolean getMaintenanceCorrectivo(String noserie) {

        //Declaración de variable booleana
        boolean resp = false;

        //Definicion de consulta SQL Nativa para obtener las fechas de las solicitudes de estudios agendadas.        
        String consultamant = "SELECT *\n"
                + "FROM AgendaDeServicio\n"
                + "WHERE EquipoImagenologia_NSerie = '" + noserie + "'\n"
                + "AND TipoMantenimiento = 'Correctivo'\n"
                + "AND EstadoDeManto = 'Programado';";

        //Ejecución de un query SQL Nativa controlado vía interfaz NativeQuery
        Query querymant = entityManager.createNativeQuery(consultamant);

        //Validación del resultado query SQL, si es diferente a vacio entra a la sentencia.
        if (!querymant.getResultList().isEmpty()) {

            //Asignación de valor verdadero a la variable resp.
            resp = true; //Mantenimiento
        }

        //Retorno verdadero si hay mantenimiento asignados al equipo y falso lo contrario.
        return resp;
    }

    /**
     * Metodo para determinar los equipos medicos relacionados a cada estudio.
     *
     * @param idestudio Número de registro de un estudio médico.
     * @return Collection<Object[]> Una colección de objetos con los datos de
     * los equipos medicos relacionados a un estudio médico.
     */
    public Collection<Object[]> getDeviceStudies(String idestudio) {

        //Definicion de consulta SQL Nativa para obtener los datos de una cita de estudios agendada.
        String consultasms = "SELECT *\n"
                + "FROM Estudio es\n"
                + "JOIN Equipo eq\n"
                + "ON eq.AreaDeServicio_idArea = es.AreaDeServicio_idArea\n"
                + "WHERE es.idEstudio = " + idestudio + ";";

        //Impresión del query SQL.
        System.out.println("Query: " + consultasms);

        //Ejecución de un query SQL Nativa controlado vía interfaz NativeQuery.
        Query query = entityManager.createNativeQuery(consultasms);

        //Retorno el resultado de la consulta SQL Nativa.
        return query.getResultList();

    }

    public static void main(String args[]) throws ParseException {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PesitenceREST");
        EntityManager entityManager = emf.createEntityManager();

        AppointmentManager test = new AppointmentManager();
        test.setEntityManager(entityManager);

        //System.out.println(test.getCalendarManual("2021-07-23 10:00:00","EQUIPOPRUEBAA2"));
        //System.out.println(test.getCalendarManual("2021-07-23 10:00:00","2021-09-06","1","EQUIPOPRUEBAA1"));
        //System.out.println(test.getCalendarManual("2021-09-07 13:00:00","2021-09-06","1","EQUIPOPRUEBAA1"));
        //System.out.println(test.getCalendarManual("2021-09-07 12:00:00","2021-09-07","1","EQUIPOPRUEBAB2"));
        //System.out.println(test.getCalendarManual("2021-09-15 13:00:00","2021-09-13","1","EQUIPOPRUEBAA1"));
        String fechamin = "2021-09-09 10:00:00";
        String fechamax = "2021-09-09 11:00:00";

        if (fechamin.compareTo(fechamax) <= 0) {
            System.out.println("fechamin < fechamax");
        } else if (fechamin.compareTo(fechamax) >= 0) {
            System.out.println("fechamax < fechamin");
        }

        fechamax = "2021-09-09 09:00:00";

        if (fechamin.compareTo(fechamax) <= 0) {
            System.out.println("fechamin < fechamax");
        } else if (fechamin.compareTo(fechamax) >= 0) {
            System.out.println("fechamax < fechamin");
        }

        System.out.println(test.getCalendarAutomatic("2021-09-16 11:00:00", "Foraneo", "1"));

        String fecha = "13:45:00";

        //if(("14:00:00".compareTo(fecha) <= 0) && ("15:00:00".compareTo(fecha) >= 0))
        if (("14:00:00".compareTo(fecha) <= 0) && ("15:00:00".compareTo(fecha) >= 0)) {
            System.out.println("Receso");
        }
        //String fechaactual = "2021-09-07 11:01:00";

        /*test.getcalendar1("EQUIPOPRUEBAA1","2021-07-21 07:30:00","foraneo","2");
        System.out.println(test.getCalendarAutomatic("2021-07-22 10:30:00","Local","2"));
        System.out.println(test.getMaintenancePreventivo("EQUIPOPRUEBAA1","2021-07-22"));
        System.out.println(test.getMaintenancePreventivo("EQUIPOPRUEBAA1","2021-07-23"));
*/
    }
}
