//Definición de URIs

var jsonsolicitudes, jsonagenda, jsonopcauto;
var uriserv="/RISSERVER/rest/USRSesionRST";
var host = "http://" + location.host + "/RISSERVER/";
var jsonhorarios = new Object();
var jsonhorariosautomatica = new Object();

var JSONStudyAssignmentDelete = new Object(), JSONStudyControlUpdateClosed = new Object(), JSONStudyControlCreate = new Object(), JSONStudyRequestCreate = new Object(), JSONStudyAssignmentCreate = new Object(), JSONStudyAssignmentUpdate = new Object(), usuario = new Object(), estado_agenda, tipo_asignacion, createagendacont = 0;
var dialogoPerfiles;   
            
//Coordinador del Servicio
PERFIL = [
    {
        "noempleado" : "102",
        "usuarioid" : "pabhernandez",
        "curp" : "PABLO101",
        "nombre" : "Pablo",
        "apellidopaterno" : "Hernandez",
        "apellidomaterno" : "Lopez",
        "rol" : "CoordinadordelServicio",
        "idarea" : "1",
        "nombrearea" : "Rayos X"
    }
];

            USRS =
                    [
                        {"Id_tbl":"1","fecha": "2020-01-31","userEmail": "Mar@correo.com", "userName": "DVG2", "userPwd": "dvg", "ID": "USR_ADM" },
                        {"Id_tbl":"2","fecha": "2020-01-31","userEmail": "fer@correo.com", "userName": "FER", "userPwd": "fernaco", "ID": "USR_CHF"},
                        {"Id_tbl":"3","fecha": "2020-01-31","userEmail": "ign@inr.gob.mx", "userName": "IGN", "userPwd": "ignacio", "ID": "USR_REC"},
                        {"Id_tbl":"4","fecha": "2020-01-31","userEmail": "RAM@correo.com", "userName": "RAM", "userPwd": "RAM", "ID": "USR_APP"},
                        {"Id_tbl":"5","fecha": "2020-01-31","userEmail": "ham@correo.com", "userName": "HAM", "userPwd": "ham", "ID": "USR_APP"},
                        {"Id_tbl":"6","fecha": "2020-01-31","userEmail": "LAU@correo.com", "userName": "LAU", "userPwd": "LAU", "ID": "USR_APP"}
                    ];


            dummy =
                    [
                        {"Id_tbl":"1","fecha": "2020-01-31", "ID": "USR_ADM", "descripción": "Control total", "Tipo": "1", "FSM": "ADM_FSM.json"},
                        {"Id_tbl":"2","fecha": "2020-01-30", "ID": "USR_CHF", "descripción": "Jefe de Servicio ", "Tipo": "2", "FSM": "CHF_FSM.json"},
                        {"Id_tbl":"3","fecha": "2020-01-29", "ID": "USR_REC", "descripción": "Recepcionista", "Tipo": "3", "FSM": "REC_FSM.json"},
						{"Id_tbl":"4","fecha": "2020-01-29", "ID": "USR_APP", "descripción": "Médico Especialista", "Tipo": "4", "FSM": "ESP_FSM.json"}
                    ];


/**
 * Función para activar eventos.
 */

