//http://localhost:8080/RESTSERVER/rest/USRSesionRST/login
var host = "http://" + location.host+"/RISSERVER/";
var FSM2;

function agregarPreloader(servicio) {
    $('#loader' + servicio).show();
    $('#fade' + servicio).show();
    //document.getElementById('loader'+servicio).style.display = 'block';
    //document.getElementById('fade'+servicio).style.display = 'block';
}

function removerPreloader(servicio) {
    $('#loader' + servicio).hide();
    $('#fade' + servicio).hide();
    //document.getElementById('loader'+servicio).style.display = 'none';
    //document.getElementById('fade'+servicio).style.display = 'none';
} 
//mover a clase de vista general (paquete vista)
function nobackbutton() {
    window.location.hash = "no-back-button";
    window.location.hash = "Again-No-back-button";
    window.onhashchange = function () {
        window.location.hash = "no-back-button";
    };
}

//mover a clase de servicios ajax (paquete controlador)
function getServicio(uriServ,tipohttp) {
    return $.ajax({
        url: uriServ,
        type: tipohttp,// 'get', // Tipo de envio 
        dataType: 'json' //Tipo de Respuesta

    }).fail(function (jqXHR, textStatus, errorThrown) {
        alert("No hay datos" + errorThrown);
    }).always(function (jqXHROrData, textStatus, jqXHROrErrorThrown) {
        //console.log(jqXHROrData);
    });
}  

//vista particular
function activamodal(str, form) {
    switch (str) {
        case "Cancelar":
            document.getElementById("formulario").reset(); //limpiar forma de login
            var dialogomodal = document.querySelector(".modal"+form.id);
            dialogomodal.style.display = "none";
            $.ajax({
                url: '/RISSERVER/rest/USRSesionRST/logout',
                type: 'GET', // Tipo de envio 
                dataType: 'json', //Tipo de Respuesta
                error: function (err) {
                    window.location = host + 'login.html';
                }
            });
            break;
         case "Ingresar": 
            var rolselected=getSelectedIndex(document.getElementById("perf2"));
            //console.log(rolselected);
            if(rolselected!=="0"){
                //Ejecutar carga de FSM de acuerdo al perfil y redireccionar pagina
                var llamadaFSM=getServicio("/RISSERVER/rest/RISFSM/fsm2/"+rolselected,"GET");
                $.when(llamadaFSM.done(function (ajaxFSMResults) {                                
                    console.log(ajaxFSMResults); 
                    FSM2= new  FSM(ajaxFSMResults);//creación de objeto FSMcon el json proveniente del back end
                    //edokparticular=FSM2.getFSMStateById("INGRESAR");
                    var edokparticular=FSM2.getFSMStateById(str.toUpperCase()); //en mayusculas: "nextState": "INGRESAR"
                    //concatenar: ""perfiles/"+rolselected+"/"+edokparticular.estado[0].vista
                    window.location = host +"vistas/perfiles/"+rolselected+"/"+edokparticular.estado[0].vista; //redireccionar a pagina de perfil particular
                }));                             
                var dialogomodal = document.querySelector(".modal"+form.id);
                dialogomodal.style.display = "none";                          
            }
            break;
    }    
}

//funcion para insertar opciones de selección en un listbox
function UpdateListBox(ref,jsonarray){
    var listbox = document.getElementById(ref);
    //var listbox = document.createElement(ref);
    listbox.options.length=1; //borra contenido del listbox
    for (var i = 0; i < jsonarray.length; i++) {
          var optionelem = document.createElement('option');
          optionelem.setAttribute('value', jsonarray[i]); //Definicion de valor para tabla dinamica
          optionelem.innerHTML=jsonarray[i];
          listbox.appendChild(optionelem);
    } 
}

//funcion para obtener la opción seleccionada de un listbox
function getSelectedIndex(refselid) {
    for (var i = 0; i < refselid.options.length; i++) {
        if (refselid.options[i].selected === true) {
            return refselid.options[i].value;
        }
    }
}