function getval(cel, tablename) {
    
    //Se obtiene el indice de un renglon
    var rowId = cel.parentNode.rowIndex;
    
    //Se excluye la cabecera de columna de una tabla
    if (rowId > 0) {
        
        const cellsOfRow = getRowCells(rowId, tablename);
        
        //Valida el nombre de la tabla.
        switch(tablename){
            case "tblperfiles":
                document.querySelector("#rowid").value = rowId;
                document.querySelector("#tblid").value = tablename;

                document.getElementById("profileid").innerHTML = cellsOfRow[0].innerHTML;
                document.getElementById("fechacreacion").innerHTML = cellsOfRow[1].innerHTML;
                document.getElementById("desc").value = cellsOfRow[2].innerHTML;
                document.getElementById("nom").value = cellsOfRow[3].innerHTML;

                botonguardar = document.getElementById("guardarPERFILES");
                botonguardar.style.display ="block";
                //botonguardar.style.visibility = "visible";

                botonagregar = document.getElementById("agregarPERFILES");
                botonagregar.style.display ="none";
                //botonagregar.style.visibility = "hidden";
                /*
                var integertipo = parseInt(cellsOfRow[3].innerHTML, 10);
                document.getElementsByName("tipo")[integertipo - 1].checked = true;
		*/

                dialogoPerfiles.style.display = "block";//activar dialogo modal
                            
            break;
            case "tblusuarios":
                console.log("Editando usuarios");
                document.querySelector("#USRrowid").value = rowId;
                document.querySelector("#USRtblid").value = tablename;
                            
                document.getElementById("uname").value = cellsOfRow[3].innerHTML;
                document.getElementById("email").value = cellsOfRow[2].innerHTML;
                            
                //document.getElementById("perfil").value = cellsOfRow[5].innerHTML;
                            
                setSelectedIndex(document.getElementById("perf2"), cellsOfRow[5].innerHTML); //poner en  el listbox el dato del englon seleccionado
                document.getElementById("perfil").innerHTML="Activo: "+cellsOfRow[5].innerHTML;
                            
                /*
                var integertipo = parseInt(cellsOfRow[4].innerHTML, 10);
                document.getElementsByName("estado")[integertipo - 1].checked = true;
							*/
                dialogoUsuarios = document.querySelector(".modalUSUARIOS");
                dialogoUsuarios.style.display = "block";//activar dialogo modal, cristal transparente  
                botonguardar = document.getElementById("guardarUSUARIOS");
                botonguardar.style.display ="block";
                //botonguardar.style.visibility = "hidden";

                botonagregar = document.getElementById("agregarUSUARIOS");
                botonagregar.style.display ="none";
                perfilupdate = document.getElementById("perfilupdate");
                perfilupdate.style.display ="block";

                           
            break;
            case "tblagenda":
                            
                //Se limpia el div con el id de elemento nameDivContainer.
                divContainer = html_GetElement("showDataHorarios");
                
                //Se vacia el elemento con id nameDivContainer
                divContainer.innerHTML = "";
                
                //Validacion para activar boton para agendar manual una agenda.
                if(document.querySelector("#manualAGENDA").style.display === "none")
                    document.querySelector("#manualAGENDA").style.display = "inline";
                
                //Se asigna estado de una agenda.
                estado_agenda = "Reprogramado";
                
                //Se llenan valores de la información de la cita en la vista.
                document.getElementById("agendanombrepaciente").value = jsonagenda[rowId-1]["paciente"];
                document.getElementById("agendadomicilio").value = jsonagenda[rowId-1]["domicilio"];
                document.getElementById("agendaareaprocedencia").value = jsonagenda[rowId-1]["areaprocedencia"];
                document.getElementById("agendatipopaciente").value = determina_tipo_paciente(jsonagenda[rowId-1]["tiempotraslado"]);
                document.getElementById("agendafechaproxima").value = jsonagenda[rowId-1]["fechaproxima"].split(" ")[0];
                document.getElementById("agendahoraproxima").value = (jsonagenda[rowId-1]["fechaproxima"].split(" ")[1]).split(".")[0];
                document.getElementById("agendaestudio").value = jsonagenda[rowId-1]["estudio"];
                
                //Se invoca función para setear fecha actual en calendario.
                seteofechaactual("agendafecha");
                
                //Se forma json JSONStudyControlCreate para agregar registros de la tabla ControlEstudios
                JSONStudyControlCreate.noempleado = PERFIL[0]["noempleado"];
                JSONStudyControlCreate.curp = PERFIL[0]["curp"];
                JSONStudyControlCreate.idpaciente = jsonagenda[rowId-1]["idpaciente"];
                JSONStudyControlCreate.idestudio = jsonagenda[rowId-1]["idestudio"];
                JSONStudyControlCreate.estado = estado_agenda;
                JSONStudyControlCreate.cerrado = "false";
                JSONStudyControlCreate.observaciones = jsonagenda[rowId-1]["observaciones"];
                
                //Se forma json JSONStudyRequestCreate para agregar registros de la tabla SolicitudDeEstudio
                JSONStudyRequestCreate.idpaciente = jsonagenda[rowId-1]["idpaciente"];
                JSONStudyRequestCreate.noempleado = jsonagenda[rowId-1]["noempleado"];
                JSONStudyRequestCreate.curp = jsonagenda[rowId-1]["curp"];
                JSONStudyRequestCreate.areaprocedencia = jsonagenda[rowId-1]["areaprocedencia"];
                JSONStudyRequestCreate.fechaproxima = jsonagenda[rowId-1]["fechaproxima"].split(".")[0];
                JSONStudyRequestCreate.diagnostico = jsonagenda[rowId-1]["diagnostico"];
                JSONStudyRequestCreate.estado = estado_agenda;
                JSONStudyRequestCreate.observaciones = jsonagenda[rowId-1]["observaciones"];
                            
                //Se forma json JSONStudyControlUpdateClosed para actualizar registros de la tabla ControlEstudios
                JSONStudyControlUpdateClosed.noempleado = jsonagenda[rowId-1]["noempleadousuario"];
                JSONStudyControlUpdateClosed.curp = jsonagenda[rowId-1]["curpempleado"];
                JSONStudyControlUpdateClosed.idpaciente = jsonagenda[rowId-1]["idpaciente"];
                JSONStudyControlUpdateClosed.idestudio = jsonagenda[rowId-1]["idestudio"];
                JSONStudyControlUpdateClosed.fechacontrolpk = jsonagenda[rowId-1]["fechasolicitudpk"];
                JSONStudyControlUpdateClosed.fechacontrol = jsonagenda[rowId-1]["fechasolicitud"].split(".")[0];
                JSONStudyControlUpdateClosed.cerrado = "true";
                            
                //Se forma json JSONStudyAssignmentUpdate para actualizar registros de la tabla AsignacionEstudio            
                JSONStudyAssignmentUpdate.noserie = jsonagenda[rowId-1]["noserie"];
                JSONStudyAssignmentUpdate.idestudio = jsonagenda[rowId-1]["idestudio"];
                JSONStudyAssignmentUpdate.fechapk = jsonagenda[rowId-1]["fechaasignpk"];
                JSONStudyAssignmentUpdate.idestudio_new = jsonagenda[rowId-1]["idestudio"];
                
                //Creación de Objecto para agregar elemento de id de área.
                var jsondevice = new Object();
                                
                //Se agrega elemento de id de area de acuerdo al estudio seleccionado.
                jsondevice.idestudio = jsonagenda[rowId-1]["idestudio"];
                
                //Se invoca función para llenar realizar petición al servicio REST para obtener las salas/equipos y llenar el listbox.
                RESTObtenerListBox( uriserv + "/getDevice","consultaagendasala", "salaequipo", "noserie",jsondevice,"POST");
                
                //Se activa el modal para agendar una cita
                document.querySelector(".modalAGENDAR").style.display = "block";
                
                //Se valida el rol del usuario JefedelServicio
                if(PERFIL[0]["rol"] == "JefedelServicio"){
                
                    //Se oculta el contenedor para mostrar horarios automaticos.
                    document.querySelector("#showDataHorarios").style.display = "none";
                    
                    //Se activa el contenedor de elementos para agendar una cita manual.
                    document.querySelector("#contenedor-manual").style.display = "block";
                    
                    //Se desactiva el formulario de hora manual.
                    document.querySelector("#manual_hora").style.display = "none";
                    
                    //Se activa el formulario de sala para el jefe de servicio.
                    document.querySelector("#manual_sala").style.display = "block";
                    
                    //Se activa el formulario de fecha para el jefe de servicio.
                    document.querySelector("#manual_fecha").style.display = "block";
                    
                    //Se activa el formulario de hora para el jefe de servicio.
                    document.querySelector("#manual_horajefe").style.display = "block";
                    
                    //Se oculta el boton para activar la opción para agendar una cita manual.
                    document.querySelector("#manualAGENDA").style.display = "none";
                    
                    //Se limpia el formulario de hora para el jefe de servicio.
                    document.querySelector("#agendahorajefe").value = "";
                                
                    //Se asigna valor al tipo de asignacion
                    tipo_asignacion = "manualjefe";
                                
                }else{
                    
                    //Se obtiene el dato del formulario con id noregistro y se insertar en un elemento del objeto.
                    jsonhorariosautomatica.fechaproxima = jsonagenda[rowId-1]["fechaproxima"].split(".")[0];
                    jsonhorariosautomatica.tipo_paciente = determina_tipo_paciente(jsonagenda[rowId-1]["tiempotraslado"]);
                    jsonhorariosautomatica.idestudio = jsonagenda[rowId-1]["idestudio"];
                                
                    //Se activa el id para mostrar tabla para mostrar automaticamente horarios disponibles.
                    document.querySelector("#showDataHorarios").style.display = "block";
                                
                    //Se desactiva el formulario de hora manual para el rol de jefe de servicio.
                    document.querySelector("#manual_horajefe").style.display = "none";
                                
                    //Se activa el formulario de hora manual.
                    document.querySelector("#manual_hora").style.display = "block";
                                
                    //Se invoca función para crear tabla para mostrar automaticamente horarios disponibles.
                    CreateTableFromREST(uriserv + "/getCalendarAutomatic", jsonhorariosautomatica, "showDataHorarios", "tblhorarios", ["Fecha","Turno","Horario","Sala"], ["fecha","turno","hora","sala"],"0");
                                
                }
                            
                            
            break;
            case "tblsolicitudes":
                            
                //Se limpia el div con el id de elemento nameDivContainer.
                divContainer = html_GetElement("showDataHorarios");
            
                //Se vacia el elemento con id nameDivContainer
                divContainer.innerHTML = "";
                
                //Validacion para activar boton para agendar manual una agenda.
                if(document.querySelector("#manualAGENDA").style.display === "none")
                    document.querySelector("#manualAGENDA").style.display = "inline";
                            
                //Se asigna estado de una agenda.
                estado_agenda = "Programado";
                
                //Se llenan valores de la información de la cita en la vista.
                document.getElementById("agendanombrepaciente").value = jsonsolicitudes[rowId-1]["paciente"];
                document.getElementById("agendadomicilio").value = jsonsolicitudes[rowId-1]["domicilio"];
                document.getElementById("agendaareaprocedencia").value = jsonsolicitudes[rowId-1]["areaprocedencia"];
                document.getElementById("agendatipopaciente").value = determina_tipo_paciente(jsonsolicitudes[rowId-1]["tiempotraslado"]);
                document.getElementById("agendafechaproxima").value = jsonsolicitudes[rowId-1]["fechaproxima"].split(" ")[0];
                document.getElementById("agendahoraproxima").value = (jsonsolicitudes[rowId-1]["fechaproxima"].split(" ")[1]).split(".")[0];
                document.getElementById("agendaestudio").value = jsonsolicitudes[rowId-1]["estudio"] + " / " + jsonsolicitudes[rowId-1]["estudiodescripcion"];
                
                //Se invoca función para setear fecha actual en calendario.
                seteofechaactual("agendafecha");
                            
                //Se forma json JSONStudyControlCreate para agregar registros de la tabla ControlEstudios            
                JSONStudyControlCreate.noempleado = PERFIL[0]["noempleado"];
                JSONStudyControlCreate.curp = PERFIL[0]["curp"];
                JSONStudyControlCreate.idpaciente = jsonsolicitudes[rowId - 1]["idpaciente"];
                JSONStudyControlCreate.idestudio = jsonsolicitudes[rowId-1]["idestudio"];
                JSONStudyControlCreate.estado = estado_agenda;
                JSONStudyControlCreate.cerrado = "false";
                JSONStudyControlCreate.observaciones = jsonsolicitudes[rowId-1]["observaciones"];
                
                //Se forma json JSONStudyRequestCreate para agregar registros de la tabla SolicitudDeEstudio
                JSONStudyRequestCreate.idpaciente = jsonsolicitudes[rowId-1]["idpaciente"];
                JSONStudyRequestCreate.noempleado = jsonsolicitudes[rowId-1]["noempleado"];
                JSONStudyRequestCreate.curp = jsonsolicitudes[rowId-1]["curp"];
                JSONStudyRequestCreate.areaprocedencia = jsonsolicitudes[rowId-1]["areaprocedencia"];
                JSONStudyRequestCreate.fechaproxima = jsonsolicitudes[rowId-1]["fechaproxima"].split(".")[0];
                JSONStudyRequestCreate.diagnostico = jsonsolicitudes[rowId-1]["diagnostico"];
                JSONStudyRequestCreate.observaciones = jsonsolicitudes[rowId-1]["observaciones"];
                JSONStudyRequestCreate.estado = estado_agenda;
                
                //Se forma json JSONStudyControlUpdateClosed para actualizar registros de la tabla ControlEstudios
                JSONStudyControlUpdateClosed.noempleado = jsonsolicitudes[rowId-1]["noempleado"];
                JSONStudyControlUpdateClosed.curp = jsonsolicitudes[rowId-1]["curp"];
                JSONStudyControlUpdateClosed.idpaciente = jsonsolicitudes[rowId-1]["idpaciente"];
                JSONStudyControlUpdateClosed.idestudio = jsonsolicitudes[rowId-1]["idestudio"];
                JSONStudyControlUpdateClosed.fechacontrolpk = jsonsolicitudes[rowId-1]["fechasolicitudpk"];
                JSONStudyControlUpdateClosed.fechacontrol = jsonsolicitudes[rowId-1]["fechasolicitud"] + " " + jsonsolicitudes[rowId-1]["horasolicitud"].split(".")[0];
                JSONStudyControlUpdateClosed.cerrado = "true";
                
                //Se forma json JSONStudyAssignmentCreate para crear registros de la tabla AsignacionEstudio
                JSONStudyAssignmentCreate.idestudio = jsonsolicitudes[rowId-1]["idestudio"];
                            
                //Creación de Objecto para agregar elemento de id de área.
                var jsondevice = new Object();
    
                //Se agrega elemento de id de area de acuerdo al estudio seleccionado.
                jsondevice.idestudio = jsonsolicitudes[rowId-1]["idestudio"];
                            
                //Se invoca función para llenar realizar petición al servicio REST para obtener las salas/equipos y llenar el listbox.
                RESTObtenerListBox( uriserv + "/getDevice","consultaagendasala", "salaequipo", "noserie",jsondevice,"POST");
                
                //Se activa el modal para agendar una cita
                document.querySelector(".modalAGENDAR").style.display = "block";
                
                //Se valida el rol del usuario JefedelServicio
                if(PERFIL[0]["rol"] == "JefedelServicio"){
                                
                    //Se oculta el contenedor para mostrar horarios automaticos.
                    document.querySelector("#showDataHorarios").style.display = "none";
                                
                    //Se activa el contenedor de elementos para agendar una cita manual.
                    document.querySelector("#contenedor-manual").style.display = "block";
                                
                    //Se desactiva el formulario de hora manual.
                    document.querySelector("#manual_hora").style.display = "none";
                                
                    //Se activa el formulario de sala para el jefe de servicio.
                    document.querySelector("#manual_sala").style.display = "block";
                                
                    //Se activa el formulario de fecha para el jefe de servicio.
                    document.querySelector("#manual_fecha").style.display = "block";
                                
                    //Se activa el formulario de hora para el jefe de servicio.
                    document.querySelector("#manual_horajefe").style.display = "block";
                                
                    //Se oculta el boton para activar la opción para agendar una cita manual.
                    document.querySelector("#manualAGENDA").style.display = "none";
                                
                    //Se limpia el formulario de hora para el jefe de servicio.
                    document.querySelector("#agendahorajefe").value = "";
                    
                    //Se asigna valor al tipo de asignacion
                    tipo_asignacion = "manualjefe";
                    
                }else{
                                
                    //Se obtiene el dato del formulario con id noregistro y se insertar en un elemento del objeto.
                    jsonhorariosautomatica.fechaproxima = jsonsolicitudes[rowId-1]["fechaproxima"].split(".")[0];
                    jsonhorariosautomatica.tipo_paciente = determina_tipo_paciente(jsonsolicitudes[rowId-1]["tiempotraslado"]);
                    jsonhorariosautomatica.idestudio = jsonsolicitudes[rowId-1]["idestudio"];
                                
                    //Se activa el id para mostrar tabla para mostrar automaticamente horarios disponibles.
                    document.querySelector("#showDataHorarios").style.display = "block";
                                
                    //Se desactiva el formulario de hora manual para el rol de jefe de servicio.
                    document.querySelector("#manual_horajefe").style.display = "none";
                                
                    //Se activa el formulario de hora manual.
                    document.querySelector("#manual_hora").style.display = "block";
                                
                    //Se invoca función para crear tabla para mostrar automaticamente horarios disponibles.
                    CreateTableFromREST(uriserv + "/getCalendarAutomatic", jsonhorariosautomatica, "showDataHorarios", "tblhorarios", ["Fecha","Turno","Horario","Sala"], ["fecha","turno","hora","sala"],"0");
                            
                }
                            
            break;                         
            case "tblhorarios":
                
                //Se obtiene la fecha de proxima cita y la hora
                var fechacita = new Date(jsonopcauto[rowId-1]["fecha"] + " " + jsonopcauto[rowId-1]["hora"]);
                
                //Se forma json JSONStudyRequestCreate para agregar registros de la tabla SolicitudDeEstudio
                JSONStudyRequestCreate.fechasolicitudpk = fechacita.valueOf();
                JSONStudyRequestCreate.fechasolicitud = FormatoFecha(fechacita);
                
                //Se forma json JSONStudyControlCreate para agregar registros de la tabla ControlEstudios
                JSONStudyControlCreate.fechacontrolpk = fechacita.valueOf();
                JSONStudyControlCreate.fechacontrol = FormatoFecha(fechacita);
                
                //Se forma json JSONStudyAssignmentCreate para crear registros de la tabla AsignacionEstudio
                JSONStudyAssignmentCreate.fechapk = fechacita.valueOf();
                JSONStudyAssignmentCreate.fecha = FormatoFecha(fechacita);
                JSONStudyAssignmentCreate.noserie = jsonopcauto[rowId-1]["equipo"];
                
                //Valida estado de agenda
                if(estado_agenda == "Reprogramado"){
                    
                    //Se forma json JSONStudyAssignmentUpdate para actualizar registros de la tabla AsignacionEstudio            
                    JSONStudyAssignmentUpdate.noserie_new = jsonopcauto[rowId-1]["equipo"];
                    JSONStudyAssignmentUpdate.fechapk_new = fechacita.valueOf();
                    JSONStudyAssignmentUpdate.fecha = FormatoFecha(fechacita);
                    
                }
            break;   
                        
        }
    }
}
            
            function deleteRegister(cel, tablename) {
                var rowId = cel.parentNode.rowIndex; //indice de reglon.
                if (rowId > 0) { //se excluye cabecera de columna
                  
                  var r = confirm("Desea Borrar el registro ");
                  if (r === true) {
                     //borrar de la base de datos (inhabilitar "borrado lógico") y despues de la vista                       
                     deleteTableRow(tablename,rowId);
                 }else{
                   return;  
                 } 
                }
            }
  
/**
 * Función para activar el modal para cancelar una cita de estudios.
 */

function cancelarcita(cel, tablename) {
    
    //Se obtiene el indice de un renglon
    var rowId = cel.parentNode.rowIndex;
    
    //Se activa el modal para cancelar una agenda de estudios,
    document.querySelector(".modalAGENDACANCELAR").style.display = "block";
    
    //Se asigna estado de una agenda.
    estado_agenda = "Cancelado";
    
    //Se forma json JSONStudyAssignmentDelete para eliminar registros de la tabla AsignacionEstudio
    JSONStudyAssignmentDelete.noserie = jsonagenda[rowId-1]["noserie"];
    JSONStudyAssignmentDelete.idestudio = jsonagenda[rowId-1]["idestudio"];
    JSONStudyAssignmentDelete.fechapk = jsonagenda[rowId-1]["fechaasignpk"];
    
    //Se forma json JSONStudyControlUpdateClosed para actualizar registros de la tabla ControlEstudios
    JSONStudyControlUpdateClosed.noempleado = jsonagenda[rowId-1]["noempleadousuario"];
    JSONStudyControlUpdateClosed.curp = jsonagenda[rowId-1]["curpempleado"];
    JSONStudyControlUpdateClosed.idpaciente = jsonagenda[rowId-1]["idpaciente"];
    JSONStudyControlUpdateClosed.idestudio = jsonagenda[rowId-1]["idestudio"];
    JSONStudyControlUpdateClosed.fechacontrolpk = jsonagenda[rowId-1]["fechasolicitudpk"];
    JSONStudyControlUpdateClosed.fechacontrol = jsonagenda[rowId-1]["fechasolicitud"].split(".")[0];
    JSONStudyControlUpdateClosed.cerrado = "true";
    
    //Se forma json JSONStudyControlCreate para agregar registros de la tabla ControlEstudios
    JSONStudyControlCreate.noempleado = PERFIL[0]["noempleado"];
    JSONStudyControlCreate.curp = PERFIL[0]["curp"];
    JSONStudyControlCreate.idpaciente = jsonagenda[rowId-1]["idpaciente"];
    JSONStudyControlCreate.idestudio = jsonagenda[rowId-1]["idestudio"];
    JSONStudyControlCreate.estado = estado_agenda;
    JSONStudyControlCreate.cerrado = "true";
    
    //Se forma json JSONStudyRequestCreate para agregar registros de la tabla SolicitudDeEstudio
    JSONStudyRequestCreate.idpaciente = jsonagenda[rowId-1]["idpaciente"];
    JSONStudyRequestCreate.noempleado = jsonagenda[rowId-1]["noempleado"];
    JSONStudyRequestCreate.curp = jsonagenda[rowId-1]["curp"];
    JSONStudyRequestCreate.areaprocedencia = jsonagenda[rowId-1]["areaprocedencia"];
    JSONStudyRequestCreate.fechaproxima = jsonagenda[rowId-1]["fechaproxima"].split(".")[0];
    JSONStudyRequestCreate.diagnostico = jsonagenda[rowId-1]["diagnostico"];
    JSONStudyRequestCreate.estado = estado_agenda;

}

/**
 * Función para dar formato "yyyy-mm-dd HH:MM:SS" a la fecha y hora.
 */

function FormatoFecha(fecha){
    return `${fecha.getFullYear().toString().padStart(4, '0')}-${
    (fecha.getMonth()+1).toString().padStart(2, '0')}-${
    fecha.getDate().toString().padStart(2, '0')} ${
    fecha.getHours().toString().padStart(2, '0')}:${
    fecha.getMinutes().toString().padStart(2, '0')}:${
    fecha.getSeconds().toString().padStart(2, '0')}`;
}

/**
 * Función para validar el contador de agenda recibiendo los mensajes exitoso y erroneos.
 */

function validaagendacont(smsexitoso, smserroreno){
    
    //Validación contador igual a 4.
    if(createagendacont == 4){
        
        //Se muestra mensaje en pantalla.
        alert(smsexitoso);
        
        //Inicializar contador de agenda.
        createagendacont = 0;
        
        //Retorno verdadero
        return true;
            
    }else{ //Validación contador a cero.
            
        //Se muestra mensaje de error en pantalla.
        alert(smserroreno);
        
        //Inicializar contador de agenda.
        createagendacont = 0;
        
        //Retorno falso
        return false;
        
    }
    
}

/**
 * Función para realizar peticiones POST a servicios REST.
 */

function RESTAgendaPOST(uri,jsondata){
    
    //Metodo AJAX para realizar peticiones al servicio REST.
    $.ajax({
        //Objetos configurables para realizar peticiones al servicio REST.
        url: uri,
        type: 'POST',
        async: false,
        dataType: 'json',
        data: jsondata
    }).done(function (data, textStatus,jqXHR){
        
        //Validación del resultado de la respuesta del servicio REST.
        if(data["CREATE"] == true || data["DELETE"] == true || data["UPDATECLOSED"] == true || data["UPDATE"] == true){
            //Se incrementa el contador
            createagendacont = createagendacont + 1;
        }else{
            //En caso de error se decrementa el contador.
            createagendacont = createagendacont - 1;
        }
        
    }).fail(function (jqXHR,textStatus, errorThrown){
        
        //En caso de falla el contador es igual a 0.
        createagendacont = 0;
        
    }).always(function(jqXHROrData, textStatus, jqXHROrErrorThrown){
    });
}

/**
 * Función para activar eventos y modales.
 */