//metodo para controlar la opción de ingreso al sistema (boton Ingresar)
function logIn(estado,e){
    agregarPreloader("login");
    var mesnajeinicial=e.target.value;
        //console.log("valor evento: "+e.target.value);
        //edok=FSM2.getFSMStateById("login");
        var edok=FSM2.getFSMStateById(estado);
        //var mensajek=FSM2.getFSMStateMessageById(edok, "Ingresar");
        var mensajek=FSM2.getFSMStateMessageById(edok, mesnajeinicial);
        var restservice=mensajek.accion;
        //console.log(edok.estado[0].vista);
     //"Carlos", "abc123"
     //"MARCO", "123"
        var username = $('#uname').val();
        var password = $('#psw').val();
        ////////Bloque para integrar la funcionalidad de JSON Web Token//////////
        var rsaKeyPair = KEYUTIL.generateKeypair("RSA", 2048);// Se crean el par de llaves
        var privateKey = KEYUTIL.getPEM(rsaKeyPair.prvKeyObj,"PKCS8PRV");// Se obtiene la llave privada
        var publicKey = KEYUTIL.getPEM(rsaKeyPair.pubKeyObj);//Se obtiene la llave pública
        console.log(publicKey);
        var header ={ alg: "RS256", typ: "JWT"};// Se declara la cabecera del JWT 
        var base64 = btoa(username + ":" + password); // Se codifica en base 64 el usuario y la contraseña
        //var payload = {"usuario":username,"contrasena":password};
        var payload = {"base64":base64}; 
        var JSONToken = KJUR.jws.JWS.sign("RS256", JSON.stringify(header), JSON.stringify(payload), privateKey);// Se crea el JWT
        console.log(JSONToken);
        var informacionJSON = {"token":JSONToken, "llavepublica":publicKey};
        
        //var enviardatosbackend = {"usuario":username, "contrasena":password};
        //Función ajax para mandar el JWT y la llave privada al Backend
        $.ajax({
            url: '/RISSERVER/rest/USRSesionRST/servicioseguridad',  // URL del servicio
            type: 'POST',  // Método POST
            contentType: 'application/x-www-form-urlencoded', 
            dataType: 'text',  // Indicamos que esperamos recibir JSON
            data: informacionJSON,  // Convertimos el objeto a JSON
        beforeSend: function () {
            console.log('Enviando datos...');
        },
        success: function (response) {
            console.log('Respuesta del servidor:', response);
            
            $.ajax({
            url: '/RISSERVER/rest/USRSesionRST/login',
            type: 'GET', // Tipo de servicio 
            dataType: 'json', //Tipo de datos
            beforeSend: function () {
                agregarPreloader("login"); // ACTIVAR INDICADOR DE ESPERA
            }
            }).done(function (data, textStatus, jqXHR) {
                //Si en java se cifra, es necesario descifrarlo.
                var datosjson = data; // Se guarda la información en la variable datosjson
                var keypub = datosjson["llavepublica"];// Se obtiene la llave pública del objeto JSON
                console.log(datosjson["JWT"]);
                //console.log(datosjson["llavepublica"]);
                
                var isValid = KJUR.jws.JWS.verifyJWT(datosjson["JWT"], keypub, { alg: ['RS256'] });
                
                if (isValid) {
                  console.log('El token es válido.');
                  // Analizar el JWT para obtener los claims
                  var parsed = KJUR.jws.JWS.parse(datosjson["JWT"]);
                  var payload = parsed.payloadObj; // Se obtiene la carga útil del JWT
                  var subject = payload.sub;
                  var access_data = JSON.parse(subject);
                  console.log(access_data);
                  var usuario=access_data.Nombre+" "+access_data.Apaterno+" "+access_data.Amaterno; //nombre del usuario
                  var arregloPeril=JSON.parse(access_data.perfil); //arreglo de perfiles
                  //console.log(arregloPeril.length);
                  //alert(arregloPeril);
                  var fsmparticular=arregloPeril[0]; //seleccioando el elemento 0 del perfil OJO hacer un dialogo para preguntar perfil de ingreso
                  console.log(fsmparticular);
                  if(arregloPeril.length>1){
                    UpdateListBox("perf2",arregloPeril);
                    var dialogoMODAL = document.querySelector(".modalUSUARIOS");
                    dialogoMODAL.style.display = "block";//activar dialogo modal   
                    document.getElementById("usuarioactivo").innerHTML = "Bienvenido: "+usuario; //NOMBRE DEL USUARIO QUE DESEA INGRESAR
                  }else{
                    var llamadaFSM=getServicio("/RISSERVER/rest/RISFSM/fsm2/"+fsmparticular,"GET");
                    $.when(llamadaFSM.done(function (ajaxFSMResults) {
                        //console.log(ajaxFSMResults); 
                        //edokparticular=FSM2.getFSMStateById("INGRESAR");
                        FSM2= new  FSM(ajaxFSMResults);//creación de objeto FSM con el json proveniente del back end
                        var edokparticular=FSM2.getFSMStateById(mesnajeinicial.toUpperCase()); //en mayusculas: "nextState": "INGRESAR"
                        //concatenar: "perfiles/"+fsmparticular+"/"+edokparticular.estado[0].vista
                        window.location = host + "vistas/perfiles/"+fsmparticular+"/"+edokparticular.estado[0].vista; //redireccionar a pagina de perfil particular
                    }));                                 
                  }
                  
                } 
                /*
                var access_data=JSON.parse(data);
                console.log(access_data);
                var usuario=access_data.Nombre+" "+access_data.Apaterno+" "+access_data.Amaterno; //nombre del usuario
                var arregloPeril=JSON.parse(access_data.perfil); //arreglo de perfiles
                //console.log(arregloPeril.length);
                //alert(arregloPeril);
                var fsmparticular=arregloPeril[0]; //seleccioando el elemento 0 del perfil OJO hacer un dialogo para preguntar perfil de ingreso
                console.log(fsmparticular);  
                if(arregloPeril.length>1){
                    UpdateListBox("perf2",arregloPeril);
                    var dialogoMODAL = document.querySelector(".modalUSUARIOS");
                    dialogoMODAL.style.display = "block";//activar dialogo modal   
                    document.getElementById("usuarioactivo").innerHTML = "Bienvenido: "+usuario; //NOMBRE DEL USUARIO QUE DESEA INGRESAR
                }else{
                    var llamadaFSM=getServicio("/RISSERVER/rest/RISFSM/fsm2/"+fsmparticular,"GET");
                    $.when(llamadaFSM.done(function (ajaxFSMResults) {
                        //console.log(ajaxFSMResults); 
                        //edokparticular=FSM2.getFSMStateById("INGRESAR");
                        FSM2= new  FSM(ajaxFSMResults);//creación de objeto FSM con el json proveniente del back end
                        var edokparticular=FSM2.getFSMStateById(mesnajeinicial.toUpperCase()); //en mayusculas: "nextState": "INGRESAR"
                        //concatenar: "perfiles/"+fsmparticular+"/"+edokparticular.estado[0].vista
                        window.location = host + "vistas/perfiles/"+fsmparticular+"/"+edokparticular.estado[0].vista; //redireccionar a pagina de perfil particular
                    }));                                 
                }
                */
            }).fail(function (jqXHR, textStatus, errorThrown) {
                alert(textStatus+': Usuario invalido ');                        
            }).always(function (jqXHROrData, textStatus, jqXHROrErrorThrown) {
                removerPreloader("login"); // REMOVER INDICADOR DE ESPERA
                document.getElementById("formulario").reset(); //limpiar campos de la forma                            
            });
            
        },
        error: function (xhr, textStatus, errorThrown) {
            console.log('Error:', textStatus, errorThrown);
        }
        });
        
        
        //console.log(username);
        //console.log(password);
        
        /*
        $.ajax({
            url: '/RISSERVER/rest/USRSesionRST/login',
            type: 'GET', // Tipo de servicio 
            dataType: 'html', //Tipo de datos
            headers : {
              'Authorization': "Basic " + btoa(username + ":" + password)
              
              
            },
            beforeSend: function () {
                agregarPreloader("login"); // ACTIVAR INDICADOR DE ESPERA
            }
        }).done(function (data, textStatus, jqXHR) {
                //Si en java se cifra, es necesario descifrarlo.
                var access_data=JSON.parse(data);
                console.log(access_data);
                var usuario=access_data.Nombre+" "+access_data.Apaterno+" "+access_data.Amaterno; //nombre del usuario
                var arregloPeril=JSON.parse(access_data.perfil); //arreglo de perfiles
                //console.log(arregloPeril.length);
                //alert(arregloPeril);
                var fsmparticular=arregloPeril[0]; //seleccioando el elemento 0 del perfil OJO hacer un dialogo para preguntar perfil de ingreso
                console.log(fsmparticular);  
                if(arregloPeril.length>1){
                    UpdateListBox("perf2",arregloPeril);
                    var dialogoMODAL = document.querySelector(".modalUSUARIOS");
                    dialogoMODAL.style.display = "block";//activar dialogo modal   
                    document.getElementById("usuarioactivo").innerHTML = "Bienvenido: "+usuario; //NOMBRE DEL USUARIO QUE DESEA INGRESAR
                }else{
                    var llamadaFSM=getServicio("/RISSERVER/rest/RISFSM/fsm2/"+fsmparticular,"GET");
                    $.when(llamadaFSM.done(function (ajaxFSMResults) {
                        //console.log(ajaxFSMResults); 
                        //edokparticular=FSM2.getFSMStateById("INGRESAR");
                        FSM2= new  FSM(ajaxFSMResults);//creación de objeto FSM con el json proveniente del back end
                        var edokparticular=FSM2.getFSMStateById(mesnajeinicial.toUpperCase()); //en mayusculas: "nextState": "INGRESAR"
                        //concatenar: "perfiles/"+fsmparticular+"/"+edokparticular.estado[0].vista
                        window.location = host + "vistas/perfiles/"+fsmparticular+"/"+edokparticular.estado[0].vista; //redireccionar a pagina de perfil particular
                    }));                                 
                }
        }).fail(function (jqXHR, textStatus, errorThrown) {
                alert(textStatus+': Usuario invalido ');                        
        }).always(function (jqXHROrData, textStatus, jqXHROrErrorThrown) {
                removerPreloader("login"); // REMOVER INDICADOR DE ESPERA
                document.getElementById("formulario").reset(); //limpiar campos de la forma                            
        });  
        */
}

$(document).ready(function () {
    var llamadaFSM=getServicio("/RISSERVER/rest/RISFSM/fsm2/General","GET");// cargar FSM general..
    $.when(llamadaFSM.done(function (ajaxFSMResults) {
        FSM2= new  FSM(ajaxFSMResults);//creación de objeto FSM con el json proveniente del back-end
        //console.log(FSM2);
    })); 
});