function activamodal(str, form) {
                
    switch (str) {
                    
        case "LimpiarFORMULARIO":
            
            //Se elimina el registro del formulario paciente.
            document.getElementById("form_paciente").reset();
            
        break;
        case "ConsultaAGENDA":
            
            //Se invoca función para consultar las agendas de estudio.
            consultaagenda();
        
        break;
        case "CancelarAGENDA":
            
            //Se desactivan algunos elementos para agendar una cita de estudios
            document.querySelector("#contenedor-manual").style.display = "none";
            document.querySelector("#manual_sala").style.display = "none";
            document.querySelector("#manual_fecha").style.display = "none";
            document.querySelector("#manual_hora").style.display = "none";
            document.querySelector(".modalAGENDAR").style.display = "none";

                        
        break;
        case "CancelarAGENDACANCELAR":
            
            //Se desactiva el modal para cancelar una cita.
            document.querySelector(".modalAGENDACANCELAR").style.display = "none";
            
        break;
        case "CancelarSOLICITUDES":
            
            //Se desactiva el modal para agendar una cita.
            document.querySelector(".modalSOLICITUDES").style.display = "none";
            
        break;
        case "ManualAGENDA":
            
            //Se limpia el listbox
            cleanlistbox("agendahora");
            
            //Se limpia json JSONStudyRequestCreate
            JSONStudyRequestCreate.fechasolicitudpk = "";
            JSONStudyRequestCreate.fechasolicitud = "";
            
            //Se limpia json JSONStudyControlCreate
            JSONStudyControlCreate.fechacontrolpk = "";
            JSONStudyControlCreate.fechacontrol = "";
            
            //Se limpia json JSONStudyAssignmentCreate
            JSONStudyAssignmentCreate.fechapk = "";
            JSONStudyAssignmentCreate.fecha = "";
            JSONStudyAssignmentCreate.noserie = "";
            
            //Valida estado de una agenda
            switch(estado_agenda){
                case "Reprogramado":
                    
                    //Se limpia json JSONStudyAssignmentUpdate
                    JSONStudyAssignmentUpdate.noserie_new = "";
                    JSONStudyAssignmentUpdate.fechapk_new = "";
                    JSONStudyAssignmentUpdate.fecha = "";
                    
                break;
            }
            
            //Se limpia el div con el id de elemento nameDivContainer.
            divContainer = html_GetElement(showDataHorarios);
            
            //Se vacia el elemento con id nameDivContainer
            divContainer.innerHTML = "";
            
            //Se desactiva div donde se muestra la asignacion horarios automatica
            document.querySelector("#showDataHorarios").style.display = "none";
            
            //Se desactiva boton para seleccionar una agenda manual
            document.querySelector("#manualAGENDA").style.display = "none";
            
            document.querySelector("#contenedor-manual").style.display = "block";
            document.querySelector("#manual_sala").style.display = "block";
            document.querySelector("#manual_fecha").style.display = "block";
            document.querySelector("#manual_hora").style.display = "block";
            
            //Se asigna valor al tipo de asignacion
            tipo_asignacion = "manual";
        
        break;
        case "AceptarAGENDA":
            
            //Declaracion de variables
            var flag_agenda = 0, resp;
            
            //Valida tipo de asignacion
            if(tipo_asignacion == "manual" || tipo_asignacion == "manualjefe"){
                
                //Valida si los formularios estan vacios
                if(!document.getElementById("consultaagendasala").value || !document.getElementById("agendafecha").value || !document.getElementById(tipo_asignacion == "manual" ? "agendahora" : "agendahorajefe").value){
                    
                    //Activacion de alerta
                    alert("Es necesario seleccionar las opciones de horario");
                    
                }else{
                    
                    //Se obtiene la fecha de los formularios
                    var fechacita = new Date(document.getElementById("agendafecha").value + " " + document.getElementById(tipo_asignacion == "manual" ? "agendahora" : "agendahorajefe").value);
                    
                    //Se forma json JSONStudyRequestCreate para agregar registros de la tabla SolicitudDeEstudio
                    JSONStudyRequestCreate.fechasolicitudpk = fechacita.valueOf();
                    JSONStudyRequestCreate.fechasolicitud = FormatoFecha(fechacita);
                    
                    //Se forma json JSONStudyControlCreate para agregar registros de la tabla ControlEstudios
                    JSONStudyControlCreate.fechacontrolpk = fechacita.valueOf();
                    JSONStudyControlCreate.fechacontrol = FormatoFecha(fechacita);
                    
                    //Se forma json JSONStudyAssignmentCreate para crear registros de la tabla AsignacionEstudio
                    JSONStudyAssignmentCreate.fechapk = fechacita.valueOf();
                    JSONStudyAssignmentCreate.fecha = FormatoFecha(fechacita);
                    JSONStudyAssignmentCreate.noserie = document.getElementById("consultaagendasala").value;
                    
                    //Valida estado Reprogramado
                    if(estado_agenda == "Reprogramado"){
                        
                        //Se forma json JSONStudyAssignmentUpdate para actualizar registros de la tabla AsignacionEstudio            
                        JSONStudyAssignmentUpdate.noserie_new = document.getElementById("consultaagendasala").value;
                        JSONStudyAssignmentUpdate.fechapk_new = fechacita.valueOf();
                        JSONStudyAssignmentUpdate.fecha = FormatoFecha(fechacita);
                        
                    }
                    
                    //Se activa bandera para indicar que se agendara una cita
                    flag_agenda = 1;
                                
                }

            }else{
                
                //Valida si se selecciono un horario para agendar
                if(!getRadioVal("citas-radio")){
                    
                    //Activacion de alerta
                    alert("Es necesario seleccionar un horario");
                    
                }else{
                    
                    //Se activa bandera para indicar que se agendara una cita
                    flag_agenda = 1;  
                }

            }

            //Valida el estado de una agenda y la bandera para agendar
            if(estado_agenda == "Reprogramado" && flag_agenda == 1){
                
                //Se invoca funcion para agendar una cita de estudios
                RESTAgendaPOST( uriserv + "/StudyAssignment/UPDATE",JSONStudyAssignmentUpdate);
                RESTAgendaPOST( uriserv + "/StudyEntity/CREATE",JSONStudyRequestCreate);
                RESTAgendaPOST( uriserv + "/AppointmentEntity/CREATE",JSONStudyControlCreate);
                RESTAgendaPOST( uriserv + "/AppointmentEntity/UPDATECLOSED",JSONStudyControlUpdateClosed);
                
                //Se invoca funcion para validar contador de agendas
                resp = validaagendacont("Cita reagendada","Problemas con el registro");
                
                //Se invoca función para consultar las agendas de estudio.
                consultaagenda();
            
            }else if(estado_agenda == "Programado" && flag_agenda == 1){
                
                //Se invoca funcion para agendar una cita de estudios
                RESTAgendaPOST( uriserv + "/StudyAssignment/CREATE",JSONStudyAssignmentCreate);
                RESTAgendaPOST( uriserv + "/StudyEntity/CREATE",JSONStudyRequestCreate);
                RESTAgendaPOST( uriserv + "/AppointmentEntity/CREATE",JSONStudyControlCreate);
                RESTAgendaPOST( uriserv + "/AppointmentEntity/UPDATECLOSED",JSONStudyControlUpdateClosed);
                
                //Se invoca funcion para validar contador de agendas
                resp = validaagendacont("Cita agendada","Problemas con el registro");
                
                //Se invoca función para consultar las solicitudes de estudio.            
                consultasolicitud();
                            
            }
                        
            if(resp == true){
                            
                //Se limpia variable para definir el estado de una agenda.
                estado_agenda = "";
                
                //Se limpia valor al tipo de asignacion
                tipo_asignacion = "";
                
                //Se desactivan algunos elementos para agendar una cita de estudios
                document.querySelector("#contenedor-manual").style.display = "none";
                document.querySelector("#manual_sala").style.display = "none";
                document.querySelector("#manual_fecha").style.display = "none";
                document.querySelector("#manual_horajefe").style.display = "none";
                document.querySelector(".modalAGENDAR").style.display = "none";
                
            }
            
        break;
        case "AceptarAGENDACANCELAR":
            
            //Declaracion de variables
            var fechaactual = new Date(), resp;
            
            //Valida textarea si esta vacio
            if(!document.getElementById("agendatextcancel").value){
                
                //Activacion de alerta
                alert("Ingresar el motivo de cancelación para continuar.");
                
            }else{
                
                //Se forma json JSONStudyControlCreate para agregar registros de la tabla ControlEstudios
                JSONStudyRequestCreate.fechasolicitudpk = fechaactual.valueOf();
                JSONStudyRequestCreate.fechasolicitud = FormatoFecha(fechaactual);
                JSONStudyRequestCreate.observaciones = document.getElementById("agendatextcancel").value;
                JSONStudyControlCreate.fechacontrolpk = fechaactual.valueOf();
                JSONStudyControlCreate.fechacontrol = FormatoFecha(fechaactual);
                JSONStudyControlCreate.observaciones = document.getElementById("agendatextcancel").value;
                
            }
            
            //Se invoca funcion para cancelar una cita de estudios
            RESTAgendaPOST( uriserv + "/StudyAssignment/DELETE",JSONStudyAssignmentDelete);
            RESTAgendaPOST( uriserv + "/AppointmentEntity/UPDATECLOSED",JSONStudyControlUpdateClosed);
            RESTAgendaPOST( uriserv + "/AppointmentEntity/CREATE",JSONStudyControlCreate);
            RESTAgendaPOST( uriserv + "/StudyEntity/CREATE",JSONStudyRequestCreate);
            
            //Se invoca funcion para validar contador de agendas
            resp = validaagendacont("Cita Cancelada","Problemas con el registro");
                        
            if(resp == true){

                //Se desactiva modal para cancenlar una cita de estudios
                document.querySelector(".modalAGENDACANCELAR").style.display = "none";
                            
                //Se limpia variable para definir el estado de una agenda.
                estado_agenda = "";
                
                //Se invoca función para consultar las agendas de estudio.
                consultaagenda();
                
            }
        break;
        case "CancelarPERFILES":
            dialogoPerfiles.style.display = "none";
        break;
        case "GuardarPERFILES":
            
            var r = confirm("Desea guardar los cambios ");
            
            if (r === true) {
                // OJO guardar en bd  y despues ctualizar tabla
                const cellsOfRow = getRowCells(form.rowid.value, form.tblid.value);
                cellsOfRow[2].innerHTML = document.getElementById("desc").value;
                cellsOfRow[3].innerHTML = document.getElementById("nom").value;
                            
                var tiposel=getRadioVal("tipo");
                cellsOfRow[4].innerHTML=tiposel;

                alert("Registro actualizado");

                } else {
                    //alert("Seleccion Cancelada");
                    return;
                }
                
                document.getElementById("formdata").reset();
                dialogoPerfiles.style.display = "none";	//cerrar dialogo
            break;

        case "AgregarPERFILES":
            var r = confirm("Desea Agregar el registro ");
            if (r === true) {
                var tableref = document.getElementById("tblperfiles");
                var numregistro = tableref.rows.length;                            
                //profileregistro = 0; //OJO CONTADOR DEL MAXIOMO DE TABLA
                            
                var descripcion=document.getElementById("desc").value;
                var nombre=document.getElementById("nom").value ;
                            
                var refregx=getRadioVal("tipo");
                            
                var tbl = "<a href='#'><i class='fa fa-pencil' style='font-size:24px ; color:black '></i></a>";
                var tbl2 = "<a href='#'><i class='fa fa-trash' style='font-size:24px; color:black'></i></a>";
                //cuerpoedo1 = '<tr><td>' + dateYYYYmmdd() + '</td><td>' + form.profileid.value + '</td><td>' + form.descrip.value + '</td><td>' + refregx + '</td><td>' + cadedo + '</td><td>' + "Editar " + tbl + '</td></tr>';
                var cuerpoedo1 = '<tr><td>' +numregistro + '</td><td>' +dateYYYYmmdd() + '</td><td>' + descripcion + '</td><td>' + nombre + '</td><td>' +refregx  + '</td><td>' +"BLOQUEADO"  + '</td><td>' + "Editar " + tbl + '</td><td>' + "Borrar " + tbl2 + '</td></tr>';
                $("#tblperfiles").append(cuerpoedo1);
	        tableRowColorCellSelection("tblperfiles", 5, 6);
							
                //OJO actualizar la tabla de perfiles.

                            
                dialogoPerfiles.style.display = "none";
            } else {
                //alert("Seleccion Cancelada");
                return;
            }
        break;
        case "CancelarUSUARIOS":
            dialogoUsuarios = document.querySelector(".modalUSUARIOS");
            dialogoUsuarios.style.display = "none";//ocultar dialogo                            
        break;
        case "AgregarUSUARIOS":
            var r = confirm("Desea Agregar el registro ");
            if (r === true) {   
                var tableref = document.getElementById("tblusuarios");
                var numregistro = tableref.rows.length;                            
                //profileregistro = 0; //OJO CONTADOR DEL MAXIOMO DE TABLA
                //ojo: GUARDAR EN bd Y DESPUES ACTUALIZAR TABLA

                var nombre=document.getElementById("uname").value;
                var correo=document.getElementById("email").value;
                var refregx=getRadioVal("estado");
                console.log("Estado: "+refregx);
                var lixboxsel=document.getElementById("perf2");
                //var lixboxval=lixboxsel[lixboxsel.selectedIndex].value;
                var perfil=lixboxsel[lixboxsel.selectedIndex].innerText;
            
                                
                var edit = "<a href='#'><i class='fa fa-pencil' style='font-size:24px ; color:black '></i></a>";
                var del = "<a href='#'><i class='fa fa-trash' style='font-size:24px; color:black'></i></a>";
                //cuerpoedo1 = '<tr><td>' + dateYYYYmmdd() + '</td><td>' + form.profileid.value + '</td><td>' + form.descrip.value + '</td><td>' + refregx + '</td><td>' + cadedo + '</td><td>' + "Editar " + tbl + '</td></tr>';
                var cuerpoedo1 = '<tr><td>' +numregistro + '</td><td>' +dateYYYYmmdd() + '</td><td>' + nombre + '</td><td>' + correo + '</td><td>' +refregx  + '</td><td>'+ perfil + '</td><td>' + "Editar " + edit + '</td><td>' + "Borrar " + del + '</td></tr>';
                $("#tblusuarios").append(cuerpoedo1);
		tableRowColorCellSelection("tblusuarios", 6, 7);                           
                                
                dialogoUsuarios = document.querySelector(".modalUSUARIOS");
                dialogoUsuarios.style.display = "none";//ocultar dialogo
            } else {
                return;
            }                            
        break;
        case "GuardarUSUARIOS":
            var r = confirm("Desea guardar los cambios ");
            if (r === true) {                            
                const cellsOfRow = getRowCells(form.USRrowid.value, form.USRtblid.value);
                cellsOfRow[3].innerHTML=document.getElementById("uname").value;
                cellsOfRow[2].innerHTML=document.getElementById("email").value;
                cellsOfRow[5].innerHTML=document.getElementById("perfil").value;
                               
                var lixboxsel=document.getElementById("perf2");
                cellsOfRow[5].innerHTML=lixboxsel[lixboxsel.selectedIndex].innerText; //perfil
                var tiposel=getRadioVal("estado");
                cellsOfRow[4].innerHTML=tiposel;
                //falta guardar en BD.

                dialogoUsuarios = document.querySelector(".modalUSUARIOS");
                dialogoUsuarios.style.display = "none";//ocultar dialogo
                alert("Registro actualizado");

            } else {
                return;
            }                            
        break;                            
    }
}



            function getRadioVal(idRadio) {
                var val;
                var tiposel = document.getElementsByName(idRadio);
                for (let x = 0; x < tiposel.length; x++) {
                   if (tiposel[x].checked){
                    val = tiposel[x].value;
                    break; // and break out of for loop
                  }
                }                
                
                return val; // return value of checked radio or undefined if none checked
            }
            
            function setSelectedIndex(refselid, texto) {
                for ( var i = 0; i < refselid.options.length; i++ ) {
                    if ( refselid.options[i].text === texto ) {
                        refselid.options[i].selected = true;
                        return;
                    }
                }
            }
//***********************************
            function salir() {
                //INVALIDAR SESION
                $.ajax({
                    url: uriserv+'/logout',
                    type: 'GET', // Tipo de envio 
                    dataType: 'json', //Tipo de Respuesta
                    error: function (err) {
                        window.location = host + 'login.html';
                    }

                });
            }
             //FUNCIONES PARA INVALIDAR NAVEGACION ENTRE  PAGINAS
            function preventBack() { 
                window.history.forward();  
            } 
          
            setTimeout("preventBack()", 0); 

            window.onunload = function () { null; };
            
            function nobackbutton() {
                window.location.hash = "no-back-button";
                window.location.hash = "Again-No-back-button";
                window.onhashchange = function () {
                    window.location.hash = "no-back-button";
                };
            } 
            //
            
            function regresar() {
                //invalidar los botones de navegación.
               window.location = host + 'MainPageRes.html'; 
            }

            function getsession() {
                $.ajax({
                    url: uriserv+'/session',
                    type: 'GET', // Tipo de envio 
                    dataType: 'json', //Tipo de Respuesta
                    success: function (data) {
                        //console.log(data); //guardar datos de sesion
                        
                    },
                    error: function (err) {
                        //alert("Regresando a la pagina principal");
                        window.location = host + 'login.html';
                    }

                });
            } 

            //servicio rest para recuperar datos o ejecutar servicio dicom
            function getJsonObject(urlser) {


            }

            function addProfile(e) {
                //console.log(event);
                //console.log(e);
                document.getElementById("formdata").reset();
                document.getElementById("fechacreacion").innerHTML = new Date().toDateString();

                botonguardar = document.getElementById("guardarPERFILES");
                botonguardar.style.display ="none";
                //botonguardar.style.visibility = "hidden";

                botonagregar = document.getElementById("agregarPERFILES");
                botonagregar.style.display ="block";
                //botonagregar.style.visibility = "visible";

                dialogoPerfiles.style.display = "block";//activar dialogo modal  
                //borrar contenido de los campos

            }
            
            function addUser(e) {

                //getUsrs(); //recuperar contenido de base de datos (usuario y perfiles)
                            
                dialogoUsuarios = document.querySelector(".modalUSUARIOS");
                dialogoUsuarios.style.display = "block";//activar dialogo modal, cristal transparente
                botonguardar = document.getElementById("guardarUSUARIOS");
                botonguardar.style.display ="none";
                //botonguardar.style.visibility = "hidden";

                botonagregar = document.getElementById("agregarUSUARIOS");
                botonagregar.style.display ="block";                
                //botonagregar.style.visibility = "visible";   
                
                //perfilupdate = document.getElementById("perfilupdate");
                //perfilupdate.style.display ="none";
                
                document.getElementById("uname").value = "";
                document.getElementById("email").value = "";
                document.getElementById("perfil").value = "";
                
                lixboxPER=document.getElementById("perf2");
                lixboxSEL=lixboxPER[0].selected = true; //OPCION 0 SELECCIONADA
                document.getElementById("perfil").innerHTML="";

                //console.log(list);
            }

            function actualizaPerfilSeleccionado(e){
               var lixboxseltxt=document.getElementById("perf2")[e].innerText;
               document.getElementById("perfil").innerHTML="Selección: "+lixboxseltxt;
               //perfilupdate.value=lixboxseltxt; //opcion default;
            }
            
            window.onload = function () {
                //console.log("ID medico: "+document.getElementById("medID").value);          
                dialogoPerfiles = document.querySelector(".modalPERFILES");
                //getsession();
                nobackbutton();
            };

            /*window.onclick = function(e){
             if(e.target == modal){
             modal.style.display = "none";
             }
             }*/


            function activeTab(evt, opcionMenu) {
                var i, tabcontent, tablinks;
                //console.log(evt.currentTarget);
                tabcontent = document.getElementsByClassName("tabcontent");
                for (i = 0; i < tabcontent.length; i++) {
                    tabcontent[i].style.display = "none";
                }
                tablinks = document.getElementsByClassName("tablinks");

                for (i = 0; i < tablinks.length; i++) {
                    tablinks[i].className = tablinks[i].className.replace(" active", "");
                }

                document.getElementById(opcionMenu).style.display = "block";
                strtablinks = evt.currentTarget.className;
                if (strtablinks === "tablinks")
                    evt.currentTarget.className += " active";//solo para la clase del menu
            }


          
            function getUsrs() {

                var colsProfile = ["Tbl", "Fecha creación", "Nombre", "Tipo", "Descripción"];
                var llamadaPRO = getTBL(uriserv + "/getAllProfile", "showDataProfile", "tblperfiles", colsProfile);

                var colsUSR = ["User ID", "Fecha creacion", "Nombre usuario", "Correo", "Estado", "Perfil"];
                var llamadaUSR = getTBL(uriserv + "/getAllUsers", "showDataUser", "tblusuarios", colsUSR);
                $.when(llamadaUSR, llamadaPRO).done(function (ajaxUSRResults, ajaxPROResults) {
                    //this code is executed when all ajax calls are done
                    //TABLA PERILES
                    tableViewFormat("tblperfiles", 5, 6); //tablName,coledit,coldelete
                    //permite ordenar en cabecera
                    tableHeaderSelection("tblperfiles", [0,1,2,4]);
                    
                    //TABLA USUARIOS
                    tableViewFormat("tblusuarios", 6, 7); //tablName,coledit,coldelete                    
                    //permite ordenar en cabecera
                    tableHeaderSelection("tblusuarios", [2,3,5]);

                    //LIST BOX DE PERFILES
                    UpdateListBox("perf2", ajaxPROResults[0], 0, 4);//atributo value = columna[0], atributo innerHTML=columna[4]
                    list = document.getElementById("perf2"); //opcion default
                    list.setAttribute('onchange', "actualizaPerfilSeleccionado(this.value)"); //action listener para el listbox
                });
                //TABLA modulos
                var colsModule = ["Referecia", "Descripción"];
                CreateTableFromJSON("showDataModule", "tblmodules", colsModule); //parametros regerencia div, nombre tabla , arreglo json, cabecera
                UpdateTableRows("tblmodules", modulesDum);
                tableViewFormat("tblmodules", 2, 3); //tablName,coledit,coldelete 
                //permite ordenar en cabecera
                tableHeaderSelection("tblmodules", [0,1]);
            } 
            
                       
            function getTBL(uriServ,nameDivContainer,tablName,columnas){
               return $.ajax({
                    url: uriServ,
                    type: 'POST', // Tipo de envio 
                    dataType: 'json' //Tipo de Respuesta
                }).done(function (data, textStatus, jqXHR) {
                    //console.log(data);
                    CreateTableFromJSON(nameDivContainer, tablName, columnas); //parametros referencia div, nombre tabla , arreglo json, cabecera
                    UpdateTableRows(tablName, data);
                }).fail(function (jqXHR, textStatus, errorThrown) {
                    alert("No hay datos en bd " + errorThrown);
                    //removerPreloader(servicio);
                }).always(function (jqXHROrData, textStatus, jqXHROrErrorThrown) {
                    //removerPreloader(servicio); 
                });                
            }
			
            
			function tableViewFormat(tablName, coledit, coldelete) {
                var tbl = "<a href='#'><i class='fa fa-pencil' style='font-size:24px; color:black'></i></a>";
                insertColumnK(tablName, coledit, "Modificar", "Editar"); //inserta columna de esdicion (columna que no pertenece a los datos)                    
                updateTableColumns(tablName, coledit, tbl);

                var tbl = "<a href='#'><i class='fa fa-trash' style='font-size:24px; color:black'></i></a>";
                insertColumnK(tablName, coldelete, "Modificar", "Borrar"); //inserta columna de esdicion (columna que no pertenece a los datos)
                updateTableColumns(tablName, coldelete, tbl);
                tableRowColorCellSelection(tablName, coledit, coldelete);
            }

            function tableRowColorCellSelection(tablName, coledit, coldelete) {
                backGroundColor(tablName, "rgba(88,142,255,.5)", "#000000", "#7F7F7F", "#FFFFFF");
                rowColor(tablName, "#00FFFF", "#000000", "#7F7F7F", "#FFFFFF", "#ffffff", "#000000"); //puntero raton (b,c); pares (b,c); impares (b,c) 
                cellKnSelection(tablName, "getval(this,'" + tablName + "')", coledit); //asignar funcion de seleccion para la celda kn de la tabla
                cellKnSelection(tablName, "cancelarcita(this,'" + tablName + "')", coldelete); //asignar funcion de seleccion para la celda kn de la tabla                 
            }
            
            function tableHeaderSelection(tablName, columnas){
                for(let x=0;x<columnas.length;x++){
                      cellHeaderSelection(tablName, "sortTable("+columnas[x]+",'"+tablName+"');", columnas[x]); //asignar funcion de seleccion para la celda kn de la tabla    
                }
            }
			




$(document).ready(function () {


    //Se llenan los encabezados de la pagina            
    document.getElementById("encabezadonombre").innerHTML="&nbsp;Nombre: " + PERFIL[0]["nombre"] + " " + PERFIL[0]["apellidopaterno"] + " " + PERFIL[0]["apellidomaterno"];
    document.getElementById("encabezadoarea").innerHTML="&nbsp;Área: " + PERFIL[0]["nombrearea"];
    document.getElementById("encabezadorol").innerHTML="&nbsp;Rol: " + PERFIL[0]["rol"];
    
    //Valida rol del usuario
    switch(PERFIL[0]["rol"]){
        case 'Recepcionista':
                        
            //Se oculta formulario área
            document.querySelector("#filtro_area").style.display = "none";
            //Se oculta formulario estado
            document.querySelector("#filtro_estado").style.display = "none";
                        
            //Se llenan las opciones de los formularios de estudio y sala
            consultaestudios(PERFIL[0]["idarea"]);
                        
        break;
        case 'JefedelServicio':
                        
            //Se oculta formulario área
            document.querySelector("#filtro_area").style.display = "none";
                        
            //Se llenan las opciones de los formularios de estudio y sala
            consultaestudios(PERFIL[0]["idarea"]);
                        
        break;
        case 'SubdirectordeServicios':
                        
            //Se oculta boton de busqueda de solicitudes de estudio
            document.querySelector("#consultarSOLICITUDES").style.display = "none";
                        
            //Se invoca función para llenar realizar petición al servicio REST para obtener las áreas de estudio y llenar el listbox.
            RESTObtenerListBox( uriserv + "/getAllAreas", "consultaarea", "nombrearea", "idarea", "","GET");
        
        break;
        case 'CoordinadordelServicio':
            
            //Se oculta formulario área
            document.querySelector("#filtro_area").style.display = "none";
            
            //Se oculta formulario estado
            document.querySelector("#filtro_estado").style.display = "none";
            
            //Se llenan las opciones de los formularios de estudio y sala
            consultaestudios(PERFIL[0]["idarea"]);
        break;
    }

                    var cols = ["#","FEcha", "Nombre", "Descripción", "Estado", "FSM"];
                    CreateTableFromJSON("showDataProfile", "tblperfiles", cols); //parametros regerencia div, nombre tabla , arreglo json, cabecera
                    UpdateTableRows("tblperfiles", dummy);

                tableViewFormat("tblperfiles", 6, 7); //tablName,coledit,coldelete 
                //permite ordenar en cabecera
                tableHeaderSelection("tblperfiles", [1,2,3]);				

                var cols = ["#","Fecha creación","Correo", "Nombre", "Clave","Perfil"];
                CreateTableFromJSON("showDataUser", "tblusuarios", cols); //parametros regerencia div, nombre tabla , arreglo json, cabecera
                UpdateTableRows("tblusuarios", USRS);
				
				tableViewFormat("tblusuarios", 6, 7); //tablName,coledit,coldelete 
                //permite ordenar en cabecera
                tableHeaderSelection("tblusuarios", [1,2,3,5]);//NOMBRE DE TABLA, COLUMNAS CON SORT	

                $('#nommed').val("ADMINISTRADOR"); //perfil del usuario
                //getUsrs();
                
                UpdateListBox("perf2", dummy,0,2);//atributo value = columna[1], atributo innerHTML=columna[3]

                
});



/**
 * Función para llenar listbox para consultar áreas.
 */

function consultaarea(){

    //Se invoca función para llenar realizar petición al servicio REST para obtener las áreas de estudio y llenar el listbox.
    RESTObtenerListBox( uriserv + "/getAllAreas", "consultaarea", "nombrearea", "idarea", "","GET");
    
}

/**
 * Función para llenar listbox para consultar los estudios médicos de un área.
 */

function consultaestudios(idarea){
    
    //Creación de objeto nuevo.
    var jsonconsulta = new Object();
    
    //Se obtiene el area de consulta y se inserta en un elemento del objeto.
    jsonconsulta.idarea = idarea;
    
    //Se invoca función para llenar realizar petición al servicio REST para obtener las áreas de estudio y llenar el listbox.
    RESTObtenerListBox( uriserv + "/getStudies", "consultaestudio", "estudio", "idestudio", jsonconsulta, "POST");
    
    //Se invoca función para llenar realizar petición al servicio REST para obtener las áreas de estudio y llenar el listbox.
    RESTObtenerListBox( uriserv + "/getSala", "consultasala", "sala", "sala", jsonconsulta, "POST");
    
}

/**
 * Función para limitar la fecha con el valor minimo.
 */

function asignamin(e){
    
    //Se asigna la fecha al elemento calendario para limitar el valor minimo.
    document.getElementById("fecha-max").min = e;
    
}

/**
 * Función para consultar las agendas de estudio.
 */

function consultaagenda(){
    
    //Creación de objeto nuevo
    var jsonconsulta = new Object();
    
    //Se obtiene el dato del formulario con id noregistro y se insertar en un elemento del objeto.
    jsonconsulta.idpaciente = document.getElementById("noregistro").value;
    jsonconsulta.nombrepaciente = document.getElementById("nombrepaciente").value;
    jsonconsulta.apellidopaternopaciente = document.getElementById("apellidopaternopaciente").value;
    jsonconsulta.apellidomaternopaciente = document.getElementById("apellidomaternopaciente").value;
    jsonconsulta.idestudio = document.getElementById("consultaestudio").value == "vacio" ? "": document.getElementById("consultaestudio").value;
    jsonconsulta.fechamin = document.getElementById("fecha-min").value;
    jsonconsulta.fechamax = document.getElementById("fecha-max").value;
    jsonconsulta.sala = document.getElementById("consultaarea").value == "vacio" ? "": document.getElementById("consultasala").value;
    
    //Validación rol del usuario.
    switch(PERFIL[0]["rol"]){
        case "Recepcionista":
            
            //Seteo id de area de usuario.
            jsonconsulta.idarea = PERFIL[0]["idarea"];
            
            //Seteo de estado de consulta.
            jsonconsulta.estado = "programadorepro";
            
        break;
        case "JefedelServicio":
            
            //Seteo id de area de usuario.
            jsonconsulta.idarea = PERFIL[0]["idarea"];
            
            //Seteo de estado de consulta.
            jsonconsulta.estado = document.getElementById("consultaestado").value;
        break;
        case "SubdirectordeServicios":
            
            //Seteo id de area de usuario.
            jsonconsulta.idarea = document.getElementById("consultaarea").value == "vacio" ? "": document.getElementById("consultaarea").value;
            
            //Seteo de estado de consulta.
            jsonconsulta.estado = document.getElementById("consultaestado").value;
            
        break;
        case "CoordinadordelServicio":
            
            //Seteo id de area de usuario.
            jsonconsulta.idarea = PERFIL[0]["idarea"];
            
            //Seteo de estado de consulta.
            jsonconsulta.estado = "programadorepro";
            
        break;
    }
    
    //Validación rol del usuario.
    if(PERFIL[0]["rol"] === 'SubdirectordeServicios'){
        
        //Invocar función para construir la tabla para mostrar agenda de citas de estudios.
        CreateTableFromREST(uriserv + "/getAppointment", jsonconsulta, "showDataAgendar", "tblagenda", ["Area","Estudio","Fecha","Paciente","Turno","Sala","Horario","Estado"], ["nombrearea","estudio","fechacita","paciente","turno","ubicacion","horacita","estado"],"1");
    }else if(PERFIL[0]["rol"] === 'CoordinadordelServicio'){
        //Invocar función para construir la tabla para mostrar agenda de citas de estudios.
        CreateTableFromREST(uriserv + "/getAppointment", jsonconsulta, "showDataAgendar", "tblagenda", ["Estudio","Fecha","Paciente","Turno","Sala","Horario","Estado"], ["estudio","fechacita","paciente","turno","ubicacion","horacita","estado"],"1");
    }else{
        //Invocar función para construir la tabla para mostrar agenda de citas de estudios.
        CreateTableFromREST(uriserv + "/getAppointment", jsonconsulta, "showDataAgendar", "tblagenda", ["Estudio","Fecha","Paciente","Turno","Sala","Horario","Estado"], ["estudio","fechacita","paciente","turno","ubicacion","horacita","estado"],"0");
    }
    
}

/**
 * Función para consultar las solicitudes de estudio.
 */

function consultasolicitud(){
    
    //Se activa el modal de solicitudes de estudios.
    document.querySelector(".modalSOLICITUDES").style.display = "block";
    
    //Creación de objeto nuevo
    var jsonsolicitud = new Object();
    
    //Se obtiene el dato del formulario con id noregistro y se insertar en un elemento del objeto.
    jsonsolicitud.idpaciente = document.getElementById("noregistro").value;
    jsonsolicitud.nombrepaciente = document.getElementById("nombrepaciente").value;
    jsonsolicitud.apellidopaternopaciente = document.getElementById("apellidopaternopaciente").value;
    jsonsolicitud.apellidomaternopaciente = document.getElementById("apellidomaternopaciente").value;
    jsonsolicitud.idestudio = document.getElementById("consultaestudio").value == "vacio" ? "": document.getElementById("consultaestudio").value;
    jsonsolicitud.fechamin = document.getElementById("fecha-min").value;
    jsonsolicitud.fechamax = document.getElementById("fecha-max").value;
    
    //Validación rol del usuario.
    switch(PERFIL[0]["rol"]){
        case "Recepcionista":
            
            //Seteo id de area de usuario.
            jsonsolicitud.idarea = PERFIL[0]["idarea"];
            
        break;
        case "JefedelServicio":
            
            //Seteo id de area de usuario.
            jsonsolicitud.idarea = PERFIL[0]["idarea"];
            
        break;
        case "SubdirectordeServicios":
            
            //Seteo id de area de usuario.
            jsonsolicitud.idarea = document.getElementById("consultaarea").value == "vacio" ? "": document.getElementById("consultaarea").value;
            
        break;
        case "CoordinadordelServicio":
            
            //Seteo id de area de usuario.
            jsonsolicitud.idarea = PERFIL[0]["idarea"];
            
        break;
    }
    
    //Invocar función para construir la tabla de consulta de estudios.
    CreateTableFromREST(uriserv + "/getStudyOrders", jsonsolicitud, "showDataSolicitudes", "tblsolicitudes", ["Fecha","Horario","Paciente","Estudio","Servicio","Medico","Estado"], ["fechasolicitud","horasolicitud","paciente","estudio","servicio","medico","estado"],"0");
    
}

/**
 * Función para setear fecha actual en elemento calendar de la vista.
 */

function seteofechaactual(identificador){
    
    //Se limpia el input de fecha
    document.getElementById(identificador).value = "";
    
    //Se crea objeto tipo Fecha, donde se obtiene la fecha actual.
    var fecha = new Date();
    
    //Se obtienen valores de año, mes y día.
    var mes = fecha.getMonth()+1;
    var dia = fecha.getDate();
    var ano = fecha.getFullYear();
    
    //Se da formato al día y mes en dos digitos.
    dia < 10 ? dia='0'+dia: dia;
    mes < 10 ? mes='0'+mes: mes;
    
    //Se asigna la fecha al elemento calendario para limitar el valor minimo.
    document.getElementById(identificador).min=ano+"-"+mes+"-"+dia;
    
}

/**
 * Función para consultar disponibilidad de horarios en una cita de estudio para agendar de forma manual.
 */

function manualasigncalendar(){
    
    //Validacion rol de usuario.
    if(PERFIL[0]["rol"] != "JefedelServicio"){
        
        //Validacion valores en formularios
        if(document.getElementById("consultaagendasala").value && document.getElementById("agendafecha").value){
            
            //Creación de objeto nuevo
            var horariomanual = new Object();
            
            //Se obtiene el dato del formulario con id noregistro y se insertar en un elemento del objeto.
            horariomanual.fechacita = document.getElementById("agendafecha").value;
            horariomanual.noserie = document.getElementById("consultaagendasala").value;
            horariomanual.idestudio = jsonhorariosautomatica.idestudio;
            horariomanual.fechaproxima = jsonhorariosautomatica.fechaproxima;
            
            //Se invoca función para llenar realizar petición al servicio REST para obtener la disponibilidad de horarios y llenar el listbox.
            RESTObtenerListBox( uriserv + "/getCalendarManual", "agendahora", "Hora", "Hora", horariomanual, "POST");
            
        }else{
            
            //Se limpia el listbox
            cleanlistbox("agendahora");
            
        }        
    }
    
}


/**
 * Función para obtener datos del Servicio REST de consulta de solicitudes de estudios de un paciente.
 */

function CreateTableFromREST(uri, jsondata, nameDivContainer, tablName, col, colsort, noedit){

    //Metodo AJAX para realizar peticiones al servicio REST.
    $.ajax({
        //Objetos configurables para realizar peticiones al servicio REST.
        url: uri, 
        type: 'POST',
        dataType:'json',
        data: jsondata
    }).done(function (data, textStatus, jqXHR){ //Respuesta de petición servicio REST exitosa.
        
        //Valia mensaje de salida de servicio rest 
        if(data[0]["salida"] != null){
        
            //Se ingresa el mensaje sin resultado al div
            alert(data[0]["salida"]);
            
        }else{
            
            //Se invoca función para llenar listbox de estudios con los datos regresados.
            UpdateTableFromJson(data, nameDivContainer, tablName, col, colsort, noedit);
            
        }
     
    }).fail(function (jqXHR, textStatus, errorThrown) {
        
        //Se habilita un mensaje de error en la pantalla.
        alert("No hay conexión con la BD: " + errorThrown);
        
    }).always(function (jqXHROrData, textStatus, jqXHROrErrorThrown) {
        //removerPreloader(servicio); 
    });  
}

/**
 * Función para actualizar los datos de una tabla.
 */

function UpdateTableFromJson(jsondata, nameDivContainer, tablName, col, colsort, noedit){
    
    //Validación elemento json respuesta  en caso de que no haya datos de busqueda.
    if(jsondata[0]["snresultados"] == "true" || jsondata.length == "0"){
        
        //Se limpia el div con el id de elemento nameDivContainer.
        divContainer = html_GetElement(nameDivContainer);
            
        //Se vacia el elemento con id nameDivContainer
        divContainer.innerHTML = "";
        
        //Se ingresa el mensaje sin resultado al div
        divContainer.appendChild(document.createTextNode("Sin resultados"));
        
    }else{ //Asignación de variable json de acuerdo a solicitudes o citas de estudio.
            
        //Invocar función para crear la estructura de tabla con encabezados.
        CreateTableFromJSON(nameDivContainer, tablName, col);
        
        //Invocar función para actualizar datos en la tabla.
        UpdateTableRowsSort(tablName, jsondata, colsort);
    
        //Valida el nombre de la tabla y setear los datos en variables globales
        switch(tablName){
            case "tblagenda":
                
                //Se setean los datos del json en una variable.
                jsonagenda = jsondata;
                
                //Se invoca función para dar formato a la tabla y agregar opciones para reagendar, solicitar o cancelar una cita.
                TableViewFormatAppointment(tablName, col.length, col.length + 1, noedit);
            
            break;
            case "tblsolicitudes":
                
                //Se setean los datos del json en una variable.
                jsonsolicitudes = jsondata;
                
                //Se invoca función para dar formato a la tabla y agregar opciones para reagendar, solicitar o cancelar una cita.
                TableViewFormatAppointment(tablName, col.length, "", noedit);
                
            break;
            case "tblhorarios":
                
                //Se setean los datos del json en una variable.            
                jsonopcauto = jsondata;
                
                //Se invoca función para dar formato a la tabla y agregar opciones para reagendar, solicitar o cancelar una cita.    
                TableViewFormatAppointment(tablName, col.length, "", noedit);
                
            break;
        }
            
    }

}

/**
 * Función para determinar el tipo de paciente Foraneo/Local
 */

function determina_tipo_paciente(tiempo){
    
    /**
     * De acuerdo al tiempo de traslado del paciente se determina si es Foraneo/Local.
     * tiempo >= 240 -> Foraneo
     * tiempo <= 240 -> Local
     */
    return (tiempo >= 240 ? "Foraneo": "Local");
    
}

/**
 * Función para agregar elementos a las columnas de la table de citas.
 */

function updateTableColumnsAppointment(tablename, column, newdata, columnsestado, estado) {
    
    //Definicion de variable tipo const, para guardar la referencia de la tabla.
    const tableReg = document.getElementById(tablename);
    
    //Ciclo for para iterar columnas
    for (var j = 1; j < tableReg.rows.length; j++) {
        
        //Valida el estado cancelado para no agregar elementos.
        if(tableReg.rows[j].cells[columnsestado].textContent != estado){
            
            //Guarda la referencia de una columna iterada.
            tmp = tableReg.rows[j].cells[column].innerHTML;
            
            //Agregar el nuevo elemento a la columna iterada.
            tableReg.rows[j].cells[column].innerHTML = tmp + " " + newdata;
            
        }
    }
}

/**
 * Función para insertar columna a una tabla.
 */

function insertColumnKAppointment(tablename, columnsIndexs, tituloCabecera, refcolumnas, columnsestado, estado) {
    
    //Definicion de variable tipo const, para guardar la referencia de la tabla.
    const tableReg = document.getElementById(tablename);
    
    //Ciclo for para iterar columnas
    for (var j = 0; j < tableReg.rows.length; j++) {
        
        //Se obtiene la referencia de una columna iterada.
        var firstRow = document.getElementById(tablename).rows[j];
        
        //Se crea una nueva celda
        var x = firstRow.insertCell(columnsIndexs);
        
        //Valida la primer iteraciòn para agregar el titulo en la cabecera
        if (j === 0) {
            
            //Asigna el titulo en la cabecera
            x.innerHTML = tituloCabecera;
            
        } else {
            //Valida si el estado no es cancelado y agrega la referencia de la columna,
            if(tableReg.rows[j].cells[columnsestado].textContent != estado)
                x.innerHTML = refcolumnas;
        }
    }
}


/**
 * Función para agregar la funcionalidad para activar el click en un elemento.
 */

function cellKnSelectionAppointment(tablename, funcion, columna, columnsestado, estado) {
    
    //Se obtiene la referencia de una tabla.
    var tbl = document.getElementById(tablename);
    
    //Valida que la tabla no este vacia.
    if (tbl !== null) {
        
        //Ciclo for para iterar columnas
        for (var i = 0; i < tbl.rows.length; i++) {
            //Valida si el estado no es cancelado y activa el click para un elemento.
            if(tbl.rows[i].cells[columnsestado].textContent != estado)
                tbl.rows[i].cells[columna].setAttribute('onclick', funcion);
        }
    }
}

/**
 * Función para dar formato a la tabla.
 */

function TableViewFormatAppointment(tablName, coledit, coldelete, noedit) {
    
    //Valida si no se insertan elementos a la tabla.
    if(noedit == "0"){
        
        //Valida nombre de la tabla
        switch(tablName){
            case "tblagenda":
                
                //Invoca funcion para agregar columnas y las cabeceras de una tabla.
                insertColumnKAppointment(tablName, coledit, "Reagendar", "Reagendar", 6, "Cancelado"); //inserta columna de esdicion (columna que no pertenece a los datos)
                
                //Invoca funcion para agregar el elemento a las columnas
                updateTableColumnsAppointment(tablName, coledit, "<a href='#'><i class='fa fa-calendar' style='font-size:24px; color:black'></i></a>", 6, "Cancelado");
                
                //Invoca funcion para agregar columnas y las cabeceras de una tabla.
                insertColumnKAppointment(tablName, coldelete, "Cancelar", "Cancelar", 6, "Cancelado"); //inserta columna de esdicion (columna que no pertenece a los datos)
                
                //Invoca funcion para agregar el elemento a las columnas
                updateTableColumnsAppointment(tablName, coldelete, "<a href='#'><i class='fa fa-times' style='font-size:24px; color:black'></i></a>", 6, "Cancelado");
            
                //Se invoca funcion para agregar referencia del evento
                tableRowColorCellSelectionAppointment(tablName, coledit, coldelete);
            
            break;
            case "tblsolicitudes":
                
                //Invoca funcion para agregar columnas y las cabeceras de una tabla.
                insertColumnK(tablName, coledit, "Agendar", "Agendar"); //inserta columna de esdicion (columna que no pertenece a los datos)
                
                //Invoca funcion para agregar el elemento a las columnas
                updateTableColumns(tablName, coledit, "<a href='#'><i class='fa fa-calendar' style='font-size:24px; color:black'></i></a>");
                
                //Se invoca funcion para agregar referencia del evento
                tableRowColorCellSelectionAppointment(tablName, coledit, coldelete);
            
            break;
            case "tblhorarios":
                
                //Invoca funcion para agregar columnas y las cabeceras de una tabla.
                insertColumnK(tablName, coledit, "", ""); //inserta columna de esdicion (columna que no pertenece a los datos)
                
                //updateTableColumns(tablName, coledit, "<a href='#'><i class='fa fa-check' style='font-size:24px; color:black'></i></a>");
                updateTableColumns(tablName, coledit, "<input name='citas-radio' id='citas-radio' type='radio' value='0'>");
                
                //Se invoca funcion para agregar referencia del evento
                tableRowColorCellSelectionAppointment(tablName, coledit, "");
                
            break;
        }
    }
    
    //Se invoca funcion para dar formato de color a una tabla
    backGroundColor(tablName, "rgba(88,142,255,.5)", "#000000", "#7F7F7F", "#FFFFFF");
        
    //puntero raton (b,c); pares (b,c); impares (b,c) 
    rowColor(tablName, "#00FFFF", "#000000", "#7F7F7F", "#FFFFFF", "#ffffff", "#000000");
    
}

/**
 * Función para agregar referencia del evento a un elemento
 */

function tableRowColorCellSelectionAppointment(tablName, coledit, coldelete) {

    //Se invoca funcion para dar formato de color a una tabla
    backGroundColor(tablName, "rgba(88,142,255,.5)", "#000000", "#7F7F7F", "#FFFFFF");
    
    //puntero raton (b,c); pares (b,c); impares (b,c) 
    rowColor(tablName, "#00FFFF", "#000000", "#7F7F7F", "#FFFFFF", "#ffffff", "#000000"); //puntero raton (b,c); pares (b,c); impares (b,c) 
    
    //Validacion columna edit diferente a cero
    if(coledit.length != 0 && tablName != "tblhorarios"){
        
        //Se invoca funcion para dar referencia al evento en un elemento
        cellKnSelectionAppointment(tablName, "getval(this,'" + tablName + "')", coledit, 6, "Cancelado"); //asignar funcion de seleccion para la celda kn de la tabla
    }else if(coledit.length != 0 && tablName == "tblhorarios"){
        
        //Se invoca funcion para dar referencia al evento en un elemento
        cellKnSelection(tablName, "getval(this,'" + tablName + "')", coledit);
    }
    
    //Validacion columna delete diferente a cero
    if(coldelete.length != 0)
        cellKnSelectionAppointment(tablName, "cancelarcita(this,'" + tablName + "')", coldelete, 6, "Cancelado"); //asignar funcion de seleccion para la celda kn de la tabla
    
}

function asignacion_manual(){
    
    document.querySelector("#showDataHorarios").style.display = "none";
    
    document.querySelector("#contenedor-manual").style.display = "block";
    document.querySelector("#manual_sala").style.display = "block";
    document.querySelector("#manual_fecha").style.display = "block";
    document.querySelector("#manual_hora").style.display = "block";
    
    
    //document.querySelector("#showDataHorarios").style.display = "none";

    consultaestudios(jsonhorariosautomatica.idestudio);
    
}

/**
 * Función para llenar ListBox de opciones estudios/salas
 */

function RESTObtenerListBox(uri, identificador, texto, valor, jsondata, metodo){
    
    //Metodo AJAX para realizar peticiones al servicio REST.
    $.ajax({
        //Objetos configurables para realizar peticiones al servicio REST.
        url: uri, 
        type: metodo,
        dataType:'json',
        data: jsondata
    }).done(function (data, textStatus, jqXHR){
        
        if(data[0]["salida"] != null){
            
            alert(data[0]["salida"]);
            
            cleanlistbox(identificador);
            
        }else{
            //Se invoca función para llenar listbox de estudios con los datos regresados.
            updatelistbox(identificador, texto, valor, data);
        }
        
    }).fail(function (jqXHR, textStatus, errorThrown) {
        
        //Se habilita un mensaje de error en la pantalla
        alert("No hay datos en bd " + errorThrown);
        
    }).always(function (jqXHROrData, textStatus, jqXHROrErrorThrown) {
        //removerPreloader(servicio); 
    });  
}

/**
 * Función para limpiar las opciones en un ListBox.
 */

function cleanlistbox(identificador){
    
    //Se vacia el listbox
    document.getElementById(identificador).options.length = 0;
    
    //Se crear un elemento tipo option
    var option = document.createElement("option");
    
    //El texto del primer option es vacio.
    option.text="";
    
    //El valor del primer option es vacio.
    option.value="";
    
    //Se agrega el elemento option
    document.getElementById(identificador).add(option);
    
}



/**
 * Función para llenar las opciones en un ListBox.
 */

function updatelistbox(identificador, texto, valor, jsonarray){
    
    //Se vacia el listbox
    document.getElementById(identificador).options.length = 0;
    
    //Se crear un elemento tipo option
    var option = document.createElement("option");
    
    //El texto del primer option es vacio.
    option.text="";
    
    //El valor del primer option es vacio.
    option.value="";
    
    //Se agrega el elemento option
    document.getElementById(identificador).add(option);
    
    //Ciclo para llenar las opciones del listbox.
    for(i=0;i<jsonarray.length;i++){
        
        //Se crear un elemento tipo option.
        var option = document.createElement("option");
        
        //Se agrega texto al option.
        option.text=jsonarray[i][texto];
        
        //Se agrega valir al option.
        option.value=jsonarray[i][valor];
        
        //Se agrega un nuevo elemento option al listbox.
        document.getElementById(identificador).add(option);
        
    }
    
}


